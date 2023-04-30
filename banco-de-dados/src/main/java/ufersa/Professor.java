package ufersa;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Scanner;

public class Professor {
    private String nome;
    private String titulacao;
    private String email;
    private int horasSemanais = 0;
    private static LinkedList<Professor> professores = new LinkedList<>();
    LinkedList<ComponenteCurricular> disciplinas;

    public Professor(String nome, String titulacao, String email) {
        this.nome = nome;
        this.titulacao = titulacao;
        this.email = email;
        this.horasSemanais = horasSemanais;
    }

    public static void cadastrarProfessor(Statement stm){
        Scanner ent = new Scanner(System.in);
        String n, t, em;
        System.out.print("Nome: ");
        n = ent.next();
        System.out.print("Titulacao: ");
        t = ent.next();
        System.out.print("Email: ");
        em = ent.next();

        String sql = "insert into professor (nome, titulacao, email) values ('" + n + "','" + t + "','" + em + "')";
        Professor p = new Professor(n, t, em);
        professores.add(p);
        
        try {
            stm.executeUpdate(sql);
        } catch (SQLException e) {
            ent.close();
            e.printStackTrace();
        }
    }

    public void excluirProfessor(Statement stm){
        Scanner ent = new Scanner(System.in);
        String n;

        //ARRUMAR DEPOIS 
        System.out.print("Nome: ");
        n = ent.next();

        String sql = "delete from professor where nome ='" + n +"'";

        try {
            stm.executeUpdate(sql);
        } catch (SQLException e) {
            ent.close();
            e.printStackTrace();
        }

    }

    public void editarProfessor() {
        String nome, titulacao, email, departamento;
        Scanner ent = new Scanner(System.in);
        int escolha[] = new int[6];

        try {
            for (int i = 0; i < escolha.length; i++) {
                System.out.println("MENU\n");
                System.out.println("O que você deseja editar: ");

                do {
                    System.out.println("->");
                    escolha[i] = ent.nextInt();
                } while (escolha[i] > 6 || escolha[i] < 0);

                if (escolha[i] == 0) {
                    System.out.println("Sem uso por tempo indeterminado");
                } else if (escolha[i] == 1) {
                    System.out.println("Digite o nome\n-> ");
                    nome = ent.next();
                } else if (escolha[i] == 2) {
                    System.out.println("Digite o titulação\n-> ");
                    titulacao = ent.next();
                } else if (escolha[i] == 3) {
                    System.out.println("Digite o email\n-> ");
                    email = ent.next();
                } else if (escolha[i] == 4) {
                    System.out.println("Digite a departamento\n-> ");
                    departamento = ent.next();
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Exceção: " + e);
            System.out.println("O dado informado não condiz com o que se pede.");
        } finally {
            System.out.println("Processo encerrado.");
            ent.close();
        }
    }

    public static void buscarProfessor(Statement stm){
        Scanner ent = new Scanner(System.in);
        String n;
        //ALTERAR DEPOIS PARA BUSCAR PELO EMAIL
        System.out.print("Nome: ");
        n = ent.next();

        String sql = "select * from professor where nome ='" + n +"'";

        try {
            ResultSet result = stm.executeQuery(sql);
            while(result.next()){
                System.out.println("Professor encontrado");                 
            } 
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ent.close();
    }

    public static void listaProfessor(Statement stm){
        String sql = "select * from professor";

        try {
            ResultSet result = stm.executeQuery(sql);

            while(result.next()){
                System.out.println("Nome: " + result.getString("nome"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } 
    }

    /*
     * Um professor pode ministrar, no máximo, 20 horas-aula por semana
     * somando a carga horária dos seus componentes curriculares
     */

    // public void HorasAulaSemanal() { // obs.: banco de dados
    //     int aux;
    //     for (int i = 1; i < disciplina.getCargaHoraria(); i++) {
    //         aux = disciplina.getCargaHoraria() / i;
    //         if (aux == 15 && i <= 20) {
    //             System.out.println("O professor ensina " + i + " horas semanais.");
    //         }
    //     }
    // }

}
