#!/bin/sh
is_running_in_container() {
  awk -F: '/cpuset/ && $3 ~ /^\/$/ { c=1 } END{ exit c }' /proc/self/cgroup
}

if is_running_in_container; then
  echo "Aye!! I'm in a container"
  newman run ./provider-ctk-local-docker.postman-collection.json
else
  echo "Nay!! I'm not in a container"
  newman run ./provider-ctk-local-docker.postman-collection.json
fi