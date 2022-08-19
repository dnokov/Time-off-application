# <a href="https://pics.me.me/me-and-the-boys-when-the-defect-isnt-reproducible-it-57360463.png"><span style="color:#8150C7">Java RuntimeErrors</span></a>


### Time off request management application

Rest API for Time off request Management Application.

## Features

- Login
- User Management
- Team Management
- Time Off Management

## Used Tools

- Java 11
- Spring Boot 2.5.2
    - Spring Boot Web
    - Spring Boot Test
    - Spring Security
    - Spring Boot Starter Mail
- Maven
- MySQL
- JUnit 5
- Lombok
- JWT
- Docker
- Swagger
- Mapstruct
- Calendarific API

### Additional features used

- According to the GDPR rules, scheduling with the help of cron jobs

## Running without Docker

1. [Download](https://gitlab.com/scalefocus-academy/students/sa21-java/java-runtimeerrors.git) the code from the repository.
2. Install MySQL and MySQL workbench / start local MySQL
   server on port 3306 and log in with username
   <b><i>root</i></b> and password <b><i>root</i></b>.
3. Use Maven clean install to build .jar file.
4. Open the application using the built .jar file.

## Rest API Documentation

### Authentication

Method | URL | Description | Request | Response |
--- | --- | --- | --- | ---
POST | /login | Login | Provide JSON with e-mail and password | Token

### User Management

Method | URL | Description | Request | Response |
--- | --- | --- | --- | ---
GET | /users | List all users |  | JSON
GET | /users/{userId} | List user | Path variable userId  | JSON
POST | /users | Save user | Provide JSON | JSON
PUT | /users/{userId} | Update user | Provide Path variable userId and JSON | JSON
DELETE | /users/{userId} | Delete user | Provide Path variable userId |
POST | /users/{userId}/unemploy | Unemployed | Provide path variable | JSON
POST | /users/{userId}/setadmin | Set admin | Provide path variable ID | JSON
### Team Management

Method | URL | Description | Request | Response |
--- | --- | --- | --- | ---
GET | /teams | List all teams |  | JSON
GET | /teams/{teamId} | List team | Path variable teamId  | JSON
POST | /teams | Save team | Provide JSON | JSON
PUT | /teams/{teamId} | Update team | Provide Path variable teamId and JSON | JSON
DELETE | /teams/{teamId} | Delete team | Provide Path variable teamId |
POST | /teams/{teamId}/{userId} | Assign user to team | Provide Path variables teamId and userId |
DELETE | /teams/{teamId}/{userId} | Remove a member from a team | Provide Path variables teamId and userId |
PATCH | /teams/{teamId}/{userId} | Set team leader | Provide Path variables teamId and userId |
GET | /teams/{id}/members | List all members | Provide Path variable teamId | JSON

### Time Off Management

Method | URL | Description | Request | Response |
--- | --- | --- | --- | ---
POST | /timeOffs | Create a time off |
GET | /timeOffs/{timeOffId} | Get a specific time off | Path variable time off Id | JSON
DELETE | /timeOffs/{timeOffId} | Delete a time off | Provide Path Variable time off Id
PUT | /timeOffs/{timeOffId} | Update time off | Provide Path Variable time off Id and Request body | JSON
PATCH | /timeOffs/send/{timeOffId}| Send time off for approval | Provide Path variable projectId | JSON
PATCH | /timeOffs/{timeOffId}/ | Approve / Reject time off | Provide Path variable projectId and Request Parameter value |

## Requests
For reference, after application start, please check:
> http://localhost:8080/swagger-ui/

## How to run application on Docker
1. Install Docker
2. Navigate to project folder where docker-compose.yaml file is and open
   terminal by choice
3. Run
- for Windows machine
  > docker compose up
- for Linux machine [install](https://docs.docker.com/compose/install/) manually docker compose and run
  
  > docker-compose up
4. Test the application in Postman / Swagger or other preferred
   software
5. After finishing testing, optionally you can stop the
   container by navigating to the project folder and running:
   > docker-compose stop && docker-compose rm -f

