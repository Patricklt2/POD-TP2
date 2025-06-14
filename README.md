# TP2 POD Grupo 1

## Building

```sh
. ./scripts/dev/build.sh
```

```ps1
. .\scripts\dev\build.ps1
```

## Executing

### Server nodes

```sh
. ./scripts/lib/server-node.sh
```

```ps1
. .\scripts\lib\server-node.ps1
```

#### Parameters

 - `-Dif='NET'`: Picks the network in which Hazelcast will look for
 other nodes, defaults to '127.0.0.*'

### Clients

Replace 0 with the query you want to execute.

```sh
. ./scripts/lib/query0.sh
```

```ps1
. .\scripts\lib\query0.ps1
```

#### Paramenters

##### Globals

 - `-DinPath='PATH'`: Picks where to search for the input files.

 - `-DoutPath='PATH'`: Picks where to write the output files.

 - `-Dcity=['CHI','NYC']`: Picks the city that will be queried.

 - `-Daddresses='IP:PORT'`: Lists the nodes to which distribute the
 query's objects, `;` separated.

 - `-Dev`: Use the -Short version of the input files.

##### Client 2

 - `-Dq='N'`: Defines the quadrants size, must be between 0 and 1

##### Client 3

 - `-Dw='N'`: Defines the monthly accumulation, must be between 1
 and 12.

##### Client 4

 - `-Dneighbourhood='NAME'`: Defines the neighbourhood to which
 filter by.
