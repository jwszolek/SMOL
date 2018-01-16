# SMOL

Network description language (SMOL), which has been designed to describe the necessary network functions, mechanisms, and devices; for the purpose of their computer simulation and verification. 

<p align="center">
  <img src="readme-media/graph_smol.png?raw=true" alt="ELK"/>
</p>



## Requirements
* Java
* Groovy

## Installation

```
git clone https://github.com/jwszolek/smol.git
cd ./smol

mvn install:install-file -Dfile=libs/desmoj-2.5.1c-bin.jar
-DgroupId=desmoj -DartifactId=desmoj -Dversion=2.5.1 -Dpackaging=jar   

mvn clean install
```


## First simulation

SMOL code below describes a simple measurement network presented on the image below.

```
adapter "eth1", {
    ip "1"
    generator_connected "true"
    dst "Server"
}

adapter "Server", {
    ip "2"
}

converter "rs485", {
    dst "Server"
}

sensor "temperature", {
    connect "rs485"
}

draw "map", {
    fullmap "true"
}

sim "run", {
    stop "10s"
}
```



## TODO
* Add support for MQTT 
* Design API for communication with DESMO-J framework
