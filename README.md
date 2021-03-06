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
  <img src="readme-media/transformer-node.png?raw=true"/>
</p>


## First simulation

SMOL code below describes a simple measurement network presented on the image below. The purpose of this task is to simulate network traffic in modeled network.
 
 The list of all available commands that you can use in the SMOL programs can be found in here: [SMOL Syntax](#smol-syntax-elements)
  

```
// Transfer node definition
// Define a network adapter
// Assign IP address
tn "eth1", {
    ip "1"
}

tn "eth3", {
    ip "3"
}

tn "server", {
    ip "2"
}

expander "bacnet-gw-1", {
    connect "eth4"
    model "bacnet"
}

// Expander node definition
// Define CAN network gateway
// Connect to the Transfer node
expander "can-gw", {
    connect "eth1"
    model "bacnet"
}


// Expander node definition
// Define Bacnet network gateway
// Connect to the Transfer node
expander "bacnet-gw", {
    connect "eth3"
    model "bacnet"
}

// Temp sensor definition
// Connect sensor to CAN bus
// Set measure values final destination address (2)
san "temp-sensor-can-1",{
    connect "can-gw"
    destAddress "2"
    freq "2000" }

san "temp-sensor-can-2",{
    connect "can-gw"
    destAddress "2"
    freq "200"
}

// Draw graph
action "draw", {
    fullmap "true"
}

// Run simulation phase. Stop simulation after 60 sec.
action "sim", {
    stop "60000"
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

* `tn "name" { ip address }`
*  `expander "name" { connect device_name, model type }`
* `san "name" { connect device_name, destAddress ip_address, freq value_generation_frequency }`


## MQTT simulation
To simulate the mqtt protocol, use "mqtt" model type in the expander.
#### Syntax elements
* destAddress - ip address of the broker that is connected to transferring node, client publishes messages to this address       
* pubTopics - list of topics on which client publishes messages, separated by a comma
* subTopics - list of topics subscribed by the client, separated by a comma, the broker transfers messages to subscribed clients

#### Example
```
tn "eth1", {
    ip "1"
}

tn "eth2", {
    ip "2"
}

tn "eth3", {
    ip "3"
}

tn "eth4", {
    ip "4"
}

expander "mqtt-adapter-1", {
    connect "eth1"
    model "mqtt"
}

expander "mqtt-adapter-2", {
    connect "eth2"
    model "mqtt"
}

expander "mqtt-adapter-3", {
    connect "eth3"
    model "mqtt"
}

expander "mqtt-adapter-4", {
    connect "eth4"
    model "mqtt"
}

//broker
san "mqtt-broker-1",{
    connect "mqtt-adapter-1"
    freq "50"
}

// only publish
san "mqtt-client-1",{
    connect "mqtt-adapter-2"
    destAddress "1"
    pubTopics "pir,hvac"
    freq "500"
}

// only subscribe
san "mqtt-client-2",{
    connect "mqtt-adapter-3"
    destAddress "1"
    subTopics "pir,hvac"
    freq "500"
}

// publish and subscribe
san "mqtt-client-3",{
    connect "mqtt-adapter-4"
    destAddress "1"
    pubTopics "pir,hvac"
    subTopics "pir,hvac"
    freq "500"
}

action "draw", {
    fullmap "true"
}

action "sim", {
    stop "10000"
}
```

#### Logs
* Client -> broker | client_name | ip_address: message
* Broker -> client | client_name | ip_address: message
* Received | client_name | ip_address: message

Message contains: srcAddress, dstAddress, transmissionTime, data (topic:UUID)

## References
* Z.Kowalczuk, J.Wszolek - Analysis Of Economical Lighting Of Highways In The Environment Of SMOL Language. Metrology and Measurement Systems, Polish Academy of Sciences. (2017)

* Z.Kowalczuk, J.Wszolek - Networked Object Monitor - a distributed system for monitoring, diagnostics and control of complex industrial facilities. Metrology And Measurement Systems, ISSN 0860-8229 (2012)

  
