import java.util.Scanner;

public class ComponenteCurricular {
    private int codigo;
    private String nome;
    private int cargaHoraria;
    private String descricao;// optativa ou obrigatoria
    private int periodo;

    public boolean cadastrarComponente(int codigo, String nome, int cargaHoraria, String descricao, int periodo) {
        // ver questão do banco de dados
        return true;
    }

    public void editarComponente() {
        int codigo, cargaHoraria, periodo;
        String nome, descricao;
        Scanner ent = new Scanner(System.in);
        int escolha[] = new int[6]; // realizar o try catch para caso digite diferente das opções

        try {

            for (int i : escolha) {
                System.out.println("\n0 - codigo\n1 - carga horária\n2 - periodo\n3 - nome\n4 - descricao\n5 - encerrar");
                System.out.print("O que deseja editar: \n");

                do{
                    System.out.print("-> ");
                    escolha[i] = ent.nextInt();
                } while(escolha[i] > 6 || escolha[i] < 0);
                
                if (escolha[i] == 0) {
                    System.out.print("Digite o codigo\n-> ");
                    codigo = ent.nextInt();
                } else if (escolha[i] == 1) {
                    System.out.print("Digite a carga horaria\n-> ");
                    cargaHoraria = ent.nextInt();
                } else if (escolha[i] == 2) {
                    System.out.print("Digite o periodo\n-> ");
                    periodo = ent.nextInt();
                } else if (escolha[i] == 3) {
                    System.out.print("Digite o nome\n-> ");
                    nome = ent.next();
                } else if (escolha[i] == 4) {
                    System.out.print("Digite a descricao\n-> ");
                    descricao = ent.next();
                } else if (escolha[i] == 5) {
                    break;
                } else{
                    System.out.println("Você informou um número inexistente do menu. Por favor, tente novamente.");
                    break;
                }
            }

        } catch (Exception e) {
            System.out.println("Excecao: " + e);
            System.out.println("O dado informado nao condiz com o que se pede");
        } finally {
            System.out.println("Processo encerrado");
            ent.close();
        }
    }

    public static void main(String[] args) {
        ComponenteCurricular cc = new ComponenteCurricular();
        cc.editarComponente();
        // System.out.println(cc.cadastrarComponente(1, "POO", 60, "Disciplina do demo",
        // 4));

    }

}
