package nl.jqno.compression.algorithms;

import java.util.HashMap;

import nl.jqno.compression.streams.InputCodeStream;
import nl.jqno.compression.streams.InputSymbolStream;
import nl.jqno.compression.streams.OutputCodeStream;
import nl.jqno.compression.streams.OutputSymbolStream;

public class Lzw {

    private static final int EOF_CODE = 256;

    public void compress(InputSymbolStream in, OutputCodeStream out) {
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
                map.put(candidate, nextCode);
                nextCode++;
                out.write(map.get(current));
                current = Character.toString(c);
            }
        }
        out.write(map.get(current));
    }

    public void decompress(InputCodeStream in, OutputSymbolStream out) {
        var map = new HashMap<Integer, String>();
        for (int i = 0; i < EOF_CODE; i++) {
            map.put(i, Character.toString(i));
        }

        var nextCode = EOF_CODE + 1;
        String previous = null;
        for (int i : in) {
            if (!map.containsKey(i)) {
               map.put(i, previous + previous.charAt(0));
            }
            out.write(map.get(i));
            if (previous != null) {
                map.put(nextCode, previous + map.get(i).charAt(0));
                nextCode++;
            }
            previous = map.get(i);
        }
    }
}
