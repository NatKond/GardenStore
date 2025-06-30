CREATE TABLE IF NOT EXISTS app_users
(
    user_id         SERIAL PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    email           VARCHAR(255) UNIQUE NOT NULL,
    phone_number    VARCHAR(255),
    password_hash   VARCHAR(255) NOT NULL,
    role            VARCHAR(30) NOT NULL CHECK (role IN ('ROLE_USER', 'ROLE_ADMINISTRATOR'))
);

CREATE TABLE IF NOT EXISTS categories
(
    category_id     SERIAL PRIMARY KEY,
    name            VARCHAR(255) NULL
);

CREATE TABLE IF NOT EXISTS products
(
    product_id      SERIAL PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    discount_price  DECIMAL,
    price           DECIMAL NOT NULL,
    category_id     INTEGER NOT NULL,
    created_at      TIMESTAMPTZ,
    updated_at      TIMESTAMPTZ,
    description     TEXT,
    image_url       TEXT
);

CREATE TABLE IF NOT EXISTS favorites
(
    favorite_id     SERIAL PRIMARY KEY,
    user_id         INTEGER NOT NULL,
    product_id      INTEGER NOT NULL
);
