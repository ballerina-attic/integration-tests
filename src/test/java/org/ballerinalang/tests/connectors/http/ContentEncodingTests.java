/*
*  Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing,
*  software distributed under the License is distributed on an
*  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*  KIND, either express or implied.  See the License for the
*  specific language governing permissions and limitations
*  under the License.
*/

package org.ballerinalang.tests.connectors.http;

import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.ballerinalang.client.HttpTestClient;
import org.ballerinalang.client.ResponseHolder;
import org.ballerinalang.client.compressor.GzipCompressor;
import org.ballerinalang.tests.base.BallerinaBaseTest;
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
public class ContentEncodingTests extends BallerinaBaseTest {

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

}
