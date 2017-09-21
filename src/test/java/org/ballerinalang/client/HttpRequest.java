package org.ballerinalang.client;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.io.IOUtils;
import org.ballerinalang.client.compressor.GzipCompressor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;

/**
 * HttpRequest sending client
 */
public class HttpRequest {

    private String host;
    private HttpClient client;
    private final GzipCompressor gzipCompressor = new GzipCompressor();
    private boolean isChunked = false;
    private boolean isContentEncoded = false;
    private String contentType = "text/plain";
    private HashMap<String, String> requestHeaders = new HashMap<>();

    public HttpRequest(String host) {

        // Initializing the client and setting the default connection and so timeout
        client = new HttpClient();
        client.getHttpConnectionManager().getParams().setConnectionTimeout(10000);
        client.getHttpConnectionManager().getParams().setSoTimeout(12000);

        if (host.endsWith("/")) {
            this.host = host.substring(0, host.length() - 1);
        } else {
            this.host = host;
        }
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

    public ResponseHolder getRequest(String resourcePath) throws IOException {

        GetMethod get = new GetMethod(getServiceURI(resourcePath));
        if (requestHeaders != null) {
            requestHeaders.forEach((key, value) -> {
                get.setRequestHeader(key, requestHeaders.get(key));
            });
        }

        client.executeMethod(get);
        ResponseHolder response = new ResponseHolder();
        response.setResponseCode(get.getStatusCode());
        response.setHeaders(getHeadersAsaMap(get));
        response.setResponseMessage(get.getResponseBodyAsString());
        return response;
    }

    public ResponseHolder postRequest(String resourcePath, String messageBody) throws IOException {

        InputStream streamedBody = IOUtils.toInputStream(messageBody, Charset.defaultCharset().toString());

        return postRequest(resourcePath, streamedBody);
    }

    public ResponseHolder postRequest(String resourcePath, InputStream requestEntity) throws IOException {

        if (isContentEncoded) {
            requestEntity = gzipCompressor.compress(requestEntity);
            this.setHeader("Content-Encoding", "gzip");
        }

        InputStreamRequestEntity streamedEntity = new InputStreamRequestEntity(requestEntity, contentType);

        PostMethod postRequest = new PostMethod(getServiceURI(resourcePath));
        if (isChunked) {
            postRequest.setContentChunked(true);
        }

        if (requestHeaders != null) {
            requestHeaders.forEach((key, value) -> {
                postRequest.setRequestHeader(key, requestHeaders.get(key));
            });
        }

        postRequest.setRequestEntity(streamedEntity);
        client.executeMethod(postRequest);
        ResponseHolder response = new ResponseHolder();
        response.setResponseCode(postRequest.getStatusCode());
        response.setHeaders(getHeadersAsaMap(postRequest));
        response.setResponseMessage(postRequest.getResponseBodyAsString());

        return response;
    }

    // TODO : Need to improve to support chunking etc. Also add methods for head, patch as well
    public ResponseHolder putRequest(String serviceURI, StringRequestEntity requestEntity) throws IOException {

        PutMethod putRequest = new PutMethod(serviceURI);
        requestHeaders.forEach((key, value) -> {
            putRequest.setRequestHeader(key, requestHeaders.get(key));
        });
        putRequest.setRequestEntity(requestEntity);
        client.executeMethod(putRequest);
        ResponseHolder response = new ResponseHolder();
        response.setResponseCode(putRequest.getStatusCode());
        response.setHeaders(getHeadersAsaMap(putRequest));
        response.setResponseMessage(putRequest.getResponseBodyAsString());

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

    private HashMap<String, String> getHeadersAsaMap(HttpMethod method) {

        HashMap<String, String> headerMap = new HashMap<>();
        Header[] headers = method.getResponseHeaders();
        for (Header header : headers) {
            headerMap.put(header.getName(), header.getValue());
        }
        return headerMap;
    }
}
