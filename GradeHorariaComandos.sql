create table professor
(nome varchar(60),
titulacao varchar(60),
email varchar(60),
departamento varchar(20),
primary key (email));

create table componenteCurricular
(codigo varchar(20),
nome varchar(60),
cargaHoraria int,
descricao varchar(20),
periodo int,
primary key (codigo));

create table turma
(nome varchar(20),
ano int,
semestre int,
diasDaSemana varchar(20),
horarios varchar(20),
turno varchar(20),
bloco varchar(20),
vagas int);
