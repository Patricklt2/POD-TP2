#!/bin/pwsh

mvn clean package

if (!$?) {
    echo "Maven build failed. Exiting."
    exit 1
}

tar -xzvf server/target/tpe2-g1-server-1.0-SNAPSHOT-bin.tar.gz -C server/target/

if (!$?) {
    echo "Failed to extract server tarball. Exiting."
    exit 1
}

tar -xzvf client/target/tpe2-g1-client-1.0-SNAPSHOT-bin.tar.gz -C client/target/

if (!$?) {
    echo "Failed to extract client tarball. Exiting."
    exit 1
}