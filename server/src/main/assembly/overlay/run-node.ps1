#!/bin/pwsh

$PATH_TO_CODE_BASE=${pwd}

#$JAVA_OPTS="-Djava.security.debug=access -Djava.security.manager -Djava.security.policy=/$PATH_TO_CODE_BASE/java.policy -Djava.rmi.server.useCodebaseOnly=false"	

$MAIN_CLASS="ar.edu.itba.pod.server.Server"

java  $JAVA_OPTS -cp "$PSScriptRoot/lib/jars/*;$JAVA_CLASSPATH" @args $MAIN_CLASS
