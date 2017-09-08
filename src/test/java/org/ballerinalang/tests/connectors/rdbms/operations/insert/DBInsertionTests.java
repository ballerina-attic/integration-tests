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

public class DBInsertionTests extends BallerinaBaseTest {

    private static final Log log = LogFactory.getLog(DBInsertionTests.class);
    HttpClient client = new HttpClient();
    Connection conn = null;
    Statement stmt = null;
    Statement stmt2 = null;
    //String dbURL = "localhost:3306/baldb";
    //private String ballerinaURL = "http://localhost:9090";

    DBInsertionTests() {
        client = new HttpClient();
        client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
        client.getHttpConnectionManager().getParams().setSoTimeout(8000);
    }

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
        String createSampleTable = "CREATE TABLE IF NOT EXISTS Samplepersons (\n" + "    PersonID int NOT NULL,\n"
                + "    LastName varchar(255),\n" + "    FirstName varchar(255),\n"
                + "    Age int,\n"
                + "    Status varchar(10),\n"
                + "    PRIMARY KEY (PersonID)\n" + ");";

        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://" + dbURL + "?" + "user=" + TestConstants.MYSQL_USERNAME + "&password="
                            + TestConstants.MYSQL_PASSWORD);

            stmt = conn.createStatement();
            stmt.executeUpdate(createPersons);
            stmt.executeUpdate(createEmployees);
            stmt.executeUpdate(createOrders);
            stmt.executeUpdate(createSampleTable);

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
        ArrayList<String> keys = new ArrayList<String>();
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
            String query = "select personid from Persons";
            ResultSet result = stmt.executeQuery(query);
            while (result.next()) {
                keys.add(i, String.valueOf(result.getInt("PersonID")));
                i = i + 1;
            }
            String expectedValue = keys.get(0) + keys.get(1) + "insertedRowCount:2";
            String endQuery = "delete from Persons";
            stmt.executeUpdate(endQuery);

            // Asserting the Status code. Expected 200 OK
            assertEquals(statuscode, HttpStatus.SC_OK);
            // Asserting the Response Message.
            assertEquals(response, expectedValue);


        } catch (IOException e) {
            log.error("Error while calling the BE server : " + e.getMessage(), e);
        }
    }

    @Test(enabled = false)
    public void insertAutoGeneratedToNonKeyCol() throws SQLException {
        String serviceURL = ballerinaURL + "/sql/insert/nonkey";
        int i = 0;
        ArrayList<String> keys = new ArrayList<String>();
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
            String query = "select ID from Employees";
            ResultSet result = stmt.executeQuery(query);
            while (result.next()) {
                keys.add(i, String.valueOf(result.getInt("ID")));
                i = i + 1;
            }
            String expectedValue = keys.get(0) + keys.get(1) + "insertedRowCount:2";
            String endQuery = "delete from Employees";
            stmt.executeUpdate(endQuery);

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

    @Test(enabled = false)
    public void insertWithEmptyStringValuesForColumns() throws SQLException {
        String serviceURL = ballerinaURL + "/sql/insert/nonkey";
        int i = 0;
        ArrayList<String> keys = new ArrayList<String>();
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
            String query = "select ID from Employees";
            ResultSet result = stmt.executeQuery(query);
            while (result.next()) {
                keys.add(i, String.valueOf(result.getInt("ID")));
                i = i + 1;
            }
            String expectedValue = keys.get(0) + keys.get(1)
                    + keys.get(2) + "insertedRowCount:3";
            String endQuery = "delete from Employees";
            stmt.executeUpdate(endQuery);

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

    @Test (enabled = false)
    public void insertWithNullForStringNullCol() throws SQLException {
        String serviceURL = ballerinaURL + "/sql/insert/nonkey";
        int i = 0;
        ArrayList<String> keys = new ArrayList<String>();
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
            String query = "select id from Employees";
            ResultSet result = stmt.executeQuery(query);
            while (result.next()) {
                keys.add(i, String.valueOf(result.getInt("ID")));
                i = i + 1;
            }
            String expectedValue = keys.get(0) + keys.get(1)
                    + "insertedRowCount:2";
            String endQuery = "delete from Employees";
            stmt.executeUpdate(endQuery);

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

    @Test(enabled = false)
    public void insertWithNullForNumericNullCol() throws SQLException {
        String serviceURL = ballerinaURL + "/sql/insert/nonkey";
        int i = 0;
        ArrayList<String> keys = new ArrayList<String>();
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
            String query = "select id from Employees";
            ResultSet result = stmt.executeQuery(query);
            while (result.next()) {
                keys.add(i, String.valueOf(result.getInt("ID")));
                i = i + 1;
            }
            String expectedValue = keys.get(0) + keys.get(1)
                    + "insertedRowCount:2";
            String endQuery = "delete from Employees";
            stmt.executeUpdate(endQuery);

            // Asserting the Status code. Expected 200 OK
            assertEquals(statuscode, HttpStatus.SC_OK);
            // Asserting the Response Message.
            assertEquals(response, expectedValue);

        } catch (IOException e) {
            log.error("Error while calling the BE server : " + e.getMessage(), e);
        }
    }

    @Test
    public void insertWithPrimaryKeyViolation() throws SQLException {
        String serviceURL = ballerinaURL + "/sql/insert/nonkey";
        int i = 0;
        String payload = "{  \n" +
                "   \"employees\":[  \n" +
                "      {  \n" +
                "    \t \"nic\":123,\n" +
                "         \"lastname\":\"EmmaD\",\n" +
                "         \"firstname\":\"SloaneD\",\n" +
                "         \"age\":25\n" +
                "      },\n" +
                "      {\n" +
                "    \t\"nic\":123,\n" +
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
    public void insertWithoutCol() throws SQLException {
        String serviceURL = ballerinaURL + "/sql/insert/withoutcol";
        int i = 0;
        int noOfRows = 0;
        ArrayList<String> ids = new ArrayList<String>();
        ArrayList<String> lastnames = new ArrayList<String>();
        ArrayList<String> firstnames = new ArrayList<String>();
        ArrayList<String> ages = new ArrayList<String>();
        ArrayList<String> status = new ArrayList<String>();
        String payload = "{  \n" +
                "   \"persons\":[  \n" +
                "      {  \n" +
                "    \t \"id\":1,\n" +
                "         \"lastname\":\"EmmaD\",\n" +
                "         \"firstname\":\"SloaneD\",\n" +
                "         \"age\":25,\n" +
                "\t \"status\":\"active\"\t\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\":2,\n" +
                "    \t\"lastname\":\"HarryA\",\n" +
                "         \"firstname\":\"SloaneB\",\n" +
                "         \"age\":15,\n" +
                "         \"status\":\"active\"\n" +
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
            String query1 = "select count(*) as total from Samplepersons";
            String query2 = "select * from Samplepersons";
            ResultSet result1 = stmt.executeQuery(query1);
            while (result1.next()) {
                noOfRows = result1.getInt("total");
            }
            ResultSet result2 = stmt.executeQuery(query2);
            while (result2.next()) {
                ids.add(i, result2.getString("PersonID"));
                lastnames.add(i, result2.getString("lastname"));
                firstnames.add(i, result2.getString("firstname"));
                ages.add(i, result2.getString("Age"));
                status.add(i, result2.getString("Status"));
                i = i + 1;
            }
            String expectedValue = String.valueOf(noOfRows);
            String endQuery = "delete from Samplepersons";
            stmt.executeUpdate(endQuery);

            // Asserting the Status code. Expected 200 OK
            assertEquals(statuscode, HttpStatus.SC_OK);
            // Asserting the Response Message.
            assertEquals(response, expectedValue);
            //Asserting values from database
            //temp[0]
            assertEquals(ids.get(0), "1");
            assertEquals(lastnames.get(0), "EmmaD");
            assertEquals(firstnames.get(0), "SloaneD");
            assertEquals(ages.get(0), "25");
            assertEquals(status.get(0), "active");
            //temp[1]
            assertEquals(ids.get(1), "2");
            assertEquals(lastnames.get(1), "HarryA");
            assertEquals(firstnames.get(1), "SloaneB");
            assertEquals(ages.get(1), "15");
            assertEquals(status.get(1), "active");


        } catch (IOException e) {
            log.error("Error while calling the BE server : " + e.getMessage(), e);
        }
    }

    @AfterClass(alwaysRun = true)
    public void afterTest() {
        String dropOrders = "drop table Orders";
        String dropPersons = "drop table Persons";
        String dropEmployees = "drop table Employees";
        String dropSamplePersons = "drop table Samplepersons";

        try {
            stmt.executeUpdate(dropOrders);
            stmt.executeUpdate(dropPersons);
            stmt.executeUpdate(dropEmployees);
            stmt.executeUpdate(dropSamplePersons);
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


