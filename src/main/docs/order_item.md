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

### OrderItemResponseDto

```json
{
  "orderId": 1,
  "quantity": 2,
  "priceAtPurchase": 99.99,
  "product": {
    "productId": 1,
    "name": "string",
    "description": "string",
    "price": 9.49,
    "discountPrice": 6.99
  }
}
```

## Endpoints

| Method | URL                              | Role Required  | Description             |
|--------|----------------------------------|----------------|-------------------------|
| POST   | `/v1/orders/items`               | USER/ADMIN     | Add item to order       |
| PUT    | `/v1/orders/items/{orderItemId}` | USER/ADMIN     | Change item in order    |
| DELETE | `/v1/orders/items/{orderItemId}` | USER/ADMIN     | Delete items from order |
