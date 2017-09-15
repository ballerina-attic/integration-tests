package resources.services;

import ballerina.net.http;
import ballerina.lang.messages;
import ballerina.lang.errors;

@http:configuration {
    basePath:"/update"
}
service <http> UpdateTestService {

   @http:resourceConfig {
        methods:["POST"],
        path:"/withParam/*"
    }
    resource updateWithParamResource (message m, @http:QueryParam {value:"value1"} string value1
                                , @http:QueryParam {value:"value2"} string value2) {
        message response = {};
        errors:Error err;
        string payload = messages:getStringPayload(m);
        var temp, _ = <float>value2;
        var result, err = updateWithParams(payload, value1, temp);
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
        path:"/withParam/missing"
    }
    resource updateWithMissingParamResource (message m, @http:QueryParam {value:"value1"} string value1
                                , @http:QueryParam {value:"value2"} string value2) {
        message response = {};
        errors:Error err;
        string payload = messages:getStringPayload(m);
        var temp, _ = <float>value2;
        var result, err = updateWithMissingParams(payload, value1, temp);
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
        path:"/withParam/false"
    }
    resource updateWithoutParamResource (message m) {
        message response = {};
        errors:Error err;
        string payload = messages:getStringPayload(m);
        var result, err = updateWithoutParams(payload);
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
