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
package org.wso2.ballerina.deployment.Base;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.ballerina.deployment.beans.InstanceUrls;
import org.wso2.ballerina.deployment.beans.Port;
import org.wso2.ballerina.deployment.commons.DeploymentConfigurationReader;
import org.wso2.ballerina.deployment.commons.DeploymentDataReader;
import org.wso2.ballerina.deployment.utills.ScriptExecutorUtil;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class BallerinaInit {

    private static final Log log = LogFactory.getLog(BallerinaInit.class);
    public static String ballerinaURL;

    protected HashMap<String, String> instanceMap;

    /**
     * This method will initialize test environment
     * based on the configuration given at testng.xml
     */
    protected void init(String pattern) throws Exception {
        setURLs(pattern);
    }

    //set the url's as specified in the deployment.json
    protected void setURLs(String patternName) {

        HashMap<String, String> instanceMap = null;
        try {
            DeploymentConfigurationReader depconf = DeploymentConfigurationReader.readConfiguration();
            instanceMap = depconf.getDeploymentInstanceMap(patternName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        DeploymentDataReader dataJsonReader = new DeploymentDataReader();
        List<InstanceUrls> urlList = dataJsonReader.getInstanceUrlsList();
        for (InstanceUrls url : urlList) {
            if (instanceMap != null) {
                if (url.getLable().equals(instanceMap.get(BallerinaConstants.POD_TAG_NAME))) {
                        ballerinaURL = getHTTPSUrl("servlet-http", url.getHostIP(), url.getPorts(), "");
                }
            }
        }
    }

    protected String getHTTPSUrl(String protocol, String hostIP, List<Port> ports, String context) {

        String Url = "http://" + hostIP + ":";
        for (Port port : ports) {
            if (port.getProtocol().equals(protocol)) {
                Url = Url + port.getPort() + context;
                break;
            }
        }
        return Url;
    }

    private boolean isURLRemapEnabled() {
        log.info("URL Remap Enabled is set to : " + System.getenv(BallerinaConstants.ENABLE_URL_REMAP));
        return Boolean.parseBoolean((System.getenv(BallerinaConstants.ENABLE_URL_REMAP)));
    }

    private String getRemappedURL(String localIP) {

        String remappedURL = System.getenv("IP_" +localIP.replace(".","_"));

        if (remappedURL.equals("") | remappedURL == null) {
            log.info("No remap value found for the Local IP : " + localIP);
        }
        return remappedURL;
    }

    //deploy environment
    protected void setTestSuite(String pattern) throws IOException {
        ScriptExecutorUtil.deployScenario(pattern);
    }

    //Undeploy environment
    protected void unSetTestSuite(String pattern) throws Exception {
        ScriptExecutorUtil.unDeployScenario(pattern);
    }
}