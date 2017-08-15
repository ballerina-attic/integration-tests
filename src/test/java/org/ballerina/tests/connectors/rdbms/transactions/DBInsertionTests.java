package org.ballerina.tests.connectors.rdbms.transactions;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ballerina.tests.TestConstants;
import org.ballerina.tests.base.BallerinaBaseTest;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;

public class DBInsertionTests {

    HttpClient client = new HttpClient();

    DBInsertionTests() {
        client = new HttpClient();
        client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
        client.getHttpConnectionManager().getParams().setSoTimeout(8000);
    }

    private static final Log log = LogFactory.getLog(RDBMSTransactions.class);
    private String ballerinaURL = "http://localhost:9090";
    Connection conn = null;
    Statement stmt = null;
    Statement stmt2 = null;
    String dbURL = "localhost:3306/baldb";

    @BeforeClass(alwaysRun = true)
    public void initializeTables() {
        String createPersons = "CREATE TABLE IF NOT EXISTS Persons (\n" + "    PersonID int NOT NULL AUTO_INCREMENT,\n"
                + "    LastName varchar(255) NOT NULL,\n" + "    FirstName varchar(255),\n"
                + "    Age int,\n" + "    PRIMARY KEY (PersonID)\n" + ");";
        String createEmployees = "CREATE TABLE IF NOT EXISTS Employees (\n" + "    NIC int NOT NULL,\n"
                + "    ID int NOT NULL AUTO_INCREMENT,\n" + "    LastName varchar(255) NOT NULL,\n"
                + "    FirstName varchar(255),\n" + "    Age int,\n" + "    INDEX (ID),\n"
                + "    PRIMARY KEY (NIC)\n" + ");";
        String createOrders = "CREATE TABLE IF NOT EXISTS Orders (\n" + "    OrderID int NOT NULL,\n"
                + "    OrderNumber int NOT NULL,\n" + "    PersonID int,\n"
                + "    PRIMARY KEY (OrderID),\n" + "    FOREIGN KEY (PersonID) REFERENCES Persons(PersonID)\n" + ");";

        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://" + dbURL + "?" + "user=" + TestConstants.MYSQL_USERNAME + "&password="
                            + TestConstants.MYSQL_PASSWORD);

            stmt = conn.createStatement();
            stmt.executeUpdate(createPersons);
            stmt.executeUpdate(createEmployees);
            stmt.executeUpdate(createOrders);

        } catch (SQLException ex) {
            log.error("SQLException: " + ex.getMessage());
            log.error("SQLState: " + ex.getSQLState());
            log.error("VendorError: " + ex.getErrorCode());
        }
    }

    @Test
    public void insertPerRecordIntoTable() throws SQLException {
        String serviceURL = ballerinaURL + "/sql/insert/perRecord";
        int i = 0;
        int keys[] = new int[2];
        String payload = "{  \n" +
                "   \"persons\":[  \n" +
                "      {  \n" +
                "         \"lastname\":\"Erandi\",\n" +
                "         \"firstname\":\"DeSilva\",\n" +
                "         \"age\":25\n" +
                "      },\n" +
                "      {\n" +
                "    \t\"lastname\":\"Chathurika\",\n" +
                "         \"firstname\":\"Perera\",\n" +
                "         \"age\":35\n" +
                "      }\n" +
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
            String query = "select personid from persons";
            ResultSet result = stmt.executeQuery(query);
            while (result.next()) {
                keys[i] = result.getInt("PersonID");
                i = i + 1;
            }
            String expectedValue = String.valueOf(keys[0]) + String.valueOf(keys[1]) + "insertedRowCount:2";
            System.out.print(expectedValue);

            // Asserting the Status code. Expected 200 OK
            assertEquals(statuscode, HttpStatus.SC_OK);
            // Asserting the Response Message.
            assertEquals(response, expectedValue);


        } catch (IOException e) {
            log.error("Error while calling the BE server : " + e.getMessage(), e);
        }
    }

    @Test
    public void insertAutoGeneratedToNonKeyCol() throws SQLException {
        String serviceURL = ballerinaURL + "/sql/insert/nonkey";
        int i = 0;
        int keys[] = new int[2];
        String payload = "{  \n" +
                "   \"employees\":[  \n" +
                "      {  \n" +
                "    \t \"nic\":1000000,\n" +
                "         \"lastname\":\"EmmaD\",\n" +
                "         \"firstname\":\"SloaneD\",\n" +
                "         \"age\":25\n" +
                "      },\n" +
                "      {\n" +
                "    \t\"nic\":10000001,\n" +
                "    \t\"lastname\":\"HarryA\",\n" +
                "         \"firstname\":\"SloaneA\",\n" +
                "         \"age\":35\n" +
                "      }\n" +
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
            String query = "select id from employees";
            ResultSet result = stmt.executeQuery(query);
            while (result.next()) {
                keys[i] = result.getInt("ID");
                System.out.print(keys[i]);
                i = i + 1;
            }
            String expectedValue = String.valueOf(keys[0]) + String.valueOf(keys[1]) + "insertedRowCount:2";

            // Asserting the Status code. Expected 200 OK
            assertEquals(statuscode, HttpStatus.SC_OK);
            // Asserting the Response Message.
            assertEquals(response, expectedValue);


        } catch (IOException e) {
            log.error("Error while calling the BE server : " + e.getMessage(), e);
        }
    }

    @Test
    public void insertWithMissingColumns() throws SQLException {
        String serviceURL = ballerinaURL + "/sql/insert/missingcol";
        int i = 0;
        int keys[] = new int[2];
        String payload = "{  \n" +
                "   \"employees\":[  \n" +
                "      {  \n" +
                "    \t \"nic\":1000000,\n" +
                "         \"lastname\":\"EmmaD\",\n" +
                "         \"firstname\":\"SloaneD\",\n" +
                "         \"age\":25\n" +
                "      },\n" +
                "      {\n" +
                "    \t\"nic\":10000001,\n" +
                "    \t\"lastname\":\"HarryA\",\n" +
                "         \"firstname\":\"SloaneA\",\n" +
                "         \"age\":35\n" +
                "      }\n" +
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

            // Asserting the Status code. Expected 200 OK
            assertEquals(statuscode, HttpStatus.SC_OK);
            // Asserting the Response Message.
            assertEquals(response, "Error in database insertion. Please retry");

        } catch (IOException e) {
            log.error("Error while calling the BE server : " + e.getMessage(), e);
        }
    }

    @Test
    public void insertWithMissingPrimaryDataForColumns() throws SQLException {
        String serviceURL = ballerinaURL + "/sql/insert/nonkey";
        int i = 0;
        int keys[] = new int[3];
        String payload = "{  \n" +
                "   \"employees\":[  \n" +
                "      {  \n" +
                "    \t \"lastname\":\"EmmaD\",\n" +
                "         \"firstname\":\"SloaneD\",\n" +
                "         \"age\":25\n" +
                "      },\n" +
                "      {\n" +
                "    \t\"nic\":100000024,\n" +
                "    \t\"lastname\":\"HarryA\",\n" +
                "         \"firstname\":\"SloaneA\",\n" +
                "         \"age\":35\n" +
                "      },\n" +
                "       {\n" +
                "    \t\"nic\":100000089,\n" +
                "    \t\"lastname\":\"HarryF\",\n" +
                "         \"firstname\":\"SloaneU\",\n" +
                "         \"age\":35\n" +
                "      }\n" +
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

            // Asserting the Status code. Expected 200 OK
            assertEquals(statuscode, HttpStatus.SC_OK);
            // Asserting the Response Message.
            assertEquals(response, "Error in database insertion. Please retry");

        } catch (IOException e) {
            log.error("Error while calling the BE server : " + e.getMessage(), e);
        }
    }

    @Test
    public void insertWithMissingOptionalDataForColumns() throws SQLException {
        String serviceURL = ballerinaURL + "/sql/insert/nonkey";
        int i = 0;
        int keys[] = new int[3];
        String payload = "{  \n" +
                "   \"employees\":[  \n" +
                "      {  \n" +
                "    \t \"nic\":10000005,\n" +
                "         \"lastname\":\"EmmaD\",\n" +
                "         \"age\":25\n" +
                "      },\n" +
                "      {\n" +
                "    \t\"nic\":100000024,\n" +
                "    \t\"lastname\":\"HarryA\",\n" +
                "         \"firstname\":\"SloaneA\",\n" +
                "         \"age\":35\n" +
                "      },\n" +
                "       {\n" +
                "    \t\"nic\":100000089,\n" +
                "    \t\"lastname\":\"HarryF\",\n" +
                "         \"firstname\":\"SloaneU\",\n" +
                "         \"age\":35\n" +
                "      }\n" +
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

            // Asserting the Status code. Expected 200 OK
            assertEquals(statuscode, HttpStatus.SC_OK);
            // Asserting the Response Message.
            assertEquals(response, "Error in database insertion. Please retry");

        } catch (IOException e) {
            log.error("Error while calling the BE server : " + e.getMessage(), e);
        }
    }

    @Test
    public void insertWithEmptyStringValuesForColumns() throws SQLException {
        String serviceURL = ballerinaURL + "/sql/insert/nonkey";
        int i = 0;
        int keys[] = new int[3];
        String payload = "{  \n" +
                "   \"employees\":[  \n" +
                "      {  \n" +
                "    \t \"nic\":10000005,\n" +
                "         \"lastname\":\"\",\n" +
                "         \"firstname\":\"SloaneD\",\n" +
                "         \"age\":25\n" +
                "      },\n" +
                "      {\n" +
                "    \t\"nic\":100000024,\n" +
                "    \t\"lastname\":\"HarryA\",\n" +
                "         \"firstname\":\"SloaneA\",\n" +
                "         \"age\":35\n" +
                "      },\n" +
                "       {\n" +
                "    \t\"nic\":100000089,\n" +
                "    \t\"lastname\":\"HarryF\",\n" +
                "         \"firstname\":\"SloaneU\",\n" +
                "         \"age\":35\n" +
                "      }\n" +
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
            String query = "select id from employees";
            ResultSet result = stmt.executeQuery(query);
            while (result.next()) {
                keys[i] = result.getInt("ID");
                System.out.println(keys[i]);
                i = i + 1;
            }
            System.out.println(keys[2]);
            System.out.println(response);
            String expectedValue = String.valueOf(keys[0]) + String.valueOf(keys[1])
                    + String.valueOf(keys[2]) + "insertedRowCount:3";

            // Asserting the Status code. Expected 200 OK
            assertEquals(statuscode, HttpStatus.SC_OK);
            // Asserting the Response Message.
            assertEquals(response, expectedValue);

        } catch (IOException e) {
            log.error("Error while calling the BE server : " + e.getMessage(), e);
        }
    }

    @Test
    public void insertWithNullForStringNotNullCol() throws SQLException {
        String serviceURL = ballerinaURL + "/sql/insert/nonkey";
        int i = 0;
        int keys[] = new int[2];
        String payload = "{  \n" +
                "   \"employees\":[  \n" +
                "      {  \n" +
                "    \t \"nic\":10000005,\n" +
                "         \"lastname\":null,\n" +
                "         \"firstname\":\"SloaneD\",\n" +
                "         \"age\":25\n" +
                "      },\n" +
                "      {\n" +
                "    \t\"nic\":100000024,\n" +
                "    \t\"lastname\":\"HarryA\",\n" +
                "         \"firstname\":\"SloaneA\",\n" +
                "         \"age\":35\n" +
                "      }\n" +
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

            // Asserting the Status code. Expected 200 OK
            assertEquals(statuscode, HttpStatus.SC_OK);
            // Asserting the Response Message.
            assertEquals(response, "Error in database insertion. Please retry");

        } catch (IOException e) {
            log.error("Error while calling the BE server : " + e.getMessage(), e);
        }
    }

     @Test
    public void insertWithNullForStringNullCol() throws SQLException {
        String serviceURL = ballerinaURL + "/sql/insert/nonkey";
        int i = 0;
        int keys[] = new int[2];
        String payload = "{  \n" +
                "   \"employees\":[  \n" +
                "      {  \n" +
                "    \t \"nic\":10000005,\n" +
                "         \"lastname\":\"EmmaD\",\n" +
                "         \"firstname\":null,\n" +
                "         \"age\":25\n" +
                "      },\n" +
                "      {\n" +
                "    \t\"nic\":100000024,\n" +
                "    \t\"lastname\":\"HarryA\",\n" +
                "         \"firstname\":\"SloaneA\",\n" +
                "         \"age\":35\n" +
                "      }\n" +
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
            String query = "select id from employees";
            ResultSet result = stmt.executeQuery(query);
            while (result.next()) {
                keys[i] = result.getInt("ID");
                System.out.println(keys[i]);
                i = i + 1;
            }
            String expectedValue = String.valueOf(keys[0]) + String.valueOf(keys[1])
                    + "insertedRowCount:2";

            // Asserting the Status code. Expected 200 OK
            assertEquals(statuscode, HttpStatus.SC_OK);
            // Asserting the Response Message.
            assertEquals(response, expectedValue);

        } catch (IOException e) {
            log.error("Error while calling the BE server : " + e.getMessage(), e);
        }
    }

    @Test
    public void insertWithNullForNumericNotNullCol() throws SQLException {
        String serviceURL = ballerinaURL + "/sql/insert/nonkey";
        int i = 0;
        int keys[] = new int[2];
        String payload = "{  \n" +
                "   \"employees\":[  \n" +
                "      {  \n" +
                "    \t \"nic\":null,\n" +
                "         \"lastname\":\"EmmaD\",\n" +
                "         \"firstname\":\"SloaneD\",\n" +
                "         \"age\":25\n" +
                "      },\n" +
                "      {\n" +
                "    \t\"nic\":100000024,\n" +
                "    \t\"lastname\":\"HarryA\",\n" +
                "         \"firstname\":\"SloaneA\",\n" +
                "         \"age\":35\n" +
                "      }\n" +
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

            // Asserting the Status code. Expected 200 OK
            assertEquals(statuscode, HttpStatus.SC_OK);
            // Asserting the Response Message.
            assertEquals(response, "Error in database insertion. Please retry");

        } catch (IOException e) {
            log.error("Error while calling the BE server : " + e.getMessage(), e);
        }
    }

     @Test
    public void insertWithNullForNumericNullCol() throws SQLException {
        String serviceURL = ballerinaURL + "/sql/insert/nonkey";
        int i = 0;
        int keys[] = new int[2];
        String payload = "{  \n" +
                "   \"employees\":[  \n" +
                "      {  \n" +
                "    \t \"nic\":10000005,\n" +
                "         \"lastname\":\"EmmaD\",\n" +
                "         \"firstname\":\"SloaneD\",\n" +
                "         \"age\":null\n" +
                "      },\n" +
                "      {\n" +
                "    \t\"nic\":100000024,\n" +
                "    \t\"lastname\":\"HarryA\",\n" +
                "         \"firstname\":\"SloaneA\",\n" +
                "         \"age\":35\n" +
                "      }\n" +
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
            String query = "select id from employees";
            ResultSet result = stmt.executeQuery(query);
            while (result.next()) {
                keys[i] = result.getInt("ID");
                System.out.println(keys[i]);
                i = i + 1;
            }
            String expectedValue = String.valueOf(keys[0]) + String.valueOf(keys[1])
                    + "insertedRowCount:2";

            // Asserting the Status code. Expected 200 OK
            assertEquals(statuscode, HttpStatus.SC_OK);
            // Asserting the Response Message.
            assertEquals(response, expectedValue);

        } catch (IOException e) {
            log.error("Error while calling the BE server : " + e.getMessage(), e);
        }
    }

    @AfterClass(alwaysRun = true)
    public void afterTest() {
        String dropOrders = "drop table orders";
        String dropPersons = "drop table persons";
        String dropEmployees = "drop table employees";
        try {
            stmt.executeUpdate(dropOrders);
            stmt.executeUpdate(dropPersons);
            stmt.executeUpdate(dropEmployees);
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


