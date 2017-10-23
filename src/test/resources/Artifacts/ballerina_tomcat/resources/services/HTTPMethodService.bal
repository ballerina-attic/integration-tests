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
        http:Response clientResponse = {};
        string resourcePath = "/RESTfulService/mock/service/all";
        string method = req.getMethod();
        clientResponse = httpCheck.execute(method, resourcePath, req);
        res.forward(clientResponse);
    }

    @http:resourceConfig {
        methods:["HEAD"],
        path:"/head"
    }
    resource headResource (http:Request req, http:Response res) {
        http:ClientConnector httpCheck;
        httpCheck = create http:ClientConnector(connection, {});
        http:Response clientResponse = {};
        string resourcePath = "/RESTfulService/mock/service/head";
        string method = "HEAD";
        clientResponse = httpCheck.execute(method, resourcePath, req);
        res.forward(clientResponse);
    }

    @http:resourceConfig {
        methods:["GET"],
        path:"/get"
    }
    resource getResource1 (http:Request req, http:Response res) {
        http:ClientConnector httpCheck;
        httpCheck = create http:ClientConnector(connection, {});
        http:Response clientResponse = {};
        string resourcePath = "/RESTfulService/mock/service/get";
        string method = "GET";
        clientResponse = httpCheck.execute(method, resourcePath, req);
        res.forward(clientResponse);
    }

    @http:resourceConfig {
        methods:["POST"],
        path:"/post"
    }
    resource postResource1 (http:Request req, http:Response res) {
        http:ClientConnector httpCheck;
        httpCheck = create http:ClientConnector(connection, {});
        http:Response clientResponse = {};
        string resourcePath = "/RESTfulService/mock/service/post";
        string method = "POST";
        clientResponse = httpCheck.execute(method, resourcePath, req);
        res.forward(clientResponse);
    }

    @http:resourceConfig {
        methods:["PUT"],
        path:"/put"
    }
    resource putResource1 (http:Request req, http:Response res) {
        http:ClientConnector httpCheck;
        httpCheck = create http:ClientConnector(connection, {});
        http:Response clientResponse = {};
        string resourcePath = "/RESTfulService/mock/service/put";
        string method = "PUT";
        clientResponse = httpCheck.execute(method, resourcePath, req);
        res.forward(clientResponse);

    }

    @http:resourceConfig {
        methods:["GET"],
        path:"/gettopres.send();ost"
    }
    resource getResource (http:Request req, http:Response res) {
        http:ClientConnector httpCheck;
        httpCheck= create http:ClientConnector(connection, {});
        http:Response clientResponse = {};
        string resourcePath = "/RESTfulService/mock/service/post";
        string method = "POST";
        clientResponse = httpCheck.execute(method, resourcePath, req);
        res.forward(clientResponse);
    }

    @http:resourceConfig {
        methods:["GET"],
        path:"/doget"
    }
    resource getResource2 (http:Request req, http:Response res) {
        http:ClientConnector httpCheck;
        httpCheck= create http:ClientConnector(connection, {});
        http:Response clientResponse = {};
        string resourcePath = "/RESTfulService/mock/service/get";
        clientResponse = httpCheck.get(resourcePath, req);
        res.forward(clientResponse);
    }

    @http:resourceConfig {
        methods:["POST"],
        path:"/posttoput"
    }
    resource postResource (http:Request req, http:Response res) {
        http:ClientConnector httpCheck;
        httpCheck= create http:ClientConnector(connection, {});
        http:Response clientResponse = {};
        string resourcePath = "/RESTfulService/mock/service/put";
        string method = "PUT";
        clientResponse = httpCheck.execute(method, resourcePath, req);
        res.forward(clientResponse);
    }

    @http:resourceConfig {
        methods:["POST"],
        path:"/dopost"
    }
    resource postResource2 (http:Request req, http:Response res) {
        http:ClientConnector httpCheck;
        httpCheck= create http:ClientConnector(connection, {});
        http:Response clientResponse = {};
        string resourcePath = "/RESTfulService/mock/service/post";
        clientResponse = httpCheck.post(resourcePath, req);
        res.forward(clientResponse);
    }

    @http:resourceConfig {
        methods:["PUT"],
        path:"/puttopost"
    }
    resource putResource (http:Request req, http:Response res) {
        http:ClientConnector httpCheck;
        httpCheck= create http:ClientConnector(connection, {});
        http:Response clientResponse = {};
        string resourcePath = "/RESTfulService/mock/service/post";
        string method = "POST";
        clientResponse = httpCheck.execute(method, resourcePath, req);
        res.forward(clientResponse);
    }

    @http:resourceConfig {
        methods:["PUT"],
        path:"/doput"
    }
    resource putResource2 (http:Request req, http:Response res) {
        http:ClientConnector httpCheck;
        httpCheck= create http:ClientConnector(connection, {});
        http:Response clientResponse = {};
        string resourcePath = "/RESTfulService/mock/service/put";
        clientResponse = httpCheck.put(resourcePath, req);
        res.forward(clientResponse);
    }
}