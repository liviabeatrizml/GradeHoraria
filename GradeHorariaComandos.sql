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

/*DADOS DOS COMPONENTES CURRICULARES*/
-- PRIMEIRO SEMESTRE --
insert into componente_curricular (codigo_comp, nome, carga_horaria, semestre, categoria)
values ('PAC0008', 'ETICA E LEGISLACAO', 30, 1, 'OBRIGATORIA'),
('PAC0050', 'ANALISE E EXPRESSAO TEXTUAL', 60, 1, 'OBRIGATORIA'),
('PEX0101', 'CALCULO I (1200003)', 60, 1, 'OBRIGATORIA'),
('PEX1236', 'ALGORITMOS', 60, 1,  'OBRIGATORIA'),
('PEX1237', 'LABORATORIO DE ALGORITMOS', 30, 1, 'OBRIGATORIA'),
('PEX1239', 'INTRODUCAO A COMPUTACAO E AOS SISTEMAS DE INFORMACAO', 60, 1, 'OBRIGATORIA'),
('PEX1240', 'SEMINARIO DE INTRODUCAO AO CURSO', 30, 1, 'OBRIGATORIA');

-- SEGUNDO SEMESTRE --
insert into componente_curricular (codigo_comp, nome, carga_horaria, semestre, categoria)
values ('PAC0178', 'SOCIOLOGIA (1200320)', 60, 2, 'OBRIGATORIA'),
('PAC0595', 'ADMINISTRACAO E EMPREENDEDORISMO', 60, 2, 'OBRIGATORIA'),
('PEX0102', 'CALCULO II', 60, 2, 'OBRIGATORIA'),
('PEX0114', 'GEOMETRIA ANALITICA (1200255)', 60, 2, 'OBRIGATORIA'),
('PEX1241', 'ALGORITMOS E ESTRUTURAS DE DADOS I', 60, 2, 'OBRIGATORIA'),
('PEX1243', 'LABORATORIO DE ALGORITMOS E ESTRUTURA DE DADOS I', 30, 2, 'OBRIGATORIA'),
('PEX1244', 'ARQUITETURA E ORGANIZACAO DE COMPUTADORES', 60, 2, 'OBRIGATORIA');

-- TERCEIRO SEMESTRE --
insert into componente_curricular (codigo_comp, nome, carga_horaria, semestre, categoria)
values ('PAC0701', 'ECONOMIA PARA ENGENHARIA (1200374)', 60, 3, 'OBRIGATORIA'),
('PAM0324', 'MATEMATICA DISCRETA (1200780)', 60, 3, 'OBRIGATORIA'),
('PEX0093', 'SISTEMAS OPERACIONAIS (120076)', 60, 3, 'OBRIGATORIA'),
('PEX0096', 'ALGEBRA LINEAR (1200260)', 60, 3, 'OBRIGATORIA'),
('PEX0117', 'INTRODUCAO AS FUNCOES DE VARIAS VARIAVEIS (1200122)', 60, 3, 'OBRIGATORIA'),
('PEX1246', 'ALGORITMOS E ESTRUTURAS DE DADOS II', 60, 3, 'OBRIGATORIA'),
('PEX1247', 'LABORATORIO DE ALGORITMOS E ESTRUTURA DE DADOS II', 30, 3, 'OBRIGATORIA');

-- QUARTO SEMESTRE --
insert into componente_curricular (codigo_comp, nome, carga_horaria, semestre, categoria)
values ('PEX0041', 'REDES DE COMPUTADORES (1200279)', 60, 4, 'OBRIGATORIA'),
('PEX0130', 'PROGRAMACAO ORIENTADA A OBJETOS', 60, 4, 'OBRIGATORIA'),
('PEX1248', 'BANCO DE DADOS', 60, 4, 'OBRIGATORIA'),
('PVE0004', 'ESTATISTICA (1104030)', 60, 4, 'OBRIGATORIA');

-- QUINTO SEMESTRE --
insert into componente_curricular (codigo_comp, nome, carga_horaria, semestre, categoria)
values ('PAC0012', 'FILOSOFIA DA CIENCIA E MET. CIENTIFICA (1200171)', 60, 5, 'OBRIGATORIA'),
('PEX0162', 'ENGENHARIA DE SOFTWARE', 60, 5, 'OBRIGATORIA'),
('PEX0183', 'SISTEMAS DISTRIBUIDOS', 60, 5, 'OBRIGATORIA'),
('PEX1249', 'COMPUTACAO GRAFICA', 60, 5, 'OBRIGATORIA');

-- SEXTO SEMESTRE --
insert into componente_curricular (codigo_comp, nome, carga_horaria, semestre, categoria)
values ('PEX1251', 'ANALISE E PROJETO DE SISTEMAS ORIENTADOS A OBJETOS', 60, 6, 'OBRIGATORIA'),
('PEX1253', 'MULTIMIDIA', 60, 6, 'OBRIGATORIA'),
('PEX1254', 'DEPENDABILIDADE E SEGURANCA', 60, 6, 'OBRIGATORIA'),
('PEX1260', 'TRABALHO DE CONCLUSAO DE CURSO', 60, 6, 'OBRIGATORIA');

/*DADOS DOS COMPONENTES CURRICULARES OPTATIVOS*/
-- QUARTO SEMESTRE --
insert into componente_curricular (codigo_comp, nome, carga_horaria, semestre, categoria)
values ('PEX0125', 'MECANICA CLASSICA', 60, 4, 'OPTATIVA'),
('PEX0122', 'LABORATORIO DE MECANICA CLASSICA', 30, 4, 'OPTATIVA'),
('PAC0027', 'QUIMICA GERAL', 60, 4, 'OPTATIVA'),
('PAC0379', 'LABORATÓRIO DE QUIMICA GERAL', 30, 4, 'OPTATIVA'),
('PEX0263', 'LOGICA MATEMÁTICA', 60, 4, 'OPTATIVA'),
('PEX1263', 'PROJETO DETALHADO DE SOFTWARE', 60, 4, 'OPTATIVA'),
('PEX1266', 'PROJETO E DESIGN DE INTERFACES', 60, 4, 'OPTATIVA');

-- QUINTO SEMESTRE --
insert into componente_curricular (codigo_comp, nome, carga_horaria, semestre, categoria)
values ('PEX0177', 'ONDAS E TERMODINAMICA', 60, 5, 'OPTATIVA'),
('PEX0176', 'LABORATORIO DE ONDAS E TERMODINAMICA', 30, 5, 'OPTATIVA'),
('PEX0240', 'CIRCUITOS DIGITAIS', 60, 5, 'OPTATIVA'),
('PEX1258', 'LABORATORIO DE CIRCUITOS DIGITAIS', 30, 5, 'OPTATIVA'),
('PEX1267', 'METODOS FORMAIS DE ENGENHARIA DE SOFTWARE', 60, 5, 'OPTATIVA'),
('PEX1271', 'TESTE DE SOFTWARE', 60, 5, 'OPTATIVA'),
('PEX1272', 'PROGRAMACAO CONCORRENTE E DISTRIBUIDA', 60, 5, 'OPTATIVA');

-- SEXTO SEMESTRE --
insert into componente_curricular (codigo_comp, nome, carga_horaria, semestre, categoria)
values ('PEX0376', 'ELETRICIDADE E MAGNETISMO', 60, 6, 'OPTATIVA'),
('PEX0150', 'LABORATORIO DE ELETRICIDADE E MAGNETISMO', 30, 6, 'OPTATIVA'),
('PEX1259', 'SINAIS E SISTEMAS', 60, 6, 'OPTATIVA'),
('PEX1273', 'PROCESSO DE SOFTWARE', 60, 6, 'OPTATIVA'),
('PEX0184', 'ENGENHARIA DE REQUISITOS', 60, 6, 'OPTATIVA'),
('PEX0198', 'QUALIDADE DE SOFTWARE', 60, 6, 'OPTATIVA');

