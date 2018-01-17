# SMOL

Network description language (SMOL), which has been designed to describe the necessary network functions, mechanisms, and devices; for the purpose of their computer simulation and verification. 

<p align="center">
  <img src="readme-media/graph_smol.png?raw=true" alt="ELK"/>
</p>



## Requirements
* Java 1.7
* Groovy 2.4.3

## Installation

```
git clone https://github.com/jwszolek/smol.git
cd ./smol

mvn install:install-file -Dfile=libs/desmoj-2.5.1e-bin.jar
-DgroupId=desmoj -DartifactId=desmoj -Dversion=2.5.1 -Dpackaging=jar   

mvn clean install
```


## First simulation

SMOL code below describes a simple measurement network presented on the image below. The list of all available commands that you can use in the SMOL programs can be found in here: [SMOL Syntax](#smol-syntax-elements)
  

```
/*
 * Define a network adapter 
 * Assign IP address and name
 * Set destination for TCP packages
 * Attach TCP packages generator   
 */
adapter "eth1", {
    ip "1"
    generator_connected "true"
    dst "Server"
}

/*
 * Define a network adapter
 * Assign IP address and name
 */
adapter "Server", {
    ip "2"
}

/*
 * Define rs485 converter
 * Set destination IP or name
 */
converter "rs485", {
    dst "Server"
}

/*
 * Temp sensor definition
 * Connect sensor to rs485 bus
 */
sensor "temperature", {
    connect "rs485"
}

/*
 * Run map function which draws a graph with all connections 
 */ 
draw "map", {
    fullmap "true"
}

/*
 * Run simulation phase 
 */
sim "run", {
    stop "10s"
}
```

Provide a SMOL script as a parameter:

```
java -jar target/smol-1.0-SNAPSHOT.jar network.smol
```

Expected results:

* Graph that describes network connections and relations. In the Edit section, you can modify graph layout by selecting appropriate options.

<p align="center">
  <img src="readme-media/graph_example.png?raw=true" alt="graph_example"/>
</p>




## SMOL syntax elements

* Adapter

* Converter

* Sensor



## TODO
* Support MQTT simulation  
