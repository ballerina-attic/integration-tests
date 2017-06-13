package resources.services;

import ballerina.data.sql;
import ballerina.lang.errors;
import ballerina.lang.system;
import ballerina.lang.strings;


function insertAndDeleteSameTable (json payload, string idToDelete) (string responsePayload){
    errors:Error e = {msg:"Data Insert Error"};
    sql:ClientConnector connectionInstance = init();
    try{
        transaction {
            sql:Parameter paraID = {sqlType:"integer", value:(int)payload.id, direction:0};
            sql:Parameter paraFirstName = {sqlType:"varchar", value:(string)payload.firstname, direction:0};
            sql:Parameter paraLastName = {sqlType:"varchar", value:(string)payload.lastname, direction:0};
            sql:Parameter paraAddress = {sqlType:"varchar", value:(string)payload.address, direction:0};
            sql:Parameter paraCity = {sqlType:"varchar", value:(string)payload.city, direction:0};
            sql:Parameter paraToDelete = {sqlType:"integer", value:(int)idToDelete, direction:0};
            sql:Parameter[] paramsEmp = [paraID, paraFirstName, paraLastName, paraAddress, paraCity];
            sql:Parameter[] paramsToDelete = [paraToDelete];
            sql:ClientConnector.update (connectionInstance, "Insert into personsinlanka(personid,firstname,lastname,address,city) values (?,?,?,?,?)", paramsEmp);
            int i = sql:ClientConnector.update (connectionInstance, "Delete from personsinlanka where personid=?", paramsToDelete);
            if (i < 1){
                  abort;
            }
            else{
                responsePayload = "Data Manipulation Successful";
            }

        }aborted {
            responsePayload = "Data Manipulation Failed";
        }
    }catch(errors:Error ex){
        system:println("Database manipulation error.");
    }
    close(connectionInstance);
    return;
}

function insertAndDeleteDiffTable (json payload, string idToDelete) (string responsePayload){
    errors:Error e = {msg:"Data Insert Error"};
    sql:ClientConnector connectionInstance = init();
    try{
        transaction {
            sql:Parameter paraID = {sqlType:"integer", value:(int)payload.id, direction:0};
            sql:Parameter paraFirstName = {sqlType:"varchar", value:(string)payload.firstname, direction:0};
            sql:Parameter paraLastName = {sqlType:"varchar", value:(string)payload.lastname, direction:0};
            sql:Parameter paraAddress = {sqlType:"varchar", value:(string)payload.address, direction:0};
            sql:Parameter paraCity = {sqlType:"varchar", value:(string)payload.city, direction:0};
            sql:Parameter paraToDelete = {sqlType:"integer", value:(int)idToDelete, direction:0};
            sql:Parameter[] paramsEmp = [paraID, paraFirstName, paraLastName, paraAddress, paraCity];
            sql:Parameter[] paramsToDelete = [paraToDelete];
            sql:ClientConnector.update (connectionInstance, "Insert into personsinlanka(personid,firstname,lastname,address,city) values (?,?,?,?,?)", paramsEmp);
            system:println("After insert");
            int i = sql:ClientConnector.update (connectionInstance, "Delete from personsinus where personid=?", paramsToDelete);
            system:println(strings:valueOf(i));
            if (i < 1){
                  abort;
            }
            else{
                responsePayload = "Data Manipulation Successful";
            }

        }aborted {
            responsePayload = "Data Manipulation Failed";
        }
    }catch(errors:Error ex){
        system:println("Database manipulation error.");
    }
    close(connectionInstance);
    return;
}

function insertAndDeleteSameTableSqlError (json payload, string idToDelete) (string responsePayload){
    errors:Error e = {msg:"Data Insert Error"};
    sql:ClientConnector connectionInstance = init();
    try{
        transaction {
            sql:Parameter paraID = {sqlType:"integer", value:(int)payload.id, direction:0};
            sql:Parameter paraFirstName = {sqlType:"varchar", value:(string)payload.firstname, direction:0};
            sql:Parameter paraLastName = {sqlType:"varchar", value:(string)payload.lastname, direction:0};
            sql:Parameter paraAddress = {sqlType:"varchar", value:(string)payload.address, direction:0};
            sql:Parameter paraCity = {sqlType:"varchar", value:(string)payload.city, direction:0};
            sql:Parameter paraToDelete = {sqlType:"integer", value:(int)idToDelete, direction:0};
            sql:Parameter[] paramsEmp = [paraID, paraFirstName, paraLastName, paraAddress, paraCity];
            sql:Parameter[] paramsToDelete = [paraToDelete];
            sql:ClientConnector.update (connectionInstance, "Insert into personsinlanka(personid,firstname,lastname,address,city) values (?,?,?,?,?)", paramsEmp);
            int i = sql:ClientConnector.update (connectionInstance, "Delete from personsinlanka where id=?", paramsToDelete);
            responsePayload = "Data Manipulation Successful";
        }aborted {
            responsePayload = "Data Manipulation Failed";
        }
    }catch(errors:Error ex){
        system:println("Database manipulation error.");
    }
    close(connectionInstance);
    return;
}

function insertAndDeleteDiffTableSqlError (json payload, string idToDelete) (string responsePayload){
    errors:Error e = {msg:"Data Insert Error"};
    sql:ClientConnector connectionInstance = init();
    try{
        transaction {
            sql:Parameter paraID = {sqlType:"integer", value:(int)payload.id, direction:0};
            sql:Parameter paraFirstName = {sqlType:"varchar", value:(string)payload.firstname, direction:0};
            sql:Parameter paraLastName = {sqlType:"varchar", value:(string)payload.lastname, direction:0};
            sql:Parameter paraAddress = {sqlType:"varchar", value:(string)payload.address, direction:0};
            sql:Parameter paraCity = {sqlType:"varchar", value:(string)payload.city, direction:0};
            sql:Parameter paraToDelete = {sqlType:"integer", value:(int)idToDelete, direction:0};
            sql:Parameter[] paramsEmp = [paraID, paraFirstName, paraLastName, paraAddress, paraCity];
            sql:Parameter[] paramsToDelete = [paraToDelete];
            sql:ClientConnector.update (connectionInstance, "Insert into personsinlanka(personid,firstname,lastname,address,city) values (?,?,?,?,?)", paramsEmp);
            int i = sql:ClientConnector.update (connectionInstance, "Delete from personsinus where id=?", paramsToDelete);
            responsePayload = "Data Manipulation Successful";
        }aborted {
            responsePayload = "Data Manipulation Failed";
        }
    }catch(errors:Error ex){
        system:println("Database manipulation error.");
    }
    close(connectionInstance);
    return;
}