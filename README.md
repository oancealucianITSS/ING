# ing 
I used liquibase to create tables and MySql as database where it needs to create schema "ing" and credentials for connection are in application.yml.
(those can be changed, but also to actualize profile liquibase from pom.xml)
It should be checked liquibase profile, reload maven, and from maven bar in plugins select liquibase -> liquibase: update

We have 3 enpoints ( please find curls to use in Postman: 
1. /createUser :
curl --location --request POST 'http://localhost:8101/rest/api/db/createUser' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email": "lucian.oancea@itsmartsystems.eu",
    "password": "test",
    "firstName": "Oancea",
    "lastName": "Lucian",
    "isActive": true,
    "age": 10
}'


2. /login :
curl --location --request POST 'http://localhost:8101/rest/api/db/login' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email": "lucian.oancea@itsmartsystems.eu",
    "password": "test"
}'


3. /logout :
curl --location --request POST 'http://localhost:8101/rest/api/db/logout' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email": "lucian.oancea@itsmartsystems.eu"
}'

!! When it is  used /createUser endpoint we have on response userDetails (without password)
