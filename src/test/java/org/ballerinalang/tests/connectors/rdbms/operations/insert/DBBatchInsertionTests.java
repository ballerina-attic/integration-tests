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

/**
 * Tests Database batch insertions
 * Uses IntegratorService.bal service and DBInsertTest.bal
 */

public class DBBatchInsertionTests extends BallerinaBaseTest {

    private static final Log log = LogFactory.getLog(DBBatchInsertionTests.class);
    HttpClient client = new HttpClient();
    Connection conn = null;
    Statement stmt = null;
    Statement stmt2 = null;
    //String dbURL = "localhost:3306/baldb";
    //String ballerinaURL = "http://localhost:9090";

    DBBatchInsertionTests() {
        client = new HttpClient();
        client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
        client.getHttpConnectionManager().getParams().setSoTimeout(8000);
    }

    @BeforeClass(alwaysRun = true)
    public void initializeTables() {
        String createPersons = "CREATE TABLE IF NOT EXISTS People (\n" + "    PersonID int NOT NULL,\n"
                + "    LastName varchar(155) NOT NULL,\n" + "    FirstName varchar(155),\n"
                + "    Age int,\n" + "    Status varchar(10),\n" + "    PRIMARY KEY (PersonID)\n" + ");";
        String createEmployees = "CREATE TABLE IF NOT EXISTS EmployeesSL (\n" + "    PersonID int NOT NULL,\n"
                + "    LastName varchar(155) NOT NULL,\n" + "    FirstName varchar(155),\n"
                + "    DeptID int,\n" + "    Status varchar(10),\n" + "    PRIMARY KEY (PersonID),\n"
                + "     FOREIGN KEY (DeptID) REFERENCES Departments(DeptID)\n" + ");";
        String createDepartments = "CREATE TABLE IF NOT EXISTS Departments (\n" + "    DeptID int NOT NULL,\n"
                + "    Name varchar(155) NOT NULL,\n" + "    PRIMARY KEY (DeptID)\n" + ");";
        String insertToDepartments = "insert into Departments (deptid, name) values (1, 'Finance')";

        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://" + dbURL + "?" + "user=" + TestConstants.MYSQL_USERNAME + "&password="
                            + TestConstants.MYSQL_PASSWORD);

            stmt = conn.createStatement();
            stmt.executeUpdate(createPersons);
            stmt.executeUpdate(createDepartments);
            stmt.executeUpdate(createEmployees);
            stmt.executeUpdate(insertToDepartments);

        } catch (SQLException ex) {
            log.error("SQLException: " + ex.getMessage());
            log.error("SQLState: " + ex.getSQLState());
            log.error("VendorError: " + ex.getErrorCode());
        }
    }

    @Test(description = "Tests successful batch insert")
    public void insertBatchSuccess() throws SQLException {
        log.info("Executing:insertBatchSuccess");
        String serviceURL = ballerinaURL + "/sql/insert/batchupdatesuccess";
        int i = 0;
        int noOfRows = 0;
        ArrayList<String> lastnames = new ArrayList<String>();
        ArrayList<String> firstnames = new ArrayList<String>();
        ArrayList<String> ages = new ArrayList<String>();
        ArrayList<String> ids = new ArrayList<String>();
        ArrayList<String> status = new ArrayList<String>();
        String payload = "{  \n" +
                "   \"persons\":[  \n" +
                "      {  \n" +
                "    \t \"id\":1,\n" +
                "         \"lastname\":\"EmmaD\",\n" +
                "         \"firstname\":\"SloaneD\",\n" +
                "         \"age\":25,\n" +
                "\t\t \"status\":\"active\"\t\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\":2,\n" +
                "    \t\"lastname\":\"HarryA\",\n" +
                "         \"firstname\":\"SloaneB\",\n" +
                "         \"age\":15,\n" +
                "         \"status\":\"active\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\":3,\n" +
                "    \t\"lastname\":\"KellyA\",\n" +
                "         \"firstname\":\"SloaneA\",\n" +
                "         \"age\":25,\n" +
                "         \"status\":\"inactive\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\":4,\n" +
                "    \t\"lastname\":\"DannyC\",\n" +
                "         \"firstname\":\"SloaneC\",\n" +
                "         \"age\":35,\n" +
                "         \"status\":\"inactive\"\n" +
                "      }\t\n" +
                "      \n" +
                "   ]\n" +
                "}";
        try {
            //Reading response and status code from response
            StringRequestEntity requestEntity = new StringRequestEntity(payload, "application/json", "UTF-8");
            PostMethod post = new PostMethod(serviceURL);
            post.setRequestEntity(requestEntity);
            int statuscode = client.executeMethod(post);
            String response = post.getResponseBodyAsString();

            //Querying the database to obtain values
            String query1 = "select count(*) as total from People";
            String query2 = "select * from People";
            ResultSet result1 = stmt.executeQuery(query1);
            while (result1.next()) {
                noOfRows = result1.getInt("total");
            }
            ResultSet result2 = stmt.executeQuery(query2);
            while (result2.next()) {
                lastnames.add(i, String.valueOf(result2.getString("LastName")));
                firstnames.add(i, String.valueOf(result2.getString("FirstName")));
                ages.add(i, String.valueOf(result2.getInt("Age")));
                ids.add(i, String.valueOf(result2.getInt("PersonID")));
                status.add(i, String.valueOf(result2.getString("Status")));
                i = i + 1;
            }
            String expectedValue = String.valueOf(noOfRows);
            String endQuery1 = "delete from People";
            stmt.executeUpdate(endQuery1);

            // Asserting the Status code. Expected 200 OK
            assertEquals(statuscode, HttpStatus.SC_OK);
            // Asserting the Response Message.
            assertEquals(response, expectedValue);
            //Asserting against inserted values to database
            //persons[0]
            assertEquals(lastnames.get(0), "EmmaD");
            assertEquals(firstnames.get(0), "SloaneD");
            assertEquals(ages.get(0), "25");
            assertEquals(ids.get(0), "1");
            assertEquals(status.get(0), "active");
            //persons[1]
            assertEquals(lastnames.get(1), "HarryA");
            assertEquals(firstnames.get(1), "SloaneB");
            assertEquals(ages.get(1), "15");
            assertEquals(ids.get(1), "2");
            assertEquals(status.get(1), "active");
            //persons[2]
            assertEquals(lastnames.get(2), "KellyA");
            assertEquals(firstnames.get(2), "SloaneA");
            assertEquals(ages.get(2), "25");
            assertEquals(ids.get(2), "3");
            assertEquals(status.get(2), "inactive");
            //persons[3]
            assertEquals(lastnames.get(3), "DannyC");
            assertEquals(firstnames.get(3), "SloaneC");
            assertEquals(ages.get(3), "35");
            assertEquals(ids.get(3), "4");
            assertEquals(status.get(3), "inactive");

        } catch (IOException e) {
            log.error("Error while calling the BE server : " + e.getMessage(), e);
        }
    }

    @Test(enabled = false, description = "Tests batch insert with primary key violation in single insertion")
    public void insertBatchPrimaryKeyViolation() throws SQLException {
        log.info("Executing:insertBatchPrimaryKeyViolation");
        String serviceURL = ballerinaURL + "/sql/insert/batchupdatesuccess";
        int i = 0;
        int noOfRows = 0;
        ArrayList<String> lastnames = new ArrayList<String>();
        ArrayList<String> firstnames = new ArrayList<String>();
        ArrayList<String> ages = new ArrayList<String>();
        ArrayList<String> ids = new ArrayList<String>();
        ArrayList<String> status = new ArrayList<String>();
        String payload = "{  \n" +
                "   \"persons\":[  \n" +
                "      {  \n" +
                "    \t \"id\":1,\n" +
                "         \"lastname\":\"EmmaD\",\n" +
                "         \"firstname\":\"SloaneD\",\n" +
                "         \"age\":25,\n" +
                "\t\t \"status\":\"active\"\t\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\":1,\n" +
                "    \t\"lastname\":\"HarryA\",\n" +
                "         \"firstname\":\"SloaneB\",\n" +
                "         \"age\":15,\n" +
                "         \"status\":\"active\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\":3,\n" +
                "    \t\"lastname\":\"KellyA\",\n" +
                "         \"firstname\":\"SloaneA\",\n" +
                "         \"age\":25,\n" +
                "         \"status\":\"inactive\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\":4,\n" +
                "    \t\"lastname\":\"DannyC\",\n" +
                "         \"firstname\":\"SloaneC\",\n" +
                "         \"age\":35,\n" +
                "         \"status\":\"inactive\"\n" +
                "      }\t\n" +
                "      \n" +
                "   ]\n" +
                "}";
        try {
            //Reading response and status code from response
            StringRequestEntity requestEntity = new StringRequestEntity(payload, "application/json", "UTF-8");
            PostMethod post = new PostMethod(serviceURL);
            post.setRequestEntity(requestEntity);
            int statuscode = client.executeMethod(post);
            String response = post.getResponseBodyAsString();

            //Querying the database to obtain values
            String query1 = "select count(*) as total from People";
            String query2 = "select * from People";
            ResultSet result1 = stmt.executeQuery(query1);
            while (result1.next()) {
                noOfRows = result1.getInt("total");
            }
            ResultSet result2 = stmt.executeQuery(query2);
            while (result2.next()) {
                lastnames.add(i, String.valueOf(result2.getString("LastName")));
                firstnames.add(i, String.valueOf(result2.getString("FirstName")));
                ages.add(i, String.valueOf(result2.getInt("Age")));
                ids.add(i, String.valueOf(result2.getInt("PersonID")));
                status.add(i, String.valueOf(result2.getString("Status")));
                i = i + 1;
            }
            String expectedValue = String.valueOf(noOfRows);
            String endQuery1 = "delete from People";
            stmt.executeUpdate(endQuery1);

            // Asserting the Status code. Expected 200 OK
            assertEquals(statuscode, HttpStatus.SC_OK);
            // Asserting the Response Message.
            assertEquals(response, expectedValue);
            //Asserting against inserted values to database
            //persons[0]
            assertEquals(lastnames.get(0), "EmmaD");
            assertEquals(firstnames.get(0), "SloaneD");
            assertEquals(ages.get(0), "25");
            assertEquals(ids.get(0), "1");
            assertEquals(status.get(0), "active");
            //persons[1]
            assertEquals(lastnames.get(1), "KellyA");
            assertEquals(firstnames.get(1), "SloaneA");
            assertEquals(ages.get(1), "25");
            assertEquals(ids.get(1), "3");
            assertEquals(status.get(1), "inactive");
            //persons[2]
            assertEquals(lastnames.get(2), "DannyC");
            assertEquals(firstnames.get(2), "SloaneC");
            assertEquals(ages.get(2), "35");
            assertEquals(ids.get(2), "4");
            assertEquals(status.get(2), "inactive");

        } catch (IOException e) {
            log.error("Error while calling the BE server : " + e.getMessage(), e);
        }
    }

    @Test(description = "Tests batch insert with int data in place of string")
    public void insertBatchDataMismatchString() throws SQLException {
        log.info("Executing:insertBatchDataMismatchString");
        String serviceURL = ballerinaURL + "/sql/insert/batchupdatesuccess";
        int i = 0;
        int noOfRows = 0;
        ArrayList<String> lastnames = new ArrayList<String>();
        ArrayList<String> firstnames = new ArrayList<String>();
        ArrayList<String> ages = new ArrayList<String>();
        ArrayList<String> ids = new ArrayList<String>();
        ArrayList<String> status = new ArrayList<String>();
        String payload = "{  \n" +
                "   \"persons\":[  \n" +
                "      {  \n" +
                "    \t \"id\":1,\n" +
                "         \"lastname\":100,\n" +
                "         \"firstname\":\"SloaneD\",\n" +
                "         \"age\":25,\n" +
                "\t\t \"status\":\"active\"\t\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\":2,\n" +
                "    \t\"lastname\":\"HarryA\",\n" +
                "         \"firstname\":\"SloaneB\",\n" +
                "         \"age\":15,\n" +
                "         \"status\":\"active\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\":3,\n" +
                "    \t\"lastname\":\"KellyA\",\n" +
                "         \"firstname\":\"SloaneA\",\n" +
                "         \"age\":25,\n" +
                "         \"status\":\"inactive\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\":4,\n" +
                "    \t\"lastname\":\"DannyC\",\n" +
                "         \"firstname\":\"SloaneC\",\n" +
                "         \"age\":35,\n" +
                "         \"status\":\"inactive\"\n" +
                "      }\t\n" +
                "      \n" +
                "   ]\n" +
                "}";
        try {
            //Reading response and status code from response
            StringRequestEntity requestEntity = new StringRequestEntity(payload, "application/json", "UTF-8");
            PostMethod post = new PostMethod(serviceURL);
            post.setRequestEntity(requestEntity);
            int statuscode = client.executeMethod(post);
            String response = post.getResponseBodyAsString();

            //Querying the database to obtain values
            String query1 = "select count(*) as total from People";
            String query2 = "select * from People";
            ResultSet result1 = stmt.executeQuery(query1);
            while (result1.next()) {
                noOfRows = result1.getInt("total");
            }
            ResultSet result2 = stmt.executeQuery(query2);
            while (result2.next()) {
                lastnames.add(i, String.valueOf(result2.getString("LastName")));
                firstnames.add(i, String.valueOf(result2.getString("FirstName")));
                ages.add(i, String.valueOf(result2.getInt("Age")));
                ids.add(i, String.valueOf(result2.getInt("PersonID")));
                status.add(i, String.valueOf(result2.getString("Status")));
                i = i + 1;
            }
            String expectedValue = String.valueOf(noOfRows);
            String endQuery1 = "delete from People";
            stmt.executeUpdate(endQuery1);

            // Asserting the Status code. Expected 200 OK
            assertEquals(statuscode, HttpStatus.SC_OK);
            // Asserting the Response Message.
            assertEquals(response, expectedValue);
            //Asserting against inserted values to database
            //persons[0]
            assertEquals(lastnames.get(0), "");
            assertEquals(firstnames.get(0), "SloaneD");
            assertEquals(ages.get(0), "25");
            assertEquals(ids.get(0), "1");
            assertEquals(status.get(0), "active");
            //persons[1]
            assertEquals(lastnames.get(1), "HarryA");
            assertEquals(firstnames.get(1), "SloaneB");
            assertEquals(ages.get(1), "15");
            assertEquals(ids.get(1), "2");
            assertEquals(status.get(1), "active");
            //persons[2]
            assertEquals(lastnames.get(2), "KellyA");
            assertEquals(firstnames.get(2), "SloaneA");
            assertEquals(ages.get(2), "25");
            assertEquals(ids.get(2), "3");
            assertEquals(status.get(2), "inactive");
            //persons[3]
            assertEquals(lastnames.get(3), "DannyC");
            assertEquals(firstnames.get(3), "SloaneC");
            assertEquals(ages.get(3), "35");
            assertEquals(ids.get(3), "4");
            assertEquals(status.get(3), "inactive");

        } catch (IOException e) {
            log.error("Error while calling the BE server : " + e.getMessage(), e);
        }
    }

    @Test(description = "Tests batch insert with string data in place of int")
    public void insertBatchDataMismatchInt() throws SQLException {
        log.info("Executing:insertBatchDataMismatchInt");
        String serviceURL = ballerinaURL + "/sql/insert/batchupdatesuccess";
        int i = 0;
        int noOfRows = 0;
        ArrayList<String> lastnames = new ArrayList<String>();
        ArrayList<String> firstnames = new ArrayList<String>();
        ArrayList<String> ages = new ArrayList<String>();
        ArrayList<String> ids = new ArrayList<String>();
        ArrayList<String> status = new ArrayList<String>();
        String payload = "{  \n" +
                "   \"persons\":[  \n" +
                "      {  \n" +
                "    \t \"id\":1,\n" +
                "         \"lastname\":\"EmmaD\",\n" +
                "         \"firstname\":\"SloaneD\",\n" +
                "         \"age\":\"a\",\n" +
                "\t\t \"status\":\"active\"\t\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\":2,\n" +
                "    \t\"lastname\":\"HarryA\",\n" +
                "         \"firstname\":\"SloaneB\",\n" +
                "         \"age\":15,\n" +
                "         \"status\":\"active\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\":3,\n" +
                "    \t\"lastname\":\"KellyA\",\n" +
                "         \"firstname\":\"SloaneA\",\n" +
                "         \"age\":25,\n" +
                "         \"status\":\"inactive\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\":4,\n" +
                "    \t\"lastname\":\"DannyC\",\n" +
                "         \"firstname\":\"SloaneC\",\n" +
                "         \"age\":35,\n" +
                "         \"status\":\"inactive\"\n" +
                "      }\t\n" +
                "      \n" +
                "   ]\n" +
                "}";
        try {
            //Reading response and status code from response
            StringRequestEntity requestEntity = new StringRequestEntity(payload, "application/json", "UTF-8");
            PostMethod post = new PostMethod(serviceURL);
            post.setRequestEntity(requestEntity);
            int statuscode = client.executeMethod(post);
            String response = post.getResponseBodyAsString();

            //Querying the database to obtain values
            String query1 = "select count(*) as total from People";
            String query2 = "select * from People";
            ResultSet result1 = stmt.executeQuery(query1);
            while (result1.next()) {
                noOfRows = result1.getInt("total");
            }
            ResultSet result2 = stmt.executeQuery(query2);
            while (result2.next()) {
                lastnames.add(i, String.valueOf(result2.getString("LastName")));
                firstnames.add(i, String.valueOf(result2.getString("FirstName")));
                ages.add(i, String.valueOf(result2.getInt("Age")));
                ids.add(i, String.valueOf(result2.getInt("PersonID")));
                status.add(i, String.valueOf(result2.getString("Status")));
                i = i + 1;
            }
            String expectedValue = String.valueOf(noOfRows);
            String endQuery1 = "delete from People";
            stmt.executeUpdate(endQuery1);

            // Asserting the Status code. Expected 200 OK
            assertEquals(statuscode, HttpStatus.SC_OK);
            // Asserting the Response Message.
            assertEquals(response, expectedValue);
            //Asserting against inserted values to database
            //persons[0]
            assertEquals(lastnames.get(0), "EmmaD");
            assertEquals(firstnames.get(0), "SloaneD");
            assertEquals(ages.get(0), "0");
            assertEquals(ids.get(0), "1");
            assertEquals(status.get(0), "active");
            //persons[1]
            assertEquals(lastnames.get(1), "HarryA");
            assertEquals(firstnames.get(1), "SloaneB");
            assertEquals(ages.get(1), "15");
            assertEquals(ids.get(1), "2");
            assertEquals(status.get(1), "active");
            //persons[2]
            assertEquals(lastnames.get(2), "KellyA");
            assertEquals(firstnames.get(2), "SloaneA");
            assertEquals(ages.get(2), "25");
            assertEquals(ids.get(2), "3");
            assertEquals(status.get(2), "inactive");
            //persons[3]
            assertEquals(lastnames.get(3), "DannyC");
            assertEquals(firstnames.get(3), "SloaneC");
            assertEquals(ages.get(3), "35");
            assertEquals(ids.get(3), "4");
            assertEquals(status.get(3), "inactive");

        } catch (IOException e) {
            log.error("Error while calling the BE server : " + e.getMessage(), e);
        }
    }

    @Test(description = "Tests batch insert with empty string for varchar field")
    public void insertBatchEmptyString() throws SQLException {
        log.info("Executing:insertBatchEmptyString");
        String serviceURL = ballerinaURL + "/sql/insert/batchupdatesuccess";
        int i = 0;
        int noOfRows = 0;
        ArrayList<String> lastnames = new ArrayList<String>();
        ArrayList<String> firstnames = new ArrayList<String>();
        ArrayList<String> ages = new ArrayList<String>();
        ArrayList<String> ids = new ArrayList<String>();
        ArrayList<String> status = new ArrayList<String>();
        String payload = "{  \n" +
                "   \"persons\":[  \n" +
                "      {  \n" +
                "    \t \"id\":1,\n" +
                "         \"lastname\":\"\",\n" +
                "         \"firstname\":\"SloaneD\",\n" +
                "         \"age\":25,\n" +
                "\t\t \"status\":\"active\"\t\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\":2,\n" +
                "    \t\"lastname\":\"HarryA\",\n" +
                "         \"firstname\":\"SloaneB\",\n" +
                "         \"age\":15,\n" +
                "         \"status\":\"active\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\":3,\n" +
                "    \t\"lastname\":\"KellyA\",\n" +
                "         \"firstname\":\"SloaneA\",\n" +
                "         \"age\":25,\n" +
                "         \"status\":\"inactive\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\":4,\n" +
                "    \t\"lastname\":\"DannyC\",\n" +
                "         \"firstname\":\"SloaneC\",\n" +
                "         \"age\":35,\n" +
                "         \"status\":\"inactive\"\n" +
                "      }\t\n" +
                "      \n" +
                "   ]\n" +
                "}";
        try {
            //Reading response and status code from response
            StringRequestEntity requestEntity = new StringRequestEntity(payload, "application/json", "UTF-8");
            PostMethod post = new PostMethod(serviceURL);
            post.setRequestEntity(requestEntity);
            int statuscode = client.executeMethod(post);
            String response = post.getResponseBodyAsString();

            //Querying the database to obtain values
            String query1 = "select count(*) as total from People";
            String query2 = "select * from People";
            ResultSet result1 = stmt.executeQuery(query1);
            while (result1.next()) {
                noOfRows = result1.getInt("total");
            }
            ResultSet result2 = stmt.executeQuery(query2);
            while (result2.next()) {
                lastnames.add(i, String.valueOf(result2.getString("LastName")));
                firstnames.add(i, String.valueOf(result2.getString("FirstName")));
                ages.add(i, String.valueOf(result2.getInt("Age")));
                ids.add(i, String.valueOf(result2.getInt("PersonID")));
                status.add(i, String.valueOf(result2.getString("Status")));
                i = i + 1;
            }
            String expectedValue = String.valueOf(noOfRows);
            String endQuery1 = "delete from People";
            stmt.executeUpdate(endQuery1);

            // Asserting the Status code. Expected 200 OK
            assertEquals(statuscode, HttpStatus.SC_OK);
            // Asserting the Response Message.
            assertEquals(response, expectedValue);
            //Asserting against inserted values to database
            //persons[0]
            assertEquals(lastnames.get(0), "");
            assertEquals(firstnames.get(0), "SloaneD");
            assertEquals(ages.get(0), "25");
            assertEquals(ids.get(0), "1");
            assertEquals(status.get(0), "active");
            //persons[1]
            assertEquals(lastnames.get(1), "HarryA");
            assertEquals(firstnames.get(1), "SloaneB");
            assertEquals(ages.get(1), "15");
            assertEquals(ids.get(1), "2");
            assertEquals(status.get(1), "active");
            //persons[2]
            assertEquals(lastnames.get(2), "KellyA");
            assertEquals(firstnames.get(2), "SloaneA");
            assertEquals(ages.get(2), "25");
            assertEquals(ids.get(2), "3");
            assertEquals(status.get(2), "inactive");
            //persons[3]
            assertEquals(lastnames.get(3), "DannyC");
            assertEquals(firstnames.get(3), "SloaneC");
            assertEquals(ages.get(3), "35");
            assertEquals(ids.get(3), "4");
            assertEquals(status.get(3), "inactive");

        } catch (IOException e) {
            log.error("Error while calling the BE server : " + e.getMessage(), e);
        }
    }

    @Test(description = "Tests batch insert with empty string for integer field")
    public void insertBatchEmptyStringForInt() throws SQLException {
        log.info("Executing:insertBatchEmptyStringForInt");
        String serviceURL = ballerinaURL + "/sql/insert/batchupdatesuccess";
        int i = 0;
        int noOfRows = 0;
        ArrayList<String> lastnames = new ArrayList<String>();
        ArrayList<String> firstnames = new ArrayList<String>();
        ArrayList<String> ages = new ArrayList<String>();
        ArrayList<String> ids = new ArrayList<String>();
        ArrayList<String> status = new ArrayList<String>();
        String payload = "{  \n" +
                "   \"persons\":[  \n" +
                "      {  \n" +
                "    \t \"id\":1,\n" +
                "         \"lastname\":\"EmmaD\",\n" +
                "         \"firstname\":\"SloaneD\",\n" +
                "         \"age\":\"\",\n" +
                "\t\t \"status\":\"active\"\t\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\":2,\n" +
                "    \t\"lastname\":\"HarryA\",\n" +
                "         \"firstname\":\"SloaneB\",\n" +
                "         \"age\":15,\n" +
                "         \"status\":\"active\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\":3,\n" +
                "    \t\"lastname\":\"KellyA\",\n" +
                "         \"firstname\":\"SloaneA\",\n" +
                "         \"age\":25,\n" +
                "         \"status\":\"inactive\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\":4,\n" +
                "    \t\"lastname\":\"DannyC\",\n" +
                "         \"firstname\":\"SloaneC\",\n" +
                "         \"age\":35,\n" +
                "         \"status\":\"inactive\"\n" +
                "      }\t\n" +
                "      \n" +
                "   ]\n" +
                "}";
        try {
            //Reading response and status code from response
            StringRequestEntity requestEntity = new StringRequestEntity(payload, "application/json", "UTF-8");
            PostMethod post = new PostMethod(serviceURL);
            post.setRequestEntity(requestEntity);
            int statuscode = client.executeMethod(post);
            String response = post.getResponseBodyAsString();

            //Querying the database to obtain values
            String query1 = "select count(*) as total from People";
            String query2 = "select * from People";
            ResultSet result1 = stmt.executeQuery(query1);
            while (result1.next()) {
                noOfRows = result1.getInt("total");
            }
            ResultSet result2 = stmt.executeQuery(query2);
            while (result2.next()) {
                lastnames.add(i, String.valueOf(result2.getString("LastName")));
                firstnames.add(i, String.valueOf(result2.getString("FirstName")));
                ages.add(i, String.valueOf(result2.getInt("Age")));
                ids.add(i, String.valueOf(result2.getInt("PersonID")));
                status.add(i, String.valueOf(result2.getString("Status")));
                i = i + 1;
            }
            String expectedValue = String.valueOf(noOfRows);
            String endQuery1 = "delete from People";
            stmt.executeUpdate(endQuery1);

            // Asserting the Status code. Expected 200 OK
            assertEquals(statuscode, HttpStatus.SC_OK);
            // Asserting the Response Message.
            assertEquals(response, expectedValue);
            //Asserting against inserted values to database
            //persons[0]
            assertEquals(lastnames.get(0), "EmmaD");
            assertEquals(firstnames.get(0), "SloaneD");
            assertEquals(ages.get(0), "0");
            assertEquals(ids.get(0), "1");
            assertEquals(status.get(0), "active");
            //persons[1]
            assertEquals(lastnames.get(1), "HarryA");
            assertEquals(firstnames.get(1), "SloaneB");
            assertEquals(ages.get(1), "15");
            assertEquals(ids.get(1), "2");
            assertEquals(status.get(1), "active");
            //persons[2]
            assertEquals(lastnames.get(2), "KellyA");
            assertEquals(firstnames.get(2), "SloaneA");
            assertEquals(ages.get(2), "25");
            assertEquals(ids.get(2), "3");
            assertEquals(status.get(2), "inactive");
            //persons[3]
            assertEquals(lastnames.get(3), "DannyC");
            assertEquals(firstnames.get(3), "SloaneC");
            assertEquals(ages.get(3), "35");
            assertEquals(ids.get(3), "4");
            assertEquals(status.get(3), "inactive");

        } catch (IOException e) {
            log.error("Error while calling the BE server : " + e.getMessage(), e);
        }
    }

    @Test(enabled = false, description = "Tests batch insert with null value for nullable column")
    public void insertBatchNullForNullCol() throws SQLException {
        log.info("Executing:insertBatchNullForNullCol");
        String serviceURL = ballerinaURL + "/sql/insert/batchupdatesuccess";
        int i = 0;
        int noOfRows = 0;
        ArrayList<String> lastnames = new ArrayList<String>();
        ArrayList<String> firstnames = new ArrayList<String>();
        ArrayList<String> ages = new ArrayList<String>();
        ArrayList<String> ids = new ArrayList<String>();
        ArrayList<String> status = new ArrayList<String>();
        String payload = "{  \n" +
                "   \"persons\":[  \n" +
                "      {  \n" +
                "    \t \"id\":1,\n" +
                "         \"lastname\":\"EmmaD\",\n" +
                "         \"firstname\":null,\n" +
                "         \"age\":25,\n" +
                "\t\t \"status\":\"active\"\t\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\":2,\n" +
                "    \t\"lastname\":\"HarryA\",\n" +
                "         \"firstname\":\"SloaneB\",\n" +
                "         \"age\":15,\n" +
                "         \"status\":\"active\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\":3,\n" +
                "    \t\"lastname\":\"KellyA\",\n" +
                "         \"firstname\":\"SloaneA\",\n" +
                "         \"age\":25,\n" +
                "         \"status\":\"inactive\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\":4,\n" +
                "    \t\"lastname\":\"DannyC\",\n" +
                "         \"firstname\":\"SloaneC\",\n" +
                "         \"age\":35,\n" +
                "         \"status\":\"inactive\"\n" +
                "      }\t\n" +
                "      \n" +
                "   ]\n" +
                "}";
        try {
            //Reading response and status code from response
            StringRequestEntity requestEntity = new StringRequestEntity(payload, "application/json", "UTF-8");
            PostMethod post = new PostMethod(serviceURL);
            post.setRequestEntity(requestEntity);
            int statuscode = client.executeMethod(post);
            String response = post.getResponseBodyAsString();

            //Querying the database to obtain values
            String query1 = "select count(*) as total from People";
            String query2 = "select * from People";
            ResultSet result1 = stmt.executeQuery(query1);
            while (result1.next()) {
                noOfRows = result1.getInt("total");
            }
            ResultSet result2 = stmt.executeQuery(query2);
            while (result2.next()) {
                lastnames.add(i, String.valueOf(result2.getString("LastName")));
                firstnames.add(i, String.valueOf(result2.getString("FirstName")));
                ages.add(i, String.valueOf(result2.getInt("Age")));
                ids.add(i, String.valueOf(result2.getInt("PersonID")));
                status.add(i, String.valueOf(result2.getString("Status")));
                i = i + 1;
            }
            String expectedValue = String.valueOf(noOfRows);
            String endQuery1 = "delete from People";
            stmt.executeUpdate(endQuery1);

            // Asserting the Status code. Expected 200 OK
            assertEquals(statuscode, HttpStatus.SC_OK);
            // Asserting the Response Message.
            assertEquals(response, expectedValue);
            //Asserting against inserted values to database
            //persons[0]
            assertEquals(lastnames.get(0), "EmmaD");
            assertEquals(firstnames.get(0), null);
            assertEquals(ages.get(0), "25");
            assertEquals(ids.get(0), "1");
            assertEquals(status.get(0), "active");
            //persons[1]
            assertEquals(lastnames.get(1), "HarryA");
            assertEquals(firstnames.get(1), "SloaneB");
            assertEquals(ages.get(1), "15");
            assertEquals(ids.get(1), "2");
            assertEquals(status.get(1), "active");
            //persons[2]
            assertEquals(lastnames.get(2), "KellyA");
            assertEquals(firstnames.get(2), "SloaneA");
            assertEquals(ages.get(2), "25");
            assertEquals(ids.get(2), "3");
            assertEquals(status.get(2), "inactive");
            //persons[3]
            assertEquals(lastnames.get(3), "DannyC");
            assertEquals(firstnames.get(3), "SloaneC");
            assertEquals(ages.get(3), "35");
            assertEquals(ids.get(3), "4");
            assertEquals(status.get(3), "inactive");

        } catch (IOException e) {
            log.error("Error while calling the BE server : " + e.getMessage(), e);
        }
    }

    @Test(enabled = false, description = "Tests batch insert with null value integer column")
    public void insertBatchNullForIntCol() throws SQLException {
        log.info("Executing:insertBatchNullForIntCol");
        String serviceURL = ballerinaURL + "/sql/insert/batchupdatesuccess";
        int i = 0;
        int noOfRows = 0;
        ArrayList<String> lastnames = new ArrayList<String>();
        ArrayList<String> firstnames = new ArrayList<String>();
        ArrayList<String> ages = new ArrayList<String>();
        ArrayList<String> ids = new ArrayList<String>();
        ArrayList<String> status = new ArrayList<String>();
        String payload = "{  \n" +
                "   \"persons\":[  \n" +
                "      {  \n" +
                "    \t \"id\":1,\n" +
                "         \"lastname\":\"EmmaD\",\n" +
                "         \"firstname\":\"SloaneD\",\n" +
                "         \"age\":null,\n" +
                "\t\t \"status\":\"active\"\t\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\":2,\n" +
                "    \t\"lastname\":\"HarryA\",\n" +
                "         \"firstname\":\"SloaneB\",\n" +
                "         \"age\":15,\n" +
                "         \"status\":\"active\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\":3,\n" +
                "    \t\"lastname\":\"KellyA\",\n" +
                "         \"firstname\":\"SloaneA\",\n" +
                "         \"age\":25,\n" +
                "         \"status\":\"inactive\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\":4,\n" +
                "    \t\"lastname\":\"DannyC\",\n" +
                "         \"firstname\":\"SloaneC\",\n" +
                "         \"age\":35,\n" +
                "         \"status\":\"inactive\"\n" +
                "      }\t\n" +
                "      \n" +
                "   ]\n" +
                "}";
        try {
            //Reading response and status code from response
            StringRequestEntity requestEntity = new StringRequestEntity(payload, "application/json", "UTF-8");
            PostMethod post = new PostMethod(serviceURL);
            post.setRequestEntity(requestEntity);
            int statuscode = client.executeMethod(post);
            String response = post.getResponseBodyAsString();

            //Querying the database to obtain values
            String query1 = "select count(*) as total from People";
            String query2 = "select * from People";
            ResultSet result1 = stmt.executeQuery(query1);
            while (result1.next()) {
                noOfRows = result1.getInt("total");
            }
            ResultSet result2 = stmt.executeQuery(query2);
            while (result2.next()) {
                lastnames.add(i, String.valueOf(result2.getString("LastName")));
                firstnames.add(i, String.valueOf(result2.getString("FirstName")));
                ages.add(i, String.valueOf(result2.getInt("Age")));
                ids.add(i, String.valueOf(result2.getInt("PersonID")));
                status.add(i, String.valueOf(result2.getString("Status")));
                i = i + 1;
            }
            String expectedValue = String.valueOf(noOfRows);
            String endQuery1 = "delete from People";
            stmt.executeUpdate(endQuery1);

            // Asserting the Status code. Expected 200 OK
            assertEquals(statuscode, HttpStatus.SC_OK);
            // Asserting the Response Message.
            assertEquals(response, expectedValue);
            //Asserting against inserted values to database
            //persons[0]
            assertEquals(lastnames.get(0), "EmmaD");
            assertEquals(firstnames.get(0), "SloaneD");
            assertEquals(ages.get(0), "0");
            assertEquals(ids.get(0), "1");
            assertEquals(status.get(0), "active");
            //persons[1]
            assertEquals(lastnames.get(1), "HarryA");
            assertEquals(firstnames.get(1), "SloaneB");
            assertEquals(ages.get(1), "15");
            assertEquals(ids.get(1), "2");
            assertEquals(status.get(1), "active");
            //persons[2]
            assertEquals(lastnames.get(2), "KellyA");
            assertEquals(firstnames.get(2), "SloaneA");
            assertEquals(ages.get(2), "25");
            assertEquals(ids.get(2), "3");
            assertEquals(status.get(2), "inactive");
            //persons[3]
            assertEquals(lastnames.get(3), "DannyC");
            assertEquals(firstnames.get(3), "SloaneC");
            assertEquals(ages.get(3), "35");
            assertEquals(ids.get(3), "4");
            assertEquals(status.get(3), "inactive");

        } catch (IOException e) {
            log.error("Error while calling the BE server : " + e.getMessage(), e);
        }
    }

    @Test(enabled = false, description = "Tests batch insert with null value for non nullable column")
    public void insertBatchNullForNotNullCol() throws SQLException {
        log.info("Executing:insertBatchNullForNotNullCol");
        String serviceURL = ballerinaURL + "/sql/insert/batchupdatesuccess";
        int i = 0;
        int noOfRows = 0;
        ArrayList<String> lastnames = new ArrayList<String>();
        ArrayList<String> firstnames = new ArrayList<String>();
        ArrayList<String> ages = new ArrayList<String>();
        ArrayList<String> ids = new ArrayList<String>();
        ArrayList<String> status = new ArrayList<String>();
        String payload = "{  \n" +
                "   \"persons\":[  \n" +
                "      {  \n" +
                "    \t \"id\":1,\n" +
                "         \"lastname\":null,\n" +
                "         \"firstname\":\"SloaneD\",\n" +
                "         \"age\":25,\n" +
                "\t\t \"status\":\"active\"\t\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\":2,\n" +
                "    \t\"lastname\":\"HarryA\",\n" +
                "         \"firstname\":\"SloaneB\",\n" +
                "         \"age\":15,\n" +
                "         \"status\":\"active\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\":3,\n" +
                "    \t\"lastname\":\"KellyA\",\n" +
                "         \"firstname\":\"SloaneA\",\n" +
                "         \"age\":25,\n" +
                "         \"status\":\"inactive\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\":4,\n" +
                "    \t\"lastname\":\"DannyC\",\n" +
                "         \"firstname\":\"SloaneC\",\n" +
                "         \"age\":35,\n" +
                "         \"status\":\"inactive\"\n" +
                "      }\t\n" +
                "      \n" +
                "   ]\n" +
                "}";
        try {
            //Reading response and status code from response
            StringRequestEntity requestEntity = new StringRequestEntity(payload, "application/json", "UTF-8");
            PostMethod post = new PostMethod(serviceURL);
            post.setRequestEntity(requestEntity);
            int statuscode = client.executeMethod(post);
            String response = post.getResponseBodyAsString();

            //Querying the database to obtain values
            String query1 = "select count(*) as total from People";
            String query2 = "select * from People";
            ResultSet result1 = stmt.executeQuery(query1);
            while (result1.next()) {
                noOfRows = result1.getInt("total");
            }
            ResultSet result2 = stmt.executeQuery(query2);
            while (result2.next()) {
                lastnames.add(i, String.valueOf(result2.getString("LastName")));
                firstnames.add(i, String.valueOf(result2.getString("FirstName")));
                ages.add(i, String.valueOf(result2.getInt("Age")));
                ids.add(i, String.valueOf(result2.getInt("PersonID")));
                status.add(i, String.valueOf(result2.getString("Status")));
                i = i + 1;
            }
            String expectedValue = String.valueOf(noOfRows);
            String endQuery1 = "delete from People";
            stmt.executeUpdate(endQuery1);

            // Asserting the Status code. Expected 200 OK
            assertEquals(statuscode, HttpStatus.SC_OK);
            // Asserting the Response Message.
            assertEquals(response, expectedValue);
            //Asserting against inserted values to database
            //persons[0]
            assertEquals(lastnames.get(0), "HarryA");
            assertEquals(firstnames.get(0), "SloaneB");
            assertEquals(ages.get(0), "15");
            assertEquals(ids.get(0), "2");
            assertEquals(status.get(0), "active");
            //persons[1]
            assertEquals(lastnames.get(1), "KellyA");
            assertEquals(firstnames.get(1), "SloaneA");
            assertEquals(ages.get(1), "25");
            assertEquals(ids.get(1), "3");
            assertEquals(status.get(1), "inactive");
            //persons[2]
            assertEquals(lastnames.get(2), "DannyC");
            assertEquals(firstnames.get(2), "SloaneC");
            assertEquals(ages.get(2), "35");
            assertEquals(ids.get(2), "4");
            assertEquals(status.get(2), "inactive");

        } catch (IOException e) {
            log.error("Error while calling the BE server : " + e.getMessage(), e);
        }
    }

    @Test(enabled = false, description = "Tests batch insert with foreign key violation in one record")
    public void insertBatchForeignKeyViolation() throws SQLException {
        log.info("Executing:insertBatchForeignKeyViolation");
        String serviceURL = ballerinaURL + "/sql/insert/batchupdateforeign";
        int i = 0;
        int noOfRows = 0;
        ArrayList<String> lastnames = new ArrayList<String>();
        ArrayList<String> firstnames = new ArrayList<String>();
        ArrayList<String> departmentids = new ArrayList<String>();
        ArrayList<String> ids = new ArrayList<String>();
        ArrayList<String> status = new ArrayList<String>();
        String payload = "{  \n" +
                "   \"persons\":[  \n" +
                "      {  \n" +
                "    \t \"id\":1,\n" +
                "         \"lastname\":\"EmmaD\",\n" +
                "         \"firstname\":\"SloaneD\",\n" +
                "         \"deptid\":1,\n" +
                "\t \"status\":\"active\"\t\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\":2,\n" +
                "    \t\"lastname\":\"HarryA\",\n" +
                "         \"firstname\":\"SloaneB\",\n" +
                "         \"deptid\":2,\n" +
                "         \"status\":\"active\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\":3,\n" +
                "    \t\"lastname\":\"KellyA\",\n" +
                "         \"firstname\":\"SloaneA\",\n" +
                "         \"deptid\":1,\n" +
                "         \"status\":\"inactive\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\":4,\n" +
                "    \t\"lastname\":\"DannyC\",\n" +
                "         \"firstname\":\"SloaneC\",\n" +
                "         \"deptid\":1,\n" +
                "         \"status\":\"inactive\"\n" +
                "      }\t\n" +
                "      \n" +
                "   ]\n" +
                "}";
        try {
            //Reading response and status code from response
            StringRequestEntity requestEntity = new StringRequestEntity(payload, "application/json", "UTF-8");
            PostMethod post = new PostMethod(serviceURL);
            post.setRequestEntity(requestEntity);
            int statuscode = client.executeMethod(post);
            String response = post.getResponseBodyAsString();

            //Querying the database to obtain values
            String query1 = "select count(*) as total from EmployeesSL";
            String query2 = "select * from EmployeesSL";
            ResultSet result1 = stmt.executeQuery(query1);
            while (result1.next()) {
                noOfRows = result1.getInt("total");
            }
            ResultSet result2 = stmt.executeQuery(query2);
            while (result2.next()) {
                lastnames.add(i, String.valueOf(result2.getString("LastName")));
                firstnames.add(i, String.valueOf(result2.getString("FirstName")));
                departmentids.add(i, String.valueOf(result2.getInt("DeptID")));
                ids.add(i, String.valueOf(result2.getInt("PersonID")));
                status.add(i, String.valueOf(result2.getString("Status")));
                i = i + 1;
            }
            String expectedValue = String.valueOf(noOfRows);
            String endQuery1 = "delete from EmployeesSL";
            stmt.executeUpdate(endQuery1);

            // Asserting the Status code. Expected 200 OK
            assertEquals(statuscode, HttpStatus.SC_OK);
            // Asserting the Response Message.
            assertEquals(response, expectedValue);
            //Asserting against inserted values to database
            //persons[0]
            assertEquals(lastnames.get(0), "EmmaD");
            assertEquals(firstnames.get(0), "SloaneD");
            assertEquals(departmentids.get(0), "1");
            assertEquals(ids.get(0), "1");
            assertEquals(status.get(0), "active");
            //persons[1]
            assertEquals(lastnames.get(1), "KellyA");
            assertEquals(firstnames.get(1), "SloaneA");
            assertEquals(departmentids.get(1), "1");
            assertEquals(ids.get(1), "3");
            assertEquals(status.get(1), "inactive");
            //persons[2]
            assertEquals(lastnames.get(2), "DannyC");
            assertEquals(firstnames.get(2), "SloaneC");
            assertEquals(departmentids.get(2), "1");
            assertEquals(ids.get(2), "4");
            assertEquals(status.get(2), "inactive");

        } catch (IOException e) {
            log.error("Error while calling the BE server : " + e.getMessage(), e);
        }
    }

    @Test(enabled = false, description = "Tests batch insert with data size exceed")
    public void insertBatchDataSizeExceed() throws SQLException {
        log.info("Executing:insertBatchDataSizeExceed");
        String serviceURL = ballerinaURL + "/sql/insert/batchupdatesuccess";
        int i = 0;
        int noOfRows = 0;
        ArrayList<String> lastnames = new ArrayList<String>();
        ArrayList<String> firstnames = new ArrayList<String>();
        ArrayList<String> ages = new ArrayList<String>();
        ArrayList<String> ids = new ArrayList<String>();
        ArrayList<String> status = new ArrayList<String>();
        String payload = "{  \n" +
                "   \"persons\":[  \n" +
                "      {  \n" +
                "    \t \"id\":1,\n" +
                "         \"lastname\":\"EmmaD\",\n" +
                "         \"firstname\":\"SloaneD\",\n" +
                "         \"age\":25,\n" +
                "\t\t \"status\":\"active\"\t\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\":2,\n" +
                "    \t\"lastname\":\"HarryA\",\n" +
                "         \"firstname\":\"SloaneB\",\n" +
                "         \"age\":15,\n" +
                "         \"status\":\"testactivenotinsertingvaluefortestpurposes\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\":3,\n" +
                "    \t\"lastname\":\"KellyA\",\n" +
                "         \"firstname\":\"SloaneA\",\n" +
                "         \"age\":25,\n" +
                "         \"status\":\"inactive\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\":4,\n" +
                "    \t\"lastname\":\"DannyC\",\n" +
                "         \"firstname\":\"SloaneC\",\n" +
                "         \"age\":35,\n" +
                "         \"status\":\"inactive\"\n" +
                "      }\t\n" +
                "      \n" +
                "   ]\n" +
                "}";
        try {
            //Reading response and status code from response
            StringRequestEntity requestEntity = new StringRequestEntity(payload, "application/json", "UTF-8");
            PostMethod post = new PostMethod(serviceURL);
            post.setRequestEntity(requestEntity);
            int statuscode = client.executeMethod(post);
            String response = post.getResponseBodyAsString();

            //Querying the database to obtain values
            String query1 = "select count(*) as total from People";
            String query2 = "select * from People";
            ResultSet result1 = stmt.executeQuery(query1);
            while (result1.next()) {
                noOfRows = result1.getInt("total");
            }
            ResultSet result2 = stmt.executeQuery(query2);
            while (result2.next()) {
                lastnames.add(i, String.valueOf(result2.getString("LastName")));
                firstnames.add(i, String.valueOf(result2.getString("FirstName")));
                ages.add(i, String.valueOf(result2.getInt("Age")));
                ids.add(i, String.valueOf(result2.getInt("PersonID")));
                status.add(i, String.valueOf(result2.getString("Status")));
                i = i + 1;
            }
            String expectedValue = String.valueOf(noOfRows);
            String endQuery1 = "delete from People";
            stmt.executeUpdate(endQuery1);

            // Asserting the Status code. Expected 200 OK
            assertEquals(statuscode, HttpStatus.SC_OK);
            // Asserting the Response Message.
            assertEquals(response, expectedValue);
            //Asserting against inserted values to database
            //persons[0]
            assertEquals(lastnames.get(0), "EmmaD");
            assertEquals(firstnames.get(0), "SloaneD");
            assertEquals(ages.get(0), "25");
            assertEquals(ids.get(0), "1");
            assertEquals(status.get(0), "active");
            //persons[1]
            assertEquals(lastnames.get(1), "KellyA");
            assertEquals(firstnames.get(1), "SloaneA");
            assertEquals(ages.get(1), "25");
            assertEquals(ids.get(1), "3");
            assertEquals(status.get(1), "inactive");
            //persons[2]
            assertEquals(lastnames.get(2), "DannyC");
            assertEquals(firstnames.get(2), "SloaneC");
            assertEquals(ages.get(2), "35");
            assertEquals(ids.get(2), "4");
            assertEquals(status.get(2), "inactive");

        } catch (IOException e) {
            log.error("Error while calling the BE server : " + e.getMessage(), e);
        }
    }

    @Test(enabled = false, description = "Tests missing parameter in payload for non nullable column")
    public void insertBatchMissingParamNotNullCol() throws SQLException {
        log.info("Executing:insertBatchMissingParamNotNullCol");
        String serviceURL = ballerinaURL + "/sql/insert/batchupdatesuccess";
        int i = 0;
        int noOfRows = 0;
        ArrayList<String> lastnames = new ArrayList<String>();
        ArrayList<String> firstnames = new ArrayList<String>();
        ArrayList<String> ages = new ArrayList<String>();
        ArrayList<String> ids = new ArrayList<String>();
        ArrayList<String> status = new ArrayList<String>();
        String payload = "{  \n" +
                "   \"persons\":[  \n" +
                "      {  \n" +
                "    \t \"id\":1,\n" +
                "         \"lastname\":\"EmmaD\",\n" +
                "         \"firstname\":\"SloaneD\",\n" +
                "         \"age\":25,\n" +
                "\t\t \"status\":\"active\"\t\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\":2,\n" +
                "    \t\"firstname\":\"SloaneB\",\n" +
                "         \"age\":15,\n" +
                "         \"status\":\"active\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\":3,\n" +
                "    \t\"lastname\":\"KellyA\",\n" +
                "         \"firstname\":\"SloaneA\",\n" +
                "         \"age\":25,\n" +
                "         \"status\":\"inactive\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\":4,\n" +
                "    \t\"lastname\":\"DannyC\",\n" +
                "         \"firstname\":\"SloaneC\",\n" +
                "         \"age\":35,\n" +
                "         \"status\":\"inactive\"\n" +
                "      }\t\n" +
                "      \n" +
                "   ]\n" +
                "}";
        try {
            //Reading response and status code from response
            StringRequestEntity requestEntity = new StringRequestEntity(payload, "application/json", "UTF-8");
            PostMethod post = new PostMethod(serviceURL);
            post.setRequestEntity(requestEntity);
            int statuscode = client.executeMethod(post);
            String response = post.getResponseBodyAsString();

            //Querying the database to obtain values
            String query1 = "select count(*) as total from People";
            String query2 = "select * from People";
            ResultSet result1 = stmt.executeQuery(query1);
            while (result1.next()) {
                noOfRows = result1.getInt("total");
            }
            ResultSet result2 = stmt.executeQuery(query2);
            while (result2.next()) {
                lastnames.add(i, String.valueOf(result2.getString("LastName")));
                firstnames.add(i, String.valueOf(result2.getString("FirstName")));
                ages.add(i, String.valueOf(result2.getInt("Age")));
                ids.add(i, String.valueOf(result2.getInt("PersonID")));
                status.add(i, String.valueOf(result2.getString("Status")));
                i = i + 1;
            }
            String expectedValue = String.valueOf(noOfRows);
            String endQuery1 = "delete from People";
            stmt.executeUpdate(endQuery1);

            // Asserting the Status code. Expected 200 OK
            assertEquals(statuscode, HttpStatus.SC_OK);
            // Asserting the Response Message.
            assertEquals(response, expectedValue);
            //Asserting against inserted values to database
            //persons[0]
            assertEquals(lastnames.get(0), "EmmaD");
            assertEquals(firstnames.get(0), "SloaneD");
            assertEquals(ages.get(0), "25");
            assertEquals(ids.get(0), "1");
            assertEquals(status.get(0), "active");
            //persons[1]
            assertEquals(lastnames.get(1), "");
            assertEquals(firstnames.get(1), "SloaneB");
            assertEquals(ages.get(1), "15");
            assertEquals(ids.get(1), "2");
            assertEquals(status.get(1), "active");
            //persons[2]
            assertEquals(lastnames.get(2), "KellyA");
            assertEquals(firstnames.get(2), "SloaneA");
            assertEquals(ages.get(2), "25");
            assertEquals(ids.get(2), "3");
            assertEquals(status.get(2), "inactive");
            //persons[3]
            assertEquals(lastnames.get(3), "DannyC");
            assertEquals(firstnames.get(3), "SloaneC");
            assertEquals(ages.get(3), "35");
            assertEquals(ids.get(3), "4");
            assertEquals(status.get(3), "inactive");

        } catch (IOException e) {
            log.error("Error while calling the BE server : " + e.getMessage(), e);
        }
    }

    @Test(enabled = false, description = "Tests missing parameter in payload for nullable column")
    public void insertBatchMissingParamNullCol() throws SQLException {
        log.info("Executing:insertBatchMissingParamNullCol");
        String serviceURL = ballerinaURL + "/sql/insert/batchupdatesuccess";
        int i = 0;
        int noOfRows = 0;
        ArrayList<String> lastnames = new ArrayList<String>();
        ArrayList<String> firstnames = new ArrayList<String>();
        ArrayList<String> ages = new ArrayList<String>();
        ArrayList<String> ids = new ArrayList<String>();
        ArrayList<String> status = new ArrayList<String>();
        String payload = "{  \n" +
                "   \"persons\":[  \n" +
                "      {  \n" +
                "    \t \"id\":1,\n" +
                "         \"lastname\":\"EmmaD\",\n" +
                "         \"firstname\":\"SloaneD\",\n" +
                "         \"age\":25,\n" +
                "\t\t \"status\":\"active\"\t\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\":2,\n" +
                "    \t\"lastname\":\"HarryA\",\n" +
                "         \"age\":15,\n" +
                "         \"status\":\"active\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\":3,\n" +
                "    \t\"lastname\":\"KellyA\",\n" +
                "         \"firstname\":\"SloaneA\",\n" +
                "         \"age\":25,\n" +
                "         \"status\":\"inactive\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\":4,\n" +
                "    \t\"lastname\":\"DannyC\",\n" +
                "         \"firstname\":\"SloaneC\",\n" +
                "         \"age\":35,\n" +
                "         \"status\":\"inactive\"\n" +
                "      }\t\n" +
                "      \n" +
                "   ]\n" +
                "}";
        try {
            //Reading response and status code from response
            StringRequestEntity requestEntity = new StringRequestEntity(payload, "application/json", "UTF-8");
            PostMethod post = new PostMethod(serviceURL);
            post.setRequestEntity(requestEntity);
            int statuscode = client.executeMethod(post);
            String response = post.getResponseBodyAsString();

            //Querying the database to obtain values
            String query1 = "select count(*) as total from People";
            String query2 = "select * from People";
            ResultSet result1 = stmt.executeQuery(query1);
            while (result1.next()) {
                noOfRows = result1.getInt("total");
            }
            ResultSet result2 = stmt.executeQuery(query2);
            while (result2.next()) {
                lastnames.add(i, String.valueOf(result2.getString("LastName")));
                firstnames.add(i, String.valueOf(result2.getString("FirstName")));
                ages.add(i, String.valueOf(result2.getInt("Age")));
                ids.add(i, String.valueOf(result2.getInt("PersonID")));
                status.add(i, String.valueOf(result2.getString("Status")));
                i = i + 1;
            }
            String expectedValue = String.valueOf(noOfRows);
            String endQuery1 = "delete from People";
            stmt.executeUpdate(endQuery1);

            // Asserting the Status code. Expected 200 OK
            assertEquals(statuscode, HttpStatus.SC_OK);
            // Asserting the Response Message.
            assertEquals(response, expectedValue);
            //Asserting against inserted values to database
            //persons[0]
            assertEquals(lastnames.get(0), "EmmaD");
            assertEquals(firstnames.get(0), "SloaneD");
            assertEquals(ages.get(0), "25");
            assertEquals(ids.get(0), "1");
            assertEquals(status.get(0), "active");
            //persons[1]
            assertEquals(lastnames.get(1), "HarryA");
            assertEquals(firstnames.get(1), "");
            assertEquals(ages.get(1), "15");
            assertEquals(ids.get(1), "2");
            assertEquals(status.get(1), "active");
            //persons[2]
            assertEquals(lastnames.get(2), "KellyA");
            assertEquals(firstnames.get(2), "SloaneA");
            assertEquals(ages.get(2), "25");
            assertEquals(ids.get(2), "3");
            assertEquals(status.get(2), "inactive");
            //persons[3]
            assertEquals(lastnames.get(3), "DannyC");
            assertEquals(firstnames.get(3), "SloaneC");
            assertEquals(ages.get(3), "35");
            assertEquals(ids.get(3), "4");
            assertEquals(status.get(3), "inactive");

        } catch (IOException e) {
            log.error("Error while calling the BE server : " + e.getMessage(), e);
        }
    }

    @Test(enabled = false, description = "Tests missing parameter in payload for integer column")
    public void insertBatchMissingParamIntCol() throws SQLException {
        log.info("Executing:insertBatchMissingParamIntCol");
        String serviceURL = ballerinaURL + "/sql/insert/batchupdatesuccess";
        int i = 0;
        int noOfRows = 0;
        ArrayList<String> lastnames = new ArrayList<String>();
        ArrayList<String> firstnames = new ArrayList<String>();
        ArrayList<String> ages = new ArrayList<String>();
        ArrayList<String> ids = new ArrayList<String>();
        ArrayList<String> status = new ArrayList<String>();
        String payload = "{  \n" +
                "   \"persons\":[  \n" +
                "      {  \n" +
                "    \t \"id\":1,\n" +
                "         \"lastname\":\"EmmaD\",\n" +
                "         \"firstname\":\"SloaneD\",\n" +
                "         \"age\":25,\n" +
                "\t\t \"status\":\"active\"\t\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\":2,\n" +
                "    \t\"lastname\":\"HarryA\",\n" +
                "         \"firstname\":\"SloaneB\",\n" +
                "         \"status\":\"active\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\":3,\n" +
                "    \t\"lastname\":\"KellyA\",\n" +
                "         \"firstname\":\"SloaneA\",\n" +
                "         \"age\":25,\n" +
                "         \"status\":\"inactive\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\":4,\n" +
                "    \t\"lastname\":\"DannyC\",\n" +
                "         \"firstname\":\"SloaneC\",\n" +
                "         \"age\":35,\n" +
                "         \"status\":\"inactive\"\n" +
                "      }\t\n" +
                "      \n" +
                "   ]\n" +
                "}";
        try {
            //Reading response and status code from response
            StringRequestEntity requestEntity = new StringRequestEntity(payload, "application/json", "UTF-8");
            PostMethod post = new PostMethod(serviceURL);
            post.setRequestEntity(requestEntity);
            int statuscode = client.executeMethod(post);
            String response = post.getResponseBodyAsString();

            //Querying the database to obtain values
            String query1 = "select count(*) as total from People";
            String query2 = "select * from People";
            ResultSet result1 = stmt.executeQuery(query1);
            while (result1.next()) {
                noOfRows = result1.getInt("total");
            }
            ResultSet result2 = stmt.executeQuery(query2);
            while (result2.next()) {
                lastnames.add(i, String.valueOf(result2.getString("LastName")));
                firstnames.add(i, String.valueOf(result2.getString("FirstName")));
                ages.add(i, String.valueOf(result2.getInt("Age")));
                ids.add(i, String.valueOf(result2.getInt("PersonID")));
                status.add(i, String.valueOf(result2.getString("Status")));
                i = i + 1;
            }
            String expectedValue = String.valueOf(noOfRows);
            String endQuery1 = "delete from People";
            stmt.executeUpdate(endQuery1);

            // Asserting the Status code. Expected 200 OK
            assertEquals(statuscode, HttpStatus.SC_OK);
            // Asserting the Response Message.
            assertEquals(response, expectedValue);
            //Asserting against inserted values to database
            //persons[0]
            assertEquals(lastnames.get(0), "EmmaD");
            assertEquals(firstnames.get(0), "SloaneD");
            assertEquals(ages.get(0), "25");
            assertEquals(ids.get(0), "1");
            assertEquals(status.get(0), "active");
            //persons[1]
            assertEquals(lastnames.get(1), "HarryA");
            assertEquals(firstnames.get(1), "SloaneB");
            assertEquals(ages.get(1), "0");
            assertEquals(ids.get(1), "2");
            assertEquals(status.get(1), "active");
            //persons[2]
            assertEquals(lastnames.get(2), "KellyA");
            assertEquals(firstnames.get(2), "SloaneA");
            assertEquals(ages.get(2), "25");
            assertEquals(ids.get(2), "3");
            assertEquals(status.get(2), "inactive");
            //persons[3]
            assertEquals(lastnames.get(3), "DannyC");
            assertEquals(firstnames.get(3), "SloaneC");
            assertEquals(ages.get(3), "35");
            assertEquals(ids.get(3), "4");
            assertEquals(status.get(3), "inactive");

        } catch (IOException e) {
            log.error("Error while calling the BE server : " + e.getMessage(), e);
        }
    }

    @AfterClass(alwaysRun = true)
    public void afterTest() {
        String dropPeople = "drop table People";
        String dropEmployees = "drop table EmployeesSL";
        String dropDepartments = "drop table Departments";
        try {
            stmt.executeUpdate(dropPeople);
            stmt.executeUpdate(dropEmployees);
            stmt.executeUpdate(dropDepartments);
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
