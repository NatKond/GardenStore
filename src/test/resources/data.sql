INSERT INTO app_users (name, email, phone_number, password_hash, role)
VALUES
    ('Alice Johnson', 'alice.johnson@example.com', '+1234567890', '12345', 'ROLE_USER'),
    ('Bob Smith', 'bob.smith@example.com', '+1987654321', '12345', 'ROLE_USER');

INSERT INTO categories (name)
VALUES
    ('Fertilizer'),
    ('Protective products and septic tanks'),
    ('Tools and equipment');

INSERT INTO products (name, discount_price, price,category_id, description, image_url,created_at, updated_at)
VALUES
    ('All-Purpose Plant Fertilizer', 8.99, 11.99, 1, 'Balanced NPK formula for all types of plants', 'https://example.com/images/fertilizer_all_purpose.jpg','2025-07-01 00:00:00','2025-07-01 00:00:00'),
    ('Organic Tomato Feed', 10.49, 13.99, 1, 'Organic liquid fertilizer ideal for tomatoes and vegetables', 'https://example.com/images/fertilizer_tomato_feed.jpg','2025-07-01 00:00:00','2025-07-01 00:00:00'),
    ('Slug & Snail Barrier Pellets', 5.75, 7.50, 2, 'Pet-safe barrier pellets to protect plants from slugs', 'https://example.com/images/protection_slug_pellets.jpg','2025-07-01 00:00:00','2025-07-01 00:00:00');

INSERT INTO favorites(user_id, product_id)
VALUES
    (1, 1),
    (1, 2);

INSERT INTO carts (user_id)
VALUES
    (1),
    (2);

INSERT INTO cart_items (cart_id, product_id, quantity)
VALUES
    (1, 1, 2),
    (1, 2, 1),
    (2, 3, 1);

INSERT INTO orders (user_id, delivery_address, contact_phone, delivery_method, status, created_at, updated_at)
VALUES
    (1, '123 Garden Street', '+1234567890', 'COURIER', 'CREATED', '2025-07-01 10:00:00', '2025-07-01 10:30:00'),
    (2, '456 Green Ave', '+1987654321', 'PICKUP', 'CREATED', '2025-07-02 12:00:00', '2025-07-02 12:05:00');

INSERT INTO order_items (order_id, product_id, quantity, price_at_purchase)
VALUES
    (1, 1, 2, 8.99),
    (1, 2, 1, 10.49),
    (2, 3, 1, 5.75);
