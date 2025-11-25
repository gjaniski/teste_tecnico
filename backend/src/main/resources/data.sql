INSERT INTO customer (id, name, email, is_active) VALUES (1, 'João Pedrosa', 'joao.pedrosa@teste.com', true);
INSERT INTO customer (id, name, email, is_active) VALUES (2, 'Maria Silva', 'maria.silva@teste.com', true);
INSERT INTO customer (id, name, email, is_active) VALUES (3, 'Pedro Alves', 'pedro.alves@teste.com', true);
INSERT INTO customer (id, name, email, is_active) VALUES (4, 'Ana Souza', 'ana.souza@teste.com', true);
INSERT INTO customer (id, name, email, is_active) VALUES (5, 'Carlos Lima', 'carlos.lima@teste.com', true);

INSERT INTO status (id, name) VALUES (1, 'ELITE');
INSERT INTO status (id, name) VALUES (2, 'PRIME');
INSERT INTO status (id, name) VALUES (3, 'VIP');
INSERT INTO status (id, name) VALUES (4, 'PREMIUM');

INSERT INTO customer_status (customer_id, status_id) VALUES (1, 1);
INSERT INTO customer_status (customer_id, status_id) VALUES (1, 4);
INSERT INTO customer_status (customer_id, status_id) VALUES (2, 2);
INSERT INTO customer_status (customer_id, status_id) VALUES (2, 3);
INSERT INTO customer_status (customer_id, status_id) VALUES (3, 1);
INSERT INTO customer_status (customer_id, status_id) VALUES (4, 1);
INSERT INTO customer_status (customer_id, status_id) VALUES (5, 2);

ALTER TABLE CUSTOMER ALTER COLUMN ID RESTART WITH (SELECT MAX(ID) + 1 FROM CUSTOMER);