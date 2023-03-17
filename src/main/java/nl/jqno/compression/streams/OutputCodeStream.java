package nl.jqno.compression.streams;

import java.io.IOException;

public interface OutputCodeStream extends AutoCloseable {
    void write(int code) throws IOException;
}

