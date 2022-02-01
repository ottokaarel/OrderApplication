Course project for "Veebirakendused Java baasil"

Web application for receiving orders in JSON format, validating & saving them to database and providing CRUD operations.

Tellimuse sisestamise päringu näide:


POST /api/orders HTTP/1.1

Host: localhost:8080

Content-Type: application/json

{ 
    "orderNumber": "A456",
    "orderRows":[
                    {"itemName":"CPU","quantity":2,"price":100},
                    {"itemName":"Motherboard","quantity":3,"price":60}
                ]
}

Vastuse näide:

HTTP/1.1 200 OK

Content-Type: application/json

{ 
    "id": 5,
    "orderNumber": "A456",
    "orderRows":[
                    {"itemName":"CPU","quantity":2,"price":100},
                    {"itemName":"Motherboard","quantity":3,"price":60}
                ]
}

Later on authentication and authorization was implemented.
Some Kotlin was also tried out at a later stage.

No front-end
REST controller was tested against Postman.
 
Covered topics:
Servlet
Jdbc
Spring Core
Spring MVC
JPA
Spring Security

