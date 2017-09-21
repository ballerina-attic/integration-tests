package resources.services;

import ballerina.lang.errors;
import ballerina.data.sql;
import ballerina.lang.strings;
import ballerina.lang.datatables;
import resources.connectorInit as conn;

sql:ClientConnector connectorInstanceCall = conn:init();

struct ResultOrders{
    int customerNumber;
    string status;
    string location;
}

function createStoredProcedure(string procedure) (int, errors:Error){

    sql:Parameter[] parameters = [];
    int insertedRowCount;
    errors:Error err;

    try {
        insertedRowCount = connectorInstanceCall.update(procedure, parameters);
    } catch (errors:Error e) {
        string msg = "Error in procedure creation. Please retry";
        e = {msg:msg};
        err = e;
    }
    return insertedRowCount, err;
}

function callProcedureSuccess(int customerNo)(any, any, any, any, any, errors:Error){

    sql:Parameter[] parameters = [];
    errors:Error err;
    int inc = 5;
    int count = 0;
    sql:Parameter paraCustomerNo;
    sql:Parameter paraInc;
    sql:Parameter paraCount;
    sql:Parameter paraShipped;
    sql:Parameter paraCanceled;
    sql:Parameter paraResolved;
    sql:Parameter paraDisputed;

    try {
        paraCustomerNo = {sqlType:"integer", value:customerNo, direction:0};
        paraInc = {sqlType:"integer", value:inc, direction:0};
        paraCount = {sqlType:"integer", value:count, direction:2};
        paraShipped = {sqlType:"integer", direction:1};
        paraCanceled = {sqlType:"integer", direction:1};
        paraResolved = {sqlType:"integer", direction:1};
        paraDisputed = {sqlType:"integer", direction:1};
        parameters = [paraCustomerNo, paraInc, paraShipped, paraCanceled, paraResolved, paraDisputed, paraCount];
        datatable dt = connectorInstanceCall.call ("{call get_order_by_cust(?,?,?,?,?,?,?)}", parameters);
        datatables:close(dt);
    } catch (errors:Error e) {
        string msg = "Error in procedure call. Please retry";
        e = {msg:msg};
        err = e;
    }
    return paraShipped.value, paraCanceled.value, paraResolved.value, paraDisputed.value, paraCount.value, err;
}

function callProcedureWithWrongDirectionForParams(int customerNo, string status)(any, any, any, any, any, errors:Error){

    sql:Parameter[] parameters = [];
    errors:Error err;
    int inc = 5;
    int count = 0;
    sql:Parameter paraCustomerNo;
    sql:Parameter paraInc;
    sql:Parameter paraCount;
    sql:Parameter paraShipped;
    sql:Parameter paraCanceled;
    sql:Parameter paraResolved;
    sql:Parameter paraDisputed;

    try {
        if (strings:equalsIgnoreCase(status, "intoout")){
            paraCustomerNo = {sqlType:"integer", value:customerNo, direction:0};
            paraInc = {sqlType:"integer", value:inc, direction:1};
            paraCount = {sqlType:"integer", value:count, direction:2};
            paraShipped = {sqlType:"integer", direction:1};
            paraCanceled = {sqlType:"integer", direction:1};
            paraResolved = {sqlType:"integer", direction:1};
            paraDisputed = {sqlType:"integer", direction:1};
        }
        else if (strings:equalsIgnoreCase(status, "intoinout")){
            paraCustomerNo = {sqlType:"integer", value:customerNo, direction:0};
            paraInc = {sqlType:"integer", value:inc, direction:2};
            paraCount = {sqlType:"integer", value:count, direction:2};
            paraShipped = {sqlType:"integer", direction:1};
            paraCanceled = {sqlType:"integer", direction:1};
            paraResolved = {sqlType:"integer", direction:1};
            paraDisputed = {sqlType:"integer", direction:1};
        }
        else if (strings:equalsIgnoreCase(status, "outtoin")){
            paraCustomerNo = {sqlType:"integer", value:customerNo, direction:0};
            paraInc = {sqlType:"integer", value:inc, direction:0};
            paraCount = {sqlType:"integer", value:count, direction:2};
            paraShipped = {sqlType:"integer", direction:0};
            paraCanceled = {sqlType:"integer", direction:1};
            paraResolved = {sqlType:"integer", direction:1};
            paraDisputed = {sqlType:"integer", direction:1};
        }
        else if (strings:equalsIgnoreCase(status, "outtoinout")){
            paraCustomerNo = {sqlType:"integer", value:customerNo, direction:0};
            paraInc = {sqlType:"integer", value:inc, direction:0};
            paraCount = {sqlType:"integer", value:count, direction:2};
            paraShipped = {sqlType:"integer", direction:2};
            paraCanceled = {sqlType:"integer", direction:1};
            paraResolved = {sqlType:"integer", direction:1};
            paraDisputed = {sqlType:"integer", direction:1};
        }
        else if (strings:equalsIgnoreCase(status, "inouttoin")){
            paraCustomerNo = {sqlType:"integer", value:customerNo, direction:0};
            paraInc = {sqlType:"integer", value:inc, direction:0};
            paraCount = {sqlType:"integer", value:count, direction:0};
            paraShipped = {sqlType:"integer", direction:1};
            paraCanceled = {sqlType:"integer", direction:1};
            paraResolved = {sqlType:"integer", direction:1};
            paraDisputed = {sqlType:"integer", direction:1};
        }
        else if (strings:equalsIgnoreCase(status, "inouttoout")){
            paraCustomerNo = {sqlType:"integer", value:customerNo, direction:0};
            paraInc = {sqlType:"integer", value:inc, direction:0};
            paraCount = {sqlType:"integer", value:count, direction:1};
            paraShipped = {sqlType:"integer", direction:1};
            paraCanceled = {sqlType:"integer", direction:1};
            paraResolved = {sqlType:"integer", direction:1};
            paraDisputed = {sqlType:"integer", direction:1};
        }
        else{
            paraCustomerNo = {sqlType:"integer", value:customerNo, direction:0};
            paraInc = {sqlType:"integer", value:inc, direction:0};
            paraCount = {sqlType:"integer", value:count, direction:2};
            paraShipped = {sqlType:"integer", direction:1};
            paraCanceled = {sqlType:"integer", direction:1};
            paraResolved = {sqlType:"integer", direction:1};
            paraDisputed = {sqlType:"integer", direction:1};
        }
        parameters = [paraCustomerNo, paraInc, paraShipped, paraCanceled, paraResolved, paraDisputed, paraCount];
        datatable dt = connectorInstanceCall.call ("{call get_order_by_cust(?,?,?,?,?,?,?)}", parameters);
        datatables:close(dt);
    } catch (errors:Error e) {
        string msg = "Error in procedure call. Please retry";
        e = {msg:msg};
        err = e;
    }
    return paraShipped.value, paraCanceled.value, paraResolved.value, paraDisputed.value, paraCount.value, err;
}

function callProcedureWithLessInParams(int customerNo, string status)(any, any, any, any, any, errors:Error){

    sql:Parameter[] parameters = [];
    errors:Error err;
    int inc = 5;
    int count = 0;
    sql:Parameter paraCustomerNo;
    sql:Parameter paraInc;
    sql:Parameter paraCount;
    sql:Parameter paraShipped;
    sql:Parameter paraCanceled;
    sql:Parameter paraResolved;
    sql:Parameter paraDisputed;
    sql:Parameter test;

    try {
        paraCustomerNo = {sqlType:"integer", value:customerNo, direction:0};
        paraInc = {sqlType:"integer", value:inc, direction:0};
        paraCount = {sqlType:"integer", value:count, direction:2};
        paraShipped = {sqlType:"integer", direction:1};
        paraCanceled = {sqlType:"integer", direction:1};
        paraResolved = {sqlType:"integer", direction:1};
        paraDisputed = {sqlType:"integer", direction:1};
        parameters = [paraCustomerNo, test,  paraShipped, paraCanceled, paraResolved, paraDisputed, paraCount];
        if (strings:equalsIgnoreCase(status, "select")){
            parameters = [test, paraInc,  paraShipped, paraCanceled, paraResolved, paraDisputed, paraCount];
        }
        else{
            parameters = [paraCustomerNo, test,  paraShipped, paraCanceled, paraResolved, paraDisputed, paraCount];
        }
        datatable dt = connectorInstanceCall.call ("{call get_order_by_cust(?,?,?,?,?,?,?)}", parameters);
        datatables:close(dt);
    } catch (errors:Error e) {
        string msg = "Error in procedure call. Please retry";
        e = {msg:msg};
        err = e;
    }
    return paraShipped.value, paraCanceled.value, paraResolved.value, paraDisputed.value, paraCount.value, err;
}

function callProcedureWithLessOutParams(int customerNo)(any, any, any, any, any, errors:Error){

    sql:Parameter[] parameters = [];
    errors:Error err;
    int inc = 5;
    int count = 0;
    sql:Parameter paraCustomerNo;
    sql:Parameter paraInc;
    sql:Parameter paraCount;
    sql:Parameter paraShipped;
    sql:Parameter paraCanceled;
    sql:Parameter paraResolved;
    sql:Parameter paraDisputed;
    sql:Parameter test;

    try {
        paraCustomerNo = {sqlType:"integer", value:customerNo, direction:0};
        paraInc = {sqlType:"integer", value:inc, direction:0};
        paraCount = {sqlType:"integer", value:count, direction:2};
        paraShipped = {sqlType:"integer", direction:1};
        paraCanceled = {sqlType:"integer", direction:1};
        paraResolved = {sqlType:"integer", direction:1};
        paraDisputed = {sqlType:"integer", direction:1};
        parameters = [paraCustomerNo, paraInc,  test, paraCanceled, paraResolved, paraDisputed, paraCount];
        datatable dt = connectorInstanceCall.call ("{call get_order_by_cust(?,?,?,?,?,?,?)}", parameters);
        datatables:close(dt);
    } catch (errors:Error e) {
        string msg = "Error in procedure call. Please retry";
        e = {msg:msg};
        err = e;
    }
    return paraShipped.value, paraCanceled.value, paraResolved.value, paraDisputed.value, paraCount.value, err;
}

function callProcedureWithLessInOutParams(int customerNo)(any, any, any, any, any, errors:Error){

    sql:Parameter[] parameters = [];
    errors:Error err;
    int inc = 5;
    int count = 0;
    sql:Parameter paraCustomerNo;
    sql:Parameter paraInc;
    sql:Parameter paraCount;
    sql:Parameter paraShipped;
    sql:Parameter paraCanceled;
    sql:Parameter paraResolved;
    sql:Parameter paraDisputed;
    sql:Parameter test;

    try {
        paraCustomerNo = {sqlType:"integer", value:customerNo, direction:0};
        paraInc = {sqlType:"integer", value:inc, direction:0};
        paraCount = {sqlType:"integer", value:count, direction:2};
        paraShipped = {sqlType:"integer", direction:1};
        paraCanceled = {sqlType:"integer", direction:1};
        paraResolved = {sqlType:"integer", direction:1};
        paraDisputed = {sqlType:"integer", direction:1};
        parameters = [paraCustomerNo, paraInc,  paraShipped, paraCanceled, paraResolved, paraDisputed, test];
        datatable dt = connectorInstanceCall.call ("{call get_order_by_cust(?,?,?,?,?,?,?)}", parameters);
        datatables:close(dt);
    } catch (errors:Error e) {
        string msg = "Error in procedure call. Please retry";
        e = {msg:msg};
        err = e;
    }
    return paraShipped.value, paraCanceled.value, paraResolved.value, paraDisputed.value, paraCount.value, err;
}

function callProcedureWithMismatchingParams(int customerNo, string status)(any, any, any, any, any, errors:Error){

    sql:Parameter[] parameters = [];
    errors:Error err;
    int inc = 5;
    int count = 0;
    sql:Parameter paraCustomerNo;
    sql:Parameter paraInc;
    sql:Parameter paraCount;
    sql:Parameter paraShipped;
    sql:Parameter paraCanceled;
    sql:Parameter paraResolved;
    sql:Parameter paraDisputed;

    try {
        if (strings:equalsIgnoreCase(status, "invaluenotchanged")){
            paraCustomerNo = {sqlType:"integer", value:customerNo, direction:0};
            paraInc = {sqlType:"varchar", value:inc, direction:0};
            paraCount = {sqlType:"integer", value:count, direction:2};
            paraShipped = {sqlType:"integer", direction:1};
            paraCanceled = {sqlType:"integer", direction:1};
            paraResolved = {sqlType:"integer", direction:1};
            paraDisputed = {sqlType:"integer", direction:1};
        }
        else if (strings:equalsIgnoreCase(status, "invaluechanged")){
            paraCustomerNo = {sqlType:"integer", value:customerNo, direction:0};
            paraInc = {sqlType:"varchar", value:"test", direction:0};
            paraCount = {sqlType:"integer", value:count, direction:2};
            paraShipped = {sqlType:"integer", direction:1};
            paraCanceled = {sqlType:"integer", direction:1};
            paraResolved = {sqlType:"integer", direction:1};
            paraDisputed = {sqlType:"integer", direction:1};
        }
        else if(strings:equalsIgnoreCase(status, "inonlyvaluechanged")){
            paraCustomerNo = {sqlType:"integer", value:customerNo, direction:0};
            paraInc = {sqlType:"integer", value:"test", direction:0};
            paraCount = {sqlType:"integer", value:count, direction:2};
            paraShipped = {sqlType:"integer", direction:1};
            paraCanceled = {sqlType:"integer", direction:1};
            paraResolved = {sqlType:"integer", direction:1};
            paraDisputed = {sqlType:"integer", direction:1};
        }
        else if (strings:equalsIgnoreCase(status, "out")){
            paraCustomerNo = {sqlType:"integer", value:customerNo, direction:0};
            paraInc = {sqlType:"integer", value:inc, direction:0};
            paraCount = {sqlType:"integer", value:count, direction:2};
            paraShipped = {sqlType:"varchar", direction:1};
            paraCanceled = {sqlType:"integer", direction:1};
            paraResolved = {sqlType:"integer", direction:1};
            paraDisputed = {sqlType:"integer", direction:1};
        }
        else if (strings:equalsIgnoreCase(status, "inoutvaluenotchanged")){
            paraCustomerNo = {sqlType:"integer", value:customerNo, direction:0};
            paraInc = {sqlType:"integer", value:inc, direction:0};
            paraCount = {sqlType:"varchar", value:count, direction:2};
            paraShipped = {sqlType:"integer", direction:1};
            paraCanceled = {sqlType:"integer", direction:1};
            paraResolved = {sqlType:"integer", direction:1};
            paraDisputed = {sqlType:"integer", direction:1};
        }
        else if (strings:equalsIgnoreCase(status, "inoutvaluechanged")){
            paraCustomerNo = {sqlType:"integer", value:customerNo, direction:0};
            paraInc = {sqlType:"integer", value:inc, direction:0};
            paraCount = {sqlType:"varchar", value:"test", direction:2};
            paraShipped = {sqlType:"integer", direction:1};
            paraCanceled = {sqlType:"integer", direction:1};
            paraResolved = {sqlType:"integer", direction:1};
            paraDisputed = {sqlType:"integer", direction:1};
        }
        else if (strings:equalsIgnoreCase(status, "inoutonlyvaluechanged")){
            paraCustomerNo = {sqlType:"integer", value:customerNo, direction:0};
            paraInc = {sqlType:"integer", value:inc, direction:0};
            paraCount = {sqlType:"integer", value:"test", direction:2};
            paraShipped = {sqlType:"integer", direction:1};
            paraCanceled = {sqlType:"integer", direction:1};
            paraResolved = {sqlType:"integer", direction:1};
            paraDisputed = {sqlType:"integer", direction:1};
        }
        else{
            paraCustomerNo = {sqlType:"integer", value:customerNo, direction:0};
            paraInc = {sqlType:"integer", value:inc, direction:0};
            paraCount = {sqlType:"integer", value:count, direction:2};
            paraShipped = {sqlType:"integer", direction:1};
            paraCanceled = {sqlType:"integer", direction:1};
            paraResolved = {sqlType:"integer", direction:1};
            paraDisputed = {sqlType:"integer", direction:1};
        }
        parameters = [paraCustomerNo, paraInc, paraShipped, paraCanceled, paraResolved, paraDisputed, paraCount];
        datatable dt = connectorInstanceCall.call ("{call get_order_by_cust(?,?,?,?,?,?,?)}", parameters);
        datatables:close(dt);
    } catch (errors:Error e) {
        string msg = "Error in procedure call. Please retry";
        e = {msg:msg};
        err = e;
    }
    return paraShipped.value, paraCanceled.value, paraResolved.value, paraDisputed.value, paraCount.value, err;
}

function callProcedureToGetResultSet(int customerNo)(json, errors:Error){

    sql:Parameter[] parameters = [];
    errors:Error err;
    errors:TypeCastError typeErr;
    int inc = 5;
    int count = 0;
    sql:Parameter paraCustomerNo;
    sql:Parameter paraInc;
    sql:Parameter paraCount;
    sql:Parameter paraShipped;
    sql:Parameter paraCanceled;
    sql:Parameter paraResolved;
    sql:Parameter paraDisputed;
    datatable dt;
    json result;

    try {
        paraCustomerNo = {sqlType:"integer", value:customerNo, direction:0};
        paraInc = {sqlType:"integer", value:inc, direction:0};
        paraCount = {sqlType:"integer", value:count, direction:2};
        paraShipped = {sqlType:"integer", direction:1};
        paraCanceled = {sqlType:"integer", direction:1};
        paraResolved = {sqlType:"integer", direction:1};
        paraDisputed = {sqlType:"integer", direction:1};
        parameters = [paraCustomerNo, paraInc, paraShipped, paraCanceled, paraResolved, paraDisputed, paraCount];
        dt = connectorInstanceCall.call ("{call get_order_by_cust(?,?,?,?,?,?,?)}", parameters);
        result, _ = <json>dt;
    } catch (errors:Error e) {
        string msg = "Error in procedure call. Please retry";
        e = {msg:msg};
        err = e;
    }
    return result, err;
}

