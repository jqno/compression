package nl.jqno.compression.algorithms;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

public class LzwTest {

    private Lzw sut = new Lzw();

    @Test
    void compress_happyPath() {
        var input = "ABBABBBABBA";
        var actual = sut.compress(input);
        var expected = List.of(65, 66, 66, 257, 258, 260, 65);
        assertEquals(expected, actual);
    }

    @Test
    void compress_edgeCase() {
        var input = "ABABABA";
        var actual = sut.compress(input);
        var expected = List.of(65, 66, 257, 259);
        assertEquals(expected, actual);
    }

    @Test
    void decompress_happyPath() {
        var input = List.of(65, 66, 66, 257, 258, 260, 65);
        var actual = sut.decompress(input);
        var expected = "ABBABBBABBA";
        assertEquals(expected, actual);
    }

    @Test
    void decompress_edgeCase() {
        var input = List.of(65, 66, 257, 259);
        var actual = sut.decompress(input);
        var expected = "ABABABA";
        assertEquals(expected, actual);
    }
}
