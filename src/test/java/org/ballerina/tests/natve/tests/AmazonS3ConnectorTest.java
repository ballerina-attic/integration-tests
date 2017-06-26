package org.ballerina.tests.natve.tests;

import org.ballerina.integration.tests.core.utills.EnvironmentUtil;
import org.ballerina.integration.tests.core.utills.TesterinaTestUtils;
import org.ballerina.tests.base.TesterinaTestBase;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class AmazonS3ConnectorTest extends TesterinaTestBase {

    AmazonS3ConnectorTest() {
        super("AmazonS3_test.bal");
    }

    @BeforeClass public void initializeTests() {
        //        EnvironmentUtil.setEnv("ACCESS_KEY", "XXXX");
        //        EnvironmentUtil.setEnv("SECRET_ACCESS_KEY", "XXXXX");
        //        EnvironmentUtil.setEnv("REGION", "XXXX");
    }

    @Test(dataProvider = "testFunctionProvider") public void testExeute(String function) throws Exception {
        TesterinaTestUtils.executeTest(bTest, function);
    }

    @AfterClass public void afterTest() {
        String keys[] = { "ACCESS_KEY", "SECRET_ACCESS_KEY", "REGION" };
        EnvironmentUtil.removeEnv(keys);
    }
}
