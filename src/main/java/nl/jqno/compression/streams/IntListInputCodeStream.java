package nl.jqno.compression.streams;

import java.util.Iterator;
import java.util.List;

public class IntListInputCodeStream implements InputCodeStream {
    private final List<Integer> codes;

    public IntListInputCodeStream(List<Integer> codes) {
        this.codes = codes;
    }

    @Override
    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {
            private int size = codes.size();
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public Integer next() {
                return codes.get(index++);
            }
        };
    }
}
