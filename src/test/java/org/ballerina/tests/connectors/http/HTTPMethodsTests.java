package org.ballerina.tests.connectors.http;

import net.sf.saxon.functions.Put;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.Assert.assertEquals;

/**
 * Tests HTTP Methods and related scenarios
 * Uses HTTPMethodService.bal service
 */
public class HTTPMethodsTests {

    HttpClient client;
    String ballerinaURL = "http://localhost:9090";

    HTTPMethodsTests() {
        client = new HttpClient();
        client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
        client.getHttpConnectionManager().getParams().setSoTimeout(7000);
    }

    @Test (description = "Test all methods in one server resource") public void testAllVerbsInOneResource(){

    }

    @Test (description = "") public void testGetResource(){

    }

    @Test (description = "") public void testPostResource(){

    }

    @Test (description = "") public void testPutResource(){

    }

    @Test (description = "") public void testHeadResource(){

    }

    @Test public void testMethodSwitchingFromGetToPostWithExecute() throws IOException {

        String serviceURL = ballerinaURL + "/httpmethods/gettopost1";
        String assertResponse = "{\n" + "    \" POST Method : Request Status\": \"Sucess\"\n" + "}";
        GetMethod get = new GetMethod(serviceURL);
        int statuscode = client.executeMethod(get);

        // Asserting the Status code. Expected 200 OK
        assertEquals(String.valueOf(statuscode), "200");
        byte[] response = get.getResponseBody();
        // Asserting the Response Message.
        assertEquals(new String(response), assertResponse);
    }

    @Test public void testMethodSwitchingFromPutToPostWithExecute() throws IOException {
        String serviceURL = ballerinaURL + "/puttopost";
        String assertResponse = "{\n" + "\"PUT Method : Request Status\": \"Sucess\"\n" + "}";
        PutMethod put = new PutMethod(serviceURL);
        int statuscode = client.executeMethod(put);

        // Asserting the Status code. Expected 200 OK
        assertEquals(String.valueOf(statuscode), "200");
        byte[] response = put.getResponseBody();
        // Asserting the Response Message.
        assertEquals(new String(response), assertResponse);

    }

    @Test public void testMethodSwitchingFromPostToPutWithExecute() throws IOException {
        String serviceURL = ballerinaURL + "/posttoput";
        String assertResponse = "{\n" + "\"PUT Method : Request Status\": \"Sucess\"\n" + "}";
        PostMethod post = new PostMethod(serviceURL);
        int statuscode = client.executeMethod(post);

        // Asserting the Status code. Expected 200 OK
        assertEquals(String.valueOf(statuscode), "200");
        byte[] response = post.getResponseBody();
        // Asserting the Response Message.
        assertEquals(new String(response), assertResponse);
    }
}
