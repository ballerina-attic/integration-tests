package org.ballerinalang.tests.connectors.rdbms.operations.insert;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ballerinalang.tests.TestConstants;
import org.ballerinalang.tests.base.BallerinaBaseTest;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static org.testng.Assert.assertEquals;

public class DBInsertionsWithJoinsTests extends BallerinaBaseTest {

    private static final Log log = LogFactory.getLog(DBInsertionsWithJoinsTests.class);
    HttpClient client = new HttpClient();
    Connection conn = null;
    Statement stmt = null;
    Statement stmt2 = null;
    //String dbURL = "localhost:3306/baldb";
    //String ballerinaURL = "http://localhost:9090";

    DBInsertionsWithJoinsTests() {
        client = new HttpClient();
        client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
        client.getHttpConnectionManager().getParams().setSoTimeout(8000);
    }

    @BeforeClass(alwaysRun = true)
    public void initializeTables() {
        String createEmployees = "CREATE TABLE IF NOT EXISTS EmployeesSL (\n" + "    PersonID int NOT NULL,\n"
                + "    LastName varchar(155) NOT NULL,\n" + "    FirstName varchar(155),\n"
                + "    DeptID int,\n" + "    Status varchar(10),\n" + "    PRIMARY KEY (PersonID),\n"
                + "    FOREIGN KEY (DeptID) REFERENCES Departments(DeptID)\n"
                + ");";
        String createDeptManagers = "CREATE TABLE IF NOT EXISTS DeptManagers (\n" + "    DeptID int NOT NULL,\n"
                + "    PersonID int NOT NULL,\n" + "    BandID int,\n"
                + "    PRIMARY KEY (PersonID)\n" + ");";
        String createDepartments = "CREATE TABLE IF NOT EXISTS Departments (\n" + "    DeptID int NOT NULL,\n"
                + "    Name varchar(155) NOT NULL,\n" + "    PRIMARY KEY (DeptID)\n" + ");";
        String createTempTable = "CREATE TABLE IF NOT EXISTS Temp (\n"
                + "    ID int NOT NULL AUTO_INCREMENT PRIMARY KEY,\n"
                + "    LastName varchar(255),\n"
                + "    FirstName varchar(255)\n" + ");";
        String insertToDepartments1 = "insert into Departments (deptid, name) values (1, 'Finance')";
        String insertToDepartments2 = "insert into Departments (deptid, name) values (2, 'Marketing')";
        String insertToEmployees1 = "insert into EmployeesSL (personid, lastname, firstname, deptid, status) " +
                "values (100, 'SlonneA', 'Emma', 1, 'active')";
        String insertToEmployees2 = "insert into EmployeesSL (personid, lastname, firstname, deptid, status) " +
                "values (101, 'SlonneB', 'Henry', 1, 'active');";
        String insertToEmployees3 = "insert into EmployeesSL (personid, lastname, firstname, deptid, status) " +
                "values (102, 'SlonneC', 'Kelly', 2, 'active')";
        String insertToDeptMgr1 = "insert into DeptManagers (deptid, personid, bandid) values (1, 100, 7)";
        String insertToDeptMgr2 = "insert into DeptManagers (deptid, personid, bandid) values (2, 102, 7)";
        String insertToDeptMgr3 = "insert into DeptManagers (deptid, personid, bandid) values (3, 105, 7)";
        String insertToDeptMgr4 = "insert into DeptManagers (deptid, personid, bandid) values (4, 108, 7)";


        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://" + dbURL + "?" + "user=" + TestConstants.MYSQL_USERNAME + "&password="
                            + TestConstants.MYSQL_PASSWORD);

            stmt = conn.createStatement();
            stmt.executeUpdate(createDepartments);
            stmt.executeUpdate(createEmployees);
            stmt.executeUpdate(createDeptManagers);
            stmt.executeUpdate(createTempTable);
            stmt.executeUpdate(insertToDepartments1);
            stmt.executeUpdate(insertToDepartments2);
            stmt.executeUpdate(insertToEmployees1);
            stmt.executeUpdate(insertToEmployees2);
            stmt.executeUpdate(insertToEmployees3);
            stmt.executeUpdate(insertToDeptMgr1);
            stmt.executeUpdate(insertToDeptMgr2);
            stmt.executeUpdate(insertToDeptMgr3);
            stmt.executeUpdate(insertToDeptMgr4);

        } catch (SQLException ex) {
            log.error("SQLException: " + ex.getMessage());
            log.error("SQLState: " + ex.getSQLState());
            log.error("VendorError: " + ex.getErrorCode());
        }
    }

    @Test(description = "This tests insert through select that uses an inner join")
    public void insertWithInnerJoin() throws SQLException {
        String serviceURL = ballerinaURL + "/sql/insert/innerjoin";
        int i = 0;
        int noOfRows = 0;
        ArrayList<String> lastnames = new ArrayList<String>();
        ArrayList<String> firstnames = new ArrayList<String>();
        String payload = "";
        try {
            //Reading response and status code from response
            StringRequestEntity requestEntity = new StringRequestEntity(payload, "application/json", "UTF-8");
            PostMethod post = new PostMethod(serviceURL);
            post.setRequestEntity(requestEntity);
            int statuscode = client.executeMethod(post);
            String response = post.getResponseBodyAsString();

            //Querying the database to obtain values
            String query1 = "select count(*) as total from Temp";
            String query2 = "select * from Temp";
            ResultSet result1 = stmt.executeQuery(query1);
            while (result1.next()) {
                noOfRows = result1.getInt("total");
            }
            ResultSet result2 = stmt.executeQuery(query2);
            while (result2.next()) {
                lastnames.add(i, result2.getString("lastname"));
                firstnames.add(i, result2.getString("firstname"));
                i = i + 1;
            }
            String expectedValue = String.valueOf(noOfRows);
            String endQuery = "delete from Temp";
            stmt.executeUpdate(endQuery);

            // Asserting the Status code. Expected 200 OK
            assertEquals(statuscode, HttpStatus.SC_OK);
            // Asserting the Response Message.
            assertEquals(response, expectedValue);
            //Asserting values from database
            //temp[0]
            assertEquals(lastnames.get(0), "SlonneA");
            assertEquals(firstnames.get(0), "Emma");
            //temp[1]
            assertEquals(lastnames.get(1), "SlonneC");
            assertEquals(firstnames.get(1), "Kelly");
        } catch (IOException e) {
            log.error("Error while calling the BE server : " + e.getMessage(), e);
        }
    }

    @Test(description = "This tests insert through select that uses a left join")
    public void insertWithLeftJoin() throws SQLException {
        String serviceURL = ballerinaURL + "/sql/insert/leftjoin";
        int i = 0;
        int noOfRows = 0;
        ArrayList<String> lastnames = new ArrayList<String>();
        ArrayList<String> firstnames = new ArrayList<String>();
        String payload = "";
        try {
            //Reading response and status code from response
            StringRequestEntity requestEntity = new StringRequestEntity(payload, "application/json", "UTF-8");
            PostMethod post = new PostMethod(serviceURL);
            post.setRequestEntity(requestEntity);
            int statuscode = client.executeMethod(post);
            String response = post.getResponseBodyAsString();

            //Querying the database to obtain values
            String query1 = "select count(*) as total from Temp";
            String query2 = "select * from Temp";
            ResultSet result1 = stmt.executeQuery(query1);
            while (result1.next()) {
                noOfRows = result1.getInt("total");
            }
            ResultSet result2 = stmt.executeQuery(query2);
            while (result2.next()) {
                lastnames.add(i, result2.getString("lastname"));
                firstnames.add(i, result2.getString("firstname"));
                i = i + 1;
            }
            String expectedValue = String.valueOf(noOfRows);
            String endQuery = "delete from Temp";
            stmt.executeUpdate(endQuery);

            // Asserting the Status code. Expected 200 OK
            assertEquals(statuscode, HttpStatus.SC_OK);
            // Asserting the Response Message.
            assertEquals(response, expectedValue);
            //Asserting values from database
            //temp[0]
            assertEquals(lastnames.get(0), "SlonneA");
            assertEquals(firstnames.get(0), "Emma");
            //temp[1]
            assertEquals(lastnames.get(1), "SlonneB");
            assertEquals(firstnames.get(1), "Henry");
            //temp[2]
            assertEquals(lastnames.get(2), "SlonneC");
            assertEquals(firstnames.get(2), "Kelly");
        } catch (IOException e) {
            log.error("Error while calling the BE server : " + e.getMessage(), e);
        }
    }

    @Test(description = "This tests insert through select that uses a right join")
    public void insertWithRightJoin() throws SQLException {
        String serviceURL = ballerinaURL + "/sql/insert/rightjoin";
        int i = 0;
        int noOfRows = 0;
        ArrayList<String> lastnames = new ArrayList<String>();
        ArrayList<String> firstnames = new ArrayList<String>();
        String payload = "";
        try {
            //Reading response and status code from response
            StringRequestEntity requestEntity = new StringRequestEntity(payload, "application/json", "UTF-8");
            PostMethod post = new PostMethod(serviceURL);
            post.setRequestEntity(requestEntity);
            int statuscode = client.executeMethod(post);
            String response = post.getResponseBodyAsString();

            //Querying the database to obtain values
            String query1 = "select count(*) as total from Temp";
            String query2 = "select * from Temp";
            ResultSet result1 = stmt.executeQuery(query1);
            while (result1.next()) {
                noOfRows = result1.getInt("total");
            }
            ResultSet result2 = stmt.executeQuery(query2);
            while (result2.next()) {
                lastnames.add(i, result2.getString("lastname"));
                firstnames.add(i, result2.getString("firstname"));
                i = i + 1;
            }
            String expectedValue = String.valueOf(noOfRows);
            String endQuery = "delete from Temp";
            stmt.executeUpdate(endQuery);

            // Asserting the Status code. Expected 200 OK
            assertEquals(statuscode, HttpStatus.SC_OK);
            // Asserting the Response Message.
            assertEquals(response, expectedValue);
            //Asserting values from database
            //temp[0]
            assertEquals(lastnames.get(0), "SlonneA");
            assertEquals(firstnames.get(0), "Emma");
            //temp[1]
            assertEquals(lastnames.get(1), "SlonneC");
            assertEquals(firstnames.get(1), "Kelly");
            //temp[2]
            assertEquals(lastnames.get(2), null);
            assertEquals(firstnames.get(2), null);
            //temp[3]
            assertEquals(lastnames.get(3), null);
            assertEquals(firstnames.get(3), null);
        } catch (IOException e) {
            log.error("Error while calling the BE server : " + e.getMessage(), e);
        }
    }

    @AfterClass(alwaysRun = true)
    public void afterTest() {
        String dropEmployees = "drop table EmployeesSL";
        String dropDepartments = "drop table Departments";
        String dropDeptManagers = "drop table DeptManagers";
        String dropTemp = "drop table Temp";
        try {
            stmt.executeUpdate(dropEmployees);
            stmt.executeUpdate(dropDeptManagers);
            stmt.executeUpdate(dropDepartments);
            stmt.executeUpdate(dropTemp);
        } catch (SQLException e) {
            log.error("SQLException: " + e.getMessage());
            log.error("SQLState: " + e.getSQLState());
            log.error("VendorError: " + e.getErrorCode());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException sqlEx) {
                }
            }
        }
    }

}
