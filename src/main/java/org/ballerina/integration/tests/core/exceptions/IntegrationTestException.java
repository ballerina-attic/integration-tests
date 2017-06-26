package org.ballerina.integration.tests.core.exceptions;

/**
 * Created by yasassri on 6/22/17.
 */
class IntegrationTestException extends Exception {
    /**
     * Constructs a new {@link IntegrationTestException} with the specified detail message.
     *
     * @param message Error Message
     */
    public IntegrationTestException(String message) {
        super(message);
    }
}
