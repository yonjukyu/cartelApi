-- Insert test users with encrypted passwords
-- Password for all users: admin123

INSERT INTO users (username, password, email, code_name, role, phone_number, territory, is_active, created_at, updated_at) VALUES
                                                                                                                               ('admin', '$2a$10$N.XW8T8jY5XrBSYZsQy7.eKmC5nRz.1OBPZJm1AjB7GrDJmB1EgL6', 'admin@cartel.com', 'El Jefe', 'ADMIN', '+1234567890', 'Headquarters', true, NOW(), NOW()),
                                                                                                                               ('boss1', '$2a$10$N.XW8T8jY5XrBSYZsQy7.eKmC5nRz.1OBPZJm1AjB7GrDJmB1EgL6', 'boss@cartel.com', 'El Patrón', 'BOSS', '+1234567891', 'North Territory', true, NOW(), NOW()),
                                                                                                                               ('lieutenant1', '$2a$10$N.XW8T8jY5XrBSYZsQy7.eKmC5nRz.1OBPZJm1AjB7GrDJmB1EgL6', 'lt1@cartel.com', 'Teniente', 'LIEUTENANT', '+1234567892', 'East District', true, NOW(), NOW()),
                                                                                                                               ('user1', '$2a$10$N.XW8T8jY5XrBSYZsQy7.eKmC5nRz.1OBPZJm1AjB7GrDJmB1EgL6', 'user1@cartel.com', 'Soldado', 'USER', '+1234567893', 'South Zone', true, NOW(), NOW());

-- Insert test products
INSERT INTO products (name, code_name, product_type, description, price_per_unit, unit_measure, origin_country, purity_level, is_available, created_at, updated_at) VALUES
                                                                                                                                                                        ('Premium White', 'PW001', 'POWDER', 'High quality white powder', 250.00, 'gram', 'Colombia', 95, true, NOW(), NOW()),
                                                                                                                                                                        ('Green Gold', 'GG002', 'HERBS', 'Premium green herbs', 50.00, 'gram', 'Mexico', 85, true, NOW(), NOW()),
                                                                                                                                                                        ('Blue Dreams', 'BD003', 'PILLS', 'Blue tablets', 15.00, 'piece', 'Netherlands', 90, true, NOW(), NOW());

-- Insert test warehouses
INSERT INTO warehouses (name, code_name, address, city, country, capacity, security_level, is_active, manager_id, created_at, updated_at) VALUES
                                                                                                                                              ('Central Storage', 'CS001', '123 Industrial Blvd', 'Miami', 'USA', 10000, 8, true, 3, NOW(), NOW()),
                                                                                                                                              ('Northern Depot', 'ND002', '456 Warehouse Ave', 'Houston', 'USA', 15000, 9, true, 3, NOW(), NOW());

-- Insert test inventory
INSERT INTO inventory (product_id, warehouse_id, quantity, reserved_quantity, minimum_stock_level, last_restocked, created_at, updated_at) VALUES
                                                                                                                                               (1, 1, 500, 50, 100, NOW(), NOW(), NOW()),
                                                                                                                                               (2, 1, 1000, 100, 200, NOW(), NOW(), NOW()),
                                                                                                                                               (3, 2, 2000, 200, 300, NOW(), NOW(), NOW());

-- Insert a test operation
INSERT INTO operations (name, code_name, description, leader_id, status, start_date, location, risk_level, estimated_profit, created_at, updated_at) VALUES
    ('Operation Sunrise', 'OP001', 'Test operation for API validation', 2, 'PLANNED', '2024-07-01 08:00:00', 'Miami', 6, 500000.00, NOW(), NOW());