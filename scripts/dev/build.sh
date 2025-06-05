#!/bin/bash

if ! mvn clean package
then
    echo "Maven build failed. Exiting."
    exit 1
fi

if ! tar -xzvf server/target/tpe2-g1-server-1.0-SNAPSHOT-bin.tar.gz -C server/target/
then
    echo "Failed to extract server tarball. Exiting."
    exit 1
fi

if ! tar -xzvf client/target/tpe2-g1-client-1.0-SNAPSHOT-bin.tar.gz -C client/target/
then
    echo "Failed to extract client tarball. Exiting."
    exit 1
fi
