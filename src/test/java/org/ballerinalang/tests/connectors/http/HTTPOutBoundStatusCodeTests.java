package org.ballerinalang.tests.connectors.http;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.ballerinalang.tests.base.BallerinaBaseTest;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

public class HTTPOutBoundStatusCodeTests extends BallerinaBaseTest {

    HttpClient client;

    HTTPOutBoundStatusCodeTests() {
        client = new HttpClient();
        client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
        client.getHttpConnectionManager().getParams().setSoTimeout(8000);
    }

    @Test
    public void testOutbound200ForGet() throws Exception {
        String serviceURL = ballerinaURL + "/http/call";

        GetMethod get = new GetMethod(serviceURL);
        int statuscode = client.executeMethod(get);
        byte[] response = get.getResponseBody();
        Header[] headers;
        headers = get.getResponseHeaders();

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_OK);
        assertEquals(new String(response), "This is a test payload");
        assertNotNull(headers);
    }

    @Test
    public void testOutbound200ForHead() throws Exception {
        String serviceURL = ballerinaURL + "/http/call";

        HeadMethod head = new HeadMethod(serviceURL);
        int statuscode = client.executeMethod(head);
        byte[] response = head.getResponseBody();
        Header[] headers;
        headers = head.getResponseHeaders();

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_OK);
        assertNull(response);
        assertNotNull(headers);
    }

    @Test
    public void testOutbound200ForPost() throws Exception {
        String serviceURL = ballerinaURL + "/http/call";

        PostMethod post = new PostMethod(serviceURL);
        int statuscode = client.executeMethod(post);
        byte[] response = post.getResponseBody();
        Header[] headers;
        headers = post.getResponseHeaders();

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_OK);
        assertEquals(new String(response), "This is a test payload");
        assertNotNull(headers);
    }

    @Test
    public void testOutbound200ForPut() throws Exception {
        String serviceURL = ballerinaURL + "/http/call";

        PutMethod put = new PutMethod(serviceURL);
        int statuscode = client.executeMethod(put);
        byte[] response = put.getResponseBody();
        Header[] headers;
        headers = put.getResponseHeaders();

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_OK);
        assertEquals(new String(response), "This is a test payload");
        assertNotNull(headers);
    }

    @Test
    public void testOutbound200ForDelete() throws Exception {
        String serviceURL = ballerinaURL + "/http/call";

        DeleteMethod delete = new DeleteMethod(serviceURL);
        int statuscode = client.executeMethod(delete);
        byte[] response = delete.getResponseBody();
        Header[] headers;
        headers = delete.getResponseHeaders();

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_OK);
        assertEquals(new String(response), "This is a test payload");
        assertNotNull(headers);
    }

    @Test
    public void testOutbound202ForGet() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/batch";

        GetMethod get = new GetMethod(serviceURL);
        int statuscode = client.executeMethod(get);
        byte[] response = get.getResponseBody();
        Header[] headers;
        headers = get.getResponseHeaders();

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_ACCEPTED);
        assertEquals(new String(response), "This is a test payload");
        assertNotNull(headers);
    }

    @Test
    public void testOutbound202ForHead() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/batch";

        HeadMethod head = new HeadMethod(serviceURL);
        int statuscode = client.executeMethod(head);
        byte[] response = head.getResponseBody();
        Header[] headers;
        headers = head.getResponseHeaders();

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_ACCEPTED);
        assertNull(response);
        assertNotNull(headers);
    }

    @Test
    public void testOutbound202ForPost() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/batch";

        PostMethod post = new PostMethod(serviceURL);
        int statuscode = client.executeMethod(post);
        byte[] response = post.getResponseBody();
        Header[] headers;
        headers = post.getResponseHeaders();

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_ACCEPTED);
        assertEquals(new String(response), "This is a test payload");
        assertNotNull(headers);
    }

    @Test
    public void testOutbound202ForPut() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/batch";

        PutMethod put = new PutMethod(serviceURL);
        int statuscode = client.executeMethod(put);
        byte[] response = put.getResponseBody();
        Header[] headers;
        headers = put.getResponseHeaders();

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_ACCEPTED);
        assertEquals(new String(response), "This is a test payload");
        assertNotNull(headers);
    }

    @Test
    public void testOutbound202ForDelete() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/batch";

        DeleteMethod delete = new DeleteMethod(serviceURL);
        int statuscode = client.executeMethod(delete);
        byte[] response = delete.getResponseBody();
        Header[] headers;
        headers = delete.getResponseHeaders();

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_ACCEPTED);
        assertEquals(new String(response), "This is a test payload");
        assertNotNull(headers);
    }

    @Test
    public void testOutbound203ForGet() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/changed";

        GetMethod get = new GetMethod(serviceURL);
        int statuscode = client.executeMethod(get);
        byte[] response = get.getResponseBody();
        Header[] headers;
        headers = get.getResponseHeaders();

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION);
        assertEquals(new String(response), "This is a test payload");
        assertNotNull(headers);
    }

    @Test
    public void testOutbound203ForHead() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/changed";

        HeadMethod head = new HeadMethod(serviceURL);
        int statuscode = client.executeMethod(head);
        byte[] response = head.getResponseBody();
        Header[] headers;
        headers = head.getResponseHeaders();

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION);
        assertNull(response);
        assertNotNull(headers);
    }

    @Test
    public void testOutbound203ForPost() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/changed";

        PostMethod post = new PostMethod(serviceURL);
        int statuscode = client.executeMethod(post);
        byte[] response = post.getResponseBody();
        Header[] headers;
        headers = post.getResponseHeaders();

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION);
        assertEquals(new String(response), "This is a test payload");
        assertNotNull(headers);
    }

    @Test
    public void testOutbound203ForPut() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/changed";

        PutMethod put = new PutMethod(serviceURL);
        int statuscode = client.executeMethod(put);
        byte[] response = put.getResponseBody();
        Header[] headers;
        headers = put.getResponseHeaders();

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION);
        assertEquals(new String(response), "This is a test payload");
        assertNotNull(headers);
    }

    @Test
    public void testOutbound203ForDelete() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/changed";

        DeleteMethod delete = new DeleteMethod(serviceURL);
        int statuscode = client.executeMethod(delete);
        byte[] response = delete.getResponseBody();
        Header[] headers;
        headers = delete.getResponseHeaders();

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION);
        assertEquals(new String(response), "This is a test payload");
        assertNotNull(headers);
    }

    @Test
    public void testOutbound201ForPost() throws Exception {
        String serviceURL = ballerinaURL + "/http/orders/ee1a9ec2-c8a5-4afe-8585-74df591f9990";

        PostMethod post = new PostMethod(serviceURL);
        int statuscode = client.executeMethod(post);
        Header[] headers;
        headers = post.getResponseHeaders();
        String etagHeader = null;
        String locationHeader = null;
        for (Header header : headers) {
            if (header.getName().equalsIgnoreCase("ETag")) {
                etagHeader = header.getValue();
            } else if (header.getName().equalsIgnoreCase("Location")) {
                locationHeader = header.getValue();
            }
        }

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_CREATED);
        assertNotNull(headers);
        assertNotNull(etagHeader);
        assertNotNull(locationHeader);
    }

    @Test
    public void testOutbound201ForPut() throws Exception {
        String serviceURL = ballerinaURL + "/http/orders/ee1a9ec2-c8a5-4afe-8585-74df591f9991";

        PutMethod put = new PutMethod(serviceURL);
        int statuscode = client.executeMethod(put);
        Header[] headers;
        headers = put.getResponseHeaders();
        String etagHeader = null;
        String locationHeader = null;
        for (Header header : headers) {
            if (header.getName().equalsIgnoreCase("ETag")) {
                etagHeader = header.getValue();
            } else if (header.getName().equalsIgnoreCase("Location")) {
                locationHeader = header.getValue();
            }
        }

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_CREATED);
        assertNotNull(headers);
        assertNotNull(etagHeader);
        assertNotNull(locationHeader);
    }

    @Test
    public void testOutbound204ForPut() throws Exception {
        String serviceURL = ballerinaURL + "/http/orders/update/ee1a9ec2-c8a5-4afe-8585-74df591f9991";

        PutMethod put = new PutMethod(serviceURL);
        int statuscode = client.executeMethod(put);
        Header[] headers;
        headers = put.getResponseHeaders();
        String etagHeader = null;
        String locationHeader = null;
        for (Header header : headers) {
            if (header.getName().equalsIgnoreCase("ETag")) {
                etagHeader = header.getValue();
            } else if (header.getName().equalsIgnoreCase("Location")) {
                locationHeader = header.getValue();
            }
        }

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_NO_CONTENT);
        assertNotNull(headers);
        assertNotNull(etagHeader);
        assertNotNull(locationHeader);
    }

    @Test
    public void testOutbound204ForDelete() throws Exception {
        String serviceURL = ballerinaURL + "/http/orders/update/ee1a9ec2-c8a5-4afe-8585-74df591f9991";

        DeleteMethod delete = new DeleteMethod(serviceURL);
        int statuscode = client.executeMethod(delete);
        Header[] headers;
        headers = delete.getResponseHeaders();
        String etagHeader = null;
        String locationHeader = null;
        for (Header header : headers) {
            if (header.getName().equalsIgnoreCase("ETag")) {
                etagHeader = header.getValue();
            } else if (header.getName().equalsIgnoreCase("Location")) {
                locationHeader = header.getValue();
            }
        }

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_NO_CONTENT);
        assertNotNull(headers);
        assertNotNull(etagHeader);
        assertNotNull(locationHeader);
    }

    @Test
    public void testOutbound205ForGet() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/batch/reset";

        GetMethod get = new GetMethod(serviceURL);
        int statuscode = client.executeMethod(get);
        byte[] response = get.getResponseBody();
        int contentLength = response.length;

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_RESET_CONTENT);
        assertEquals(contentLength, 0);
    }

    @Test
    public void testOutbound205ForPost() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/batch/reset";

        PostMethod post = new PostMethod(serviceURL);
        int statuscode = client.executeMethod(post);
        byte[] response = post.getResponseBody();
        int contentLength = response.length;

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_RESET_CONTENT);
        assertEquals(contentLength, 0);
    }

    @Test
    public void testOutbound205ForPut() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/batch/reset";

        PutMethod put = new PutMethod(serviceURL);
        int statuscode = client.executeMethod(put);
        byte[] response = put.getResponseBody();
        int contentLength = response.length;

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_RESET_CONTENT);
        assertEquals(contentLength, 0);
    }

    @Test
    public void testOutbound205ForDelete() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/batch/reset";

        DeleteMethod delete = new DeleteMethod(serviceURL);
        int statuscode = client.executeMethod(delete);
        byte[] response = delete.getResponseBody();
        int contentLength = response.length;

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_RESET_CONTENT);
        assertEquals(contentLength, 0);
    }

    @Test
    public void testOutbound205ForHead() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/batch/reset";

        HeadMethod head = new HeadMethod(serviceURL);
        int statuscode = client.executeMethod(head);
        byte[] response = head.getResponseBody();
        int contentLength = response.length;

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_RESET_CONTENT);
        assertEquals(contentLength, 0);
    }

    @Test
    public void testOutbound300ForGet() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/batch/300";

        GetMethod get = new GetMethod(serviceURL);
        int statuscode = client.executeMethod(get);
        Header[] headers;
        headers = get.getResponseHeaders();
        String locationHeader = null;
        for (Header header : headers) {
            if (header.getName().equalsIgnoreCase("Location")) {
                locationHeader = header.getValue();
            }
        }

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_MULTIPLE_CHOICES);
        assertNotNull(headers);
        assertNotNull(locationHeader);
    }

    @Test
    public void testOutbound301ForGet() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/batch/301";

        GetMethod get = new GetMethod(serviceURL);
        int statuscode = client.executeMethod(get);
        Header[] headers;
        headers = get.getResponseHeaders();
        String locationHeader = null;
        for (Header header : headers) {
            if (header.getName().equalsIgnoreCase("Location")) {
                locationHeader = header.getValue();
            }
        }

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_MOVED_PERMANENTLY);
        assertNotNull(headers);
        assertNotNull(locationHeader);
    }

    @Test
    public void testOutbound302ForGet() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/batch/302";

        GetMethod get = new GetMethod(serviceURL);
        int statuscode = client.executeMethod(get);
        Header[] headers;
        headers = get.getResponseHeaders();
        String locationHeader = null;
        for (Header header : headers) {
            if (header.getName().equalsIgnoreCase("Location")) {
                locationHeader = header.getValue();
            }
        }

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_MOVED_TEMPORARILY);
        assertNotNull(headers);
        assertNotNull(locationHeader);
    }

    @Test
    public void testOutbound303ForGet() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/batch/303";

        GetMethod get = new GetMethod(serviceURL);
        int statuscode = client.executeMethod(get);
        Header[] headers;
        headers = get.getResponseHeaders();
        String locationHeader = null;
        for (Header header : headers) {
            if (header.getName().equalsIgnoreCase("Location")) {
                locationHeader = header.getValue();
            }
        }

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_SEE_OTHER);
        assertNotNull(headers);
        assertNotNull(locationHeader);
    }

    @Test
    public void testOutbound305ForGet() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/batch/305";

        GetMethod get = new GetMethod(serviceURL);
        int statuscode = client.executeMethod(get);
        Header[] headers;
        headers = get.getResponseHeaders();
        String locationHeader = null;
        for (Header header : headers) {
            if (header.getName().equalsIgnoreCase("Location")) {
                locationHeader = header.getValue();
            }
        }

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_USE_PROXY);
        assertNotNull(headers);
        assertNotNull(locationHeader);
    }

    @Test
    public void testOutbound307ForGet() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/batch/307";

        GetMethod get = new GetMethod(serviceURL);
        int statuscode = client.executeMethod(get);
        Header[] headers;
        headers = get.getResponseHeaders();
        String locationHeader = null;
        for (Header header : headers) {
            if (header.getName().equalsIgnoreCase("Location")) {
                locationHeader = header.getValue();
            }
        }

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_TEMPORARY_REDIRECT);
        assertNotNull(headers);
        assertNotNull(locationHeader);
    }

    @Test
    public void testOutbound300ForPost() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/batch/300";

        PostMethod post = new PostMethod(serviceURL);
        int statuscode = client.executeMethod(post);
        Header[] headers;
        headers = post.getResponseHeaders();
        String locationHeader = null;
        for (Header header : headers) {
            if (header.getName().equalsIgnoreCase("Location")) {
                locationHeader = header.getValue();
            }
        }

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_MULTIPLE_CHOICES);
        assertNotNull(headers);
        assertNotNull(locationHeader);
    }

    @Test
    public void testOutbound301ForPost() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/batch/301";

        PostMethod post = new PostMethod(serviceURL);
        int statuscode = client.executeMethod(post);
        Header[] headers;
        headers = post.getResponseHeaders();
        String locationHeader = null;
        for (Header header : headers) {
            if (header.getName().equalsIgnoreCase("Location")) {
                locationHeader = header.getValue();
            }
        }

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_MOVED_PERMANENTLY);
        assertNotNull(headers);
        assertNotNull(locationHeader);
    }

    @Test
    public void testOutbound302ForPost() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/batch/302";

        PostMethod post = new PostMethod(serviceURL);
        int statuscode = client.executeMethod(post);
        Header[] headers;
        headers = post.getResponseHeaders();
        String locationHeader = null;
        for (Header header : headers) {
            if (header.getName().equalsIgnoreCase("Location")) {
                locationHeader = header.getValue();
            }
        }

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_MOVED_TEMPORARILY);
        assertNotNull(headers);
        assertNotNull(locationHeader);
    }

    @Test
    public void testOutbound303ForPost() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/batch/303";

        PostMethod post = new PostMethod(serviceURL);
        int statuscode = client.executeMethod(post);
        Header[] headers;
        headers = post.getResponseHeaders();
        String locationHeader = null;
        for (Header header : headers) {
            if (header.getName().equalsIgnoreCase("Location")) {
                locationHeader = header.getValue();
            }
        }

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_SEE_OTHER);
        assertNotNull(headers);
        assertNotNull(locationHeader);
    }

    @Test
    public void testOutbound305ForPost() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/batch/305";

        PostMethod post = new PostMethod(serviceURL);
        int statuscode = client.executeMethod(post);
        Header[] headers;
        headers = post.getResponseHeaders();
        String locationHeader = null;
        for (Header header : headers) {
            if (header.getName().equalsIgnoreCase("Location")) {
                locationHeader = header.getValue();
            }
        }

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_USE_PROXY);
        assertNotNull(headers);
        assertNotNull(locationHeader);
    }

    @Test
    public void testOutbound307ForPost() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/batch/307";

        PostMethod post = new PostMethod(serviceURL);
        int statuscode = client.executeMethod(post);
        Header[] headers;
        headers = post.getResponseHeaders();
        String locationHeader = null;
        for (Header header : headers) {
            if (header.getName().equalsIgnoreCase("Location")) {
                locationHeader = header.getValue();
            }
        }

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_TEMPORARY_REDIRECT);
        assertNotNull(headers);
        assertNotNull(locationHeader);
    }

    @Test
    public void testOutbound300ForPut() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/batch/300";

        PutMethod put = new PutMethod(serviceURL);
        int statuscode = client.executeMethod(put);
        Header[] headers;
        headers = put.getResponseHeaders();
        String locationHeader = null;
        for (Header header : headers) {
            if (header.getName().equalsIgnoreCase("Location")) {
                locationHeader = header.getValue();
            }
        }

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_MULTIPLE_CHOICES);
        assertNotNull(headers);
        assertNotNull(locationHeader);
    }

    @Test
    public void testOutbound301ForPut() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/batch/301";

        PutMethod put = new PutMethod(serviceURL);
        int statuscode = client.executeMethod(put);
        Header[] headers;
        headers = put.getResponseHeaders();
        String locationHeader = null;
        for (Header header : headers) {
            if (header.getName().equalsIgnoreCase("Location")) {
                locationHeader = header.getValue();
            }
        }

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_MOVED_PERMANENTLY);
        assertNotNull(headers);
        assertNotNull(locationHeader);
    }

    @Test
    public void testOutbound302ForPut() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/batch/302";

        PutMethod put = new PutMethod(serviceURL);
        int statuscode = client.executeMethod(put);
        Header[] headers;
        headers = put.getResponseHeaders();
        String locationHeader = null;
        for (Header header : headers) {
            if (header.getName().equalsIgnoreCase("Location")) {
                locationHeader = header.getValue();
            }
        }

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_MOVED_TEMPORARILY);
        assertNotNull(headers);
        assertNotNull(locationHeader);
    }

    @Test
    public void testOutbound303ForPut() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/batch/303";

        PutMethod put = new PutMethod(serviceURL);
        int statuscode = client.executeMethod(put);
        Header[] headers;
        headers = put.getResponseHeaders();
        String locationHeader = null;
        for (Header header : headers) {
            if (header.getName().equalsIgnoreCase("Location")) {
                locationHeader = header.getValue();
            }
        }

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_SEE_OTHER);
        assertNotNull(headers);
        assertNotNull(locationHeader);
    }

    @Test
    public void testOutbound305ForPut() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/batch/305";

        PutMethod put = new PutMethod(serviceURL);
        int statuscode = client.executeMethod(put);
        Header[] headers;
        headers = put.getResponseHeaders();
        String locationHeader = null;
        for (Header header : headers) {
            if (header.getName().equalsIgnoreCase("Location")) {
                locationHeader = header.getValue();
            }
        }

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_USE_PROXY);
        assertNotNull(headers);
        assertNotNull(locationHeader);
    }

    @Test
    public void testOutbound307ForPut() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/batch/307";

        PutMethod put = new PutMethod(serviceURL);
        int statuscode = client.executeMethod(put);
        Header[] headers;
        headers = put.getResponseHeaders();
        String locationHeader = null;
        for (Header header : headers) {
            if (header.getName().equalsIgnoreCase("Location")) {
                locationHeader = header.getValue();
            }
        }

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_TEMPORARY_REDIRECT);
        assertNotNull(headers);
        assertNotNull(locationHeader);
    }

    @Test
    public void testOutbound300ForDelete() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/batch/300";

        DeleteMethod delete = new DeleteMethod(serviceURL);
        int statuscode = client.executeMethod(delete);
        Header[] headers;
        headers = delete.getResponseHeaders();
        String locationHeader = null;
        for (Header header : headers) {
            if (header.getName().equalsIgnoreCase("Location")) {
                locationHeader = header.getValue();
            }
        }

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_MULTIPLE_CHOICES);
        assertNotNull(headers);
        assertNotNull(locationHeader);
    }

    @Test
    public void testOutbound301ForDelete() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/batch/301";

        DeleteMethod delete = new DeleteMethod(serviceURL);
        int statuscode = client.executeMethod(delete);
        Header[] headers;
        headers = delete.getResponseHeaders();
        String locationHeader = null;
        for (Header header : headers) {
            if (header.getName().equalsIgnoreCase("Location")) {
                locationHeader = header.getValue();
            }
        }

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_MOVED_PERMANENTLY);
        assertNotNull(headers);
        assertNotNull(locationHeader);
    }

    @Test
    public void testOutbound302ForDelete() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/batch/302";

        DeleteMethod delete = new DeleteMethod(serviceURL);
        int statuscode = client.executeMethod(delete);
        Header[] headers;
        headers = delete.getResponseHeaders();
        String locationHeader = null;
        for (Header header : headers) {
            if (header.getName().equalsIgnoreCase("Location")) {
                locationHeader = header.getValue();
            }
        }

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_MOVED_TEMPORARILY);
        assertNotNull(headers);
        assertNotNull(locationHeader);
    }

    @Test
    public void testOutbound303ForDelete() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/batch/303";

        DeleteMethod delete = new DeleteMethod(serviceURL);
        int statuscode = client.executeMethod(delete);
        Header[] headers;
        headers = delete.getResponseHeaders();
        String locationHeader = null;
        for (Header header : headers) {
            if (header.getName().equalsIgnoreCase("Location")) {
                locationHeader = header.getValue();
            }
        }

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_SEE_OTHER);
        assertNotNull(headers);
        assertNotNull(locationHeader);
    }

    @Test
    public void testOutbound305ForDelete() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/batch/305";

        DeleteMethod delete = new DeleteMethod(serviceURL);
        int statuscode = client.executeMethod(delete);
        Header[] headers;
        headers = delete.getResponseHeaders();
        String locationHeader = null;
        for (Header header : headers) {
            if (header.getName().equalsIgnoreCase("Location")) {
                locationHeader = header.getValue();
            }
        }

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_USE_PROXY);
        assertNotNull(headers);
        assertNotNull(locationHeader);
    }

    @Test
    public void testOutbound307ForDelete() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/batch/307";

        DeleteMethod delete = new DeleteMethod(serviceURL);
        int statuscode = client.executeMethod(delete);
        Header[] headers;
        headers = delete.getResponseHeaders();
        String locationHeader = null;
        for (Header header : headers) {
            if (header.getName().equalsIgnoreCase("Location")) {
                locationHeader = header.getValue();
            }
        }

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_TEMPORARY_REDIRECT);
        assertNotNull(headers);
        assertNotNull(locationHeader);
    }

    @Test
    public void testOutbound300ForHead() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/batch/300";

        HeadMethod head = new HeadMethod(serviceURL);
        int statuscode = client.executeMethod(head);
        Header[] headers;
        headers = head.getResponseHeaders();
        String locationHeader = null;
        for (Header header : headers) {
            if (header.getName().equalsIgnoreCase("Location")) {
                locationHeader = header.getValue();
            }
        }

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_MULTIPLE_CHOICES);
        assertNotNull(headers);
        assertNotNull(locationHeader);
    }

    @Test
    public void testOutbound301ForHead() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/batch/301";

        HeadMethod head = new HeadMethod(serviceURL);
        int statuscode = client.executeMethod(head);
        Header[] headers;
        headers = head.getResponseHeaders();
        String locationHeader = null;
        for (Header header : headers) {
            if (header.getName().equalsIgnoreCase("Location")) {
                locationHeader = header.getValue();
            }
        }

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_MOVED_PERMANENTLY);
        assertNotNull(headers);
        assertNotNull(locationHeader);
    }

    @Test
    public void testOutbound302ForHead() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/batch/302";

        HeadMethod head = new HeadMethod(serviceURL);
        int statuscode = client.executeMethod(head);
        Header[] headers;
        headers = head.getResponseHeaders();
        String locationHeader = null;
        for (Header header : headers) {
            if (header.getName().equalsIgnoreCase("Location")) {
                locationHeader = header.getValue();
            }
        }

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_MOVED_TEMPORARILY);
        assertNotNull(headers);
        assertNotNull(locationHeader);
    }

    @Test
    public void testOutbound303ForHead() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/batch/303";

        HeadMethod head = new HeadMethod(serviceURL);
        int statuscode = client.executeMethod(head);
        Header[] headers;
        headers = head.getResponseHeaders();
        String locationHeader = null;
        for (Header header : headers) {
            if (header.getName().equalsIgnoreCase("Location")) {
                locationHeader = header.getValue();
            }
        }

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_SEE_OTHER);
        assertNotNull(headers);
        assertNotNull(locationHeader);
    }

    @Test
    public void testOutbound305ForHead() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/batch/305";

        HeadMethod head = new HeadMethod(serviceURL);
        int statuscode = client.executeMethod(head);
        Header[] headers;
        headers = head.getResponseHeaders();
        String locationHeader = null;
        for (Header header : headers) {
            if (header.getName().equalsIgnoreCase("Location")) {
                locationHeader = header.getValue();
            }
        }

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_USE_PROXY);
        assertNotNull(headers);
        assertNotNull(locationHeader);
    }

    @Test
    public void testOutbound307ForHead() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/batch/307";

        HeadMethod head = new HeadMethod(serviceURL);
        int statuscode = client.executeMethod(head);
        Header[] headers;
        headers = head.getResponseHeaders();
        String locationHeader = null;
        for (Header header : headers) {
            if (header.getName().equalsIgnoreCase("Location")) {
                locationHeader = header.getValue();
            }
        }

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_TEMPORARY_REDIRECT);
        assertNotNull(headers);
        assertNotNull(locationHeader);
    }

    @Test
    public void testOutbound400ForPost() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/order/ee1a9ec2-c8a5-4afe-8585-74df591f9990";

        PostMethod post = new PostMethod(serviceURL);
        post.setRequestHeader("Content-Type", "application/xml");
        StringRequestEntity requestEntity = new StringRequestEntity("{ \"Hello\":\"Ballerina\" };", "application/json",
                "UTF-8");
        post.setRequestEntity(requestEntity);
        int statuscode = client.executeMethod(post);

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void testOutbound400ForPut() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/order/ee1a9ec2-c8a5-4afe-8585-74df591f9990";

        PutMethod put = new PutMethod(serviceURL);
        put.setRequestHeader("Content-Type", "application/xml");
        StringRequestEntity requestEntity = new StringRequestEntity("{ \"Hello\":\"Ballerina\" };", "application/json",
                "UTF-8");
        put.setRequestEntity(requestEntity);
        int statuscode = client.executeMethod(put);

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void testOutbound403ForGet() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/order/ee1a9ec2-c8a5-4afe-8585-74df591f9990";

        GetMethod get = new GetMethod(serviceURL);
        get.setRequestHeader("Authorization", "YWRtaW46YWRtaW4=");
        get.setRequestHeader("Content-Length", "0");
        int statuscode = client.executeMethod(get);

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_FORBIDDEN);
    }

    @Test
    public void testOutbound403ForPost() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/order/ee1a9ec2-c8a5-4afe-8585-74df591f9990";

        PostMethod post = new PostMethod(serviceURL);
        post.setRequestHeader("Authorization", "YWRtaW46YWRtaW4=");
        int statuscode = client.executeMethod(post);

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_FORBIDDEN);
    }

    @Test
    public void testOutbound403ForPut() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/order/ee1a9ec2-c8a5-4afe-8585-74df591f9990";

        PutMethod put = new PutMethod(serviceURL);
        put.setRequestHeader("Authorization", "YWRtaW46YWRtaW4=");
        int statuscode = client.executeMethod(put);

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_FORBIDDEN);
    }

    @Test
    public void testOutbound403ForHead() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/order/ee1a9ec2-c8a5-4afe-8585-74df591f9990";

        HeadMethod head = new HeadMethod(serviceURL);
        head.setRequestHeader("Authorization", "YWRtaW46YWRtaW4=");
        head.setRequestHeader("Content-Length", "0");
        int statuscode = client.executeMethod(head);

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_FORBIDDEN);
    }

    @Test
    public void testOutbound406ForGet() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/order/123x";

        GetMethod get = new GetMethod(serviceURL);
        get.setRequestHeader("Authorization", "YWRtaW46YWRtaW45=");
        get.setRequestHeader("Content-Length", "0");
        int statuscode = client.executeMethod(get);

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_NOT_ACCEPTABLE);
    }

    @Test
    public void testOutbound406ForPost() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/order/123x";

        PostMethod post = new PostMethod(serviceURL);
        post.setRequestHeader("Authorization", "YWRtaW46YWRtaW45=");
        int statuscode = client.executeMethod(post);

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_NOT_ACCEPTABLE);
    }

    @Test
    public void testOutbound406ForPut() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/order/123x";

        PutMethod put = new PutMethod(serviceURL);
        put.setRequestHeader("Authorization", "YWRtaW46YWRtaW45=");
        int statuscode = client.executeMethod(put);

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_NOT_ACCEPTABLE);
    }

    @Test
    public void testOutbound406ForHead() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/order/123x";

        HeadMethod head = new HeadMethod(serviceURL);
        head.setRequestHeader("Authorization", "YWRtaW46YWRtaW45=");
        head.setRequestHeader("Content-Length", "0");
        int statuscode = client.executeMethod(head);

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_NOT_ACCEPTABLE);
    }

    @Test
    public void testOutbound409ForGet() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/order/123xy";

        GetMethod get = new GetMethod(serviceURL);
        get.setRequestHeader("Authorization", "YWRtaW46YWRtaW45=");
        get.setRequestHeader("Content-Length", "0");
        int statuscode = client.executeMethod(get);
        byte[] response = get.getResponseBody();

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_CONFLICT);
        assertEquals(new String(response), "Resource conflicted. Please try again");
    }

    @Test
    public void testOutbound409ForPost() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/order/123xy";

        PostMethod post = new PostMethod(serviceURL);
        post.setRequestHeader("Authorization", "YWRtaW46YWRtaW45=");
        post.setRequestHeader("Content-Type", "application/json");
        StringRequestEntity requestEntity = new StringRequestEntity("{ \"Hello\":\"Ballerina\" };", "application/json",
                "UTF-8");
        post.setRequestEntity(requestEntity);
        int statuscode = client.executeMethod(post);
        byte[] response = post.getResponseBody();

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_CONFLICT);
        assertEquals(new String(response), "Resource conflicted. Please try again");
    }

    @Test
    public void testOutbound409ForPut() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/order/123xy";

        PutMethod put = new PutMethod(serviceURL);
        put.setRequestHeader("Authorization", "YWRtaW46YWRtaW45=");
        put.setRequestHeader("Content-Type", "application/json");
        StringRequestEntity requestEntity = new StringRequestEntity("{ \"Hello\":\"Ballerina\" };", "application/json",
                "UTF-8");
        put.setRequestEntity(requestEntity);
        int statuscode = client.executeMethod(put);
        byte[] response = put.getResponseBody();

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_CONFLICT);
        assertEquals(new String(response), "Resource conflicted. Please try again");
    }

    @Test
    public void testOutbound409ForHead() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/order/123xy";

        HeadMethod head = new HeadMethod(serviceURL);
        head.setRequestHeader("Authorization", "YWRtaW46YWRtaW45=");
        head.setRequestHeader("Content-Length", "0");
        int statuscode = client.executeMethod(head);

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_CONFLICT);
    }

    @Test
    public void testOutbound410ForGet() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/order/123ab";

        GetMethod get = new GetMethod(serviceURL);
        get.setRequestHeader("Authorization", "YWRtaW46YWRtaW45=");
        get.setRequestHeader("Content-Length", "0");
        int statuscode = client.executeMethod(get);
        byte[] response = get.getResponseBody();

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_GONE);
        assertEquals(new String(response), "Resource Gone. Please try again");
    }

    @Test
    public void testOutbound410ForPost() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/order/123ab";

        PostMethod post = new PostMethod(serviceURL);
        post.setRequestHeader("Authorization", "YWRtaW46YWRtaW45=");
        post.setRequestHeader("Content-Type", "application/json");
        StringRequestEntity requestEntity = new StringRequestEntity("{ \"Hello\":\"Ballerina\" };", "application/json",
                "UTF-8");
        post.setRequestEntity(requestEntity);
        int statuscode = client.executeMethod(post);
        byte[] response = post.getResponseBody();

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_GONE);
        assertEquals(new String(response), "Resource Gone. Please try again");
    }

    @Test
    public void testOutbound410ForPut() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/order/123ab";

        PutMethod put = new PutMethod(serviceURL);
        put.setRequestHeader("Authorization", "YWRtaW46YWRtaW45=");
        put.setRequestHeader("Content-Type", "application/json");
        StringRequestEntity requestEntity = new StringRequestEntity("{ \"Hello\":\"Ballerina\" };", "application/json",
                "UTF-8");
        put.setRequestEntity(requestEntity);
        int statuscode = client.executeMethod(put);
        byte[] response = put.getResponseBody();

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_GONE);
        assertEquals(new String(response), "Resource Gone. Please try again");
    }

    @Test
    public void testOutbound410ForHead() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/order/123ab";

        HeadMethod head = new HeadMethod(serviceURL);
        head.setRequestHeader("Authorization", "YWRtaW46YWRtaW45=");
        head.setRequestHeader("Content-Length", "0");
        int statuscode = client.executeMethod(head);

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_GONE);
    }

    @Test
    public void testOutbound411ForPost() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/order/ee1a9ec2-c8a5-4afe-8585-74df591f9990";

        PostMethod post = new PostMethod(serviceURL);
        post.setRequestHeader("Authorization", "YWRtaW46YWRtaW45=");
        post.setRequestHeader("Content-Length", "");
        int statuscode = client.executeMethod(post);
        byte[] response = post.getResponseBody();

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_LENGTH_REQUIRED);
        assertEquals(new String(response), "Content length missing. Please try again");
    }

    @Test
    public void testOutbound411ForPut() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/order/ee1a9ec2-c8a5-4afe-8585-74df591f9990";

        PutMethod put = new PutMethod(serviceURL);
        put.setRequestHeader("Authorization", "YWRtaW46YWRtaW45=");
        put.setRequestHeader("Content-Length", "");
        int statuscode = client.executeMethod(put);
        byte[] response = put.getResponseBody();

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_LENGTH_REQUIRED);
        assertEquals(new String(response), "Content length missing. Please try again");
    }

    @Test
    public void testOutbound411ForDelete() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/order/ee1a9ec2-c8a5-4afe-8585-74df591f9990";

        DeleteMethod delete = new DeleteMethod(serviceURL);
        delete.setRequestHeader("Authorization", "YWRtaW46YWRtaW45=");
        delete.setRequestHeader("Content-Length", "");
        int statuscode = client.executeMethod(delete);
        byte[] response = delete.getResponseBody();

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_LENGTH_REQUIRED);
        assertEquals(new String(response), "Content length missing. Please try again");
    }

    @Test
    public void testOutbound413ForPost() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/order/ee1a9ec2-c8a5-4afe-8585-74df591f9990";

        PostMethod post = new PostMethod(serviceURL);
        post.setRequestHeader("Authorization", "YWRtaW46YWRtaW45=");
        post.setRequestHeader("Content-Type", "application/json");
        StringRequestEntity requestEntity = new StringRequestEntity("{ \"Hello\":\"BallerinaTestPayloadFor413\" };"
                , "application/json",
                "UTF-8");
        post.setRequestEntity(requestEntity);
        int statuscode = client.executeMethod(post);
        byte[] response = post.getResponseBody();
        Header[] headers;
        headers = post.getResponseHeaders();
        String retryAfterHeader = null;
        for (Header header : headers) {
            if (header.getName().equalsIgnoreCase("Retry-After")) {
                retryAfterHeader = header.getValue();
            }
        }

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_REQUEST_TOO_LONG);
        assertEquals(new String(response), "Content is too large. Please try again");
        assertNotNull(retryAfterHeader);
    }

    @Test
    public void testOutbound413ForPut() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/order/ee1a9ec2-c8a5-4afe-8585-74df591f9990";

        PutMethod put = new PutMethod(serviceURL);
        put.setRequestHeader("Authorization", "YWRtaW46YWRtaW45=");
        put.setRequestHeader("Content-Type", "application/json");
        StringRequestEntity requestEntity = new StringRequestEntity("{ \"Hello\":\"BallerinaTestPayloadFor413\" };"
                , "application/json",
                "UTF-8");
        put.setRequestEntity(requestEntity);
        int statuscode = client.executeMethod(put);
        byte[] response = put.getResponseBody();
        Header[] headers;
        headers = put.getResponseHeaders();
        String retryAfterHeader = null;
        for (Header header : headers) {
            if (header.getName().equalsIgnoreCase("Retry-After")) {
                retryAfterHeader = header.getValue();
            }
        }

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_REQUEST_TOO_LONG);
        assertEquals(new String(response), "Content is too large. Please try again");
        assertNotNull(retryAfterHeader);
    }

    @Test
    public void testOutbound404ForGet() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/error";

        GetMethod get = new GetMethod(serviceURL);
        get.setRequestHeader("Authorization", "YWRtaW46YWRtaW45=");
        int statuscode = client.executeMethod(get);

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void testOutbound404ForPost() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/error";

        PostMethod post = new PostMethod(serviceURL);
        post.setRequestHeader("Authorization", "YWRtaW46YWRtaW45=");
        post.setRequestHeader("Content-Type", "application/json");
        StringRequestEntity requestEntity = new StringRequestEntity("{ \"Hello\":\"BallerinaTestPayloadFor413\" };"
                , "application/json",
                "UTF-8");
        post.setRequestEntity(requestEntity);
        int statuscode = client.executeMethod(post);

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void testOutbound404ForPut() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/error";

        PutMethod put = new PutMethod(serviceURL);
        put.setRequestHeader("Authorization", "YWRtaW46YWRtaW45=");
        put.setRequestHeader("Content-Type", "application/json");
        StringRequestEntity requestEntity = new StringRequestEntity("{ \"Hello\":\"BallerinaTestPayloadFor413\" };"
                , "application/json",
                "UTF-8");
        put.setRequestEntity(requestEntity);
        int statuscode = client.executeMethod(put);

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void testOutbound404ForHead() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/error";

        HeadMethod head = new HeadMethod(serviceURL);
        head.setRequestHeader("Authorization", "YWRtaW46YWRtaW45=");
        int statuscode = client.executeMethod(head);

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void testOutbound405ForGet() throws Exception {
        String serviceURL = ballerinaURL + "/http/orders/er-567-567";

        GetMethod get = new GetMethod(serviceURL);
        int statuscode = client.executeMethod(get);

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_METHOD_NOT_ALLOWED);
    }

    @Test
    public void testOutbound404ForDelete() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/error";

        DeleteMethod delete = new DeleteMethod(serviceURL);
        delete.setRequestHeader("Authorization", "YWRtaW46YWRtaW45=");
        int statuscode = client.executeMethod(delete);

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void testOutbound500ForGet() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/new";

        GetMethod get = new GetMethod(serviceURL);
        int statuscode = client.executeMethod(get);

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    public void testOutbound500ForPost() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/new";

        PostMethod post = new PostMethod(serviceURL);
        post.setRequestHeader("Content-Type", "application/json");
        StringRequestEntity requestEntity = new StringRequestEntity("{ \"Hello\":\"BallerinaTestPayloadFor413\" };"
                , "application/json",
                "UTF-8");
        post.setRequestEntity(requestEntity);
        int statuscode = client.executeMethod(post);

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    public void testOutbound500ForPut() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/new";

        PutMethod put = new PutMethod(serviceURL);
        put.setRequestHeader("Content-Type", "application/json");
        StringRequestEntity requestEntity = new StringRequestEntity("{ \"Hello\":\"BallerinaTestPayloadFor413\" };"
                , "application/json",
                "UTF-8");
        put.setRequestEntity(requestEntity);
        int statuscode = client.executeMethod(put);

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    public void testOutbound500ForDelete() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/new";

        DeleteMethod delete = new DeleteMethod(serviceURL);
        int statuscode = client.executeMethod(delete);

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    public void testOutbound500ForHead() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/new";

        HeadMethod head = new HeadMethod(serviceURL);
        int statuscode = client.executeMethod(head);

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    public void testOutbound501ForGet() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/notimplemented";

        GetMethod get = new GetMethod(serviceURL);
        int statuscode = client.executeMethod(get);

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_NOT_IMPLEMENTED);
    }

    @Test
    public void testOutbound501ForPost() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/notimplemented";

        PostMethod post = new PostMethod(serviceURL);
        post.setRequestHeader("Content-Type", "application/json");
        StringRequestEntity requestEntity = new StringRequestEntity("{ \"Hello\":\"BallerinaTestPayloadFor413\" };"
                , "application/json",
                "UTF-8");
        post.setRequestEntity(requestEntity);
        int statuscode = client.executeMethod(post);

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_NOT_IMPLEMENTED);
    }

    @Test
    public void testOutbound501ForPut() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/notimplemented";

        PutMethod put = new PutMethod(serviceURL);
        put.setRequestHeader("Content-Type", "application/json");
        StringRequestEntity requestEntity = new StringRequestEntity("{ \"Hello\":\"BallerinaTestPayloadFor413\" };"
                , "application/json",
                "UTF-8");
        put.setRequestEntity(requestEntity);
        int statuscode = client.executeMethod(put);

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_NOT_IMPLEMENTED);
    }

    @Test
    public void testOutbound501ForDelete() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/notimplemented";

        DeleteMethod delete = new DeleteMethod(serviceURL);
        int statuscode = client.executeMethod(delete);

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_NOT_IMPLEMENTED);
    }

    @Test
    public void testOutbound501ForHead() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/notimplemented";

        HeadMethod head = new HeadMethod(serviceURL);
        int statuscode = client.executeMethod(head);

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_NOT_IMPLEMENTED);
    }

    @Test
    public void testOutbound503ForGet() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/unavailable";

        GetMethod get = new GetMethod(serviceURL);
        int statuscode = client.executeMethod(get);

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_SERVICE_UNAVAILABLE);
    }

    @Test
    public void testOutbound503ForPost() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/unavailable";

        PostMethod post = new PostMethod(serviceURL);
        post.setRequestHeader("Content-Type", "application/json");
        StringRequestEntity requestEntity = new StringRequestEntity("{ \"Hello\":\"BallerinaTestPayloadFor413\" };"
                , "application/json",
                "UTF-8");
        post.setRequestEntity(requestEntity);
        int statuscode = client.executeMethod(post);

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_SERVICE_UNAVAILABLE);
    }

    @Test
    public void testOutbound503ForPut() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/unavailable";

        PutMethod put = new PutMethod(serviceURL);
        put.setRequestHeader("Content-Type", "application/json");
        StringRequestEntity requestEntity = new StringRequestEntity("{ \"Hello\":\"BallerinaTestPayloadFor413\" };"
                , "application/json",
                "UTF-8");
        put.setRequestEntity(requestEntity);
        int statuscode = client.executeMethod(put);

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_SERVICE_UNAVAILABLE);
    }

    @Test
    public void testOutbound503ForDelete() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/unavailable";

        DeleteMethod delete = new DeleteMethod(serviceURL);
        int statuscode = client.executeMethod(delete);

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_SERVICE_UNAVAILABLE);
    }

    @Test
    public void testOutbound503ForHead() throws Exception {
        String serviceURL = ballerinaURL + "/http/call/unavailable";

        HeadMethod head = new HeadMethod(serviceURL);
        int statuscode = client.executeMethod(head);

        // Asserting the Response Message and headers.
        assertEquals(statuscode, HttpStatus.SC_SERVICE_UNAVAILABLE);
    }

}

