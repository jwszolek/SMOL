# SMOL

Network description language (SMOL), which has been designed to describe the necessary network functions, mechanisms, and devices; for the purpose of their computer simulation and verification. 

<p align="center">
  <img src="readme-media/graph_smol.png?raw=true" alt="ELK"/>
</p>



## Requirements
* Java 1.7
* Groovy 2.4.3
* R 3.4.2

## Installation

```
git clone https://github.com/jwszolek/smol.git
cd ./smol

mvn install:install-file -Dfile=libs/desmoj-2.5.1e-bin.jar
-DgroupId=desmoj -DartifactId=desmoj -Dversion=2.5.1 -Dpackaging=jar   

mvn clean install
```

## Language concept

SMOL, the dedicated language of semantics enabling the designer to model measurement- diagnostics-control networks, is an example of a domain-specific tool, tailored to solve problems from a specific area.

Description of the network structure is an essential basis for representing the problems of configuration, re-structuralisation, and optimisation of network subsystems. In other words, SMOL is used to describe the components of a diagnostic network, and the accompanying connections, along with their appropriate parameterisation. Such a formal representation will also enable one to verify the future process of implementing such a network with the use of a relevant parser; which is a software platform for analysing a programmed network structure and its parameters.

The semantics of the language (SMOL) are based on structures of hierarchical processing. It is assumed that all network signals are eventually bundled together in the Central Node, which is a central vertex of the analysed graph.

## Components of network description

One of the best ways of modelling industrial computer networks is to use a directed flow graph as a tool suitable for describing relations occurring in a network of interconnections. Such a model is represented by a triple of sets (vertices, edges, and mappings). By defining these sets, we can precisely express the diagram of information flows in the MDC networks.

Below we introduce the basic types of nodes used in graphs to properly represent network relations. The main division concerns the use of static and dynamic nodes implementing transforming functions.

### Central Node CN

In general, a central node (CN component) is an object of type MIMO (with multiple inputs and outputs). It is a necessary component in defining practical MDC networks. In an actual computer connection network, such a component represents a centre for transmitted information that receives data from measuring gauges and transmits information or control signals to receivers or actuators. A general scheme of such node is shown below.

<p align="center">
  <img src="readme-media/central_node.png?raw=true"  />
</p>

### Transferring Node TN

Transferring node is a universal element of type MIMO, which functions as a transmitter (element TN) in the network; it has a structure shown in figure below, where components CC and R/T function analogously to those described above for the central node.
The kernel of this node can perform all necessary functions T(A) transforming information A.

In an actual implementation of an MDC network, the components TN are assigned specific inputs and outputs, and the desired methods of signal transmission attributed to them. In practice, transmitters are working in most universal contemporary wire and wireless standards (WiFi11, ZigBee12, Switch13, Modem).

<p align="center">
  <img src="readme-media/transfer_node.png?raw=true"  />
</p>


### Transformer TR – transforming function

MDC systems often require quick, and rather simple, modification of the transmitted data stream. Therefore, transformers (TRs) enable one to implement a program of universal mathematical function T(A) (sum, difference, product, quotient, differentiation, integration, averaging, and some slightly more complex digital filters) operating on the input data. Currently, the possibility of using transformers is provided only for the nodes CN and TN.

<p align="center">
  <img src="readme-media/transformer-node.png?raw=true"  />
</p>


## First simulation

SMOL code below describes a simple measurement network presented on the image below. The purpose of this task is to simulate network traffic in modeled network.
 
 The list of all available commands that you can use in the SMOL programs can be found in here: [SMOL Syntax](#smol-syntax-elements)
  

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

To start the simulation provide a SMOL script as a parameter:

```
java -jar target/smol-1.0-SNAPSHOT.jar network.smol
```

Expected results:

* Graph that describes network connections and relations. You can modify graph layout by selecting the appropriate options.

<p align="center">
  <img src="readme-media/graph_example.png?raw=true" alt="graph_example"/>
</p>


Simulation results:

* The plot below describes network traffic generated by 3 separate network adapters working in the same network segment. Simulating network is 1Mb/s. Maximum throughput of such network is around 83 Frames per second. During this simulation, we assume that a single frame has 1500B.

<p align="center">
  <img src="readme-media/smol.png?raw=true" alt="plot_smol"/>
</p>



## SMOL syntax elements

* Adapter

* Converter

* Sensor



## TODO
* Support MQTT simulation



## References
* Z.Kowalczuk, J.Wszolek - Analysis Of Economical Lighting Of Highways In The Environment Of SMOL Language. Metrology and Measurement Systems, Polish Academy of Sciences. (2017)

* Z.Kowalczuk, J.Wszolek - Networked Object Monitor - a distributed system for monitoring, diagnostics and control of complex industrial facilities. Metrology And Measurement Systems, ISSN 0860-8229 (2012)

  
