# birthdaySB
Spring Boot REST API for storage of birthdays

[swagger URL ](http://localhost:8081/swagger-ui/index.html)
##profiles
###prod
It works with PostgreSQL database. Environment variables needs to be set: JDBC_DATABASE_USERNAME, JDBC_DATABASE_PASSWORD,JDBC_DATABASE_URL
###dev
It works with in memory H2 database. data.sql contains initial data.