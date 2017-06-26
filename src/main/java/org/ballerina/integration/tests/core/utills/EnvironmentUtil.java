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

    public static void setEnv(String key, String value) {
        getModifiableEnvironmentMap().put(key, value);
    }
}
