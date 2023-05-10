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

        //VERIFICA SE EXISTE PROFESSOR COM EMAIL INFORMADO
        while (Professor.buscarProfessor(stm, email) == null) {
            do {
                //DA AO USUARIO A OPÇÃO DE ESCOLHA DE INFORMAR NOVAMENTE DURANTE 3 TENTATIVAS OU CADASTRAR O NOVO PROFESSOR
                System.out.println("Erro! Cadastre um novo professor ou vincule a disciplina a outro professor.");
                System.out.println("-- MENU --\n1 - Informar novamente o email\n2 - Cadastrar Professor");
                escolha = ent.nextInt();

                esvaziarBuffer(ent);

                //IMPLEMENTAÇÃO DA ESCOLHA
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

        //CASO O NÚMERO DE TENTATIVAS EXPIRE O SISTEMA É ENCERRADO, CASO CONTRARIO CONTINUA A VINCULAÇÃO
        if (decremento == 0) {
            System.out.println("Execução encerrada, número de tentativas expirou.");
        } else {
            //IRÁ INCREMENTAR NO ATRIBUTO HORAS_SEMANAIS DO PROFESSOR A QUANT DE HORAS/AULAS QUE ELE TEM QUE MINISTRAR NA SEMANA
            if (incrementaHoraAula(stm, codigo_comp, email) == true) {
                if (numero_turma != 0) {
                    System.out.println("");
                } else {
                    //TRATAMENTO DE ERRO DE NUMERO DE TURMA NULL OU VAZIO
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
                    //COMANDO DE INSERÇÃO NO BANCO DE DADOS
                    String sql = "insert into professor_turma (email, numero_turma, codigo_comp) values ('" + email+ "', "+ numero_turma + ", '" + codigo_comp + "')";
                    stm.executeUpdate(sql);
                    System.out.println("\n-- Vinculação realizada --\n");
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println("Falha na vinculação.");
                }
            //VALIDAÇÃO DO HORARIO DO PROFESSOR COM HORAS PREENCHIDA DESSA FORMA NÃO É INSERIDO NA TURMA
            } else {
                System.out.println("Professor com horário preenchido.");
            }
        }
    }

    public static void desvinculaProfessorTurma(Statement stm, String em, String cod, int num_tur) {
        //COMANDO DE BUSCA NO BANCO DE DADOS DE UM PROFESSOR VINCULADO A TURMA PASSANDO O EMAIL E O NUMERO DA TURMA
        String sql_carga_horaria = "select * from professor_turma where email = '" + em + "' AND codigo_comp = '" + cod
                + "' AND numero_turma = " + num_tur;

        try {
            if (stm.executeQuery(sql_carga_horaria) != null) {
                //DECREMENTA A HORA ANTES DE FAZER A EXCLUSÃO
                decrementaHoraAula(stm, cod, em);
                //COMANDO DE EXCLUSÃO DE PROFESSOR VINCULADO A TURMA
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
        //COMANDO DE BUSCA NO BANCO DE DADOS DAS CARGAS HORARIAS DOS COMPONENTES PASSANDO UM CÓDIGO ESPECIFICO
        String sql_carga_horaria = "select carga_horaria from componente_curricular where codigo_comp = '" + vincula_cod+ "'";

        int aux = 0;
        int qntdHora = 0;
        ResultSet resultComp;

        try {
            resultComp = stm.executeQuery(sql_carga_horaria);
            //PERCORRENDO A SELEÇÃO FEITA E PEGANDO CADA ATRIBUTO PASSADO NELA
            //VERIFICA PELA CARGA HORARIA DO COMPONENTE, QUANTAS HORAS SEMANAIS O DOCENTE TEM QUE REALIZAR
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
        //COMANDO DE BUSCA DAS HORAS SEMANAIS DO PROFESSOR SELECIONANDO O EMAIL 
        String sql_horas_semanais = "select horas_semanais from professor where email = '" + email + "'";
        int horas_semanais = 0;
        int qntdHora = 0;
        ResultSet resultProf;

        try {
            resultProf = stm.executeQuery(sql_horas_semanais);
            String sql = "update professor set horas_semanais = '";

            //PERCORRENDO AS HORAS SEMANAIS DO PROFESSOR
            while (resultProf.next()) {
                horas_semanais = resultProf.getInt("horas_semanais");
            }

            //VERIFICA SE É POSSÍVEL ADICIONAR HORAS SEMANAIS AO PROFESSOR
            qntdHora = verificaHoraAula(stm, vincula_cod);
            //SOMA A NOVA QUANTIDADE DE HORAS DO PROFESSOR
            int resultado = horas_semanais + qntdHora;

            //VERIFICA AS HORAS SEMANAIS E ATUALIZA O VALOR NO BANCO DE DADOS
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
        //COMANDO DE BUSCA DAS HORAS SEMANAIS DO PROFESSOR SELECIONANDO O EMAIL 
        String sql_horas_semanais = "select horas_semanais from professor where email = '" + email + "'";
        int horas_semanais = 0;
        int qntdHora = 0;
        ResultSet resultProf;

        try {
            resultProf = stm.executeQuery(sql_horas_semanais);
            String sql = "update professor set horas_semanais = '";

            //PERCORRENDO AS HORAS SEMANAIS DO PROFESSOR
            while (resultProf.next()) {
                horas_semanais = resultProf.getInt("horas_semanais");
            }

            //VERIFICA SE É POSSÍVEL ADICIONAR HORAS SEMANAIS AO PROFESSOR
            qntdHora = verificaHoraAula(stm, vincula_cod);

            //VERIFICA AS HORAS SEMANAIS E ATUALIZA O VALOR NO BANCO DE DADOS
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

        //BUSCA O PROFESSOR DE ACORDO COM O EMAIL INFORMADO
        String sql = "select * from professor_turma where email = '" + email + "'";

        try {
            ResultSet result = stm.executeQuery(sql);
            String em = "";

            //PERCORRE A SELEÇÃO E PEDE O CÓDIGO E O NUMERO DA TURMA DO RESPECTIVO EMAIL
            while (result.next()) {
                em = result.getString("email");

                if (email.equals(em)) {
                    System.out.print("Informe o código do componente que se deseja buscar ao professor: ");
                    codigo_comp = ent.next().toUpperCase();
                    System.out.print("Informe o número da turma que se deseja buscar ao professor: ");
                    numero_turma = ent.nextInt();

                    //IMPRIME UM PROFESSOR VINCULADO A UMA TURMA 
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
        //SELECIONA TODOS OS PROFESSOR VINCULADO A UMA TURMA DE ACORDO COM O RESPECTIVO CODIGO
        String sql = "select * from professor_turma where codigo_comp = '" + codigo + "'";

        try {
            ResultSet result = stm.executeQuery(sql);
            int numero_turma = 1;
            String codigo_comp = "";
            String email = "";

            //PERCORRE O RESULTADO DA CONSULTA E PEGA AS INFORMAÇÕES CORRESPONDENTES
            while (result.next()) {
                numero_turma = result.getInt("numero_turma");
                codigo_comp = result.getString("codigo_comp");
                email = result.getString("email");
            }

            //NÃO EXISTE PROFESSOR COM O CODIGO INFORMADO 
            if (codigo == "") {
                return null;
            } else {
                //VERIFICA SE EXISTE TURMA VINCULADA AO CODIGO
                if (Turma.buscarTurma(stm, codigo_comp, numero_turma) != null) {
                    ProfessorTurma prof_turma_temporario = new ProfessorTurma(numero_turma,
                            Turma.buscarTurma(stm, codigo_comp, numero_turma).getHorario(),
                            Turma.buscarTurma(stm, codigo_comp, numero_turma).getVagas(),
                            ComponenteCurricular.buscarComponente(stm, codigo_comp),
                            Professor.buscarProfessor(stm, email));
                    return prof_turma_temporario;
                //CRIA UMA VINCULAÇÃO TEMPORÁRIA PARA PEGAR AS INFORMAÇÕES SEM ACRESCENTAR AO BANCO DE DADOS
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