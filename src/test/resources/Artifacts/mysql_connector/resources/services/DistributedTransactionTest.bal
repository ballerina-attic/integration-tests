package resources.services;

import ballerina.data.sql;
import resources.connectorInit as conn;
import ballerina.lang.system;
import ballerina.lang.strings;

sql:ClientConnector connectorInstanceFirst = conn:initDistributedOne();
sql:ClientConnector connectorInstanceSecond = conn:initDistributedTwo();

function disTransctionSuccess () (string, error){

    sql:Parameter[] parameters = [];
    error err;
    string returnValue;
    string[] keyColumns = [];

    try {
        transaction {
            string[] ids;
            int rowCount_1;
            rowCount_1, ids = connectorInstanceFirst.updateWithGeneratedKeys("Insert into Persons
               (LastName,FirstName,Age,Status) values ('Clerk', 'James', 54, 'active')",
               parameters, keyColumns);
            int rowCount_2 = connectorInstanceSecond.update("Insert into People
               (PersonID,LastName,FirstName,Age,Status) values (1, 'Clerk', 'James', 54, 'active')",
                                       parameters);
        } failed {
            returnValue = "Inside failed block";
        } aborted {
            returnValue = "Inside aborted block";
        } committed {
            returnValue = "Inside committed block";
        }
    } catch (error e) {
            returnValue = "Error in transaction. Please retry";
            err = e;
    }
    return returnValue, err;
}

function disTransctionFailWithDefaultRetry () (string, int, error){

    sql:Parameter[] parameters = [];
    error err;
    int retryCount = 0;
    string returnValue;
    string[] keyColumns = [];

    try {
        transaction {
            string[] ids;
            int rowCount_1;
            rowCount_1, ids = connectorInstanceFirst.updateWithGeneratedKeys("Insert into Persons
               (LastName,FirstName,Age,Status) values ('Clerk', 'James', 54, 'active')",
               parameters, keyColumns);
            int rowCount_2 = connectorInstanceSecond.update("Insert into People
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
    } catch (error e) {
            string temp = "Error in transaction. Please retry";
            err = {msg:temp};
            //err = e;
    }
    return returnValue, retryCount, err;
}

function disTransctionFailWithCustomRetry () (string, int, error){

    sql:Parameter[] parameters = [];
    error err;
    int retryCount = 0;
    string returnValue;
    string[] keyColumns = [];

    try {
        transaction {
            string[] ids;
            int rowCount_1;
            rowCount_1, ids = connectorInstanceFirst.updateWithGeneratedKeys("Insert into Persons
               (LastName,FirstName,Age,Status) values ('Clerk', 'James', 54, 'active')",
               parameters, keyColumns);
            int rowCount_2 = connectorInstanceSecond.update("Insert into People
               (PersonID,LastName,FirstName,Age,Status) values ('Clerk', 'James', 54, 'active')",
                                       parameters);
        } failed {
            retryCount = retryCount + 1;
            returnValue = "Inside failed block";
            retry 4;
        } aborted {
            returnValue = "Inside aborted block";
        } committed {
            returnValue = "Inside committed block";
        }
    } catch (error e) {
            string temp = "Error in transaction. Please retry";
            err = {msg:temp};
            err = e;
    }
    return returnValue, retryCount, err;
}

function disTransctionFailForceAbort () (string, int, error){

    sql:Parameter[] parameters = [];
    error err;
    int retryCount = 0;
    string returnValue;
    string[] keyColumns = [];
    boolean value = true;

    try {
        transaction {
            string[] ids;
            int rowCount_1;
            rowCount_1= connectorInstanceSecond.update("Insert into People
               (PersonID,LastName,FirstName,Age,Status) values (1, 'Clerk', 'James', 54, 'active')",
                                       parameters);
            if (value){
                abort;
            }
            int rowCount_2;
            rowCount_2, ids = connectorInstanceFirst.updateWithGeneratedKeys("Insert into Persons
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
    } catch (error e) {
            string temp = "Error in transaction. Please retry";
            err = {msg:temp};
            err = e;
    }
    return returnValue, retryCount, err;
}

function disTransctionFailForceThrow () (string, int, error){

    sql:Parameter[] parameters = [];
    error err;
    int retryCount = 0;
    string returnValue;
    string[] keyColumns = [];
    boolean value = true;

    try {
        transaction {
            string[] ids;
            int rowCount_1;
            rowCount_1= connectorInstanceSecond.update("Insert into People
               (PersonID,LastName,FirstName,Age,Status) values (1, 'Clerk', 'James', 54, 'active')",
                                       parameters);
            if (value){
                error ex = {msg:"Thrown out from transaction"};
                throw ex;
            }
            int rowCount_2;
            rowCount_2, ids = connectorInstanceFirst.updateWithGeneratedKeys("Insert into Persons
               (LastName,FirstName,Age,Status) values ('Clerk', 'James', 54, 'active')",
               parameters, keyColumns);

        } failed {
            retryCount = retryCount + 1;
            returnValue = "Inside failed block";
            retry 4;
        } aborted {
            returnValue = "Inside aborted block";
        } committed {
            returnValue = "Inside committed block";
        }
    } catch (error e) {
            err = e;
    }
    return returnValue, retryCount, err;
}

function disMultipleTransSuccess () (string, error){

    sql:Parameter[] parameters = [];
    error err;
    string returnValue = "Before trx";
    string[] keyColumns = [];

    try {
        transaction {
            string[] ids;
            int rowCount_1;
            rowCount_1, ids = connectorInstanceFirst.updateWithGeneratedKeys("Insert into Persons
               (LastName,FirstName,Age,Status) values ('Clerk', 'James', 54, 'active')",
               parameters, keyColumns);

        } failed {
            returnValue = returnValue + ": failed trx 1";
        } aborted {
            returnValue = returnValue + ": aborted trx 1";
        } committed {
            returnValue = returnValue + ": committed trx 1";
        }

        transaction {
             int rowCount_2 = connectorInstanceSecond.update("Insert into People
               (PersonID,LastName,FirstName,Age,Status) values (1, 'Clerk', 'James', 54, 'active')",
                                       parameters);

        } failed {
            returnValue = returnValue + ": failed trx 2";
        } aborted {
            returnValue = returnValue + ": aborted trx 2";
        } committed {
            returnValue = returnValue + ": committed trx 2";
        }
    } catch (error e) {
            returnValue = "Error in transaction. Please retry";
            err = e;
    }
    return returnValue, err;
}

function disMultipleTransFailWithRetryOne () (string, int, error){

    sql:Parameter[] parameters = [];
    error err;
    string returnValue = "Before trx";
    string[] keyColumns = [];
    int retryCount = 0;

    try {
        transaction {
            string[] ids;
            int rowCount_1;
            rowCount_1, ids = connectorInstanceFirst.updateWithGeneratedKeys("Insert into Persons
               (LastName,FirstName,Age,Status) values (1, 'Clerk', 'James', 54, 'active')",
               parameters, keyColumns);

        } failed {
            returnValue = returnValue + ": failed trx 1";
            retryCount = retryCount + 1;
            retry 50;
        } aborted {
            returnValue = returnValue + ": aborted trx 1";
        } committed {
            returnValue = returnValue + ": committed trx 1";
        }

        transaction {
             int rowCount_2 = connectorInstanceSecond.update("Insert into People
               (PersonID,LastName,FirstName,Age,Status) values (1, 'Clerk', 'James', 54, 'active')",
                                       parameters);
        } failed {
            returnValue = returnValue + ": failed trx 2";
        } aborted {
            returnValue = returnValue + ": aborted trx 2";
        } committed {
            returnValue = returnValue + ": committed trx 2";
        }
    } catch (error e) {
            string temp = "Error in transaction. Please retry";
            err = {msg:temp};
            //err = e;
    }
    return returnValue, retryCount, err;
}

function disNestedTransFailRetryChild () (string, int, int, error){

    sql:Parameter[] parameters = [];
    error err;
    string returnValue = "Before trx";
    string[] keyColumns = [];
    int childRetryCount = 0;
    int parentRetryCount = 0;

    try {
        transaction {
            string[] ids;
            int rowCount_1;
            rowCount_1, ids = connectorInstanceFirst.updateWithGeneratedKeys("Insert into Persons
               (LastName,FirstName,Age,Status) values ('Clerk', 'James', 54, 'active')",
               parameters, keyColumns);

            transaction {
                 int rowCount_2 = connectorInstanceSecond.update("Insert into People
                   (PersonID,LastName,FirstName,Age,Status) values ('Clerk', 'James', 54, 'active')",
                                               parameters);
            } failed {
                returnValue = returnValue + ": failed child trx";
                childRetryCount = childRetryCount + 1;
                retry 35;
            } aborted {
                returnValue = returnValue + ": aborted child trx";
            } committed {
                returnValue = returnValue + ": committed child trx";
            }

        } failed {
            returnValue = returnValue + ": failed parent trx";
            parentRetryCount = parentRetryCount + 1;
            retry 50;
        } aborted {
            returnValue = returnValue + ": aborted parent trx";
        } committed {
            returnValue = returnValue + ": committed parent trx";
        }
    } catch (error e) {
            string temp = "Error in transaction. Please retry";
            err = {msg:temp};
            //err = e;
    }
    return returnValue, childRetryCount, parentRetryCount, err;
}

function disNestedTransFailRetryParent () (string, int, int, error){

    sql:Parameter[] parameters = [];
    error err;
    string returnValue = "Before trx";
    string[] keyColumns = [];
    int childRetryCount = 0;
    int parentRetryCount = 0;

    try {
        transaction {
            string[] ids;
            int rowCount_1;
            rowCount_1, ids = connectorInstanceFirst.updateWithGeneratedKeys("Insert into Persons
               (LastName,FirstName,Age,Status) values (1, 'Clerk', 'James', 54, 'active')",
               parameters, keyColumns);

            transaction {
                 int rowCount_2 = connectorInstanceSecond.update("Insert into People
                   (PersonID,LastName,FirstName,Age,Status) values (1, 'Clerk', 'James', 54, 'active')",
                                               parameters);
            } failed {
                returnValue = returnValue + ": failed child trx";
                childRetryCount = childRetryCount + 1;
                retry 4;
            } aborted {
                returnValue = returnValue + ": aborted child trx";
            } committed {
                returnValue = returnValue + ": committed child trx";
            }

        } failed {
            returnValue = returnValue + ": failed parent trx";
            parentRetryCount = parentRetryCount + 1;
            retry 4;
        } aborted {
            returnValue = returnValue + ": aborted parent trx";
        } committed {
            returnValue = returnValue + ": committed parent trx";
        }
    } catch (error e) {
            string temp = "Error in transaction. Please retry";
            err = {msg:temp};
            //err = e;
    }
    return returnValue, childRetryCount, parentRetryCount, err;
}

function disTransctionGeneral (json dataset) (string, int, error){

    sql:Parameter[] parametersPersons = [];
    sql:Parameter[] parametersPeople = [];
    error err;
    string returnValue;
    string[] keyColumns = [];
    int length = lengthof dataset.people;
    int i = 0;
    int retryCount = 0;

    try {
        transaction {
            while (i < length) {
                var id, _ = (int)dataset.people[i].id;
                sql:Parameter paraID = {sqlType:"integer", value:id, direction:0};
                var lastname1, _ = (string)dataset.people[i].lastname;
                sql:Parameter paraLPName = {sqlType:"varchar", value:lastname1, direction:0};
                var firstname1, _ = (string)dataset.people[i].firstname;
                sql:Parameter paraFPName = {sqlType:"varchar", value:firstname1, direction:0};
                var age1, _ = (int)dataset.people[i].age;
                sql:Parameter paraAgeP = {sqlType:"varchar", value:age1, direction:0};
                var status1, _ = (string)dataset.people[i].status;
                sql:Parameter paraStatusP = {sqlType:"varchar", value:status1, direction:0};
                parametersPeople = [paraID, paraLPName, paraFPName, paraAgeP, paraStatusP];
                parametersPersons = [paraLPName, paraFPName, paraAgeP, paraStatusP];

                int rowCount_1 = connectorInstanceSecond.update("Insert into People (PersonID,LastName,FirstName,Age,Status) values (?, ?, ?, ?, ?)",parametersPeople);
                if (strings:equalsIgnoreCase(status1, "active")){
                    string[] ids;
                    int rowCount_2;
                    rowCount_2, ids = connectorInstanceFirst.updateWithGeneratedKeys("Insert into Persons (LastName,FirstName,Age,Status) values (?, ?, ?, ?)",parametersPersons, keyColumns);
                }
                else{
                    abort;
                }
                i = i + 1;
            }

        } failed {
            returnValue = "Inside failed block";
            retryCount = retryCount + 1;
            retry 4;
        } aborted {
            returnValue = "Inside aborted block";
        } committed {
            returnValue = "Inside committed block";
        }
    } catch (error e) {
            returnValue = "Error in transaction. Please retry";
            err = e;
    }
    return returnValue, retryCount, err;
}






