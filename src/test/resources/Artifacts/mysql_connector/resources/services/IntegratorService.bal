package resources.services;

import ballerina.lang.messages;
import ballerina.net.http;
import ballerina.lang.strings;

@http:BasePath {value:"/persons"}
service Integrator {

    @http:POST {}
    @http:Path {value:"/insert/{status}"}
    resource insertPersonInfoSuccess (message m, @http:PathParam {value:"status"}string status) {
        json payload;
        string responseFromInsert;
        message response = {};
        payload = messages:getJsonPayload(m);
        if(strings:equalsIgnoreCase(status, "success")){
            responseFromInsert = insertToDBSuccess(payload);
        }
        else if (strings:equalsIgnoreCase(status, "sqlError")){
            responseFromInsert = insertToDBSqlError(payload);
        }
        else if (strings:equalsIgnoreCase(status, "forceAbort")){
            responseFromInsert = insertToDBForceAbort(payload);
        }
        else if (strings:equalsIgnoreCase(status, "multipleSuccess")){
            responseFromInsert = insertToMultipleDbSuccess(payload);
        }
        else if (strings:equalsIgnoreCase(status, "multipleSqlError")){
            responseFromInsert = insertToMultipleDbSqlError(payload);
        }
        else if (strings:equalsIgnoreCase(status, "multipleForceAbort")){
            responseFromInsert = insertToMultipleDbForceAbort(payload);
        }
        else{
            responseFromInsert = "Functionality not supported.";
        }
        messages:setStringPayload( response, responseFromInsert);
        reply response;
    }

    @http:POST {}
    @http:Path {value:"/update/{status}"}
    resource updatePersonInfo (message m, @http:PathParam {value:"status"}string status) {
        json payload;
        string responseFromUpdate;
        message response = {};
        payload = messages:getJsonPayload(m);
        if(strings:equalsIgnoreCase(status, "success")){
            responseFromUpdate = updateDBSuccess(payload);
        }
        else if(strings:equalsIgnoreCase(status, "sqlError")){
            responseFromUpdate = updateDBSqlError(payload);
        }
        else if(strings:equalsIgnoreCase(status, "forceAbort")){
            responseFromUpdate = updateDBForceAbort(payload);
        }
        else if(strings:equalsIgnoreCase(status, "multipleSuccess")){
            responseFromUpdate = updateMultipleDBSuccess(payload);
        }
        else if(strings:equalsIgnoreCase(status, "multipleSqlError")){
            responseFromUpdate = updateMultipleDBSqlError(payload);
        }
        else if(strings:equalsIgnoreCase(status, "multipleForceAbort")){
            responseFromUpdate = updateMultipleForceAbort(payload);
        }
        else{
            responseFromUpdate = "Functionality not supported";
        }
        messages:setStringPayload( response, responseFromUpdate);
        reply response;
    }

    @http:POST {}
    @http:Path {value:"/change/{status}/{deleteId}"}
    resource manipulatePersonInfo(message m, @http:PathParam {value:"status"}string status, @http:PathParam {value:"deleteId"}string deleteId){
        json payload;
        string responseFromManipulation;
        message response = {};
        payload = messages:getJsonPayload(m);
        if(strings:equalsIgnoreCase(status, "samedb")){
            responseFromManipulation = insertAndDeleteSameTable(payload, deleteId);
        }
        else if(strings:equalsIgnoreCase(status, "diffdb")){
            responseFromManipulation = insertAndDeleteDiffTable(payload, deleteId);
        }
        else if(strings:equalsIgnoreCase(status, "samedbSqlError")){
            responseFromManipulation = insertAndDeleteSameTableSqlError(payload, deleteId);
        }
        else if(strings:equalsIgnoreCase(status, "diffdbSqlError")){
            responseFromManipulation = insertAndDeleteDiffTableSqlError(payload, deleteId);
        }
        messages:setStringPayload(response, responseFromManipulation);
        reply response;
    }


}