#!/bin/bash

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PATH_TO_CODE_BASE=`pwd`

MAIN_CLASS="ar.edu.itba.pod.server.Server"

java $JAVA_OPTS -cp "$SCRIPT_DIR/lib/jars/*:$JAVA_CLASSPATH" $* $MAIN_CLASS 
