--
-- CARGA INICIAL DE DATOS (DATA SEEDING)
--
-- El uso de INSERT IGNORE previene errores si el script se ejecuta más de una vez
-- y si los IDs explícitos ya existen.

-- =======================================================
-- 1. SECCIÓN DE SEGURIDAD (CORREGIDA)
-- =======================================================

-- 1.1 ROLES DE USUARIO
--------------------------------------------------------
-- CORREGIDO: Usando la tabla 'roles' y la columna 'name' (según Role.java)
INSERT IGNORE INTO roles (id, name) VALUES (1, 'ROLE_ADMIN');
INSERT IGNORE INTO roles (id, name) VALUES (2, 'ROLE_USER');
INSERT IGNORE INTO roles (id, name) VALUES (3, 'ROLE_ESPECIALISTO');

-- Nota: La inserción del usuario 'admin' y la asignación de su rol
-- deben gestionarse en Java (CommandLineRunner) para cifrar la contraseña.

-- =======================================================
-- 2. DATOS MAESTROS DEL CARNAVAL
-- =======================================================

-- 2.1 LOCALIDAD
--------------------------------------------------------
INSERT IGNORE INTO localidad (id, nombre) VALUES (1, 'Cádiz');
INSERT IGNORE INTO localidad (id, nombre) VALUES (2, 'San Fernando');
INSERT IGNORE INTO localidad (id, nombre) VALUES (3, 'El Puerto de Santa María');
INSERT IGNORE INTO localidad (id, nombre) VALUES (4, 'Chiclana');
INSERT IGNORE INTO localidad (id, nombre) VALUES (5, 'Sevilla');


-- 2.2 CONCURSO
--------------------------------------------------------
INSERT IGNORE INTO concurso (id,esta_activo,nombre,localidad_id) VALUES (1,1,'COAC', 1);

-- 2.3 EDICION (COAC)
--------------------------------------------------------
INSERT IGNORE INTO edicion (id, anho, concurso_id) VALUES (1, 2024, 1);
INSERT IGNORE INTO edicion (id, anho, concurso_id) VALUES (2, 2023, 1);
INSERT IGNORE INTO edicion (id, anho, concurso_id) VALUES (3, 2022, 1);




-- =======================================================
-- 3. AGRUPACIONES Y PREMIOS (COAC 2022-2024)
-- =======================================================

-- 3.1 AGRUPACION
--------------------------------------------------------

-- COAC 2024 - Comparsa Ganadora (La Oveja Negra)
INSERT IGNORE INTO agrupacion (id, nombre, descripcion, fecha_alta, localidad_id, usuario_creador_id, modalidad, anho)
VALUES (1, 'La oveja negra', 'Comparsa de Antonio Martínez Ares', '2025-12-16', 1, 1, 'COMPARSA',2024);

-- COAC 2024 - Chirigota Ganadora (Los Exageraos)
INSERT IGNORE INTO agrupacion (id, nombre, descripcion, fecha_alta, localidad_id, usuario_creador_id, modalidad, anho)
VALUES (2, 'Los exageraos', 'Chirigoton"', '2025-12-16', 1, 1, 'CHIRIGOTA', 2024);

-- COAC 2024 - Comparsa 2º Premio (Los Colgaos)
INSERT IGNORE INTO agrupacion (id, nombre, descripcion, fecha_alta, localidad_id, usuario_creador_id, modalidad, anho)
VALUES (3, 'Los colgaos', 'García Argüez y Raúl Cabrera', '2025-12-16', 1, 1, 'COMPARSA',2024);

-- COAC 2024 - Coro Ganador (Los Iluminados)
INSERT IGNORE INTO agrupacion (id, nombre, descripcion, fecha_alta, localidad_id, usuario_creador_id, modalidad, anho)
VALUES (4, 'Los iluminados', 'David Fernández', '2025-12-16', 1, 1, 'CORO',2024);

-- COAC 2023 - Comparsa Ganadora (La Ciudad Invisible - M. Ares)
INSERT IGNORE INTO agrupacion (id, nombre, descripcion, fecha_alta, localidad_id, usuario_creador_id, modalidad, anho)
VALUES (5, 'La ciudad invisible', 'Antonio Martínez Ares',  '2025-12-16', 1, 1, 'COMPARSA',2023);

-- COAC 2023 - Chirigota Ganadora (Amo Escuchá)
INSERT IGNORE INTO agrupacion (id, nombre, descripcion, fecha_alta, localidad_id, usuario_creador_id, modalidad, anho)
VALUES (6, 'Amo escuchá (Chirigota callejera)', 'Juan Manuel Braza Benítez "El Sheriff"', '2025-12-16', 1, 1, 'CHIRIGOTA', 2023);

-- Agrupación de fuera de Cádiz (San Fernando)
INSERT IGNORE INTO agrupacion (id, nombre, descripcion, fecha_alta, localidad_id, usuario_creador_id, modalidad, anho)
VALUES (7, 'Los indomables', 'David Carapapa', '2025-12-16', 1, 1, 'COMPARSA', 2023);


-- 3.2 PREMIO (Clasificaciones del COAC)
--------------------------------------------------------
-- COAC 2024
INSERT IGNORE INTO premio (edicion_id, agrupacion_id, puesto, modalidad) VALUES (1, 1, 1, 'COMPARSA'); -- La oveja negra (1º Comparsa)
INSERT IGNORE INTO premio (edicion_id, agrupacion_id, puesto, modalidad) VALUES (1, 2, 1, 'CHIRIGOTA'); -- Los exageraos (1º Chirigota)
INSERT IGNORE INTO premio (edicion_id, agrupacion_id, puesto, modalidad) VALUES (1, 3, 2, 'COMPARSA'); -- Los colgaos (2º Comparsa)
INSERT IGNORE INTO premio (edicion_id, agrupacion_id, puesto, modalidad) VALUES (1, 4, 1, 'CORO'); -- Los iluminados (1º Coro)

-- COAC 2023
INSERT IGNORE INTO premio (edicion_id, agrupacion_id, puesto, modalidad) VALUES (2, 5, 1, 'COMPARSA'); -- La ciudad invisible (1º Comparsa)
INSERT IGNORE INTO premio (edicion_id, agrupacion_id, puesto, modalidad) VALUES (2, 6, 1, 'CHIRIGOTA'); -- Amo escuchá (1º Chirigota)


-- =======================================================
-- 4. PERSONAS Y COMPONENTES
-- =======================================================

-- 4.1 PERSONA (Componentes)
--------------------------------------------------------
-- Autores
INSERT IGNORE INTO persona (id, apodo, nombre_real, localidad_id) VALUES (1, 'Goku', 'Antonio Martínez Ares',  1);
INSERT IGNORE INTO persona (id, apodo, nombre_real, localidad_id) VALUES (2, 'Sherif', 'Juan ManuelBraza Benítez',  1);
INSERT IGNORE INTO persona (id, apodo, nombre_real, localidad_id) VALUES (3, 'Rafa', 'Rafa Velázquez',  1);
INSERT IGNORE INTO persona (id, apodo, nombre_real, localidad_id) VALUES (4, 'David', 'David Fernández',  1);
INSERT IGNORE INTO persona (id, apodo, nombre_real, localidad_id) VALUES (5, 'David Carapapa', 'David Carapapa',  2); -- San Fernando

-- Componentes de relleno (ficticios)
INSERT IGNORE INTO persona (id, apodo, nombre_real, localidad_id) VALUES (6, 'Manuel', 'López',  1);
INSERT IGNORE INTO persona (id, apodo, nombre_real, localidad_id) VALUES (7, 'Felipe', 'Ruiz',  1);
INSERT IGNORE INTO persona (id, apodo, nombre_real, localidad_id) VALUES (8, 'Juana', 'García',  1);
INSERT IGNORE INTO persona (id, apodo, nombre_real, localidad_id) VALUES (9, 'Alberto', 'Díaz',  1);
INSERT IGNORE INTO persona (id, apodo, nombre_real, localidad_id) VALUES (10, 'María', 'Sánchez',  1);


-- 4.2 AGRUPACION_COMPONENTE (Asignación de roles)
--------------------------------------------------------
-- Agrupación 1: La Oveja Negra
INSERT IGNORE INTO componente (agrupacion_id, persona_id, rol) VALUES (1, 1, 'DIRECTOR');
INSERT IGNORE INTO componente (agrupacion_id, persona_id, rol) VALUES (1, 1, 'AUTOR_MUSICA');
INSERT IGNORE INTO componente (agrupacion_id, persona_id, rol) VALUES (1, 3, 'TENOR');
INSERT IGNORE INTO componente (agrupacion_id, persona_id, rol) VALUES (1, 6, 'TENOR');
INSERT IGNORE INTO componente (agrupacion_id, persona_id, rol) VALUES (1, 7, 'TENOR');

-- Agrupación 2: Los Exageraos
INSERT IGNORE INTO componente (agrupacion_id, persona_id, rol) VALUES (2, 2, 'DIRECTOR');
INSERT IGNORE INTO componente (agrupacion_id, persona_id, rol) VALUES (2, 2, 'AUTOR_MUSICA');
INSERT IGNORE INTO componente (agrupacion_id, persona_id, rol) VALUES (2, 9, 'AUTOR_LETRA');
INSERT IGNORE INTO componente (agrupacion_id, persona_id, rol) VALUES (2, 10, 'TENOR');

-- Agrupación 4: Los Iluminados
INSERT IGNORE INTO componente (agrupacion_id, persona_id, rol) VALUES (4, 4, 'DIRECTOR');
INSERT IGNORE INTO componente (agrupacion_id, persona_id, rol) VALUES (4, 4, 'AUTOR_MUSICA');
INSERT IGNORE INTO componente (agrupacion_id, persona_id, rol) VALUES (4, 8, 'TENOR');

-- Agrupación 7: Los Indomables
INSERT IGNORE INTO componente (agrupacion_id, persona_id, rol) VALUES (7, 5, 'DIRECTOR');
INSERT IGNORE INTO componente (agrupacion_id, persona_id, rol) VALUES (7, 5, 'AUTOR_LETRA');
INSERT IGNORE INTO componente (agrupacion_id, persona_id, rol) VALUES (7, 6, 'TENOR');