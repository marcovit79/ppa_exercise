package it.mvit.pagopa.exercises.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientHandler {
    private final Socket clientConnection;
    private final BufferedReader in;
    private final PrintWriter out;

    private final AtomicBoolean mustRun = new AtomicBoolean();
    private Thread thread = null;

    public ClientHandler(Socket clientConnection ) throws IOException {
        this.clientConnection = clientConnection;
        this.in = new BufferedReader( new InputStreamReader(clientConnection.getInputStream()));
        this.out = new PrintWriter( clientConnection.getOutputStream(), true);
    }

    public void start( OnMessage messageHandler) {
        if( messageHandler == null ) {
            throw new IllegalArgumentException("messageHandler can't be null");
        }
        mustRun.set( true );
        this.thread = new Thread( () -> {
            try {
                String line;
                while( mustRun.get() && (line = this.in.readLine()) != null ) {
                    messageHandler.onMessage( this, line, null);
                }
                messageHandler.onMessage( this, null, null);
            }
            catch( IOException exc ) {
                messageHandler.onMessage( this, null, exc);
            }
        });
        thread.start();
    }

    public void stop() throws IOException {
        mustRun.set( false );
        if( this.thread != null ) {
            this.thread.interrupt();
        }
        if( ! this.clientConnection.isClosed() ) {
            this.clientConnection.close();
        }
    }

    public synchronized void write( String message ) {
        this.out.println( message );
    }

}
