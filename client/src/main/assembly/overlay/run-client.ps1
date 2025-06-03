#!/bin/pwsh

$PATH_TO_CODE_BASE=${pwd}

if (!$JAVA_RUN_CLASS) {
    echo "Missing JAVA_RUN_CLASS variable"
    exit 1
}

#$JAVA_OPTS="-Djava.rmi.server.codebase=file://$PATH_TO_CODE_BASE/lib/jars/rmi-params-client-1.0-SNAPSHOT.jar"

$MAIN_CLASS="ar.edu.itba.pod.client.$JAVA_RUN_CLASS"

java $JAVA_OPTS -cp 'lib/jars/*' @args $MAIN_CLASS