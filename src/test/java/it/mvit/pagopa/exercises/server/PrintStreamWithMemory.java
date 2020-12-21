package it.mvit.pagopa.exercises.server;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class PrintStreamWithMemory extends PrintStream {

    public PrintStreamWithMemory() throws UnsupportedEncodingException {
        super( new ByteArrayOutputStream(), true, StandardCharsets.UTF_8.name());
    }

    public String getMemory() {
        ByteArrayOutputStream memory = (ByteArrayOutputStream) this.out;
        return new String( memory.toByteArray() );
    }
}
