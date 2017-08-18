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

# Copying the Resources to docker directory since docker cannot read from other locations
cp -r ${current_location}/src/test/resources/Artifacts ${current_location}/src/test/resources/docker

cd ${current_location}/src/test/resources/docker
sudo docker build --no-cache -t dockerhub.private.wso2.com/ballerina_integration_test_server:2.0 .

# Pushing the docker image to registry
sudo docker push dockerhub.private.wso2.com/ballerina_integration_test_server:2.0
