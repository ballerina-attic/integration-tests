package resources.connectorInit;

import ballerina.data.sql;
import ballerina.os;


public function init() (sql:ClientConnector connInit){
    string mysqlHostName = os:getEnv("MYSQL_HOSTNAME");
    var mysqlPort, _ = <int>os:getEnv("MYSQL_PORT");
    string mysqlDatabase = os:getEnv("MYSQL_DATABASE");
    string mysqlUserName = os:getEnv("MYSQL_USER");
    string mysqlPassword = os:getEnv("MYSQL_PASSWORD");

    sql:ConnectionProperties propertiesInit = {maximumPoolSize:5, connectionTimeout:300000};
    connInit = create sql:ClientConnector(
                                 sql:MYSQL, mysqlHostName, mysqlPort, mysqlDatabase, mysqlUserName, mysqlPassword, propertiesInit);
    return;
}

public function initDistributedOne () (sql:ClientConnector connDisOne){
    string mysqlHostName = os:getEnv("MYSQL_HOSTNAME");
    var mysqlPort, _ = <int>os:getEnv("MYSQL_PORT");
    string mysqlDatabase = os:getEnv("MYSQL_DATABASE");
    string mysqlUserName = os:getEnv("MYSQL_USER");
    string mysqlPassword = os:getEnv("MYSQL_PASSWORD");

    sql:ConnectionProperties propertiesDisOne = {isXA:true, maximumPoolSize:5, connectionTimeout:300000};
    connDisOne = create sql:ClientConnector(
                                 sql:MYSQL, mysqlHostName, mysqlPort, mysqlDatabase, mysqlUserName, mysqlPassword, propertiesDisOne);
    return;
}


public function initDistributedTwo () (sql:ClientConnector connDisTwo){
    string mysqlHostNameOther = os:getEnv("MYSQL_OTHER_HOSTNAME");
    var mysqlPortOther, _ = <int>os:getEnv("MYSQL_OTHER_PORT");
    string mysqlDatabaseOther = os:getEnv("MYSQL_OTHER_DATABASE");
    string mysqlUserNameOther = os:getEnv("MYSQL_OTHER_USER");
    string mysqlPasswordOther = os:getEnv("MYSQL_OTHER_PASSWORD");

    sql:ConnectionProperties propertiesDisTwo = {isXA:true, maximumPoolSize:8, connectionTimeout:300000};
    connDisTwo = create sql:ClientConnector(
                                 sql:MYSQL, mysqlHostNameOther, mysqlPortOther, mysqlDatabaseOther, mysqlUserNameOther, mysqlPasswordOther, propertiesDisTwo);
    return;
}
