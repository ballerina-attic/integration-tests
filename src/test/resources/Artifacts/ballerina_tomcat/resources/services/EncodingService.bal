package resources.services;

import ballerina.net.http;
import ballerina.lang.messages;

@http:configuration {basePath:"/encoded"}
service<http> EncodingService {

    @http:resourceConfig {
        methods:["POST","GET","PUT","PATCH"],
        path:"/spayload"
    }
    resource s_echo (message m) {

            string payload = messages:getStringPayload(m);
            message msg = {};
            messages:setStringPayload(msg, payload);
            reply msg;
    }
}