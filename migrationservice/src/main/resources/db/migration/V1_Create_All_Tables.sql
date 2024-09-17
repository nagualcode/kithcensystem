CREATE SCHEMA IF NOT EXISTS kitchenservice;

CREATE TABLE kitchenservice.kitchen_order (
    id SERIAL PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL,
    price NUMERIC NOT NULL
);

CREATE SCHEMA IF NOT EXISTS paymentservice;

CREATE TABLE paymentservice.payment (
    id SERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL
);

CREATE SCHEMA IF NOT EXISTS userservice;

CREATE TABLE userservice.users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL
);

CREATE SCHEMA IF NOT EXISTS menuservice;

CREATE TABLE menuservice.plates (
    id SERIAL PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    price NUMERIC(10, 2) NOT NULL
);

CREATE SCHEMA IF NOT EXISTS orderservice;

CREATE TABLE orderservice.orders (
    id SERIAL PRIMARY KEY,
    customer_name VARCHAR(255) NOT NULL,
    customer_email VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    total_price NUMERIC(10, 2) NOT NULL
);

CREATE TABLE orderservice.order_items (
    id SERIAL PRIMARY KEY,
    plate_description VARCHAR(255) NOT NULL,
    plate_price NUMERIC(10, 2) NOT NULL,
    order_id BIGINT,
    CONSTRAINT fk_order
        FOREIGN KEY(order_id) 
        REFERENCES orderservice.orders(id)
        ON DELETE CASCADE
);
