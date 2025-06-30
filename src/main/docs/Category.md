# Entity: Category

## Description
Represents a product category.

## Table
- Table name: `categories`

## Fields

| Field      | Type   | DB Column     | Description          |
|------------|--------|---------------|----------------------|
| categoryId | Long   | category_id   | Primary Key          |
| name       | String | name          | Category name        |

## DTOs

### CategoryCreateRequestDto

```json
{
  "name": "string"
}
```

### CategoryResponseDto

```json
{
  "categoryId": 1,
  "name": "string"
}
```

## Endpoints

| Method | URL                          | Role Required | Description          |
|--------|------------------------------|---------------|----------------------|
| GET    | `/categories`                | —             | Get all categories   |
| GET    | `/categories/{categoryId}`   | —             | Get categories by id |
| POST   | `/categories`                | ADMIN         | Create category      |
| PUT    | `/categories/{icategoryIdd}` | ADMIN         | Update category      |
| DELETE | `/categories/{categoryId}`   | ADMIN         | Delete category      |