package resources.services;

import ballerina.lang.errors;
import ballerina.data.sql;
import resources.connectorInit as conn;
import ballerina.lang.system;

sql:ClientConnector connectorInstanceFirst = conn:init();
sql:ClientConnector connectorInstanceSecond = conn:initOther();

function disTransctionSuccess () (string, errors:Error){

    sql:Parameter[] parameters = [];
    errors:Error err;
    string returnValue;
    string[] keyColumns = [];

    try {
        transaction {
            connectorInstanceFirst.updateWithGeneratedKeys("Insert into Persons
               (LastName,FirstName,Age,Status) values ('Clerk', 'James', 54, 'active')",
               parameters, keyColumns);
            connectorInstanceSecond.update("Insert into People
               (PersonID,LastName,FirstName,Age,Status) values (1, 'Clerk', 'James', 54, 'active')",
                                       parameters);
        } failed {
            returnValue = "Inside failed block";
        } aborted {
            returnValue = "Inside aborted block";
        } committed {
            returnValue = "Inside committed block";
        }
    } catch (errors:Error e) {
            returnValue = "Error in transaction. Please retry";
            err = e;
    }
    return returnValue, err;
}

function disTransctionFailWithDefaultRetry () (string, int, errors:Error){

    sql:Parameter[] parameters = [];
    errors:Error err;
    int retryCount = 0;
    string returnValue;
    string[] keyColumns = [];

    try {
        transaction {
            connectorInstanceFirst.updateWithGeneratedKeys("Insert into Persons
               (LastName,FirstName,Age,Status) values ('Clerk', 'James', 54, 'active')",
               parameters, keyColumns);
            connectorInstanceSecond.update("Insert into People
               (PersonID,LastName,FirstName,Age,Status) values ('Clerk', 'James', 54, 'active')",
                                       parameters);
        } failed {
            retryCount = retryCount + 1;
            returnValue = "Inside failed block";
        } aborted {
            returnValue = "Inside aborted block";
        } committed {
            returnValue = "Inside committed block";
        }
    } catch (errors:Error e) {
            string temp = "Error in transaction. Please retry";
            err = {msg:temp};
    }
    return returnValue, retryCount, err;
}

function disTransctionFailWithCustomRetry () (string, int, errors:Error){

    sql:Parameter[] parameters = [];
    errors:Error err;
    int retryCount = 0;
    string returnValue;
    string[] keyColumns = [];

    try {
        transaction {
            connectorInstanceFirst.updateWithGeneratedKeys("Insert into Persons
               (LastName,FirstName,Age,Status) values ('Clerk', 'James', 54, 'active')",
               parameters, keyColumns);
            connectorInstanceSecond.update("Insert into People
               (PersonID,LastName,FirstName,Age,Status) values ('Clerk', 'James', 54, 'active')",
                                       parameters);
        } failed {
            retryCount = retryCount + 1;
            returnValue = "Inside failed block";
            retry 100;
        } aborted {
            returnValue = "Inside aborted block";
        } committed {
            returnValue = "Inside committed block";
        }
    } catch (errors:Error e) {
            string temp = "Error in transaction. Please retry";
            err = {msg:temp};
    }
    return returnValue, retryCount, err;
}

function disTransctionFailForceAbort () (string, int, errors:Error){

    sql:Parameter[] parameters = [];
    errors:Error err;
    int retryCount = 0;
    string returnValue;
    string[] keyColumns = [];
    boolean value = true;

    try {
        transaction {
            connectorInstanceSecond.update("Insert into People
               (PersonID,LastName,FirstName,Age,Status) values (1, 'Clerk', 'James', 54, 'active')",
                                       parameters);
            if (value){
                abort;
            }
            connectorInstanceFirst.updateWithGeneratedKeys("Insert into Persons
               (LastName,FirstName,Age,Status) values ('Clerk', 'James', 54, 'active')",
               parameters, keyColumns);

        } failed {
            retryCount = retryCount + 1;
            system:println(retryCount);
            returnValue = "Inside failed block";
        } aborted {
            returnValue = "Inside aborted block";
        } committed {
            returnValue = "Inside committed block";
        }
    } catch (errors:Error e) {
            string temp = "Error in transaction. Please retry";
            err = {msg:temp};
    }
    return returnValue, retryCount, err;
}

