-- Inserir clientes
INSERT INTO clientes (nome, email, telefone, endereco, data_cadastro, ativo) VALUES
('João Silva', 'joao@email.com', '(11) 99999-1111', 'Rua A, 123 - São Paulo/SP', '2025-11-21', 1),
('Maria Santos', 'maria@email.com', '(11) 99999-2222', 'Rua B, 456 - São Paulo/SP', '2025-11-21', 1),
('Pedro Oliveira', 'pedro@email.com', '(11) 99999-3333', 'Rua C, 789 - São Paulo/SP', '2025-11-21', 1);

-- Inserir restaurantes
INSERT INTO restaurantes (nome, categoria, endereco, telefone, taxa_entrega, avaliacao, ativo) VALUES
('Pizzaria Bella', 'Italiana', 'Av. Paulista, 1000 - São Paulo/SP', '(11) 3333-1111', 5.00, 4.5, 1),
('Burger House', 'Hamburgueria', 'Rua Augusta, 500 - São Paulo/SP', '(11) 3333-2222', 3.50, 4.2, 1),
('Sushi Master', 'Japonesa', 'Rua Liberdade, 200 - São Paulo/SP', '(11) 3333-3333', 8.00, 4.8, 1);

-- Inserir produtos
INSERT INTO produtos (nome, descricao, preco, categoria, disponivel, restaurante_id) VALUES
-- Pizzaria Bella
-- Sushi Master
('Combo Sashimi', '15 peças de sashimi variado', 45.90, 'Sashimi', 1, 3),
('Hot Roll Salmão', '8 peças de hot roll de salmão', 32.90, 'Hot Roll', 1, 3),
('Temaki Atum', 'Temaki de atum com cream cheese', 15.90, 'Temaki', 1, 3);

-- Inserir pedidos de exemplo
INSERT INTO pedidos (numero_pedido, data_pedido, status, valor_total, observacoes, cliente_id, restaurante_id, endereco_entrega, taxa_entrega) VALUES
('PED1234567890', '2025-11-21', 'PENDENTE', 54.80, 'Sem cebola na pizza', 1, 1, 'Rua A, 123 - São Paulo/SP', 5.00),
('PED1234567891', '2025-11-21', 'CONFIRMADO', 41.80, '', 2, 2, 'Rua B, 456 - São Paulo/SP', 3.50),
('PED1234567892', '2025-11-21', 'ENTREGUE', 78.80, 'Wasabi à parte', 3, 3, 'Rua Liberdade, 200 - São Paulo/SP', 8.00);

-- Inserir itens dos pedidos
INSERT INTO item_pedido (quantidade, preco_unitario, subtotal, pedido_id, produto_id) VALUES
(1, 35.90, 35.90, 1, 1), -- Pizza Margherita no pedido 1
(1, 18.90, 18.90, 1, 2), -- Pizza Calabresa no pedido 1

(1, 18.90, 18.90, 2, 4), -- X-Burger no pedido 2
(1, 12.90, 12.90, 2, 6), -- Batata Frita no pedido 2

(1, 45.90, 45.90, 3, 7), -- Combo Sashimi no pedido 3
(1, 32.90, 32.90, 3, 8), -- Hot Roll Salmão no pedido 3
(1, 15.90, 15.90, 3, 9); -- Temaki Atum no pedido 3

-- Inserir usuários
INSERT INTO usuarios (id, nome, email, senha, role, ativo, data_criacao , restaurante_id) VALUES
(1, 'Admin Sistema', 'admin@delivery.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKXulpZR8J4OY6Nd4EMCFyZw4ufC', 'ADMIN', 1, '2025-07-31 00:00:00', NULL),
(2, 'João Cliente', 'joao@email.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKXulpZR8J4OY6Nd4EMCFyZw4ufC', 'CLIENTE', 1, '2025-07-31 00:00:00', NULL),
(3, 'Maria Cliente', 'maria@email.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKXulpZR8J4OY6Nd4EMCFyZw4ufC', 'CLIENTE', 1, '2025-07-31 00:00:00', NULL),
(4, 'Pizza Palace', 'pizza@palace.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKXulpZR8J4OY6Nd4EMCFyZw4ufC', 'RESTAURANTE', 1, '2025-07-31 00:00:00', 1),
(5, 'Burger King', 'burger@king.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKXulpZR8J4OY6Nd4EMCFyZw4ufC', 'RESTAURANTE', 1, '2025-07-31 00:00:00', 2);