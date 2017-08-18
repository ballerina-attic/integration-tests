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

#cd ${current_location}/src/test/resources
echo "Starting to download dependencies!"

for path in ${current_location}/src/test/resources/Artifacts/*; do
    [ -d "${path}" ] || continue # if not a directory then skip
    dirname="$(basename "${path}")"
    cd ${path}
    if [ -d 'resources/bre/lib' ]; then
        echo "Downloading dependencies for pattern : " ${dirname}
        cd resources/bre/lib/
        mvn dependency:copy-dependencies -DoutputDirectory=. -q
        #cp -r ${ballerina_home}/${ballerina_test_repo_name}/src/test/resources/Artifacts/${pattern}/resources/bre/lib/*.jar ${ballerina_home}/distribution/bre/lib
    fi
done

echo "Successfully downloaded the dependencies!"