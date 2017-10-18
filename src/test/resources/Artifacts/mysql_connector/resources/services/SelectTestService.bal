package resources.services;

import ballerina.net.http;

@http:configuration {
    basePath:"/select"
}
service <http> SelectTestService {

   @http:resourceConfig {
        methods:["POST"],
        path:"/general/*"
    }
    resource selectGeneralResource (http:Request req, http:Response res) {
        error err;
        string payload = req.getStringPayload();
        var result, err = selectGeneral(payload);
        if(err == null){
              res.setJsonPayload(result);

         }
         else{
              res.setStringPayload(err.msg);
         }
        res.send();
    }

     @http:resourceConfig {
        methods:["POST"],
        path:"/general/between"
    }
    resource selectBetweenResource (http:Request req, http:Response res) {
        error err;
        string payload = req.getStringPayload();
        var result, err = selectBetween(payload);
        if(err == null){
              res.setJsonPayload(result);
         }
         else{
              res.setStringPayload(err.msg);
        }
        res.send();
    }

    @http:resourceConfig {
        methods:["POST"],
        path:"/general/like"
    }
    resource selectLikeResource (http:Request req, http:Response res) {
        error err;
        string payload = req.getStringPayload();
        var result, err = selectLike(payload);
        if(err == null){
              res.setJsonPayload(result);
         }
         else{
              res.setStringPayload(err.msg);
         }
        res.send();
    }

    @http:resourceConfig {
        methods:["POST"],
        path:"/general/in"
    }
    resource selectInResource (http:Request req, http:Response res) {
        error err;
        string payload = req.getStringPayload();
        var result, err = selectIn(payload);
        if(err == null){
              res.setJsonPayload(result);
         }
         else{
              res.setStringPayload(err.msg);
         }
        res.send();
    }

    @http:resourceConfig {
        methods:["POST"],
        path:"/general/andor"
    }
    resource selectAndOrResource (http:Request req, http:Response res) {
        error err;
        string payload = req.getStringPayload();
        var result, err = selectAndOr(payload);
        if(err == null){
              res.setJsonPayload(result);
         }
         else{
              res.setStringPayload(err.msg);
         }
        res.send();
    }

     @http:resourceConfig {
        methods:["POST"],
        path:"/general/limit"
    }
    resource selectWithLimitResource (http:Request req, http:Response res) {
        error err;
        var result, err = selectWithLimit();
        if(err == null){
              res.setJsonPayload(result);
         }
         else{
              res.setStringPayload(err.msg);
         }
        res.send();
    }

      @http:resourceConfig {
        methods:["POST"],
        path:"/general/exists"
    }
    resource selectWithExistsResource (http:Request req, http:Response res) {
        error err;
        string payload = req.getStringPayload();
        var result, err = selectWithExists(payload);
        if(err == null){
              res.setJsonPayload(result);
         }
         else{
              res.setStringPayload(err.msg);
         }
        res.send();
    }

     @http:resourceConfig {
        methods:["POST"],
        path:"/general/complexsql"
    }
    resource selectWithComplexSqlResource (http:Request req, http:Response res) {
        error err;
        var result, err = selectWithComplexSql();
        if(err == null){
              res.setJsonPayload(result);
         }
         else{
              res.setStringPayload(err.msg);
         }
        res.send();
    }

    @http:resourceConfig {
        methods:["POST"],
        path:"/general/xml"
    }
    resource selectGeneralXmlResource (http:Request req, http:Response res) {
        error err;
        string payload = req.getStringPayload();
        var result, err = selectGeneralToXml(payload);
        if(err == null){
              res.setXmlPayload(result);
         }
         else{
              res.setStringPayload(err.msg);
         }
        res.send();
    }
}