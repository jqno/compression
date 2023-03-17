package nl.jqno.compression.streams;

import java.io.IOException;

public interface InputCodeStream extends AutoCloseable {
    int read() throws IOException;
}
