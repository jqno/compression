package nl.jqno.compression.streams;

public interface OutputSymbolStream extends AutoCloseable {
    void write(String symbol);
}
