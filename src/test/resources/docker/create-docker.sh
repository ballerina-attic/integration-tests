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

# This file is triggered from Jenkins pre-build Step

current_location=${WORKSPACE}
echo "Current location is set to : " ${current_location}
cd ${current_location}/src/test/resources/docker

# Deleting any leftovers from the previous builds
rm -rf ballerina-tools-*
rm -rf Artifacts

echo "Downloading the Ballerina Distribution!"
source ${current_location}/src/test/resources/docker/get-latest-distribution.sh

#unzipping the distribution pack
unzip -q ballerina-tools-*.zip

#deleting the zip
rm ballerina-tools-*.zip

# Download dependencies
source ${current_location}/src/test/resources/docker/get-depencencies.sh

# Creating the Docker image and pushing the image
source ${current_location}/src/test/resources/docker/build.sh

echo "Creating the docker image!"
