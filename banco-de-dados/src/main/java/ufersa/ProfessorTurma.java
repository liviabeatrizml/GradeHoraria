package ufersa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class ProfessorTurma extends Turma {
    private Professor email;
    private static Scanner ent = new Scanner(System.in);

    public ProfessorTurma(int numero_turma, String horario, int vagas, ComponenteCurricular codigo_comp,
            Professor email) {
        super(numero_turma, horario, vagas, codigo_comp);
        this.email = email;
    }

    public static void vinculaProfessorTurma(Statement stm, String codigo_comp, int numero_turma) {
        String email = "";
        int escolha = 1;
        int decremento = 3;

        System.out.print("Informe o nome de usuário do professor: ");
        email = ent.next().toLowerCase();
        email += "@ufersa.edu.br";

        while (Professor.buscarProfessor(stm, email) == null) {
            do {
                System.out.println("Erro! Cadastre um novo professor ou vincule a disciplina a outro professor.");
                System.out.println("-- MENU --\n1 - Informar novamente o email\n2 - Cadastrar Professor");
                escolha = ent.nextInt();

                esvaziarBuffer(ent);

                if (escolha == 1) {
                    System.out.println("Restam " + decremento + " tentativas.");
                    decremento--;
                    System.out.print("Informe o nome de usuário do professor: ");
                    email = ent.next().toLowerCase();
                    email += "@ufersa.edu.br";
                } else if (escolha == 2) {
                    Professor.cadastrarProfessor(stm);
                } else {
                    System.out.println("Valor incorreto.");
                }
            } while (decremento == 0);
        }

        if (decremento == 0) {
            System.out.println("Execução encerrada, número de tentativas expirou.");
        } else {
            if (incrementaHoraAula(stm, codigo_comp, email) == true) {
                if (numero_turma != 0) {
                    System.out.println("");
                } else {
                    if (Turma.buscarTurma(stm, codigo_comp, numero_turma) == null) {
                        ProfessorTurma prof_turma_temporario = new ProfessorTurma(numero_turma,
                                Turma.buscarTurma(stm, codigo_comp, numero_turma).getHorario(),
                                Turma.buscarTurma(stm, codigo_comp, numero_turma).getVagas(),
                                ComponenteCurricular.buscarComponente(stm, codigo_comp),
                                Professor.buscarProfessor(stm, email));
                    } else {
                        ProfessorTurma prof_turma_temporario = new ProfessorTurma(numero_turma,
                                "", 0, ComponenteCurricular.buscarComponente(stm, codigo_comp),
                                Professor.buscarProfessor(stm, email));
                    }
                }

                try {
                    String sql = "insert into professor_turma (email, numero_turma, codigo_comp) values ('" + email
                            + "', "
                            + numero_turma + ", '" + codigo_comp + "')";
                    stm.executeUpdate(sql);
                    System.out.println("\n-- Vinculação realizada --\n");
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println("Falha na vinculação.");
                }
            } else {
                System.out.println("Professor com horário preenchido.");
            }
        }
    }

    public static void desvinculaProfessorTurma(Statement stm, String em, String cod, int num_tur) {
        String sql_carga_horaria = "select * from professor_turma where email = '" + em + "' AND codigo_comp = '" + cod
                + "' AND numero_turma = " + num_tur;

        try {
            if (stm.executeQuery(sql_carga_horaria) != null) {
                decrementaHoraAula(stm, cod, em);
                String sql = "delete from professor_turma where email = '" + em + "' AND codigo_comp = '" + cod
                        + "' AND numero_turma = " + num_tur;
                stm.executeUpdate(sql);
                System.out.println("\n-- Desvinculação realizada --\n");
            } else {
                System.out.println("Impossível desvincular.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int verificaHoraAula(Statement stm, String vincula_cod) {
        String sql_carga_horaria = "select carga_horaria from componente_curricular where codigo_comp = '" + vincula_cod
                + "'";
        int aux = 0;
        int qntdHora = 0;
        ResultSet resultComp;

        try {
            resultComp = stm.executeQuery(sql_carga_horaria);
            while (resultComp.next()) {
                qntdHora = resultComp.getInt("carga_horaria");
                for (int i = 1; i < qntdHora; i++) {
                    aux = qntdHora / i;
                    if (aux == 15 && i <= 20) {
                        qntdHora = i;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return qntdHora;
    }

    public static boolean incrementaHoraAula(Statement stm, String vincula_cod, String email) {
        String sql_horas_semanais = "select horas_semanais from professor where email = '" + email + "'";
        int horas_semanais = 0;
        int qntdHora = 0;
        ResultSet resultProf;

        try {
            resultProf = stm.executeQuery(sql_horas_semanais);
            String sql = "update professor set horas_semanais = '";

            while (resultProf.next()) {
                horas_semanais = resultProf.getInt("horas_semanais");
            }

            qntdHora = verificaHoraAula(stm, vincula_cod);
            int resultado = horas_semanais + qntdHora;

            if (horas_semanais != 0 && resultado < 20) {
                sql += (horas_semanais + qntdHora) + "' where email = '" + email + "'";
            } else if (horas_semanais == 0) {
                sql += qntdHora + "' where email = '" + email + "'";
            } else {
                sql += horas_semanais + "' where email = '" + email + "'";
                return false;
            }

            stm.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean decrementaHoraAula(Statement stm, String vincula_cod, String email) {
        String sql_horas_semanais = "select horas_semanais from professor where email = '" + email + "'";
        int horas_semanais = 0;
        int qntdHora = 0;
        ResultSet resultProf;

        try {
            resultProf = stm.executeQuery(sql_horas_semanais);
            String sql = "update professor set horas_semanais = '";

            while (resultProf.next()) {
                horas_semanais = resultProf.getInt("horas_semanais");
            }

            qntdHora = verificaHoraAula(stm, vincula_cod);

            if (horas_semanais != 0 && horas_semanais <= 20) {
                sql += (horas_semanais - qntdHora) + "' where email = '" + email + "'";
            } else {
                sql += horas_semanais + "' where email = '" + email + "'";
                return false;
            }

            stm.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static ProfessorTurma verProfessorTurma(Statement stm) {
        String codigo_comp = "";
        int numero_turma = 0;

        System.out.println("Buscar professor turma ");
        System.out.print("Informe o nome de usuário do email do professor que se deseja buscar: ");
        String email = ent.next().toLowerCase();
        email += "@ufersa.edu.br";

        String sql = "select * from professor_turma where email = '" + email + "'";

        try {
            ResultSet result = stm.executeQuery(sql);
            String em = "";

            while (result.next()) {
                em = result.getString("email");

                if (email.equals(em)) {
                    System.out.print("Informe o código do componente que se deseja buscar ao professor: ");
                    codigo_comp = ent.next().toUpperCase();
                    System.out.print("Informe o número da turma que se deseja buscar ao professor: ");
                    numero_turma = ent.nextInt();

                    if (codigo_comp.equals(result.getString("codigo_comp"))
                            && numero_turma == result.getInt("numero_turma")) {
                        System.out.println("\n-- Professor vinculado à turma --");
                        System.out.println("[ " + email + " | " + numero_turma + " | " + codigo_comp + " ]");
                        ProfessorTurma professorTurmaTemporario = new ProfessorTurma(numero_turma,
                                Turma.buscarTurma(stm, codigo_comp, numero_turma).getHorario(),
                                buscarTurma(stm, codigo_comp, numero_turma).getVagas(),
                                ComponenteCurricular.buscarComponente(stm, codigo_comp),
                                Professor.buscarProfessor(stm, email));

                        return professorTurmaTemporario;
                    }
                }
            }
            return null;

        } catch (SQLException e) {
            System.out.println("Falha na busca do professor.");
            e.printStackTrace();
            return null;
        }
    }

    public static ProfessorTurma buscarProfessorComponente(Statement stm, String codigo) {
        String sql = "select * from professor_turma where codigo_comp = '" + codigo + "'";

        try {
            ResultSet result = stm.executeQuery(sql);
            int numero_turma = 1;
            String codigo_comp = "";
            String email = "";

            while (result.next()) {
                numero_turma = result.getInt("numero_turma");
                codigo_comp = result.getString("codigo_comp");
                email = result.getString("email");
            }

            if (codigo == "") {
                return null;
            } else {
                if (Turma.buscarTurma(stm, codigo_comp, numero_turma) != null) {
                    ProfessorTurma prof_turma_temporario = new ProfessorTurma(numero_turma,
                            Turma.buscarTurma(stm, codigo_comp, numero_turma).getHorario(),
                            Turma.buscarTurma(stm, codigo_comp, numero_turma).getVagas(),
                            ComponenteCurricular.buscarComponente(stm, codigo_comp),
                            Professor.buscarProfessor(stm, email));
                    return prof_turma_temporario;
                } else {
                    ProfessorTurma prof_turma_temporario = new ProfessorTurma(numero_turma,
                            "", 0, ComponenteCurricular.buscarComponente(stm, codigo_comp),
                            Professor.buscarProfessor(stm, email));
                    return prof_turma_temporario;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Professor getEmail() {
        return email;
    }

    public void setEmail(Professor email) {
        this.email = email;
    }
    
    @Override
    public String toString() {
        return "ProfessorTurma [email=" + email + "]";
    }
}