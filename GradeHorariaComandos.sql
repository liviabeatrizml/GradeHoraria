/*CRIANDO TABELAS PRINCIPAIS*/
create table professor
(nome varchar(60),
titulacao varchar(60),
email varchar(60),
horas_semanais int default 0,
primary key (email));

create table componente_curricular
(codigo_comp varchar(20),
nome varchar(60),
carga_horaria int,
semestre int,
categoria varchar(20),
primary key (codigo_comp));

/*CRIANDO TABELA PRINCIPAL COM ADIÇÃO DE COLUNAS*/
create table turma
(numero_turma int,
vagas int,
horario varchar(20),
codigo_comp varchar(20),
primary key (numero_turma, codigo_comp),
foreign key (codigo_comp) references componente_curricular);

/*CRIANDO TABELAS DE RELAÇÃO (TABELAS PRÓPRIAS)*/
create table professor_turma
(email varchar (60),
numero_turma int,
codigo_comp varchar(20),
primary key (numero_turma, email, codigo_comp),
foreign key (numero_turma, codigo_comp) references turma,
foreign key (email) references professor);
