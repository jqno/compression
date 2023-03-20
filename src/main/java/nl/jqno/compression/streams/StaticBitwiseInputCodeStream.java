package nl.jqno.compression.streams;

import java.io.IOException;
import java.io.InputStream;

public class StaticBitwiseInputCodeStream implements InputCodeStream {

    private final InputStream input;
    private final int codeSize;
    private int availableBits;
    private int pendingInput;

    public StaticBitwiseInputCodeStream(InputStream input, int maxCode) {
        this.input = input;
        this.availableBits = 0;
        this.pendingInput = 0;
        this.codeSize = (int) Math.ceil(Math.log(maxCode + 1) / Math.log(2));
    }

    @Override
    public int read() throws IOException {
        if (!hasNext()) {
            throw new IOException("No more data available.");
        }

        int i = pendingInput & ~(~0 << codeSize);
        pendingInput >>= codeSize;
        availableBits -= codeSize;

        return i;
    }

    public boolean hasNext() throws IOException {
        while (availableBits < codeSize) {
            int c = input.read();

            if (c == -1) {
                return false;
            }

            pendingInput |= (c & 0xff) << availableBits;
            availableBits += 8;
        }

        return true;
    }

    @Override
    public void close() throws IOException {
        if (input != null) {
            input.close();
        }
    }
}
