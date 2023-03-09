package nl.jqno.compression.algorithms;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

import nl.jqno.compression.streams.IntListInputCodeStream;
import nl.jqno.compression.streams.IntListOutputCodeStream;

public class LzwTest {

    private Lzw sut = new Lzw();

    @Test
    void compress_happyPath() {
        var input = "ABBABBBABBA";
        var out = new IntListOutputCodeStream();
        sut.compress(input, out);
        var expected = List.of(65, 66, 66, 257, 258, 260, 65);
        assertEquals(expected, out.getCodes());
    }

    @Test
    void compress_edgeCase() {
        var input = "ABABABA";
        var out = new IntListOutputCodeStream();
        sut.compress(input, out);
        var expected = List.of(65, 66, 257, 259);
        assertEquals(expected, out.getCodes());
    }

    @Test
    void decompress_happyPath() {
        var input = List.of(65, 66, 66, 257, 258, 260, 65);
        var in = new IntListInputCodeStream(input);
        var actual = sut.decompress(in);
        var expected = "ABBABBBABBA";
        assertEquals(expected, actual);
    }

    @Test
    void decompress_edgeCase() {
        var input = List.of(65, 66, 257, 259);
        var in = new IntListInputCodeStream(input);
        var actual = sut.decompress(in);
        var expected = "ABABABA";
        assertEquals(expected, actual);
    }
}
