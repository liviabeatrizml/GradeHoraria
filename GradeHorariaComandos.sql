create table professor
(matricula varchar(20),
nome varchar(60),
titulacao varchar(60),
email varchar(60),
departamento varchar(20),
primary key (matricula));

create table componenteCurricular
(codigo varchar(20),
nome varchar(60),
cargaHoraria int,
descricao varchar(20),
periodo int,
primary key (codigo));

insert into componenteCurricular (codigo, nome, cargaHoraria, descricao, periodo)
values ('PEX1248', 'Banco de Dados', 60, 'Obrigatória', 4);