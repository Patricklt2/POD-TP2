#!/bin/bash

PATH_TO_CODE_BASE=`pwd`

MAIN_CLASS="ar.edu.itba.pod.server.Server"

java  $JAVA_OPTS -cp "$0/../lib/jars/*;$JAVA_CLASSPATH" $MAIN_CLASS $*
