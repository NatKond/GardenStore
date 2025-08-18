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

### CategoryShortResponseDto

```json
{
  "categoryId": 1,
  "name": "string"
}
```
### CategoryResponseDto
```json
{
  "categoryId": 1,
  "name": "string",
  "products": [
    {
      "productId": 1,
      "name": "string",
      "description": "string",
      "price": 11.99,
      "discountPrice": 8.99
    },
    {
      "productId": 2,
      "name": "string",
      "description": "string",
      "price": 13.99,
      "discountPrice": 10.49
    }
  ]
}
```

## Endpoints

| Method | URL                            | Role Required | Description        |
|--------|--------------------------------|---------------|--------------------|
| GET    | `/v1/categories`               | —             | Get all categories |
| GET    | `/v1/categories/{category_id}` | —             | Get category by Id |
| POST   | `/v1/categories`               | ADMIN         | Create category    |
| PUT    | `/v1/categories/{categoryId}`  | ADMIN         | Update category    |
| DELETE | `/v1/categories/{categoryId}`  | ADMIN         | Delete category    |