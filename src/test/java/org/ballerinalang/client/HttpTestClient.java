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

package org.ballerinalang.client;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.GzipCompressingEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import javax.net.ssl.SSLException;

/**
 * HttpRequest sending client.
 */
public class HttpTestClient {

    private String host;
    private CloseableHttpClient httpclient;
    private RequestConfig requestConfig;
    private boolean isChunked = false;
    private boolean isContentEncoded = false;
    private String contentType = "text/plain";
    private String charSet = "UTF-8";
    private HashMap<String, String> requestHeaders = new HashMap<>();

    public HttpTestClient(String host) {

        if (host.endsWith("/")) {
            this.host = host.substring(0, host.length() - 1);
        } else {
            this.host = host;
        }

        // Timeout configs
        requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();

        // Handler for managing reties
        HttpRequestRetryHandler conRetryHandler = (exception, executionCount, context) -> {
            if (executionCount >= 3) {
                // Do not retry if over max retry count
                return false;
            }
            if (exception instanceof InterruptedIOException) {
                // Timeout
                return false;
            }
            if (exception instanceof UnknownHostException) {
                // Unknown host
                return false;
            }
            if (exception instanceof ConnectTimeoutException) {
                // Connection refused
                return true;
            }
            if (exception instanceof SSLException) {
                // SSL handshake exception
                return false;
            }
            HttpClientContext clientContext = HttpClientContext.adapt(context);
            org.apache.http.HttpRequest request = clientContext.getRequest();
            boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
            if (idempotent) {
                // Retry if the request is considered idempotent
                return true;
            }
            return false;
        };

        httpclient = HttpClients.custom().setRetryHandler(conRetryHandler).disableContentCompression().build();
    }

    public String getCharSet() {
        return charSet;
    }

    public void setCharSet(String charSet) {
        this.charSet = charSet;
    }

    public void setChunked(boolean chunked) {
        isChunked = chunked;
    }

    public void setContentEncoded(boolean contentEncoded) {
        isContentEncoded = contentEncoded;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setHeader(String header, String value) {
        this.requestHeaders.put(header, value);
    }

    /**
     * This method perform a HTTP GET and returns the response.
     * @param resourcePath Request resource path
     * @return Response holder object
     * @throws IOException
     */
    public ResponseHolder getRequest(String resourcePath) throws IOException {

        HttpGet httpget = new HttpGet(getServiceURI(resourcePath));

        if (requestHeaders != null) {
            requestHeaders.forEach((key, value) -> {
                httpget.setHeader(key, value);
            });
        }

        return setResponseValues(httpclient.execute(httpget));
    }

    public ResponseHolder postRequest(String resourcePath, String payload) throws IOException {

        return postRequest(resourcePath, IOUtils.toInputStream(payload, charSet));
    }

    /**
     * This method perform a HTTP POST and returns the response.
     * @param resourcePath Request resource path
     * @param requestStream Request message as a stream
     * @return ResponseHolder Object
     * @throws IOException
     */
    public ResponseHolder postRequest(String resourcePath, InputStream requestStream) throws IOException {

        HttpPost httppost = new HttpPost(getServiceURI(resourcePath));

        ContentType cType = ContentType.create(contentType, charSet);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IOUtils.copy(requestStream, baos);
        byte[] bytes = baos.toByteArray();
        StringEntity str = new StringEntity(new String(bytes));

        if (isChunked) {
            InputStreamEntity streamEntity = new InputStreamEntity(new ByteArrayInputStream(bytes), -1, cType);
            streamEntity.setChunked(true);
            if (isContentEncoded) {
                GzipCompressingEntity entity = new GzipCompressingEntity(streamEntity);
                httppost.setEntity(entity);
            } else {
                httppost.setEntity(streamEntity);
            }
        } else if (isContentEncoded) {
            str.setChunked(false);
            GzipCompressingEntity entity = new GzipCompressingEntity(str);
            httppost.setEntity(entity);
        } else {
            httppost.setEntity(str);
        }

        if (requestHeaders != null) {
            requestHeaders.forEach((key, value) -> {
                httppost.setHeader(key, value);
            });
        }
        return setResponseValues(httpclient.execute(httppost));
    }

    /**
     * This method perform a HTTP POST and returns the response.
     * @param resourcePath Request resource path
     * @param requestStream Request message as a stream
     * @return ResponseHolder Object
     * @throws IOException
     */
    public ResponseHolder putRequest(String resourcePath, InputStream requestStream) throws IOException {

        HttpPut httpPutt = new HttpPut(getServiceURI(resourcePath));

        ContentType cType = ContentType.getByMimeType(contentType);

        InputStreamEntity streamEntity = new InputStreamEntity(requestStream, -1, cType);

        streamEntity.setChunked(false);
        if (isChunked) {
            streamEntity.setChunked(true);
        }

        if (isContentEncoded) {
            GzipCompressingEntity entity = new GzipCompressingEntity(streamEntity);
            httpPutt.setEntity(entity);
        } else {
            httpPutt.setEntity(streamEntity);
        }

        if (requestHeaders != null) {
            requestHeaders.forEach((key, value) -> {
                httpPutt.setHeader(key, value);
            });
        }

        return setResponseValues(httpclient.execute(httpPutt));
    }

    private ResponseHolder setResponseValues(CloseableHttpResponse closeableHttpResponse) throws IOException {
        ResponseHolder response = new ResponseHolder();
        response.setResponseCode(closeableHttpResponse.getStatusLine().getStatusCode());
        response.setHeaders(getHeadersAsaMap(closeableHttpResponse.getAllHeaders()));

        byte[] responseArray = EntityUtils.toByteArray(closeableHttpResponse.getEntity());

        response.setResponseAsByteArray(responseArray);
        response.setResponseMessage(new String(responseArray));

        return response;
    }

    private String getServiceURI(String resourcepath) {

        String serviceURI = host;
        if (host.endsWith("/") && resourcepath.startsWith("/")) {
            serviceURI = serviceURI + resourcepath.substring(1);
        } else if (host.endsWith("/")) {
            serviceURI = "/" + serviceURI;
        } else {
            serviceURI = serviceURI + resourcepath;
        }
        return serviceURI;
    }

    private HashMap<String, String> getHeadersAsaMap(Header[] headers) {

        HashMap<String, String> headerMap = new HashMap<>();
        for (Header header : headers) {
            headerMap.put(header.getName(), header.getValue());
        }
        return headerMap;
    }
}
