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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Class to set and Unset Environment variables
 */
public class EnvironmentUtil {

    private static final Log log = LogFactory.getLog(EnvironmentUtil.class);

    /**
     * Get Modifiable environment variable map
     * @return
     */
    private static Map<String, String> getModifiableEnvironmentMap() {
        Map<String, String> writableEnv = null;
        try {
            Map<String, String> env = System.getenv();
            Class<?> cl = env.getClass();
            Field field = cl.getDeclaredField("m");
            field.setAccessible(true);
            writableEnv = (Map<String, String>) field.get(env);
        } catch (IllegalAccessException e) {
            log.error("Error occurred while getting Environment variable map : " + e.getMessage(), e);
        } catch (NoSuchFieldException e) {
            log.error("Error occurred while getting Environment variable map : " + e.getMessage(), e);
        }
        return writableEnv;
    }

    /**
     * Method to remove environment variables
     * @param keys
     */
    public static void removeEnv(String[] keys) {

        Map<String, String> modifiableEnv = getModifiableEnvironmentMap();

        for (String k : keys) {
            modifiableEnv.remove(k);
        }
    }

    /**
     * Mathod to set environment variables
     * @param key
     * @param value
     */
    public static void setEnv(String key, String value) {
        getModifiableEnvironmentMap().put(key, value);
    }
}
