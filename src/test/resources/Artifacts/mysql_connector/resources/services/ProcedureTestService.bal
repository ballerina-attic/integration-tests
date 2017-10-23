package resources.services;

import ballerina.net.http;

@http:configuration {
    basePath:"/procedure"
}
service <http> ProcedureTestService {

   @http:resourceConfig {
        methods:["POST"],
        path:"/create"
    }
    resource createProcedureResource (http:Request req, http:Response res) {
        error err;
        string payload = req.getStringPayload();
        var resultCount, err = createStoredProcedure(payload);
        if (err == null){
              string responsePayload;
              responsePayload = "Procedure created successfully.";
              res.setStringPayload(responsePayload);
        }
        else{
              res.setStringPayload(err.msg);
        }
        res.send();
    }

    @http:resourceConfig {
        methods:["GET"],
        path:"/callsucces/parameter"
    }
    resource callProcedureSuccessWithParamsResource (http:Request req, http:Response res) {
        error err;
        map params = req.getQueryParams();
        var custNo, _ = (string)params.custNo;
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
              res.setStringPayload(responsePayload);
        }
        else{
              res.setStringPayload(err.msg);
        }
        res.send();
    }

     @http:resourceConfig {
        methods:["GET"],
        path:"/call/directionchange"
    }
    resource callProWithWrongParamDirectionResource (http:Request req, http:Response res) {
        error err;
        map params = req.getQueryParams();
        var custNo, _ = (string)params.custNo;
        var status, _ = (string)params.status;
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
              res.setStringPayload(responsePayload);
        }
        else{
              res.setStringPayload(err.msg);
        }
        res.send();
    }

      @http:resourceConfig {
        methods:["GET"],
        path:"/call/lessparamter/in"
    }
    resource callProcWithLessParamsInResource (http:Request req, http:Response res) {
        error err;
        map params = req.getQueryParams();
        var custNo, _ = (string)params.custNo;
        var status, _ = (string)params.status;
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
              res.setStringPayload(responsePayload);
        }
        else{
              res.setStringPayload(err.msg);
        }
        res.send();
    }

     @http:resourceConfig {
        methods:["GET"],
        path:"/call/lessparamter/out"
    }
    resource callProcWithLessParamsOutResource (http:Request req, http:Response res) {
        error err;
        map params = req.getQueryParams();
        var custNo, _ = (string)params.custNo;
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
              res.setStringPayload(responsePayload);
        }
        else{
              res.setStringPayload(err.msg);
        }
        res.send();
    }

    @http:resourceConfig {
        methods:["GET"],
        path:"/call/lessparamter/inout"
    }
    resource callProcWithLessParamsInOutResource (http:Request req, http:Response res) {
        error err;
        map params = req.getQueryParams();
        var custNo, _ = (string)params.custNo;
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
              res.setStringPayload(responsePayload);
        }
        else{
              res.setStringPayload(err.msg);
        }
        res.send();
    }

     @http:resourceConfig {
        methods:["GET"],
        path:"/call/mismatchdatatype"
    }
    resource callProWithMismatchingDataTypeResource (http:Request req, http:Response res) {
        error err;
        map params = req.getQueryParams();
        var custNo, _ = (string)params.custNo;
        var status, _ = (string)params.status;
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
              res.setStringPayload(responsePayload);
        }
        else{
              res.setStringPayload(err.msg);
        }
        res.send();
    }

     @http:resourceConfig {
        methods:["GET"],
        path:"/callsucces/resultset"
    }
    resource callProcedureSuccessWithResultSetResource (http:Request req, http:Response res) {
        error err;
        map params = req.getQueryParams();
        var custNo, _ = (string)params.custNo;
        var temp, _ = <int>custNo;
        var result, err = callProcedureToGetResultSet(temp);
        if (err == null){
              res.setJsonPayload(result);
        }
        else{
              res.setStringPayload(err.msg);
        }
        res.send();
    }
}