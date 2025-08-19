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

### ProductShortResponseDto

```json
{
  "productId": 1,
  "name": "string",
  "description": "string",
  "price": 11.99,
  "discountPrice": 8.99,
  "imageUrl": "string"
}
```
## Endpoints

| Method | URL                                                      | Role Required | Description                     |
|--------|----------------------------------------------------------|---------------|---------------------------------|
| GET    | `/v1/products`                                           | —             | Get all products with filtering |
| GET    | `/v1/products/{productId}`                               | —             | Get product by ID               |
| GET    | `/v1/products/product-of-the-day`                        | —             | Get product of the day          |
| POST   | `/v1/products`                                           | ADMIN         | Create new product              |
| PUT    | `/v1/products/{productId}`                               | ADMIN         | Update product                  |
| PATCH  | `/v1/products/{productId}/discount/{discountPercentage}` | ADMIN         | Update product discount         |
| DELETE | `/v1/products/{productId}`                               | ADMIN         | Delete product                  |