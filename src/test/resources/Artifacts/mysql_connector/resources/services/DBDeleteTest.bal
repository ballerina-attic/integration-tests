package resources.services;

import ballerina.lang.errors;
import ballerina.data.sql;

function deleteWithParams (string query, string valueToBeDeleted) (int, errors:Error){

    sql:Parameter[] parameters = [];
    errors:Error err;
    int noOfRows;

    try {
        sql:Parameter para = {sqlType:"varchar", value:valueToBeDeleted, direction:0};
        parameters = [para];
        noOfRows = connectorInstance.update (query, parameters);
    } catch (errors:Error e) {
        err = e;
    }
    return noOfRows, err;
}

function deleteGeneral (string query) (int, errors:Error){

    sql:Parameter[] parameters = [];
    errors:Error err;
    int noOfRows;

    try {
        noOfRows = connectorInstance.update (query, parameters);
    } catch (errors:Error e) {
        err = e;
    }
    return noOfRows, err;
}