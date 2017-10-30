package resources.services;

import ballerina.net.http;
import ballerina.os;

@http:configuration {
    basePath:"/statuscode"
}
service <http> StatusCodeService {

    string connection = os:getEnv("TOMCAT_HOST");
    @http:resourceConfig {
        methods:["POST", "GET"],
        path:"/code/{code}"
    }
    resource statusCodeResource (http:Request req, http:Response res, string code) {
        http:ClientConnector httpCheck;
        httpCheck= create http:ClientConnector(connection, {});
        http:Response clientResponse = {};
        map params = req.getQueryParams();
        var withbody, _ = (string)params.withbody;
        println(withbody);
        string resourcePath = "/RESTfulService/mock/statusCodeService/" + code + "?withbody=" + withbody;
        string method = req.getMethod();
        clientResponse = httpCheck.execute(method, resourcePath, req);
        res.forward(clientResponse);
    }

    @http:resourceConfig {
        methods:["HEAD"],
        path:"/code/{code}"
    }
    resource statusCodeResource2 (http:Request req, http:Response res, string code) {
        http:ClientConnector httpCheck;
        httpCheck= create http:ClientConnector(connection, {});
        http:Response clientResponse = {};
        string resourcePath = "/RESTfulService/mock/statusCodeService/" + code;
        string method = req.getMethod();
        clientResponse = httpCheck.execute(method, resourcePath, req);
        res.forward(clientResponse);
    }
}