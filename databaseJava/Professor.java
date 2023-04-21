import java.util.LinkedList;
import java.util.Scanner;

public class Professor{
    private String matricula;
    private String nome;
    private String titulacao;
    private String email;
    private String departamento;
    private static LinkedList<Professor> listaProfessor = new LinkedList<>();

    public void inicializaProfessor(String matricula, String nome, String titulacao, String email, String departamento) {
        this.matricula = matricula;
        this.nome = nome;
        this.titulacao = titulacao;
        this.email = email;
        this.departamento = departamento;
    }

    public static Professor cadastrarProfessor(String m, String n, String t, String e, String d){
        Professor prof = new Professor();
        prof.inicializaProfessor(m,n,t,e,d);
        System.out.println("Professor cadastrado.");
        listaProfessor.add(prof);
        return prof;
    }

    public void excluirProfessor(String m, String n){
        if(!listaProfessor.isEmpty()){
            if(m == matricula && n == nome){
                listaProfessor.remove();
                System.out.println("Professor removido.");
            } else {
                System.out.println("Professor não encontrado.");
            }
        }
    }

    public void editarProfessor(){
        String matricula, nome, titulacao, email, departamento;
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
                    System.out.println("Digite a matricula\n-> ");
                    matricula = ent.next();
                    setMatricula(matricula);
                } else if(escolha[i] == 1){
                    System.out.println("Digite o nome\n-> ");
                    nome = ent.next();
                    setNome(nome);
                } else if(escolha[i] == 2){
                    System.out.println("Digite o titulação\n-> ");
                    titulacao = ent.next();
                    setTitulacao(titulacao);
                } else if(escolha[i] == 3){
                    System.out.println("Digite o email\n-> ");
                    email = ent.next();
                    setEmail(email);
                } else if(escolha[i] == 4){
                    System.out.println("Digite a departamento\n-> ");
                    departamento = ent.next();
                    setDepartamento(departamento);
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

    public boolean validaProfessor(String m, String n){
        return (m == matricula && n == nome) ? true : false;
    }

    public static void buscarComponente(String c, String n){
        for (int i = 0; i < listaProfessor.size(); i++) {
            if(listaProfessor.get(i).validaProfessor(c, n)){
                System.out.println(listaProfessor);
            } else {
                System.out.println("Professor não existe.");
            }
        }
    }

    public static void listaProfessor(){
        System.out.println(listaProfessor);
    }

    // public void HorasAulaSemanal(){ //obs.: banco de dados
    //     int aux;
    //     for (int i = 1; i < getCargaHoraria(); i++) {
    //         aux = getCargaHoraria()/i;
    //         if(aux == 15){
    //             System.out.println(i + " horas/aulas semanal");
    //         }
    //     }
    // }

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
}
