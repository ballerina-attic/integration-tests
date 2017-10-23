package resources.services;

import ballerina.net.http;

@http:configuration {
    basePath:"/delete"
}
service <http> DeleteTestService {

   @http:resourceConfig {
        methods:["POST"],
        path:"/withParam/*"
    }
    resource deleteWithParamResource (http:Request req, http:Response res) {
        map params = req.getQueryParams();
        var valueToBeDeleted, _ = (string)params.value;
        error err;
        string payload = req.getStringPayload();
        var result, err = deleteWithParams(payload, valueToBeDeleted);
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
        path:"/general/*"
    }
    resource deleteGeneralResource (http:Request req, http:Response res) {
        error err;
        string payload = req.getStringPayload();
        var result, err = deleteGeneral(payload);
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
