package org.ballerinalang.tests.connectors.http;

import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.ballerinalang.client.HttpTestClient;
import org.ballerinalang.client.ResponseHolder;
import org.ballerinalang.client.compressor.GzipCompressor;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Test class for testing transfer and content encoding
 * Uses EncodingService.bal
 */
public class ContentEncodingTests {

    String ballerinaURL = "http://localhost:8899";

    /**
     * Tests small chunked message for a Post request
     */
    @Test public void chunkedMessageForPostWithSmallPayload() throws IOException {

        HttpTestClient request = new HttpTestClient(ballerinaURL);

        String payload = "This is the payload";
        request.setChunked(true);
        request.setContentType("text/plain");
        ResponseHolder response = request.postRequest("/encoded/spayload", payload);

        assertEquals(response.getResponseMessage(), payload);
        assertEquals(response.getResponseCode(), 200);

    }

    /**
     * Tests a large chunked message for a Post request
     */
    @Test public void chunkedMessageForPostWithLargePayload() throws IOException {

        HttpTestClient request = new HttpTestClient(ballerinaURL);

        File file = new File("src/test/java/resources/content.txt");
        FileInputStream inputStream = new FileInputStream(file);
        InputStreamRequestEntity streamRequestEntity = new InputStreamRequestEntity(inputStream, "text/plain");
        //String payload = "This is the payload";
        request.setChunked(true);
        ResponseHolder response = request.postRequest("/encoded/spayload", inputStream);

        assertNotNull(response.getResponseMessage());
        assertEquals(response.getResponseCode(), 200);

    }

    @Test public void chunkedGzipEncodedMessageForPost() throws IOException {

        HttpTestClient request = new HttpTestClient(ballerinaURL);

        String payload = "This is the payload";
        request.setChunked(true);
        request.setContentEncoded(true);
        request.setContentType("text/plain");
        ResponseHolder response = request.postRequest("/encoded/spayload", payload);

        assertEquals(response.getResponseMessage(), payload);
        assertEquals(response.getResponseCode(), 200);

    }

    @Test public void chunkedGzipEncodedMessageWithAcceptEncodingForPost() throws IOException {

        HttpTestClient request = new HttpTestClient(ballerinaURL);

        String payload = "This is the payloadXXXXXXXXXXXXXXXXXX";
        request.setChunked(false);
        request.setContentEncoded(true);
        request.setHeader("Accept-Encoding", "gzip");
        request.setContentType("text/plain");
        ResponseHolder response = request.postRequest("/encoded/spayload", payload);

        GzipCompressor compressor = new GzipCompressor();
        String decompressedMessage = compressor.decompress(new ByteArrayInputStream(response.getResponseAsByteArray()));
        assertEquals(decompressedMessage, payload);
        assertEquals(response.getResponseCode(), 200);

    }

    //    @Test public void chunkedMessageForPut() throws Exception {
    //
    //        HttpRequest request = new HttpRequest(ballerinaURL);
    //
    //        String payload = "Payload for Put Message";
    //        request.setChunked(true);
    //
    //        ResponseHolder responseHolder = request.putRequest("/encoded/spayload", payload);
    //
    //        assertEquals(payload, responseHolder.getResponseMessage());
    //        assertEquals(responseHolder.getResponseCode(), 200);
    //
    //    }
}
