-- Criando tabelas para cada serviço em seus respectivos esquemas

-- Schema: kitchenservice
CREATE TABLE kitchenservice.kitchen_order (
    id SERIAL PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL,
    price NUMERIC NOT NULL
);

-- Schema: paymentservice
CREATE TABLE paymentservice.payment (
    id SERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL
);

-- Schema: userservice
CREATE TABLE userservice.users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL
);

-- Schema: menuservice
CREATE TABLE menuservice.plates (
    id SERIAL PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    price NUMERIC(10, 2) NOT NULL
);

-- Schema: orderservice
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
