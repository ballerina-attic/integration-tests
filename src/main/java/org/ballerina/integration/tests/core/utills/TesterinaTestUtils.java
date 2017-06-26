package org.ballerina.integration.tests.core.utills;

import org.ballerina.integration.tests.core.FrameworkConstants;
import org.ballerina.integration.tests.core.beans.TesterinaTest;
import org.ballerina.integration.tests.core.exceptions.IntegrationTestException;
import org.ballerinalang.BLangProgramLoader;
import org.ballerinalang.natives.connectors.BallerinaConnectorManager;
import org.ballerinalang.services.MessageProcessor;
import org.ballerinalang.testerina.core.entity.TesterinaContext;
import org.ballerinalang.testerina.core.entity.TesterinaFunction;
import org.ballerinalang.util.codegen.ProgramFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Util methods to load and run testerina test files
 */

public class TesterinaTestUtils {

    /**
     *
     * @param testContext
     * @param functionName
     * @throws Exception
     */
    public static void executeTest(TesterinaTest testContext, String functionName) throws Exception {
        BallerinaConnectorManager.getInstance().initialize(new MessageProcessor());

        ArrayList<TesterinaFunction> testFunctions = testContext.getTestFunctions();
        for (TesterinaFunction function : testFunctions) {
            if (function.getName().equals(functionName)) {
                function.invoke();
                return;
            }
        }
        throw new IntegrationTestException("The test function : " + functionName + " not found!");
    }

    /**
     *
     * @param filePath
     * @return
     */
    public static TesterinaTest loadBalTests(String filePath) {
        // When executing through maven the directory is set to target, hence extracting the path
        Path programDirPath = Paths.get(System.getProperty("user.dir").split("target")[0]);
        EnvironmentUtil.setEnv("integration.home", programDirPath.toString());
        // Setting the netty-transports.yml configuration file to load the https configs
        System.setProperty("transports.netty.conf",
                programDirPath.toString() + "/src/main/resources/netty-transports.yml");
        TesterinaTest ballerinaTest = new TesterinaTest(filePath);
        ProgramFile progFile = new BLangProgramLoader().loadServiceProgramFile(programDirPath,
                Paths.get(programDirPath + FrameworkConstants.NATIVE_TESTFILE_LOCATION + filePath));

        TesterinaContext tContext = new TesterinaContext(new ProgramFile[] { progFile });
        ballerinaTest.setTestFunctions(tContext.getTestFunctions());
        return ballerinaTest;
    }
}
