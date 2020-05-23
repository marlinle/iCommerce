# NAB iCommerce Microservice Demo

Example implementation for some operations of 2 backend REST API : **Product** & **User** services. *Audit log REST resource* is organized in Product service in order to demonstrate inter-service communication between those 2 services (i.e. User service will call Product service for audit logging). 

## Folder Structure

The project structure follows **Maven** convention as below:
```bash
├── src
│   ├── main
│   │   ├── java
│   │   │   ├── au.com.nab.demo
│   │   │   │   ├── domain
│   │   │   │   │   ├── *.java
│   │   │   │   ├── repo
│   │   │   │   │   ├── *.java
│   │   ├── resource
│   │   │   ├── application.properties
│   ├── test
│   │   ├── java
│   │   │   ├── au.com.nab.demo
│   │   │   │   ├── *.java
│   │   ├── resource
│   │   │   ├── application.properties
├── target
│   ├── **
│   ├── *.jar
├── Dockerfile
├── .dockerignore
├── docker-compose.yml
├── README.md
├── pom.xml
├── Entity Relation Diagram.jpg                 # ERD in PostgreSQL
├── Serverless Design.jpg                       # Serverless Architecture on AWS
├── test.sh                                     # curl test commands
├── *
├── users                                       # new Maven module for User service
│   ├── src
│   │   ├── main
│   │   │   ├── java
│   │   │   │   ├── au.com.nab.users
│   │   │   │   │   ├── domain
│   │   │   │   │   │   ├── *.java
│   │   │   │   │   ├── repo
│   │   │   │   │   │   ├── *.java
│   │   │   ├── resource
│   │   │   │   ├── application.properties
│   │   ├── test
│   │   │   ├── java
│   │   │   │   ├── au.com.nab.users
│   │   │   │   │   ├── *.java
│   │   │   ├── resource
│   │   │   │   ├── application.properties
│   ├── target
│   │   ├── **
│   │   ├── *.jar
│   ├── Dockerfile
│   ├── .dockerignore
│   ├── pom.xml
│   ├── *
```

### Library & Frameworks

1. Spring Boot version *2.3.0.RELEASE*
    * Spring Data JPA
    * Spring Data REST
    * Spring Test
    * Spring Actuator
2. PostgresQL
3. H2
4. Lombok
5. Jackson Databind
6. JUnit
7. Maven
8. Docker Compose

### Entity Relation Diagram

![ERD on PostgreSQL](Entity%20Relation%20Diagram.jpg)

### Local building & testing

Assume $root_dir is the root directory contains the downloaded iCommerce repo from GitHub.
    
1. **Maven** packaging for 2 services:

    ```bash
    # entering the project directory
    cd $root_dir/iCommerce
    mvn clean package
    
    # next to user service
    cd users
    mvn clean package
    ```

2. Run **docker-compose**
    ```bash
    # already entering the project directory in the above step

    # build 2 service docker images locally  
    docker-compose build

    # run postgres on 5432, product service on 8080, user service on 8081. Can choose to run as deamon or not via option -d
    docker-compose up -d

    # do the testing ...

    # clean up when finish testing
    docker-compose down
    docker-compose rm
    ```

3. Running the tests

    Product service derives from Spring Data Rest **PagingAndSortingRepository** that already provides so many CRUD, Paging and Sorting endpoints.
User service derives from **CrudRepository**

   ```bash
   # ---------- Test product service ----------
   
   # Add some products
   curl -i -H "Content-Type:application/json" -d '{"name": "Fossil Q", "price":"100", "brand": "fossil", "color":"red"}' http://localhost:8080/products
   curl -i -H "Content-Type:application/json" -d '{"name": "Fossil Q", "price":"100", "brand": "fossil", "color":"blue"}' http://localhost:8080/products
   curl -i -H "Content-Type:application/json" -d '{"name": "Skagen Sport", "price":"120", "brand": "skagen", "color":"red"}' http://localhost:8080/products
   curl -i -H "Content-Type:application/json" -d '{"name": "Skagen Sport", "price":"120", "brand": "skagen", "color":"blue"}' http://localhost:8080/products
   
   # Get all products
   curl -i http://localhost:8080/products
   # Get all products, paging with size of each page
   curl -i http://localhost:8080/products?size=2
   # Get all products, paging with size of each page, and specific page (page 0 & page 1)
   curl -i http://localhost:8080/products?page=1&size=2
   # Get all products, sort by color in desc
   curl -i http://localhost:8080/products/?sort=color,desc
   
   # Find products by name
   curl -i http://localhost:8080/products/search/findByName?name=Fossil%20Q
   # Find products by name
   curl -i http://localhost:8080/products/search/findByBrand?brand=fossil
   # Find products by name
   curl -i http://localhost:8080/products/search/findByColor?color=red
   # Find products by price between (priceLow, priceHigh)
   curl -i "http://localhost:8080/products/search/findByPriceBetween?priceLow=100&priceHigh=110"
   
   # Get all audit logs
   curl -i http://localhost:8080/logEntries
   # Manually insert 1 audit log entry
   curl -i -H "Content-Type:application/json" -d '{"timestamp": "2020-05-21 10:00:00.123", "severity":"INFO", "service": "product", "action":"findAll", "result":"200 OK"}' http://localhost:8080/logEntries
   
   # ---------- Test user service ----------
   
   # Get all user profiles
   curl -i http://localhost:8081/users
   # Manually insert 1 user profile
   curl -i -H "Content-Type:application/json" -d '{"fbId": "1234567890", "firstName":"Marlin", "lastName": "Le", "gender":"MALE", "age":37}' http://localhost:8081/users
   ```

### Serverless Architecture in AWS

![Serverless Design](Serverless%20Design.jpg)

1. iCommerce site

    Written in **ReactJS**, the iCommerce site is just a collection of static files (HTML, CSS, JS, images, ...). As the result, those files can be statically hosted on **AWS S3# with appropriate configuration (e.g Public Access, DNS bucket, Region, Lifecycle rules, ...). We also leverage **AWS Cloundfront** for CDN feature.

2. Authentication and User Identity

    iCommerce site can use various authentication libaries from Google, Facebook, ... in order to indentify their Users. Thank to **AWS Cognito User Pool**, all user IDs from those IdPs (i.e. Identity Providers) will be transformed into a **uniform one** in our system. Those unified user ID are primary keys thoughout our system.
We can even build our own IdP based on open Authen Protocols like **SAML, OpenID** to support even more users worldwide.

3. Backend RESTful API

    Leveraging **AWS API Gateway**, we can build up REST APIs that the frontend site need to access directly. It's fully managed and we can set them up by clicking only, (i.e. no coding, no deploying, no devops,...). The actual business logic is on **AWS Lambda** function that require only processing code for input events & output results (i.e no boilerplate codes to hulk them up).

4. NoSQL DynamoDB

    Thank to **DynamoDB** constant latency no matter what scale the site is operating, the whole system is stable and cost effective in long term ranging from hundreds to millions users around the globe without reconsidering the design for scaling.
