#!/bin/pwsh

$JAVA_RUN_CLASS="ClientQuery5"
$JAVA_CLASSPATH="./client/target/classes"

. ./client/target/tpe2-g1-client-1.0-SNAPSHOT/run-client.ps1 @args

$JAVA_RUN_CLASS=$null
$JAVA_CLASSPATH=$null
