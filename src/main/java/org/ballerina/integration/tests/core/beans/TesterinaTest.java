package org.ballerina.integration.tests.core.beans;

import org.ballerinalang.testerina.core.entity.TesterinaFunction;

import java.util.ArrayList;

/**
 * Testerina Test Context, holds a testerina file
 */
public class TesterinaTest {

    private ArrayList<TesterinaFunction> testFunctions;
    private String fileName;

    public TesterinaTest(String filePath) {
        setFileName(filePath);
    }

    public String getFileName() {
        return fileName;
    }

    private void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public ArrayList<TesterinaFunction> getTestFunctions() {
        return testFunctions;
    }

    public void setTestFunctions(ArrayList<TesterinaFunction> beforeTestFunctions) {
        this.testFunctions = beforeTestFunctions;
    }

}
