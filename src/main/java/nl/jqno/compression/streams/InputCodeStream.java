package nl.jqno.compression.streams;

import java.io.IOException;

public interface InputCodeStream {
    int read() throws IOException;
}
