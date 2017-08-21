package org.ballerinalang.tests.connectors.http;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.ballerinalang.tests.base.BallerinaBaseTest;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.Assert.assertEquals;

/**
 * Tests HTTP Methods and related scenarios
 * Uses HTTPMethodService.bal service
 */
public class HTTPMethodsTests extends BallerinaBaseTest {

    HttpClient client;

    HTTPMethodsTests() {
        client = new HttpClient();
        client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
        client.getHttpConnectionManager().getParams().setSoTimeout(7000);
    }

    @Test(description = "Test all methods in one server resource") public void testAllVerbsInOneResource()
            throws IOException {

        String serviceURL = ballerinaURL + "/httpmethods/all";
        //String assertResponse = "{\"POST Method:Request Status\":\"Sucess\"}";
        GetMethod get = new GetMethod(serviceURL);
        int statuscode = client.executeMethod(get);
        assertEquals(statuscode, HttpStatus.SC_OK);

        PostMethod post = new PostMethod(serviceURL);
        statuscode = client.executeMethod(post);
        assertEquals(statuscode, HttpStatus.SC_OK);

        PutMethod put = new PutMethod(serviceURL);
        statuscode = client.executeMethod(put);
        assertEquals(statuscode, HttpStatus.SC_OK);

        DeleteMethod delete = new DeleteMethod(serviceURL);
        statuscode = client.executeMethod(delete);
        assertEquals(statuscode, HttpStatus.SC_OK);

        HeadMethod head = new HeadMethod(serviceURL);
        statuscode = client.executeMethod(head);
        assertEquals(statuscode, HttpStatus.SC_OK);
    }

    @Test(description = "Doing a GET call and calling the BE with a GET using execute action")
    public void testGetResource()
            throws IOException {
        String serviceURL = ballerinaURL + "/httpmethods/get";
        GetMethod get = new GetMethod(serviceURL);
        int statuscode = client.executeMethod(get);
        assertEquals(statuscode, HttpStatus.SC_OK);
    }

    @Test(description = "Doing a GET call and calling the BE with a GET using the get action")
    public void testGetResourceWithSpecificMethod() throws IOException {
        String serviceURL = ballerinaURL + "/httpmethods/doget";
        GetMethod get = new GetMethod(serviceURL);
        int statuscode = client.executeMethod(get);
        assertEquals(statuscode, HttpStatus.SC_OK);
    }

    @Test(description = "") public void testPostResource() throws IOException {

        String serviceURL = ballerinaURL + "/httpmethods/post";
        PostMethod post = new PostMethod(serviceURL);
        int statuscode = client.executeMethod(post);
        assertEquals(statuscode, HttpStatus.SC_OK);

    }

    @Test(description = "") public void testPostResourceWithSpecificMethod() throws IOException {

        String serviceURL = ballerinaURL + "/httpmethods/dopost";
        PostMethod post = new PostMethod(serviceURL);
        int statuscode = client.executeMethod(post);
        assertEquals(statuscode, HttpStatus.SC_OK);

    }

    @Test(description = "") public void testPutResource() throws IOException {

        String serviceURL = ballerinaURL + "/httpmethods/put";
        PutMethod put = new PutMethod(serviceURL);
        int statuscode = client.executeMethod(put);
        assertEquals(statuscode, HttpStatus.SC_OK);

    }

    @Test(description = "") public void testPutResourceWithSpecificMethod() throws IOException {

        String serviceURL = ballerinaURL + "/httpmethods/doput";
        PutMethod put = new PutMethod(serviceURL);
        int statuscode = client.executeMethod(put);
        assertEquals(statuscode, HttpStatus.SC_OK);

    }

    @Test(description = "") public void testHeadResource() throws IOException {

        String serviceURL = ballerinaURL + "/httpmethods/head";
        HeadMethod delete = new HeadMethod(serviceURL);
        int statuscode = client.executeMethod(delete);
        assertEquals(statuscode, HttpStatus.SC_OK);

    }

    @Test public void testMethodSwitchingFromGetToPostWithExecute() throws IOException {

        String serviceURL = ballerinaURL + "/httpmethods/gettopost";
        String assertResponse = "{\"POST Method:Request Status\":\"Sucess\"}";
        GetMethod get = new GetMethod(serviceURL);
        int statuscode = client.executeMethod(get);

        // Asserting the Status code. Expected 200 OK
        assertEquals(String.valueOf(statuscode), "200");
        byte[] response = get.getResponseBody();
        // Asserting the Response Message.
        assertEquals(new String(response), assertResponse);
    }

    @Test public void testMethodSwitchingFromPutToPostWithExecute() throws IOException {
        String serviceURL = ballerinaURL + "/httpmethods/puttopost";
        String assertResponse = "{\"POST Method:Request Status\":\"Sucess\"}";
        PutMethod put = new PutMethod(serviceURL);
        int statuscode = client.executeMethod(put);

        // Asserting the Status code. Expected 200 OK
        assertEquals(String.valueOf(statuscode), "200");
        byte[] response = put.getResponseBody();
        // Asserting the Response Message.
        assertEquals(new String(response), assertResponse);

    }

    @Test public void testMethodSwitchingFromPostToPutWithExecute() throws IOException {
        String serviceURL = ballerinaURL + "/httpmethods/posttoput";
        String assertResponse = "{\"PUT Method:Request Status\":\"Sucess\"}";
        PostMethod post = new PostMethod(serviceURL);
        int statuscode = client.executeMethod(post);

        // Asserting the Status code. Expected 200 OK
        assertEquals(String.valueOf(statuscode), "200");
        byte[] response = post.getResponseBody();
        // Asserting the Response Message.
        assertEquals(new String(response), assertResponse);
    }
}

