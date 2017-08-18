/*
*  Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing,
*  software distributed under the License is distributed on an
*  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*  KIND, either express or implied.  See the License for the
*  specific language governing permissions and limitations
*  under the License.
*/

package org.ballerina.tests.testerina;

import org.ballerinalang.integration.tests.core.utills.EnvironmentUtil;
import org.ballerinalang.integration.tests.core.utills.TesterinaTestUtils;
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

    @Test(dataProvider = "testFunctionProvider") public void testExecute(String function) throws Exception {
        TesterinaTestUtils.executeTest(bTest, function);
    }

    @AfterClass public void afterTest() {
        String keys[] = { "ETCD_URL", "ETCD_USERNAME", "ETCD_PASSWORD", "API_VERSION" };
        EnvironmentUtil.removeEnv(keys);
    }
}
