package nl.jqno.compression.streams;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class StaticBitwiseInputCodeStreamTest {

    private static final int MAX_CODE = 0x1FF; // 511; represents 9 bits

    private ByteArrayInputStream in;
    private StaticBitwiseInputCodeStream sut;

    @AfterEach
    void tearDown() throws IOException {
        if (in != null) {
            in.close();
        }
    }

    @Test
    void singleCode() throws IOException {
        byte[] inputData = { 42, 0 }; // extra 0 for the 9th bit
        createSut(inputData);

        assertEquals(42, sut.read());
        assertThrows(IOException.class, () -> sut.read());
    }

    @Test
    void multipleCodes() throws IOException {
        byte[] inputData = {
            (byte) 0b11110000, // 11110000
            (byte) 0b10101010, // 1010101 . 0
            (byte) 0b00110010, // 001100 . 10
            (byte) 0b00000011, // 00000 . 011
            (byte) 0b00001000 //  0000 . 1000
        };
        var expected = List.of(
            0b011110000, // 8 bit
            0b101010101, // 9 bit
            0b011001100, // 8 bit
            0b100000000 //  9 bit
        );
        createSut(inputData);

        var actual = new ArrayList<Integer>();
        for (int i = 0; i < expected.size(); i++) {
            actual.add(sut.read());
        }

        assertEquals(expected, actual);
    }

    @Test
    void readWithNoMoreData() throws IOException {
        byte[] inputData = {};
        createSut(inputData);

        assertThrows(IOException.class, () -> sut.read());
    }

    private void createSut(byte[] inputData) {
        in = new ByteArrayInputStream(inputData);
        sut = new StaticBitwiseInputCodeStream(in, MAX_CODE);
    }
}
