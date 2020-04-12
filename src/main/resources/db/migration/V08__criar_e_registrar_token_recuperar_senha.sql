CREATE TABLE verification_token (
	codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	token VARCHAR(250) NOT NULL,
	expiry_date TIMESTAMP NOT NULL,
	codigo_usuario BIGINT(20) NOT NULL,
	FOREIGN KEY (codigo_usuario) REFERENCES usuario(codigo),
	UNIQUE KEY UK_5171l57faosmj8myawaucatdw (codigo_usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
