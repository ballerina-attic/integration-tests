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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ballerina.integration.tests.core.beans.InstanceUrls;
import org.ballerina.integration.tests.core.beans.Port;
import org.ballerina.integration.tests.core.commons.DeploymentConfigurationReader;
import org.ballerina.integration.tests.core.commons.DeploymentDataReader;
import org.ballerina.integration.tests.core.utills.ScriptExecutorUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class BallerinaInit {

    private static final Log log = LogFactory.getLog(BallerinaInit.class);
    public static String ballerinaURL = null;
    public static String dbURL = null;

    protected HashMap<String, String> instanceMap;

    /**
     * This method will initialize test environment
     * based on the configuration given at testng.xml
     */
    protected void init(String pattern) throws Exception {
        setURLs(pattern);
    }

    /**
     * Sets the url's as specified in the deployment.json
     *
     * @param patternName
     */
    protected void setURLs(String patternName) {

        HashMap<String, String> instanceMap = null;
        try {
            DeploymentConfigurationReader depconf = DeploymentConfigurationReader.readConfiguration();
            instanceMap = depconf.getDeploymentInstanceMap(patternName);
        } catch (IOException e) {
            log.error("Exception occured while getting the deployment instance map : " + e.getMessage(), e);
        }
        DeploymentDataReader dataJsonReader = new DeploymentDataReader();
        List<InstanceUrls> urlList = dataJsonReader.getInstanceUrlsList();
        for (InstanceUrls url : urlList) {
            if (instanceMap != null) {
                if (url.getLable().equals(instanceMap.get(BallerinaConstants.POD_TAG_NAME))) {
                    ballerinaURL = getHTTPSUrl(BallerinaConstants.BAL_PORT_NAME, url.getHostIP(), url.getPorts(), "");
                }
                if (url.getLable().equals(instanceMap.get(BallerinaConstants.POD_TAG_NAME_MYSQL))) {
                    dbURL = getJDBCUrl(BallerinaConstants.MYSQL_PORT_NAME, url.getHostIP(), url.getPorts(),
                            "/BAL_DB");
                }

            }
        }
    }

    protected String getHTTPSUrl(String protocol, String hostIP, List<Port> ports, String context) {

        String url = "http://" + hostIP + ":";
        for (Port port : ports) {
            if (port.getProtocol().equals(protocol)) {
                url = url + port.getPort() + context;
                break;
            }
        }
        return url;
    }

    /**
     * Generates and return JDBC URL
     *
     * @param protocol
     * @param hostIP
     * @param ports
     * @param databasename
     * @return
     */
    protected String getJDBCUrl(String protocol, String hostIP, List<Port> ports, String databasename) {

        // 192.168.48.44:30306/BAL_DB
        String url = hostIP + ":";
        for (Port port : ports) {
            if (port.getProtocol().equals(protocol)) {
                url = url + port.getPort() + databasename;
                break;
            }
        }
        return url;
    }

    /**
     * Deploy given environment
     *
     * @param pattern
     * @throws IOException
     */
    protected void setTestSuite(String pattern) throws IOException {
        ScriptExecutorUtil.deployScenario(pattern);
    }

    /**
     * Undeploy given environment
     *
     * @param pattern
     * @throws Exception
     */
    protected void unSetTestSuite(String pattern) throws Exception {
        ScriptExecutorUtil.unDeployScenario(pattern);
    }
}
