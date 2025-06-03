#!/bin/bash

JAVA_CLASSPATH="./server/target/classes"
. ./server/target/tpe2-g1-server-1.0-SNAPSHOT/run-server.sh $*
unset JAVA_CLASSPATH
