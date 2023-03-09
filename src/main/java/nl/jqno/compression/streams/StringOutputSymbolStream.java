package nl.jqno.compression.streams;

public class StringOutputSymbolStream implements OutputSymbolStream {
    private final StringBuilder builder = new StringBuilder();

    @Override
    public void write(String symbol) {
        builder.append(symbol);
    }

    public String getOutput() {
        return builder.toString();
    }
}
