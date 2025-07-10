# Entity: User

## Description
Represents a system user.

## Table
- Table name: `app_users`

## Fields

| Field       | Type   | DB Column     | Description                    |
|-------------|--------|---------------|--------------------------------|
| userId      | Long   | user_id       | Primary Key                    |
| name        | String | name          | Full name                      |
| email       | String | email         | Unique, required               |
| phoneNumber | String | phone_number  | Required                       |
| password    | String | password_hash | Hashed password                |
| role        | Enum   | role          | ROLE_USER / ROLE_ADMINISTRATOR |

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

| Method | URL                  | Role Required | Description         |
|--------|----------------------|---------------|---------------------|
| POST   | `/v1/users/register` | —             | Register user       |
| POST   | `/v1/users/login`    | —             | Authenticate user   |
| GET    | `/v1/users`          | ADMIN         | Get all users       |
| GET    | `/v1/users/{userId}` | USER / ADMIN  | Get user by ID      |
| PUT    | `/v1/users/{userId}` | USER / ADMIN  | Update user profile |
| DELETE | `/v1/users/{userId}` | USER / ADMIN  | Delete user         |