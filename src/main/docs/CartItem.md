# Entity: CartItem

## Description
Items added to a shopping cart.

## Table
- Table name: `cart_items`

## Fields

| Field      | Type | DB Column    | Description                           |
|------------|------|--------------|---------------------------------------|
| cartItemId | Long | cart_item_id | Primary key                           |
| cartId     | Long | cart_id      | Foreign key to `cart(cart_id)`        |
| productId  | Long | product_id   | Foreign key to `products(product_id)` |
| quantity   | Int  | quantity     | Quantity of the product in the cart   |

## DTOs

### CartItemCreateRequestDto

```json
{
  "productId": 1,
  "quantity": 2
}
```

### CartItemResponseDto

```json
{
  "cartItemId": 1,
  "cartId": 1,
  "quantity": 2,
  "product": {
    "productId": 5,
    "name": "string",
    "description": "string",
    "price": 9.49,
    "discountPrice": 6.99
  }
}
```

## Endpoints

| Method | URL                       | Role Required  | Description           |
|--------|---------------------------|----------------|-----------------------|
| POST   | `/v1/cart/items`          | USER           | Add item to cart      |
| PUT    | `/v1/cart/items/{itemId}` | USER           | Update item in cart   |
| DELETE | `/v1/cart/items/{itemId}` | USER           | Remove item from cart |
