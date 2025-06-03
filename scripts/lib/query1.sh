#!/bin/bash

JAVA_RUN_CLASS="ClientQuery1"
cd client/target/tpe2-g1-client-1.0-SNAPSHOT
. ./run-client.sh $*
cd -
unset JAVA_RUN_CLASS