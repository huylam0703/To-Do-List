# Todo List App

A simple Todo List application built for the Intern Developer test.  
The application allows users to manage daily tasks with basic CRUD features, search, filter, pagination, responsive UI, and Docker support.

## Features

- View todo list
- Create new todo
- Update todo
- Delete todo
- Mark todo as completed / pending
- Search todo by keyword
- Filter todo by status
- Pagination
- Responsive user interface
- Docker support
- PostgreSQL database

## Technologies

### Backend

- Java 21
- Spring Boot 3.5.0
- Spring Web
- Spring Data JPA
- PostgreSQL
- Maven
- Lombok
- MapStruct
- Jakarta Validation
- Docker

### Frontend

- ReactJS
- Vite
- Axios
- CSS
- Nginx
- Docker

### Database

- PostgreSQL

## Project Structure

```txt
todolist
├── src
│   └── main
│       ├── java
│       │   └── com.web.todolist
│       │       ├── controller
│       │       ├── dto
│       │       ├── entity
│       │       ├── exception
│       │       ├── mapper
│       │       ├── repository
│       │       ├── service
│       │       └── TodolistApplication.java
│       └── resources
│           └── application.yml
│
├── todolist-fe
│   ├── src
│   │   ├── api
│   │   ├── components
│   │   ├── pages
│   │   ├── App.jsx
│   │   └── main.jsx
│   ├── Dockerfile
│   ├── nginx.conf
│   └── package.json
│
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md
```

## Database Design

The project uses one main table: `tbl_todo`.

| Column | Type | Description |
|---|---|---|
| id | String / UUID | Primary key |
| title | varchar(100) | Todo title |
| description | varchar(500) | Todo description |
| completed | boolean | Todo status |
| created_at | timestamp | Created time |
| updated_at | timestamp | Updated time |

## API Endpoints

Base URL when running locally:

```txt
http://localhost:8080/api/v1/todo
```

Base URL when running with Docker:

```txt
http://localhost:8081/api/v1/todo
```

### Create Todo

```http
POST /create
```

Request body:

```json
{
  "title": "Learn Spring Boot",
  "description": "Practice Todo List API"
}
```

### Get Todo By Id

```http
GET /{todoId}
```

### Get Todo List With Search, Filter, Pagination

```http
GET /getTodo?pageNo=1&pageSize=10&keyword=learn&status=complete
```

Query parameters:

| Param | Description |
|---|---|
| pageNo | Current page number |
| pageSize | Number of items per page |
| keyword | Search keyword |
| status | Filter status |

Status values:

```txt
complete
incomplete
```

### Update Todo

```http
PUT /update/{todoId}
```

Request body:

```json
{
  "title": "Learn Docker",
  "description": "Run full project with Docker Compose",
  "completed": true
}
```

### Delete Todo

```http
DELETE /delete/{todoId}
```

### Toggle Todo Status

```http
PATCH /status/{todoId}
```

## Run Project With Docker

Make sure Docker Desktop is running.

From the root folder, run:

```bash
docker compose up --build
```

Or run in background:

```bash
docker compose up --build -d
```

After starting successfully:

```txt
Frontend: http://localhost:3000
Backend:  http://localhost:8081
Database: localhost:5433
```

Stop containers:

```bash
docker compose down
```

Stop containers and remove database volume:

```bash
docker compose down -v
```

## Docker Services

| Service | Description | Port |
|---|---|---|
| todo-frontend | React app served by Nginx | 3000:80 |
| todo-backend | Spring Boot API | 8081:8080 |
| todo-db | PostgreSQL database | 5433:5432 |

## Run Backend Locally

Requirements:

- Java 21
- Maven
- PostgreSQL

Create a PostgreSQL database:

```txt
todolist_db
```

Run backend:

```bash
mvn spring-boot:run
```

Backend will run at:

```txt
http://localhost:8080/api/v1
```

## Run Frontend Locally

Go to frontend folder:

```bash
cd todolist-fe
```

Install dependencies:

```bash
npm install
```

Run frontend:

```bash
npm run dev
```

Frontend will run at:

```txt
http://localhost:5173
```

## Run Tests

Run backend unit tests:

```bash
mvn test
```

## Validation

The application handles invalid input cases such as:

- Todo title must not be blank
- Todo title must not exceed 100 characters
- Todo description must not exceed 500 characters
- Todo not found
- Duplicate todo title

## Notes

- This project does not include login/register because authentication is not required in the test description.
- The main goal is to demonstrate clean code structure, CRUD implementation, validation, exception handling, API integration, and Docker usage.
- Docker Compose is included to run the full project with frontend, backend, and PostgreSQL database.