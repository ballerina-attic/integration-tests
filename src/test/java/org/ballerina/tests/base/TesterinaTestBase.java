package org.ballerina.tests.base;

import org.ballerina.integration.tests.core.beans.TesterinaTest;
import org.ballerina.integration.tests.core.utills.TesterinaTestUtils;
import org.ballerinalang.testerina.core.entity.TesterinaFunction;
import org.testng.annotations.DataProvider;

import java.util.ArrayList;

/**
 * Created by yasassri on 6/23/17.
 */
public class TesterinaTestBase {
    String testFileName;
    public static TesterinaTest bTest;

    public TesterinaTestBase(String testFileName) {
        this.testFileName = testFileName;
    }

    @DataProvider(name = "testFunctionProvider") public static Object[][] testFunctionProvider() {
        bTest = TesterinaTestUtils.loadBalTests("AmazonS3_test.bal");
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
