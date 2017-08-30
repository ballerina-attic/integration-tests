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

cd /opt
dependency_path='/opt/Artifacts/'${pattern}'/resources/bre/lib'
# Check if the dependency folder exists, If exists copy the dependencies
if [ -d ${dependency_path} ]; then
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
