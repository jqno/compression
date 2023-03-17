package nl.jqno.compression.streams;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class StaticBitwiseOutputCodeStreamTest {

    private static final int MAX_CODE = 0x1FF; // 511; represents 9 bits

    private ByteArrayOutputStream out;
    private StaticBitwiseOutputCodeStream sut;

    @BeforeEach
    void setUp() {
        out = new ByteArrayOutputStream();
        sut = new StaticBitwiseOutputCodeStream(out, MAX_CODE);
    }

    @AfterEach
    void tearDown() throws IOException {
        out.close();
    }

    @Test
    void singleCode() throws IOException {
        sut.write(42);
        sut.close();
        byte[] actual = out.toByteArray();
        byte[] expected = { 42, 0 }; // extra 0 for the 9th bit
        assertArrayEquals(expected, actual);
    }

    @Test
    void multipleCodes() throws IOException {
        int[] codes = {
            0b011110000, // 8 bit
            0b101010101, // 9 bit
            0b011001100, // 8 bit
            0b100000000 //  9 bit
        };
        byte[] expected = {
            (byte) 0b11110000, // 11110000
            (byte) 0b10101010, // 1010101 . 0
            (byte) 0b00110010, // 001100 . 10
            (byte) 0b00000011, // 00000 . 011
            (byte) 0b00001000 //  0000 . 1000
        };
        for (int code : codes) {
            sut.write(code);
        }
        sut.close();
        byte[] actual = out.toByteArray();
        assertArrayEquals(expected, actual);
    }

    @Test
    void invalidCode() {
        assertThrows(IllegalArgumentException.class, () -> sut.write(MAX_CODE + 1));
    }
}
