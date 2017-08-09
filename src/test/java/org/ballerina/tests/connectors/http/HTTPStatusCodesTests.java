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

package org.ballerina.tests.connectors.http;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

/**
 * Tests HTTP Status Codes
 */
public class HTTPStatusCodesTests {

    String ballerinaURL = "http://localhost:9090/"; // For testing purpose
    HttpClient client;
    HTTPStatusCodesTests(){
        client = new HttpClient();
        client.getHttpConnectionManager().getParams().setConnectionTimeout(10000);
        client.getHttpConnectionManager().getParams().setSoTimeout(15000);
    }

    /**
     * Test Pass through Scenario for a GET request, that receives HTTP 200 Status code with/without a message body
     * Client (GET) --> Ballerina --> BE | BE (HTTP 200 OK) --> Ballerina --> Client
     */
    @Test public void getHTTP200() throws IOException {

        //http://localhost:9090/statuscode/code/201?withbody=false
        String serviceURL = ballerinaURL + "statuscode/code/200?withbody=true";
        GetMethod get = new GetMethod(serviceURL);
        int statuscode = client.executeMethod(get);

        // Asserting the Status code. Expected 200 OK
        assertEquals(statuscode, HttpStatus.SC_OK);
        byte[] response = get.getResponseBody();
        // Asserting the Response Message.
        assertEquals(new String(response), "200 with Body as the respose!");

        serviceURL = ballerinaURL + "statuscode/code/200?withbody=false";
        GetMethod nobodyGet = new GetMethod(serviceURL);
        int statuscode2 = client.executeMethod(nobodyGet);
        assertEquals(statuscode2, HttpStatus.SC_OK);
        byte[] respose2 = nobodyGet.getResponseBody();
        assertEquals(new String(respose2), "");
    }

    /**
     * Test Pass through Scenario for a HEAD request, that receives HTTP 200 Status code
     * Client (HEAD) --> Ballerina --> BE | BE (HTTP 200 OK) --> Ballerina --> Client
     */
    @Test(enabled = true) public void headHTTP200() throws IOException {

        //http://localhost:9090/statuscode/code/201?withbody=false
        String serviceURL = ballerinaURL + "statuscode/code/200";
        HeadMethod headRequest = new HeadMethod(serviceURL);
        int statuscode = client.executeMethod(headRequest);

        // Asserting the Status code. Expected 200 OK
        assertEquals(statuscode, HttpStatus.SC_OK);
        byte[] response = headRequest.getResponseBody();
        // Asserting the Response Message.
        assertNull(response);
    }

    /**
     * Test Pass through Scenario for a GET request, that receives HTTP 200 Status code with/without a message body
     * Client (GET) --> Ballerina --> BE | BE (HTTP 201 Created) --> Ballerina --> Client
     */
    @Test public void getHTTP201() throws IOException {

        //http://localhost:9090/statuscode/code/201?withbody=false
        String serviceURL = ballerinaURL + "statuscode/code/201?withbody=true";
        GetMethod get = new GetMethod(serviceURL);
        int statuscode = client.executeMethod(get);

        // Asserting the Status code. Expected 201 Created
        assertEquals(statuscode, HttpStatus.SC_CREATED);
        byte[] response = get.getResponseBody();
        // Asserting the Response Message.
        assertEquals(new String(response), "201 with Body as the respose!");

        serviceURL = ballerinaURL + "statuscode/code/201?withbody=false";
        GetMethod nobodyGet = new GetMethod(serviceURL);
        int statuscode2 = client.executeMethod(nobodyGet);
        assertEquals(statuscode2, HttpStatus.SC_CREATED);
        byte[] respose2 = nobodyGet.getResponseBody();
        assertEquals(new String(respose2), "");
    }

    /**
     * Test Pass through Scenario for a HEAD request, that receives HTTP 201 Status code
     * Client (HEAD) --> Ballerina --> BE | BE (HTTP 201) --> Ballerina --> Client
     */
    @Test(enabled = true) public void headHTTP201() throws IOException {

        //http://localhost:9090/statuscode/code/201
        String serviceURL = ballerinaURL + "statuscode/code/201";
        //serviceURL = "http://localhost:8080/RESTfulService/mock/statusCodeService/201";
        HeadMethod headRequest = new HeadMethod(serviceURL);
        int statuscode = client.executeMethod(headRequest);

        // Asserting the Status code. Expected 201 Created
        assertEquals(statuscode, HttpStatus.SC_CREATED);
        byte[] response = headRequest.getResponseBody();
        // Asserting the Response Message.
        assertNull(response);
    }

    /**
     * Test Pass through Scenario for a GET request, that receives HTTP 200 Status code with/without a message body
     * Client (GET) --> Ballerina --> BE | BE (HTTP 202 Accepted) --> Ballerina --> Client
     */
    @Test public void getHTTP202() throws IOException {

        //http://localhost:9090/statuscode/code/202?withbody=false
        String serviceURL = ballerinaURL + "statuscode/code/202?withbody=true";
        GetMethod get = new GetMethod(serviceURL);
        int statuscode = client.executeMethod(get);

        // Asserting the Status code. Expected 202 Accepted
        assertEquals(statuscode, HttpStatus.SC_ACCEPTED);
        byte[] response = get.getResponseBody();
        // Asserting the Response Message.
        assertEquals(new String(response), "202 with Body as the respose!");

        serviceURL = ballerinaURL + "statuscode/code/202?withbody=false";
        GetMethod nobodyGet = new GetMethod(serviceURL);
        int statuscode2 = client.executeMethod(nobodyGet);
        assertEquals(statuscode2, HttpStatus.SC_ACCEPTED);
        byte[] respose2 = nobodyGet.getResponseBody();
        assertEquals(new String(respose2), "");
    }

    /**
     * Test Pass through Scenario for a HEAD request, that receives HTTP 200 Status code
     * Client (HEAD) --> Ballerina --> BE | BE (HTTP 202) --> Ballerina --> Client
     */
    @Test(enabled = true) public void headHTTP202() throws IOException {

        //http://localhost:9090/statuscode/code/202
        String serviceURL = ballerinaURL + "statuscode/code/202";
        HeadMethod headRequest = new HeadMethod(serviceURL);
        int statuscode = client.executeMethod(headRequest);

        // Asserting the Status code. Expected 202 Accepted
        assertEquals(statuscode, HttpStatus.SC_ACCEPTED);
        byte[] response = headRequest.getResponseBody();
        // Asserting the Response Message.
        assertNull(response);
    }

    /**
     * Test Pass through Scenario for a GET request, that receives HTTP 203 Status code with/without a message body
     * Client (GET) --> Ballerina --> BE | BE (HTTP 203) --> Ballerina --> Client
     */
    @Test public void getHTTP203() throws IOException {

        //http://localhost:9090/statuscode/code/202?withbody=false
        String serviceURL = ballerinaURL + "statuscode/code/203?withbody=true";
        GetMethod get = new GetMethod(serviceURL);
        int statuscode = client.executeMethod(get);

        // Asserting the Status code. Expected 202 Accepted
        assertEquals(statuscode, HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION);
        byte[] response = get.getResponseBody();
        // Asserting the Response Message.
        assertEquals(new String(response), "203 with Body as the respose!");

        serviceURL = ballerinaURL + "statuscode/code/203?withbody=false";
        GetMethod nobodyGet = new GetMethod(serviceURL);
        int statuscode2 = client.executeMethod(nobodyGet);
        assertEquals(statuscode2, HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION);
        byte[] respose2 = nobodyGet.getResponseBody();
        assertEquals(new String(respose2), "");
    }

    /**
     * Test Pass through Scenario for a HEAD request, that receives HTTP 203 Status code
     * Client (HEAD) --> Ballerina --> BE | BE (HTTP 203) --> Ballerina --> Client
     */
    @Test(enabled = true) public void headHTTP203() throws IOException {

        //http://localhost:9090/statuscode/code/202
        String serviceURL = ballerinaURL + "statuscode/code/203";
        HeadMethod headRequest = new HeadMethod(serviceURL);
        int statuscode = client.executeMethod(headRequest);

        // Asserting the Status code. Expected 203
        assertEquals(statuscode, HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION);
        byte[] response = headRequest.getResponseBody();
        // Asserting the Response Message.
        assertNull(response);
    }

    /**
     * Test Pass through Scenario for a GET request, that receives HTTP 204 Status code without a message body
     * Client (GET) --> Ballerina --> BE | BE (HTTP 204 Accepted) --> Ballerina --> Client
     */
    @Test public void getHTTP204() throws IOException {

        //http://localhost:9090/statuscode/code/204?withbody=false
        String serviceURL = ballerinaURL + "statuscode/code/204?withbody=false";
        GetMethod nobodyGet = new GetMethod(serviceURL);
        int statuscode2 = client.executeMethod(nobodyGet);
        assertEquals(statuscode2, HttpStatus.SC_NO_CONTENT);
        byte[] respose2 = nobodyGet.getResponseBody();
        assertEquals(new String(respose2), "");
    }

    /**
     * Test Pass through Scenario for a HEAD request, that receives HTTP 204 Status code
     * Client (HEAD) --> Ballerina --> BE | BE (HTTP 204) --> Ballerina --> Client
     */
    @Test(enabled = true) public void headHTTP204() throws IOException {

        //http://localhost:9090/statuscode/code/202
        String serviceURL = ballerinaURL + "statuscode/code/204";
        HeadMethod headRequest = new HeadMethod(serviceURL);
        int statuscode = client.executeMethod(headRequest);

        // Asserting the Status code. Expected 204
        assertEquals(statuscode, HttpStatus.SC_NO_CONTENT);
        byte[] response = headRequest.getResponseBody();
        // Asserting the Response Message.
        assertNull(response);
    }

    /**
     * Test Pass through Scenario for a POST request, that receives HTTP 205 Status code without a message body
     * Client (POST) --> Ballerina --> BE | BE (HTTP 205) --> Ballerina --> Client
     */
    @Test public void postHTTP205() throws IOException {

        //http://localhost:9090/statuscode/code/205?withbody=false
        String serviceURL = ballerinaURL + "statuscode/code/205?withbody=false";
        PostMethod nobodyPost = new PostMethod(serviceURL);
        int statuscode2 = client.executeMethod(nobodyPost);
        assertEquals(statuscode2, HttpStatus.SC_RESET_CONTENT);
        byte[] respose2 = nobodyPost.getResponseBody();
        assertEquals(new String(respose2), "");
    }
}
