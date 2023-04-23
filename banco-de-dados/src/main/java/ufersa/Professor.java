package ufersa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Scanner;

public class Professor {
    private String matricula;
    private String nome;
    private String titulacao;
    private String email;
    private String departamento;
    private ComponenteCurricular disciplina;
    private static LinkedList<Professor> listaProfessor = new LinkedList<>();

    //Metodos do banco de dados
    public static void cadastrarProfessor(Statement stm){
        Scanner ent = new Scanner(System.in);
        String m, n, t, em, d;
        System.out.print("Matricula: ");
        m = ent.next();
        System.out.print("Nome: ");
        n = ent.next();
        System.out.print("Titulacao: ");
        t = ent.next();
        System.out.print("Email: ");
        em = ent.next();
        System.out.print("Departamento: ");
        d = ent.next();

        String sql = "insert into professor (matricula, nome, titulacao, email, departamento) values ('" + m + "','" + n + "','" + t + "','" + em + "','" + d + "')";

        try {
            stm.executeUpdate(sql);
        } catch (SQLException e) {
            ent.close();
            e.printStackTrace();
        }
    }

    public static void excluirProfessor(Statement stm){
        Scanner ent = new Scanner(System.in);
        String m, n;
        System.out.print("Matricula: ");
        m = ent.next();
        System.out.print("Nome: ");
        n = ent.next();

        String sql = "delete from professor where matricula = '" + m + "' and nome ='" + n +"'";

        try {
            stm.executeUpdate(sql);
        } catch (SQLException e) {
            ent.close();
            e.printStackTrace();
        }

    }

    public void editarProfessor() {
        String matricula, nome, titulacao, email, departamento;
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
                    System.out.println("Digite a matricula\n-> ");
                    matricula = ent.next();
                    setMatricula(matricula);
                } else if (escolha[i] == 1) {
                    System.out.println("Digite o nome\n-> ");
                    nome = ent.next();
                    setNome(nome);
                } else if (escolha[i] == 2) {
                    System.out.println("Digite o titulação\n-> ");
                    titulacao = ent.next();
                    setTitulacao(titulacao);
                } else if (escolha[i] == 3) {
                    System.out.println("Digite o email\n-> ");
                    email = ent.next();
                    setEmail(email);
                } else if (escolha[i] == 4) {
                    System.out.println("Digite a departamento\n-> ");
                    departamento = ent.next();
                    setDepartamento(departamento);
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
        String m, n;
        System.out.print("Matricula: ");
        m = ent.next();
        System.out.print("Nome: ");
        n = ent.next();

        String sql = "select * from professor where matricula = '" + m + "' and nome ='" + n +"'";

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

    public void HorasAulaSemanal() { // obs.: banco de dados
        int aux;
        for (int i = 1; i < disciplina.getCargaHoraria(); i++) {
            aux = disciplina.getCargaHoraria() / i;
            if (aux == 15 && i <= 20) {
                System.out.println("O professor ensina " + i + " horas semanais.");
            }
        }
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTitulacao() {
        return titulacao;
    }

    public void setTitulacao(String titulacao) {
        this.titulacao = titulacao;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public static LinkedList<Professor> getListaProfessor() {
        return listaProfessor;
    }

    public static void setListaProfessor(LinkedList<Professor> listaProfessor) {
        Professor.listaProfessor = listaProfessor;
    }

    @Override
    public String toString() {
        return "Professor [matricula=" + matricula + ", nome=" + nome + ", titulacao=" + titulacao + ", email=" + email
                + ", departamento=" + departamento + "]";
    }

}
