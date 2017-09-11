package org.ballerinalang.tests.connectors.rdbms.operations.call;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
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
 * Tests database stored procedure creation and related operations
 * Uses ProcedureTestService.bal service and DBStoredProcedureTest.bal
 */


public class DBStoredProcedureTest extends BallerinaBaseTest {

    private static final Log log = LogFactory.getLog(DBStoredProcedureTest.class);
    HttpClient client = new HttpClient();
    Connection conn = null;
    Statement stmt = null;
    Statement stmt2 = null;
    //String dbURL = "localhost:3306/baldb";
    //private String ballerinaURL = "http://localhost:9090";

    DBStoredProcedureTest() {
        client = new HttpClient();
        client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
        client.getHttpConnectionManager().getParams().setSoTimeout(8000);
    }

    @BeforeClass(alwaysRun = true)
    public void initializeTables() {
        String createOrders = "CREATE TABLE IF NOT EXISTS orders (\n" + "    orderId int NOT NULL,\n"
                + "    customerNumber int,\n" + "    status varchar(20),\n"
                + "    location varchar(20),\n"
                + "    PRIMARY KEY (orderId)\n" + ");";
        String insertQueryOne = "insert into orders (orderId, customerNumber, status, location) " +
                "values (1, 001, 'Shipped', 'srilanka');";
        String insertQueryTwo = "insert into orders (orderId, customerNumber, status, location) " +
                "values (2, 002, 'Shipped', 'srilanka');";
        String insertQueryThree = "insert into orders (orderId, customerNumber, status, location) " +
                "values (3, 003, 'Shipped', 'us');";
        String insertQueryFive = "insert into orders (orderId, customerNumber, status, location) " +
                "values (5, 002, 'Canceled', 'srilanka');";
        String insertQueryFour = "insert into orders (orderId, customerNumber, status, location)" +
                " values (4, 001, 'Canceled', 'srilanka');";
        String insertQuerySix = "insert into orders (orderId, customerNumber, status, location) " +
                "values (6, 003, 'Canceled', 'us');";
        String insertQuerySeven = "insert into orders (orderId, customerNumber, status, location) " +
                "values (7, 001, 'Resolved', 'srilanka');";
        String insertQueryEight = "insert into orders (orderId, customerNumber, status, location) " +
                "values (8, 002, 'Resolved', 'srilanka');";
        String insertQueryNine = "insert into orders (orderId, customerNumber, status, location) " +
                "values (9, 003, 'Resolved', 'us');";
        String insertQueryTen = "insert into orders (orderId, customerNumber, status, location) " +
                "values (10, 001, 'Disputed', 'srilanka');";
        String insertQueryEleven = "insert into orders (orderId, customerNumber, status, location) " +
                "values (11, 002, 'Disputed', 'srilanka');";
        String insertQueryTwelve = "insert into orders (orderId, customerNumber, status, location) " +
                "values (12, 003, 'Disputed', 'us');";

        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://" + dbURL + "?" + "user=" + TestConstants.MYSQL_USERNAME + "&password="
                            + TestConstants.MYSQL_PASSWORD);

            stmt = conn.createStatement();
            stmt.executeUpdate(createOrders);
            stmt.executeUpdate(insertQueryOne);
            stmt.executeUpdate(insertQueryTwo);
            stmt.executeUpdate(insertQueryThree);
            stmt.executeUpdate(insertQueryFour);
            stmt.executeUpdate(insertQueryFive);
            stmt.executeUpdate(insertQuerySix);
            stmt.executeUpdate(insertQuerySeven);
            stmt.executeUpdate(insertQueryEight);
            stmt.executeUpdate(insertQueryNine);
            stmt.executeUpdate(insertQueryTen);
            stmt.executeUpdate(insertQueryEleven);
            stmt.executeUpdate(insertQueryTwelve);
            Thread.sleep(5000);

        } catch (SQLException ex) {
            log.error("SQLException: " + ex.getMessage());
            log.error("SQLState: " + ex.getSQLState());
            log.error("VendorError: " + ex.getErrorCode());
        } catch (InterruptedException e) {
            log.error("SQLException: " + e.getMessage());
        }
    }

    @Test(description = "This tests creation of a procedure through ballerina")
    public void createProcedure() throws SQLException {
        log.info("Executing:createProcedure");
        String serviceURL = ballerinaURL + "/procedure/create";
        String resultFromDb = null;
        String payload = "CREATE PROCEDURE get_order_by_cust(\n" +
                " IN cust_no INT,\n" +
                " IN inc INT,\n" +
                " OUT shipped INT,\n" +
                " OUT canceled INT,\n" +
                " OUT resolved INT,\n" +
                " OUT disputed INT,\n" +
                " INOUT count INT)\n" +
                "BEGIN\n" +
                "DECLARE no_of_srilankans INT DEFAULT 0;\n" +
                " -- shipped\n" +
                " SELECT\n" +
                "            count(*) INTO shipped\n" +
                "        FROM\n" +
                "            orders\n" +
                "        WHERE\n" +
                "            customerNumber = cust_no\n" +
                "                AND status = 'Shipped';\n" +
                " \n" +
                " -- canceled\n" +
                " SELECT\n" +
                "            count(*) INTO canceled\n" +
                "        FROM\n" +
                "            orders\n" +
                "        WHERE\n" +
                "            customerNumber = cust_no\n" +
                "                AND status = 'Canceled';\n" +
                " \n" +
                " -- resolved\n" +
                " SELECT\n" +
                "            count(*) INTO resolved\n" +
                "        FROM\n" +
                "            orders\n" +
                "        WHERE\n" +
                "            customerNumber = cust_no\n" +
                "                AND status = 'Resolved';\n" +
                " \n" +
                " -- disputed\n" +
                " SELECT\n" +
                "            count(*) INTO disputed\n" +
                "        FROM\n" +
                "            orders\n" +
                "        WHERE\n" +
                "            customerNumber = cust_no\n" +
                "                AND status = 'Disputed';\n" +
                "\n" +
                "SET count = count + inc;\n" +
                "\n" +
                "SELECT \n" +
                "\tcustomerNumber, status, location \n" +
                "\tFROM\n" +
                "\t   orders;\t\n" +
                " \n" +
                "END";
        try {
            //Reading response and status code from response
            StringRequestEntity requestEntity = new StringRequestEntity(payload, "text/plain", "UTF-8");
            PostMethod post = new PostMethod(serviceURL);
            post.setRequestEntity(requestEntity);
            int statuscode = client.executeMethod(post);
            String response = post.getResponseBodyAsString();

            //Querying the database to check actual creation of procedure
            String query = "show create procedure get_order_by_cust";
            boolean status = stmt.execute(query);
            if (status) {
                ResultSet result = stmt.getResultSet();
                while (result.next()) {
                    resultFromDb = result.getString("Procedure");
                    if (resultFromDb != null) {
                        break;
                    }
                }
            } else {
                resultFromDb = "Error in procedure check";
            }

            // Asserting the Status code. Expected 200 OK
            assertEquals(statuscode, HttpStatus.SC_OK);
            // Asserting the Response Message.
            //assertEquals(response, "Procedure created successfully.");
            //Asserting procedure existence from database
            //assertEquals(resultFromDb, "get_order_by_cust");
        } catch (IOException e) {
            log.error("Error while calling the BE server : " + e.getMessage(), e);
        }
    }

    @Test(description = "This tests return of all out parameters")
    public void invokeProcedureWithAllParams() throws SQLException {
        log.info("Executing:invokeProcedureWithAllParams");
        String serviceURL = ballerinaURL + "/procedure/callsucces/parameter?custNo=1";

        try {
            //Reading response and status code from response
            GetMethod get = new GetMethod(serviceURL);
            int statuscode = client.executeMethod(get);
            String response = get.getResponseBodyAsString();

            // Asserting the Status code. Expected 200 OK
            assertEquals(statuscode, HttpStatus.SC_OK);
            // Asserting the Response Message.
            assertEquals(response, "1:1:1:1:5");
        } catch (IOException e) {
            log.error("Error while calling the BE server : " + e.getMessage(), e);
        }
    }

    @Test(description = "This tests direction change of a in parameter to out")
    public void invokeProcWithDirInToOutParams() throws SQLException {
        log.info("Executing:invokeProcWithDirInToOutParams");
        String serviceURL = ballerinaURL + "/procedure/call/directionchange?custNo=1&status=intoout";

        try {
            //Reading response and status code from response
            GetMethod get = new GetMethod(serviceURL);
            int statuscode = client.executeMethod(get);
            String response = get.getResponseBodyAsString();

            // Asserting the Status code. Expected 200 OK
            assertEquals(statuscode, HttpStatus.SC_OK);
            // Asserting the Response Message.
            assertEquals(response, "Error in procedure call. Please retry");
        } catch (IOException e) {
            log.error("Error while calling the BE server : " + e.getMessage(), e);
        }
    }

    @Test(description = "This tests direction change of an in parameter to inout")
    public void invokeProcWithDirInToInOutParams() throws SQLException {
        log.info("Executing:invokeProcWithDirInToInOutParams");
        String serviceURL = ballerinaURL + "/procedure/call/directionchange?custNo=1&status=intoinout";

        try {
            //Reading response and status code from response
            GetMethod get = new GetMethod(serviceURL);
            int statuscode = client.executeMethod(get);
            String response = get.getResponseBodyAsString();

            // Asserting the Status code. Expected 200 OK
            assertEquals(statuscode, HttpStatus.SC_OK);
            // Asserting the Response Message.
            assertEquals(response, "Error in procedure call. Please retry");
        } catch (IOException e) {
            log.error("Error while calling the BE server : " + e.getMessage(), e);
        }
    }

    @Test(description = "This tests direction change of an out parameter to in")
    public void invokeProcWithDirOutToInParams() throws SQLException {
        log.info("Executing:invokeProcWithDirOutToInParams");
        String serviceURL = ballerinaURL + "/procedure/call/directionchange?custNo=1&status=outtoin";

        try {
            //Reading response and status code from response
            GetMethod get = new GetMethod(serviceURL);
            int statuscode = client.executeMethod(get);
            String response = get.getResponseBodyAsString();

            // Asserting the Status code. Expected 200 OK
            assertEquals(statuscode, HttpStatus.SC_OK);
            // Asserting the Response Message.
            assertEquals(response, "0:1:1:1:5");
        } catch (IOException e) {
            log.error("Error while calling the BE server : " + e.getMessage(), e);
        }
    }

    @Test(description = "This tests direction change of an out parameter to inout")
    public void invokeProcWithDirOutToInOutParams() throws SQLException {
        log.info("Executing:invokeProcWithDirOutToInOutParams");
        String serviceURL = ballerinaURL + "/procedure/call/directionchange?custNo=1&status=outtoinout";

        try {
            //Reading response and status code from response
            GetMethod get = new GetMethod(serviceURL);
            int statuscode = client.executeMethod(get);
            String response = get.getResponseBodyAsString();

            // Asserting the Status code. Expected 200 OK
            assertEquals(statuscode, HttpStatus.SC_OK);
            // Asserting the Response Message.
            assertEquals(response, "1:1:1:1:5");
        } catch (IOException e) {
            log.error("Error while calling the BE server : " + e.getMessage(), e);
        }
    }

    @Test(description = "This tests direction change of an inout parameter to in")
    public void invokeProcWithDirInOutToInParams() throws SQLException {
        log.info("Executing:invokeProcWithDirInOutToInParams");
        String serviceURL = ballerinaURL + "/procedure/call/directionchange?custNo=1&status=inouttoin";

        try {
            //Reading response and status code from response
            GetMethod get = new GetMethod(serviceURL);
            int statuscode = client.executeMethod(get);
            String response = get.getResponseBodyAsString();

            // Asserting the Status code. Expected 200 OK
            assertEquals(statuscode, HttpStatus.SC_OK);
            // Asserting the Response Message.
            assertEquals(response, "1:1:1:1:0");
        } catch (IOException e) {
            log.error("Error while calling the BE server : " + e.getMessage(), e);
        }
    }

    @Test(description = "This tests direction change of an inout parameter to out")
    public void invokeProcWithDirInOutToOutParams() throws SQLException {
        log.info("Executing:invokeProcWithDirInOutToOutParams");
        String serviceURL = ballerinaURL + "/procedure/call/directionchange?custNo=1&status=inouttoout";

        try {
            //Reading response and status code from response
            GetMethod get = new GetMethod(serviceURL);
            int statuscode = client.executeMethod(get);
            String response = get.getResponseBodyAsString();

            // Asserting the Status code. Expected 200 OK
            assertEquals(statuscode, HttpStatus.SC_OK);
            // Asserting the Response Message.
            assertEquals(response, "1:1:1:1:0");
        } catch (IOException e) {
            log.error("Error while calling the BE server : " + e.getMessage(), e);
        }
    }

    @Test(enabled = false, description = "Tests invoking the procedure:less params:param is used in select")
    public void invokeProcWithLessParamsInSelect() throws SQLException {
        log.info("Executing:invokeProcWithLessParamsInSelect");
        String serviceURL = ballerinaURL + "/procedure/call/lessparamter/in?custNo=1&status=select";

        try {
            //Reading response and status code from response
            GetMethod get = new GetMethod(serviceURL);
            int statuscode = client.executeMethod(get);
            String response = get.getResponseBodyAsString();

            // Asserting the Status code. Expected 200 OK
            assertEquals(statuscode, HttpStatus.SC_OK);
            // Asserting the Response Message.
            assertEquals(response, "0:0:0:0:5");
        } catch (IOException e) {
            log.error("Error while calling the BE server : " + e.getMessage(), e);
        }
    }

    @Test(enabled = false, description = "Tests invoking the procedure:less params for in:param is used in operation")
    public void invokeProcWithLessParamsInOperation() throws SQLException {
        log.info("Executing:invokeProcWithLessParamsInOperation");
        String serviceURL = ballerinaURL + "/procedure/call/lessparamter/in?custNo=1&status=op";

        try {
            //Reading response and status code from response
            GetMethod get = new GetMethod(serviceURL);
            int statuscode = client.executeMethod(get);
            String response = get.getResponseBodyAsString();

            // Asserting the Status code. Expected 200 OK
            assertEquals(statuscode, HttpStatus.SC_OK);
            // Asserting the Response Message.
            assertEquals(response, "1:1:1:1:0");
        } catch (IOException e) {
            log.error("Error while calling the BE server : " + e.getMessage(), e);
        }
    }

    @Test(description = "This tests invoking the procedure with less params for out")
    public void invokeProcWithLessParamsOut() throws SQLException {
        log.info("Executing:invokeProcWithLessParamsOut");
        String serviceURL = ballerinaURL + "/procedure/call/lessparamter/out";

        try {
            //Reading response and status code from response
            GetMethod get = new GetMethod(serviceURL);
            int statuscode = client.executeMethod(get);
            String response = get.getResponseBodyAsString();

            // Asserting the Status code. Expected 200 OK
            assertEquals(statuscode, HttpStatus.SC_OK);
            // Asserting the Response Message.
            assertEquals(response, "Error in procedure call. Please retry");
        } catch (IOException e) {
            log.error("Error while calling the BE server : " + e.getMessage(), e);
        }
    }

    @Test(description = "This tests invoking the procedure with less params for inout")
    public void invokeProcWithLessParamsInOut() throws SQLException {
        log.info("Executing:invokeProcWithLessParamsInOut");
        String serviceURL = ballerinaURL + "/procedure/call/lessparamter/inout";

        try {
            //Reading response and status code from response
            GetMethod get = new GetMethod(serviceURL);
            int statuscode = client.executeMethod(get);
            String response = get.getResponseBodyAsString();

            // Asserting the Status code. Expected 200 OK
            assertEquals(statuscode, HttpStatus.SC_OK);
            // Asserting the Response Message.
            assertEquals(response, "Error in procedure call. Please retry");
        } catch (IOException e) {
            log.error("Error while calling the BE server : " + e.getMessage(), e);
        }
    }

    @Test(enabled = false, description = "Tests invoking the procedure with data type mismatch:in:value not changed")
    public void invokeProcWithMismatchTypeForInOne() throws SQLException {
        log.info("Executing:invokeProcWithMismatchTypeForInOne");
        String serviceURL = ballerinaURL + "/procedure/call/mismatchdatatype?custNo=1&status=invaluenotchanged";

        try {
            //Reading response and status code from response
            GetMethod get = new GetMethod(serviceURL);
            int statuscode = client.executeMethod(get);
            String response = get.getResponseBodyAsString();

            // Asserting the Status code. Expected 200 OK
            assertEquals(statuscode, HttpStatus.SC_OK);
            // Asserting the Response Message.
            assertEquals(response, "Error in procedure call. Please retry");
        } catch (IOException e) {
            log.error("Error while calling the BE server : " + e.getMessage(), e);
        }
    }

    @Test(description = "This tests invoking the procedure with mismatching data type for in, with value changed")
    public void invokeProcWithMismatchTypeForInTwo() throws SQLException {
        log.info("Executing:invokeProcWithMismatchTypeForInTwo");
        String serviceURL = ballerinaURL + "/procedure/call/mismatchdatatype?custNo=1&status=invaluechanged";

        try {
            //Reading response and status code from response
            GetMethod get = new GetMethod(serviceURL);
            int statuscode = client.executeMethod(get);
            String response = get.getResponseBodyAsString();

            // Asserting the Status code. Expected 200 OK
            assertEquals(statuscode, HttpStatus.SC_OK);
            // Asserting the Response Message.
            assertEquals(response, "Error in procedure call. Please retry");
        } catch (IOException e) {
            log.error("Error while calling the BE server : " + e.getMessage(), e);
        }
    }

    @Test(description = "This tests calling the procedure with mismatching data type for in, with only value changed")
    public void invokeProcWithMismatchTypeForInThree() throws SQLException {
        log.info("Executing:invokeProcWithMismatchTypeForInThree");
        String serviceURL = ballerinaURL + "/procedure/call/mismatchdatatype?custNo=1&status=inonlyvaluechanged";

        try {
            //Reading response and status code from response
            GetMethod get = new GetMethod(serviceURL);
            int statuscode = client.executeMethod(get);
            String response = get.getResponseBodyAsString();

            // Asserting the Status code. Expected 200 OK
            assertEquals(statuscode, HttpStatus.SC_OK);
            // Asserting the Response Message.
            assertEquals(response, "Error in procedure call. Please retry");
        } catch (IOException e) {
            log.error("Error while calling the BE server : " + e.getMessage(), e);
        }
    }

    @Test(description = "This tests calling the procedure with mismatching data type for out")
    public void invokeProcWithMismatchTypeForOut() throws SQLException {
        log.info("Executing:invokeProcWithMismatchTypeForOut");
        String serviceURL = ballerinaURL + "/procedure/call/mismatchdatatype?custNo=1&status=out";

        try {
            //Reading response and status code from response
            GetMethod get = new GetMethod(serviceURL);
            int statuscode = client.executeMethod(get);
            String response = get.getResponseBodyAsString();

            // Asserting the Status code. Expected 200 OK
            assertEquals(statuscode, HttpStatus.SC_OK);
            // Asserting the Response Message.
            assertEquals(response, "0:1:1:1:5");
        } catch (IOException e) {
            log.error("Error while calling the BE server : " + e.getMessage(), e);
        }
    }

    @Test(enabled = false, description = "Tests calling the procedure with data type mismatch:inout:values not changed")
    public void invokeProcWithMismatchTypeForInOutOne() throws SQLException {
        log.info("Executing:invokeProcWithMismatchTypeForInOutOne");
        String serviceURL = ballerinaURL + "/procedure/call/mismatchdatatype?custNo=1&status=inoutvaluenotchanged";

        try {
            //Reading response and status code from response
            GetMethod get = new GetMethod(serviceURL);
            int statuscode = client.executeMethod(get);
            String response = get.getResponseBodyAsString();

            // Asserting the Status code. Expected 200 OK
            assertEquals(statuscode, HttpStatus.SC_OK);
            // Asserting the Response Message.
            assertEquals(response, "Error in procedure call. Please retry");
        } catch (IOException e) {
            log.error("Error while calling the BE server : " + e.getMessage(), e);
        }
    }

    @Test(description = "This tests calling the procedure with mismatching data type for inout, values changed")
    public void invokeProcWithMismatchTypeForInOutTwo() throws SQLException {
        log.info("Executing:invokeProcWithMismatchTypeForInOutTwo");
        String serviceURL = ballerinaURL + "/procedure/call/mismatchdatatype?custNo=1&status=inoutvaluechanged";

        try {
            //Reading response and status code from response
            GetMethod get = new GetMethod(serviceURL);
            int statuscode = client.executeMethod(get);
            String response = get.getResponseBodyAsString();

            // Asserting the Status code. Expected 200 OK
            assertEquals(statuscode, HttpStatus.SC_OK);
            // Asserting the Response Message.
            assertEquals(response, "Error in procedure call. Please retry");
        } catch (IOException e) {
            log.error("Error while calling the BE server : " + e.getMessage(), e);
        }
    }

    @Test(description = "This tests calling the procedure with mismatching data type for inout, only values changed")
    public void invokeProcWithMismatchTypeForInOutThree() throws SQLException {
        log.info("Executing:invokeProcWithMismatchTypeForInOutThree");
        String serviceURL = ballerinaURL + "/procedure/call/mismatchdatatype?custNo=1&status=inoutonlyvaluechanged";

        try {
            //Reading response and status code from response
            GetMethod get = new GetMethod(serviceURL);
            int statuscode = client.executeMethod(get);
            String response = get.getResponseBodyAsString();

            // Asserting the Status code. Expected 200 OK
            assertEquals(statuscode, HttpStatus.SC_OK);
            // Asserting the Response Message.
            assertEquals(response, "Error in procedure call. Please retry");
        } catch (IOException e) {
            log.error("Error while calling the BE server : " + e.getMessage(), e);
        }
    }

    @Test(description = "This tests calling the procedure to obtain resultset in json")
    public void invokeProcToGetResultInJson() throws SQLException {
        log.info("Executing:invokeProcToGetResultInJson");
        String serviceURL = ballerinaURL + "/procedure/callsucces/resultset?custNo=1";
        String expectedResult = "[{\"customerNumber\":1,\"status\":\"Shipped\",\"location\":\"srilanka\"}" +
                ",{\"customerNumber\":2,\"status\":\"Shipped\",\"location\":\"srilanka\"}" +
                ",{\"customerNumber\":3,\"status\":\"Shipped\",\"location\":\"us\"}" +
                ",{\"customerNumber\":1,\"status\":\"Canceled\",\"location\":\"srilanka\"}" +
                ",{\"customerNumber\":2,\"status\":\"Canceled\",\"location\":\"srilanka\"}" +
                ",{\"customerNumber\":3,\"status\":\"Canceled\",\"location\":\"us\"}" +
                ",{\"customerNumber\":1,\"status\":\"Resolved\",\"location\":\"srilanka\"}" +
                ",{\"customerNumber\":2,\"status\":\"Resolved\",\"location\":\"srilanka\"}" +
                ",{\"customerNumber\":3,\"status\":\"Resolved\",\"location\":\"us\"}" +
                ",{\"customerNumber\":1,\"status\":\"Disputed\",\"location\":\"srilanka\"}" +
                ",{\"customerNumber\":2,\"status\":\"Disputed\",\"location\":\"srilanka\"}" +
                ",{\"customerNumber\":3,\"status\":\"Disputed\",\"location\":\"us\"}]";

        try {
            //Reading response and status code from response
            GetMethod get = new GetMethod(serviceURL);
            int statuscode = client.executeMethod(get);
            String response = get.getResponseBodyAsString();

            // Asserting the Status code. Expected 200 OK
            assertEquals(statuscode, HttpStatus.SC_OK);
            // Asserting the Response Message.
            assertEquals(response, expectedResult);
        } catch (IOException e) {
            log.error("Error while calling the BE server : " + e.getMessage(), e);
        }
    }

    @AfterClass(alwaysRun = true)
    public void afterTest() {
        String dropOrders = "drop table orders";
        String dropProcedure = "drop procedure get_order_by_cust";

        try {
            stmt.executeUpdate(dropOrders);
            stmt.executeUpdate(dropProcedure);
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
