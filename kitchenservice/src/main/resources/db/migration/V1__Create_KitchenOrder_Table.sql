-- V1__Create_KitchenOrder_Table.sql
CREATE TABLE kitchen_order (
    id SERIAL PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL,
    price NUMERIC NOT NULL
);
