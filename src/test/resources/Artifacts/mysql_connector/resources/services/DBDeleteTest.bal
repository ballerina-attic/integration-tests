package resources.services;

import ballerina.data.sql;
import resources.connectorInit as conn;

sql:ClientConnector connectorInstanceDelete = conn:init();

function deleteWithParams (string query, string valueToBeDeleted) (int, error){

    sql:Parameter[] parameters = [];
    error err;
    int noOfRows;

    try {
        sql:Parameter para = {sqlType:"varchar", value:valueToBeDeleted, direction:0};
        parameters = [para];
        noOfRows = connectorInstanceDelete.update (query, parameters);
    } catch (error e) {
        err = e;
    }
    return noOfRows, err;
}

function deleteGeneral (string query) (int, error){

    sql:Parameter[] parameters = [];
    error err;
    int noOfRows;

    try {
        noOfRows = connectorInstanceDelete.update (query, parameters);
    } catch (error e) {
        err = e;
    }
    return noOfRows, err;
}