package org.ballerina.tests.natve.tests;

import org.ballerina.integration.tests.core.utills.EnvironmentUtil;
import org.ballerina.integration.tests.core.utills.TesterinaTestUtils;
import org.ballerina.tests.base.TesterinaTestBase;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Tests for ETCD connector
 */
public class ETCDConnectorTest extends TesterinaTestBase {

    ETCDConnectorTest() {
        super("ETCDConnector_test.bal");
    }

    @BeforeClass public void initializeTests() {
        EnvironmentUtil.setEnv("ETCD_URL", "http://10.100.5.128:2379");
        EnvironmentUtil.setEnv("ETCD_USERNAME", "root");
        EnvironmentUtil.setEnv("ETCD_PASSWORD", "test123");
        EnvironmentUtil.setEnv("API_VERSION", "v2");
    }

    @Test(dataProvider = "testFunctionProvider") public void testExeute(String function) throws Exception {
        TesterinaTestUtils.executeTest(bTest, function);
    }

    @AfterClass public void afterTest() {
        String keys[] = { "ETCD_URL", "ETCD_USERNAME", "ETCD_PASSWORD", "API_VERSION" };
        EnvironmentUtil.removeEnv(keys);
    }
}
