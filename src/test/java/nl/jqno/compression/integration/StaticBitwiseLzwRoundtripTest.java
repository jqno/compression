package nl.jqno.compression.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import nl.jqno.compression.algorithms.Lzw;
import nl.jqno.compression.streams.StaticBitwiseInputCodeStream;
import nl.jqno.compression.streams.StaticBitwiseOutputCodeStream;
import nl.jqno.compression.streams.StringInputSymbolStream;
import nl.jqno.compression.streams.StringOutputSymbolStream;

public class StaticBitwiseLzwRoundtripTest {

    private static final int MAX_CODE = 0x1FF; // 511; represents 9 bits

    private Lzw sut = new Lzw();

    @Test
    void roundtrip_happyPath() throws IOException {
        assertRoundtrip("ABBABBBABBA");
    }

    private void assertRoundtrip(String symbols) throws IOException {
        var backingCompressOut = new ByteArrayOutputStream();
        var compressIn = new StringInputSymbolStream(symbols);
        var compressOut = new StaticBitwiseOutputCodeStream(backingCompressOut, MAX_CODE);

        sut.compress(compressIn, compressOut);

        byte[] compressed = backingCompressOut.toByteArray();

        var backingDecompressIn = new ByteArrayInputStream(compressed);
        var decompressIn = new StaticBitwiseInputCodeStream(backingDecompressIn, MAX_CODE);
        var decompressOut = new StringOutputSymbolStream();

        sut.decompress(decompressIn, decompressOut);

        var actual = decompressOut.getOutput();
        assertTrue(compressed.length < symbols.length());
        assertEquals(symbols, actual);
    }
}
