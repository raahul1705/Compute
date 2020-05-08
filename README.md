# Compute
Vanderbilt Undergraduate Research Project on Fog Computing

## Overview
This repository holds the source code for a basic fog-based application. The source code is composed of the 
config files for the Consul application as well as the Android Studio project for the application itself.

Here is the architecture diagram for the application itself.

![Diagram]()

At the moment, the data load that is processed either locally or on the fog network is a simple 
request-reply service.

## Requirements
- HashiCorp Consul (https://www.consul.io/downloads.html)
- Android Studio (https://developer.android.com/studio)
- Android Phone running Android 8.0 or higher (tested on Android 10.0)
- (Optional) Other devices on which you can run Consul

## Installation
### Consul
The configuration files for the Consul service are stored in consul.d. In order to start the Consul
service on a computer from your terminal, run the following command:

`consul agent -data-dir=/path/to/data/dir -config-dir=/path/to/consul.d` 

Make sure that you are in the directory where Consul is installed, or that you
have Consul set up as part of your environment variables.

### Android Studio
Currently, the entry point server is set up to be a hardcoded IP address, as it is the stable part of 
the network. So, set up a computer to be the entry point server in the Consul server such that it has 
a consistent IP address. Once you have set up the entry point machine, obtain its IP address, and replace
the IP address in NetworkSearch.java.

You should modify the line
```java
client = Consul.builder().withUrl("http://192.168.0.11:8500").build();
```
so that the URL is the IP address of the entry point server instead of 192.168.0.11.

Now, once you have finished that step, you can simply compile the application and build it for your
Android phone. If you have any issues, please mention them in the Issues page. 

## Known Problems
1. Consul service discovery not working: In this case, check to make sure that your firewall allows
the Consul service to communicate using certain ports. More information about what ports the Consul
service uses can be found at https://www.consul.io/docs/install/ports.html. A quick fix would be to 
disable the firewall for the duration that you are running the application.
