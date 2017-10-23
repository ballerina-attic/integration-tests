package resources.services;

import ballerina.net.http;
import ballerina.lang.system;

@http:configuration {
    basePath:"/statuscode"
}
service <http> StatusCodeService {

    string connection = system:getEnv("TOMCAT_HOST");
    @http:resourceConfig {
        methods:["POST", "GET"],
        path:"/code/{code}"
    }
    resource statusCodeResource (http:Request req, http:Response res, string codeValue) {
        http:ClientConnector httpCheck;
        httpCheck= create http:ClientConnector(connection, {});
        http:Response clientResponse = {};
        map params = req.getQueryParams();
        var withbody, _ = (string)params.withbody;
        string resourcePath = "/RESTfulService/mock/statusCodeService/" + codeValue + "?withbody=" + withbody;
        string method = req.getMethod();
        clientResponse = httpCheck.execute(method, resourcePath, req);
        res.forward(clientResponse);
    }

    @http:resourceConfig {
        methods:["HEAD"],
        path:"/code/{code}"
    }
    resource statusCodeResource2 (http:Request req, http:Response res, string codeValue) {
        http:ClientConnector httpCheck;
        httpCheck= create http:ClientConnector(connection, {});
        http:Response clientResponse = {};
        string resourcePath = "/RESTfulService/mock/statusCodeService/" + codeValue;
        string method = req.getMethod();
        clientResponse = httpCheck.execute(method, resourcePath, req);
        res.forward(clientResponse);
    }
}