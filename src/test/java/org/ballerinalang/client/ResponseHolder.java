package org.ballerinalang.client;

import java.util.HashMap;

/**
 * Response Object which holds response information
 */
public class ResponseHolder {

    private String responseMessage;
    private int responseCode;
    private String httpVersion;
    private HashMap<String, String> Headers;

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public HashMap<String, String> getHeaders() {
        return Headers;
    }

    public void setHeaders(HashMap<String, String> headers) {
        Headers = headers;
    }
}
