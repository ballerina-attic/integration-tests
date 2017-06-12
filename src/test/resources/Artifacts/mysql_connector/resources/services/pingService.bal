package resources.services;

import ballerina.lang.messages;
import ballerina.net.http;

@http:BasePath {value:"/hello"}
service pingService {
    
    @http:GET{}
    @http:Path {value:"/"}
    resource sayHello (message m) {
        message response = {};
        messages:setStringPayload(response, "Ping from the server!");
        reply response;
    }
}
