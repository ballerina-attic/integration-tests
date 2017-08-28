#! /bin/bash
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

ballerina_port=32013
server_response="Ping from the server!"

prgdir=$(dirname "$0")
script_path=$(cd "$prgdir"; pwd)
echo "Kube Master URL is Set to : "$KUBERNETES_MASTER
echo "Current location : "$script_path
echo "Creating the K8S Pods!!!!"

echo "Creating the MySQL RC and Service!"
kubectl create -f $script_path/mysql_service.yaml
kubectl create -f $script_path/mysql_rc.yaml

sleep 30

echo "Creating the ballerina server RC and Service!"
kubectl create -f $script_path/ballerina_server_service.yaml
kubectl create -f $script_path/ballerina_server_rc.yaml

function getKubeNodeIP() {
    IFS=$','
    node_ip=$(kubectl get node $1 -o template --template='{{range.status.addresses}}{{if eq .type "ExternalIP"}}{{.address}}{{end}}{{end}}')
    if [ -z $node_ip ]; then
      echo $(kubectl get node $1 -o template --template='{{range.status.addresses}}{{if eq .type "InternalIP"}}{{.address}}{{end}}{{end}}')
    else
      echo $node_ip
    fi
}

kube_nodes=($(kubectl get nodes | awk '{if (NR!=1) print $1}'))
host=$(getKubeNodeIP "${kube_nodes[0]}")

echo "Waiting Ballerina to launch on http://${host}:${ballerina_port}"
sleep 10

# The loop is used as a global timer. Current loop timer is 3*100 Sec.
for number in {1..100}
do
echo $(date)" Waiting for server startup!"
 if [ "$server_response" == "$(curl --silent --get --fail --connect-timeout 5 --max-time 10 http://${host}:${ballerina_port}/ping)" ]
 then
  break
 fi
sleep 3
done

echo 'Generating The deployment.json!'
pods=$(kubectl get pods --output=jsonpath={.items..metadata.name})
json='['
for pod in $pods; do
         hostip=$(kubectl get pods "$pod" --output=jsonpath={.status.hostIP})
         lable=$(kubectl get pods "$pod" --output=jsonpath={.metadata.labels.name})
         servicedata=$(kubectl describe svc "$lable")
         json+='{"hostIP" :"'$hostip'", "lable" :"'$lable'", "ports" :['
         declare -a dataarray=($servicedata)
         let count=0
         for data in ${dataarray[@]}  ; do
            if [ "$data" = "NodePort:" ]; then
            IFS='/' read -a myarray <<< "${dataarray[$count+2]}"
            json+='{'
            json+='"protocol" :"'${dataarray[$count+1]}'",  "port" :"'${myarray[0]}'"'
            json+="},"
            fi

         ((count+=1))
         done
         i=$((${#json}-1))
         lastChr=${json:$i:1}

         if [ "$lastChr" = "," ]; then
         json=${json:0:${#json}-1}
         fi

         json+="]},"
done

json=${json:0:${#json}-1}
json+="]"

echo $json;

cat > deployment.json << EOF1
$json
EOF1