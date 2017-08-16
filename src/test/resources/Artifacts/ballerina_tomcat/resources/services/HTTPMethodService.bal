package resources.services;
import ballerina.lang.messages;

import ballerina.net.http;
import ballerina.lang.system;

@http:configuration {
    basePath:"/httpmethods"
}
service <http> HTTPMethodService {


    @http:resourceConfig {
        methods:["POST", "GET", "HEAD", "PUT", "DELETE"],
        path:"/method/all"
    }
    resource statusCodeResource (message m) {
        string resourcePath = "/RESTfulService/mock/statusCodeService/";
        message response = {};
        string method = http:getMethod(m);
        string connection = system:getEnv("TOMCAT_HOST");

        http:ClientConnector httpCheck = create http:ClientConnector(connection);
        response = httpCheck.execute(method, resourcePath, m);

        reply response;
    }

    @http:resourceConfig {
        methods:["GET"],
        path:"/method/get/range"
    }
    resource statusCodeResource2 (message m) {
        message response = {};
        messages:setStringPayload(response, "abcdefghijklmnopqrstuvxyz");

        reply response;
    }

    @http:resourceConfig {
        methods:["GET"],
        path:"/gettopost1"
    }
    resource getResource (message m) {
        string resourcePath = "/RESTfulService/mock/service/post";
        message response = {};
        string method = "POST";
        string connection = system:getEnv("TOMCAT_HOST");

        http:ClientConnector httpCheck = create http:ClientConnector(connection);
        response = httpCheck.execute(method, resourcePath, m);

        reply response;
    }

    @http:resourceConfig {
        methods:["POST"],
        path:"/gettopost1"
    }
    resource postResource (message m) {
        string resourcePath = "/RESTfulService/mock/service/put";
        message response = {};
        string method = "PUT";
        string connection = system:getEnv("TOMCAT_HOST");

        http:ClientConnector httpCheck = create http:ClientConnector(connection);
        response = httpCheck.execute(method, resourcePath, m);

        reply response;
    }
}