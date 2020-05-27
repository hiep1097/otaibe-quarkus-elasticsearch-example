#!/bin/sh
set -x #echo on

# Staging
# bash src/test/resources/shell/docker_build_and_run.sh

export PROFILE=staging
export SERVICE_NAME=otaibe-quarkus-elasticsearch-example
export IMAGE_NAME=quarkus/${SERVICE_NAME}
export PORT=11025
export JAVA_HOME=/usr/lib/jvm/java-1.8.0-amazon-corretto

mvn clean package -Pnative -Dquarkus.native.container-build=true -Dquarkus.profile=${PROFILE} && \
  docker build --network host -f src/main/docker/Dockerfile.native -t ${IMAGE_NAME} . && \
  docker tag ${IMAGE_NAME}:latest ${IMAGE_NAME}:${PROFILE} && \
  docker run -i --rm -p ${PORT}:${PORT} \
    --name=${SERVICE_NAME} \
    --network host \
    ${IMAGE_NAME}:${PROFILE}
