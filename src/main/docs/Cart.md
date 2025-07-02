# Entity: Cart

## Description
Represents a user's shopping cart.

## Table
- Table name: `carts`

## Fields

| Field   | Type | DB Column | Description                         |
|---------|------|-----------|-------------------------------------|
| cartId  | Long | cart_id   | Primary key                         |
| userId  | Long | user_id   | Foreign key to `users(user_id)`     |

## DTOs

### CartCreateRequestDto

```json
{
  "userId": 2
}
```

### CartResponseDto

```json
{
  "cartId": 1,
  "userId": 2
}
```

## Endpoints

| Method | URL                | Role Required | Description     |
|--------|--------------------|---------------|-----------------|
| GET    | `/cart/{cartId}`   | USER          | Get cart by ID  |
| GET    | `/cart`            | ADMIN         | Get all carts   |
| POST   | `/cart`            | USER          | Create new cart |
| DELETE | `/cart/{icartIdd}` | ADMIN         | Delete cart     |
