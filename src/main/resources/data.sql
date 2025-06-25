TRUNCATE TABLE app_users RESTART IDENTITY;

INSERT INTO app_users (name, email, phone_number, password_hash, role)
VALUES
    ('Alice Johnson', 'alice.johnson@example.com', '+1234567890', 'hashed_password_1', 'ROLE_USER'),
    ('Bob Smith', 'bob.smith@example.com', '+1987654321', 'hashed_password_2', 'ROLE_USER'),
    ('Carol Lee', 'carol.lee@example.com', '+1122334455', 'hashed_password_3', 'ROLE_USER'),
    ('David Brown', 'david.brown@example.com', '+1222333444', 'hashed_password_4', 'ROLE_USER');

TRUNCATE TABLE products RESTART IDENTITY;
INSERT INTO products (name, discount_price, price, description, image_url)
VALUES
    ('Garden Trowel', 15.99, 19.99, 'Sturdy garden trowel with wooden handle', 'https://example.com/images/garden_trowel.jpg'),
    ('Pruning Shears', 35.99, 39.99, 'Heavy-duty pruning shears for trimming bushes and small branches', 'https://example.com/images/pruning_shears.jpg'),
    ('Gas Lawn Mower', 299.99, 349.99, 'Gas-powered lawn mower with 21-inch cutting deck', 'https://example.com/images/lawn_mower.jpg'),
    ('New Gas Lawn Mower', 699.99, 849.99, 'Gas-powered lawn mower with 21-inch cutting deck', 'https://example.com/images/lawn_mower.jpg');

TRUNCATE TABLE categories RESTART IDENTITY;
INSERT INTO categories (name)
VALUES
    ('Garden Tools'),
    ('Outdoor Power Equipment'),
    ('Watering Equipment'),
    ('Domestic Chemicals');

TRUNCATE TABLE favorites RESTART IDENTITY;
INSERT INTO favorites DEFAULT VALUES;
INSERT INTO favorites DEFAULT VALUES;
INSERT INTO favorites DEFAULT VALUES;