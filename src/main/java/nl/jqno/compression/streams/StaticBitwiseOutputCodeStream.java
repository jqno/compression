package nl.jqno.compression.streams;

import java.io.IOException;
import java.io.OutputStream;

public class StaticBitwiseOutputCodeStream implements OutputCodeStream {

    private final OutputStream out;
    private final int codeSize;
    private int pendingOutput;
    private int pendingBits;

    public StaticBitwiseOutputCodeStream(OutputStream out, int maxCode) {
        this.out = out;
        this.codeSize = (int) Math.ceil(Math.log(maxCode + 1) / Math.log(2));
        this.pendingOutput = 0;
        this.pendingBits = 0;
    }

    @Override
    public void write(int code) throws IOException {
        if (code >= (1 << codeSize)) {
            throw new IllegalArgumentException("code must be less than 2^codeSize");
        }

        pendingOutput |= code << pendingBits;
        pendingBits += codeSize;

        flush(8);
    }

    private void flush(int val) throws IOException {
        while (pendingBits >= val) {
            out.write(pendingOutput & 0xFF);
            pendingOutput >>>= 8;
            pendingBits -= 8;
        }
    }

    @Override
    public void close() throws IOException {
        flush(0);
    }
}
