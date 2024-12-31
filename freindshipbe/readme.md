to start up run
docker-compose up --build
check that both the db and the app are running in docker

can perform crud to
http://localhost:8080

using spring data rest.. therefor..

Spring Data REST automatically exposes the following endpoints for your Patient entity:

GET /patients - Retrieve all patients.
POST /patients - Create a new patient.
GET /patients/{id} - Retrieve a specific patient by ID.
PUT /patients/{id} - Update an existing patient.
DELETE /patients/{id} - Delete a specific patient.

example for post

post
http://localhost:8080/patients

raw json body

{
"firstName": "John",
"lastName": "Doe"
}

should return the above with an id..

If you want to add addition feilds..
using JpaRepository
so all you need to do is add feilds to the Patient entity class
then need to rebuild and then restart so..

mvn clean package

docker-compose up --build

and check that both db and app are running...

added swagger need to use this link
http://localhost:8080/swagger-ui/index.html

if that doesn't wotk then try
http://localhost:8080/v3/api-docs
for full docs
and aybe this will work
http://localhost:8080/swagger-ui/
