package ufersa; 

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Turma {
    private int numero_turma;
    private String horario;
    private int vagas;
    private ComponenteCurricular codigo_comp;
    private static Scanner ent = new Scanner(System.in);

    public Turma(int numero_turma, String horario, int vagas, ComponenteCurricular codigo_comp) {
        this.numero_turma = 1;
        this.horario = horario;
        this.vagas = vagas;
        this.codigo_comp = codigo_comp;
    }

    public static void cadastrarTurma(Statement stm) {
        //INICIALIZAÇÃO DAS VARIÁVEIS
        String codigo_comp = "";
        String horario = "";
        int numero_turma = 1;
        int vagas = 0;
        int escolha = 1;

        //EXECUTA O CADASTRO DA TURMA QUANTO O USUÁRIO QUISER
        do {
        System.out.print("Informe o código do componente para cadastrar a turma: ");
        codigo_comp = ent.next().toUpperCase();

        //VERIFICA SE O CÓDIGO DO COMPONENTE EXISTE
        if (ComponenteCurricular.verComponente(stm, codigo_comp) != null) {
            //PERCORRE A BUSCA PARA VERIFICAR SE JÁ EXISTE TURMA, REALIZANDO O INCREMENTO SOBRE O NÚMERO DA TURMA 
            while (Turma.buscarTurma(stm, codigo_comp, numero_turma) != null) {
                numero_turma++;
            }
            System.out.println("Cadastrando turma " + numero_turma);

            //PEDE A QUANTIDADE DE VAGAS ENQUANTO NÃO ENTRAR NA VERIFICAÇÃO
            do {
                System.out.print("Informe a quantidade de vagas da turma: ");
                vagas = ent.nextInt();
            } while (vagas < 0 || vagas > 100);

            //REALIZA O CADASTRO DO HORÁRIO DA TURMA
            horario = cadastrarHorario(stm, codigo_comp);

        } else {
            System.out.print("Código não cadastrado.");
        }

        //CRIA UM OBJETO DE TURMA
        Turma turma = new Turma(numero_turma, horario, vagas, ComponenteCurricular.buscarComponente(stm, codigo_comp));

        //REALIZA A INSERÇÃO DE UMA TURMA NO BANCO DE DADOS
        String sql = "insert into turma (numero_turma, vagas, horario, codigo_comp) values ('" + numero_turma + "', '" + vagas + "', '" + horario + "', '" + turma.getCodigo_comp().getCodigo_comp() + "')";

        try {
            //ATUALIZA O COMANDO NO BANCO DE DADOS
            stm.executeUpdate(sql);
            //VINCULA UM PROFESSOR A UMA TURMA
            ProfessorTurma.vinculaProfessorTurma(stm, codigo_comp, numero_turma);
            System.out.println("\n-- Turma cadastrada --");
            System.out.print("\nDeseja cadastrar mais alguma turma?\n0 - NÃO\n1 - SIM\n-> ");
                escolha = ent.nextInt();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        } while (escolha != 0);
        System.out.println("Cadastro encerrado.");

    }

    public static String cadastrarHorario(Statement stm, String codigo_comp) {
        //INICIALIZANDO AS VARIÁVEIS
        int dia_da_semana = 0;
        String turno = "";
        int hora = 0;
        int carga_horaria = 0;
        String horario_final = "";
        String horario_aux = "";
        int info_semestre = 0;
        String info_professor = "";
        String hora_diS = "";
        String hora_disP = "";

        //CAPTURA OS ATRIBUTOS DO SEMESTRE DIANTE DO COMPONENTE PASSADO
        info_semestre = ComponenteCurricular.buscarComponente(stm, codigo_comp).getSemestre();
        //CAPTURA OS ATRIBUTOS DO PROFESSOR DIANTE DO PROFESSOR PASSADO
        info_professor = ProfessorTurma.buscarProfessorComponente(stm, codigo_comp).getEmail().getEmail();

        //DECLARAÇÕES DE ARRAYLIST PARA CADA ATRIBUTO DE SEMESTRE E PROFESSOR
        //CAPTURA AS INFORMAÇÕES DIANTE DO SEMESTRE E PROFESSOR INFORMADO 
        ArrayList<Turma> turma_sem = buscarSemestre(stm, info_semestre);
        ArrayList<Turma> turma_prof = buscarProfessor(stm, info_professor);
        ArrayList<String> horario_sem = new ArrayList<>();
        ArrayList<String> horario_prof = new ArrayList<>();
        
        //COMPARA COM O HORARIO NOVO QUE JÁ ESTA NO BANCO DE DADOS E ADICIONA ELES PARA SER HORARIOS INDISPONIVEIS PARA SEMESTRE
        if (turma_sem != null) {
            for (Turma turma : turma_sem) {
                hora_diS = turma.getHorario();
                String[] separador = hora_diS.split(" ");
                for (String string : separador) {
                    horario_sem.add(string);
                }
            }
        }

        //COMPARA COM O HORARIO NOVO QUE JÁ ESTA NO BANCO DE DADOS E ADICIONA ELES PARA SER HORARIOS INDISPONIVEIS PARA PROFESSOR
        if (turma_prof != null) {
            for (Turma turma : turma_prof) {
                hora_disP = turma.getHorario();
                String[] separador = hora_disP.split(" ");
                for (String string : separador) {
                    horario_prof.add(string);
                }
            }
        }

        //VARIAVEL QUE VERIFICA QUANTAS HORAS POR SEMANA DIANTE DA CARGA HORARIA DO COMPONENTE CURRICULAR
        carga_horaria = (ComponenteCurricular.buscarComponente(stm, codigo_comp).getCarga_horaria()) / 30;

        do {
            //VERIFICAÇÃO SE A CARGA HORARIA É DIFERENTE DE ZERO, CASO SEJA ELE CADASTRA O HORARIO
            while (carga_horaria != 0) {
                System.out.println(
                        "-- CADASTRAR HORÁRIO --\n2 - Segunda\n3 - Terça\n4 - Quarta\n5 - Quinta\n6 - Sexta\n7 - Sábado");
                do {
                    System.out.print("Informe o valor correspondente ao dia da semana: ");
                    dia_da_semana = ent.nextInt();
                } while (dia_da_semana < 2 || dia_da_semana > 7);

                System.out.println("M - Matutino\nV - Vespertino\nN - Noturno");
                
                do {
                    System.out.print("Informe o turno: ");
                    turno = ent.next().toUpperCase();
                } while (!turno.equals("M") && !turno.equals("V") && !turno.equals("N"));

                //VALIDAÇÃO DE TURNO "M" E "V"
                if (turno.equals("M") || turno.equals("V")) {
                    if (turno.equals("M")) {
                        System.out.println(
                                "1 - 07h00 às 07h55\n2 - 07h55 às 08h50\n3 - 08h50 às 09h45\n4 - 09h55 às 10h50\n5 - 10h50 às 11h45\n6 - 11h45 às 12h40");
                    } else {
                        System.out.println(
                                "1 - 13h00 às 13h55\n2 - 13h55 às 14h50\n3 - 14h50 às 15h45\n4 - 15h55 às 16h50\n5 - 16h50 às 17h45\n6 - 17h45 às 18h40");
                    }
                    do {
                        System.out.print("Informe a primeira aula correspondente ao bloco de aulas: ");
                        hora = ent.nextInt();

                        //VALIDAÇÃO CASO O USUARIO INFORME UM BLOCO DE HORARIO INVALIDO
                        while (hora == 6) {
                            System.out.println(
                                    "Não é possível informar o valor 6 como primeira aula. Informe outro valor da primeira aula: ");
                            hora = ent.nextInt();
                        }
                    } while (hora < 1 || hora > 6);
                }

                //VALIDAÇÃO DE TURNO NOTURNO
                if (turno.equals("N")) {
                    do {
                        System.out.println(
                                "1 - 18h50 às 19h45\n2 - 19h45 às 20h40\n3 - 20h40 às 21h35\n4 - 21h35 às 22h30");
                        System.out.print("Informe o valor correspondente a hora: ");
                        hora = ent.nextInt();

                        //VALIDAÇÃO CASO O USUARIO INFORME UM BLOCO DE HORARIO INVALIDO
                        while (hora == 4) {
                            System.out.println(
                                    "Não é possível informar o valor 4 como primeira aula. Informe outro valor da primeira aula: ");
                            hora = ent.nextInt();
                        }
                    } while (hora < 1 || hora > 4);
                }

                //SEPARA OS HORARIOS EM STRINGS
                horario_final += dia_da_semana + "" + turno + "" + hora + "" + (hora + 1);
                horario_aux = dia_da_semana + "" + turno + "" + hora + "" + (hora + 1);

                //VERIFICA SE A DISPONIBILIDADE DO HORARIO É TRUE SE FOR CONTINUA O CADASTRO
                if (disponibilidade(stm, horario_aux, horario_sem) == true) {
                    if (carga_horaria > 1) {
                        horario_final += " ";
                    }
                    //REALIZA O DECREMENTO SÓ SE O HORARIO ESTIVER DISPONIVEL
                    carga_horaria--;
                    horario_aux = "";
                } else {
                    horario_final = "";
                    System.out.println("Horário preenchido, refaça o horário.");
                }

                esvaziarBuffer(ent);
            }
        } while (carga_horaria != 0);
        return horario_final;
    }

    public static boolean disponibilidade(Statement stm, String hora, ArrayList<String> hora_indisp) {
        //PERCORRE OS HORARIOS QUE ESTÃO EM APENAS UMA STRING E SEPARA CADA BLOCO EM UMA STRING SE ESSES HORARIOS FOREM IGUAIS RETORNA FALSE, DIZENDO QUE NÃO ESTÁ DISPONIVEL
        for (String string : hora_indisp) {
            String[] separa_indisp = string.split(" ");
            for (String string2 : separa_indisp) {
                if (string2.equals(hora)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static Turma buscarTurma(Statement stm, String codigo_comp, int numero_turma) {
        //PEGA OS DADOS DE UM COMPONENTE ESPECÍFICO SEM MOSTRAR NA TELA 
        String sql = "select * from turma where codigo_comp = '" + codigo_comp + "' AND numero_turma = " + numero_turma;

        try {
            //EXECUTA O COMANDO NO BANCO DE DADOS
            ResultSet result = stm.executeQuery(sql);
            int numero = numero_turma;
            String horario = "";
            int vagas = 0;
            String codigo = codigo_comp;

            //PERCORRE O RESULTADO DA CONSULTA E PEGA AS INFORMAÇÕES RESPECTIVAS A CADA ATRIBUTO
            while (result.next()) {
                numero = result.getInt("numero_turma");
                horario = result.getString("horario");
                vagas = result.getInt("vagas");
                codigo = result.getString("codigo_comp");
            }

            //CASO SEJA VAZIO O HORÁRIO NÃO EXISTE
            if (horario == "") {
                return null;
            } else {
                //CRIA UM OBJETO DE TURMA E REALIZA O RETORNO
                Turma turmaTemporario = new Turma(numero, horario, vagas,
                ComponenteCurricular.buscarComponente(stm, codigo_comp));
                return turmaTemporario;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<Turma> buscarSemestre(Statement stm, int semestre) {
        //SELECIONA TODOS OS ATRIBUTOS DE TURMA
        String sql = "select * from turma";

        ArrayList<Turma> turmas = new ArrayList<>();

        try {
            //EXECUTA O COMANDO NO BANCO DE DADOS
            ResultSet result = stm.executeQuery(sql);
            int numero = 1;
            String horario = "";
            int vagas = 0;
            String codigo = "";

            //CRIA UM ARRAY PARA CADA ATRIBUTO 
            ArrayList<Integer> numeros = new ArrayList<>();
            ArrayList<String> horarios = new ArrayList<>();
            ArrayList<Integer> qnt_vagas = new ArrayList<>();
            ArrayList<String> codigos = new ArrayList<>();

            //PERCORRE O RESULTADO DA CONSULTA, PEGA AS INFORMAÇÕES CORRESPONDENTES E ADICIONA CADA ATRIBUTO NO SEU RESPECTIVO ARRAY 
            while (result.next()) {
                numero = result.getInt("numero_turma");
                horario = result.getString("horario");
                vagas = result.getInt("vagas");
                codigo = result.getString("codigo_comp");

                numeros.add(numero);
                horarios.add(horario);
                qnt_vagas.add(vagas);
                codigos.add(codigo);
            }

            //PERCORRE O TAMANHO DO VETOR DE CODIGOS E VERIFICA SE EXISTE COMPONENTE COM AQUELE CODIGO E NAQUELE SEMESTRE
            for (int i = 0; i < codigos.size(); i++) {
                if(ComponenteCurricular.buscarComponente(stm, codigos.get(i)).getSemestre() == semestre){
                    //CRIA UM OBJETO DE TURMA E ADICIONA NO VETOR E TRUMAS 
                    Turma turma = new Turma(numeros.get(i), horarios.get(i), qnt_vagas.get(i),ComponenteCurricular.buscarComponente(stm, codigos.get(i)));
                    turmas.add(turma);
                }   
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return turmas;
    }

    public static ArrayList<Turma> buscarProfessor(Statement stm, String email) {
        //SELECIONA TODOS OS ATRIBUTOS DO PROFESSOR VINCULADO A UMA TURMA DE ACORDO COM O EMAIL
        String sql = "select * from professor_turma where email = '" + email + "'";

        ArrayList<Turma> turmas = new ArrayList<>();

        try {
            //EXECUTA O COMANDO NO BANCO DE DADOS
            ResultSet result = stm.executeQuery(sql);
            int numero = 1;
            String horario = "";
            int vagas = 0;
            String codigo = "";

            //CRIA UM ARRAY PARA CADA COLUNA DE ATRIBUTOS 
            ArrayList<Integer> numeros = new ArrayList<>();
            ArrayList<String> codigos = new ArrayList<>();
            ArrayList<Integer> qtd_vagas = new ArrayList<>();
            ArrayList<String> horarios = new ArrayList<>();

            //PERCORRE O RESULTADO DA CONSULTA, PEGA AS INFORMAÇÕES CORRESPONDENTES E ADICIONA CADA ATRIBUTO NO SEU RESPECTIVO ARRAY
            while (result.next()) {
                numero = result.getInt("numero_turma");
                codigo = result.getString("codigo_comp");

                numeros.add(numero);
                codigos.add(codigo);
            }

            //PERCORRE O TAMANHO DO VETOR DE CODIGOS E SELECIONA TODOS OS ATRIBUTOS DE TURMA COM O RESPECTIVO CODIGO E NUMERO
            for (int i = 0; i < codigos.size(); i++) {
                sql = "select * from turma where codigo_comp = '" + codigos.get(i) + "' AND numero_turma = " + numeros.get(i);
                result = stm.executeQuery(sql);
                //PERCORRE O RESULTADO DA CONSULTA, PEGA AS INFORMAÇÕES CORRESPONDENTES E ADICIONA CADA ATRIBUTO NO SEU RESPECTIVO ARRAY 
                while (result.next()) {
                    horario = result.getString("horario");
                    vagas = result.getInt("vagas");

                    horarios.add(horario);
                    qtd_vagas.add(vagas);
                }

                //CRIA UM OBJETO DE TURMAS E ADICIONA NO ARRAY
                Turma turma = new Turma(numeros.get(i), horarios.get(i), qtd_vagas.get(i), ComponenteCurricular.buscarComponente(stm, codigos.get(i)));
                turmas.add(turma);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return turmas;
    }

    public static Turma verTurma(Statement stm) {
        System.out.println("Buscar Turma ");
        System.out.print("Informe o código do componente da turma que se deseja buscar: ");
        String codigo_comp = ent.next().toUpperCase();
        System.out.print("Informe o número da turma que se deseja buscar: ");
        int numero_turma = ent.nextInt();

        //CHAMA O OUTRO METODO PARA FAZER A IMPRESSÃO
        return verTurma(stm, codigo_comp, numero_turma);
    }

    public static Turma verTurma(Statement stm, String codigo_comp, int numero_turma) {
        //COMANDO DE CONSULTA PARA A VERIFICAÇÃO DA EXISTÊNCIA DE UMA TURMA NO BANCO DE DADOS, ESPECIFICANDO O CÓDIGO E O NÚMERO DA TURMA
        String sql = "select * from turma where codigo_comp = '" + codigo_comp + "' AND numero_turma = " + numero_turma;

        try {
            ResultSet result = stm.executeQuery(sql);
            // INICIALIZAÇÃO DAS VARIAVEIS PARA CHAMAR NO BANCO DE DADOS
            int numero = numero_turma;
            String horario = "";
            int vagas = 0;
            String codigo = codigo_comp;

            //PERCORRE A CONSULTA E IMPRIME A TURMA ESPECIFICA INFORMADA
            while (result.next()) {
                System.out.println("\n-- Turma encontrada --");
                numero = result.getInt("numero_turma");
                horario = result.getString("horario");
                vagas = result.getInt("vagas");
                codigo = result.getString("codigo_comp");
                System.out.println("[ " + codigo + " | " + numero + " | " + horario + " | " + vagas + " ]");
            }

            //SE O CÓDIGO FOR VAZIO ELE NÃO EXISTE, CASO CONTRARIO CRIA-SE UMA TURMA TEMPORARIA
            if (codigo == "") {
                System.out.println("Turma não encontrada.");
                return null;
            } else {
                Turma turmaTemporario = new Turma(numero, horario, vagas,
                        ComponenteCurricular.buscarComponente(stm, codigo_comp));
                return turmaTemporario;
            }

        } catch (SQLException e) {
            System.out.println("Falha na busca da turma. ");
            e.printStackTrace();
            return null;
        }
    }

    public static void listarTurma(Statement stm) {
        System.out.println("Listar Turma ");
        String sql = "select * from turma";

        try {
            ResultSet result = stm.executeQuery(sql);

            //IMPRIME TODAS AS TURMAS REGISTRADAS NO BANCO DE DADOS
            while (result.next()) {
                System.out.println("\n[ " + result.getInt("numero_turma") + " | " + result.getInt("vagas") + " | "
                        + result.getString("horario") + " | " + result.getString("codigo_comp") + " ]");
            }
        } catch (SQLException e) {
            System.out.println("Falha na listagem das turmas.");
            e.printStackTrace();
        }
    }

    public static ArrayList<Turma> listaHorarioDisponivel(Statement stm, String hora, ComponenteCurricular codigo) {
        //COMANDO DE BUSCA POR HORÁRIOS ESPECIFICOS
        String sql = "select * from turma where horario = '" + hora + "'";
        ResultSet result;

        try {
            int numero_turma = 1;
            String horario = "";
            int vagas = 0;
            String codigo_comp = "";
            ArrayList<Turma> horario_disp = new ArrayList<>();

            result = stm.executeQuery(sql);
            //PERCORRE A CONSULTA E PEGA OS DADOS DOS COMPONENTES
            while (result.next()) {
                codigo_comp = result.getString("codigo_comp");
                numero_turma = result.getInt("numero_turma");
                horario = result.getString("horario");
                vagas = result.getInt("vagas");

                //VERIFICA SE O CÓDIGO NÃO É VAZIO E ADICIONA NO ARRAY DE HORARIOS INDISPONIVEIS
                if (codigo_comp != "") {
                    Turma turmaTemporaria = new Turma(numero_turma, horario, vagas, codigo);
                    horario_disp.add(turmaTemporaria);
                }
            }

            //VERIFICA SE O HORÁRIO NÃO É VAZIO E RETORNA OS HORARIOS
            if (!horario_disp.isEmpty()) {
                return horario_disp;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public static void editarTurma(Statement stm) {
        System.out.println("Editar Turma ");
        //INICIALIZAÇÃO DAS VARIÁVEIS
        String codigo_ent = "";
        int numero_turma = 0;
        int escolha = 0;
        String horario = "";
        int vagas = 0;

        //VERIFICAÇÃO DE EXISTÊNCIA DO CÓDIGO E NUMERO TURMA
        do{
            System.out.print("Informe o código do componente da turma que se deseja editar: ");
            codigo_ent = ent.next().toUpperCase();
        }while(ComponenteCurricular.verComponente(stm, codigo_ent) == null);

        do{ 
            System.out.print("Informe o número da turma que se deseja editar: ");
            numero_turma = ent.nextInt();
        }while(Turma.buscarTurma(stm, codigo_ent, numero_turma) == null);

        //VERIFICA SE O CÓDIGO E O NÚMERO DA TURMA EXISTE NA TABELA
        if (buscarTurma(stm, codigo_ent, numero_turma) != null) {
            try {
                //PERGUNTA QUAL ATRIBUTO DESEJA EDITAR DE ACORDO COM A ESCOLHA
                do {
                    System.out.print("1 - Horário\n2 - Vagas\n3 - Sair\n");
                    do{
                        System.out.print("Informe o que se deseja editar: ");
                        escolha = ent.nextInt();
                    }while(escolha < 1 || escolha > 3);

                    //REALIZA A ATUALIZAÇÃO NO BANCO DE DADOS
                    String alterInit = "update turma set ";

                    //ENTRA EM CADA CASO DE ACORDO COM A ESCOLHA DO USUÁRIO 
                    switch (escolha) {
                        case 1:
                            System.out.print("Informe o novo horário: ");
                            horario = cadastrarHorario(stm, codigo_ent);

                            alterInit += "horario = '" + horario + "' where codigo_comp = '" + codigo_ent
                                    + "' AND numero_turma = " + numero_turma;
                            stm.executeUpdate(alterInit);
                            System.out.println("Edição concluída.");
                            break;
                        case 2:
                            System.out.print("Informe a nova quantidade de vagas: ");
                            vagas = ent.nextInt();
                            while (vagas < 0 && vagas > 100) {
                                System.out.print("Quantidade de vagas inválida.\nInforme a quantidade corretamente: ");
                                vagas = ent.nextInt();
                            }
                            alterInit += "vagas = " + vagas + " where codigo_comp = '" + codigo_ent
                                    + "' AND numero_turma = " + numero_turma;
                            stm.executeUpdate(alterInit);
                            System.out.println("Edição concluída.");
                            break;
                        default:
                            System.out.println("Saida de edição");

                            break;
                    }
                } while (escolha >= 1 && escolha < 3);
                //TRATAMENTO DE EXCEÇÕES
            } catch (SQLException e) {
                System.out.println("Falha na edição do turma.");
                e.printStackTrace();
            } catch (InputMismatchException e) {
                System.out.println("\nInserção inesperada de tipo. Edição concluída.");
            } 
        }
    }

    public static void excluirTurma(Statement stm) {
        String codigo_comp = "";
        String email = "";
        int numero_turma = 0;
        int escolha = 1;

        //PEDE PARA EXCLUIR MAIS DE UMA TURMA DE ACORDO COM A ESCOLHA DO USUÁRIO 
        do {
            System.out.println("Excluir Turma ");
            System.out.print("Informe o nome de usuário do email do professor que se deseja excluir: ");
            email = ent.next().toLowerCase();
            email += "@ufersa.edu.br";
            System.out.print("Informe o código do componente da turma que se deseja excluir: ");
            codigo_comp = ent.next().toUpperCase();
            System.out.print("Informe o número da turma que se deseja excluir: ");
            numero_turma = ent.nextInt();

            //SÓ É POSSÍVEL EXCLUIR CASO OCORRA A DESVINCULAÇÃO DE UM PROFESSOR A UMA TURMA 
            ProfessorTurma.desvinculaProfessorTurma(stm, email, codigo_comp, numero_turma);

            //VERIFICA SE EXISTE TURMA COM O EMAIL, CÓDIGO E NÚMERO DE TURMA INFORMADO
            if (verTurma(stm, codigo_comp, numero_turma) != null) {
                //DELETA A TURMA COM O RESPECTIVO CODIGO E NUMERO DE TURMA INFORMADO 
                String sql = "delete from turma where codigo_comp = '" + codigo_comp
                        + "' AND numero_turma = " + numero_turma;
                try {
                    //ATUALIZA NO BANCO DE DADOS
                    stm.executeUpdate(sql);
                    System.out.println("\n-- Turma excluída --\n");
                    System.out.print("Deseja excluir mais alguma turma?\n0 - NÃO\n1 - SIM\n-> ");
                    escolha = ent.nextInt();

                    esvaziarBuffer(ent);

                } catch (SQLException e) {

                    System.out.println("Falha na exclusão da turma. ");
                    e.printStackTrace();
                }
            }
        } while (escolha != 0);
        System.out.println("Exclusão encerrada.");
    }

    static void esvaziarBuffer(Scanner ent) {
        if (ent.hasNextLine()) {
            ent.nextLine();
        }
    }

    public int getNumero_turma() {
        return numero_turma;
    }

    public void setNumero_turma(int numero_turma) {
        this.numero_turma = numero_turma;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public int getVagas() {
        return vagas;
    }

    public void setVagas(int vagas) {
        this.vagas = vagas;
    }

    public ComponenteCurricular getCodigo_comp() {
        return codigo_comp;
    }

    public void setCodigo_comp(ComponenteCurricular codigo_comp) {
        this.codigo_comp = codigo_comp;
    }

    @Override
    public String toString() {
        return "Turma [numero_turma=" + numero_turma + ", horario=" + horario + ", vagas=" + vagas + ", codigo_comp=" + codigo_comp + "]";
    }
}