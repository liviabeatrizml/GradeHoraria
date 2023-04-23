package ufersa;

import java.util.LinkedList;
import java.util.Scanner;

public class Turma {
    private String nome;
    private int ano;
    private int semestre;
    private String diasDaSemana;
    private String horarios;
    private String turno;
    private String bloco;
    private int vagas;

    private static LinkedList<Turma> listaTurma = new LinkedList<>();

    public void inicializaTurma(String nome, int ano, int semestre, String diasDaSemana, String horarios, String turno, String bloco,
            int vagas) {
        this.nome = nome;
        this.ano = ano;
        this.semestre = semestre;
        this.diasDaSemana = diasDaSemana;
        this.horarios = horarios;
        this.turno = turno;
        this.bloco = bloco;
        this.vagas = vagas;
    }

    public static Turma cadastrarTurma(String n, int a, int s, String ds, String h, String t, String b){
        Turma turma = new Turma();
        turma.inicializaTurma(n, a, s, n, h, t, b, s);
        System.out.println("Turma cadastrada.");
        listaTurma.add(turma);
        return turma;
    }

    public void excluirTurma(String n, int a, int s){
        if(!listaTurma.isEmpty()){
            if(n == nome && a == ano && s == semestre){
                listaTurma.remove();
                System.out.println("Turma removida.");
            } else {
                System.out.println("Não existe turma cadastrada.");
            }
        }
    }

    public boolean validarTurma(String n, int a, int s){
        return (n == nome && a == ano && s == semestre) ? true : false;
    }

    public static void buscarTurma(){

    }

    public void editarTurma(){
        String nome, diasDaSemana, horarios, turno, bloco;
        int ano, semestre, vagas;

        Scanner ent = new Scanner(System.in);
        int[] escolha = new int[6];

        try {
            for (int i = 0; i < escolha.length; i++) {
                
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public int getSemestre() {
        return semestre;
    }

    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }

    public String getDiasDaSemana() {
        return diasDaSemana;
    }

    public void setDiasDaSemana(String diasDaSemana) {
        this.diasDaSemana = diasDaSemana;
    }

    public String getHorarios() {
        return horarios;
    }

    public void setHorarios(String horarios) {
        this.horarios = horarios;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getBloco() {
        return bloco;
    }

    public void setBloco(String bloco) {
        this.bloco = bloco;
    }

    public int getVagas() {
        return vagas;
    }

    public void setVagas(int vagas) {
        this.vagas = vagas;
    }

    public static LinkedList<Turma> getListaTurma() {
        return listaTurma;
    }

    public static void setListaTurma(LinkedList<Turma> listaTurma) {
        Turma.listaTurma = listaTurma;
    } 

    @Override
    public String toString() {
        return "Turma [nome=" + nome + ", ano=" + ano + ", semestre=" + semestre + ", diasDaSemana=" + diasDaSemana
                + ", horarios=" + horarios + ", turno=" + turno + ", bloco=" + bloco + ", vagas=" + vagas + "]";
    }   

}