CREATE TABLE IF NOT EXISTS app_users (
    user_id IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone_number VARCHAR(255),
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(30) NOT NULL CHECK (role IN ('ROLE_USER', 'ROLE_ADMINISTRATOR'))
);

CREATE TABLE IF NOT EXISTS categories (
    category_id IDENTITY PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS products (
    product_id IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    discount_price DECIMAL,
    price DECIMAL NOT NULL,
    category_id INTEGER NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    description TEXT,
    image_url TEXT
);

CREATE TABLE IF NOT EXISTS favorites (
    favorite_id IDENTITY PRIMARY KEY,
    user_id INTEGER NOT NULL,
    product_id INTEGER NOT NULL
);
