package it.mvit.pagopa.exercises.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class TestClient {

    private int port;
    private String host;
    private StringBuffer received = new StringBuffer();
    private Thread t;
    private Socket socket;
    private PrintStream out;

    public TestClient( int port, String host) {
        this.port = port;
        this.host = host;
    }

    public void connect() throws IOException {
        socket = new Socket(host, port);
        out = new PrintStream( socket.getOutputStream(), true );
        BufferedReader reader = new BufferedReader( new InputStreamReader(
                socket.getInputStream() ));

        this.t = new Thread( () -> {
            try {
                String line;
                while( (line = reader.readLine()) != null) {
                    this.received.append( line ).append('\n');
                }
            }
            catch( IOException exc ) {
                // exc.printStackTrace();
            }
        });
        t.start();
    }

    public void disconnect() throws IOException {
        t.interrupt();
        socket.close();
    }

    public void send(String msg ) {
        out.println( msg );
    }

    public String getReceived() {
        return this.received.toString();
    }
}
