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

package org.ballerina.tests.base;

import org.ballerina.integration.tests.core.beans.TesterinaTest;
import org.ballerina.integration.tests.core.utills.TesterinaTestUtils;
import org.ballerinalang.testerina.core.entity.TesterinaFunction;
import org.testng.annotations.DataProvider;

import java.util.ArrayList;

/**
 * Testerina test base
 */
public class TesterinaTestBase {
    static String testFileName;
    public static TesterinaTest bTest;

    public TesterinaTestBase(String testFileName) {
        this.testFileName = testFileName;
    }

    @DataProvider(name = "testFunctionProvider") public static Object[][] testFunctionProvider() {
        bTest = TesterinaTestUtils.loadBalTests(testFileName);
        ArrayList<TesterinaFunction> functions = bTest.getTestFunctions();
        String[][] arr = new String[functions.size()][1];
        int index = 0;
        for (TesterinaFunction function : functions) {
            arr[index][0] = function.getName();
            index++;
        }
        return arr;
    }
}
