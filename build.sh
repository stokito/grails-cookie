#!/usr/bin/env bash
export TERM="dumb"

printf "\n> \e[1;37mBuilding grails-cookie\e[0m\n"

set -e

ABSOLUTE_PATH=$(cd `dirname "${BASH_SOURCE[0]}"` && pwd)
cd $ABSOLUTE_PATH

printf "\n# Checking for failed dependencies\n\n"
./gradlew dependencies | grep FAILED && exit 1

if [ -n "${ARTIFACTORY_CONTEXT_URL}" ]; then
    printf "\n# Building jar\n\n"
    ./gradlew jar

    printf "\n# Building jar with sources\n\n"
    ./gradlew sourcesJar

    printf "\n# Publishing plugin to Artifactory\n\n"
    ./gradlew artifactoryPublish
else
    printf "# Installing Grails plugin\n\n"
    grails install
fi