package org.ballerinalang.tests.connectors.rdbms.operations.transactions.distributed;

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
 * Tests distributed transactions
 * Uses DisTransactionService.bal service and DistributedTransactionTest.bal
 */

public class DisTransactionTest extends BallerinaBaseTest {

    private static final Log log = LogFactory.getLog(DisTransactionTest.class);
    HttpClient client = new HttpClient();
    Connection conn = null;
    Connection connOther = null;
    Statement stmt = null;
    Statement stmtOther = null;
    //String dbURL = "localhost:3306/baldb";
    //String otherDbURL = "localhost:3306/BAL_OTHER_DB";
    //private String ballerinaURL = "http://localhost:9090";

    DisTransactionTest() {
        client = new HttpClient();
        client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
        client.getHttpConnectionManager().getParams().setSoTimeout(8000);
    }

    @BeforeClass(alwaysRun = true)
    public void initializeTables() {
        String createPerson = "CREATE TABLE IF NOT EXISTS Persons (\n" + "    PersonID int NOT NULL AUTO_INCREMENT,\n"
                + "    LastName varchar(255) NOT NULL,\n" + "    FirstName varchar(255),\n"
                + "    Age int,\n" + "    Status varchar(10),\n"
                + "    PRIMARY KEY (PersonID)\n" + ");";
        String createPeople = "CREATE TABLE IF NOT EXISTS People (\n" + "    PersonID int NOT NULL,\n"
                + "    LastName varchar(255) NOT NULL,\n" + "    FirstName varchar(255),\n"
                + "    Age int,\n" + "    Status varchar(10),\n"
                + "    PRIMARY KEY (PersonID)\n" + ");";

        try {
            connOther = DriverManager.getConnection(
                    "jdbc:mysql://" + otherDbURL + "?" + "user=" + TestConstants.MYSQL_OTHER_USERNAME + "&password="
                            + TestConstants.MYSQL_OTHER_PASSWORD);
            conn = DriverManager.getConnection(
                    "jdbc:mysql://" + dbURL + "?" + "user=" + TestConstants.MYSQL_USERNAME + "&password="
                            + TestConstants.MYSQL_PASSWORD);

            stmt = conn.createStatement();
            stmtOther = connOther.createStatement();
            stmt.executeUpdate(createPerson);
            stmtOther.executeUpdate(createPeople);

        } catch (SQLException ex) {
            log.error("SQLException: " + ex.getMessage());
            log.error("SQLState: " + ex.getSQLState());
            log.error("VendorError: " + ex.getErrorCode());
        }
    }

    @Test(description = "This tests successful distributed transaction invocation")
    public void executeDisTransSuccess() throws SQLException, IOException {
        log.info("Executing:executeDisTransSuccess");
        String serviceURL = ballerinaURL + "/distransaction/success";
        String firstNamePerson = null;
        String firstNamePeople = null;

        //Reading response and status code from response
        GetMethod get = new GetMethod(serviceURL);
        int statuscode = client.executeMethod(get);
        String response = get.getResponseBodyAsString();

        //Querying the database to obtain values
        String queryPerson = "select FirstName from Persons where Lastname='Clerk'";
        String queryPeople = "select FirstName from People where Lastname='Clerk'";
        ResultSet resultPerson = stmt.executeQuery(queryPerson);
        ResultSet resultPeople = stmtOther.executeQuery(queryPeople);
        while (resultPerson.next()) {
            firstNamePerson = resultPerson.getString("FirstName");
        }
        while (resultPeople.next()) {
            firstNamePeople = resultPeople.getString("FirstName");
        }
        String expectedPerson = "James";
        String expectedPeople = "James";
        String endQueryPerson = "delete from Persons";
        String endQueryPeople = "delete from People";
        stmt.executeUpdate(endQueryPerson);
        stmtOther.executeUpdate(endQueryPeople);

        // Asserting the Status code. Expected 200 OK
        assertEquals(statuscode, HttpStatus.SC_OK);
        // Asserting the Response Message.
        assertEquals(response, "Inside committed block");
        //Asseting database values
        assertEquals(firstNamePerson, expectedPerson);
        assertEquals(firstNamePeople, expectedPeople);
    }

    @Test(description = "Tests transaction failure and default retry when a sql action fails")
    public void executeDisTransDefaultRetryAtFailure() throws SQLException, IOException {
        log.info("Executing:executeDisTransDefaultRetryAtFailure");
        String serviceURL = ballerinaURL + "/distransaction/fail/retry/default";
        String firstNamePerson = null;
        String firstNamePeople = null;

        //Reading response and status code from response
        GetMethod get = new GetMethod(serviceURL);
        int statuscode = client.executeMethod(get);
        String response = get.getResponseBodyAsString();

        //Querying the database to obtain values
        String queryPerson = "select FirstName from Persons where Lastname='Clerk'";
        String queryPeople = "select FirstName from People where Lastname='Clerk'";
        ResultSet resultPerson = stmt.executeQuery(queryPerson);
        ResultSet resultPeople = stmtOther.executeQuery(queryPeople);
        while (resultPerson.next()) {
            firstNamePerson = resultPerson.getString("FirstName");
        }
        while (resultPeople.next()) {
            firstNamePeople = resultPeople.getString("FirstName");
        }
        String endQueryPerson = "delete from Persons";
        String endQueryPeople = "delete from People";
        stmt.executeUpdate(endQueryPerson);
        stmtOther.executeUpdate(endQueryPeople);

        // Asserting the Status code. Expected 200 OK
        assertEquals(statuscode, HttpStatus.SC_OK);
        // Asserting the Response Message.
        assertEquals(response, "Error in transaction. Please retry. Retried: 3");
        //Asseting database values
        assertEquals(firstNamePerson, null);
        assertEquals(firstNamePeople, null);
    }

    @Test(description = "Tests transaction failure and custom retry when a sql action fails")
    public void executeDisTransCustomRetryAtFailure() throws SQLException, IOException {
        log.info("Executing:executeDisTransCustomRetryAtFailure");
        String serviceURL = ballerinaURL + "/distransaction/fail/retry/custom";
        String firstNamePerson = null;
        String firstNamePeople = null;

        //Reading response and status code from response
        GetMethod get = new GetMethod(serviceURL);
        int statuscode = client.executeMethod(get);
        String response = get.getResponseBodyAsString();

        //Querying the database to obtain values
        String queryPerson = "select FirstName from Persons where Lastname='Clerk'";
        String queryPeople = "select FirstName from People where Lastname='Clerk'";
        ResultSet resultPerson = stmt.executeQuery(queryPerson);
        ResultSet resultPeople = stmtOther.executeQuery(queryPeople);
        while (resultPerson.next()) {
            firstNamePerson = resultPerson.getString("FirstName");
        }
        while (resultPeople.next()) {
            firstNamePeople = resultPeople.getString("FirstName");
        }
        String endQueryPerson = "delete from Persons";
        String endQueryPeople = "delete from People";
        stmt.executeUpdate(endQueryPerson);
        stmtOther.executeUpdate(endQueryPeople);

        // Asserting the Status code. Expected 200 OK
        assertEquals(statuscode, HttpStatus.SC_OK);
        // Asserting the Response Message.
        assertEquals(response, "Error in transaction. Please retry. Retried: 4");
        //Asseting database values
        assertEquals(firstNamePerson, null);
        assertEquals(firstNamePeople, null);
    }

    @Test(description = "Tests transaction failure with force aborting sql action of one database")
    public void executeDisTransForceAbort() throws SQLException, IOException {
        log.info("Executing:executeDisTransForceAbort");
        String serviceURL = ballerinaURL + "/distransaction/fail/abort";
        String firstNamePerson = null;
        String firstNamePeople = null;

        //Reading response and status code from response
        GetMethod get = new GetMethod(serviceURL);
        int statuscode = client.executeMethod(get);
        String response = get.getResponseBodyAsString();

        //Querying the database to obtain values
        String queryPerson = "select FirstName from Persons where Lastname='Clerk'";
        String queryPeople = "select FirstName from People where Lastname='Clerk'";
        ResultSet resultPerson = stmt.executeQuery(queryPerson);
        ResultSet resultPeople = stmtOther.executeQuery(queryPeople);
        while (resultPerson.next()) {
            firstNamePerson = resultPerson.getString("FirstName");
        }
        while (resultPeople.next()) {
            firstNamePeople = resultPeople.getString("FirstName");
        }
        String endQueryPerson = "delete from Persons";
        String endQueryPeople = "delete from People";
        stmt.executeUpdate(endQueryPerson);
        stmtOther.executeUpdate(endQueryPeople);

        // Asserting the Status code. Expected 200 OK
        assertEquals(statuscode, HttpStatus.SC_OK);
        // Asserting the Response Message.
        assertEquals(response, "Inside aborted block. Retried: 0");
        //Asseting database values
        assertEquals(firstNamePerson, null);
        assertEquals(firstNamePeople, null);
    }

    @Test(description = "Tests transaction failure with explicit throw")
    public void executeDisTransForceThrow() throws SQLException, IOException {
        log.info("Executing:executeDisTransForceThrow");
        String serviceURL = ballerinaURL + "/distransaction/fail/throw";
        String firstNamePerson = null;
        String firstNamePeople = null;

        //Reading response and status code from response
        GetMethod get = new GetMethod(serviceURL);
        int statuscode = client.executeMethod(get);
        String response = get.getResponseBodyAsString();

        //Querying the database to obtain values
        String queryPerson = "select FirstName from Persons where Lastname='Clerk'";
        String queryPeople = "select FirstName from People where Lastname='Clerk'";
        ResultSet resultPerson = stmt.executeQuery(queryPerson);
        ResultSet resultPeople = stmtOther.executeQuery(queryPeople);
        while (resultPerson.next()) {
            firstNamePerson = resultPerson.getString("FirstName");
        }
        while (resultPeople.next()) {
            firstNamePeople = resultPeople.getString("FirstName");
        }
        String endQueryPerson = "delete from Persons";
        String endQueryPeople = "delete from People";
        stmt.executeUpdate(endQueryPerson);
        stmtOther.executeUpdate(endQueryPeople);

        // Asserting the Status code. Expected 200 OK
        assertEquals(statuscode, HttpStatus.SC_OK);
        // Asserting the Response Message.
        assertEquals(response, "Thrown out from transaction. Retried: 4");
        //Asseting database values
        assertEquals(firstNamePerson, null);
        assertEquals(firstNamePeople, null);
    }

    @Test(description = "This tests successful distributed multiple transaction invocation")
    public void executeDisMultipleTransSuccess() throws SQLException, IOException {
        log.info("Executing:executeDisMultipleTransSuccess");
        String serviceURL = ballerinaURL + "/distransaction/success/multiple";
        String firstNamePerson = null;
        String firstNamePeople = null;

        //Reading response and status code from response
        GetMethod get = new GetMethod(serviceURL);
        int statuscode = client.executeMethod(get);
        String response = get.getResponseBodyAsString();

        //Querying the database to obtain values
        String queryPerson = "select FirstName from Persons where Lastname='Clerk'";
        String queryPeople = "select FirstName from People where Lastname='Clerk'";
        ResultSet resultPerson = stmt.executeQuery(queryPerson);
        ResultSet resultPeople = stmtOther.executeQuery(queryPeople);
        while (resultPerson.next()) {
            firstNamePerson = resultPerson.getString("FirstName");
        }
        while (resultPeople.next()) {
            firstNamePeople = resultPeople.getString("FirstName");
        }
        String expectedPerson = "James";
        String expectedPeople = "James";
        String endQueryPerson = "delete from Persons";
        String endQueryPeople = "delete from People";
        stmt.executeUpdate(endQueryPerson);
        stmtOther.executeUpdate(endQueryPeople);

        // Asserting the Status code. Expected 200 OK
        assertEquals(statuscode, HttpStatus.SC_OK);
        // Asserting the Response Message.
        assertEquals(response, "Before trx: committed trx 1: committed trx 2");
        //Asseting database values
        assertEquals(firstNamePerson, expectedPerson);
        assertEquals(firstNamePeople, expectedPeople);
    }

    @Test(description = "Tests single transaction failure in multiple transaction blocks with custom retry")
    public void executeDisMulTransFailRetryOne() throws SQLException, IOException {
        log.info("Executing:executeDisMulTransFailRetryOne");
        String serviceURL = ballerinaURL + "/distransaction/failure/retry/one";
        String firstNamePerson = null;
        String firstNamePeople = null;

        //Reading response and status code from response
        GetMethod get = new GetMethod(serviceURL);
        int statuscode = client.executeMethod(get);
        String response = get.getResponseBodyAsString();

        //Querying the database to obtain values
        String queryPerson = "select FirstName from Persons where Lastname='Clerk'";
        String queryPeople = "select FirstName from People where Lastname='Clerk'";
        ResultSet resultPerson = stmt.executeQuery(queryPerson);
        ResultSet resultPeople = stmtOther.executeQuery(queryPeople);
        while (resultPerson.next()) {
            firstNamePerson = resultPerson.getString("FirstName");
        }
        while (resultPeople.next()) {
            firstNamePeople = resultPeople.getString("FirstName");
        }
        String endQueryPerson = "delete from Persons";
        String endQueryPeople = "delete from People";
        stmt.executeUpdate(endQueryPerson);
        stmtOther.executeUpdate(endQueryPeople);

        // Asserting the Status code. Expected 200 OK
        assertEquals(statuscode, HttpStatus.SC_OK);
        // Asserting the Response Message.
        assertEquals(response, "Error in transaction. Please retry. Retried: 50");
        //Asseting database values
        assertEquals(firstNamePerson, null);
        assertEquals(firstNamePeople, null);
    }

    @Test(description = "Tests child transaction failure in nested with retry")
    public void executeDisNestedTransChildFailureRetry() throws SQLException, IOException {
        log.info("Executing:executeDisNestedTransChildFailureRetry");
        String serviceURL = ballerinaURL + "/distransaction/nested/failure/retry/child";
        String firstNamePerson = null;
        String firstNamePeople = null;

        //Reading response and status code from response
        GetMethod get = new GetMethod(serviceURL);
        int statuscode = client.executeMethod(get);
        String response = get.getResponseBodyAsString();

        //Querying the database to obtain values
        String queryPerson = "select FirstName from Persons where Lastname='Clerk'";
        String queryPeople = "select FirstName from People where Lastname='Clerk'";
        ResultSet resultPerson = stmt.executeQuery(queryPerson);
        ResultSet resultPeople = stmtOther.executeQuery(queryPeople);
        while (resultPerson.next()) {
            firstNamePerson = resultPerson.getString("FirstName");
        }
        while (resultPeople.next()) {
            firstNamePeople = resultPeople.getString("FirstName");
        }
        String endQueryPerson = "delete from Persons";
        String endQueryPeople = "delete from People";
        stmt.executeUpdate(endQueryPerson);
        stmtOther.executeUpdate(endQueryPeople);

        // Asserting the Status code. Expected 200 OK
        assertEquals(statuscode, HttpStatus.SC_OK);
        // Asserting the Response Message.
        assertEquals(response, "Error in transaction. Please retry. Child Retried: 35. Parent Retried: 0");
        //Asseting database values
        assertEquals(firstNamePerson, null);
        assertEquals(firstNamePeople, null);
    }

    @Test(description = "Tests parent transaction failure in nested with retry")
    public void executeDisNestedTransParentFailureRetry() throws SQLException, IOException {
        log.info("Executing:executeDisNestedTransParentFailureRetry");
        String serviceURL = ballerinaURL + "/distransaction/nested/failure/retry/parent";
        String firstNamePerson = null;
        String firstNamePeople = null;

        //Reading response and status code from response
        GetMethod get = new GetMethod(serviceURL);
        int statuscode = client.executeMethod(get);
        String response = get.getResponseBodyAsString();

        //Querying the database to obtain values
        String queryPerson = "select FirstName from Persons where Lastname='Clerk'";
        String queryPeople = "select FirstName from People where Lastname='Clerk'";
        ResultSet resultPerson = stmt.executeQuery(queryPerson);
        ResultSet resultPeople = stmtOther.executeQuery(queryPeople);
        while (resultPerson.next()) {
            firstNamePerson = resultPerson.getString("FirstName");
        }
        while (resultPeople.next()) {
            firstNamePeople = resultPeople.getString("FirstName");
        }
        String endQueryPerson = "delete from Persons";
        String endQueryPeople = "delete from People";
        stmt.executeUpdate(endQueryPerson);
        stmtOther.executeUpdate(endQueryPeople);

        // Asserting the Status code. Expected 200 OK
        assertEquals(statuscode, HttpStatus.SC_OK);
        // Asserting the Response Message.
        assertEquals(response, "Error in transaction. Please retry. Child Retried: 0. Parent Retried: 50");
        //Asseting database values
        assertEquals(firstNamePerson, null);
        assertEquals(firstNamePeople, null);
    }


    @Test(description = "Tests successful transaction for multiple data")
    public void executeDisMultipleData() throws SQLException, IOException {
        log.info("Executing:executeDisMultipleData");
        String serviceURL = ballerinaURL + "/distransaction/general/single/success";
        String countPerson = null;
        String countPeople = null;
        String payload = "{  \n" +
                "   \"people\":[  \n" +
                "      {  \n" +
                "    \t \"id\":1,\n" +
                "         \"lastname\":\"SloaneD\",\n" +
                "         \"firstname\":\"Emma\",\n" +
                "         \"age\":25,\n" +
                "\t \"status\":\"active\"\t\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\":2,\n" +
                "    \t\"lastname\":\"SloaneB\",\n" +
                "         \"firstname\":\"Harry\",\n" +
                "         \"age\":15,\n" +
                "         \"status\":\"active\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\":3,\n" +
                "    \t\"lastname\":\"SloaneA\",\n" +
                "         \"firstname\":\"Kelly\",\n" +
                "         \"age\":25,\n" +
                "         \"status\":\"active\"\n" +
                "      }\n" +
                "    ]\n" +
                "}";

        //Reading response and status code from response
        StringRequestEntity requestEntity = new StringRequestEntity(payload, "application/json", "UTF-8");
        PostMethod post = new PostMethod(serviceURL);
        post.setRequestEntity(requestEntity);
        int statuscode = client.executeMethod(post);
        String response = post.getResponseBodyAsString();

        //Querying the database to obtain values
        String queryPerson = "select count(PersonID) as Count from Persons";
        String queryPeople = "select count(PersonID) as Count from People";
        ResultSet resultPerson = stmt.executeQuery(queryPerson);
        ResultSet resultPeople = stmtOther.executeQuery(queryPeople);
        while (resultPerson.next()) {
            countPerson = String.valueOf(resultPerson.getInt("Count"));
        }
        while (resultPeople.next()) {
            countPeople = String.valueOf(resultPeople.getInt("Count"));
        }
        String endQueryPerson = "delete from Persons";
        String endQueryPeople = "delete from People";
        stmt.executeUpdate(endQueryPerson);
        stmtOther.executeUpdate(endQueryPeople);

        // Asserting the Status code. Expected 200 OK
        assertEquals(statuscode, HttpStatus.SC_OK);
        // Asserting the Response Message.
        assertEquals(response, "Inside committed block. Retried: 0");
        //Asseting database values
        assertEquals(countPerson, "3");
        assertEquals(countPeople, "3");
    }

    @Test(description = "Tests transaction failure for multiple data with retry")
    public void executeDisMultipleDataRetry() throws SQLException, IOException {
        log.info("Executing:executeDisMultipleDataRetry");
        String serviceURL = ballerinaURL + "/distransaction/general/single/retry";
        String countPerson = null;
        String countPeople = null;
        String payload = "{  \n" +
                "   \"people\":[  \n" +
                "      {  \n" +
                "    \t \"id\":1,\n" +
                "         \"lastname\":\"SloaneD\",\n" +
                "         \"firstname\":\"Emma\",\n" +
                "         \"age\":25,\n" +
                "\t \"status\":\"active\"\t\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\":2,\n" +
                "    \t\"lastname\":\"SloaneB\",\n" +
                "         \"firstname\":\"Harry\",\n" +
                "         \"age\":15,\n" +
                "         \"status\":\"active\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\":3,\n" +
                "    \t\"lastname\":\"SloaneA\",\n" +
                "         \"firstname\":\"Kelly\",\n" +
                "         \"age\":25,\n" +
                "         \"status\":\"active\"\n" +
                "      }\n" +
                "    ]\n" +
                "}";

        //Reading response and status code from response
        StringRequestEntity requestEntity = new StringRequestEntity(payload, "application/json", "UTF-8");
        PostMethod post = new PostMethod(serviceURL);
        post.setRequestEntity(requestEntity);
        int statuscode = client.executeMethod(post);
        String response = post.getResponseBodyAsString();

        //Querying the database to obtain values
        String queryPerson = "select count(PersonID) as Count from Persons";
        String queryPeople = "select count(PersonID) as Count from People";
        ResultSet resultPerson = stmt.executeQuery(queryPerson);
        ResultSet resultPeople = stmtOther.executeQuery(queryPeople);
        while (resultPerson.next()) {
            countPerson = String.valueOf(resultPerson.getInt("Count"));
        }
        while (resultPeople.next()) {
            countPeople = String.valueOf(resultPeople.getInt("Count"));
        }
        String endQueryPerson = "delete from Persons";
        String endQueryPeople = "delete from People";
        stmt.executeUpdate(endQueryPerson);
        stmtOther.executeUpdate(endQueryPeople);

        // Asserting the Status code. Expected 200 OK
        assertEquals(statuscode, HttpStatus.SC_OK);
        // Asserting the Response Message.
        assertEquals(response, "Error in transaction. Please retry. Retried: 4");
        //Asseting database values
        assertEquals(countPerson, "0");
        assertEquals(countPeople, "0");
    }

    @Test(description = "Tests transaction failure for multiple data with force abort")
    public void executeDisMultipleDataForceAbort() throws SQLException, IOException {
        log.info("Executing:executeDisMultipleDataForceAbort");
        String serviceURL = ballerinaURL + "/distransaction/general/single/abort";
        String countPerson = null;
        String countPeople = null;
        String payload = "{  \n" +
                "   \"people\":[  \n" +
                "      {  \n" +
                "    \t \"id\":1,\n" +
                "         \"lastname\":\"SloaneD\",\n" +
                "         \"firstname\":\"Emma\",\n" +
                "         \"age\":25,\n" +
                "\t \"status\":\"active\"\t\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\":2,\n" +
                "    \t\"lastname\":\"SloaneB\",\n" +
                "         \"firstname\":\"Harry\",\n" +
                "         \"age\":15,\n" +
                "         \"status\":\"active\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\":3,\n" +
                "    \t\"lastname\":\"SloaneA\",\n" +
                "         \"firstname\":\"Kelly\",\n" +
                "         \"age\":25,\n" +
                "         \"status\":\"inactive\"\n" +
                "      }\n" +
                "    ]\n" +
                "}";

        //Reading response and status code from response
        StringRequestEntity requestEntity = new StringRequestEntity(payload, "application/json", "UTF-8");
        PostMethod post = new PostMethod(serviceURL);
        post.setRequestEntity(requestEntity);
        int statuscode = client.executeMethod(post);
        String response = post.getResponseBodyAsString();

        //Querying the database to obtain values
        String queryPerson = "select count(PersonID) as Count from Persons";
        String queryPeople = "select count(PersonID) as Count from People";
        ResultSet resultPerson = stmt.executeQuery(queryPerson);
        ResultSet resultPeople = stmtOther.executeQuery(queryPeople);
        while (resultPerson.next()) {
            countPerson = String.valueOf(resultPerson.getInt("Count"));
        }
        while (resultPeople.next()) {
            countPeople = String.valueOf(resultPeople.getInt("Count"));
        }
        String endQueryPerson = "delete from Persons";
        String endQueryPeople = "delete from People";
        stmt.executeUpdate(endQueryPerson);
        stmtOther.executeUpdate(endQueryPeople);

        // Asserting the Status code. Expected 200 OK
        assertEquals(statuscode, HttpStatus.SC_OK);
        // Asserting the Response Message.
        assertEquals(response, "Inside aborted block. Retried: 0");
        //Asseting database values
        assertEquals(countPerson, "0");
        assertEquals(countPeople, "0");
    }

    @AfterClass(alwaysRun = true)
    public void afterTest() {
        String dropPersons = "drop table Persons";
        String dropPeople = "drop table People";

        try {
            if (stmt != null & stmtOther != null) {
                stmt.executeUpdate(dropPersons);
                stmtOther.executeUpdate(dropPeople);
            }
        } catch (SQLException e) {
            log.error("SQLException: " + e.getMessage());
            log.error("SQLState: " + e.getSQLState());
            log.error("VendorError: " + e.getErrorCode());
        } finally {
            if (conn != null & connOther != null) {
                try {
                    conn.close();
                    connOther.close();
                } catch (SQLException sqlEx) {
                }
            }
        }
    }

}
