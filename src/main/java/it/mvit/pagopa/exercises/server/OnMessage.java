package it.mvit.pagopa.exercises.server;

@FunctionalInterface
public interface OnMessage {

    public void onMessage( ClientHandler conn, String message, Exception diedBy);
}
