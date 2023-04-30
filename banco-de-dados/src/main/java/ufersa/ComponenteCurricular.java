package ufersa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class ComponenteCurricular {
    private String codigo_comp;
    private String nome;
    private int carga_horaria;
    private int semestre;
    private static ArrayList<ComponenteCurricular> listaDisciplinas = new ArrayList<>();

    public ComponenteCurricular(String codigo_comp, String nome, int carga_horaria, int semestre) {
        this.codigo_comp = codigo_comp;
        this.nome = nome;
        this.carga_horaria = carga_horaria;
        this.semestre = semestre;
    }

    public static void cadastrarComponente(Statement stm) {
        String c = "", n = "";
        int ch, s;
        int escolha = 1;
        Scanner ent = new Scanner(System.in);

        do{
            System.out.println("Cadastrar Componente Curricular");
            System.out.print("Informe o código: ");
            c = ent.next();

            //fazendo a seleção para saber se tem algum componente com o mesmo codigo 
            try {
                ResultSet result = stm.executeQuery("select * from componente_curricular where codigo_comp = '" + c + "'");
                while (result.next()) {
                    //dando erro quando digitado algum já colocado e excluida
                    do{
                        System.out.println("Componente já existente.");
                        System.out.println("Informe um novo código: ");
                        c = ent.next();   
                    }while(c != result.getString("codigo_comp"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            if(c == "" || c.length() != 7){
                System.out.println("Código inválido.\nInforme o codigo corretamente: ");
                c = ent.next();
            }   
            
            //verificar validação nome ARRUMAR ESSE CARAI AQUI
            try {
                System.out.print("Informe o nome: ");
                n = ent.next();
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.print("Informe a carga horária: ");
            ch = ent.nextInt();
                while(ch != 30 && ch != 60 && ch != 90){
                    System.out.println("Carga Horária inválida.\nInforme a carga horária corretamente: ");
                    ch = ent.nextInt();
                }

            System.out.print("Informe o semestre: ");
            s = ent.nextInt();
                while(s <= 0 && s > 6){
                    System.out.println("Semestre inválido.\nInforme o semestre corretamente: ");
                    s = ent.nextInt();
                }

            String sql = "insert into componente_curricular (codigo_comp, nome, carga_horaria, semestre) values ('" + c + "','" + n + "','" + ch + "','" + s + "')";

            ComponenteCurricular disc = new ComponenteCurricular(c, n, ch, s);
            listaDisciplinas.add(disc);

            try {
                stm.executeUpdate(sql);  
                System.out.println("\nComponente cadastrado.");
                System.out.print("Deseja cadastrar mais algum componente?\n0 - NÃO\n1 - SIM\n-> ");
                escolha = ent.nextInt();
                esvaziarBuffer(ent);
            } catch (SQLException e) {
                ent.close();
                System.out.println("Falha no cadastro do componente");
                e.printStackTrace();
            }
        }while(escolha != 0);
    }

    public static void excluirComponente(Statement stm) {
        Scanner ent = new Scanner(System.in);
        int escolha = 1;
        do{
            System.out.println("Excluir Componente Curricular ");
            System.out.print("Informe o código do componente que se deseja excluir: ");
            String c = ent.next();

            String sql = "delete from componente_curricular where codigo_comp = '" + c + "'";
            //ver se ele esta na array NAO CONSIGOOOOOOOOO

            try {
                stm.executeUpdate(sql);
                System.out.println("Componente excluído!");
                System.out.print("Deseja excluir mais algum componente?\n0 - NÃO\n1 - SIM\n-> ");
                escolha = ent.nextInt();
                esvaziarBuffer(ent);
            } catch (SQLException e) {
                ent.close();
                System.out.println("Falha na exclusão do componente");
                e.printStackTrace();
            }
        }while(escolha != 0);
        
    }

    public static void buscarComponente(Statement stm) {
        Scanner ent = new Scanner(System.in);
        System.out.println("Buscar Componente Curricular ");
        System.out.print("Informe o código do componente que se deseja buscar: ");
        String c = ent.next();
        
        //verificar se ele ta nas disciplinas
        String sql = "select * from componente_curricular where codigo_comp = '" + c + "'";

        try {
            ResultSet result = stm.executeQuery(sql);
            //if(!sql.isEmpty()){
                while (result.next()) {
                    System.out.println("Componente encontrado.");
                    System.out.println(result.getString("codigo_comp") + " | " + result.getString("nome") + " | " + result.getString("carga_horaria") + " | " + result.getString("semestre"));
                }
            //} else { 
                //ele nao entra nesse else mdssss ESSA PORRAAAAAAAAAAAAAA
                //System.out.println("Componente não encontrado");
            //}
            
        } catch (SQLException e) {
            ent.close();
            System.out.println("Falha na busca do componente");
            e.printStackTrace();
        }
    }

    public static void listaComponente(Statement stm) {
        System.out.println("Listar Componente Curricular ");
        String sql = "select * from componente_curricular";

        try {
            ResultSet result = stm.executeQuery(sql);

            while (result.next()) {
                System.out.println("\n" + result.getString("codigo_comp") + " | " + result.getString("nome") + " | " + result.getString("carga_horaria") + " | " + result.getString("semestre"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void editarComponente(Statement stm) {
        Scanner ent = new Scanner(System.in);
        System.out.println("Editar Componente Curricular ");
        String codEnt;
        int escolha = 0;
        String cod_comp = "";
        String nome = "";

        System.out.print("Informe o código do componente que se deseja editar: ");
        codEnt = ent.next();

        try {
            do{
                System.out.print("MENU\n0 - Código\n1 - Nome\n2 - Carga Horária\n3 - Semestre\nUse qualquer outra tecla para encerrar\n");
                System.out.print("Informe o atributo que se deseja editar: ");
                escolha = ent.nextInt();
                esvaziarBuffer(ent);

                String alterInit = "update componente_curricular set ";

                switch(escolha){
                    case 0:   
                        System.out.print("Informe o novo código: ");
                        cod_comp = ent.next();

                            if(cod_comp == "" || cod_comp.length() != 7){
                                System.out.println("Código inválido.\nInforme o codigo corretamente: ");
                                cod_comp = ent.next();
                            }   

                        alterInit += "codigo_comp = '" + cod_comp + "' where codigo_comp = '" + codEnt + "'"; 
                        codEnt = cod_comp;
                        stm.executeUpdate(alterInit);
                    break;

                    case 1:
                        System.out.println("Informe o novo nome: ");
                        nome = ent.nextLine();  
                        alterInit += "nome = '" + nome + "' where codigo_comp = '" + codEnt + "'";
                        stm.executeUpdate(alterInit);
                    break;

                    case 2: 
                    System.out.println("Informe a nova carga horária: ");
                    int ch = ent.nextInt();

                        if(ch != 30 && ch != 60 && ch != 90){
                            System.out.println("Carga Horária inválida.\nInforme a carga horária corretamente: ");
                            ch = ent.nextInt();
                        }

                    alterInit += "carga_horaria = '" + ch + "' where codigo_comp = '" + codEnt + "'";
                    stm.executeUpdate(alterInit);
                    break;

                    case 3:
                    System.out.println("Informe o novo semestre: ");
                    int s = ent.nextInt();

                        if(s < 0 || s > 6){
                            System.out.println("Semestre inválido.\nInforme o semestre corretamente: ");
                            s = ent.nextInt(); 
                        }

                    alterInit += "semestre = '" + s + "' where codigo_comp = '" + codEnt + "'";
                    stm.executeUpdate(alterInit);
                    break;

                    default:
                        System.out.println("Edição encerrada");
                    break;
                }
            }while(escolha >= 0 && escolha < 4);
        } catch (SQLException e) {
            System.out.println("Falha na edição do componente");
            ent.close();
            e.printStackTrace();
        }
    }

    public String getCodigo_comp() {
        return codigo_comp;
    }

    public void setCodigo_comp(String codigo_comp) {
        this.codigo_comp = codigo_comp;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getCarga_horaria() {
        return carga_horaria;
    }

    public void setCarga_horaria(int carga_horaria) {
        this.carga_horaria = carga_horaria;
    }

    public int getSemestre() {
        return semestre;
    }

    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }

    private static void esvaziarBuffer(Scanner ent){
        if(ent.hasNextLine()){
            ent.nextLine();
        }
    }
    
    @Override
    public String toString() {
        return "ComponenteCurricular [codigo_comp=" + codigo_comp + ", nome=" + nome + ", carga_horaria="
                + carga_horaria + ", semestre=" + semestre + "]";
    }
}
