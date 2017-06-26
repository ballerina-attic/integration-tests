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

# This file is triggered from init.sh with the Docker Entry point

echo "ballerina_home is : " ${ballerina_home}
echo "ballerina_test_repo is : " ${balerina_test_repo}
echo "ballerina_test_repo_name is : " ${balerina_test_repo_name}
echo "Deployment Pattern is : " ${pattern}


#Download the latest dirtribution
sh get-latest-distribution.sh

#Extract the distribution to the temporary location and move it to the distribution directory
mkdir tmp
unzip -q ballerina-tools-*.zip -d tmp/

echo "Copying ballerina files from the temp directory to distribution directory"
cp -r tmp/*/* ${ballerina_home}/distribution/
echo "Deleting the temp directory!!"
rm -rf tmp
echo "Copying the dependency Jars"

#Copy the dependency Jars to the server
cp -r ${ballerina_home}/${ballerina_test_repo_name}/src/test/resources/Artifacts/${pattern}/resources/bre/lib/* ${ballerina_home}/distribution/bre/lib

#cd into the services package structure root
cd ${ballerina_home}/${ballerina_test_repo_name}/src/test/resources/Artifacts/${pattern}
chmod +x ${ballerina_home}/distribution/bin/ballerina

#Source the Environment file
echo "Starting the Ballerina Server!!!"

#Start the Server
sh ${ballerina_home}/distribution/bin/ballerina run service resources/services