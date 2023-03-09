package nl.jqno.compression.algorithms;

import java.util.HashMap;

import nl.jqno.compression.streams.InputCodeStream;
import nl.jqno.compression.streams.OutputCodeStream;

public class Lzw {

    private static final int EOF_CODE = 256;

    public void compress(String input, OutputCodeStream out) {
        var map = new HashMap<String, Integer>();
        for (int i = 0; i < EOF_CODE; i++) {
            map.put(Character.toString(i), i);
        }

        var nextCode = EOF_CODE + 1;
        var current = "";
        for (int i = 0; i < input.length(); i++) {
            var candidate = current + input.charAt(i);
            if (map.containsKey(candidate)) {
                current = candidate;
            } else {
                map.put(candidate, nextCode);
                nextCode++;
                out.write(map.get(current));
                current = Character.toString(input.charAt(i));
            }
        }
        out.write(map.get(current));
    }

    public String decompress(InputCodeStream input) {
        var map = new HashMap<Integer, String>();
        for (int i = 0; i < EOF_CODE; i++) {
            map.put(i, Character.toString(i));
        }

        var result = new StringBuilder();
        var nextCode = EOF_CODE + 1;
        String previous = null;
        for (int i : input) {
            if (!map.containsKey(i)) {
               map.put(i, previous + previous.charAt(0));
            }
            result.append(map.get(i));
            if (previous != null) {
                map.put(nextCode, previous + map.get(i).charAt(0));
                nextCode++;
            }
            previous = map.get(i);
        }

        return result.toString();
    }
}
