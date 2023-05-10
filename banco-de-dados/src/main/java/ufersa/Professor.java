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
    private static Scanner ent = new Scanner(System.in);

    public Professor(String nome, String titulacao, String email) {
        this.nome = nome;
        this.titulacao = titulacao;
        this.email = email;
        this.horasSemanais = 0;
    }

    public static void cadastrarProfessor(Statement stm) {
        //INICIALIZAÇÃO DAS VARIÁVEIS
        String nome = "";
        String titulacao = "";
        String email = "";
        int escolha = 1;

        //ESTRUTURA DE REPETIÇÃO PARA CASO O USUARIO QUEIRA REPETIR O PROCESSO DE CADASTRO
        do {
            System.out.println("Cadastrar Professor");
            System.out.print("Informe o nome do usuário de email: ");
            email = ent.next().toLowerCase();
            email += "@ufersa.edu.br";

            //VERIFICAÇÃO DE EXISTÊNCIA DE PROFESSOR PARA CASO ELE JÁ EXISTA NÃO TER COMO CADASTRAR
            while (buscarProfessor(stm, email) != null) {
                System.out.println("Professor já registrado.");
                System.out.print("Informe o novo nome do usuário de email: ");
                email = ent.next().toLowerCase();
                email += "@ufersa.edu.br";
            }

            esvaziarBuffer(ent);

            System.out.print("Informe o nome do professor: ");
            nome = ent.nextLine().toUpperCase();

            System.out.print("Informe a titulação: ");
            titulacao = ent.next().toUpperCase();

            //COMANDO DE INSERÇÃO NO BANCO DE DADOS
            String sql = "insert into professor (nome, titulacao, email) values ('" + nome + "','" + titulacao + "','"
                    + email + "')";

            //CRIAÇÃO DE OBJETO ADICIONANDO AO ARRAY
            Professor prof = new Professor(nome, titulacao, email);
            listaProfessor.add(prof);

            try {
                stm.executeUpdate(sql);
                System.out.println("\n-- Professor cadastrado --");
                System.out.print("\nDeseja cadastrar mais algum professor?\n0 - NÃO\n1 - SIM\n-> ");
                escolha = ent.nextInt();

                esvaziarBuffer(ent);

            } catch (SQLException e) {
                System.out.println("Falha no cadastro do professor.");
                e.printStackTrace();
            }
        } while (escolha != 0);
        System.out.println("Cadastro encerrado.");
    }

    public static Professor buscarProfessor(Statement stm, String email) {
        //METODO SEM EXIBIÇÃO
        //COMANDO DE BUSCA NO BANCO DE DADOS DE UM PROFESSOR PASSANDO UM EMAIL ESPECIFICO
        String sql = "select * from professor where email = '" + email + "'";

        try {
            ResultSet result = stm.executeQuery(sql);
            String nome = "";
            String titulacao = "";
            String em = "";

            //PERCORRENDO A SELEÇÃO FEITA E PEGA CADA ATRIBUTO PASSADO NELA
            while (result.next()) {
                nome = result.getString("nome");
                titulacao = result.getString("titulacao");
                em = result.getString("email");
            }

            //SE O EMAIL FOR IGUAL O EMAIL PASSADO É PORQUE O PROFESSOR EXISTE, CASO CONTRARIO O RETORNO É NULL
            if (email.equals(em)) {
                Professor professorTemporario = new Professor(nome, titulacao, email);
                return professorTemporario;
            } else {
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Professor verProfessor(Statement stm) {
        System.out.println("Buscar Professor ");
        System.out.print("Informe o nome de usuario de email do professor que se deseja buscar: ");
        String userEmail = ent.next().toLowerCase();
        userEmail += "@ufersa.edu.br";

        return verProfessor(stm, userEmail);
    }

    public static Professor verProfessor(Statement stm, String email) {
        //METODO COM EXIBIÇÃO
        //COMANDO DE BUSCA NO BANCO DE DADOS DE UM PROFESSOR PASSANDO UM EMAIL ESPECIFICO
        String sql = "select * from professor where email = '" + email + "'";

        try {
            ResultSet result = stm.executeQuery(sql);
            String nome = "";
            String titulacao = "";
            String em = "";

            //PERCORRENDO A SELEÇÃO FEITA E PEGA CADA ATRIBUTO PASSADO NELA
            //MOSTRA AS INFORMAÇÕES DO PROFESSOR ENCONTRADO
            while (result.next()) {
                System.out.println("\n-- Professor encontrado --");
                nome = result.getString("nome");
                titulacao = result.getString("titulacao");
                em = result.getString("email");
                System.out.println("[ " + nome + " | " + titulacao + " | " + em + " ]");
            }

            //SE O EMAIL FOR IGUAL O EMAIL PASSADO É PORQUE O PROFESSOR EXISTE, CASO CONTRARIO O RETORNO É NULL
            if (email.equals(em)) {
                Professor professorTemporario = new Professor(nome, titulacao, email);
                return professorTemporario;
            } else {
                return null;
            }

        } catch (SQLException e) {
            System.out.println("Falha na busca do professor.");
            e.printStackTrace();
            return null;
        }

    }

    public static void listarProfessor(Statement stm) {
        //METODO COM EXIBIÇÃO
        //LISTA TODOS OS PROFESSORES CADASTRADO NO BANCO DE DADOS
        System.out.println("Listar Professor ");
        String sql = "select * from professor";

        try {
            ResultSet result = stm.executeQuery(sql);

            while (result.next()) {
                System.out.println("\n[ " + result.getString("nome") + " | " + result.getString("titulacao") + " | "
                        + result.getString("email") + " ]");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void editarProfessor(Statement stm) {
        //INICIALIZANDO AS VARIÁVEIS
        String email = "";
        String nome = "";
        String titulacao = "";
        int escolha = 0;

        System.out.println("Editar Professor ");
        System.out.print("Informe o nome de usuário do email que se deseja editar: ");
        email = ent.next().toLowerCase();
        email += "@ufersa.edu.br";

        //VERIFICA SE O PROFESSOR EXISTE PARA PODER ASSIM FAZER A EDIÇÃO
        if (verProfessor(stm, email) != null) {
            try {
                //ESTRUTURA DE REPETIÇÃO PARA CASO O USUARIO QUEIRA EDITAR MAIS DE UM ATRIBUTO DO MESMO PROFESSOR
                do {
                    System.out.print("-- MENU --\n1 - Nome\n2 - Titulação\n3 - Sair\n");
                    System.out.print("Informe o que se deseja editar: ");
                    escolha = ent.nextInt();

                    esvaziarBuffer(ent);

                    //COMANDO INICIAL DE ATUALIZAÇÃO DO BANCO DE DADOS
                    String alterInit = "update professor set ";

                    //VALIDA A ESCOLHA E ENTRA NO CASO ESCOLHIDO PARA FAZER A EDIÇÃO
                    switch (escolha) {
                        case 1:
                            System.out.print("Informe o novo nome: ");
                            nome = ent.nextLine().toUpperCase();
                            alterInit += "nome = '" + nome + "' where email = '" + email + "'";
                            stm.executeUpdate(alterInit);
                            break;
                        case 2:
                            System.out.print("Informe a nova titulação: ");
                            titulacao = ent.next().toUpperCase();
                            alterInit += "titulacao = '" + titulacao + "' where email = '" + email + "'";
                            stm.executeUpdate(alterInit);
                            break;
                        default:
                            System.out.println("Edição encerrada.");
                            break;
                    }
                } while (escolha >= 1 && escolha < 3);

            } catch (SQLException e) {
                System.out.println("Falha na edição do componente.");
                e.printStackTrace();
            }
        }
    }

    public static void excluirProfessor(Statement stm) {
        String email = "";
        int escolha = 1;

        do {
            System.out.println("Excluir Professor ");
            System.out.print("Informe o nome de usuário do email que se deseja excluir: ");
            email = ent.next().toLowerCase();
            email += "@ufersa.edu.br";

            //VERIFICA SE O PROFESSOR EXISTE PARA PODER FAZER A EXCLUSÃO
            if (verProfessor(stm, email) != null) {
                //COMANDO DE EXCLUSÃO NO BANCO DE DADOS DE UM PROFESSOR PASSANDO UM EMAIL ESPECIFICO
                String sql = "delete from professor where email = '" + email + "'";

                try {
                    stm.executeUpdate(sql);
                    System.out.println("\n-- Professor excluído --");
                    System.out.print("\nDeseja excluir mais algum professor?\n0 - NÃO\n1 - SIM\n-> ");
                    escolha = ent.nextInt();

                    esvaziarBuffer(ent);
                    
                } catch (SQLException e) {
                    System.out.println("Falha na exclusão do professor.");
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
}