package resources.connectorInit;

import ballerina.data.sql;
import ballerina.lang.system;


public function init() (sql:ClientConnector connInit){
    string mysqlHostName = system:getEnv("MYSQL_HOSTNAME");
    var mysqlPort, _ = <int>system:getEnv("MYSQL_PORT");
    string mysqlDatabase = system:getEnv("MYSQL_DATABASE");
    string mysqlUserName = system:getEnv("MYSQL_USER");
    string mysqlPassword = system:getEnv("MYSQL_PASSWORD");

    sql:ConnectionProperties propertiesInit = {maximumPoolSize:5, connectionTimeout:300000};
    connInit = create sql:ClientConnector(
                                 sql:MYSQL, mysqlHostName, mysqlPort, mysqlDatabase, mysqlUserName, mysqlPassword, propertiesInit);
    return;
}

public function initDistributedOne () (sql:ClientConnector connDisOne){
    string mysqlHostName = system:getEnv("MYSQL_HOSTNAME");
    var mysqlPort, _ = <int>system:getEnv("MYSQL_PORT");
    string mysqlDatabase = system:getEnv("MYSQL_DATABASE");
    string mysqlUserName = system:getEnv("MYSQL_USER");
    string mysqlPassword = system:getEnv("MYSQL_PASSWORD");

    sql:ConnectionProperties propertiesDisOne = {isXA:true, maximumPoolSize:5, connectionTimeout:300000};
    connDisOne = create sql:ClientConnector(
                                 sql:MYSQL, mysqlHostName, mysqlPort, mysqlDatabase, mysqlUserName, mysqlPassword, propertiesDisOne);
    return;
}


public function initDistributedTwo () (sql:ClientConnector connDisTwo){
    string mysqlHostNameOther = system:getEnv("MYSQL_OTHER_HOSTNAME");
    var mysqlPortOther, _ = <int>system:getEnv("MYSQL_OTHER_PORT");
    string mysqlDatabaseOther = system:getEnv("MYSQL_OTHER_DATABASE");
    string mysqlUserNameOther = system:getEnv("MYSQL_OTHER_USER");
    string mysqlPasswordOther = system:getEnv("MYSQL_OTHER_PASSWORD");

    sql:ConnectionProperties propertiesDisTwo = {isXA:true, maximumPoolSize:8, connectionTimeout:300000};
    connDisTwo = create sql:ClientConnector(
                                 sql:MYSQL, mysqlHostNameOther, mysqlPortOther, mysqlDatabaseOther, mysqlUserNameOther, mysqlPasswordOther, propertiesDisTwo);
    return;
}
