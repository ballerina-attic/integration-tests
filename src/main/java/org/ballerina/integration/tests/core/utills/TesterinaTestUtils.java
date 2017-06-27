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

package org.ballerina.integration.tests.core.utills;

import org.ballerina.integration.tests.core.FrameworkConstants;
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
     * @param testContext
     * @param functionName
     * @throws Exception
     */
    public static void executeTest(TesterinaContext testContext, String functionName) throws Exception {
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
     * @param filePath
     * @return
     */
    public static TesterinaContext loadBalTests(String filePath) {
        // When executing through maven the directory is set to target, hence extracting the path accordingly
        String path = System.getProperty("user.dir");
        Path programDirPath;
        if (path.endsWith("target")) {
            programDirPath = Paths.get(path.split("target")[0]);
        } else {
            programDirPath = Paths.get(path);
        }

        EnvironmentUtil.setEnv("integration.home", programDirPath.toString());
        // Setting the netty-transports.yml configuration file to load the https configs
        System.setProperty("transports.netty.conf",
                programDirPath.toString() + "/src/main/resources/netty-transports.yml");
        ProgramFile progFile = new BLangProgramLoader().loadServiceProgramFile(programDirPath,
                Paths.get(programDirPath + FrameworkConstants.NATIVE_TESTFILE_LOCATION + filePath));

        TesterinaContext tContext = new TesterinaContext(new ProgramFile[] { progFile });
        return tContext;
    }
}
