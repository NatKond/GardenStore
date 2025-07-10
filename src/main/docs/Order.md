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
  "orderItems": [
    {
      "productId": 1,
      "quantity": 2,
      "priceAtPurchase": 8.99
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
  "orderItems": [
    {
      "productId": 1,
      "quantity": 2,
      "priceAtPurchase": 8.99
    },
    {
      "productId": 2,
      "quantity": 1,
      "priceAtPurchase": 10.49
    }
  ],
  "status": "CREATED"
}
```

## Endpoints

| Method | URL                              | Role Required | Description                             |
|--------|----------------------------------|---------------|-----------------------------------------|
| GET    | `/v1/orders/history`             | USER/ADMIN    | Get all user orders                     |
| GET    | `/v1/orders/{orderId}`           | USER/ADMIN    | Get order by ID                         |
| POST   | `/v1/orders`                     | USER          | Create new order                        |
| POST   | `/v1/orders/items`               | USER          | Add item to order                       |
| PUT    | `/v1/orders/items/{orderItemId}` | USER          | Change item in order                    |
| DELETE | `/v1/orders/items/{orderItemId}` | USER          | Delete items from order                 |
| DELETE | `/v1/orders/{orderId}`           | USER/ADMIN    | Cancel order : mark order as CANCELLED. |
