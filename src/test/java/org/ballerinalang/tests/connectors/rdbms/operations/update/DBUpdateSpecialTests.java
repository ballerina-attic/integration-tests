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

public class DBUpdateSpecialTests extends BallerinaBaseTest {

    private static final Log log = LogFactory.getLog(DBUpdateSpecialTests.class);
    HttpClient client = new HttpClient();
    Connection conn = null;
    Statement stmt = null;
    Statement stmt2 = null;
    //String dbURL = "localhost:3306/baldb";
    //private String ballerinaURL = "http://localhost:9090";

    DBUpdateSpecialTests() {
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
        String createProducts = "CREATE TABLE IF NOT EXISTS Products (\n" + "    ProductID int NOT NULL,\n"
                + "    ProductName varchar(255) NOT NULL,\n" + "    CustomerID int,\n"
                + "    CategoryID int,\n" + "    Unit varchar(255),\n" + "    Price double,\n"
                + "    PRIMARY KEY (ProductID)\n" + ");";
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
        String insertProductOne = "insert into Products " +
                "(ProductID, ProductName, CustomerID, CategoryID, Unit, Price) " +
                "values (1, 'Chais', 6, 1, '10 boxes x 20 bags', 18.25);";
        String insertProductTwo = "insert into Products " +
                "(ProductID, ProductName, CustomerID, CategoryID, Unit, Price) " +
                "values (2, 'Chang', 2, 1, '24 - 12 oz bottles', 19.50);";
        String insertProductThree = "insert into Products " +
                "(ProductID, ProductName, CustomerID, CategoryID, Unit, Price) " +
                "values (3, 'Aniseed Syrup', 7, 2, '12 - 550 ml bottles', 10.0);";
        String insertProductFour = "insert into Products " +
                "(ProductID, ProductName, CustomerID, CategoryID, Unit, Price)" +
                " values (4, 'Chef Antons Cajun Seasoning', 15, 2, '48 - 6 oz jars', 22.75);";
        String insertProductFive = "insert into Products " +
                "(ProductID, ProductName, CustomerID, CategoryID, Unit, Price)" +
                " values (5, 'Chili Paste', 16, 2, '48 - 6 oz jars', 26.75);";
        String insertProductSix = "insert into Products " +
                "(ProductID, ProductName, CustomerID, CategoryID, Unit, Price)" +
                " values (6, 'Veg Chili Paste', 8, 2, '50 - 6 oz jars', 20.75);";

        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://" + dbURL + "?" + "user=" + TestConstants.MYSQL_USERNAME + "&password="
                            + TestConstants.MYSQL_PASSWORD);

            stmt = conn.createStatement();
            stmt.executeUpdate(createCustomers);
            stmt.executeUpdate(createProducts);
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
            stmt.executeUpdate(insertProductOne);
            stmt.executeUpdate(insertProductTwo);
            stmt.executeUpdate(insertProductThree);
            stmt.executeUpdate(insertProductFour);
            stmt.executeUpdate(insertProductFive);
            stmt.executeUpdate(insertProductSix);

        } catch (SQLException ex) {
            log.error("SQLException: " + ex.getMessage());
            log.error("SQLState: " + ex.getSQLState());
            log.error("VendorError: " + ex.getErrorCode());
        }
    }

    @Test(description = "Tests updating multiple tables")
    public void updateMultipleTables() throws SQLException {
        log.info("Executing:updateMultipleTables");
        String serviceURL = ballerinaURL + "/update/withParam/false";
        String payload = "UPDATE Customers, Products SET Products.Price=0.0" +
                ", TotalPurchases=TotalPurchases+5000 WHERE Customers.CustomerID=Products.CustomerID " +
                "AND Customers.Country='South Korea'";
        String expectedValue = "2";
        String actualChangedPrice = null;
        String actualChangedTotalPurchase = null;

        try {
            //Reading response and status code from response
            StringRequestEntity requestEntity = new StringRequestEntity(payload, "text/plain", "UTF-8");
            PostMethod post = new PostMethod(serviceURL);
            post.setRequestEntity(requestEntity);
            int statuscode = client.executeMethod(post);
            String response = post.getResponseBodyAsString();

            //Querying the database to obtain the updated values
            String query = "SELECT TotalPurchases from Customers WHERE CustomerID=8";
            ResultSet result = stmt.executeQuery(query);
            while (result.next()) {
                actualChangedTotalPurchase = String.valueOf(result.getDouble("TotalPurchases"));
            }
            query = "SELECT Price from Products WHERE CustomerID=8";
            result = stmt.executeQuery(query);
            while (result.next()) {
                actualChangedPrice = String.valueOf(result.getDouble("Price"));
            }

            // Asserting the Status code. Expected 200 OK
            assertEquals(statuscode, HttpStatus.SC_OK);
            // Asserting the Response Message.
            assertEquals(response, expectedValue);
            //Asserting against actual database values
            assertEquals(actualChangedPrice, "0.0");
            assertEquals(actualChangedTotalPurchase, "5000.0");
        } catch (IOException e) {
            log.error("Error while calling the BE server : " + e.getMessage(), e);
        }
    }

    @AfterClass(alwaysRun = true)
    public void afterTest() {
        String dropCustomers = "drop table Customers";
        String dropProducts = "drop table Products";

        try {
            stmt.executeUpdate(dropCustomers);
            stmt.executeUpdate(dropProducts);
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
