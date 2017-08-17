package resources.connectorInit;

import ballerina.data.sql;
import ballerina.lang.system;


function init () (sql:ClientConnector connection){
    string mysqlHostName = system:getEnv("MYSQL_HOSTNAME");
    var mysqlPort, _ = <int>system:getEnv("MYSQL_PORT");
    string mysqlDatabase = system:getEnv("MYSQL_DATABASE");
    string mysqlUserName = system:getEnv("MYSQL_USER");
    string mysqlPassword = system:getEnv("MYSQL_PASSWORD");

    sql:ConnectionProperties properties = {maximumPoolSize:100, connectionTimeout:300000};
    connection = create sql:ClientConnector(
                                 sql:MYSQL, mysqlHostName, mysqlPort, mysqlDatabase, mysqlUserName, mysqlPassword, properties);
    return;
}
