# Birthday Spring Boot Application
Spring Boot REST API for storage of people with its birthdays

[swagger URL ](http://localhost:8081/swagger-ui/index.html)

## profiles

### prod
It works with PostgreSQL database. Environment variables needs to be set: JDBC_DATABASE_USERNAME, JDBC_DATABASE_PASSWORD,JDBC_DATABASE_URL.

ddl-auto: update is set. Flyway is not implemented.
### dev, test
It works with in memory H2 database. data.sql contains initial data.


##Authorisation
Request /register creates new user with defined password.
Request /login returns jwt token, if login is successful.


## Unit tests
They are started with test profile.

PersonRESTTests: REST API is called directly against H2 database. 

PersonControllerAutowiredTests: Controller is tested against H2 database.

PersonControllerMockTests: Controller is tested with mocked Repository.

## Open points
Localization is not taken into consideration.
Flyway