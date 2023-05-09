package ufersa; //ESPERAR POR LIVIA, ACHO QUE TÁ FALTANDO COISA AQUI 

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
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
        String codigo_comp = "";
        String horario = "";
        String hora = "";
        int numero_turma = 1;
        int vagas = 0;
        int info_semestre = 0;
        int escolha = 1;

        // do {
        System.out.print("Informe o código do componente para cadastrar a turma: ");
        codigo_comp = ent.next().toUpperCase();

        // verificar se o código do componente existe
        if (ComponenteCurricular.verComponente(stm, codigo_comp) != null) {
            while (Turma.buscarTurma(stm, codigo_comp, numero_turma) != null) {
                numero_turma++;
            }
            System.out.println("Cadastrando turma " + numero_turma);

            do {
                System.out.print("Informe a quantidade de vagas da turma: ");
                vagas = ent.nextInt();
            } while (vagas < 0 || vagas > 100);

            horario = cadastrarHorario(stm, codigo_comp);

        } else {
            System.out.print("Código não cadastrado.");
        }

        Turma turma = new Turma(numero_turma, horario, vagas,
                ComponenteCurricular.buscarComponente(stm, codigo_comp));

        String sql = "insert into turma (numero_turma, vagas, horario, codigo_comp) values ('" + numero_turma + "', '"
                + vagas + "', '" + horario + "', '" + turma.getCodigo_comp().getCodigo_comp() + "')";

        try {
            stm.executeUpdate(sql);
            ProfessorTurma.vinculaProfessorTurma(stm, codigo_comp, numero_turma);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // } while (escolha != 0);

    }

    public static String cadastrarHorario(Statement stm, String codigo_comp) {
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
        // ArrayList<String> captura_aux = new ArrayList<>();

        info_semestre = ComponenteCurricular.buscarComponente(stm, codigo_comp).getSemestre();
        info_professor = ProfessorTurma.buscarProfessorComponente(stm, codigo_comp).getEmail().getEmail();

        ArrayList<Turma> turma_sem = buscarSemestre(stm, info_semestre);
        ArrayList<Turma> turma_prof = buscarProfessor(stm, info_professor);
        ArrayList<String> horario_sem = new ArrayList<>();
        ArrayList<String> horario_prof = new ArrayList<>();

        // comparar com o horario novo que vai ser adicionado
        if (turma_sem != null) {
            for (Turma turma : turma_sem) {
                hora_diS = turma.getHorario();
                String[] separador = hora_diS.split(" ");
                for (String string : separador) {
                    horario_sem.add(string);
                }
            }
        }

        if (turma_prof != null) {
            for (Turma turma : turma_prof) {
                hora_disP = turma.getHorario();
                String[] separador = hora_disP.split(" ");
                for (String string : separador) {
                    horario_prof.add(string);
                }
            }
        }

        carga_horaria = (ComponenteCurricular.buscarComponente(stm, codigo_comp).getCarga_horaria()) / 30;

        do {
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
                        while (hora == 6) {
                            System.out.println(
                                    "Não é possível informar o valor 6 como primeira aula. Informe outro valor da primeira aula: ");
                            hora = ent.nextInt();
                        }
                    } while (hora < 1 || hora > 6);
                }

                if (turno.equals("N")) {
                    do {
                        System.out.println(
                                "1 - 18h50 às 19h45\n2 - 19h45 às 20h40\n3 - 20h40 às 21h35\n4 - 21h35 às 22h30");
                        System.out.print("Informe o valor correspondente a hora: ");
                        hora = ent.nextInt();
                    } while (hora < 1 || hora > 4);
                }

                horario_final += dia_da_semana + "" + turno + "" + hora + "" + (hora + 1);
                horario_aux = dia_da_semana + "" + turno + "" + hora + "" + (hora + 1);
                // captura_aux.add(horario_aux);

                if (disponibilidadePorSemestre(stm, horario_aux, horario_sem) == true
                        && disponibilidadePorProfessor(stm, horario_aux, horario_prof) == true) {
                    if (carga_horaria > 1) {
                        horario_final += " ";
                    }
                    // realizar o decremento só se o horario estiver disponivel
                    carga_horaria--;
                    horario_aux = "";
                } else {
                    horario_final = "";
                    System.out.println("Horário preenchido, refaça o horário.");
                }
                esvaziarBuffer(ent);
            }
        } while (carga_horaria != 0);
        System.out.println("-- Turma cadastrada --");

        return horario_final;
    }

    public static boolean disponibilidadePorSemestre(Statement stm, String hora, ArrayList<String> hora_indisp) {
        // fazer a comparação aqui dentro entre o horario que quero adionar com o
        // horario do array de string
        // cado seja disponivel retorna true, caso contrario retorna falso

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

    public static boolean disponibilidadePorProfessor(Statement stm, String hora, ArrayList<String> hora_indisp) {
        // fazer a comparação aqui dentro entre o horario que quero adionar com o
        // horario do array de string
        // cado seja disponivel retorna true, caso contrario retorna falso

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
        // pegar dados de um componente especifico sem mostrar na tela
        String sql = "select * from turma where codigo_comp = '" + codigo_comp + "' AND numero_turma = " + numero_turma;

        try {
            ResultSet result = stm.executeQuery(sql);
            int numero = numero_turma;
            String horario = "";
            int vagas = 0;
            String codigo = codigo_comp;

            while (result.next()) {
                numero = result.getInt("numero_turma");
                horario = result.getString("horario");
                vagas = result.getInt("vagas");
                codigo = result.getString("codigo_comp");
            }

            // se o codigo for vazio ele nao existe
            if (horario == "") {
                return null;
            } else {
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
        String sql = "select * from turma";

        ArrayList<Turma> turmas = new ArrayList<>();

        try {
            ResultSet result = stm.executeQuery(sql);
            int numero = 1;
            String horario = "";
            int vagas = 0;
            String codigo = "";

            ArrayList<Integer> numeros = new ArrayList<>();
            ArrayList<String> horarios = new ArrayList<>();
            ArrayList<Integer> qnt_vagas = new ArrayList<>();
            ArrayList<String> codigos = new ArrayList<>();

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

            for (int i = 0; i < codigos.size(); i++) {
                if(ComponenteCurricular.buscarComponente(stm, codigos.get(i)).getSemestre() == semestre){
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
        String sql = "select * from professor_turma where email = '" + email + "'";

        ArrayList<Turma> turmas = new ArrayList<>();

        try {
            ResultSet result = stm.executeQuery(sql);
            int numero = 1;
            String horario = "";
            int vagas = 0;
            String codigo = "";

            ArrayList<Integer> numeros = new ArrayList<>();
            ArrayList<String> codigos = new ArrayList<>();
            ArrayList<Integer> qtd_vagas = new ArrayList<>();
            ArrayList<String> horarios = new ArrayList<>();

            while (result.next()) {
                numero = result.getInt("numero_turma");
                codigo = result.getString("codigo_comp");

                numeros.add(numero);
                codigos.add(codigo);
            }

            for (int i = 0; i < codigos.size(); i++) {
                sql = "select * from turma where codigo_comp = '" + codigos.get(i) + "' AND numero_turma = "
                        + numeros.get(i);
                result = stm.executeQuery(sql);
                while (result.next()) {
                    horario = result.getString("horario");
                    vagas = result.getInt("vagas");

                    horarios.add(horario);
                    qtd_vagas.add(vagas);
                }

                Turma turma = new Turma(numeros.get(i), horarios.get(i), qtd_vagas.get(i),
                        ComponenteCurricular.buscarComponente(stm, codigos.get(i)));
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
        String codigo_comp = ent.next();
        System.out.print("Informe o número da turma que se deseja buscar: ");
        int numero_turma = ent.nextInt();

        return verTurma(stm, codigo_comp, numero_turma);
    }

    public static Turma verTurma(Statement stm, String codigo_comp, int numero_turma) {
        // Consulta para verificação de existência no banco de dados
        String sql = "select * from turma where codigo_comp = '" + codigo_comp + "' AND numero_turma = " + numero_turma;

        try {
            ResultSet result = stm.executeQuery(sql);
            // Inicialização das variaveis para chamar no banco de dados
            int numero = numero_turma;
            String horario = "";
            int vagas = 0;
            String codigo = codigo_comp;

            while (result.next()) {
                System.out.println("-- Turma encontrada --");
                numero = result.getInt("numero_turma");
                horario = result.getString("horario");
                vagas = result.getInt("vagas");
                codigo = result.getString("codigo_comp");
                System.out.println("[ " + codigo + " | " + numero + " | " + horario + " | " + vagas + " ]");
            }

            // se o codigo for vazio ele nao existe
            if (codigo == "") {
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
        String sql = "select * from turma where horario = '" + hora + "'";
        ResultSet result;

        try {
            int numero_turma = 1;
            String horario = "";
            int vagas = 0;
            String codigo_comp = "";
            ArrayList<Turma> horario_disp = new ArrayList<>();

            result = stm.executeQuery(sql);
            while (result.next()) {
                codigo_comp = result.getString("codigo_comp");
                numero_turma = result.getInt("numero_turma");
                horario = result.getString("horario");
                vagas = result.getInt("vagas");

                if (codigo_comp != "") {
                    Turma turmaTemporaria = new Turma(numero_turma, horario, vagas, codigo);
                    horario_disp.add(turmaTemporaria);
                }
            }

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
        String codigo_ent;
        int numero_turma;
        int escolha = 0;
        String horario = "";
        int vagas = 0;

        System.out.print("Informe o código do componente da turma que se deseja editar: ");
        codigo_ent = ent.next().toUpperCase();
        System.out.print("Informe o número da turma que se deseja editar: ");
        numero_turma = ent.nextInt();

        if (buscarTurma(stm, codigo_ent, numero_turma) != null) {
            try {
                do {
                    System.out.print("1 - Horário\n2 - Vagas\n3 - Sair\n");
                    System.out.print("Informe o que se deseja editar: ");
                    escolha = ent.nextInt();

                    String alterInit = "update turma set ";

                    switch (escolha) {
                        case 1:
                            System.out.print("Informe o novo horário: ");
                            horario = cadastrarHorario(stm, codigo_ent);
                            alterInit += "horario = '" + horario + "' where codigo_comp = '" + codigo_ent
                                    + "' AND numero_turma = " + numero_turma;
                            stm.executeUpdate(alterInit);
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
                            break;

                        default:
                            System.out.println("Edição concluída.");
                            esvaziarBuffer(ent);
                            break;
                    }
                } while (escolha >= 1 && escolha < 3);
            } catch (SQLException e) {
                System.out.println("Falha na edição do turma.");

                e.printStackTrace();
            } catch (InputMismatchException e) {
                System.out.println("\nInserção inesperada de tipo. Edição concluída.");
            } catch (NoSuchElementException e) {
                System.out.println("\nFalha na entrada de dados. Edição concluída.");
            }
        }
    }

    public static void excluirTurma(Statement stm) {
        String codigo_comp = "";
        String email = "";
        int numero_turma = 0;
        int escolha = 1;

        do {
            System.out.println("Excluir Turma ");
            System.out.print("Informe o nome de usuário do email do professor que se deseja excluir: ");
            email = ent.next().toLowerCase();
            email += "@ufersa.edu.br";
            System.out.print("Informe o código do componente da turma que se deseja excluir: ");
            codigo_comp = ent.next().toUpperCase();
            System.out.print("Informe o número da turma que se deseja excluir: ");
            numero_turma = ent.nextInt();

            ProfessorTurma.desvinculaProfessorTurma(stm, email, codigo_comp, numero_turma);

            if (verTurma(stm, codigo_comp, numero_turma) != null) {
                String sql = "delete from turma where codigo_comp = '" + codigo_comp
                        + "' AND numero_turma = " + numero_turma;
                try {
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
        return "Turma [numero_turma=" + numero_turma + ", horario=" + horario + ", vagas=" + vagas + ", codigo_comp="
                + codigo_comp + "]";
    }

    /*
     * CADASTRAR TURMA
     * a turma está vinculada a qual componente? (ocorre isso através do
     * codigo_comp)
     * - se o componente existe, fazer um select * (queremos saber principalemente o
     * código_comp e o semestre)
     * informe o nome da turma
     * informe a quantidade de vagas para turma (<0 && <=100)
     * horario
     * - 1: cadastrar horario manualmente
     * (o atributo horário corresponderá a uma tabela com duas colunas
     * - coluna 1: tipo int com valor inicial zero
     * - coluna 2: todas os horários possíveis)
     * -----> Para cadastrar uma turma, é necessário que ambos os atributos da
     * primeira coluna das tabelas horario e local_turma seja zero (usar &&)
     * -----> Caso == 0
     * realizar a junção (JOIN) para pegar o resultado da união das duas tuplas de
     * cada uma das tabelas
     * atualizar em cada uma das tabelas o valor de zero para 1
     * turma x vai receber o resultado do JOIN
     * -----> Caso != 0
     * (entra no do/while)
     * deseja informar um novo valor ou gerar automaticamente?
     * --------------> Cadastro realizado.
     */

    /*
     * Possíveis horários de turma
     * Uma turma pode ter vários professores
     * Pode haver mais de uma turma do mesmo componente curricular
     * Turmas do mesmo semestre não podem ter o mesmo horário
     * Turmas ministradas por um mesmo professor não podem ter o mesmo horário
     */
}