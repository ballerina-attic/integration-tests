package resources.services;

import ballerina.data.sql;
import resources.connectorInit as conn;

sql:ClientConnector connectorInstanceCall = conn:init();

struct ResultOrders{
    int customerNumber;
    string status;
    string location;
}

function createStoredProcedure(string procedure) (int, error){

    sql:Parameter[] parameters = [];
    int insertedRowCount;
    error err;

    try {
        insertedRowCount = connectorInstanceCall.update(procedure, parameters);
    } catch (error e) {
        string msg = "Error in procedure creation. Please retry";
        e = {msg:msg};
        err = e;
    }
    return insertedRowCount, err;
}

function callProcedureSuccess(int customerNo)(any, any, any, any, any, error){

    sql:Parameter[] parameters = [];
    error err;
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
        dt.close();
    } catch (error e) {
        string msg = "Error in procedure call. Please retry";
        e = {msg:msg};
        err = e;
    }
    return paraShipped.value, paraCanceled.value, paraResolved.value, paraDisputed.value, paraCount.value, err;
}

function callProcedureWithWrongDirectionForParams(int customerNo, string status)(any, any, any, any, any, error){

    sql:Parameter[] parameters = [];
    error err;
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
        if (status.equalsIgnoreCase("intoout")){
            paraCustomerNo = {sqlType:"integer", value:customerNo, direction:0};
            paraInc = {sqlType:"integer", value:inc, direction:1};
            paraCount = {sqlType:"integer", value:count, direction:2};
            paraShipped = {sqlType:"integer", direction:1};
            paraCanceled = {sqlType:"integer", direction:1};
            paraResolved = {sqlType:"integer", direction:1};
            paraDisputed = {sqlType:"integer", direction:1};
        }
        else if (status.equalsIgnoreCase("intoinout")){
            paraCustomerNo = {sqlType:"integer", value:customerNo, direction:0};
            paraInc = {sqlType:"integer", value:inc, direction:2};
            paraCount = {sqlType:"integer", value:count, direction:2};
            paraShipped = {sqlType:"integer", direction:1};
            paraCanceled = {sqlType:"integer", direction:1};
            paraResolved = {sqlType:"integer", direction:1};
            paraDisputed = {sqlType:"integer", direction:1};
        }
        else if (status.equalsIgnoreCase("outtoin")){
            paraCustomerNo = {sqlType:"integer", value:customerNo, direction:0};
            paraInc = {sqlType:"integer", value:inc, direction:0};
            paraCount = {sqlType:"integer", value:count, direction:2};
            paraShipped = {sqlType:"integer", direction:0};
            paraCanceled = {sqlType:"integer", direction:1};
            paraResolved = {sqlType:"integer", direction:1};
            paraDisputed = {sqlType:"integer", direction:1};
        }
        else if (status.equalsIgnoreCase("outtoinout")){
            paraCustomerNo = {sqlType:"integer", value:customerNo, direction:0};
            paraInc = {sqlType:"integer", value:inc, direction:0};
            paraCount = {sqlType:"integer", value:count, direction:2};
            paraShipped = {sqlType:"integer", direction:2};
            paraCanceled = {sqlType:"integer", direction:1};
            paraResolved = {sqlType:"integer", direction:1};
            paraDisputed = {sqlType:"integer", direction:1};
        }
        else if (status.equalsIgnoreCase("inouttoin")){
            paraCustomerNo = {sqlType:"integer", value:customerNo, direction:0};
            paraInc = {sqlType:"integer", value:inc, direction:0};
            paraCount = {sqlType:"integer", value:count, direction:0};
            paraShipped = {sqlType:"integer", direction:1};
            paraCanceled = {sqlType:"integer", direction:1};
            paraResolved = {sqlType:"integer", direction:1};
            paraDisputed = {sqlType:"integer", direction:1};
        }
        else if (status.equalsIgnoreCase("inouttoout")){
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
        dt.close();
    } catch (error e) {
        string msg = "Error in procedure call. Please retry";
        e = {msg:msg};
        err = e;
    }
    return paraShipped.value, paraCanceled.value, paraResolved.value, paraDisputed.value, paraCount.value, err;
}

function callProcedureWithLessInParams(int customerNo, string status)(any, any, any, any, any, error){

    sql:Parameter[] parameters = [];
    error err;
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
        if (status.equalsIgnoreCase("select")){
            parameters = [test, paraInc,  paraShipped, paraCanceled, paraResolved, paraDisputed, paraCount];
        }
        else{
            parameters = [paraCustomerNo, test,  paraShipped, paraCanceled, paraResolved, paraDisputed, paraCount];
        }
        datatable dt = connectorInstanceCall.call ("{call get_order_by_cust(?,?,?,?,?,?,?)}", parameters);
        dt.close();
    } catch (error e) {
        string msg = "Error in procedure call. Please retry";
        e = {msg:msg};
        err = e;
    }
    return paraShipped.value, paraCanceled.value, paraResolved.value, paraDisputed.value, paraCount.value, err;
}

function callProcedureWithLessOutParams(int customerNo)(any, any, any, any, any, error){

    sql:Parameter[] parameters = [];
    error err;
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
        dt.close();
    } catch (error e) {
        string msg = "Error in procedure call. Please retry";
        e = {msg:msg};
        err = e;
    }
    return paraShipped.value, paraCanceled.value, paraResolved.value, paraDisputed.value, paraCount.value, err;
}

function callProcedureWithLessInOutParams(int customerNo)(any, any, any, any, any, error){

    sql:Parameter[] parameters = [];
    error err;
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
        dt.close();
    } catch (error e) {
        string msg = "Error in procedure call. Please retry";
        e = {msg:msg};
        err = e;
    }
    return paraShipped.value, paraCanceled.value, paraResolved.value, paraDisputed.value, paraCount.value, err;
}

function callProcedureWithMismatchingParams(int customerNo, string status)(any, any, any, any, any, error){

    sql:Parameter[] parameters = [];
    error err;
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
        if (status.equalsIgnoreCase("invaluenotchanged")){
            paraCustomerNo = {sqlType:"integer", value:customerNo, direction:0};
            paraInc = {sqlType:"varchar", value:inc, direction:0};
            paraCount = {sqlType:"integer", value:count, direction:2};
            paraShipped = {sqlType:"integer", direction:1};
            paraCanceled = {sqlType:"integer", direction:1};
            paraResolved = {sqlType:"integer", direction:1};
            paraDisputed = {sqlType:"integer", direction:1};
        }
        else if (status.equalsIgnoreCase("invaluechanged")){
            paraCustomerNo = {sqlType:"integer", value:customerNo, direction:0};
            paraInc = {sqlType:"varchar", value:"test", direction:0};
            paraCount = {sqlType:"integer", value:count, direction:2};
            paraShipped = {sqlType:"integer", direction:1};
            paraCanceled = {sqlType:"integer", direction:1};
            paraResolved = {sqlType:"integer", direction:1};
            paraDisputed = {sqlType:"integer", direction:1};
        }
        else if(status.equalsIgnoreCase("inonlyvaluechanged")){
            paraCustomerNo = {sqlType:"integer", value:customerNo, direction:0};
            paraInc = {sqlType:"integer", value:"test", direction:0};
            paraCount = {sqlType:"integer", value:count, direction:2};
            paraShipped = {sqlType:"integer", direction:1};
            paraCanceled = {sqlType:"integer", direction:1};
            paraResolved = {sqlType:"integer", direction:1};
            paraDisputed = {sqlType:"integer", direction:1};
        }
        else if (status.equalsIgnoreCase("out")){
            paraCustomerNo = {sqlType:"integer", value:customerNo, direction:0};
            paraInc = {sqlType:"integer", value:inc, direction:0};
            paraCount = {sqlType:"integer", value:count, direction:2};
            paraShipped = {sqlType:"varchar", direction:1};
            paraCanceled = {sqlType:"integer", direction:1};
            paraResolved = {sqlType:"integer", direction:1};
            paraDisputed = {sqlType:"integer", direction:1};
        }
        else if (status.equalsIgnoreCase("inoutvaluenotchanged")){
            paraCustomerNo = {sqlType:"integer", value:customerNo, direction:0};
            paraInc = {sqlType:"integer", value:inc, direction:0};
            paraCount = {sqlType:"varchar", value:count, direction:2};
            paraShipped = {sqlType:"integer", direction:1};
            paraCanceled = {sqlType:"integer", direction:1};
            paraResolved = {sqlType:"integer", direction:1};
            paraDisputed = {sqlType:"integer", direction:1};
        }
        else if (status.equalsIgnoreCase("inoutvaluechanged")){
            paraCustomerNo = {sqlType:"integer", value:customerNo, direction:0};
            paraInc = {sqlType:"integer", value:inc, direction:0};
            paraCount = {sqlType:"varchar", value:"test", direction:2};
            paraShipped = {sqlType:"integer", direction:1};
            paraCanceled = {sqlType:"integer", direction:1};
            paraResolved = {sqlType:"integer", direction:1};
            paraDisputed = {sqlType:"integer", direction:1};
        }
        else if (status.equalsIgnoreCase("inoutonlyvaluechanged")){
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
        dt.close();
    } catch (error e) {
        string msg = "Error in procedure call. Please retry";
        e = {msg:msg};
        err = e;
    }
    return paraShipped.value, paraCanceled.value, paraResolved.value, paraDisputed.value, paraCount.value, err;
}

function callProcedureToGetResultSet(int customerNo)(json, error){

    sql:Parameter[] parameters = [];
    error err;
    error typeErr;
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
    } catch (error e) {
        string msg = "Error in procedure call. Please retry";
        e = {msg:msg};
        err = e;
    }
    return result, err;
}

