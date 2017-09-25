package resources.services;

import ballerina.net.http;
import ballerina.lang.messages;
import ballerina.lang.errors;

@http:configuration {
    basePath:"/distransaction"
}
service <http> DisTransctionService {

   @http:resourceConfig {
        methods:["GET"],
        path:"/success"
    }
    resource disTransactionSuccessResource (message m) {
        message response = {};
        errors:Error err;
        var result, err = disTransctionSuccess();
        if(err == null){
              string responseValue = <string>result;
              messages:setStringPayload(response, responseValue);
         }
         else{
              messages:setStringPayload(response, err.msg);
         }
        reply response;
    }
}
