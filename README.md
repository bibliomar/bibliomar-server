# bibliomar-server
The main backend/API for Bibliomar.

### Stack
- Java 17
- Spring Boot
- Spring Data
- Spring Security
- MySQL
- MongoDB
- ManticoreSearch (external dependency and configuration)

### What does it do?
This API is responsible for retrieving metadata/entries from the Bibliomar's MySQL database. It's also responsible for user authentication and user library handling.

## Installation

The easiest way to get up and running is to use the provided `docker-compose.yml`
It will automatically load your `.env` file and start the server.
```shell
$ docker-compose up -d
```

The compose file uses ports `8080` - `8082` for the API (3 instances).  
You should set up your reverse proxy to handle the requests to the API.  

You can use the `docker-to-systemd` file in this repo to quickly create a systemd service that starts runs the compose script.  


## Configuration
You need to set your `.env` file with all the required variables. A `.env.example` file is provided.

Keep in mind that the actual database setup is separate and not handled by this API. 
You need to setup your MySQL database and configure the connection strings in the `.env` file.

