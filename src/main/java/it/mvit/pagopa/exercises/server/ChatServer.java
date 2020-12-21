package it.mvit.pagopa.exercises.server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class ChatServer {

    public static final int LISTEN_PORT = 10000;
    private static final String STOP_MESSAGE = "STOP!!";

    public static void main(String[] args ) throws IOException {
        ChatServer server = new ChatServer(LISTEN_PORT, System.out);

        System.out.println("Starting server on port " + LISTEN_PORT + " ... ");
        server.start();
        System.out.println("... started!");
    }

    private final int listenPort;
    private final PrintStream logs;
    private final Set<ClientHandler> clientHandlers = new HashSet<>();
    private final AtomicBoolean mustRun = new AtomicBoolean();
    private Thread thread;

    public ChatServer( int listenPort, PrintStream out ) {
        this.listenPort = listenPort;
        this.logs = out;
    }

    public void start() throws IOException {
        ServerSocket listenSocket = new ServerSocket( this.listenPort );
        listenSocket.setSoTimeout( 1000 ); // - check mustRun every second

        this.thread = new Thread( () -> {
            while( mustRun.get() ) {
                try {
                    Socket clientConnection = listenSocket.accept();
                    startConnectionThread( clientConnection );
                }
                catch( SocketTimeoutException exc ) {
                    // - timeout is needed only to check for mustRun
                }
                catch( IOException exc ) {
                    exc.printStackTrace( logs );
                }
            }
        });

        mustRun.set( true );
        this.thread.start();
    }

    public void stop() {
        while( ! this.clientHandlers.isEmpty() ) {
            closeClient( this.clientHandlers.iterator().next() );
        }

        mustRun.set( false );
        this.thread.interrupt();
        logs.println("Server stopped!");
    }

    private void startConnectionThread(Socket clientConnection ) throws IOException {
        ClientHandler clientHandler = new ClientHandler( clientConnection );
        this.clientHandlers.add( clientHandler );
        this.logs.println( "Added a client! total " + this.clientHandlers.size());
        clientHandler.start( this::handleClientConnectionEvent );
    }

    private void handleClientConnectionEvent( ClientHandler ch, String msg, Exception e) {
        if( msg != null ) {
            this.broadcast(msg, ch);
            if( STOP_MESSAGE.equals( msg )) {
                this.stop();
            }
        }
        else {
            closeClient( ch );
        }
    }

    private synchronized void closeClient(ClientHandler ch) {
        if( this.clientHandlers.contains( ch )) {

            try {
                ch.stop();
            } catch (IOException exc) {
                exc.printStackTrace(logs);
            }
            this.clientHandlers.remove( ch );
            this.logs.println( "Removed a client! total " + this.clientHandlers.size());
        }
    }

    private void broadcast(String msg, ClientHandler ch) {
        this.clientHandlers.stream()
                .filter( el -> el != ch)
                .forEach( el -> el.write( msg ));
    }


}
