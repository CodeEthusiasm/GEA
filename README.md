# Gas and Electricity preditive API
This project is a Net-Computing course project at University of Groningen.
The project is about implementation of a smart building system which shows the
predictive gas and electricity consumption. It subdivides into two modules which
are WebServer and Client.

### WebServer
The implementation of the server includes Restful API calls for clients, web
application and message controller through RabbitMQ.

### Client
The implementation of building client part is to send/receive message through
RabbitMQ as well as P2P network between building clients through JAVA sockets.

## Prerequisite
Maven is used for building system so that it may install required libraries in order
to build the projects.
```
Java 9+
Maven v.3.3.9+
```

## Running Server (webserver)
In order to run clients, the server should be run first. In order to listen
messages from the clients, you have to change constant `SERVER_URL` in
`MessageController` class according to your server url.
For connection between remote computers, the rabbitMQ configuration file
`rabbitmq.conf` may include the following lines.
```
loopback_users = none
listners.tcp.1 = YOUR_SERVER_URL:5672
```
The default web application port number is `8080`.

## Running Client (client)
After running the server, the client part is required to change constant `SERVER_URL`
in class `BuildnigClient`.
to send/receive message from the server. The port number constant `PORT` that might
be used is required to change to the one that you want to use in `BuildingFrame` class.
For generating P2P network, the building IP address and used port number is required
to be registered. The server notifies updated information about neighbors within a
certian zip-code so the client should be registered in the same zip-code for updating
information.

## Built With
* Spring boot application
* Maven
* RabbitMQ
* MongoDB
* JSON

## Authors (Group 27)
* **Jeongkyun Oh**
* **Nikolas Hadjipanayi**
* **Thomas den Hollander**
