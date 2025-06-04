#!/bin/bash

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PATH_TO_CODE_BASE=`pwd`

if [[ ! $JAVA_RUN_CLASS ]]
then
    echo "Missing JAVA_RUN_CLASS variable"
    exit 1
fi

#JAVA_OPTS="-Djava.rmi.server.codebase=file://$PATH_TO_CODE_BASE/lib/jars/rmi-params-client-1.0-SNAPSHOT.jar"

JAVA_CLASSPATH="./client/target/classes"
MAIN_CLASS="ar.edu.itba.pod.client.$JAVA_RUN_CLASS"

java $JAVA_OPTS $* -cp "$SCRIPT_DIR/lib/jars/*:$JAVA_CLASSPATH" $MAIN_CLASS
