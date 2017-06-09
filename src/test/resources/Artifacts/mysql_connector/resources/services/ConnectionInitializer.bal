package resources.services;

import ballerina.data.sql;
import ballerina.lang.system;


function init () (sql:ClientConnector connection){
    string jdbcUrl = system:getEnv("JDBC_URL");
    string jdbcUsername = system:getEnv("MYSQL_USERNAME");
    string jdbcPassword = system:getEnv("MYSQL_PASSWORD");

    map dbProperties1 = {"jdbcUrl":jdbcUrl, "username":jdbcUsername, "password":jdbcPassword};
    connection = create sql:ClientConnector (dbProperties1);
    return;
}

function close(sql:ClientConnector connection){
    sql:ClientConnector.close(connection);
}