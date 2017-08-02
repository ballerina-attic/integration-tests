# integration-tests

Integration-tests is a Kubernetes based integration test running framework. At a glance it supports the following set of features.

* Running native testerina tests
* Deploying a given deployment pattern and running tests
* Un-deploying deployments
* Password based value encryption and decryption
* Setting Environment level variables

### How to add a new deployment and a Test
 
The main configuration file related to the deployment is deployment.yaml, Following is the format of deployment.yaml file,
````
deployments:
  -
    name: ballerina_standalone
    enable: true
    deployscripts: deploy.sh
    repository: N/A
    suite: pattern1
    undeployscripts: undeploy.sh
    filePath: deployment.json
    instancemap :
    -
      ballerina : ballerina-test-version-1
 
  -
    name: mysql_connector
    enable: true
    deployscripts: deploy.sh
    repository: N/A
    suite: pattern2
    undeployscripts: undeploy.sh
    filePath: deployment.json
    instancemap :
    -
      ballerina : ballerina-server
      mysql : mysql-db
````
You can have multiple deployment and deployment have following properties

````
name: ballerina_standalone	    : Name of the deployment
enable: true				    : Whether the deployment is enabled or not
deployscripts: deploy.sh		: The location of the deployment script file
repository: N/A			        : Git repository to fetch deployment scripts (Not used)
suite: pattern1			        : Name of the suite thats being used.
undeployscripts: undeploy.sh	: Undeploy script location
filePath: deployment.json		: Location of the deployment.json
instancemap :			        : list of tag name mappings.
````
If adding a new deployment to the test infrastructure, related configurations should be added in the deployment.yaml.

### Adding Deployment Artifacts
 
Deployment artifacts contains replication controllers and kubernetes services. The nature and configurations change according to your environment requirement. Note : Before adding any new environments make sure that you cannot run your tests on top of already existing environment. 
 
All the commands and kubernetes artefacts should be wrapped by a shellscript called deploy.sh. This shell script will be executed by the framword when a setenvironmenbt is triggered. After the deployment is done it will create a output file called deployment.json which contains all the details about pods and relevant ports (Endpoints).  The script is responsible of waiting till the server is started properly.
 
The outcome of the deploy.sh is deployment.json, which has all the endpoints. Following is a sample file. 

````
[  
    {  
        "hostIP":"192.168.48.164",
        "lable":"ballerina-server",
        "ports":[  
            {  
                "protocol":"servlet-http",
                "port":"32013"
            }
        ]
    },
    {  
        "hostIP":"192.168.48.163",
        "lable":"mysql-db",
        "ports":[  
            {  
                "protocol":"mysql-port",
                "port":"30306"
            }
        ]
    }
]

````

### Adding Test Artifacts

In Order to test some functionality you need ballerina services to be started in the ballerina server pod. To add services and external jars you need to add them to following locations
 
**Bal Service**  : Artifacts/<PATTERN_NAME>/resources/services
**External jars** : Artifacts/<PATTERN_NAME>/resources/bre/lib

To add external Jars, define third party dependencies inside a POM and they will be automatically downloaded.
 
When writing a test BallerinaBaseTest should be extended in your test class. When you extend this class, you will have access to ballerina endpoint and other relevant endpoints etc. 
 
After adding a test the test should be added to TestNG file. Each pattern has a relevant test suite and each suite will have its own testng file, and to group these files, there is a master testng.xml file which is located src/test/resources/testng.xml This fle contains the following,

````
<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd" >
<suite name="suite of suites">
    <suite-files>
        <suite-file path="Test-suites/testng_pattern1.xml"/>
        <suite-file path="Test-suites/testng_pattern2.xml"/>
    </suite-files>
</suite>
````
testng_pattern1.xml and testng_pattern2.xml represents tests relevant to each deployment. This contains following,

````
<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd" >
<suite name="ballerina_standalone">
    <test name="ballerina-tests" preserve-order="true" parallel="false">
        <classes>
            <class name="org.ballerina.tests.routing.EchoService"/>
        </classes>
    </test>
</suite>
````
If you look at the above, the Suite name attribute should match the deployment name which was defined in deployment.yaml. This is how a suite is mapped against a deployment pattern.
