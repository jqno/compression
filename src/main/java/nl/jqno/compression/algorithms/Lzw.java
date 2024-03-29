package nl.jqno.compression.algorithms;

import java.io.IOException;
import java.util.HashMap;

import nl.jqno.compression.streams.InputCodeStream;
import nl.jqno.compression.streams.InputSymbolStream;
import nl.jqno.compression.streams.OutputCodeStream;
import nl.jqno.compression.streams.OutputSymbolStream;

public class Lzw {

    private static final int EOF_CODE = 256;

    private final int maxCode;

    public Lzw(int maxCode) {
        this.maxCode = maxCode;
    }

    public void compress(InputSymbolStream in, OutputCodeStream out) throws IOException {
        var map = new HashMap<String, Integer>();
        for (int i = 0; i < EOF_CODE; i++) {
            map.put(Character.toString(i), i);
        }

        var nextCode = EOF_CODE + 1;
        var current = "";
        for (char c : in) {
            var candidate = current + c;
            if (map.containsKey(candidate)) {
                current = candidate;
            } else {
                if (nextCode <= maxCode) {
                    map.put(candidate, nextCode);
                    nextCode++;
                }
                out.write(map.get(current));
                current = Character.toString(c);
            }
        }
        out.write(map.get(current));
        out.write(EOF_CODE);
    }

    public void decompress(InputCodeStream in, OutputSymbolStream out) throws IOException {
        var map = new HashMap<Integer, String>();
        for (int i = 0; i < EOF_CODE; i++) {
            map.put(i, Character.toString(i));
        }

        var nextCode = EOF_CODE + 1;
        String previous = null;
        int i = in.read();
        while (i != EOF_CODE) {
            if (!map.containsKey(i)) {
               map.put(i, previous + previous.charAt(0));
            }
            out.write(map.get(i));
            if (previous != null && nextCode <= maxCode) {
                map.put(nextCode, previous + map.get(i).charAt(0));
                nextCode++;
            }
            previous = map.get(i);
            i = in.read();
        }
    }
}
