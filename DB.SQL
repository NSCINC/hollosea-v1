-- Passo 1: Criação do banco de dados
CREATE DATABASE IF NOT EXISTS UserAuthDB;

-- Passo 2: Seleciona o banco de dados criado
USE UserAuthDB;

-- Passo 3: Criação da tabela Users
CREATE TABLE IF NOT EXISTS Users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,  -- Mantém a unicidade do username
    password_hash BINARY(64) NOT NULL,      -- Usa BINARY para armazenar hashes de maneira mais eficiente
    salt BINARY(32) NOT NULL,               -- Coluna para o salt
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Data de criação
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP  -- Data de atualização
);

-- Passo 4: Inserção de dados de exemplo
INSERT INTO Users (username, password_hash, salt) VALUES 
    ('alice', UNHEX('63a9f0ea7bb98050796b649e85481845'), UNHEX('random_salt1')),
    ('bob', UNHEX('2ba6f0ea7bb98050e4f998ba5b4f874a'), UNHEX('random_salt2')),
    ('charlie', UNHEX('46de5f0ea7bb98050796b649e85481845'), UNHEX('random_salt3'));

-- Passo 5: Consulta de autenticação de usuário
SELECT * FROM Users 
WHERE username = 'alice' 
AND password_hash = UNHEX('63a9f0ea7bb98050796b649e85481845') 
AND salt = UNHEX('random_salt1');
