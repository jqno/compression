package nl.jqno.compression.streams;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IntListOutputCodeStream implements OutputCodeStream {
    private final List<Integer> codes = new ArrayList<>();

    @Override
    public void write(int code) {
        codes.add(code);
    }

    @Override
    public void close() {}

    public List<Integer> getCodes() {
        return Collections.unmodifiableList(codes);
    }
}
