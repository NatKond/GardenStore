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

| Method | URL                           | Role Required | Description          |
|--------|-------------------------------|---------------|----------------------|
| GET    | `/v1/categories`              | —             | Get all categories   |
| POST   | `/v1/categories`              | ADMIN         | Create category      |
| PUT    | `/v1/categories/{categoryId}` | ADMIN         | Update category      |
| DELETE | `/v1/categories/{categoryId}` | ADMIN         | Delete category      |