package jms.automation;
import ballerina.lang.messages;
import ballerina.lang.system;
import ballerina.net.jms;
import ballerina.lang.errors;

@jms:JMSSource {
    factoryInitial:"org.wso2.andes.jndi.PropertiesFileInitialContextFactory",
    providerUrl:"/home/dilinig/wso2_products/ballerina/tests/jms/automation/jndi.properties"}
@jms:ConnectionProperty {key:"connectionFactoryType", value:"topic"}
@jms:ConnectionProperty {key:"destination", value:"MyTopic"}
@jms:ConnectionProperty {key:"useReceiver", value:"true"}
@jms:ConnectionProperty {key:"subscriptionDurable", value:"false"}
@jms:ConnectionProperty {key:"durableSubscriberClientID", value:"10"}
@jms:ConnectionProperty {key:"durableSubscriberName", value:"Ballerina"}
@jms:ConnectionProperty {key:"concurrentConsumers", value:"1"}
@jms:ConnectionProperty {key:"connectionFactoryJNDIName", value:"TopicConnectionFactory"}
@jms:ConnectionProperty {key:"sessionAcknowledgement", value:"AUTO_ACKNOWLEDGE"}

service jmsTopicSubscriberService {

    resource onMessage (message m) {

        message replyMsg = {};
        string payload;
        boolean status;

        try {

            string msgType = messages:getProperty(m, "JMS_MESSAGE_TYPE");

            if (msgType == "TextMessage") {
                payload = messages:getStringPayload(m);
                system:println(payload);

            } else {
                system:println("Invalid");

            }

            status = true;

        } catch (errors:Error e) {
            status = false;

        }

        messages:setStringPayload(replyMsg, status);
        reply replyMsg;
    }
}