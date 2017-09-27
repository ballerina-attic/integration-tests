package org.ballerinalang.client.compressor;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * This class compresses/decompresses the input stream with GZIP.
 */
public class GzipCompressor {

    /**
     * Compress the input stream with GZIP.
     *
     * @param inputStream is the input
     * @return compressed stream
     * @throws IOException
     */
    public InputStream compress(final InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GZIPOutputStream gout = new GZIPOutputStream(baos);
        // Code to read from original uncompressed data and write to gout.
        IOUtils.copy(inputStream, gout);
        gout.finish();
        //Convert to InputStream.
        return new ByteArrayInputStream(baos.toByteArray());
    }

    /**
     * De-Compress the input stream from GZIP.
     *
     * @param inputStream is the input
     * @return decompress the stream
     * @throws IOException
     */
    public String decompress(final InputStream inputStream) throws IOException {
        OutputStream writer = new ByteArrayOutputStream();
        GZIPInputStream gzipStream = new GZIPInputStream(inputStream);
        IOUtils.copy(gzipStream, writer);
        return IOUtils.toString(new ByteArrayInputStream(((ByteArrayOutputStream) writer).toByteArray()), "UTF-8");
    }
}
