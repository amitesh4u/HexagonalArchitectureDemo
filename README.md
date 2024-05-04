# Hexagonal Architecture with Java and Spring Boot

This project contains a simple Java/Spring Boot application implemented according to hexagonal architecture in multiple steps.

## What is a Hexagonal Architecture?
Alistair Cockburn introduced the hexagonal software architecture as follows
* All non-technical business logic must reside in Application core and must be invoked by User, External application or automated Tests without any difference.
* All communication to the Application core from external entities say User, Database or Test suite must happen through Ports (Interfaces).
* An Adapter is like a middleman who will control the communication between External entities and Application core via Ports. 


![Hexagonal Architecture Modules](documents/hexagonal-architecture-with-control-flow.png)
Hexagonal architecture with control flow (www.happycoders.eu)

# Application overview
The application mimics a simplified online store with following functionalities:

* Product search
* Adding a product to the shopping cart
* Retrieving the shopping cart with the products, their respective quantity, and the total price
* Emptying the shopping cart

### Note:
* The amount of a product added to the cart must be at least one.
* After adding a product, the total quantity of this product in the cart must not exceed the amount of the product available in the warehouse.
* After emptying the Cart there should be no products available in the Cart

# Architecture Overview
The application will be developed in following steps:

* Step 1 - Hexagonal Architecture without any application framework say Spring. The code will only use JDK 21, RESTEasy and Undertow (lightweight server).
* Step 2 - Replace In-Memory Database to MySql
* Step 3 - Add [Spring Boot](https://spring.io/projects/spring-boot/) framework

The source code has four modules:
* `model` - It contains the domain models
* `application` - It contains the domain services and the ports
* `adapters` - It contains the adapters like REST, In-memory and JPA
* `boostrap` - It contains the configuration and bootstrapping logic like instantiating adapters/domain services and running Undertow web server

The following diagram shows the final hexagonal architecture of the application along with the source code modules.

![Modules](documents/hexagonal-architecture-modules-uml.png)

![Hexagonal Architecture Modules](documents/hexagonal-architecture-modules.png)
Image source :www.happycoders.eu
