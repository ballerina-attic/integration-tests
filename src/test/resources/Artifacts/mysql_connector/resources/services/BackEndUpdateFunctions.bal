package resources.services;

import ballerina.data.sql;
import ballerina.lang.errors;
import ballerina.lang.system;
import ballerina.lang.strings;



function updateDBSuccess (json payload) (string responsePayload){
    int i;
    sql:ClientConnector connectionInstance = init();
    try{
        transaction {
            sql:Parameter paraID = {sqlType:"integer", value:(int)payload.id, direction:0};
            sql:Parameter paraAddress = {sqlType:"varchar", value:(string)payload.address, direction:0};
            sql:Parameter[] paramsEmp = [paraAddress, paraID];
            system:println("Before update");
            i = sql:ClientConnector.update(connectionInstance, "update personsinlanka set address=? where personid=?", paramsEmp);
            if(i == 1){
                responsePayload = "Data Updating Successful";
            }
            else{
                abort;
            }
            system:println(strings:valueOf(i));
        }aborted {
            responsePayload = "Data Updating Failed";
            system:println("Inside if: aborted");

        }
    }catch(errors:Error ex){
        system:println("Database update error.");
    }
    close(connectionInstance);
    return;
}

function updateDBSqlError (json payload) (string responsePayload){
    int i;
    sql:ClientConnector connectionInstance = init();
    try{
        transaction {
            sql:Parameter paraID = {sqlType:"integer", value:(int)payload.id, direction:0};
            sql:Parameter paraAddress = {sqlType:"varchar", value:(string)payload.address, direction:0};
            sql:Parameter[] paramsEmp = [paraAddress, paraID];
            system:println("Before update");
            i = sql:ClientConnector.update(connectionInstance, "update personsinlanka set addressOfPerson=? where personid=?", paramsEmp);
            system:println(strings:valueOf(i));
        }aborted {
            responsePayload = "Data Updating Failed";
        }
        responsePayload = "Data Updating Successful";
    }catch(errors:Error ex){
        system:println("Database update error.");
    }
    close(connectionInstance);
    return;
}

function updateDBForceAbort (json payload) (string responsePayload){
    int i;
    sql:ClientConnector connectionInstance = init();
    try{
        transaction {
            sql:Parameter paraID = {sqlType:"integer", value:(int)payload.id, direction:0};
            sql:Parameter paraAddress = {sqlType:"varchar", value:(string)payload.address, direction:0};
            sql:Parameter[] paramsEmp = [paraAddress, paraID];
            system:println("Before update");
            if ((int)payload.id == 1){
                    abort;
             }
            i = sql:ClientConnector.update(connectionInstance, "update personsinlanka set address=? where personid=?", paramsEmp);
            if(i == 1){
                 responsePayload = "Data Updating Successful";
            }
            system:println(strings:valueOf(i));
        }aborted {
            responsePayload = "Data Updating Failed";
            system:println("Inside aborted");

        }
    }catch(errors:Error ex){
        system:println("Database update error.");
    }
    close(connectionInstance);
    return;
}

function updateMultipleDBSuccess (json payload) (string responsePayload){
    sql:ClientConnector connectionInstance = init();
    int i;
    int j;
    try{
        transaction {
            sql:Parameter paraID = {sqlType:"integer", value:(int)payload.id, direction:0};
            sql:Parameter paraAddress = {sqlType:"varchar", value:(string)payload.address, direction:0};
            sql:Parameter[] paramsEmp = [paraAddress, paraID];
            system:println("Before update");
            i = sql:ClientConnector.update(connectionInstance, "update personsinlanka set address=? where personid=?", paramsEmp);
            j = sql:ClientConnector.update(connectionInstance, "update personsinus set address=? where personid=?", paramsEmp);
            if(i == 1 && j ==1){
                responsePayload = "Data Updating Successful";
            }
            else{
                abort;
            }

            system:println("value of i"+ strings:valueOf(i));
            system:println("value of j"+ strings:valueOf(j));
        }aborted {
            responsePayload = "Data Updating Failed";

        }
    }catch(errors:Error ex){
        system:println("Database update error.");
    }
    close(connectionInstance);
    return;
}

function updateMultipleDBSqlError (json payload) (string responsePayload){
    int i;
    int j;
    sql:ClientConnector connectionInstance = init();
    try{
        transaction {
            sql:Parameter paraID = {sqlType:"integer", value:(int)payload.id, direction:0};
            sql:Parameter paraAddress = {sqlType:"varchar", value:(string)payload.address, direction:0};
            sql:Parameter[] paramsEmp = [paraAddress, paraID];
            system:println("Before update");
            i = sql:ClientConnector.update(connectionInstance, "update personsinlanka set addressOfPerson=? where personid=?", paramsEmp);
            j = sql:ClientConnector.update(connectionInstance, "update personsinus set addressInUs=? where personid=?", paramsEmp);
            system:println(strings:valueOf(i));
        }aborted {
            responsePayload = "Data Updating Failed";
        }
        responsePayload = "Data Updating Successful";
    }catch(errors:Error ex){
        system:println("Database update error.");
    }
    close(connectionInstance);
    return;
}

function updateMultipleForceAbort (json payload) (string responsePayload){
    sql:ClientConnector connectionInstance = init();
    int i;
    int j;
    try{
        transaction {
            sql:Parameter paraID = {sqlType:"integer", value:(int)payload.id, direction:0};
            sql:Parameter paraAddress = {sqlType:"varchar", value:(string)payload.address, direction:0};
            sql:Parameter[] paramsEmp = [paraAddress, paraID];
            system:println("Before update");
            i = sql:ClientConnector.update(connectionInstance, "update personsinlanka set address=? where personid=?", paramsEmp);
            if (!strings:equalsIgnoreCase((string)payload.city, "kentaky")){
                j = sql:ClientConnector.update(connectionInstance, "update personsinus set address=? where personid=?", paramsEmp);
            }
            else{
                abort;
            }
            if(i == 1 && j ==1){
                responsePayload = "Data Updating Successful";
            }
            system:println("value of i"+ strings:valueOf(i));
            system:println("value of j"+ strings:valueOf(j));
        }aborted {
            responsePayload = "Data Updating Failed";

        }
    }catch(errors:Error ex){
        system:println("Database update error.");
    }
    close(connectionInstance);
    return;
}