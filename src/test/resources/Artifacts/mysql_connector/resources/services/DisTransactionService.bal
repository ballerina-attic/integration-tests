package resources.services;

import ballerina.net.http;

@http:configuration {
    basePath:"/distransaction"
}
service <http> DisTransctionService {

   @http:resourceConfig {
        methods:["GET"],
        path:"/success"
    }
    resource disTransactionSuccessResource (http:Request req, http:Response res) {
        error err;
        var result, err = disTransctionSuccess();
        if(err == null){
              res.setStringPayload(result);
         }
         else{
              res.setStringPayload(err.msg);
         }
        res.send();
    }

     @http:resourceConfig {
        methods:["GET"],
        path:"/fail/retry/default"
    }
    resource disTransFailDefaultRetryResource (http:Request req, http:Response res) {
        error err;
        var result_string, result_int, err = disTransctionFailWithDefaultRetry();
        if(err == null){
              res.setStringPayload(result_string);
         }
         else{
              string retryValue = <string>result_int;
              string re = err.msg +". Retried: "+retryValue;
              res.setStringPayload(re);
         }
        res.send();
    }

     @http:resourceConfig {
        methods:["GET"],
        path:"/fail/retry/custom"
    }
    resource disTransFailCustomRetryResource (http:Request req, http:Response res) {
        error err;
        var result_string, result_int, err = disTransctionFailWithCustomRetry();
        if(err == null){
              res.setStringPayload(result_string);
         }
         else{
              string retryValue = <string>result_int;
              string re = err.msg +". Retried: "+retryValue;
              res.setStringPayload(re);
         }
        res.send();
    }

    @http:resourceConfig {
        methods:["GET"],
        path:"/fail/throw"
    }
    resource disTransFailThrowResource (http:Request req, http:Response res) {
        error err;
        var result_string, result_int, err = disTransctionFailForceThrow();
        if(err == null){
              string retryValue1 = <string>result_int;
              string re1 = result_string +". Retried: "+retryValue1;
              res.setStringPayload(re1);
         }
         else{
              string retryValue = <string>result_int;
              string re = err.msg +". Retried: "+retryValue;
              res.setStringPayload(re);
         }
        res.send();
    }

    @http:resourceConfig {
        methods:["GET"],
        path:"/fail/abort"
    }
    resource disTransFailAbortResource (http:Request req, http:Response res) {
        error err;
        var result_string, result_int, err = disTransctionFailForceAbort();
        if(err == null){
              string retryValue1 = <string>result_int;
              string re1 = result_string +". Retried: "+retryValue1;
              res.setStringPayload(re1);
         }
         else{
              string retryValue = <string>result_int;
              string re = err.msg +". Retried: "+retryValue;
              res.setStringPayload(re);
         }
        res.send();
    }

    @http:resourceConfig {
        methods:["GET"],
        path:"/success/multiple"
    }
    resource disMultipleTransactionSuccessResource (http:Request req, http:Response res) {
        error err;
        var result, err = disMultipleTransSuccess();
        if(err == null){
              res.setStringPayload(result);
         }
         else{
              res.setStringPayload(err.msg);
         }
        res.send();
    }

     @http:resourceConfig {
        methods:["GET"],
        path:"/failure/retry/one"
    }
    resource disMultipleTransFailureRetryOneResource (http:Request req, http:Response res) {
        error err;
        var result_string, result_int, err = disMultipleTransFailWithRetryOne();
         if(err == null){
              string retryValue1 = <string>result_int;
              string re1 = result_string +". Retried: "+retryValue1;
              res.setStringPayload(re1);
         }
         else{
              string retryValue = <string>result_int;
              string re = err.msg +". Retried: "+retryValue;
              res.setStringPayload(re);
         }
        res.send();
    }

   @http:resourceConfig {
        methods:["GET"],
        path:"/nested/failure/retry/child"
    }
    resource disNestedFailRetryChildResource (http:Request req, http:Response res) {
        error err;
        var result_string, result_int_child, result_int_parent, err = disNestedTransFailRetryChild();
         if(err == null){
              string retryValueChild = <string>result_int_child;
              string retryValueParent = <string>result_int_parent;
              string re1 = result_string +". Child Retried: "+retryValueChild +". Parent Retried: "+retryValueParent;
              res.setStringPayload(re1);
         }
         else{
              string retryValueChild = <string>result_int_child;
              string retryValueParent = <string>result_int_parent;
              string re = err.msg +". Child Retried: "+retryValueChild +". Parent Retried: "+retryValueParent;
              res.setStringPayload(re);
         }
        res.send();
    }

    @http:resourceConfig {
        methods:["GET"],
        path:"/nested/failure/retry/parent"
    }
    resource disNestedFailRetryParentResource (http:Request req, http:Response res) {
        error err;
        var result_string, result_int_child, result_int_parent, err = disNestedTransFailRetryParent();
         if(err == null){
              string retryValueChild = <string>result_int_child;
              string retryValueParent = <string>result_int_parent;
              string re1 = result_string +". Child Retried: "+retryValueChild +". Parent Retried: "+retryValueParent;
              res.setStringPayload(re1);
         }
         else{
              string retryValueChild = <string>result_int_child;
              string retryValueParent = <string>result_int_parent;
              string re = err.msg +". Child Retried: "+retryValueChild +". Parent Retried: "+retryValueParent;
              res.setStringPayload(re);
         }
        res.send();
    }

     @http:resourceConfig {
        methods:["POST"],
        path:"/general/single/*"
    }
    resource disGeneralSingleResource (http:Request req, http:Response res) {
        error err;
        json payload = req.getJsonPayload();
        var result_string, result_int, err = disTransctionGeneral(payload);
         if(err == null){
              string retryValue = <string>result_int;
              string re1 = result_string +". Retried: "+retryValue;
              res.setStringPayload(re1);
         }
         else{
              string retryValue = <string>result_int;
              string re = err.msg +". Retried: "+retryValue;
              res.setStringPayload(re);
         }
        res.send();
    }
}



