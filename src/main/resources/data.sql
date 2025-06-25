TRUNCATE TABLE app_users RESTART IDENTITY;

INSERT INTO app_users (name, email, phone_number, password_hash, role) VALUES
    ('Alice Johnson', 'alice.johnson@example.com', '+1234567890', 'hashed_password_1', 'ROLE_USER'),
    ('Bob Smith', 'bob.smith@example.com', '+1987654321', 'hashed_password_2', 'ROLE_USER'),
    ('Carol Lee', 'carol.lee@example.com', '+1122334455', 'hashed_password_3', 'ROLE_USER'),
    ('David Brown', 'david.brown@example.com', '+1222333444', 'hashed_password_4', 'ROLE_USER');