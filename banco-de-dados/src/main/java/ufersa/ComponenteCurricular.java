package ufersa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Scanner;

public class ComponenteCurricular {
    private static String codigo;
    private static String nome;
    private static int carga_horaria;
    private static int periodo;
    private static LinkedList<ComponenteCurricular> listaComponente = new LinkedList<>();

    public static void cadastrarComponente(Statement stm) {
        System.out.println("Cadastrar Componente Curricular ");
        Scanner ent = new Scanner(System.in);
        System.out.print("Informe o código: ");
        codigo = ent.next();
        System.out.print("Informe o nome: ");
        nome = ent.next();
        System.out.print("Informe a carga horária: ");
        carga_horaria = ent.nextInt();
        System.out.print("Informe o período: ");
        periodo = ent.nextInt();

        String sql = "insert into componentecurricular (codigo, nome, cargaHoraria, periodo) values ('" + codigo + "','" + nome + "','" + carga_horaria + "','" + periodo + "')";

        try {
            stm.executeUpdate(sql);
            System.out.println("\nComponente cadastrado.");
        } catch (SQLException e) {
            ent.close();
            e.printStackTrace();
        }
    }

    public void excluirComponente(Statement stm) {
        Scanner ent = new Scanner(System.in);
        System.out.println("Excluir Componente Curricular ");
        System.out.print("Informe o código do componente que se deseja excluir: ");
        codigo = ent.next();

        String sql = "delete from componentecurricular where codigo = '" + codigo + "'";

        try {
            stm.executeUpdate(sql);
        } catch (SQLException e) {
            ent.close();
            e.printStackTrace();
        }
    }

    public static void buscarComponente(Statement stm) {
        Scanner ent = new Scanner(System.in);
        System.out.println("Buscar Componente Curricular ");
        System.out.print("Informe o código do componente que se deseja buscar: ");
        codigo = ent.next();

        String sql = "select * from componentecurricular where codigo = '" + codigo + "'";

        try {
            ResultSet result = stm.executeQuery(sql);
            while (result.next()) {
                System.out.println("Componente encontrado. ");
                System.out.println(result.getString("codigo") + " | " + result.getString("nome") + " | "
             + result.getString("carga_horaria") + " | " + result.getString("periodo"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ent.close();
    }

    public static void listaComponente(Statement stm) {
        System.out.println("Listar Componente Curricular ");
        String sql = "select * from componentecurricular";

        try {
            ResultSet result = stm.executeQuery(sql);

            while (result.next()) {
                System.out.println("\n" + result.getString("codigo") + " | " + result.getString("nome") + " | "
                        + result.getString("carga_horaria") + " | " + result.getString("periodo"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void editarComponente(Statement stm) {
        System.out.println("Editar Componente Curricular ");
        String codEnt;
        Scanner ent = new Scanner(System.in);
        int[] escolha = new int[5];

        System.out.print("Informe o código do componente que se deseja editar: ");
        codEnt = ent.next();

        String sql = "select * from componentecurricular where codigo = '" + codEnt + "'";

        try {
            ResultSet result = stm.executeQuery(sql);
            while (result.next()) {

                for (int i = 0; i < escolha.length; i++) {
                    System.out.print("MENU\n0 - Código\n1 - Nome\n2 - Carga Horária\n4 - Período\n");
                    System.out.print("Informe o atributo que se deseja editar: ");

                    do {
                        escolha[i] = ent.nextInt();
                        // ent.nextLine();

                    } while (escolha[i] > 5 || escolha[i] < 0);

                    String alterInit = "update componentecurricular set ";

                    if (escolha[i] == 0) {
                        System.out.print("Informe o novo código: ");
                        codigo = ent.next();

                        alterInit += "codigo = '" + codigo + "' where codigo = '" + codEnt + "'"; 
                        stm.executeUpdate(alterInit);

                    } else if (escolha[i] == 1) {
                        ent.nextLine();
                        System.out.println("Informe o novo nome: ");
                        nome = ent.nextLine();

                        alterInit += "nome = '" + nome + "' where codigo = '" + codEnt + "'";
                        stm.executeUpdate(alterInit);

                    } else if (escolha[i] == 2) {
                        System.out.println("Informe a nova carga horária: ");
                        carga_horaria = ent.nextInt();

                        alterInit += "cargahoraria = '" + carga_horaria + "' where codigo = '" + codEnt + "'";
                        stm.executeUpdate(alterInit);

                    } else if (escolha[i] == 3) {
                        System.out.println("Informe o novo período: ");
                        periodo = ent.nextInt();

                        alterInit += "periodo = '" + periodo + "' where codigo = '" + codEnt + "'";
                        stm.executeUpdate(alterInit);

                    } else {
                        break;
                    }
                }
            }
            ent.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void HorasAulaSemanal() { // obs.: banco de dados

    }

}
