# Entity: User

## Description
Represents a system user.

## Table
- Table name: `app_users`

## Fields

| Field        | Type   | DB Column     | Description                    |
|--------------|--------|---------------|--------------------------------|
| userId       | Long   | user_id       | Primary Key                    |
| name         | String | name          | Full name                      |
| email        | String | email         | Unique, required               |
| phoneNumber  | String | phone_number  | Required                       |
| passwordHash | String | password_hash | Hashed password                |
| role         | Enum   | role          | ROLE_USER / ROLE_ADMINISTRATOR |

## DTOs

### UserCreateRequestDto

```json
{
  "name": "string",
  "email": "string",
  "phoneNumber": "string",
  "password": "string"
}
```

### UserResponseDto

```json
{
  "userId": 1,
  "name": "string",
  "role": "ROLE_USER"
}
```

### LoginRequest

```json
{
  "email": "string",
  "password": "string"
}
```

## Endpoints

| Method | URL                | Role Required | Description         |
|--------|--------------------|---------------|---------------------|
| POST   | `/users/register`  | —             | Register user       |
| POST   | `/users/login`     | —             | Authenticate user   |
| GET    | `/users`           | ADMIN         | Get all users       |
| GET    | `/users/{userId}`  | USER / ADMIN  | Get user by ID      |
| PUT    | `/users/{userId}`  | USER / ADMIN  | Update user profile |
| DELETE | `/users/{userId}`  | USER / ADMIN  | Delete user         |