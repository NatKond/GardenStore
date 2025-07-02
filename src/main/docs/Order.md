# Entity: Order

## Description
A customer's order.

## Table
- Table name: `orders`

## Fields

| Field           | Type          | DB Column        | Description                                                   |
|-----------------|---------------|------------------|---------------------------------------------------------------|
| orderId         | Long          | order_id         | Primary key                                                   |
| userId          | Long          | user_id          | Foreign key to `users(user_id)`                               |
| createdAt       | LocalDateTime | created_at       | Timestamp of order creation                                   |
| deliveryAddress | String        | delivery_address | Delivery address                                              |
| contactPhone    | String        | contact_phone    | Customer contact phone                                        |
| deliveryMethod  | String        | delivery_method  | Delivery method                                               |
| status          | Enum          | status           | Order status NEW / PROCESSING/ SHIPPED / DELIVERED / CANCELED |
| updatedAt       | LocalDateTime | updated_at       | Last update timestamp                                         |

## DTOs

### OrderCreateRequestDto

```json
{
  "userId": 3,
  "deliveryAddress": "string",
  "deliveryMethod": "string",
  "order_items": [
    {
      "productId": 1,
      "quantity": 2,
      "priceAtPurchase": 99.99
    }
  ]
}
```

### OrderResponseDto

```json
{
  "orderId": 1,
  "userId": 3,
  "deliveryAddress": "string",
  "deliveryMethod": "string",
  "order_items": [
    {
      "productId": 1,
      "quantity": 2,
      "priceAtPurchase": 99.99
    }
  ],
  "status": "NEW",
  "createdAt": "2024-01-01T12:00:00",
  "updatedAt": "2024-01-01T12:00:00"
}
```

## Endpoints

| Method | URL                 | Role Required  | Description      |
|--------|---------------------|----------------|------------------|
| GET    | `/orders/history`   | USER/ADMIN     | Get user orders  |
| GET    | `/orders/{orderId}` | USER/ADMIN     | Get order by ID  |
| POST   | `/orders`           | USER           | Create new order |
| PUT    | `/orders/{orderId}` | USER/ADMIN     | Update order     |
| DELETE | `/orders/{orderId}` | USER/ADMIN     | Cancel order     |
