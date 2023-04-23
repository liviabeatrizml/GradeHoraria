package ufersa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Scanner;

public class ComponenteCurricular{
    private String codigo;
    private String nome;
    private int cargaHoraria;
    private String descricao;
    private int periodo;
    private static LinkedList<ComponenteCurricular> listaComponente = new LinkedList<>();

    public static void cadastrarComponente(Statement stm){
        Scanner ent = new Scanner(System.in);
        String cod, nome, desc;
        int carHor, per;
        System.out.print("Código: ");
        cod = ent.next();
        System.out.print("Nome: ");
        nome = ent.next();
        System.out.print("Carga Horária: ");
        carHor = ent.nextInt();
        System.out.print("Descrição: ");
        desc = ent.next();
        System.out.print("Período: ");
        per = ent.nextInt();

        String sql = "insert into professor (codigo, nome, cargaHoraria, descricao, periodo) values ('" + cod + "','" + nome + "','" + carHor + "','" + desc + "','" + per + "')";

        try {
            stm.executeUpdate(sql);
        } catch (SQLException e) {
            ent.close();
            e.printStackTrace();
        }
    }

    public void excluirComponente(Statement stm){
        Scanner ent = new Scanner(System.in);
        String cod;
        System.out.print("Código: ");
        cod = ent.next();

        String sql = "delete from professor where codigo = '" + cod + "'";

        try {
            stm.executeUpdate(sql);
        } catch (SQLException e) {
            ent.close();
            e.printStackTrace();
        }
    }

    public static void editarComponente(Statement stm){
        int carHor, per;
        String cod, nome, desc, codInit;
        Scanner ent = new Scanner(System.in);
        int[] escolha = new int[6];

        System.out.print("Informe o código do componente que se deseja editar: ");
        codInit = ent.next();

        String sql = "select * from componentecurricular where codigo = '" + codInit + "'";
        

        try {
            ResultSet result = stm.executeQuery(sql);
            while(result.next()){

                for (int i = 0; i < escolha.length; i++) {
                    System.out.print("MENU\n0 - Código\n1 - Nome\n2 - Carga Horaria\n3 - Descricao\n4 - Periodo\n");
                    System.out.print("Informe o atributo que se deseja editar: ");

                    do{
                    //System.out.println("->");
                    escolha[i] = ent.nextInt();
                } while(escolha[i] > 6 || escolha[i] < 0);

                if(escolha[i] == 0){
                    System.out.print("Informe o código: \n-> ");
                    cod = ent.next();
                    
                    String alterInit = "update componentecurricular set codigo = '" + codInit + "' where codigo = '" + cod + "'";
                    //System.out.println(alterInit);
                    
                    stm.executeUpdate(alterInit);

                    //setCodigo(codigo);
                // } else if(escolha[i] == 1){
                //     System.out.println("Informe o nome: \n-> ");
                //     nome = ent.next();
                //     //setCargaHoraria(cargaHoraria);
                // } else if(escolha[i] == 2){
                //     System.out.println("Digite o período\n-> ");
                //     per = ent.nextInt();
                //     //setPeriodo(periodo);
                // } else if(escolha[i] == 3){
                //     System.out.println("Digite o nome\n-> ");
                //     nome = ent.next();
                //     //setNome(nome);
                // } else if(escolha[i] == 4){
                //     System.out.println("Digite a descrição\n-> ");
                //     desc = ent.next();
                //     //setNome(descricao);
                 } else {
                     break;
                }
            }
                //}

            }
            result.close();
            ent.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

    public boolean validarComponente(String c, String n){
        return (c == codigo && n == nome) ? true : false;
    }

    public static void buscarComponente(String c, String n){
        for (int i = 0; i < listaComponente.size(); i++) {
            if(listaComponente.get(i).validarComponente(c, n)){
                System.out.println(listaComponente);
            } else {
                System.out.println("Componente não existe.");
            }
        }
    }

    public static void listaComponete(){
        System.out.println(listaComponente);
    }

    public void HorasAulaSemanal(){ //obs.: banco de dados
        int aux;
        for (int i = 1; i < getCargaHoraria(); i++) {
            aux = getCargaHoraria()/i;
            if(aux == 15){
                System.out.println(i + " horas/aulas semanal");
            }
        }
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(int cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }

    public static LinkedList<ComponenteCurricular> getListaComponente() {
        return listaComponente;
    }

    public static void setListaComponente(LinkedList<ComponenteCurricular> listaComponente) {
        ComponenteCurricular.listaComponente = listaComponente;
    }

    @Override
    public String toString() {
        return "ComponenteCurricular [codigo=" + codigo + ", nome=" + nome + ", cargaHoraria=" + cargaHoraria
                + ", descricao=" + descricao + ", periodo=" + periodo + "]";
    }

}
