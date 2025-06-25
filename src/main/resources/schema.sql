CREATE TYPE user_role AS ENUM ('ROLE_USER', 'ROLE_ADMINISTRATOR');

CREATE TABLE IF NOT EXISTS app_users
(
    id              SERIAL PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    email           VARCHAR(255) NOT NULL,
    phone_number    VARCHAR(255),
    password_hash   VARCHAR(255) NOT NULL,
    role            user_role
);

CREATE TABLE IF NOT EXISTS categories
(
    id              SERIAL PRIMARY KEY,
    name            VARCHAR(255) NULL
);

CREATE TABLE IF NOT EXISTS products
(
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    discount_price DOUBLE PRECISION,
    price DOUBLE PRECISION,
    created_at TIMESTAMPTZ DEFAULT now(),
    updated_at TIMESTAMPTZ DEFAULT now(),
    description TEXT,
    image_url TEXT
);

CREATE TABLE IF NOT EXISTS favorites
(
    id SERIAL PRIMARY KEY
);
