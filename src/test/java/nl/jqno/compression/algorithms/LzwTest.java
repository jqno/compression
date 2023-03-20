package nl.jqno.compression.algorithms;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.List;
import nl.jqno.compression.streams.IntListInputCodeStream;
import nl.jqno.compression.streams.IntListOutputCodeStream;
import nl.jqno.compression.streams.StringInputSymbolStream;
import nl.jqno.compression.streams.StringOutputSymbolStream;
import org.junit.jupiter.api.Test;

public class LzwTest {

    private static final int EOF_CODE = 256;
    private static final int MAX_CODE = 0x1FF; // 511; represents 9 bits

    private Lzw sut = new Lzw(MAX_CODE);

    @Test
    void compress_happyPath() throws IOException {
        var in = new StringInputSymbolStream("ABBABBBABBA");
        var out = new IntListOutputCodeStream();
        var expected = List.of(65, 66, 66, 257, 258, 260, 65, EOF_CODE);

        sut.compress(in, out);

        assertEquals(expected, out.getCodes());
    }

    @Test
    void compress_edgeCase() throws IOException {
        var in = new StringInputSymbolStream("ABABABA");
        var out = new IntListOutputCodeStream();
        var expected = List.of(65, 66, 257, 259, EOF_CODE);

        sut.compress(in, out);

        assertEquals(expected, out.getCodes());
    }

    @Test
    void decompress_happyPath() throws IOException {
        var in = new IntListInputCodeStream(List.of(65, 66, 66, 257, 258, 260, 65, EOF_CODE));
        var out = new StringOutputSymbolStream();
        var expected = "ABBABBBABBA";

        sut.decompress(in, out);

        assertEquals(expected, out.getOutput());
    }

    @Test
    void decompress_edgeCase() throws IOException {
        var in = new IntListInputCodeStream(List.of(65, 66, 257, 259, EOF_CODE));
        var out = new StringOutputSymbolStream();
        var expected = "ABABABA";

        sut.decompress(in, out);

        assertEquals(expected, out.getOutput());
    }
}
