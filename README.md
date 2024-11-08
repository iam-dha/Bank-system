# Bank-system

# Add application.yml
```spring: 
    datasource:
        url: jdbc:postgresql://localhost:5432/{your database}
        username: {user database}
        password: {user password}
        driver-class-name: org.postgresql.Driver
    jpa:
        hibernate:
            ddl-auto: create-drop
        show-sql: true
        properties:
            hibernate:
                format_sql: true
        database: postgresql
        database-platform: org.hibernate.dialect.PostgreSQLDialect
```
# Use Postman
# First GET: http://localhost:8080/api/v1/auth/register with body
```
{
    "firstname": {First Name},
    "lastname": {Last Name},
    "email": {User's Email},
    "password": {User's Password}
}
```
# Second GET: http://localhost:8080/api/v1/auth/authenticate with body
```
{
     "email": {User's Email},
    "password": {User's Password}
}
```
# Copy Token Authentication 

# Last GET: http://localhost:8080/api/v1/demo-controller with Bearer Token