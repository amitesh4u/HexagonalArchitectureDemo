# Hexagonal Architecture with Java and Spring Boot

This project contains a simple Java/Spring Boot application implemented according to hexagonal architecture in multiple steps.

## Technologies used - Step 2
* **JDK 21** - Core language
* **Junit5** - Unit Testing
* **Mockito** - Mocking objects while Unit testing
* **AssertJ** - Simple assertion style while Unit testing
* **Lombok** - Auto generates boilerplate code for POJOs
* **RESTful Web Services (JAX-RS)** - To implement REST Adapters
* **Undertow** - Web server
* **Hibernate** - Persistence Framework
* **MySql** - Database 
* **TestContainers** - A framework that allows us to launch a MySQL database as a Docker container from tests.
* **Docker** - To run MySQL as container. Just install Docker and done


## Important Plugins for Intellij
* HTTP Client - Required to run sample http commands from document/sample-requests.http (Step 1,2)
* JUnit
* Lombok
* Jakarta EE: RESTful Web Services (JAX-RS) (Step 1,2)
* Jakarta EE: Persistence (JPA) (Step 2)


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
* Step 2 - Replace In-Memory Database with JPA and MySQL
* Step 3 - Add [Spring Boot](https://spring.io/projects/spring-boot/) framework

The source code has four modules:
* `model` - It contains the domain models
* `application` - It contains the domain services and the ports
* `adapter` - It contains the adapters like REST, In-memory and JPA
* `boostrap` - It contains the configuration and bootstrapping logic like instantiating adapters/domain services and running Undertow web server

The following diagram shows the final hexagonal architecture of the application along with the source code modules.

![Modules](documents/hexagonal-architecture-modules-uml.png)

![Hexagonal Architecture Modules](documents/hexagonal-architecture-modules.png)
Image source :www.happycoders.eu

## Lombok annotations used in project
* **@RequiredArgsConstructor** - Generates constructor that take one argument per final / non-nullfield
* **@AllArgsConstructor** - Generates constructor that take one argument for every field.
* **@Data** - A shortcut for @ToString, @EqualsAndHashCode, @Getter on all fields, and @Setter on all non-final fields, and @RequiredArgsConstructor!
* **@Accessors** 
* * **fluent** – A boolean. If true, the getter for pepper is just pepper(), and the setter is pepper(T newValue). Furthermore, unless specified, chain defaults to true.
  Default: false.
* * **chain** – A boolean. If true, generated setters return this instead of void.
  Default: false, unless fluent=true, then Default: true.
* **@Getter/@Setter** - Generates getter/setter methods for the fields
* **@ToString** - Generates a toString method for the class
* **@EqualsAndHashCode** - Generates hashCode and equals implementations from the fields of your object


## How to run
* Based on the System property value of '**persistence**' key (_'inmemory'/'mysql'_) we can run the application with 
* **InMemory DB** - Data will persist till the application is running
* **MySql DB** - We can either run a local MySql server (**DB-_shop_, Root Pwd-_test_**) or run a Docker container

_docker run --name hexagon-mysql -d -p3306:3306 -e MYSQL_DATABASE=shop -e MYSQL_ROOT_PASSWORD=test mysql:8.1_
* The **'persistence'** Key value is used in _RestEasyUndertowShopApplication.initPersistenceAdapters()_
* You can invoke HTTP commands from '**documents/sample-requests.http**' directly from Intellij
