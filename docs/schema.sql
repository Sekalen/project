-- Таблица ролей
CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL
);

-- Таблица прав
CREATE TABLE permissions (
    id BIGSERIAL PRIMARY KEY,
    permission VARCHAR(255) NOT NULL,
    operation VARCHAR(255) NOT NULL
);

-- Таблица связей роль-право
CREATE TABLE role_permissions (
    role_id BIGINT REFERENCES roles(id),
    permission_id BIGINT REFERENCES permissions(id),
    PRIMARY KEY (role_id, permission_id)
);

-- Таблица пользователей
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN DEFAULT TRUE
);

-- Таблица связей пользователь-роль
CREATE TABLE user_roles (
    user_id BIGINT REFERENCES users(id),
    role_id BIGINT REFERENCES roles(id),
    PRIMARY KEY (user_id, role_id)
);

-- Таблица студентов
CREATE TABLE students (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    group_name VARCHAR(255)
);

-- Таблица записей времени
CREATE TABLE time_entries (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT REFERENCES students(id),
    type VARCHAR(50),
    description VARCHAR(255),
    start TIMESTAMP,
    end_time TIMESTAMP,
    is_billable BOOLEAN
);

-- Таблица норм
CREATE TABLE norms (
    id BIGSERIAL PRIMARY KEY,
    task_type VARCHAR(255) NOT NULL UNIQUE,
    hours DOUBLE PRECISION NOT NULL
);