package resources.services;

import ballerina.net.http;
import ballerina.lang.messages;
import ballerina.lang.strings;
import ballerina.lang.system;
import ballerina.lang.errors;

@http:configuration {
    basePath:"/http"
}
service <http> ServerConnectorService {

    @http:GET {}
    @http:POST {}
    @http:HEAD {}
    @http:PUT {}
    @http:DELETE {}
    @http:Path {value:"/call"}
    resource getMenuDetailsResource (message m) {
        message response = {};

        string payload = "This is a test payload";
        messages:setStringPayload(response, payload);

        reply response;
    }

    @http:GET {}
    @http:POST {}
    @http:HEAD {}
    @http:PUT {}
    @http:DELETE {}
    @http:Path {value:"/call/batch"}
    resource batchProcessingResource(message m){
        int statusCode = 202;
        message response = {};

        http:setStatusCode(response, statusCode);
        string payload = "This is a test payload";
        messages:setStringPayload(response, payload);

        reply response;
    }

    @http:GET {}
    @http:POST {}
    @http:HEAD {}
    @http:PUT {}
    @http:DELETE {}
    @http:Path {value:"/call/changed"}
    resource getModifiedMenuDetailsResource(message m){
        int statusCode = 203;
        message response = {};

        string payload = "This is a test payload";
        messages:setStringPayload(response, payload);
        http:setStatusCode(response, statusCode);
        reply response;
    }

    @http:POST {}
    @http:PUT {}
    @http:Path {value:"/orders/{orderid}"}
    resource createOrderResource(message m, @http:PathParam {value:"orderid"} string orderId){
        message response = {};
        int statusCode = 201;
        string location = "/orders/" + orderId;

        http:setStatusCode(response, statusCode);
        messages:setHeader(response, "Location", location);
        messages:setHeader(response, "ETag", "errtyui"+orderId);

        reply response;
    }

    @http:PUT {}
    @http:DELETE {}
    @http:Path {value:"/orders/update/{orderid}"}
    resource updateOrderResource(message m, @http:PathParam {value:"orderid"} string orderId){
        message response = {};
        int statusCode = 204;
        string location = "/orders/" + orderId;

        http:setStatusCode(response, statusCode);
        messages:setHeader(response, "Location", location);
        messages:setHeader(response, "ETag", "errtyui"+orderId);

        reply response;
    }

    @http:GET {}
    @http:POST {}
    @http:HEAD {}
    @http:PUT {}
    @http:DELETE {}
    @http:Path {value:"/call/batch/reset"}
    resource batchProcessingResetResource(message m){
        int statusCode = 205;
        message response = {};

        http:setStatusCode(response, statusCode);
        reply response;
    }

    @http:GET {}
    @http:POST {}
    @http:HEAD {}
    @http:PUT {}
    @http:DELETE {}
    @http:Path {value:"/call/batch/{code}"}
    resource batchProcessingRedirectionResource(message m, @http:PathParam {value:"code"} string code){
        message response = {};
        int statusCode;

        if(strings:equalsIgnoreCase(code, "300")){
            statusCode = 300;
        }
        else if (strings:equalsIgnoreCase(code, "301")){
            system:println(code);
            statusCode = 301;
        }
        else if (strings:equalsIgnoreCase(code, "302")){
            statusCode = 302;
        }
        else if (strings:equalsIgnoreCase(code, "303")){
            statusCode = 303;
        }
        else if (strings:equalsIgnoreCase(code, "305")){
            statusCode = 305;
        }
        else if (strings:equalsIgnoreCase(code, "307")){
            statusCode = 307;
        }

        http:setStatusCode(response, statusCode);
        messages:setHeader(response, "Location", "http://www.google.com");
        reply response;
    }

    @http:GET {}
    @http:POST {}
    @http:HEAD {}
    @http:PUT {}
    @http:DELETE {}
    @http:Path {value:"/call/order/{orderid}"}
    resource getOrderDetailsResource (message m, @http:PathParam {value:"orderid"} string orderid) {
        message response = {};
        errors:TypeConversionError err;
        int cLength;

        string auth = messages:getHeader(m, "Authorization");
        string length = messages:getHeader(m, "Content-Length");
        cLength, err = <int>length;

        if(strings:equalsIgnoreCase(auth, "YWRtaW46YWRtaW4=")){
            int statusCode = 403;
            http:setStatusCode(response, statusCode);
        }
        if(strings:equalsIgnoreCase(orderid, "123x")){
            int statusCode = 406;
            http:setStatusCode(response, statusCode);
        }
        if(strings:equalsIgnoreCase(orderid, "123xy")){
            int statusCode = 409;
            http:setStatusCode(response, statusCode);
            string sPayload = "Resource conflicted. Please try again";
            messages:setStringPayload(response, sPayload);
        }
        if(strings:equalsIgnoreCase(orderid, "123ab")){
            int statusCode = 410;
            http:setStatusCode(response, statusCode);
            string sPayload = "Resource Gone. Please try again";
            messages:setStringPayload(response, sPayload);
        }
        if(strings:equalsIgnoreCase(length, "")){
            int statusCode = 411;
            http:setStatusCode(response, statusCode);
            string sPayload = "Content length missing. Please try again";
            messages:setStringPayload(response, sPayload);
        }
        if(cLength > 4){
            int statusCode = 413;
            http:setStatusCode(response, statusCode);
            messages:setHeader(response, "Retry-After", "3600");
            string sPayload = "Content is too large. Please try again";
            messages:setStringPayload(response, sPayload);
        }
        reply response;
    }

    @http:GET {}
    @http:POST {}
    @http:HEAD {}
    @http:PUT {}
    @http:DELETE {}
    @http:Path {value:"/call/new"}
    resource getNewMenuDetailsResource (message m) {
        message response = {};

        string expect = messages:getHeader(m, "Expect");
        system:println(expect);
        if(!strings:equalsIgnoreCase(expect, "100-continue")){
            system:println("a");
            int statusCode = 417;
            http:setStatusCode(response, statusCode);
        }
        reply response;
    }

    @http:GET {}
    @http:POST {}
    @http:HEAD {}
    @http:PUT {}
    @http:DELETE {}
    @http:Path {value:"/call/notimplemented"}
    resource workInprogressResource (message m) {
        message response = {};

        int statusCode = 501;
        http:setStatusCode(response, statusCode);
        reply response;
    }

    @http:GET {}
    @http:POST {}
    @http:HEAD {}
    @http:PUT {}
    @http:DELETE {}
    @http:Path {value:"/call/unavailable"}
    resource serviceUnavailableResource (message m) {
        message response = {};

        int statusCode = 503;
        http:setStatusCode(response, statusCode);
        reply response;
    }

}




