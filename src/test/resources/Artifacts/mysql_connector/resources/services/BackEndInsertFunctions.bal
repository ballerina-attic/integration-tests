package resources.services;

import ballerina.data.sql;
import ballerina.lang.errors;
import ballerina.lang.system;
import ballerina.lang.strings;

int returnVal = 0;

function insertToDBSuccess (json payload) (string responsePayload){
    errors:Error e = {msg:"Data Insert Error"};
    sql:ClientConnector connectionInstance = init();
    try{
        transaction {
            sql:Parameter paraID = {sqlType:"integer", value:(int)payload.id, direction:0};
            sql:Parameter paraFirstName = {sqlType:"varchar", value:(string)payload.firstname, direction:0};
            sql:Parameter paraLastName = {sqlType:"varchar", value:(string)payload.lastname, direction:0};
            sql:Parameter paraAddress = {sqlType:"varchar", value:(string)payload.address, direction:0};
            sql:Parameter paraCity = {sqlType:"varchar", value:(string)payload.city, direction:0};
            sql:Parameter[] paramsEmp = [paraID, paraFirstName, paraLastName, paraAddress, paraCity];
            sql:ClientConnector.update (connectionInstance, "Insert into personsinlanka(personid,firstname,lastname,address,city) values (?,?,?,?,?)", paramsEmp);
        } aborted {
            returnVal = -1;
            responsePayload = "Data Insertion Failed";
        }
        responsePayload = "Data Insertion Successful";
    }catch(errors:Error ex){
        system:println("Database insert error.");
    }
    close(connectionInstance);
    return;
}

function insertToDBSqlError (json payload) (string responsePayload){
    errors:Error e = {msg:"Data Insert Error"};
    sql:ClientConnector connectionInstance = init();
    try{
        transaction {
            sql:Parameter paraID = {sqlType:"integer", value:(int)payload.id, direction:0};
            sql:Parameter paraFirstName = {sqlType:"varchar", value:(string)payload.firstname, direction:0};
            sql:Parameter paraLastName = {sqlType:"varchar", value:(string)payload.lastname, direction:0};
            sql:Parameter paraAddress = {sqlType:"varchar", value:(string)payload.address, direction:0};
            sql:Parameter paraCity = {sqlType:"varchar", value:(string)payload.city, direction:0};
            sql:Parameter[] paramsEmp = [paraID, paraFirstName, paraLastName, paraAddress, paraCity];
            sql:ClientConnector.update (connectionInstance, "Insert into personsinlanka(id,firstname,lastname,address,city) values (?,?,?,?,?)", paramsEmp);
        }aborted {
            returnVal = -1;
            responsePayload = "Data Insertion Failed";
        }
        responsePayload = "Data Insertion Successful";
    }catch(errors:Error ex){
        system:println("Database insert error.");
    }
    close(connectionInstance);
    return;
}

function insertToDBForceAbort (json payload) (string responsePayload){
    errors:Error e = {msg:"Data Insert Error"};
    sql:ClientConnector connectionInstance = init();
    try{
        transaction {
            sql:Parameter paraID = {sqlType:"integer", value:(int)payload.id, direction:0};
            sql:Parameter paraFirstName = {sqlType:"varchar", value:(string)payload.firstname, direction:0};
            sql:Parameter paraLastName = {sqlType:"varchar", value:(string)payload.lastname, direction:0};
            sql:Parameter paraAddress = {sqlType:"varchar", value:(string)payload.address, direction:0};
            sql:Parameter paraCity = {sqlType:"varchar", value:(string)payload.city, direction:0};
            sql:Parameter[] paramsEmp = [paraID, paraFirstName, paraLastName, paraAddress, paraCity];
            if((int)payload.id < 0){
                    system:println("Near abort statement");
                    abort;
            }
            sql:ClientConnector.update (connectionInstance, "Insert into personsinlanka(personid,firstname,lastname,address,city) values (?,?,?,?,?)", paramsEmp);
            responsePayload = "Data Insertion Successful";
        }aborted {
            returnVal = -1;
            responsePayload = "Data Insertion Failed";
            system:println("After abort, inside aborted");
        }
    }catch(errors:Error ex){
        system:println("Database insert error.");
    }
    close(connectionInstance);
    return;
}

function insertToMultipleDbSuccess (json payload) (string responsePayload){
    errors:Error e = {msg:"Data Insert Error"};
    sql:ClientConnector connectionInstance = init();
    try{
        transaction {
            sql:Parameter paraID = {sqlType:"integer", value:(int)payload.id, direction:0};
            sql:Parameter paraFirstName = {sqlType:"varchar", value:(string)payload.firstname, direction:0};
            sql:Parameter paraLastName = {sqlType:"varchar", value:(string)payload.lastname, direction:0};
            sql:Parameter paraAddress = {sqlType:"varchar", value:(string)payload.address, direction:0};
            sql:Parameter paraCity = {sqlType:"varchar", value:(string)payload.city, direction:0};
            sql:Parameter[] paramsEmp = [paraID, paraFirstName, paraLastName, paraAddress, paraCity];
            sql:ClientConnector.update (connectionInstance, "Insert into personsinlanka(personid,firstname,lastname,address,city) values (?,?,?,?,?)", paramsEmp);
            sql:ClientConnector.update (connectionInstance, "Insert into personsinus(personid,firstname,lastname,address,city) values (?,?,?,?,?)", paramsEmp);
        }aborted {
            returnVal = -1;
            responsePayload = "Data Insertion Failed";
        }
        responsePayload = "Data Insertion Successful";
    }catch(errors:Error ex){
        system:println("Database insert error.");
    }
    close(connectionInstance);
    return;
}

function insertToMultipleDbSqlError (json payload) (string responsePayload){
    errors:Error e = {msg:"Data Insert Error"};
    sql:ClientConnector connectionInstance = init();
    try{
        transaction {
            sql:Parameter paraID = {sqlType:"integer", value:(int)payload.id, direction:0};
            sql:Parameter paraFirstName = {sqlType:"varchar", value:(string)payload.firstname, direction:0};
            sql:Parameter paraLastName = {sqlType:"varchar", value:(string)payload.lastname, direction:0};
            sql:Parameter paraAddress = {sqlType:"varchar", value:(string)payload.address, direction:0};
            sql:Parameter paraCity = {sqlType:"varchar", value:(string)payload.city, direction:0};
            sql:Parameter[] paramsEmp = [paraID, paraFirstName, paraLastName, paraAddress, paraCity];
            sql:ClientConnector.update (connectionInstance, "Insert into personsinlanka(personid,firstname,lastname,address,city) values (?,?,?,?,?)", paramsEmp);
            sql:ClientConnector.update (connectionInstance, "Insert into personsinus(id,firstname,lastname,address,city) values (?,?,?,?,?)", paramsEmp);
        }aborted {
            returnVal = -1;
            responsePayload = "Data Insertion Failed";
        }
        responsePayload = "Data Insertion Successful";
    }catch(errors:Error ex){
        system:println("Database insert error.");
    }
    close(connectionInstance);
    return;
}

function insertToMultipleDbForceAbort (json payload) (string responsePayload){
    errors:Error e = {msg:"Data Insert Error"};
    sql:ClientConnector connectionInstance = init();
    try{
        transaction {
            sql:Parameter paraID = {sqlType:"integer", value:(int)payload.id, direction:0};
            sql:Parameter paraFirstName = {sqlType:"varchar", value:(string)payload.firstname, direction:0};
            sql:Parameter paraLastName = {sqlType:"varchar", value:(string)payload.lastname, direction:0};
            sql:Parameter paraAddress = {sqlType:"varchar", value:(string)payload.address, direction:0};
            sql:Parameter paraCity = {sqlType:"varchar", value:(string)payload.city, direction:0};
            sql:Parameter[] paramsEmp = [paraID, paraFirstName, paraLastName, paraAddress, paraCity];
            sql:ClientConnector.update (connectionInstance, "Insert into personsinlanka(personid,firstname,lastname,address,city) values (?,?,?,?,?)", paramsEmp);
            if (!strings:equalsIgnoreCase((string)payload.city, "kentaky")){
                sql:ClientConnector.update (connectionInstance, "Insert into personsinus(personid,firstname,lastname,address,city) values (?,?,?,?,?)", paramsEmp);
             }
            else{
                abort;
            }

            responsePayload = "Data Insertion Successful";
        }aborted {
            returnVal = -1;
            responsePayload = "Data Insertion Failed";
        }
    }catch(errors:Error ex){
        system:println("Database insert error.");
    }
    close(connectionInstance);
    return;
}