TRUNCATE TABLE app_users RESTART IDENTITY CASCADE;
TRUNCATE TABLE products RESTART IDENTITY CASCADE;
TRUNCATE TABLE categories RESTART IDENTITY CASCADE;


INSERT INTO app_users (name, email, phone_number, password_hash, role)
VALUES
    ('Alice Johnson', 'alice.johnson@example.com', '+1234567890', '12345', 'ROLE_USER'),
    ('Bob Smith', 'bob.smith@example.com', '+1987654321', '12345', 'ROLE_USER'),
    ('Carol Lee', 'carol.lee@example.com', '+1122334455', '12345', 'ROLE_USER'),
    ('David Brown', 'david.brown@example.com', '+1222333444', '12345', 'ROLE_USER');

INSERT INTO categories (name)
VALUES
    ('Fertilizer'),
    ('Protective products and septic tanks'),
    ('Planting material'),
    ('Tools and equipment'),
    ('Pots and planters');

INSERT INTO products (name, discount_price, price,category_id, description, image_url)
VALUES
('All-Purpose Plant Fertilizer', 8.99, 11.99, 1, 'Balanced NPK formula for all types of plants', 'https://example.com/images/fertilizer_all_purpose.jpg'),
('Organic Tomato Feed', 10.49, 13.99, 1, 'Organic liquid fertilizer ideal for tomatoes and vegetables', 'https://example.com/images/fertilizer_tomato_feed.jpg'),
('Slug & Snail Barrier Pellets', 5.75, 7.50, 2, 'Pet-safe barrier pellets to protect plants from slugs', 'https://example.com/images/protection_slug_pellets.jpg'),
('Septic Tank Activator Powder', 12.95, 16.00, 2, 'Enzyme-based powder to maintain septic tank health', 'https://example.com/images/septic_tank_activator.jpg'),
('Tulip Bulb Mix (10 pcs)', 6.99, 9.49, 3, 'Colorful tulip bulbs perfect for spring blooms', 'https://example.com/images/tulip_bulbs.jpg'),
('Seed Pack - Carrots (Heirloom)', 2.95, 3.99, 3, 'Non-GMO heirloom carrot seeds for rich harvests', 'https://example.com/images/carrot_seeds.jpg'),
('Electric Hedge Trimmer', 49.95, 64.99, 4, 'Cordless hedge trimmer with rechargeable battery', 'https://example.com/images/hedge_trimmer.jpg'),
('Garden Tool Set (5 pcs)', 19.99, 24.99, 4, 'Essential hand tools set for everyday gardening', 'https://example.com/images/garden_tool_set.jpg'),
('Ceramic Plant Pot - 8 inch', 14.50, 18.99, 5, 'Stylish ceramic pot with drainage hole', 'https://example.com/images/ceramic_pot.jpg'),
('Hanging Planter Basket', 9.25, 12.50, 5, 'Woven hanging basket with metal chain', 'https://example.com/images/hanging_planter.jpg');

INSERT INTO favorites(user_id, product_id)
VALUES
    (1, 5),
    (1, 10),
    (2, 3),
    (3, 1),
    (3, 4),
    (4, 9),
    (4, 6);

INSERT INTO carts (user_id)
VALUES
    (1),
    (2),
    (3),
    (4);

INSERT INTO cart_items (cart_id, product_id, quantity)
VALUES
    (1, 1, 2),
    (1, 2, 1),
    (2, 1, 3);

INSERT INTO orders (user_id, delivery_address, contact_phone, delivery_method, status, created_at, updated_at)
VALUES
    (1, '123 Garden Street', '+1234567890', 'COURIER', 'AWAITING_PAYMENT', '2024-07-01 10:00:00', '2024-07-01 10:30:00'),
    (2, '456 Green Ave', '+1987654321', 'PICKUP', 'CREATED', '2024-07-02 12:00:00', '2024-07-02 12:05:00');

INSERT INTO order_items (order_id, product_id, quantity, price_at_purchase)
VALUES
    (1, 1, 2, 8.99),
    (1, 2, 1, 10.49),
    (2, 3, 1, 5.75);
