DROP TYPE user_role;


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