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
        map params = req.getQueryParams();
        var withbody, _ = (string)params.withbody;
        string resourcePath = "/RESTfulService/mock/statusCodeService/" + codeValue + "?withbody=" + withbody;
        string method = req.getMethod();
        res = httpCheck.execute(method, resourcePath, req);
        res.send();
    }

    @http:resourceConfig {
        methods:["HEAD"],
        path:"/code/{code}"
    }
    resource statusCodeResource2 (http:Request req, http:Response res, string codeValue) {
        http:ClientConnector httpCheck;
        httpCheck= create http:ClientConnector(connection, {});
        string resourcePath = "/RESTfulService/mock/statusCodeService/" + codeValue;
        string method = req.getMethod();
        res = httpCheck.execute(method, resourcePath, req);
        res.send();
    }
}