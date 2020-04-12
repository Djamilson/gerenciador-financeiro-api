CREATE TABLE usuario (
	codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(50) NOT NULL,
	sobre_nome VARCHAR(150) NOT NULL,
	email VARCHAR(50) NOT NULL,
	senha VARCHAR(150) NOT NULL,
	data_nascimento DATE NOT NULL,
	ativo BOOLEAN NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE permissao (
	codigo BIGINT(20) PRIMARY KEY,
	descricao VARCHAR(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE grupo (
	codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(50) NOT NULL,
	descricao VARCHAR(50) NOT NULL	
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE grupo_permissao (
	codigo_grupo BIGINT(20) NOT NULL,
	codigo_permissao BIGINT(20) NOT NULL,
	PRIMARY KEY (codigo_grupo, codigo_permissao),
	FOREIGN KEY (codigo_grupo) REFERENCES grupo(codigo),
	FOREIGN KEY (codigo_permissao) REFERENCES permissao(codigo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE usuario_grupo (
	codigo_usuario BIGINT(20) NOT NULL,
	codigo_grupo BIGINT(20) NOT NULL,
	PRIMARY KEY (codigo_usuario, codigo_grupo),
	FOREIGN KEY (codigo_usuario) REFERENCES usuario(codigo),
	FOREIGN KEY (codigo_grupo) REFERENCES grupo(codigo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#CREATE TABLE usuario_permissao (
#	codigo_usuario BIGINT(20) NOT NULL,
#	codigo_permissao BIGINT(20) NOT NULL,
#	PRIMARY KEY (codigo_usuario, codigo_permissao),
#	FOREIGN KEY (codigo_usuario) REFERENCES usuario(codigo),
#	FOREIGN KEY (codigo_permissao) REFERENCES permissao(codigo)
#) ENGINE=InnoDB DEFAULT CHARSET=utf8;


INSERT INTO usuario (codigo, nome, sobre_nome, email, senha, data_nascimento, ativo) values (1, 'Usuario Default', 'Default', 'usuario.defalt@usuario.default.com', '$2a$10$soAAiGMmY339hMdkSUKAjeDAe7zdqdZMgb2z8pfJVv4zoZJ22iP4.', '2018-10-01', true);
INSERT INTO usuario (codigo, nome, sobre_nome, email, senha, data_nascimento, ativo) values (2, 'Administrador', 'Administrador','admin@algamoney.com', '$2a$10$hpprEcEx3P7Di927vb/ZHOJlDO4ZgzG.fRezSec8e2rpTA.Hqv/jS', '2018-10-01', true);
INSERT INTO usuario (codigo, nome, sobre_nome, email, senha, data_nascimento, ativo) values (3, 'Maria', 'Silva','maria@algamoney.com', '$2a$10$hpprEcEx3P7Di927vb/ZHOJlDO4ZgzG.fRezSec8e2rpTA.Hqv/jS', '2018-10-01', true);
INSERT INTO usuario (codigo, nome, sobre_nome, email, senha, data_nascimento, ativo) values (4, 'Santos','Gomes Nogueira', 'gomes@algamoney.com', '$2a$10$hpprEcEx3P7Di927vb/ZHOJlDO4ZgzG.fRezSec8e2rpTA.Hqv/jS', '2018-10-01', true);

INSERT INTO permissao (codigo, descricao) values (1, 'USUARIO_DEFAULT');
INSERT INTO permissao (codigo, descricao) values (2, 'CADASTRAR_CATEGORIA');
INSERT INTO permissao (codigo, descricao) values (3, 'PESQUISAR_CATEGORIA');
INSERT INTO permissao (codigo, descricao) values (4, 'CADASTRAR_PESSOA');
INSERT INTO permissao (codigo, descricao) values (5, 'REMOVER_PESSOA');
INSERT INTO permissao (codigo, descricao) values (6, 'PESQUISAR_PESSOA');
INSERT INTO permissao (codigo, descricao) values (7, 'CADASTRAR_LANCAMENTO');
INSERT INTO permissao (codigo, descricao) values (8, 'REMOVER_LANCAMENTO');
INSERT INTO permissao (codigo, descricao) values (9, 'PESQUISAR_LANCAMENTO');
INSERT INTO permissao (codigo, descricao) values (10, 'PESQUISAR_PERMISSAO_USUARIO');
INSERT INTO permissao (codigo, descricao) values (11, 'CADASTRAR_USUARIO');
INSERT INTO permissao (codigo, descricao) values (12, 'PESQUISAR_USUARIO');
INSERT INTO permissao (codigo, descricao) values (13, 'REMOVER_USUARIO');
INSERT INTO permissao (codigo, descricao) values (14, 'CADASTRAR_PERMISSAO_USUARIO');
INSERT INTO permissao (codigo, descricao) values (15, 'REMOVER_PERMISSAO_USUARIO');
INSERT INTO permissao (codigo, descricao) values (16, 'CADASTRAR_VENDEDOR');


insert into grupo (codigo, nome, descricao) values (1, 'USUARIO DEFAULT', 'Default');
insert into grupo (codigo, nome, descricao) values (2, 'ADMINISTRADOR', 'Grupo de administradores');
insert into grupo (codigo, nome, descricao) values (3, 'ANALISTA', 'Grupo de analistas');
insert into grupo (codigo, nome, descricao) values (4, 'VENDEDOR', 'Grupo de vendedores');


insert into usuario_grupo (codigo_usuario, codigo_grupo) values (1, 1);
insert into usuario_grupo (codigo_usuario, codigo_grupo) values (2, 2);
insert into usuario_grupo (codigo_usuario, codigo_grupo) values (2, 3);
insert into usuario_grupo (codigo_usuario, codigo_grupo) values (2, 4);
insert into usuario_grupo (codigo_usuario, codigo_grupo) values (3, 3);
insert into usuario_grupo (codigo_usuario, codigo_grupo) values (4, 4);

-- recuperarsenha
INSERT INTO grupo_permissao (codigo_grupo, codigo_permissao) values (1, 1);

-- admin
INSERT INTO grupo_permissao (codigo_grupo, codigo_permissao) values (2, 3);
INSERT INTO grupo_permissao (codigo_grupo, codigo_permissao) values (2, 4);
INSERT INTO grupo_permissao (codigo_grupo, codigo_permissao) values (2, 6);
INSERT INTO grupo_permissao (codigo_grupo, codigo_permissao) values (2, 7);
INSERT INTO grupo_permissao (codigo_grupo, codigo_permissao) values (2, 9);
INSERT INTO grupo_permissao (codigo_grupo, codigo_permissao) values (2, 10);
INSERT INTO grupo_permissao (codigo_grupo, codigo_permissao) values (2, 11);
INSERT INTO grupo_permissao (codigo_grupo, codigo_permissao) values (2, 12);
INSERT INTO grupo_permissao (codigo_grupo, codigo_permissao) values (2, 13);
INSERT INTO grupo_permissao (codigo_grupo, codigo_permissao) values (2, 14);
INSERT INTO grupo_permissao (codigo_grupo, codigo_permissao) values (2, 15);
INSERT INTO grupo_permissao (codigo_grupo, codigo_permissao) values (4, 16);
-- sistema
INSERT INTO grupo_permissao (codigo_grupo, codigo_permissao) values (3, 2);
INSERT INTO grupo_permissao (codigo_grupo, codigo_permissao) values (3, 5);
INSERT INTO grupo_permissao (codigo_grupo, codigo_permissao) values (3, 8);

