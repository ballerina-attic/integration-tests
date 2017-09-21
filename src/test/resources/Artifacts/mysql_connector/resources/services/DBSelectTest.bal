package resources.services;

import ballerina.lang.errors;
import ballerina.data.sql;
import ballerina.lang.datatables;
import resources.connectorInit as conn;

sql:ClientConnector connectorInstanceSelect = conn:init();

struct ResultCount{
    int COUNTTENPERCENT;
}

function selectGeneral (string query) (json, errors:Error){

    sql:Parameter[] parameters = [];
    errors:Error err;
    json data = "e";

    try {
        datatable dt = connectorInstanceSelect.select (query, parameters);
        data, _ = <json>dt;
    } catch (errors:Error e) {
        err = e;
    }
    return data, err;
}

function selectBetween (string query) (json, errors:Error){

    sql:Parameter[] parameters = [];
    errors:Error err;
    json data;

    try {
        sql:Parameter para1 = {sqlType:"double", value:5500.50, direction:0};
        sql:Parameter para2 = {sqlType:"double", value:11350.50, direction:0};
        parameters = [para1, para2];
        datatable dt = connectorInstanceSelect.select (query, parameters);
        data, _ = <json>dt;
    } catch (errors:Error e) {
        err = e;
    }
    return data, err;
}

function selectLike (string query) (json, errors:Error){

    sql:Parameter[] parameters = [];
    errors:Error err;
    json data;
    string value = "or";
    string likeValue = string `%{{value}}%`;

    try {
        sql:Parameter para = {sqlType:"varchar", value:likeValue, direction:0};
        parameters = [para];
        datatable dt = connectorInstanceSelect.select (query, parameters);
        data, _ = <json>dt;
    } catch (errors:Error e) {
        err = e;
    }
    return data, err;
}

function selectIn (string query) (json, errors:Error){

    sql:Parameter[] parameters = [];
    errors:Error err;
    json data;
    string [] in = ["Germany", "UK"];
    try {
        sql:Parameter para = {sqlType:"varchar", value:in, direction:0};
        parameters = [para];
        datatable dt = connectorInstanceSelect.select (query, parameters);
        data, _ = <json>dt;
    } catch (errors:Error e) {
        err = e;
    }
    return data, err;
}

function selectAndOr (string query) (json, errors:Error){

    sql:Parameter[] parameters = [];
    errors:Error err;
    json data;

    try {
        sql:Parameter para1 = {sqlType:"varchar", value:"Berlin", direction:0};
        sql:Parameter para2 = {sqlType:"varchar", value:"München", direction:0};
        sql:Parameter para3 = {sqlType:"varchar", value:"Germany", direction:0};
        parameters = [para3, para1, para2];
        datatable dt = connectorInstanceSelect.select (query, parameters);
        data, _ = <json>dt;
    } catch (errors:Error e) {
        err = e;
    }
    return data, err;
}

function selectWithLimit () (json, errors:Error){

    sql:Parameter[] parameters = [];
    errors:Error err;
    errors:TypeCastError ex;
    json data;
    int count;

    try {
        datatable dt = connectorInstanceSelect.select ("select CEIL(count(CustomerID)*50/100) as countTenPercent from Customers", parameters);
        ResultCount rs;
        while (datatables:hasNext(dt)) {
            any dataStruct = datatables:next(dt);
            rs, ex = (ResultCount) dataStruct;
            count = rs.COUNTTENPERCENT;
        }
        sql:Parameter para = {sqlType:"integer", value:count, direction:0};
        parameters = [para];
        dt = connectorInstanceSelect.select ("select CustomerName from Customers ORDER BY TotalPurchases DESC limit 4", parameters);
        data, _ = <json>dt;
    } catch (errors:Error e) {
        err = e;
    }
    return data, err;
}

function selectWithExists (string query) (json, errors:Error){

    sql:Parameter[] parameters = [];
    errors:Error err;
    json data;

    try {
        sql:Parameter para = {sqlType:"integer", value:20, direction:0};
        parameters = [para];
        datatable dt = connectorInstanceSelect.select (query, parameters);
        data, _ = <json>dt;
    } catch (errors:Error e) {
        err = e;
    }
    return data, err;
}

function selectWithComplexSql () (json, errors:Error){

    sql:Parameter[] parameters = [];
    errors:Error err;
    json data;

    try {
        sql:Parameter para1 = {sqlType:"integer", value:3, direction:0};
        sql:Parameter para2 = {sqlType:"integer", value:0, direction:0};
        parameters = [para1, para2];
        datatable dt = connectorInstanceSelect.select ("select Country, TRUNCATE(MAX(LoyaltyPoints/TotalPurchases), ?) as MaxBuyingRatio from Customers where TotalPurchases > ? group by Country", parameters);
        data, _ = <json>dt;
    } catch (errors:Error e) {
        err = e;
    }
    return data, err;
}

function selectGeneralToXml (string query) (xml, errors:Error){

    sql:Parameter[] parameters = [];
    errors:Error err;
    xml data;

    try {
        datatable dt = connectorInstance.select (query, parameters);
        data, _ = <xml>dt;
    } catch (errors:Error e) {
        err = e;
    }
    return data, err;
}
