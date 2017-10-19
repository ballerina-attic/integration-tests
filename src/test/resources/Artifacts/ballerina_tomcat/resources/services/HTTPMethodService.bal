package resources.services;

import ballerina.net.http;
import ballerina.lang.system;

@http:configuration {
    basePath:"/httpmethods"
}
service <http> HTTPMethodService {

    string connection = system:getEnv("TOMCAT_HOST");
    @http:resourceConfig {
        methods:["POST", "GET", "HEAD", "PUT", "DELETE"],
        path:"/all"
    }
    resource statusCodeResource (http:Request req, http:Response res) {
        http:ClientConnector httpCheck;
        httpCheck = create http:ClientConnector(connection, {});
        string resourcePath = "/RESTfulService/mock/service/all";
        string method = req.getMethod();
        res = httpCheck.execute(method, resourcePath, req);
        res.send();
    }

    @http:resourceConfig {
        methods:["HEAD"],
        path:"/head"
    }
    resource headResource (http:Request req, http:Response res) {
        http:ClientConnector httpCheck;
        httpCheck = create http:ClientConnector(connection, {});
        string resourcePath = "/RESTfulService/mock/service/head";
        string method = "HEAD";
        res = httpCheck.execute(method, resourcePath, req);
        res.send();
    }

    @http:resourceConfig {
        methods:["GET"],
        path:"/get"
    }
    resource getResource1 (http:Request req, http:Response res) {
        http:ClientConnector httpCheck;
        httpCheck = create http:ClientConnector(connection, {});
        string resourcePath = "/RESTfulService/mock/service/get";
        string method = "GET";
        res = httpCheck.execute(method, resourcePath, req);
        res.send();
    }

    @http:resourceConfig {
        methods:["POST"],
        path:"/post"
    }
    resource postResource1 (http:Request req, http:Response res) {
        http:ClientConnector httpCheck;
        httpCheck = create http:ClientConnector(connection, {});
        string resourcePath = "/RESTfulService/mock/service/post";
        string method = "POST";
        res = httpCheck.execute(method, resourcePath, req);
        res.send();
    }

    @http:resourceConfig {
        methods:["PUT"],
        path:"/put"
    }
    resource putResource1 (http:Request req, http:Response res) {
        http:ClientConnector httpCheck;
        httpCheck = create http:ClientConnector(connection, {});
        string resourcePath = "/RESTfulService/mock/service/put";
        string method = "PUT";
        res = httpCheck.execute(method, resourcePath, req);
        res.send();

    }

    @http:resourceConfig {
        methods:["GET"],
        path:"/gettopres.send();ost"
    }
    resource getResource (http:Request req, http:Response res) {
        http:ClientConnector httpCheck;
        httpCheck= create http:ClientConnector(connection, {});
        string resourcePath = "/RESTfulService/mock/service/post";
        string method = "POST";
        res = httpCheck.execute(method, resourcePath, req);
        res.send();
    }

    @http:resourceConfig {
        methods:["GET"],
        path:"/doget"
    }
    resource getResource2 (http:Request req, http:Response res) {
        http:ClientConnector httpCheck;
        httpCheck= create http:ClientConnector(connection, {});
        string resourcePath = "/RESTfulService/mock/service/get";
        res = httpCheck.get(resourcePath, req);
        res.send();
    }

    @http:resourceConfig {
        methods:["POST"],
        path:"/posttoput"
    }
    resource postResource (http:Request req, http:Response res) {
        http:ClientConnector httpCheck;
        httpCheck= create http:ClientConnector(connection, {});
        string resourcePath = "/RESTfulService/mock/service/put";
        string method = "PUT";
        res = httpCheck.execute(method, resourcePath, req);
        res.send();
    }

    @http:resourceConfig {
        methods:["POST"],
        path:"/dopost"
    }
    resource postResource2 (http:Request req, http:Response res) {
        http:ClientConnector httpCheck;
        httpCheck= create http:ClientConnector(connection, {});
        string resourcePath = "/RESTfulService/mock/service/post";
        res = httpCheck.post(resourcePath, req);
        res.send();
    }

    @http:resourceConfig {
        methods:["PUT"],
        path:"/puttopost"
    }
    resource putResource (http:Request req, http:Response res) {
        http:ClientConnector httpCheck;
        httpCheck= create http:ClientConnector(connection, {});
        string resourcePath = "/RESTfulService/mock/service/post";
        string method = "POST";
        res = httpCheck.execute(method, resourcePath, req);
        res.send();
    }

    @http:resourceConfig {
        methods:["PUT"],
        path:"/doput"
    }
    resource putResource2 (http:Request req, http:Response res) {
        http:ClientConnector httpCheck;
        httpCheck= create http:ClientConnector(connection, {});
        string resourcePath = "/RESTfulService/mock/service/put";
        res = httpCheck.put(resourcePath, req);
        res.send();
    }
}