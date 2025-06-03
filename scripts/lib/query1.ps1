#!/bin/pwsh

$JAVA_RUN_CLASS="Query1"
cd client/target/tpe2-g1-client-1.0-SNAPSHOT
. ./run-client.ps1 @args
cd -
$JAVA_RUN_CLASS = $null