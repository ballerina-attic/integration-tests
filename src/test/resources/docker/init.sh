#!/bin/bash
# Copyright (c) 2017, WSO2 Inc. (http://wso2.com) All Rights Reserved.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

# Docker init script, this is the Entry Point of the Docker image
# These variables are parsed as Environment variables through Kubernetes controller

#pattern=null
#ballerina_home=/opt/ballerina
#ballerina_test_repo=https://github.com/yasassri/ballerina-distributed-tests.git
#ballerina_test_repo_name=ballerina-distributed-tests


echo "ballerina_home is set to : " $ballerina_home
echo "repo name is set to : " $ballerina_test_repo
echo "ballerina_test_repo_name is set to : "$ballerina_test_repo_name

echo "Creating the Ballerina Home Directory"
mkdir -p ${ballerina_home}

#echo "Cloning the Ballerina Test Repo!!!"
cd ${ballerina_home}
mkdir distribution
#git clone ${ballerina_test_repo}

#cd ${ballerina_home}/${ballerina_test_repo_name}/src/test/resources/Artifacts/common-scripts

#echo "ballerina_home is : " ${ballerina_home}
#echo "ballerina_test_repo is : " ${balerina_test_repo}
#echo "ballerina_test_repo_name is : " ${balerina_test_repo_name}
#echo "Deployment Pattern is : " ${pattern}

cd /opt
# Check if the dependency folder exists, If exists copy the dependencies
if [ -d '/opt/Artifacts/${pattern}/resources/bre/lib' ]; then
  echo "Copying Ballerina Dependencies for Pattern : " ${pattern}
  cp -r /opt/Artifacts/${pattern}/resources/bre/lib/*.jar /opt/distribution/bre/lib
fi

#cd into the services package structure root
cd /opt/Artifacts/${pattern}
chmod +x /opt/distribution/bin/ballerina

#Source the Environment file
echo "Starting the Ballerina Server!!!"

#Start the Server
sh /opt/distribution/bin/ballerina run resources/services
