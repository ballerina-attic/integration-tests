package resources.services;

import ballerina.net.http;
import ballerina.lang.messages;
import ballerina.lang.errors;

@http:configuration {
    basePath:"/procedure"
}
service <http> ProcedureTestService {

   @http:resourceConfig {
        methods:["POST"],
        path:"/create"
    }
    resource createProcedureResource (message m) {
        message response = {};
        errors:Error err;
        string payload = messages:getStringPayload(m);
        var resultCount, err = createStoredProcedure(payload);
        if (err == null){
              string responsePayload;
              responsePayload = "Procedure created successfully.";
              messages:setStringPayload(response, responsePayload);
        }
        else{
              messages:setStringPayload(response, err.msg);
        }
        reply response;
    }

    @http:resourceConfig {
        methods:["GET"],
        path:"/callsucces/parameter"
    }
    resource callProcedureSuccessWithParamsResource (message m, @http:QueryParam {value:"custNo"} string custNo) {
        message response = {};
        errors:Error err;
        var temp, _ = <int>custNo;
        var shipped, cancelled, resolved, disputed, count, err = callProcedureSuccess(temp);
        if (err == null){
              string responsePayload;
              var a, e1 = (int)shipped;
              var b, _ = (int)cancelled ;
              var c, _ = (int)resolved;
              var d, _ = (int)disputed;
              var e, _ = (int)count;
              responsePayload = a + ":" + b + ":" + c + ":" + d + ":" + e;
              messages:setStringPayload(response, responsePayload);
        }
        else{
              messages:setStringPayload(response, err.msg);
        }
        reply response;
    }

     @http:resourceConfig {
        methods:["GET"],
        path:"/call/directionchange"
    }
    resource callProWithWrongParamDirectionResource (message m, @http:QueryParam {value:"custNo"} string custNo, @http:QueryParam {value:"status"} string status) {
        message response = {};
        errors:Error err;
        var temp, _ = <int>custNo;
        var shipped, cancelled, resolved, disputed, count, err = callProcedureWithWrongDirectionForParams(temp, status);
        if (err == null){
              string responsePayload;
              var a, e1 = (int)shipped;
              var b, _ = (int)cancelled ;
              var c, _ = (int)resolved;
              var d, _ = (int)disputed;
              var e, _ = (int)count;
              responsePayload = a + ":" + b + ":" + c + ":" + d + ":" + e;
              messages:setStringPayload(response, responsePayload);
        }
        else{
              messages:setStringPayload(response, err.msg);
        }
        reply response;
    }

      @http:resourceConfig {
        methods:["GET"],
        path:"/call/lessparamter/in"
    }
    resource callProcWithLessParamsInResource (message m, @http:QueryParam {value:"custNo"} string custNo, @http:QueryParam {value:"status"} string status) {
        message response = {};
        errors:Error err;
        var temp, _ = <int>custNo;
        var shipped, cancelled, resolved, disputed, count, err = callProcedureWithLessInParams(temp, status);
        if (err == null){
              string responsePayload;
              var a, e1 = (int)shipped;
              var b, _ = (int)cancelled ;
              var c, _ = (int)resolved;
              var d, _ = (int)disputed;
              var e, _ = (int)count;
              responsePayload = a + ":" + b + ":" + c + ":" + d + ":" + e;
              messages:setStringPayload(response, responsePayload);
        }
        else{
              messages:setStringPayload(response, err.msg);
        }
        reply response;
    }

     @http:resourceConfig {
        methods:["GET"],
        path:"/call/lessparamter/out"
    }
    resource callProcWithLessParamsOutResource (message m, @http:QueryParam {value:"custNo"} string custNo) {
        message response = {};
        errors:Error err;
        var temp, _ = <int>custNo;
        var shipped, cancelled, resolved, disputed, count, err = callProcedureWithLessOutParams(temp);
        if (err == null){
              string responsePayload;
              var a, e1 = (int)shipped;
              var b, _ = (int)cancelled ;
              var c, _ = (int)resolved;
              var d, _ = (int)disputed;
              var e, _ = (int)count;
              responsePayload = a + ":" + b + ":" + c + ":" + d + ":" + e;
              messages:setStringPayload(response, responsePayload);
        }
        else{
              messages:setStringPayload(response, err.msg);
        }
        reply response;
    }

    @http:resourceConfig {
        methods:["GET"],
        path:"/call/lessparamter/inout"
    }
    resource callProcWithLessParamsInOutResource (message m, @http:QueryParam {value:"custNo"} string custNo) {
        message response = {};
        errors:Error err;
        var temp, _ = <int>custNo;
        var shipped, cancelled, resolved, disputed, count, err = callProcedureWithLessInOutParams(temp);
        if (err == null){
              string responsePayload;
              var a, e1 = (int)shipped;
              var b, _ = (int)cancelled ;
              var c, _ = (int)resolved;
              var d, _ = (int)disputed;
              var e, _ = (int)count;
              responsePayload = a + ":" + b + ":" + c + ":" + d + ":" + e;
              messages:setStringPayload(response, responsePayload);
        }
        else{
              messages:setStringPayload(response, err.msg);
        }
        reply response;
    }

     @http:resourceConfig {
        methods:["GET"],
        path:"/call/mismatchdatatype"
    }
    resource callProWithMismatchingDataTypeResource (message m, @http:QueryParam {value:"custNo"} string custNo, @http:QueryParam {value:"status"} string status) {
        message response = {};
        errors:Error err;
        var temp, _ = <int>custNo;
        var shipped, cancelled, resolved, disputed, count, err = callProcedureWithMismatchingParams(temp, status);
        if (err == null){
              string responsePayload;
              var a, e1 = (int)shipped;
              var b, _ = (int)cancelled ;
              var c, _ = (int)resolved;
              var d, _ = (int)disputed;
              var e, _ = (int)count;
              responsePayload = a + ":" + b + ":" + c + ":" + d + ":" + e;
              messages:setStringPayload(response, responsePayload);
        }
        else{
              messages:setStringPayload(response, err.msg);
        }
        reply response;
    }
}