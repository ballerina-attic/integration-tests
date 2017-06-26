import org.wso2.ballerina.connectors.etcd;
import ballerina.lang.messages;
import ballerina.lang.system;
import ballerina.lang.strings;
import ballerina.test;


string etcdURL = system:getEnv("ETCD_URL");
string username = system:getEnv("ETCD_USERNAME");
string password = system:getEnv("ETCD_PASSWORD");
string apiVersion = system:getEnv("API_VERSION");
message etcdResponse = {};
json payload;

function init () (etcd:ClientConnector etcdConnector){
    etcdConnector = create etcd:ClientConnector(etcdURL, username, password, apiVersion);
    return;
}

function testSetKeyValue () {
    string arg1 = "key4";
    string arg2 = "testValueForKey4";
    etcd:ClientConnector connectorInstance = init();
    etcdResponse = etcd:ClientConnector.setKeyValue (connectorInstance, arg1, arg2);
    system:println(messages:getStringPayload(etcdResponse));
    payload = messages:getJsonPayload(etcdResponse);
    string returnedKey = (string)payload.node.key;
    string returnedValue = (string)payload.node.value;
    test:assertTrue(strings:equalsIgnoreCase("/" + arg1, returnedKey) && strings:equalsIgnoreCase(arg2, returnedValue)
                    , "testSetKeyValue passed");
}


function testGetKeyValue () {
    string arg1 = "key4";
    string arg2 = "testValueForKey4";
    string arg3 = "get";
    etcd:ClientConnector connectorInstance = init();
    etcdResponse = etcd:ClientConnector.getValue (connectorInstance, arg1);
    system:println(messages:getStringPayload(etcdResponse));
    payload = messages:getJsonPayload(etcdResponse);
    string returnedKey = (string)payload.node.key;
    string returnedValue = (string)payload.node.value;
    test:assertTrue(strings:equalsIgnoreCase("/" + arg1, returnedKey) && strings:equalsIgnoreCase(arg2, returnedValue)
                    , "testGetKeyValue passed");
}

function testUpdateKeyValue () {
    string arg1 = "key4";
    string arg2 = "testUpdatedValueForKey4";
    string arg3 = "testValueForKey4";
    string arg4 = "set";
    etcd:ClientConnector connectorInstance = init();
    etcdResponse = etcd:ClientConnector.updateValue (connectorInstance, arg1, arg2);
    system:println(messages:getStringPayload(etcdResponse));
    payload = messages:getJsonPayload(etcdResponse);
    string returnedKey = (string)payload.node.key;
    string returnedValue = (string)payload.node.value;
    string previousValue = (string)payload.prevNode.value;
    test:assertTrue(strings:equalsIgnoreCase("/" + arg1, returnedKey) && strings:equalsIgnoreCase(arg2, returnedValue)
                    && strings:equalsIgnoreCase(arg3, previousValue)
                    , "testUpdateKeyValue passed");
}

function testDeleteKeyValue () {
    string arg1 = "key4";
    string arg2 = "delete";
    etcd:ClientConnector connectorInstance = init();
    etcdResponse = etcd:ClientConnector.deleteKey (connectorInstance, arg1);
    system:println(messages:getStringPayload(etcdResponse));
    payload = messages:getJsonPayload(etcdResponse);
    string returnedKey = (string)payload.node.key;
    test:assertTrue(strings:equalsIgnoreCase("/" + arg1, returnedKey)
                    , "testDeleteKeyValue passed");
}

function testCreateDirectory () {
    string arg1 = "dir6";
    etcd:ClientConnector connectorInstance = init();
    etcdResponse = etcd:ClientConnector.createDir (connectorInstance, arg1);
    system:println(messages:getStringPayload(etcdResponse));
    payload = messages:getJsonPayload(etcdResponse);
    string returnedKey = (string)payload.node.key;
    test:assertTrue(strings:equalsIgnoreCase("/" + arg1, returnedKey)
                    , "testCreateDirectory passed");
}

function testListDirectoryRecursiveFalse () {
    string arg1 = "/dir2";
    string arg2 = "recursive=false";
    etcd:ClientConnector connectorInstance = init();
    etcdResponse = etcd:ClientConnector.listDir (connectorInstance, arg1, arg2);
    system:println(messages:getStringPayload(etcdResponse));
    payload = messages:getJsonPayload(etcdResponse);
    string returnedDir = (string)payload.node.key;
    test:assertTrue(strings:equalsIgnoreCase(arg1, returnedDir)
                    , "testListDirectoryRecursiveFalse passed");
}

function testListDirectoryRecursiveTrue () {
    string arg1 = "/dir2";
    string arg2 = "recursive=true";
    etcd:ClientConnector connectorInstance = init();
    etcdResponse = etcd:ClientConnector.listDir (connectorInstance, arg1, arg2);
    system:println(messages:getStringPayload(etcdResponse));
    payload = messages:getJsonPayload(etcdResponse);
    string returnedDir = (string)payload.node.key;
    test:assertTrue(strings:equalsIgnoreCase(arg1, returnedDir)
                    , "testListDirectoryRecursiveTrue passed");
}

function testDeleteDirectoryRecursiveFalse () {
    string arg1 = "/dir4";
    string arg2 = "recursive=false";
    etcd:ClientConnector connectorInstance = init();
    etcdResponse = etcd:ClientConnector.deleteDir (connectorInstance, arg1, arg2);
    system:println(messages:getStringPayload(etcdResponse));
    payload = messages:getJsonPayload(etcdResponse);
    string returnedDir = (string)payload.node.key;
    test:assertTrue(strings:equalsIgnoreCase(arg1, returnedDir)
                    , "testDeleteDirectoryRecursiveFalse passed");
}

function testDeleteDirectoryRecursiveTrue () {
    string arg1 = "/dir5";
    string arg2 = "recursive=true";
    etcd:ClientConnector connectorInstance = init();
    etcdResponse = etcd:ClientConnector.deleteDir (connectorInstance, arg1, arg2);
    system:println(messages:getStringPayload(etcdResponse));
    payload = messages:getJsonPayload(etcdResponse);
    string returnedDir = (string)payload.node.key;
    test:assertTrue(strings:equalsIgnoreCase(arg1, returnedDir)
                    , "testDeleteDirectoryRecursiveTrue passed");
}