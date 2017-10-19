package resources.services;

import ballerina.net.http;
import ballerina.lang.strings;
import ballerina.lang.system;

@http:configuration {
    basePath:"/http"
}
service <http> ServerConnectorService {

   @http:resourceConfig {
        methods:["POST", "GET", "HEAD", "PUT", "DELETE"],
        path:"/call"
    }
    resource getMenuDetailsResource (http:Request req, http:Response res) {
        string payload = "This is a test payload";
        res.setStringPayload(payload);

        res.send();
    }

    @http:resourceConfig {
        methods:["POST", "GET", "HEAD", "PUT", "DELETE"],
        path:"/call/batch"
    }
    resource batchProcessingResource(http:Request req, http:Response res){
        int statusCode = 202;
        res.setStatusCode(statusCode);
        string payload = "This is a test payload";
        res.setStringPayload(payload);

        res.send();
    }

    @http:resourceConfig {
        methods:["POST", "GET", "HEAD", "PUT", "DELETE"],
        path:"/call/changed"
    }
    resource getModifiedMenuDetailsResource(http:Request req, http:Response res){
        int statusCode = 203;

        string payload = "This is a test payload";
        res.setStringPayload(payload);
        res.setStatusCode(statusCode);
        res.send();
    }

    @http:resourceConfig {
        methods:["POST", "PUT"],
        path:"/orders/{orderid}"
    }
    resource createOrderResource(http:Request req, http:Response res, string orderId){
        int statusCode = 201;
        string location = "/orders/" + orderId;

        res.setStatusCode(statusCode);
        res.setHeader("Location", location);
        res.setHeader("ETag", "errtyui"+orderId);

        res.send();
    }

    @http:resourceConfig {
        methods:["DELETE", "PUT"],
        path:"/orders/update/{orderid}"
    }
    resource updateOrderResource(http:Request req, http:Response res, string orderId){
        int statusCode = 204;
        string location = "/orders/" + orderId;

        res.setStatusCode(statusCode);
        res.setHeader("Location", location);
        res.setHeader("ETag", "errtyui"+orderId);

        res.send();
    }

    @http:resourceConfig {
        methods:["POST", "GET", "HEAD", "PUT", "DELETE"],
        path:"/call/batch/reset"
    }
    resource batchProcessingResetResource(http:Request req, http:Response res){
        int statusCode = 205;
        res.setStatusCode(statusCode);
        res.send();
    }


    @http:resourceConfig {
        methods:["POST", "GET", "HEAD", "PUT", "DELETE"],
        path:"/call/batch/{code}"
    }
    resource batchProcessingRedirectionResource(http:Request req, http:Response res, string code){
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

        res.setStatusCode(statusCode);
        res.setHeader("Location", "https://jsonplaceholder.typicode.com/users");
        res.send();
    }

    @http:resourceConfig {
        methods:["POST", "GET", "HEAD", "PUT", "DELETE"],
        path:"/call/order/{orderid}"
    }
    resource getOrderDetailsResource (http:Request req, http:Response res, string orderid) {
        TypeConversionError err;
        int cLength;

        string auth = req.getHeader("Authorization");
        string length = req.getHeader("Content-Length");
        cLength, err = <int>length;

        if(strings:equalsIgnoreCase(auth, "YWRtaW46YWRtaW4=")){
            int statusCode = 403;
            res.setStatusCode(statusCode);
        }
        if(strings:equalsIgnoreCase(orderid, "123x")){
            int statusCode = 406;
            res.setStatusCode(statusCode);
        }
        if(strings:equalsIgnoreCase(orderid, "123xy")){
            int statusCode = 409;
            res.setStatusCode(statusCode);
            string sPayload = "Resource conflicted. Please try again";
            res.setStringPayload(sPayload);
        }
        if(strings:equalsIgnoreCase(orderid, "123ab")){
            int statusCode = 410;
            res.setStatusCode(statusCode);
            string sPayload = "Resource Gone. Please try again";
            res.setStringPayload(sPayload);
        }
        if(strings:equalsIgnoreCase(length, "")){
            int statusCode = 411;
            res.setStatusCode(statusCode);
            string sPayload = "Content length missing. Please try again";
            res.setStringPayload(sPayload);
        }
        if(cLength > 4){
            int statusCode = 413;
            res.setStatusCode(statusCode);
            res.setHeader("Retry-After", "3600");
            string sPayload = "Content is too large. Please try again";
            res.setStringPayload(sPayload);
        }
        res.send();
    }

    @http:resourceConfig {
        methods:["POST", "GET", "HEAD", "PUT", "DELETE"],
        path:"/call/new"
    }
    resource getNewMenuDetailsResource (http:Request req, http:Response res) {
        string expect = req.getHeader("Expect");
        system:println(expect);
        if(!strings:equalsIgnoreCase(expect, "100-continue")){
            system:println("a");
            int statusCode = 417;
            res.setStatusCode(statusCode);
        }
        res.send();
    }

    @http:resourceConfig {
        methods:["POST", "GET", "HEAD", "PUT", "DELETE"],
        path:"/call/notimplemented"
    }
    resource workInprogressResource (http:Request req, http:Response res) {
        int statusCode = 501;
        res.setStatusCode(statusCode);
        res.send();
    }

    @http:resourceConfig {
        methods:["POST", "GET", "HEAD", "PUT", "DELETE"],
        path:"/call/unavailable"
    }
    resource serviceUnavailableResource (http:Request req, http:Response res) {
        int statusCode = 503;
        res.setStatusCode(statusCode);
        res.send();
    }

}




