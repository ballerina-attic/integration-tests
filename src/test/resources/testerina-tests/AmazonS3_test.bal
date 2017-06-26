import ballerina.lang.system;
import ballerina.lang.messages;
import ballerina.net.http;
import org.wso2.ballerina.connectors.amazons3;
import ballerina.test;
import ballerina.lang.xmls;

function init () (amazons3:ClientConnector) {
    string accessKeyId = system:getEnv("ACCESS_KEY");
    string secretAccessKey = system:getEnv("SECRET_ACCESS_KEY");
    string region = system:getEnv("REGION");
    amazons3:ClientConnector s3Connector = create amazons3:ClientConnector(accessKeyId, secretAccessKey, region);
    return s3Connector;

}

function testCreateBucket () {
    amazons3:ClientConnector s3Connector = init();
    message s3Response = amazons3:ClientConnector.createBucket (s3Connector, "TesterinaTest");
    int s3Status = http:getStatusCode(s3Response);
    test:assertIntEquals(s3Status, 200, "CreateBucket Failed");
    system:println("===testCreateBucket completed===");
}

function testPutObject () {
    amazons3:ClientConnector s3Connector = init();
    message s3Response = amazons3:ClientConnector.putObject (s3Connector, "TesterinaTest", "test.txt", "Sample Content");
    int s3Status = http:getStatusCode(s3Response);
    test:assertIntEquals(s3Status, 200, "PutObject Failed");
    system:println("===testPutBucket completed===");

}

function testPutInvalidBucket () {
    amazons3:ClientConnector s3Connector = init();
    message s3Response = amazons3:ClientConnector.putObject (s3Connector, "BallerinaTest1", "test.txt", "Sample Content");
    int s3Status = http:getStatusCode(s3Response);
    xml s3XmlResponse = messages:getXmlPayload(s3Response);
    string code = xmls:getString(s3XmlResponse, "/Error/Code/text()");
    test:assertIntEquals(s3Status, 404, "PutInvalidBucket Failed");
    test:assertStringEquals(code, "NoSuchBucket", "Invalid Error Code");
    system:println("===testPutInvalidBucket completed===");
}

function testGetObject () {
    amazons3:ClientConnector s3Connector = init();
    message s3Response = amazons3:ClientConnector.getObject (s3Connector, "TesterinaTest", "test.txt");
    int s3Status = http:getStatusCode(s3Response);
    string s3StringResponse = messages:getStringPayload(s3Response);
    test:assertIntEquals(s3Status, 200, "GetObject Failed");
    test:assertStringEquals(s3StringResponse, "Sample Content", "Invalid Content");
    system:println("===testGetObject completed===");
}

function testGetObjectList () {
    amazons3:ClientConnector s3Connector = init();
    message s3Response = amazons3:ClientConnector.getObjectList (s3Connector, "TesterinaTest");
    int s3Status = http:getStatusCode(s3Response);
    test:assertIntEquals(s3Status, 200, "GetObjectList Failed");
    system:println("===testGetObjectList completed===");
}

function testGetBucketList () {
    amazons3:ClientConnector s3Connector = init();
    message s3Response = amazons3:ClientConnector.getBucketList (s3Connector);
    int s3Status = http:getStatusCode(s3Response);
    xml s3XmlResponse = messages:getXmlPayload(s3Response);
    test:assertIntEquals(s3Status, 200, "GetBucketList Failed");
    system:println("===testGetBucketList completed===");
}

function testDeleteObject () {
    amazons3:ClientConnector s3Connector = init();
    message s3Response = amazons3:ClientConnector.deleteObject (s3Connector, "TesterinaTest", "test.txt");
    int s3Status = http:getStatusCode(s3Response);
    test:assertIntEquals(s3Status, 204, "DeleteObject Failed");
    system:println("===testDeleteObject completed===");
}

function testDeleteBucket () {
    amazons3:ClientConnector s3Connector = init();
    message s3Response = amazons3:ClientConnector.deleteBucket (s3Connector, "TesterinaTest");
    int s3Status = http:getStatusCode(s3Response);
    test:assertIntEquals(s3Status, 204, "DeleteBucket Failed");
    system:println("===testDeleteBucket completed===");
}