package resources.services;

import ballerina.net.http;

@http:configuration {
    basePath:"/update"
}
service <http> UpdateTestService {

   @http:resourceConfig {
        methods:["POST"],
        path:"/withParam/*"
    }
    resource updateWithParamResource (http:Request req, http:Response res) {
        error err;
        map params = req.getQueryParams();
        var value2, _ = (string)params.value2;
        var value1, _ = (string)params.value1;
        var temp, _ = <float>value2;
        string payload = req.getStringPayload();
        var result, err = updateWithParams(payload, value1, temp);
        if(err == null){
              string valueToReturn = <string>result;
              res.setStringPayload(valueToReturn);

         }
         else{
              res.setStringPayload(err.msg);

         }
              res.send();
    }

    @http:resourceConfig {
        methods:["POST"],
        path:"/withParam/missing"
    }
    resource updateWithMissingParamResource (http:Request req, http:Response res) {
        error err;
        map params = req.getQueryParams();
        var value2, _ = (string)params.value2;
        var value1, _ = (string)params.value1;
        var temp, _ = <float>value2;
        string payload = req.getStringPayload();
        var result, err = updateWithMissingParams(payload, value1, temp);
        if(err == null){
              string valueToReturn = <string>result;
              res.setStringPayload(valueToReturn);

         }
         else{
              res.setStringPayload(err.msg);

         }
              res.send();

    }

    @http:resourceConfig {
        methods:["POST"],
        path:"/withParam/false"
    }
    resource updateWithoutParamResource (http:Request req, http:Response res) {
        error err;
        string payload = req.getStringPayload();
        var result, err = updateWithoutParams(payload);
        if(err == null){
              string valueToReturn = <string>result;
              res.setStringPayload(valueToReturn);
        }
         else{
              res.setStringPayload(err.msg);

         }
              res.send();

    }
}
