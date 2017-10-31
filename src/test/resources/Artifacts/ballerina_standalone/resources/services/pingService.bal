package resources.services;

import ballerina.net.http;

@http:configuration {basePath:"/ping"}
service<http> pingService {

    @http:resourceConfig {
        methods:["GET"],
        path:"/"
    }
    resource sayHello (http:Request req, http:Response res) {
        res.setStringPayload("Ping from the server!");
        res.send();
    }
}
