CREATE TABLE lancamento (
	codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	descricao VARCHAR(50) NOT NULL,
	data_vencimento DATE NOT NULL,
	data_pagamento DATE,	
	data_baixa TIMESTAMP NULL DEFAULT NULL,
	data_update TIMESTAMP NULL DEFAULT NULL,
	data_cad TIMESTAMP NOT NULL,
	valor DECIMAL(10,2) NOT NULL,
	valor_pago_recebido DECIMAL(10,2),
	observacao VARCHAR(100),
	tipo VARCHAR(20) NOT NULL,
	numero_lancamento VARCHAR(20) NOT NULL,
	
	codigo_categoria BIGINT(20) NOT NULL,
	codigo_pessoa BIGINT(20) NOT NULL,
	
	FOREIGN KEY (codigo_categoria) REFERENCES categoria(codigo),
	FOREIGN KEY (codigo_pessoa) REFERENCES pessoa(codigo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO lancamento (descricao, data_vencimento, data_pagamento, data_cad, data_baixa, data_update, valor, valor_pago_recebido, observacao, tipo, codigo_categoria, codigo_pessoa, numero_lancamento) values ('Salário mensal', '2018-11-07', null, '2016-11-09 10:21:56', null, null, 6500.00, null, 'Distribuição de lucros', 'RECEITA', 1, 1,'1/1');
INSERT INTO lancamento (descricao, data_vencimento, data_pagamento, data_cad, data_baixa, data_update, valor, valor_pago_recebido, observacao, tipo, codigo_categoria, codigo_pessoa, numero_lancamento) values ('Supermercado', '2018-11-03', '2018-03-01', '2016-11-09 10:21:56', null, null, 100.32, null, null, 'DESPESA', 2, 2,'1/1');
INSERT INTO lancamento (descricao, data_vencimento, data_pagamento, data_cad, data_baixa, data_update, valor, valor_pago_recebido, observacao, tipo, codigo_categoria, codigo_pessoa, numero_lancamento) values ('Academia', '2018-11-01', null, '2016-11-09 10:21:56', null, null, 120, null, null, 'DESPESA', 3, 3,'1/1');
INSERT INTO lancamento (descricao, data_vencimento, data_pagamento, data_cad, data_baixa, data_update, valor, valor_pago_recebido, observacao, tipo, codigo_categoria, codigo_pessoa, numero_lancamento) values ('Conta de luz', '2018-11-09', '2018-02-10', '2016-11-09 10:21:56', null, null, 110.44, null, null, 'DESPESA', 3, 4,'1/1');
INSERT INTO lancamento (descricao, data_vencimento, data_pagamento, data_cad, data_baixa, data_update, valor, valor_pago_recebido, observacao, tipo, codigo_categoria, codigo_pessoa, numero_lancamento) values ('Conta de água', '2018-11-08', null, '2016-11-09 10:21:56', null, null, 200.30, null, null, 'DESPESA', 3, 5,'1/1');
INSERT INTO lancamento (descricao, data_vencimento, data_pagamento, data_cad, data_baixa, data_update, valor, valor_pago_recebido, observacao, tipo, codigo_categoria, codigo_pessoa, numero_lancamento) values ('Restaurante', '2018-11-06', '2018-03-14', '2016-11-09 10:21:56', null, null, 1010.32, null, null, 'DESPESA', 4, 6,'1/1');
INSERT INTO lancamento (descricao, data_vencimento, data_pagamento, data_cad, data_baixa, data_update, valor, valor_pago_recebido, observacao, tipo, codigo_categoria, codigo_pessoa, numero_lancamento) values ('Venda vídeo game', '2018-11-05', null, '2016-11-09 10:21:56', null, null, 500, null, null, 'RECEITA', 1, 7,'1/1');
INSERT INTO lancamento (descricao, data_vencimento, data_pagamento, data_cad, data_baixa, data_update, valor, valor_pago_recebido, observacao, tipo, codigo_categoria, codigo_pessoa, numero_lancamento) values ('Clube', '2018-10-26', '2018-11-06', '2016-11-09 10:21:56', null, null, 400.32, null, null, 'DESPESA', 4, 8,'1/1');
INSERT INTO lancamento (descricao, data_vencimento, data_pagamento, data_cad, data_baixa, data_update, valor, valor_pago_recebido, observacao, tipo, codigo_categoria, codigo_pessoa, numero_lancamento) values ('Impostos', '2018-11-04', null, '2016-11-09 10:21:56', null, null, 123.64, null, 'Multas', 'DESPESA', 3, 9,'1/1');
INSERT INTO lancamento (descricao, data_vencimento, data_pagamento, data_cad, data_baixa, data_update, valor, valor_pago_recebido, observacao, tipo, codigo_categoria, codigo_pessoa, numero_lancamento) values ('Multa', '2018-11-05', null, '2016-11-09 10:21:56', null, null, 665.33, null, null, 'DESPESA', 5, 10,'1/1');
INSERT INTO lancamento (descricao, data_vencimento, data_pagamento, data_cad, data_baixa, data_update, valor, valor_pago_recebido, observacao, tipo, codigo_categoria, codigo_pessoa, numero_lancamento) values ('Padaria', '2018-11-08', '2018-02-28', '2016-11-09 10:21:56', null, null, 8.32, null, null, 'DESPESA', 1, 5,'1/1');
INSERT INTO lancamento (descricao, data_vencimento, data_pagamento, data_cad, data_baixa, data_update, valor, valor_pago_recebido, observacao, tipo, codigo_categoria, codigo_pessoa, numero_lancamento) values ('Papelaria', '2018-11-08', '2018-04-10', '2016-11-09 10:21:56', null, null, 2100.32, null, null, 'DESPESA', 5, 4,'1/1');
INSERT INTO lancamento (descricao, data_vencimento, data_pagamento, data_cad, data_baixa, data_update, valor, valor_pago_recebido, observacao, tipo, codigo_categoria, codigo_pessoa, numero_lancamento) values ('Almoço', '2018-11-07', null, '2016-11-09 10:21:56', null, null, 1040.32, null, null, 'DESPESA', 4, 3,'1/1');
INSERT INTO lancamento (descricao, data_vencimento, data_pagamento, data_cad, data_baixa, data_update, valor, valor_pago_recebido, observacao, tipo, codigo_categoria, codigo_pessoa, numero_lancamento) values ('Café', '2018-11-05', '2018-02-18', '2016-11-09 10:21:56', null, null, 4.32, null, null, 'DESPESA', 4, 2,'1/1');
INSERT INTO lancamento (descricao, data_vencimento, data_pagamento, data_cad, data_baixa, data_update, valor, valor_pago_recebido, observacao, tipo, codigo_categoria, codigo_pessoa, numero_lancamento) values ('Lanche', '2018-11-02', null, '2016-11-09 10:21:56', null, null, 10.20, null, null, 'DESPESA', 4, 1,'1/1');
