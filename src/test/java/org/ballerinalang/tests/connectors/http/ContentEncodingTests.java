package org.ballerinalang.tests.connectors.http;

import org.ballerinalang.client.HttpRequest;
import org.ballerinalang.client.ResponseHolder;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * Test class for testing transfer and content encoding
 */
public class ContentEncodingTests {

    String ballerinaURL = "http://localhost:9090";

    @Test public void ChunkedMessageStreamForPost() throws IOException {

        HttpRequest request = new HttpRequest(ballerinaURL);

        String payload = "This is the payload";
        request.setChunked(true);
        request.setContentEncoded(true);
        request.setContentType("text/plain");
        ResponseHolder response = request.postRequest("/echo", payload);

    }
}
