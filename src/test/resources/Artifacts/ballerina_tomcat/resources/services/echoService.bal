package resources.services;

import ballerina.net.http;

@http:configuration {basePath:"/echo"}
service<http> echo {

    @http:resourceConfig {
        methods:["POST"],
        path:"/"
    }
    resource echo (http:Request req, http:Response res) {
        json payload = req.getJsonPayload();
        res.setJsonPayload(payload);
        res.send();
    }
}