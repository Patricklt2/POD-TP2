#!/bin/bash

JAVA_RUN_CLASS="ClientQuery5"
JAVA_CLASSPATH="./client/target/classes"

. ./client/target/tpe2-g1-client-1.0-SNAPSHOT/run-client.sh $*

unset JAVA_RUN_CLASS
unset JAVA_CLASSPATH
