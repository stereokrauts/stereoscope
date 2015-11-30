#!/bin/bash

if [[ $1 == "" ]]; then
	NEW_VERSION="3.0.0-SNAPSHOT"
else
	NEW_VERSION=$1
fi

echo "Setting version to $NEW_VERSION"

mvn -f stereoscope.app/pom.xml tycho-versions:set-version -DnewVersion="${NEW_VERSION}" -Dartifacts="stereoscope.app"
#mvn -f stereoscope.app/pom.xml tycho-versions:update-pom
