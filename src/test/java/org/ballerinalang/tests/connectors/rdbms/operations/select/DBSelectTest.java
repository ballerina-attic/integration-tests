package org.ballerinalang.tests.connectors.rdbms.operations.select;

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
import java.sql.SQLException;
import java.sql.Statement;

import static org.testng.Assert.assertEquals;

/**
 * Tests database select operations and its variations
 * Uses SelectTestService.bal service and DBSelectTest.bal
 */

public class DBSelectTest extends BallerinaBaseTest {

    private static final Log log = LogFactory.getLog(DBSelectTest.class);
    HttpClient client = new HttpClient();
    Connection conn = null;
    Statement stmt = null;
    Statement stmt2 = null;
    //String dbURL = "localhost:3306/baldb";
    //private String ballerinaURL = "http://localhost:9090";

    DBSelectTest() {
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
        String insertProductOne = "insert into Products " +
                "(ProductID, ProductName, CustomerID, CategoryID, Unit, Price) " +
                "values (1, 'Chais', 1, 1, '10 boxes x 20 bags', 18.25);";
        String insertProductTwo = "insert into Products " +
                "(ProductID, ProductName, CustomerID, CategoryID, Unit, Price) " +
                "values (2, 'Chang', 1, 1, '24 - 12 oz bottles', 19.50);";
        String insertProductThree = "insert into Products " +
                "(ProductID, ProductName, CustomerID, CategoryID, Unit, Price) " +
                "values (3, 'Aniseed Syrup', 1, 2, '12 - 550 ml bottles', 10.0);";
        String insertProductFour = "insert into Products " +
                "(ProductID, ProductName, CustomerID, CategoryID, Unit, Price)" +
                " values (4, 'Chef Antons Cajun Seasoning', 2, 2, '48 - 6 oz jars', 22.75);";

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
            stmt.executeUpdate(insertProductOne);
            stmt.executeUpdate(insertProductTwo);
            stmt.executeUpdate(insertProductThree);
            stmt.executeUpdate(insertProductFour);

        } catch (SQLException ex) {
            log.error("SQLException: " + ex.getMessage());
            log.error("SQLState: " + ex.getSQLState());
            log.error("VendorError: " + ex.getErrorCode());
        }
    }

    @Test(description = "Tests select distinct")
    public void selectWithDistinct() throws SQLException {
        log.info("Executing:selectWithDistinct");
        String serviceURL = ballerinaURL + "/select/general/distinct";
        String payload = "SELECT DISTINCT Country FROM Customers;";
        String expectedValue = "[{\"Country\":\"Germany\"},{\"Country\":\"Mexico\"},{\"Country\":\"UK\"}" +
                ",{\"Country\":\"Sweden\"},{\"Country\":\"Sri Lanka\"}" +
                ",{\"Country\":\"India\"},{\"Country\":\"South Korea\"}]";
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

    @Test(description = "Tests select distinct with count")
    public void selectWithDistinctCount() throws SQLException {
        log.info("Executing:selectWithDistinctCount");
        String serviceURL = ballerinaURL + "/select/general/distinctwithcount";
        String payload = "SELECT COUNT(DISTINCT Country) FROM Customers;";
        String expectedValue = "[{\"COUNT(DISTINCT Country)\":7}]";
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

    @Test(description = "Tests select with where equality")
    public void selectWithWhereEqual() throws SQLException {
        log.info("Executing:selectWithWhereEqual");
        String serviceURL = ballerinaURL + "/select/general/where";
        String payload = "SELECT CustomerID FROM Customers WHERE Country = 'Sri Lanka';";
        String expectedValue = "[{\"CustomerID\":6}]";
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

    @Test(description = "Tests select with where not equal")
    public void selectWithWhereNotEqual() throws SQLException {
        log.info("Executing:selectWithWhereNotEqual");
        String serviceURL = ballerinaURL + "/select/general/where";
        String payload = "SELECT CustomerID FROM Customers WHERE Country <> 'Mexico';";
        String expectedValue = "[{\"CustomerID\":1},{\"CustomerID\":4},{\"CustomerID\":5}" +
                ",{\"CustomerID\":6},{\"CustomerID\":7},{\"CustomerID\":8}]";
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

    @Test(description = "Tests select with where greater than")
    public void selectWithWhereGreaterThan() throws SQLException {
        log.info("Executing:selectWithWhereGreaterThan");
        String serviceURL = ballerinaURL + "/select/general/where";
        String payload = "SELECT CustomerID FROM Customers WHERE LoyaltyPoints > 100;";
        String expectedValue = "[{\"CustomerID\":3}]";
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

    @Test(description = "Tests select with where less than")
    public void selectWithWhereLessThan() throws SQLException {
        log.info("Executing:selectWithWhereLessThan");
        String serviceURL = ballerinaURL + "/select/general/where";
        String payload = "SELECT CustomerID FROM Customers WHERE LoyaltyPoints < 3;";
        String expectedValue = "[{\"CustomerID\":4},{\"CustomerID\":7},{\"CustomerID\":8}]";
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

    @Test(description = "Tests select with where greater than or equal")
    public void selectWithWhereGreaterThanOrEqual() throws SQLException {
        log.info("Executing:selectWithWhereGreaterThanOrEqual");
        String serviceURL = ballerinaURL + "/select/general/where";
        String payload = "SELECT CustomerID FROM Customers WHERE LoyaltyPoints >= 85;";
        String expectedValue = "[{\"CustomerID\":3},{\"CustomerID\":6}]";
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

    @Test(description = "Tests select with where less than or equal")
    public void selectWithWhereLessThanOrEqual() throws SQLException {
        log.info("Executing:selectWithWhereLessThanOrEqual");
        String serviceURL = ballerinaURL + "/select/general/where";
        String payload = "SELECT CustomerID FROM Customers WHERE LoyaltyPoints <= 2;";
        String expectedValue = "[{\"CustomerID\":4},{\"CustomerID\":7},{\"CustomerID\":8}]";
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

    @Test(description = "Tests select with where between")
    public void selectWithWhereBetween() throws SQLException {
        log.info("Executing:selectWithWhereBetween");
        String serviceURL = ballerinaURL + "/select/general/between";
        String payload = "SELECT CustomerID FROM Customers WHERE TotalPurchases BETWEEN ? and ?";
        String expectedValue = "[{\"CustomerID\":5},{\"CustomerID\":6}]";
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

    @Test(description = "Tests select with where like")
    public void selectWithWhereLike() throws SQLException {
        log.info("Executing:selectWithWhereLike");
        String serviceURL = ballerinaURL + "/select/general/like";
        String payload = "SELECT CustomerID FROM Customers WHERE CustomerName LIKE ?;";
        String expectedValue = "[{\"CustomerID\":3},{\"CustomerID\":4}]";
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

    @Test(enabled = false, description = "Tests select with where in")
    public void selectWithWhereIn() throws SQLException {
        log.info("Executing:selectWithWhereIn");
        String serviceURL = ballerinaURL + "/select/general/in";
        String payload = "SELECT CustomerID FROM Customers WHERE Country IN (?)";
        String expectedValue = "[{\"CustomerID\":1},{\"CustomerID\":4}]";
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

    @Test(enabled = false, description = "Tests select with where not in")
    public void selectWithWhereNotIn() throws SQLException {
        log.info("Executing:selectWithWhereNotIn");
        String serviceURL = ballerinaURL + "/select/general/in";
        String payload = "SELECT CustomerID FROM Customers WHERE Country NOT IN (?)";
        String expectedValue = "[{\"CustomerID\":2},{\"CustomerID\":3},{\"CustomerID\":5}" +
                ",{\"CustomerID\":6},{\"CustomerID\":7},{\"CustomerID\":8}]";
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

    @Test(description = "Tests select with where and or")
    public void selectWithWhereAndOr() throws SQLException {
        log.info("Executing:selectWithWhereAndOr");
        String serviceURL = ballerinaURL + "/select/general/andor";
        String payload = "SELECT CustomerID FROM Customers WHERE Country=? AND (City=? OR City=?)";
        String expectedValue = "[{\"CustomerID\":1}]";
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

    @Test(description = "Tests select with where order by desc, asec")
    public void selectWithWhereOrderBy() throws SQLException {
        log.info("Executing:selectWithWhereOrderBy");
        String serviceURL = ballerinaURL + "/select/general/orderby";
        String payload = "SELECT CustomerID FROM Customers ORDER BY Country ASC, CustomerName DESC";
        String expectedValue = "[{\"CustomerID\":1},{\"CustomerID\":7},{\"CustomerID\":3},{\"CustomerID\":2}" +
                ",{\"CustomerID\":8},{\"CustomerID\":6},{\"CustomerID\":5},{\"CustomerID\":4}]";
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

    @Test(description = "Tests select with where is null")
    public void selectWithWhereIsNull() throws SQLException {
        log.info("Executing:selectWithWhereIsNull");
        String serviceURL = ballerinaURL + "/select/general/isnull";
        String payload = "SELECT CustomerID FROM Customers WHERE Address IS NULL";
        String expectedValue = "[{\"CustomerID\":7},{\"CustomerID\":8}]";

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

    @Test(description = "Tests select with where is not null")
    public void selectWithWhereIsNotNull() throws SQLException {
        log.info("Executing:selectWithWhereIsNotNull");
        String serviceURL = ballerinaURL + "/select/general/isnotnull";
        String payload = "SELECT CustomerID FROM Customers WHERE Address IS NOT NULL";
        String expectedValue = "[{\"CustomerID\":1},{\"CustomerID\":2},{\"CustomerID\":3}" +
                ",{\"CustomerID\":4},{\"CustomerID\":5},{\"CustomerID\":6}]";

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

    @Test(enabled = false, description = "Tests select with where limit")
    public void selectWithWhereLimit() throws SQLException {
        log.info("Executing:selectWithWhereLimit");
        String serviceURL = ballerinaURL + "/select/general/limit";
        String payload = "";
        String expectedValue = "[{\"CustomerName\":\"Antonio Moreno Taquería\"}" +
                ",{\"CustomerName\":\"Alfreds Futterkiste\"},{\"CustomerName\":\"Padmasiri Samarapala\"}" +
                ",{\"CustomerName\":\"Berglunds snabbköp\"}]";

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

    @Test(description = "Tests select with minimum value")
    public void selectWithMinimum() throws SQLException {
        log.info("Executing:selectWithMinimum");
        String serviceURL = ballerinaURL + "/select/general/min";
        String payload = "SELECT MIN(TotalPurchases) AS SmallestPurchase FROM Customers;";
        String expectedValue = "[{\"SmallestPurchase\":0.0}]";

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

    @Test(description = "Tests select with maximum value")
    public void selectWithMaximum() throws SQLException {
        log.info("Executing:selectWithMaximum");
        String serviceURL = ballerinaURL + "/select/general/max";
        String payload = "SELECT MAX(TotalPurchases) AS LargestPurchase FROM Customers";
        String expectedValue = "[{\"LargestPurchase\":50550.25}]";

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

    @Test(description = "Tests select with average and sum")
    public void selectWithAverageSum() throws SQLException {
        log.info("Executing:selectWithAverageSum");
        String serviceURL = ballerinaURL + "/select/general/sumavg";
        String payload = "SELECT SUM(TotalPurchases) as SumOfPurchases" +
                ", AVG(TotalPurchases) as AvgOfPurchases FROM Customers;";
        String expectedValue = "[{\"SumOfPurchases\":81950.55000000002" +
                ",\"AvgOfPurchases\":10243.818750000002}]";

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

    @Test(description = "Tests select with having clause")
    public void selectWithHaving() throws SQLException {
        log.info("Executing:selectWithHaving");
        String serviceURL = ballerinaURL + "/select/general/having";
        String payload = "SELECT COUNT(CustomerID) as CustomerIDCount" +
                ", Country FROM Customers GROUP BY Country HAVING COUNT(CustomerID) < 3 ";
        String expectedValue = "[{\"CustomerIDCount\":1,\"Country\":\"Germany\"}" +
                ",{\"CustomerIDCount\":1,\"Country\":\"India\"},{\"CustomerIDCount\":2,\"Country\":\"Mexico\"}" +
                ",{\"CustomerIDCount\":1,\"Country\":\"South Korea\"}" +
                ",{\"CustomerIDCount\":1,\"Country\":\"Sri Lanka\"}" +
                ",{\"CustomerIDCount\":1,\"Country\":\"Sweden\"},{\"CustomerIDCount\":1,\"Country\":\"UK\"}]";

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

    @Test(description = "Tests select with exists")
    public void selectWithExists() throws SQLException {
        log.info("Executing:selectWithExists");
        String serviceURL = ballerinaURL + "/select/general/exists";
        String payload = "SELECT CustomerName FROM Customers WHERE EXISTS" +
                " (SELECT ProductName FROM Products WHERE CustomerID = Customers.CustomerID AND Price < ?)";
        String expectedValue = "[{\"CustomerName\":\"Alfreds Futterkiste\"}]";

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

    @Test(description = "Tests select with not exists")
    public void selectWithNotExists() throws SQLException {
        log.info("Executing:selectWithNotExists");
        String serviceURL = ballerinaURL + "/select/general/exists";
        String payload = "SELECT CustomerName FROM Customers WHERE NOT EXISTS " +
                "(SELECT ProductName FROM Products WHERE CustomerID = Customers.CustomerID AND Price < ?)";
        String expectedValue = "[{\"CustomerName\":\"Ana Trujillo Emparedados y helados\"}" +
                ",{\"CustomerName\":\"Antonio Moreno TaquerÃ\u00ADa\"}" +
                ",{\"CustomerName\":\"Around the Horn\"},{\"CustomerName\":\"Berglunds snabbkÃ¶p\"}" +
                ",{\"CustomerName\":\"Padmasiri Samarapala\"},{\"CustomerName\":\"Jani Kethak\"}" +
                ",{\"CustomerName\":\"Jae Peter\"}]";

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

    @Test(description = "Tests select with a complex sql that uses several functions and operators")
    public void selectWithComplexSql() throws SQLException {
        log.info("Executing:selectWithComplexSql");
        String serviceURL = ballerinaURL + "/select/general/complexsql";
        String payload = "";
        String expectedValue = "[{\"Country\":\"Germany\",\"MaxBuyingRatio\":0.004}" +
                ",{\"Country\":\"India\",\"MaxBuyingRatio\":0.0},{\"Country\":\"Mexico\",\"MaxBuyingRatio\":0.007}" +
                ",{\"Country\":\"Sri Lanka\",\"MaxBuyingRatio\":0.012}" +
                ",{\"Country\":\"Sweden\",\"MaxBuyingRatio\":0.012},{\"Country\":\"UK\",\"MaxBuyingRatio\":0.001}]";

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
