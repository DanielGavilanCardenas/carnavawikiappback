-- data.sql - Script para inicialización de datos fijos

-- 1. ROLES DE USUARIO
--------------------------------------------------------
-- Si no existen, insertamos los roles de usuario
INSERT IGNORE INTO roles (id, name) VALUES (1, 'ROLE_ADMIN');
INSERT IGNORE INTO roles (id, name) VALUES (2, 'ROLE_USER');
INSERT IGNORE INTO roles (id, name) VALUES (3, 'ROLE_ESPECIALISTO');


-- 2. USUARIO ADMINISTRADOR INICIAL
--------------------------------------------------------
-- Contraseña cifrada de 'admin123'
SET @hashed_password = '$2a$10$T8V03P.3n/S.K7T1Q/9m0.V8W.tq/D3yR5x0N2J8Y8zL5nQ4L7U9';

-- Insertar el usuario administrador si no existe (con enabled=true)
INSERT IGNORE INTO usuario (id, username, email, password, enabled, activation_token, fecha_alta)
VALUES (1, 'admin', 'admin@carnavawiky.com', @hashed_password, TRUE, NULL, NOW());


-- 3. ASIGNAR ROL DE ADMINISTRADOR
--------------------------------------------------------
-- Asignar ROLE_ADMIN (id=1) al usuario 'admin' (id=1)
INSERT IGNORE INTO usuario_roles (usuario_id, role_id) VALUES (1, 1);