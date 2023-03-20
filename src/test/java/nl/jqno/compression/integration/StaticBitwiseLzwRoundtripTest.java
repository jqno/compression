package nl.jqno.compression.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import nl.jqno.compression.algorithms.Lzw;
import nl.jqno.compression.streams.StaticBitwiseInputCodeStream;
import nl.jqno.compression.streams.StaticBitwiseOutputCodeStream;
import nl.jqno.compression.streams.StringInputSymbolStream;
import nl.jqno.compression.streams.StringOutputSymbolStream;
import org.junit.jupiter.api.Test;

public class StaticBitwiseLzwRoundtripTest {

    private static final int MAX_CODE = 0x1FF; // 511; represents 9 bits

    private Lzw sut = new Lzw();

    @Test
    void roundtrip_happyPath() throws IOException {
        assertRoundtrip("ABBABBBABBA");
    }

    @Test
    void roundtrip_edgeCase() throws IOException {
        assertRoundtrip("ABABABA");
    }

    @Test
    void roundtrip_noCompression() throws IOException {
        assertRoundtrip("The quick brown fox jumps over the lazy dog");
    }

    private void assertRoundtrip(String symbols) throws IOException {
        byte[] compressed = compress(symbols);
        var actual = decompress(compressed);

        assertEquals(symbols, actual);
    }

    private byte[] compress(String symbols) throws IOException {
        var backingCompressOut = new ByteArrayOutputStream();
        try (
            var compressIn = new StringInputSymbolStream(symbols);
            var compressOut = new StaticBitwiseOutputCodeStream(backingCompressOut, MAX_CODE)
        ) {
            sut.compress(compressIn, compressOut);
        }

        return backingCompressOut.toByteArray();
    }

    private String decompress(byte[] compressed) throws IOException {
        var backingDecompressIn = new ByteArrayInputStream(compressed);
        try (
            var decompressOut = new StringOutputSymbolStream();
            var decompressIn = new StaticBitwiseInputCodeStream(backingDecompressIn, MAX_CODE)
        ) {
            sut.decompress(decompressIn, decompressOut);
            return decompressOut.getOutput();
        }
    }
}
