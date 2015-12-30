#!/usr/bin/env bash

printf "\n> \e[1;37mBuilding grails-cache-redis\e[0m\n"

set -e

ABSOLUTE_PATH=$(cd `dirname "${BASH_SOURCE[0]}"` && pwd)
cd $ABSOLUTE_PATH

printf "\n# Checking for failed dependencies\n\n"
./gradlew dependencies | grep FAILED && exit 1

if [ -n "${ARTIFACTORY_CONTEXT_URL}" ]; then
    printf "\n# Assembling plugin\n\n"
    ./gradlew assemble

    printf "\n# Publishing plugin to Artifactory\n\n"
    ./gradlew artifactoryPublish --info
fi