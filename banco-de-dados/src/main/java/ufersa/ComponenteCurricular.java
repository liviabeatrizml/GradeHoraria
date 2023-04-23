package ufersa;

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

    public void inicializaComponente(String codigo, String nome, int cargaHoraria, String descricao, int periodo) {
        this.codigo = codigo;
        this.nome = nome;
        this.cargaHoraria = cargaHoraria;
        this.descricao = descricao;
        this.periodo = periodo;
    }

    public static ComponenteCurricular cadastrarComponente(String c, String n, int ch, String d, int p){
        ComponenteCurricular disciplina = new ComponenteCurricular();
        disciplina.inicializaComponente(c, n, ch, d, p);
        System.out.println("Componente cadastrado.");
        listaComponente.add(disciplina);
        return disciplina;
    }

    public void excluirComponente(String c){
        if(!listaComponente.isEmpty()){
            if(c == codigo){
                listaComponente.remove();
                System.out.println("Componente removido.");
            } else {
                System.out.println("Componente não encontrado.");
            }
        }
    }

    public void editarComponente(){
        int cargaHoraria, periodo;
        String codigo, nome, descricao;
        Scanner ent = new Scanner(System.in);
        int escolha[] = new int[6];

        try{
            for (int i = 0; i < escolha.length; i++) {
                System.out.println("MENU\n");
                System.out.println("O que você deseja editar: ");

                do{
                    System.out.println("->");
                    escolha[i] = ent.nextInt();
                } while(escolha[i] > 6 || escolha[i] < 0);

                if(escolha[i] == 0){
                    System.out.println("Digite o código\n-> ");
                    codigo = ent.next();
                    setCodigo(codigo);
                } else if(escolha[i] == 1){
                    System.out.println("Digite a carga horária\n-> ");
                    cargaHoraria = ent.nextInt();
                    setCargaHoraria(cargaHoraria);
                } else if(escolha[i] == 2){
                    System.out.println("Digite o período\n-> ");
                    periodo = ent.nextInt();
                    setPeriodo(periodo);
                } else if(escolha[i] == 3){
                    System.out.println("Digite o nome\n-> ");
                    nome = ent.next();
                    setNome(nome);
                } else if(escolha[i] == 4){
                    System.out.println("Digite a descrição\n-> ");
                    descricao = ent.next();
                    setNome(descricao);
                } else {
                    break;
                }
            }
        } catch (Exception e){
            System.out.println("Exceção: " + e);
            System.out.println("O dado informado não condiz com o que se pede.");
        } finally {
            System.out.println("Processo encerrado.");
            ent.close();
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
