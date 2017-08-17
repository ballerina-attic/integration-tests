package resources.services;

import ballerina.net.http;
import ballerina.lang.system;

@http:configuration {
    basePath:"/httpmethods"
}
service <http> HTTPMethodService {


    @http:resourceConfig {
        methods:["POST", "GET", "HEAD", "PUT", "DELETE"],
        path:"/all"
    }
    resource statusCodeResource (message m) {
        string resourcePath = "/RESTfulService/mock/service/all";
        message response = {};
        string method = http:getMethod(m);
        string connection = system:getEnv("TOMCAT_HOST");

        http:ClientConnector httpCheck = create http:ClientConnector(connection);
        response = httpCheck.execute(method, resourcePath, m);

        reply response;
    }

    @http:resourceConfig {
        methods:["HEAD"],
        path:"/head"
    }
    resource headResource (message m) {
        string resourcePath = "/RESTfulService/mock/service/head";
        message response = {};
        string method = "HEAD";
        string connection = system:getEnv("TOMCAT_HOST");

        http:ClientConnector httpCheck = create http:ClientConnector(connection);
        response = httpCheck.execute(method, resourcePath, m);

        reply response;
    }

    @http:resourceConfig {
        methods:["GET"],
        path:"/get"
    }
    resource getResource1 (message m) {
        string resourcePath = "/RESTfulService/mock/service/get";
        message response = {};
        string method = "GET";
        string connection = system:getEnv("TOMCAT_HOST");

        http:ClientConnector httpCheck = create http:ClientConnector(connection);
        response = httpCheck.execute(method, resourcePath, m);

        reply response;
    }

    @http:resourceConfig {
        methods:["POST"],
        path:"/post"
    }
    resource postResource1 (message m) {
        string resourcePath = "/RESTfulService/mock/service/post";
        message response = {};
        string method = "POST";
        string connection = system:getEnv("TOMCAT_HOST");

        http:ClientConnector httpCheck = create http:ClientConnector(connection);
        response = httpCheck.execute(method, resourcePath, m);

        reply response;
    }

    @http:resourceConfig {
        methods:["PUT"],
        path:"/put"
    }
    resource putResource1 (message m) {
        string resourcePath = "/RESTfulService/mock/service/put";
        message response = {};
        string method = "PUT";
        string connection = system:getEnv("TOMCAT_HOST");

        http:ClientConnector httpCheck = create http:ClientConnector(connection);
        response = httpCheck.execute(method, resourcePath, m);

        reply response;
    }

    @http:resourceConfig {
        methods:["GET"],
        path:"/gettopost"
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
        methods:["GET"],
        path:"/doget"
    }
    resource getResource2 (message m) {
        string resourcePath = "/RESTfulService/mock/service/get";
        message response = {};
        string connection = system:getEnv("TOMCAT_HOST");

        http:ClientConnector httpCheck = create http:ClientConnector(connection);
        response = httpCheck.get(resourcePath, m);

        reply response;
    }

    @http:resourceConfig {
        methods:["POST"],
        path:"/posttoput"
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

    @http:resourceConfig {
        methods:["POST"],
        path:"/dopost"
    }
    resource postResource2 (message m) {
        string resourcePath = "/RESTfulService/mock/service/post";
        message response = {};
        string connection = system:getEnv("TOMCAT_HOST");

        http:ClientConnector httpCheck = create http:ClientConnector(connection);
        response = httpCheck.post(resourcePath, m);

        reply response;
    }

    @http:resourceConfig {
        methods:["PUT"],
        path:"/puttopost"
    }
    resource putResource (message m) {
        string resourcePath = "/RESTfulService/mock/service/post";
        message response = {};
        string method = "POST";
        string connection = system:getEnv("TOMCAT_HOST");

        http:ClientConnector httpCheck = create http:ClientConnector(connection);
        response = httpCheck.execute(method, resourcePath, m);

        reply response;
    }

    @http:resourceConfig {
        methods:["PUT"],
        path:"/doput"
    }
    resource putResource2 (message m) {
        string resourcePath = "/RESTfulService/mock/service/put";
        message response = {};
        string connection = system:getEnv("TOMCAT_HOST");

        http:ClientConnector httpCheck = create http:ClientConnector(connection);
        response = httpCheck.put(resourcePath, m);

        reply response;
    }
}