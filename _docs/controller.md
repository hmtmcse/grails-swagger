

responseToApi


1. list
2. details
3. create
4. update
5. delete
6. customQuery
7. customQueryAndResponse

1. apiDefinition Name should be same as Controller otherwise the swagger UI will not generate 
2. apiDefinition should have 5 definition method, the will allow to determine the response types




List
---
1. POST
    a. Where
    b. Pagination
    c. Order
    
2. GET
    a. propertyName, propertyValue
    b. Pagination
    c. Order

    
Read
---
1. POST
    a. Where Limited

    
2. GET
    a. propertyName, propertyValue


Create
---
1. POST
    a. Allowed filed can post


    
Update 
---
1. POST
    a. Limited Where
    b. Allowed filed can post

    
Delete 
---
1. DELETE
    a. Limited Where