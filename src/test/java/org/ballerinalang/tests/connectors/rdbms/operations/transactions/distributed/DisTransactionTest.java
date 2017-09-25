package org.ballerinalang.tests.connectors.rdbms.operations.transactions.distributed;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
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

public class DisTransactionTest extends BallerinaBaseTest {

    private static final Log log = LogFactory.getLog(DisTransactionTest.class);
    HttpClient client = new HttpClient();
    Connection conn = null;
    Connection connOther = null;
    Statement stmt = null;
    Statement stmtOther = null;
    //String dbURL = "localhost:3306/baldb";
    //private String ballerinaURL = "http://localhost:9090";
    //String otherDbURL = "localhost:3306/BAL_OTHER_DB";

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
