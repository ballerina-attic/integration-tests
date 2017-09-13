package resources.services;

import ballerina.net.http;
import ballerina.lang.messages;
import ballerina.lang.errors;

@http:configuration {
    basePath:"/delete"
}
service <http> DeleteTestService {

   @http:resourceConfig {
        methods:["POST"],
        path:"/withParam/*"
    }
    resource deleteWithParamResource (message m, @http:QueryParam {value:"value"} string valueToBeDeleted) {
        message response = {};
        errors:Error err;
        string payload = messages:getStringPayload(m);
        var result, err = deleteWithParams(payload, valueToBeDeleted);
        if(err == null){
              string valueToReturn = <string>result;
              messages:setStringPayload(response, valueToReturn);
         }
         else{
              messages:setStringPayload(response, err.msg);
         }
        reply response;
    }

    @http:resourceConfig {
        methods:["POST"],
        path:"/general/*"
    }
    resource deleteGeneralResource (message m) {
        message response = {};
        errors:Error err;
        string payload = messages:getStringPayload(m);
        var result, err = deleteGeneral(payload);
        if(err == null){
              string valueToReturn = <string>result;
              messages:setStringPayload(response, valueToReturn);
         }
         else{
              messages:setStringPayload(response, err.msg);
         }
        reply response;
    }
}
