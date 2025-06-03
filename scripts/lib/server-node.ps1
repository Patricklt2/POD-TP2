#!/bin/pwsh

$JAVA_CLASSPATH="./server/target/classes"
. ./server/target/tpe2-g1-server-1.0-SNAPSHOT/run-node.ps1 @args
$JAVA_CLASSPATH=$null
