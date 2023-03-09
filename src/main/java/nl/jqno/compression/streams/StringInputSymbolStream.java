package nl.jqno.compression.streams;

import java.util.Iterator;

public class StringInputSymbolStream implements InputSymbolStream {
    private final String symbols;

    public StringInputSymbolStream(String symbols) {
        this.symbols = symbols;
    }

    @Override
    public Iterator<Character> iterator() {
        return new Iterator<Character>() {
            private int size = symbols.length();
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public Character next() {
                return symbols.charAt(index++);
            }
        };
    }
}
