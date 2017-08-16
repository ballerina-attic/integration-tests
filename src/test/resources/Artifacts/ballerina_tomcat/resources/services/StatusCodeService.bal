package resources.services;

import ballerina.net.http;
import ballerina.lang.system;

@http:configuration {
    basePath:"/statuscode"
}
service <http> StatusCodeService {

    @http:resourceConfig {
        methods:["POST", "GET"],
        path:"/code/{code}"
    }
    resource statusCodeResource (message m, @http:PathParam {value:"code"} string codeValue, @http:QueryParam {value:"withbody"}string withbody) {
        string resourcePath = "/RESTfulService/mock/statusCodeService/" + codeValue + "?withbody=" + withbody;
        message response = {};
        string method = http:getMethod(m);
        string connection = system:getEnv("TOMCAT_HOST");

        http:ClientConnector httpCheck = create http:ClientConnector(connection);
        response = httpCheck.execute(method, resourcePath, m);

        reply response;
    }

    @http:resourceConfig {
        methods:["HEAD"],
        path:"/code/{code}"
    }
    resource statusCodeResource2 (message m, @http:PathParam {value:"code"} string codeValue) {
        string resourcePath = "/RESTfulService/mock/statusCodeService/" + codeValue;
        message response = {};
        string method = http:getMethod(m);
        string connection = system:getEnv("TOMCAT_HOST");

        http:ClientConnector httpCheck = create http:ClientConnector(connection);
        response = httpCheck.execute(method, resourcePath, m);

        reply response;
    }
}