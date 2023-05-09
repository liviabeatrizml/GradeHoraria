package ufersa;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class App {
    private static Scanner ent = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            Connection bancodedados = Conexao.inicializaConexao();
            Statement stm = bancodedados.createStatement();

            String str = "";
            int escolha = 1;

            System.out.println("\t\t-- INFORMAÇÕES --");;
            System.out.println("\nDISCIPLINA: PEX0130 - PROGRAMAÇÃO ORIENTADA A OBJETOS\nPROFESSOR: ITALO AUGUSTO DE SOUZA DE ASSIS\nDESCRIÇÃO: SISTEMA DE GRADE HORÁRIA\nDISCENTES: GEÍSA MORAIS GABRIEL - 2021010372\n\t   LÍVIA BEATRIZ MAIA DE LIMA - 2021010871");

            do{
                str += "\n\n\t-- SEJA BEM VINDO AO SISTEMA DE GRADE HORÁRIA UFERSA --\n\n";
            str += " 1 -- MENU COMPONENTE CURRICULAR\n 2 -- MENU PROFESSOR\n 3 -- MENU TURMA\n 4 -- MENU GRADE HORÁRIA\n 5 -- ENCERRAR\n";
            do {
                str += "\nQual menu se deseja editar: ";
                System.out.print(str);
                escolha = ent.nextInt();
            } while (escolha < 1 && escolha > 5);

            switch (escolha) {
                case 1:
                    do {
                        System.out.print("\n-- MENU COMPONENTE CURRICULAR --\n");
                        System.out.print(" 1 -- CADASTRAR\n 2 -- BUSCAR\n 3 -- LISTAR\n 4 -- EDITAR\n 5 -- EXCLUIR\n 6 -- SAIR\n\nQual ação deseja fazer: ");
                        escolha = ent.nextInt();
                        if (escolha == 1) {
                            ComponenteCurricular.cadastrarComponente(stm);
                        } else if (escolha == 2) {
                            ComponenteCurricular.verComponente(stm);
                        } else if (escolha == 3) {
                            ComponenteCurricular.listarComponente(stm);
                        } else if (escolha == 4) {
                            ComponenteCurricular.editarComponente(stm);
                        } else if (escolha == 5) {
                            ComponenteCurricular.excluirComponente(stm);
                        } 
                    } while (escolha != 6);

                    break;
                case 2:
                do {
                    System.out.print("\n-- MENU PROFESSOR --\n");
                    System.out.print(" 1 -- CADASTRAR\n 2 -- BUSCAR\n 3 -- LISTAR\n 4 -- EDITAR\n 5 -- EXCLUIR\n 6 -- SAIR\n\nQual ação deseja fazer: ");
                    escolha = ent.nextInt();
                    if (escolha == 1) {
                        Professor.cadastrarProfessor(stm);
                    } else if (escolha == 2) {
                        Professor.verProfessor(stm);
                    } else if (escolha == 3) {
                        Professor.listarProfessor(stm);
                    } else if (escolha == 4) {
                        Professor.editarProfessor(stm);
                    } else if (escolha == 5) {
                        Professor.excluirProfessor(stm);
                    } 
                } while (escolha != 6);

                    break;
                case 3:
                do {
                    System.out.print("\n-- MENU TURMA --\n");
                    System.out.print(" 1 -- CADASTRAR\n 2 -- BUSCAR\n 3 -- LISTAR\n 4 -- EDITAR\n 5 -- EXCLUIR\n 6 -- SAIR\n\nQual ação deseja fazer: ");
                    escolha = ent.nextInt();
                    if (escolha == 1) {
                        Turma.cadastrarTurma(stm);
                    } else if (escolha == 2) {
                        Turma.verTurma(stm);
                    } else if (escolha == 3) {
                        Turma.listarTurma(stm);
                    } else if (escolha == 4) {
                        Turma.editarTurma(stm);
                    } else if (escolha == 5) {
                        Turma.excluirTurma(stm);
                    } 
                } while (escolha != 6);
                    break;
                case 4:
                do {
                    System.out.print("\n-- MENU GRADE HORÁRIA --\n");
                    System.out.print(" 1 -- GRADE HORÁRIA POR SEMESTRE\n 2 -- GRADE HORÁRIA POR PROFESSOR\n 3 -- SAIR\n\nQual ação deseja fazer: ");
                    escolha = ent.nextInt();
                    if (escolha == 1) {
                        Horario.gradeHorariaPorSemestre(stm);
                    } else if (escolha == 2) {
                        Horario.gradeHorariaPorProfessor(stm);
                    } 
                } while (escolha != 3);
                    break;
                default:
                    System.out.println("\n\t-- SISTEMA ENCERRADO --\n");
                    break;
            }
            }while (escolha != 5);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}