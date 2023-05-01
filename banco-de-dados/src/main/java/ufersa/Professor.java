package ufersa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class Professor {
    private String nome;
    private String titulacao;
    private String email;
    private int horasSemanais = 0;
    private static ArrayList<Professor> listaProfessor = new ArrayList<>();
    ArrayList<ComponenteCurricular> disciplinas;

    public Professor(String nome, String titulacao, String email) {
        this.nome = nome;
        this.titulacao = titulacao;
        this.email = email;
        this.horasSemanais = 0;
    }

    public static void cadastrarProfessor(Statement stm){
        //MUDAR A VARIAVEL "C" DEPOIS 
        String email;
        int escolha = 1;
        Scanner ent = new Scanner(System.in);

        do {
            System.out.println("Cadastrar Professor");
            System.out.print("Informe o email: ");
            email = ent.next();

            while (buscarProfessor(stm, email) == null) {
                System.out.println("Professor já existe.");
                System.out.print("Informe um novo email: ");
                email = ent.next();
            }

            System.out.print("Informe o nome: ");
            String nome = "";
            nome = ent.next();

            System.out.print("Informe a titulação: ");
            String titul = "";
            titul = ent.next();

            String sql = "insert into professor (nome, titulacao, email) values ('" + nome + "','" + titul + "','" + email + "')";

            Professor prof = new Professor(nome, titul, email);
            listaProfessor.add(prof);

            try {
                stm.executeUpdate(sql);
                System.out.println("\nProfessor cadastrado.");
                System.out.print("Deseja cadastrar mais algum professor?\n0 - NÃO\n1 - SIM\n-> ");
                escolha = ent.nextInt();
                esvaziarBuffer(ent);
            } catch (SQLException e) {
                ent.close();
                System.out.println("Falha no cadastro do professor.");
                e.printStackTrace();
            }
        } while (escolha != 0);
    }

    public static Professor buscarProfessor(Statement stm) {
        Scanner ent = new Scanner(System.in);
        System.out.println("Buscar Professor ");
        System.out.print("Informe o código do professor que se deseja buscar: ");
        String c = ent.next();
        ent.close();

        return buscarProfessor(stm, c);
    }

    public static Professor buscarProfessor(Statement stm, String email) {
        String sql = "select * from professor where email = '" + email + "'";

        try {
            ResultSet result = stm.executeQuery(sql);
            String n = "";
            String tit = "";
            String em = "";

            while (result.next()) {
                System.out.println("Professor encontrado.");
                n = result.getString("nome");
                tit = result.getString("titulacao");
                em = result.getString("email");
                System.out.println(result.getString("nome") + " | " + result.getString("titulacao") + " | " + result.getString("email"));
            }

            if(email.equals(em)){
                return null;
            } else {
                Professor prof = new Professor(n, tit, em);
                return prof;
            }

        } catch (SQLException e) {
            System.out.println("Falha na busca do professor.");
            e.printStackTrace();
            return null;
        }
    }

    public static void excluirProfessor(Statement stm) {
        Scanner ent = new Scanner(System.in);
        int escolha = 1;
        do {
            System.out.println("Excluir Professor ");
            System.out.print("Informe o email do professor que se deseja excluir: ");
            String em = ent.next();

            String sql = "delete from professor where email = '" + em + "'";

            try {
                stm.executeUpdate(sql);
                System.out.println("Professor excluído!");
                System.out.print("Deseja excluir mais algum professor?\n0 - NÃO\n1 - SIM\n-> ");
                escolha = ent.nextInt();
                esvaziarBuffer(ent);
            } catch (SQLException e) {
                ent.close();
                System.out.println("Falha na exclusão do professor.");
                e.printStackTrace();
            }
        } while (escolha != 0);
    }

    public static void editarProfessor(Statement stm) {
        Scanner ent = new Scanner(System.in);
        System.out.println("Editar Professor ");
        String emailEnt;
        int escolha = 0;
        String nome = "";

        System.out.print("Informe o email do professor que se deseja editar: ");
        emailEnt = ent.next();

        try {
            do {
                System.out.print(
                        "MENU\n1 - Nome\n2 - Titulação\nUse qualquer outra tecla para encerrar\n");
                System.out.print("Informe o atributo que se deseja editar: ");
                escolha = ent.nextInt();
                esvaziarBuffer(ent);

                String alterInit = "update professor set ";

                switch (escolha) {
                    case 1:
                        System.out.println("Informe o novo nome: ");
                        nome = ent.nextLine();
                        alterInit += "nome = '" + nome + "' where email = '" + emailEnt + "'";
                        stm.executeUpdate(alterInit);
                        break;

                    case 2:
                        System.out.println("Informe a nova titulação: ");
                        String titul = ent.next();

                        alterInit += "titulacao = '" + titul + "' where email = '" + emailEnt + "'";
                        stm.executeUpdate(alterInit);
                        break;

                    default:
                        System.out.println("Edição encerrada.");
                        break;
                }
            } while (escolha >= 0 && escolha < 4);
        } catch (SQLException e) {
            System.out.println("Falha na edição do componente.");
            ent.close();
            e.printStackTrace();
        }
    }

    private static void esvaziarBuffer(Scanner ent) {
        if (ent.hasNextLine()) {
            ent.nextLine();
        }
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

    public int getHorasSemanais() {
        return horasSemanais;
    }

    public void setHorasSemanais(int horasSemanais) {
        this.horasSemanais = horasSemanais;
    }

    @Override
    public String toString() {
        return "Professor [nome=" + nome + ", titulacao=" + titulacao + ", email=" + email + ", horasSemanais="
                + horasSemanais + "]";
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
