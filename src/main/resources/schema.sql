CREATE TABLE IF NOT EXISTS app_users
(
    user_id         SERIAL PRIMARY KEY NOT NULL,
    name            VARCHAR(255) NOT NULL,
    email           VARCHAR(255) UNIQUE NOT NULL,
    phone_number    VARCHAR(255),
    password_hash   VARCHAR(255) NOT NULL,
    role            VARCHAR(30) NOT NULL CHECK (role IN ('ROLE_USER', 'ROLE_ADMIN'))
);

CREATE TABLE IF NOT EXISTS categories
(
    category_id     SERIAL PRIMARY KEY NOT NULL,
    name            VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS products
(
    product_id      SERIAL PRIMARY KEY NOT NULL,
    name            VARCHAR(255) NOT NULL,
    discount_price  DECIMAL,
    price           DECIMAL NOT NULL,
    category_id     INTEGER NOT NULL,
    created_at      TIMESTAMPTZ DEFAULT NOW(),
    updated_at      TIMESTAMPTZ DEFAULT NOW(),
    description     TEXT,
    image_url       TEXT,

    CONSTRAINT fk_category
        FOREIGN KEY (category_id)
        REFERENCES categories (category_id)
        ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS favorites
(
    favorite_id     SERIAL PRIMARY KEY NOT NULL,
    user_id         INTEGER NOT NULL,
    product_id      INTEGER NOT NULL,

    CONSTRAINT uc_user_product UNIQUE (user_id, product_id),

    CONSTRAINT fk_user
        FOREIGN KEY (user_id)
            REFERENCES app_users (user_id)
            ON DELETE CASCADE,

    CONSTRAINT fk_product
        FOREIGN KEY (product_id)
            REFERENCES products (product_id)
            ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_products_category_id ON products (category_id);
CREATE INDEX IF NOT EXISTS idx_favorites_user_id ON favorites (user_id);
CREATE INDEX IF NOT EXISTS idx_favorites_product_id ON favorites (product_id);
