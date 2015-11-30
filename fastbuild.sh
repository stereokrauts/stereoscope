#!/bin/bash

mvn --offline -DskipTests -Dfindbugs.skip=true -Dcheckstyle.skip=true -Dmaven.pmd.enable=false clean install
