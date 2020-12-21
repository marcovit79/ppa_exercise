package it.mvit.pagopa.exercises.server;

import org.junit.jupiter.api.Test;

import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;

public class ChatServerTest {

    private ChatServer toBeTested;

    @Test
    public void testTwoClient() throws Exception {
        int port = randomPort();
        PrintStreamWithMemory log = new PrintStreamWithMemory();
        ChatServer srv = buildServer( port, log );
        srv.start();

        TestClient c1 = new TestClient( port, "localhost");
        c1.connect();

        TestClient c2 = new TestClient( port, "localhost");
        c2.connect();

        Thread.sleep(100);
        c1.send("Hello from c1");
        c2.send("Hello from c2");
        Thread.sleep(100);

        assertThat( c1.getReceived() ).isEqualTo( "Hello from c2\n");
        assertThat( c2.getReceived() ).isEqualTo( "Hello from c1\n");
        c1.disconnect();
        c2.send("STOP!!");
        Thread.sleep(100);

        assertThat( log.getMemory() ).isEqualTo(
                "Added a client! total 1\n" +
                "Added a client! total 2\n" +
                "Removed a client! total 1\n" +
                "Removed a client! total 0\n" +
                "Server stopped!\n"
            );
    }

    private int randomPort() {
        return 2000 + (int) (100 * Math.random());
    }

    private ChatServer buildServer(int port, PrintStream out) {
        return new ChatServer( port, out );
    }

}
