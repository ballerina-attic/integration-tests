import ballerina.net.jms;
import ballerina.lang.messages;
import ballerina.lang.system;
import ballerina.net.http;
import ballerina.lang.errors;


boolean response;
boolean status;

@http:BasePath {value:"/publish"}
service PublisherService {

    @http:GET {}
    @http:Path {value:"/queue/{type}"}
    resource onQueueMessage (message m, @http:PathParam {value:"type"} string messageType) {

        //Invoke : curl -v http://localhost:9091/publish/queue/text

        message replyMsg = {};

        if (messageType == "text") {
            response = sendTextMessageToQueue();

        } else if (messageType == "map") {
            system:println("Currently not supported");

        } else if (messageType == "object") {
            system:println("Currently not supported");

        } else if (messageType == "byte") {
            system:println("Currently not supported");

        } else {
            system:println("Invalid type");
        }

        messages:setStringPayload(replyMsg, response);
        reply replyMsg;
    }



    @http:GET {}
    @http:Path {value:"/topic/{type}"}
    resource onTopicMessage (message m, @http:PathParam {value:"type"} string messageType) {

        //Invoke : curl -v http://localhost:9091/publish/topic/text

        message replyMsg = {};

        if (messageType == "text") {
            response = sendTextMessageToTopic();

        } else if (messageType == "map") {
            system:println("Currently not supported");

        } else if (messageType == "object") {
            system:println("Currently not supported");

        } else if (messageType == "byte") {
            system:println("Currently not supported");

        } else {
            system:println("Invalid type");
        }

        messages:setStringPayload(replyMsg, response);
        reply replyMsg;
    }


}


function sendTextMessageToQueue () (boolean) {

    map properties = {"factoryInitial":"org.wso2.andes.jndi.PropertiesFileInitialContextFactory",
                         "providerUrl":"jndi.properties",
                         "connectionFactoryJNDIName":"QueueConnectionFactory",
                         "connectionFactoryType":"queue"};

    jms:ClientConnector jmsEP = create jms:ClientConnector(properties);
    message queueMessage = {};
    messages:setStringPayload(queueMessage, "This is a text message to queue");

    try {

        int i = 0;

        while (i < 10) {

            jms:ClientConnector.send(jmsEP, "MyQueue", "TextMessage", queueMessage);
            i = i + 1;

        }

        status = true;
        system:println("Text message sent to queue");

    } catch (errors:Error e) {
        status = false;
        system:println("Error occurred");

    }
    return status;
}




function sendTextMessageToTopic () (boolean) {
    map properties = {"factoryInitial":"org.wso2.andes.jndi.PropertiesFileInitialContextFactory",
                         "providerUrl":"jndi.properties",
                         "connectionFactoryJNDIName":"TopicConnectionFactory",
                         "connectionFactoryType":"topic"};

    jms:ClientConnector jmsEP = create jms:ClientConnector(properties);
    message topicMessage = {};
    messages:setStringPayload(topicMessage, "This is a text message to topic");

    try {
        jms:ClientConnector.send(jmsEP, "MyTopic", "TextMessage", topicMessage);
        status = true;
        system:println("Text message sent to topic");

    } catch (errors:Error e) {
        status = false;
        system:println("Error occurred");
    }
    return status;
}
