package resources.services;

import ballerina.lang.messages;
import ballerina.net.http;

@http:configuration {basePath:"/ping"}
service<http> pingService {

    @http:resourceConfig {
        methods:["GET"],
        path:"/"
    }
    resource sayHello (message m) {
        message response = {};
        messages:setStringPayload(response, "Ping from the server!");
        reply response;
    }
}
