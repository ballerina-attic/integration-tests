package resources.services;

import ballerina.lang.errors;
import ballerina.data.sql;

function updateWithParams (string query, string value1, float value2) (int, errors:Error){

    sql:Parameter[] parameters = [];
    errors:Error err;
    int noOfRows;

    try {
        sql:Parameter para1 = {sqlType:"varchar", value:value1, direction:0};
        sql:Parameter para2 = {sqlType:"double", value:value2, direction:0};
        parameters = [para1, para2];
        noOfRows = connectorInstance.update (query, parameters);
    } catch (errors:Error e) {
        string msg = "Error in database update. Please retry";
        err = {msg:msg};
    }
    return noOfRows, err;
}

function updateWithMissingParams (string query, string value1, float value2) (int, errors:Error){

    sql:Parameter[] parameters = [];
    errors:Error err;
    int noOfRows;

    try {
        sql:Parameter para1 = {sqlType:"varchar", value:value1, direction:0};
        parameters = [para1];
        noOfRows = connectorInstance.update (query, parameters);
    } catch (errors:Error e) {
        string msg = "Error in database update. Please retry";
        err = {msg:msg};
    }
    return noOfRows, err;
}

function updateWithoutParams (string query) (int, errors:Error){

    sql:Parameter[] parameters = [];
    errors:Error err;
    int noOfRows;

    try {
        noOfRows = connectorInstance.update (query, parameters);
    } catch (errors:Error e) {
        string msg = "Error in database update. Please retry";
        err = {msg:msg};
    }
    return noOfRows, err;
}