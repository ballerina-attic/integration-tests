package org.ballerinalang.tests.connectors.rdbms.operations.update;

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

import static org.testng.Assert.assertEquals;

/**
 * Tests database update operations and its variations
 * Uses UpdateTestService.bal service and DBUpdateTest.bal
 */

public class DBUpdateTests extends BallerinaBaseTest {

    private static final Log log = LogFactory.getLog(DBUpdateTests.class);
    HttpClient client = new HttpClient();
    Connection conn = null;
    Statement stmt = null;
    Statement stmt2 = null;
    //String dbURL = "localhost:3306/baldb";
    //private String ballerinaURL = "http://localhost:9090";

    DBUpdateTests() {
        client = new HttpClient();
        client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
        client.getHttpConnectionManager().getParams().setSoTimeout(8000);
    }

    @BeforeClass(alwaysRun = true)
    public void initializeTables() {
        String createCustomers = "CREATE TABLE IF NOT EXISTS Customers (\n" + "    CustomerID int NOT NULL,\n"
                + "    CustomerName varchar(255) NOT NULL,\n" + "    ContactName varchar(255),\n"
                + "    Address varchar(255),\n" + "    City varchar(255),\n" + "    PostalCode varchar(20),\n"
                + "    Country varchar(30),\n" + "    TotalPurchases double,\n" + "    LoyaltyPoints int,\n"
                + "    RecordTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n"
                + "    PRIMARY KEY (CustomerID)\n" + ");";
        String insertDataOne = "insert into Customers " +
                "(CustomerID, CustomerName, ContactName, Address, City, PostalCode" +
                ", Country, TotalPurchases, LoyaltyPoints) " +
                "values (1, 'Alfreds Futterkiste', 'Maria Anders', 'Obere Str. 57', 'Berlin'" +
                ", '12209', 'Germany', 12000.50, 60);";
        String insertDataTwo = "insert into Customers (CustomerID, CustomerName, ContactName, Address, City" +
                ", PostalCode, Country, TotalPurchases, LoyaltyPoints) " +
                "values (2, 'Ana Trujillo Emparedados y helados'" +
                ", 'Ana Trujillo', 'Avda. de la Constitución 2222', 'México D.F.', '05021', 'Mexico', 4678.25, 34);";
        String insertDataThree = "insert into Customers (CustomerID, CustomerName, ContactName, Address, City" +
                ", PostalCode, Country, TotalPurchases, LoyaltyPoints) values (3, 'Antonio Moreno Taquería'" +
                ", 'Antonio Moreno', 'Mataderos 2312', 'México D.F.', '05023', 'Mexico', 50550.25, 104);";
        String insertDataFour = "insert into Customers (CustomerID, CustomerName, ContactName, Address, City" +
                ", PostalCode, Country, TotalPurchases, LoyaltyPoints) values (4, 'Around the Horn', 'Thomas Hardy'" +
                ", '120 Hanover Sq.', 'London', 'WA1 1DP', 'UK', 1425.50, 2);";
        String insertDataFive = "insert into Customers (CustomerID, CustomerName, ContactName, Address, City" +
                ", PostalCode, Country, TotalPurchases, LoyaltyPoints) values (5, 'Berglunds snabbköp'" +
                ", 'Christina Berglund', 'Berguvsvägen 8', 'Luleå', 'S-958 22', 'Sweden', 5895.35, 75);";
        String insertDataSix = "insert into Customers (CustomerID, CustomerName, ContactName, Address, City" +
                ", PostalCode, Country, TotalPurchases, LoyaltyPoints) values (6, 'Padmasiri Samarapala', 'Somapala'" +
                ", '45, Jambugasmulla Mw, Nugegoda', 'Colombo', '000014','Sri Lanka', 6900.35, 85);";
        String insertDataSeven = "insert into Customers (CustomerID, CustomerName, ContactName, City" +
                ", PostalCode, Country, TotalPurchases, LoyaltyPoints) values (7, 'Jani Kethak', 'Sham Chris'" +
                ", 'Mumbai', '34000', 'India', 500.35, 0);";
        String insertDataEight = "insert into Customers (CustomerID, CustomerName, ContactName, City" +
                ", PostalCode, Country, TotalPurchases, LoyaltyPoints) values (8, 'Jae Peter', 'Sam Won'" +
                ", 'Gangam', 'xr567', 'South Korea', 0, 0);";
        String insertDataNine = "\n" +
                "insert into Customers (CustomerID, CustomerName, ContactName, Address, City, PostalCode, Country" +
                ", TotalPurchases, LoyaltyPoints) values (9, 'Kenau Reeves', 'Maria Anders', 'Tail Man 60'" +
                ", 'Gangam', '12209', 'South Korea', 14000.50, 60)";
        String insertDataTen = "\n" +
                "insert into Customers (CustomerID, CustomerName, ContactName, Address, City, PostalCode, Country" +
                ", TotalPurchases, LoyaltyPoints) values (10, 'Mac Grath', 'Maria Anders', 'Obere Str. 62'" +
                ", 'Berlin', '12209', 'Germany', 14000.50, 60)";
        String insertDataEleven = "\n" +
                "insert into Customers (CustomerID, CustomerName, ContactName, Address, City, PostalCode, Country" +
                ", TotalPurchases, LoyaltyPoints) values (11, 'Abel Merrywheather', 'Maria Anders', 'Obere Str. 65'" +
                ", 'Berlin', '12209', 'Germany', 14000.50, 60)";

        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://" + dbURL + "?" + "user=" + TestConstants.MYSQL_USERNAME + "&password="
                            + TestConstants.MYSQL_PASSWORD);

            stmt = conn.createStatement();
            stmt.executeUpdate(createCustomers);
            stmt.executeUpdate(insertDataOne);
            stmt.executeUpdate(insertDataTwo);
            stmt.executeUpdate(insertDataThree);
            stmt.executeUpdate(insertDataFour);
            stmt.executeUpdate(insertDataFive);
            stmt.executeUpdate(insertDataSix);
            stmt.executeUpdate(insertDataSeven);
            stmt.executeUpdate(insertDataEight);
            stmt.executeUpdate(insertDataNine);
            stmt.executeUpdate(insertDataTen);
            stmt.executeUpdate(insertDataEleven);

        } catch (SQLException ex) {
            log.error("SQLException: " + ex.getMessage());
            log.error("SQLState: " + ex.getSQLState());
            log.error("VendorError: " + ex.getErrorCode());
        }
    }

    @Test(description = "Tests updating a single record in single table")
    public void updateSingleRecordSingleTable() throws SQLException {
        log.info("Executing:updateSingleRecordSingleTable");
        String serviceURL = ballerinaURL + "/update/withParam/simple?value1=Obere%20Str.%2053&value2=14500.25";
        String payload = "UPDATE Customers SET Address=?, TotalPurchases=? WHERE CustomerID=2";
        String expectedValue = "1";
        String actualChangedAddress = null;
        String actualChangedTotalPurchase = null;

        try {
            //Reading response and status code from response
            StringRequestEntity requestEntity = new StringRequestEntity(payload, "text/plain", "UTF-8");
            PostMethod post = new PostMethod(serviceURL);
            post.setRequestEntity(requestEntity);
            int statuscode = client.executeMethod(post);
            String response = post.getResponseBodyAsString();

            //Querying the database to obtain the updated values
            String query = "SELECT Address, TotalPurchases from Customers WHERE CustomerID=2";
            ResultSet result = stmt.executeQuery(query);
            while (result.next()) {
                actualChangedAddress = result.getString("Address");
                actualChangedTotalPurchase = String.valueOf(result.getDouble("TotalPurchases"));
            }

            // Asserting the Status code. Expected 200 OK
            assertEquals(statuscode, HttpStatus.SC_OK);
            // Asserting the Response Message.
            assertEquals(response, expectedValue);
            //Asserting against actual database values
            assertEquals(actualChangedAddress, "Obere Str. 53");
            assertEquals(actualChangedTotalPurchase, "14500.25");
        } catch (IOException e) {
            log.error("Error while calling the BE server : " + e.getMessage(), e);
        }
    }

    @Test(description = "Tests updating multiple records in single table")
    public void updateMulRecordsSingleTable() throws SQLException {
        log.info("Executing:updateMulRecordsSingleTable");
        String serviceURL = ballerinaURL + "/update/withParam/simple?value1=hidden&value2=0.0";
        String payload = "UPDATE Customers SET Address=?, TotalPurchases=? WHERE Country='South Korea'";
        String expectedValue = "2";
        String actualChangedAddress = null;
        String actualChangedTotalPurchase = null;

        try {
            //Reading response and status code from response
            StringRequestEntity requestEntity = new StringRequestEntity(payload, "text/plain", "UTF-8");
            PostMethod post = new PostMethod(serviceURL);
            post.setRequestEntity(requestEntity);
            int statuscode = client.executeMethod(post);
            String response = post.getResponseBodyAsString();

            //Querying the database to obtain the updated values
            String query = "SELECT Address, TotalPurchases from Customers WHERE Country='South Korea'";
            ResultSet result = stmt.executeQuery(query);
            while (result.next()) {
                actualChangedAddress = result.getString("Address");
                actualChangedTotalPurchase = String.valueOf(result.getDouble("TotalPurchases"));
            }

            // Asserting the Status code. Expected 200 OK
            assertEquals(statuscode, HttpStatus.SC_OK);
            // Asserting the Response Message.
            assertEquals(response, expectedValue);
            //Asserting against actual database values
            assertEquals(actualChangedAddress, "hidden");
            assertEquals(actualChangedTotalPurchase, "0.0");
        } catch (IOException e) {
            log.error("Error while calling the BE server : " + e.getMessage(), e);
        }
    }

    @Test(description = "Tests updating records with lesser params")
    public void updateRecordsLesserParams() throws SQLException {
        log.info("Executing:updateRecordsLesserParams");
        String serviceURL = ballerinaURL + "/update/withParam/missing?value1=hidden";
        String payload = "UPDATE Customers SET Address=?, TotalPurchases=? WHERE Country='South Korea'";
        String expectedValue = "Error in database update. Please retry";

        try {
            //Reading response and status code from response
            StringRequestEntity requestEntity = new StringRequestEntity(payload, "text/plain", "UTF-8");
            PostMethod post = new PostMethod(serviceURL);
            post.setRequestEntity(requestEntity);
            int statuscode = client.executeMethod(post);
            String response = post.getResponseBodyAsString();

            // Asserting the Status code. Expected 200 OK
            assertEquals(statuscode, HttpStatus.SC_OK);
            // Asserting the Response Message.
            assertEquals(response, expectedValue);

        } catch (IOException e) {
            log.error("Error while calling the BE server : " + e.getMessage(), e);
        }
    }

    @Test(description = "Tests updating records with more params")
    public void updateRecordsMoreParams() throws SQLException {
        log.info("Executing:updateRecordsMoreParams");
        String serviceURL = ballerinaURL + "/update/withParam/missing?value1=hidden&value2=4.0";
        String payload = "UPDATE Customers SET Address='hidden', TotalPurchases=9.0 WHERE Country='South Korea'";
        String expectedValue = "Error in database update. Please retry";

        try {
            //Reading response and status code from response
            StringRequestEntity requestEntity = new StringRequestEntity(payload, "text/plain", "UTF-8");
            PostMethod post = new PostMethod(serviceURL);
            post.setRequestEntity(requestEntity);
            int statuscode = client.executeMethod(post);
            String response = post.getResponseBodyAsString();

            // Asserting the Status code. Expected 200 OK
            assertEquals(statuscode, HttpStatus.SC_OK);
            // Asserting the Response Message.
            assertEquals(response, expectedValue);

        } catch (IOException e) {
            log.error("Error while calling the BE server : " + e.getMessage(), e);
        }
    }

    @Test(description = "Tests updating records with invalid column name")
    public void updateRecordWInvalidColName() throws SQLException {
        log.info("Executing:updateRecordWInvalidColName");
        String serviceURL = ballerinaURL + "/update/withParam/missing?value1=hidden&value2=0.0";
        String payload = "UPDATE Customers SET Address=?, TotalPurchases1=? WHERE Country='South Korea'";
        String expectedValue = "Error in database update. Please retry";

        try {
            //Reading response and status code from response
            StringRequestEntity requestEntity = new StringRequestEntity(payload, "text/plain", "UTF-8");
            PostMethod post = new PostMethod(serviceURL);
            post.setRequestEntity(requestEntity);
            int statuscode = client.executeMethod(post);
            String response = post.getResponseBodyAsString();

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
        String dropCustomers = "drop table Customers";

        try {
            stmt.executeUpdate(dropCustomers);
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
