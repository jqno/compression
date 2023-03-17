package nl.jqno.compression.streams;

import java.util.ArrayList;
import java.util.List;

public class IntListInputCodeStream implements InputCodeStream {
    private final List<Integer> codes;

    public IntListInputCodeStream(List<Integer> codes) {
        this.codes = new ArrayList<>(codes);
    }

    @Override
    public int read() {
        if (codes.isEmpty()) {
            throw new IllegalStateException("List is empty");
        }
        return codes.remove(0);
    }
}
