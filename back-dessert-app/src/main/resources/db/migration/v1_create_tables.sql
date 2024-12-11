-- Crear tabla de descuentos
CREATE TABLE discounts (
                           id BIGSERIAL PRIMARY KEY,
                           code VARCHAR(50) NOT NULL,
                           description VARCHAR(255),
                           discount_percentage DECIMAL(5, 2),
                           valid_from DATE,
                           valid_to DATE
);

-- Crear tabla de estados
CREATE TABLE status (
                        id BIGSERIAL PRIMARY KEY,
                        label VARCHAR(50) NOT NULL
);

-- Crear tabla de usuarios
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       created_date TIMESTAMP NOT NULL DEFAULT NOW(),
                       email VARCHAR(100) UNIQUE NOT NULL,
                       enabled BOOLEAN DEFAULT TRUE,
                       first_name VARCHAR(50),
                       last_name VARCHAR(50),
                       password VARCHAR(255) NOT NULL,
                       token_expired BOOLEAN DEFAULT FALSE
);

-- Crear tabla de roles
CREATE TABLE role (
                      id BIGSERIAL PRIMARY KEY,
                      name VARCHAR(50) UNIQUE NOT NULL
);

-- Crear tabla de privilegios
CREATE TABLE privilege (
                           id BIGSERIAL PRIMARY KEY,
                           name VARCHAR(50) UNIQUE NOT NULL
);

-- Crear tabla intermedia de usuario-rol
CREATE TABLE user_role (
                           user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
                           role_id BIGINT REFERENCES role(id) ON DELETE CASCADE,
                           PRIMARY KEY (user_id, role_id)
);

-- Crear tabla intermedia de rol-privilegio
CREATE TABLE role_privilege (
                                role_id BIGINT REFERENCES role(id) ON DELETE CASCADE,
                                privilege_id BIGINT REFERENCES privilege(id) ON DELETE CASCADE,
                                PRIMARY KEY (role_id, privilege_id)
);

-- Crear tabla de categorías
CREATE TABLE categories (
                            id BIGSERIAL PRIMARY KEY,
                            name VARCHAR(50) NOT NULL
);

-- Crear tabla de productos
CREATE TABLE products (
                          id BIGSERIAL PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          flavour VARCHAR(50),
                          duration_days INT,
                          unit_price DECIMAL(10, 2),
                          image VARCHAR(255),
                          category_id BIGINT REFERENCES categories(id) ON DELETE SET NULL
);

-- Crear tabla de eventos
CREATE TABLE events (
                        id BIGSERIAL PRIMARY KEY,
                        name VARCHAR(100) NOT NULL,
                        image VARCHAR(255),
                        description VARCHAR(255)
);

-- Crear tabla de detalles de órdenes
CREATE TABLE orders (
                        id BIGSERIAL PRIMARY KEY,
                        order_date DATE NOT NULL,
                        deliver_date DATE,
                        total_price DECIMAL(10, 2),
                        delivery_instructions VARCHAR(255),
                        user_id BIGINT REFERENCES users(id),
                        status_id BIGINT REFERENCES status(id),
                        discount_id BIGINT REFERENCES discounts(id)
);

-- Crear tabla de detalles de órdenes
CREATE TABLE orders_details (
                                id BIGSERIAL PRIMARY KEY,
                                amount INT NOT NULL,
                                order_id BIGINT REFERENCES orders(id) ON DELETE CASCADE,
                                product_id BIGINT REFERENCES products(id) ON DELETE SET NULL
);

-- Crear tabla intermedia de eventos-productos
CREATE TABLE events_products (
                                 product_id BIGINT REFERENCES products(id) ON DELETE CASCADE,
                                 event_id BIGINT REFERENCES events(id) ON DELETE CASCADE,
                                 PRIMARY KEY (product_id, event_id)
);
