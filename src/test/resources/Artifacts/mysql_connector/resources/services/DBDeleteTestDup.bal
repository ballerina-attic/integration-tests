package resources.services;

import ballerina.lang.errors;
import ballerina.data.sql;
import resources.connectorInit as conn;

sql:ClientConnector connectorInstanceOtherDelete = conn:initOther();

function deleteWithParamsOther (string query1, string valueToBeDeleted1) (int, errors:Error){

    sql:Parameter[] parameters = [];
    errors:Error err;
    int noOfRows;

    try {
        sql:Parameter para = {sqlType:"varchar", value:valueToBeDeleted1, direction:0};
        parameters = [para];
        noOfRows = connectorInstanceDelete.update (query1, parameters);
    } catch (errors:Error e) {
        err = e;
    }
    return noOfRows, err;
}
