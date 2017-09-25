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
              messages:setStringPayload(response, result);
         }
         else{
              messages:setStringPayload(response, err.msg);
         }
        reply response;
    }

     @http:resourceConfig {
        methods:["GET"],
        path:"/fail/retry/default"
    }
    resource disTransFailDefaultRetryResource (message m) {
        message response = {};
        errors:Error err;
        var result_string, result_int, err = disTransctionFailWithDefaultRetry();
        if(err == null){
              messages:setStringPayload(response, result_string);
         }
         else{
              string retryValue = <string>result_int;
              string re = err.msg +". Retried: "+retryValue;
              messages:setStringPayload(response, re);
         }
        reply response;
    }

     @http:resourceConfig {
        methods:["GET"],
        path:"/fail/retry/custom"
    }
    resource disTransFailCustomRetryResource (message m) {
        message response = {};
        errors:Error err;
        var result_string, result_int, err = disTransctionFailWithCustomRetry();
        if(err == null){
              messages:setStringPayload(response, result_string);
         }
         else{
              string retryValue = <string>result_int;
              string re = err.msg +". Retried: "+retryValue;
              messages:setStringPayload(response, re);
         }
        reply response;
    }

     @http:resourceConfig {
        methods:["GET"],
        path:"/fail/abort"
    }
    resource disTransFailAbortResource (message m) {
        message response = {};
        errors:Error err;
        var result_string, result_int, err = disTransctionFailForceAbort();
        if(err == null){
              string retryValue1 = <string>result_int;
              string re1 = result_string +". Retried: "+retryValue1;
              messages:setStringPayload(response, re1);
         }
         else{
              string retryValue = <string>result_int;
              string re = err.msg +". Retried: "+retryValue;
              messages:setStringPayload(response, re);
         }
        reply response;
    }
}
