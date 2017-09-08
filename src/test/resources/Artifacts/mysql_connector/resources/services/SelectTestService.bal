package resources.services;

import ballerina.net.http;
import ballerina.lang.messages;
import ballerina.lang.errors;

@http:configuration {
    basePath:"/select"
}
service <http> SelectTestService {

   @http:resourceConfig {
        methods:["POST"],
        path:"/general/*"
    }
    resource selectGeneralResource (message m) {
        message response = {};
        errors:Error err;
        string payload = messages:getStringPayload(m);
        var result, err = selectGeneral(payload);
        if(err == null){
              messages:setJsonPayload(response, result);
         }
         else{
              messages:setStringPayload(response, err.msg);
         }
        reply response;
    }

     @http:resourceConfig {
        methods:["POST"],
        path:"/general/between"
    }
    resource selectBetweenResource (message m) {
        message response = {};
        errors:Error err;
        string payload = messages:getStringPayload(m);
        var result, err = selectBetween(payload);
        if(err == null){
              messages:setJsonPayload(response, result);
         }
         else{
              messages:setStringPayload(response, err.msg);
         }
        reply response;
    }

    @http:resourceConfig {
        methods:["POST"],
        path:"/general/like"
    }
    resource selectLikeResource (message m) {
        message response = {};
        errors:Error err;
        string payload = messages:getStringPayload(m);
        var result, err = selectLike(payload);
        if(err == null){
              messages:setJsonPayload(response, result);
         }
         else{
              messages:setStringPayload(response, err.msg);
         }
        reply response;
    }

    @http:resourceConfig {
        methods:["POST"],
        path:"/general/in"
    }
    resource selectInResource (message m) {
        message response = {};
        errors:Error err;
        string payload = messages:getStringPayload(m);
        var result, err = selectIn(payload);
        if(err == null){
              messages:setJsonPayload(response, result);
         }
         else{
              messages:setStringPayload(response, err.msg);
         }
        reply response;
    }

    @http:resourceConfig {
        methods:["POST"],
        path:"/general/andor"
    }
    resource selectAndOrResource (message m) {
        message response = {};
        errors:Error err;
        string payload = messages:getStringPayload(m);
        var result, err = selectAndOr(payload);
        if(err == null){
              messages:setJsonPayload(response, result);
         }
         else{
              messages:setStringPayload(response, err.msg);
         }
        reply response;
    }

     @http:resourceConfig {
        methods:["POST"],
        path:"/general/limit"
    }
    resource selectWithLimitResource (message m) {
        message response = {};
        errors:Error err;
        var result, err = selectWithLimit();
        if(err == null){
              messages:setJsonPayload(response, result);
         }
         else{
              messages:setStringPayload(response, err.msg);
         }
        reply response;
    }

      @http:resourceConfig {
        methods:["POST"],
        path:"/general/exists"
    }
    resource selectWithExistsResource (message m) {
        message response = {};
        errors:Error err;
        string payload = messages:getStringPayload(m);
        var result, err = selectWithExists(payload);
        if(err == null){
              messages:setJsonPayload(response, result);
         }
         else{
              messages:setStringPayload(response, err.msg);
         }
        reply response;
    }
}