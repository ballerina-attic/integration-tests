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
        path:"/fail/throw"
    }
    resource disTransFailThrowResource (message m) {
        message response = {};
        errors:Error err;
        var result_string, result_int, err = disTransctionFailForceThrow();
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

    @http:resourceConfig {
        methods:["GET"],
        path:"/success/multiple"
    }
    resource disMultipleTransactionSuccessResource (message m) {
        message response = {};
        errors:Error err;
        var result, err = disMultipleTransSuccess();
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
        path:"/failure/retry/one"
    }
    resource disMultipleTransFailureRetryOneResource (message m) {
        message response = {};
        errors:Error err;
        var result_string, result_int, err = disMultipleTransFailWithRetryOne();
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

   @http:resourceConfig {
        methods:["GET"],
        path:"/nested/failure/retry/child"
    }
    resource disNestedFailRetryChildResource (message m) {
        message response = {};
        errors:Error err;
        var result_string, result_int_child, result_int_parent, err = disNestedTransFailRetryChild();
         if(err == null){
              string retryValueChild = <string>result_int_child;
              string retryValueParent = <string>result_int_parent;
              string re1 = result_string +". Child Retried: "+retryValueChild +". Parent Retried: "+retryValueParent;
              messages:setStringPayload(response, re1);
         }
         else{
              string retryValueChild = <string>result_int_child;
              string retryValueParent = <string>result_int_parent;
              string re = err.msg +". Child Retried: "+retryValueChild +". Parent Retried: "+retryValueParent;
              messages:setStringPayload(response, re);
         }
        reply response;
    }

    @http:resourceConfig {
        methods:["GET"],
        path:"/nested/failure/retry/parent"
    }
    resource disNestedFailRetryParentResource (message m) {
        message response = {};
        errors:Error err;
        var result_string, result_int_child, result_int_parent, err = disNestedTransFailRetryParent();
         if(err == null){
              string retryValueChild = <string>result_int_child;
              string retryValueParent = <string>result_int_parent;
              string re1 = result_string +". Child Retried: "+retryValueChild +". Parent Retried: "+retryValueParent;
              messages:setStringPayload(response, re1);
         }
         else{
              string retryValueChild = <string>result_int_child;
              string retryValueParent = <string>result_int_parent;
              string re = err.msg +". Child Retried: "+retryValueChild +". Parent Retried: "+retryValueParent;
              messages:setStringPayload(response, re);
         }
        reply response;
    }

     @http:resourceConfig {
        methods:["POST"],
        path:"/general/single/*"
    }
    resource disGeneralSingleResource (message m) {
        message response = {};
        errors:Error err;
        json payload = messages:getJsonPayload(m);
        var result_string, result_int, err = disTransctionGeneral(payload);
         if(err == null){
              string retryValue = <string>result_int;
              string re1 = result_string +". Retried: "+retryValue;
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



