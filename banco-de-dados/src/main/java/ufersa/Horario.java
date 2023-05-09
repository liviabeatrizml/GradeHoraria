package ufersa;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class Horario {
    private String codigo_comp;
    private String horario;
    private int numero_turma;
    private static Scanner ent = new Scanner(System.in);

    public Horario(String codigo_comp, String horario, int numero_turma) {
        this.codigo_comp = codigo_comp;
        this.horario = horario;
        this.numero_turma = numero_turma;
    }

    public static void gradeHorariaPorSemestre(Statement stm) {
        int semestre = 0;
        do {
            System.out.print("Informe o semestre da grade horaria que se deseja apresentar: ");
            semestre = ent.nextInt();
        } while (semestre < 1 || semestre > 6);

        // Declarando os arrays para armazenar as informações
        ArrayList<Horario> horarios_preenchidos = new ArrayList<>();
        ArrayList<Turma> turmas = Turma.buscarSemestre(stm, semestre);

        if (turmas != null) {
            System.out.print("\n-- GRADE HORÁRIA DO " + semestre + "º SEMESTRE --\n\n");
            // pegando os horários que estão registrados e separando eles em blocos
            for (Turma turma : turmas) {
                if (turma.getHorario() != null) {
                    String[] separador_horarios = turma.getHorario().split(" ");
                    for (String hora : separador_horarios) {
                        horarios_preenchidos
                                .add(new Horario(turma.getCodigo_comp().getCodigo_comp(), turma.getHorario(),
                                        turma.getNumero_turma()));
                    }
                }
            }
        }

        esqueletoHorario(horarios_preenchidos);
    }

    public static void gradeHorariaPorProfessor(Statement stm) {
        String email = "";
        do {
            System.out.print("Informe o email do professor que se deseja apresentar a grade de horario: ");
            email = ent.next().toLowerCase();
            email += "@ufersa.edu.br";
        } while (Professor.buscarProfessor(stm, email) == null);

        // Declarando os arrays para armazenar as informações
        ArrayList<Horario> horarios_preenchidos = new ArrayList<>();
        ArrayList<Turma> turmas = Turma.buscarProfessor(stm, email);

        // Primeiro verificar se existe alguma turma registrada
        if (turmas != null) {
            System.out.print("\n\t -- GRADE HORÁRIA DOCENTE " + Professor.buscarProfessor(stm, email).getNome() + " -- \n\n");
            // pegando os horários que estão registrados e separando eles em blocos
            for (Turma turma : turmas) {
                if (turma.getHorario() != null) {
                    String[] separador_horarios = turma.getHorario().split(" ");
                    for (String hora : separador_horarios) {
                        horarios_preenchidos.add(new Horario(turma.getCodigo_comp().getCodigo_comp(), turma.getHorario(),turma.getNumero_turma()));
                    }
                }
            }
        }

        esqueletoHorario(horarios_preenchidos);
    }

    public static void esqueletoHorario(ArrayList<Horario> horarios_preenchidos){
        int dia = 0;
        int bloco_horarios = 0;
        String turno_horario = "";
        int aloca = 0;

        String[][] tabela = new String[16][7]; 
        String[] horarios = { "07:00 - 07:55", "07:55 - 08:50", "08:50 - 09:45", "09:55 - 10:50", "10:50 - 11:45","11:45 - 12:40", "13:00 - 13:55", "13:55 - 14:50", "14:50 - 15:45", "15:55 - 16:50", "16:50 - 17:45","17:45 - 18:40", "18:50 - 19:45", "19:55 - 20:40", "20:40 - 21:35", "21:35 - 22:30" };
        String dia_da_semana = "    HORARIOS       SEGUNDA      TERÇA       QUARTA      QUINTA      SEXTA       SABADO\n";

        // Selecionando os horarios para marcar na matriz
        for (Horario horas : horarios_preenchidos) {
            String[] separa = horas.getHorario().split(" ");
            for (String horarios_separados : separa) {
                dia = Character.getNumericValue(horarios_separados.charAt(0)) - 1;
                //bloco de aula
                bloco_horarios = Character.getNumericValue(horarios_separados.charAt(2));
                turno_horario = horarios_separados.charAt(1) + "";

                if (turno_horario.equals("M")) {
                    aloca = 0;
                } else if (turno_horario.equals("V")) {
                    aloca = 6;
                } else {
                    aloca = 12;
                }

                // Alocar na matriz os valores
                tabela[(bloco_horarios - 1) + aloca][dia - 1] = horas.getCodigo_comp();
                tabela[(bloco_horarios - 1) + aloca + 1][dia - 1] = horas.getCodigo_comp();

            }

        }

        // IMPRESSÃO DA TABELA
        String str = "";

        str += dia_da_semana;
        for (int i = 0; i < 16; i++) {
            str += horarios[i];
            str += " |\t";
            for (int j = 0; j < 6; j++) {
                if (tabela[i][j] == null) {
                    str += "     ---    ";
                    if (j == 5) {
                        str += " |\n";
                    }
                } else {
                    str += "   " + tabela[i][j] + "  ";
                    if (j == 5) {
                        str += " |\n";
                    }
                }
            }
            if (i == 5 || i == 11) {
                str += "\n";
            }
        }
        System.out.print(str);
    }

    public String getCodigo_comp() {
        return codigo_comp;
    }

    public String getHorario() {
        return horario;
    }

    public int getNumero_turma() {
        return numero_turma;
    }

    @Override
    public String toString() {
        return "Horario [codigo_comp=" + codigo_comp + ", horario=" + horario + ", numero_turma=" + numero_turma + "]";
    }
}