# Entity: Product

## Description
Represents a product in the catalog.

## Table
- Table name: `products`

## Fields

| Field         | Type          | DB Column      | Description                              |
|---------------|---------------|----------------|------------------------------------------|
| productId     | Long          | product_id     | Primary Key                              |
| name          | String        | name           | Product name                             |
| description   | String        | description    | Product description                      |
| price         | BigDecimal    | price          | Product price                            |
| discountPrice | BigDecimal    | discount_price | Discount price (nullable)                |
| categoryId    | Long          | category_id    | Foreign key to `categories(category_id)` |
| imageUrl      | String        | image_url      | Product image URL                        |
| createdAt     | LocalDateTime | created_at     | Creation timestamp                       |
| updatedAt     | LocalDateTime | updated_at     | Last update timestamp                    |


## DTOs

### ProductCreateRequestDto

```json
{
  "name": "string",
  "description": "string",
  "price": 1000,
  "discountPrice": 900,
  "categoryId": 2,
  "imageUrl": "https://example.com/image.png"
}
```

### ProductResponseDto

```json
{
  "productId": 1,
  "name": "string",
  "description": "string",
  "price": 1000,
  "discountPrice": 900,
  "categoryId": 2,
  "imageUrl": "https://example.com/image.png",
  "createdAt": "2024-01-01T12:00:00",
  "updatedAt": "2024-01-01T12:00:00"
}
```

## Endpoints

| Method | URL                     | Role Required | Description             |
|--------|-------------------------|---------------|-------------------------|
| GET    | `/products`             | —             | Get all products        |
| GET    | `/products/{productId}` | —             | Get product by ID       |
| POST   | `/products`             | ADMIN         | Create new product      |
| PUT    | `/products/{productId}` | ADMIN         | Update product          |
| DELETE | `/products/{productId}` | ADMIN         | Delete product          |