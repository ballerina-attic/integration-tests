package resources.services;

import ballerina.net.http;
import ballerina.lang.messages;
import ballerina.lang.errors;

@http:configuration {
    basePath:"/deletedup"
}
service <http> DeleteDupTestService {

   @http:resourceConfig {
        methods:["POST"],
        path:"/withParam/*"
    }
    resource deleteWithParamDupResource (message m, @http:QueryParam {value:"value"} string valueToBeDeleted) {
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

}
