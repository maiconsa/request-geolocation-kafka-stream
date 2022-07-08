#!/usr/bin/env bash

set -eu
set -o pipefail

echo "Waiting for Elasticsearch availability";
until curl -s  "http://elasticsearch:9200" | grep -q "missing authentication credentials"; do sleep 30; done;

echo "Setting kibana_system password";
until curl -s -X POST  -u "elastic:${ELASTIC_PASSWORD}" -H "Content-Type: application/json" "http://elasticsearch:9200/_security/user/kibana_system/_password" -d "{\"password\":\"${KIBANA_SYSTEM_PASS}\"}" | grep -q "^{}"; do sleep 10; done;

echo "Creating logstash_internal user";
 curl -s -X POST  -u "elastic:${ELASTIC_PASSWORD}" -H "Content-Type: application/json" "http://elasticsearch:9200/_security/user/${LOGSTASH_INTERNAL_USER}" -d "{\"password\":\"${LOGSTASH_INTERNAL_PASS}\" ,\"roles\":[\"logstash_internal_role\"]}" 

echo "Setting role to  logstash_internal_role";
curl -s -X POST  -u "elastic:${ELASTIC_PASSWORD}" -H "Content-Type: application/json" "http://elasticsearch:9200/_security/role/logstash_internal_role" -d "$(< ./logstash_role.json)"


echo "All done!";