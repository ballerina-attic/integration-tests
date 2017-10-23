package resources.services;

import ballerina.data.sql;
import resources.connectorInit as conn;

sql:ClientConnector connectorInstanceUpdate = conn:init();

function updateWithParams (string query, string value1, float value2) (int, error){

    sql:Parameter[] parameters = [];
    error err;
    int noOfRows;

    try {
        sql:Parameter para1 = {sqlType:"varchar", value:value1, direction:0};
        sql:Parameter para2 = {sqlType:"double", value:value2, direction:0};
        parameters = [para1, para2];
        noOfRows = connectorInstanceUpdate.update (query, parameters);
    } catch (error e) {
        string msg = "Error in database update. Please retry";
        err = {msg:msg};
    }
    return noOfRows, err;
}

function updateWithMissingParams (string query, string value1, float value2) (int, error){

    sql:Parameter[] parameters = [];
    error err;
    int noOfRows;

    try {
        sql:Parameter para1 = {sqlType:"varchar", value:value1, direction:0};
        parameters = [para1];
        noOfRows = connectorInstanceUpdate.update (query, parameters);
    } catch (error e) {
        string msg = "Error in database update. Please retry";
        err = {msg:msg};
    }
    return noOfRows, err;
}

function updateWithoutParams (string query) (int, error){

    sql:Parameter[] parameters = [];
    error err;
    int noOfRows;

    try {
        noOfRows = connectorInstanceUpdate.update (query, parameters);
    } catch (error e) {
        err = e;
    }
    return noOfRows, err;
}