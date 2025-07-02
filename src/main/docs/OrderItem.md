# Entity: OrderItem

## Description
A product included in an order.

## Table
- Table name: `order_items`

## Fields

| Field           | Type       | DB Column           | Description                                  |
|-----------------|------------|---------------------|----------------------------------------------|
| orderItemId     | Long       | order_item_id       | Primary key                                  |
| orderId         | Long       | order_id            | Foreign key to `orders(order_id)`            |
| productId       | Long       | product_id          | Foreign key to `products(product_id)`        |
| quantity        | Int        | quantity            | Quantity of product in the order             |
| priceAtPurchase | BigDecimal | price_at_purchase   | Product price at the time of purchase        |

## DTOs

### OrderItemCreateDto

```json
{
  "orderId": 1,
  "productId": 1,
  "quantity": 2,
  "priceAtPurchase": 99.99
}
```

### OrderItemResponseDto

```json
{
  "orderId": 1,
  "productId": 1,
  "quantity": 2,
  "priceAtPurchase": 99.99
}
```

## Endpoints

| Method | URL                           | Role Required  | Description             |
|--------|-------------------------------|----------------|-------------------------|
| GET    | `/orders/{orderId}/items`     | USER/ADMIN     | Get items from order    |
| POST   | `/orders/{orderId}/items`     | USER/ADMIN     | Add item to order       |
| PUT    | `/orders/items/{orderItemId}` | USER/ADMIN     | Change item in order    |
| DELETE | `/orders/items/{orderItemId}` | USER/ADMIN     | Delete items from order |
