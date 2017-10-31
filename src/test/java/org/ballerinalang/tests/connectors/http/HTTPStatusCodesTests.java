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

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ballerinalang.tests.base.BallerinaBaseTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

/**
 * Tests HTTP Status Codes
 * Uses StatusCodeService.bal service
 */
public class HTTPStatusCodesTests extends BallerinaBaseTest {
    HttpClient client;
    private static final Log log = LogFactory.getLog(HTTPStatusCodesTests.class);
    //String ballerinaURL = "http://192.168.48.12:32013";
    //String ballerinaURL = "http://localhost:9090";

    HTTPStatusCodesTests() {
        client = new HttpClient();
        client.getHttpConnectionManager().getParams().setConnectionTimeout(10000);
        client.getHttpConnectionManager().getParams().setSoTimeout(15000);
    }

    /**
     * Data provider for Data driven tests for generic 5xx status codes
     */
    @DataProvider(name = "5xxStatusCodes") public static Object[][] statusCodeSet1() {

        return new String[][] { { "500" }, { "501" }, { "502" }, { "503" }, { "504" }, { "505" } };

    }

    /**
     * Data provider for Data driven tests for generic 4xx status codes
     */
    @DataProvider(name = "4xxStatusCodes") public static Object[][] statusCodeSet2() {

        return new String[][] { { "400" }, { "401" }, { "402" }, { "403" }, { "404" }, { "405" }, { "408" }, { "409" },
                { "410" }, { "411" }, { "413" }, { "414" }, { "415" }, { "417" }, { "426" } };

    }

    /**
     * Data provider for Data driven tests for generic 3xx status codes
     */
    @DataProvider(name = "3xxStatusCodes") public static Object[][] statusCodeSet3() {

        return new String[][] { { "300" }, { "301" }, { "302" }, { "303" }, { "307" } };

    }

    /**
     * Data provider for Data driven tests for generic 2xx status codes
     */
    @DataProvider(name = "2xxStatusCodes") public static Object[][] statusCodeSet4() {

        return new String[][] { { "200" }, { "201" }, { "202" }, { "203" } };

    }

    /**
     * Test Pass through Scenario for a GET request, that receives HTTP 2XX Status code with a message body
     * Client (GET) --> Ballerina --> BE | BE (HTTP 200 OK) --> Ballerina --> Client
     */
    @Test(dataProvider = "2xxStatusCodes") public void getHTTP2XXWithBody(String code) throws IOException {
        log.info("Executing Test Method : getHTTP2XX for status code : " + code);
        //http://localhost:9090/statuscode/code/201?withbody=false
        String serviceURL = ballerinaURL + "/statuscode/code/" + code + "?withbody=true";
        GetMethod get = new GetMethod(serviceURL);
        int statuscode = client.executeMethod(get);

        // Asserting the Status code. Expected 200 OK
        assertEquals(String.valueOf(statuscode), code);
        byte[] response = get.getResponseBody();
        // Asserting the Response Message.
        assertEquals(new String(response), code + " with Body as the respose!");

    }


    /**
     * Test Pass through Scenario for a GET request, that receives HTTP 2XX Status code without a message body
     * Client (GET) --> Ballerina --> BE | BE (HTTP 200 OK) --> Ballerina --> Client
     */
    @Test(dataProvider = "2xxStatusCodes") public void getHTTP2XXWithoutBody(String code) throws IOException {
        log.info("Executing Test Method : getHTTP2XX for status code : " + code);
        //http://localhost:9090/statuscode/code/201?withbody=false
        String serviceURL = ballerinaURL + "/statuscode/code/" + code + "?withbody=false";
        GetMethod get = new GetMethod(serviceURL);
        int statuscode = client.executeMethod(get);

        // Asserting the Status code. Expected 200 OK
        assertEquals(String.valueOf(statuscode), code);
        byte[] response = get.getResponseBody();
        // Asserting the Response Message.
        assertEquals(new String(response), "");
    }

    /**
     * Test Pass through Scenario for a HEAD request, that receives HTTP 2XX Status code
     * Client (HEAD) --> Ballerina --> BE | BE (HTTP 200 OK) --> Ballerina --> Client
     * Disabled due to a bug
     */
    @Test(dataProvider = "2xxStatusCodes", enabled = false) public void headHTTP2XX(String code) throws IOException {
        log.info("Executing Test Method : headHTTP2XX for status code : " + code);
        //http://localhost:9090/statuscode/code/201?withbody=false
        String serviceURL = ballerinaURL + "/statuscode/code/" + code;
        HeadMethod headRequest = new HeadMethod(serviceURL);
        int statuscode = client.executeMethod(headRequest);

        // Asserting the Status code. Expected 200 OK
        assertEquals(String.valueOf(statuscode), code);
        byte[] response = headRequest.getResponseBody();
        // Asserting the Response Message.
        assertNull(response);
    }

    /**
     * Test Pass through Scenario for a GET request, that receives HTTP 204 Status code without a message body
     * Client (GET) --> Ballerina --> BE | BE (HTTP 204 Accepted) --> Ballerina --> Client
     */
    @Test public void getHTTP204() throws IOException {
        log.info("Executing Test Method : getHTTP204");
        //http://localhost:9090/statuscode/code/204?withbody=false
        String serviceURL = ballerinaURL + "/statuscode/code/204?withbody=false";
        GetMethod nobodyGet = new GetMethod(serviceURL);
        int statuscode2 = client.executeMethod(nobodyGet);
        assertEquals(statuscode2, HttpStatus.SC_NO_CONTENT);
        byte[] respose2 = nobodyGet.getResponseBody();
        assertNull(respose2);
        // assertEquals(new String(respose2), "");
    }

    /**
     * Test Pass through Scenario for a HEAD request, that receives HTTP 204 Status code
     * Client (HEAD) --> Ballerina --> BE | BE (HTTP 204) --> Ballerina --> Client
     * disabled due to a bug
     */
    @Test(enabled = false) public void headHTTP204() throws IOException {
        log.info("Executing Test Method : headHTTP204");
        //http://localhost:9090/statuscode/code/202
        String serviceURL = ballerinaURL + "/statuscode/code/204";
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
     * HTTP 205 doesn't allow to send a payload
     * Client (POST) --> Ballerina --> BE | BE (HTTP 205) --> Ballerina --> Client
     * disabled due to a bug
     */
    @Test (enabled = false) public void postHTTP205() throws IOException {
        log.info("Executing Test Method : postHTTP205");
        //http://localhost:9090/statuscode/code/205?withbody=false
        String serviceURL = ballerinaURL + "/statuscode/code/205?withbody=false";
        PostMethod nobodyPost = new PostMethod(serviceURL);
        int statuscode2 = client.executeMethod(nobodyPost);
        assertEquals(statuscode2, HttpStatus.SC_RESET_CONTENT);
        byte[] respose2 = nobodyPost.getResponseBody();
        assertNull(respose2);
    }

    /**
     * Test Pass through Scenario for a POST request, that receives HTTP 206 Status code with a
     * message body and Content-range header
     * Client (POST) --> Ballerina --> BE | BE (HTTP 206) --> Ballerina --> Client
     */
    @Test public void postHTTP206() throws IOException {
        log.info("Executing Test Method : postHTTP206");
        //http://localhost:9090/statuscode/code/205?withbody=false
        String serviceURL = ballerinaURL + "/statuscode/code/206?withbody=true";
        PostMethod nobodyPost = new PostMethod(serviceURL);
        int statuscode2 = client.executeMethod(nobodyPost);
        assertEquals(statuscode2, HttpStatus.SC_PARTIAL_CONTENT);
        byte[] respose2 = nobodyPost.getResponseBody();
        Header header = nobodyPost.getResponseHeader("Content-Range");
        assertEquals(new String(respose2), "206 with Body as the respose!");
        assertNotNull(header, "The Header value content-range is not present");
        assertEquals(header.getValue(), "bytes 21010-47021/47022");
    }

    // TODO: Need to look into other HTTP status codes such as 207 +
    /**
     * Test Pass through Scenario for a POST request, that receives HTTP 300 Status code with a
     * message body and Location header
     * Client (POST) --> Ballerina --> BE | BE (HTTP 205) --> Ballerina --> Client
     */
    @Test(dataProvider = "3xxStatusCodes") public void postHTTP3XX(String code) throws IOException {
        log.info("Executing Test Method : postHTTP3XX for status code : " + code);
        //http://localhost:9090/statuscode/code/205?withbody=false
        String serviceURL = ballerinaURL + "/statuscode/code/" + code + "?withbody=true";
        PostMethod nobodyPost = new PostMethod(serviceURL);
        int statuscode = client.executeMethod(nobodyPost);
        assertEquals(String.valueOf(statuscode), code);
        byte[] response = nobodyPost.getResponseBody();

        Header locationHeader = nobodyPost.getResponseHeader("Location");
        Header contentLocationHeader = nobodyPost.getResponseHeader("Content-Location");
        assertNotNull(locationHeader, "The Location Header is not present");
        assertNotNull(contentLocationHeader, "The Content-Location Header is not present");
        assertEquals(locationHeader.getValue(), "/RESTfulService/mock/redirect/get");
        assertEquals(new String(response), code + " with Body as the respose!");
    }

    /**
     * Test Pass through Scenario for a POST request, that receives HTTP 304 Status code with a
     * message body and Location header
     * Client (POST) --> Ballerina --> BE | BE (HTTP 304) --> Ballerina --> Client
     * With 304 a Body is not allowed
     */
    @Test public void postHTTP304() throws IOException {
        log.info("Executing Test Method : postHTTP304");
        //http://localhost:9090/statuscode/code/304?withbody=false
        String serviceURL = ballerinaURL + "/statuscode/code/304";
        PostMethod nobodyPost = new PostMethod(serviceURL);
        int statuscode = client.executeMethod(nobodyPost);
        assertEquals(statuscode, HttpStatus.SC_NOT_MODIFIED);
        byte[] response = nobodyPost.getResponseBody();
        Header header = nobodyPost.getResponseHeader("Location");
        Header contentLocationHeader = nobodyPost.getResponseHeader("Content-Location");
        assertNull(response);
        assertNotNull(header, "The Location Header is not present");
        assertNotNull(contentLocationHeader, "The Content-Location Header is not present");
        assertEquals(header.getValue(), "/RESTfulService/mock/redirect/get");
    }

    //TODO: Add tests for HTTP 308 etc

    /**
     * Data Driven Test to cover 4xx status codes,
     * Test Pass through Scenario for a POST request, that receives HTTP 4XX Status code with a
     * message body and Location headers
     * Client (POST) --> Ballerina --> BE | BE (HTTP 304) --> Ballerina --> Client
     */
    @Test(dataProvider = "4xxStatusCodes") public void postHTTP4XX(String code) throws IOException {
        log.info("Executing Test Method : postHTTP4XX for status code : " + code);
        //http://localhost:9090/statuscode/code/400?withbody=false
        String serviceURL = ballerinaURL + "/statuscode/code/" + code + "?withbody=true";
        PostMethod nobodyPost = new PostMethod(serviceURL);
        int statuscode = client.executeMethod(nobodyPost);
        assertEquals(String.valueOf(statuscode), code);
        byte[] response = nobodyPost.getResponseBody();
        assertEquals(new String(response), code + " with Body as the respose!");
    }

    /**
     * Test Pass through Scenario for a POST request, that receives HTTP 426 Status code with a
     * message body and Location header
     * HTTP 426 Sends special types of headers
     * Client (POST) --> Ballerina --> BE | BE (HTTP 405) --> Ballerina --> Client
     */
    @Test public void postHTTP426() throws IOException {
        log.info("Executing Test Method : postHTTP426");
        //http://localhost:9090/statuscode/code/404?withbody=false
        String serviceURL = ballerinaURL + "/statuscode/code/426?withbody=true";
        PostMethod nobodyPost = new PostMethod(serviceURL);
        int statuscode = client.executeMethod(nobodyPost);
        assertEquals(statuscode, 426);
        byte[] response = nobodyPost.getResponseBody();
        assertEquals(new String(response), "426 with Body as the respose!");
        Header connectionHeader = nobodyPost.getResponseHeader("Connection");
        Header upgradeHeader = nobodyPost.getResponseHeader("Upgrade");
        assertNotNull(connectionHeader, "The Connection Header is not present");
        assertNotNull(upgradeHeader, "The Upgrade Header is not present");
        assertEquals(connectionHeader.getValue(), "Upgrade");
        assertEquals(upgradeHeader.getValue(), "HTTP/2.0");
    }

    /**
     * Data Driven Test to cover 5xx status codes for a POST request,
     * Test Pass through Scenario for a POST request, that receives HTTP 5XX Status code with a message body
     * Client (POST) --> Ballerina --> BE | BE (HTTP 304) --> Ballerina --> Client
     */
    @Test(dataProvider = "5xxStatusCodes") public void postHTTP5XX(String code) throws IOException {
        log.info("Executing Test Method : postHTTP5XX for status code : " + code);
        // http://localhost:9090/statuscode/code/500?withbody=false
        String serviceURL = ballerinaURL + "/statuscode/code/" + code + "?withbody=true";
        PostMethod nobodyPost = new PostMethod(serviceURL);
        int statuscode = client.executeMethod(nobodyPost);
        assertEquals(String.valueOf(statuscode), code);
        byte[] response = nobodyPost.getResponseBody();
        assertEquals(new String(response), code + " with Body as the respose!");
    }

    /**
     * Data Driven Test to cover 5xx status codes for a GET request,
     * Test Pass through Scenario for a GET request, that receives HTTP 5XX Status code with a message body
     * Client (GET) --> Ballerina --> BE | BE (HTTP 5XX) --> Ballerina --> Client
     */
    @Test(dataProvider = "5xxStatusCodes") public void getHTTP5XX(String code) throws IOException {
        log.info("Executing Test Method : getHTTP5XX for status code : " + code);
        // http://localhost:9090/statuscode/code/500?withbody=false
        String serviceURL = ballerinaURL + "/statuscode/code/" + code + "?withbody=true";
        GetMethod nobodyPost = new GetMethod(serviceURL);
        int statuscode = client.executeMethod(nobodyPost);
        assertEquals(String.valueOf(statuscode), code);
        byte[] response = nobodyPost.getResponseBody();
        assertEquals(new String(response), code + " with Body as the respose!");
    }
}
