# SMOL

Network description language (SMOL), which has been designed to describe the necessary network functions, mechanisms, and devices; for the purpose of their computer simulation and verification. 




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



## TODO
* Add support for MQTT 
* Design API for communication with DESMO-J framework
