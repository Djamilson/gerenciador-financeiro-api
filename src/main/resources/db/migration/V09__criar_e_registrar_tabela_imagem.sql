CREATE TABLE imagem (
	codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(50) NOT NULL,
	caminho_s3 VARCHAR(250) NOT NULL,
	data_cad TIMESTAMP NOT NULL,
	codigo_lancamento BIGINT(20) NOT NULL,
	FOREIGN KEY (codigo_lancamento) REFERENCES lancamento(codigo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;