package nl.jqno.compression.streams;

import java.io.IOException;
import java.io.InputStream;

public class StaticBitwiseInputCodeStream implements InputCodeStream {

    private final InputStream in;
    private final int codeSize;
    private int availableBits;
    private int pendingInput;

    public StaticBitwiseInputCodeStream(InputStream input, int maxCode) {
        this.in = input;
        this.availableBits = 0;
        this.pendingInput = 0;
        this.codeSize = (int) Math.ceil(Math.log(maxCode + 1) / Math.log(2));
    }

    @Override
    public int read() throws IOException {
        while (availableBits < codeSize) {
            int c = in.read();

            if (c == -1) {
                throw new IOException("No more data available");
            }

            pendingInput |= (c & 0xff) << availableBits;
            availableBits += 8;
        }

        int code = pendingInput & ~(~0 << codeSize);
        pendingInput >>= codeSize;
        availableBits -= codeSize;

        return code;
    }
    @Override
    public void close() throws IOException {
        if (in != null) {
            in.close();
        }
    }
}
