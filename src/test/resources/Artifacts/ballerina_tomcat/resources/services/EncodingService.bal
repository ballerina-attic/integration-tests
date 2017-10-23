package resources.services;

import ballerina.net.http;

@http:configuration {basePath:"/encoded"}
service<http> EncodingService {

    @http:resourceConfig {
        methods:["POST","GET","PUT","PATCH"],
        path:"/spayload"
    }
    resource s_echo (http:Request req, http:Response res) {

            string payload = req.getStringPayload();
            res.setStringPayload(payload);
            res.send();
    }
}