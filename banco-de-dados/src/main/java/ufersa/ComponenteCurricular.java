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
    private String categoria;
    private static ArrayList<ComponenteCurricular> listaDisciplinas = new ArrayList<>();

    public ComponenteCurricular(String codigo_comp, String nome, int carga_horaria, int semestre, String categoria) {
        this.codigo_comp = codigo_comp;
        this.nome = nome;
        this.carga_horaria = carga_horaria;
        this.semestre = semestre;
        this.categoria = categoria;
    }

    public static void cadastrarComponente(Statement stm) {
        String c, categ;
        int ch, s, escolha = 1;
        Scanner ent = new Scanner(System.in);

        do {
            System.out.println("Cadastrar Componente Curricular");
            System.out.print("Informe o código: ");
            c = ent.next();

            while (buscarComponente(stm, c) != null) {
                System.out.println("Componente já existe.");
                System.out.print("Informe um novo código: ");
                c = ent.next();
            }

            if (c == "" || c.length() != 7) {
                System.out.println("Código inválido.\nInforme o codigo corretamente: ");
                c = ent.next();
            }

            System.out.print("Informe o nome: ");
            String n = "";
            n = ent.next();

            System.out.print("Informe a carga horária: ");
            ch = ent.nextInt();
            while (ch != 30 && ch != 60 && ch != 90) {
                System.out.println("Carga Horária inválida.\nInforme a carga horária corretamente: ");
                ch = ent.nextInt();
            }

            System.out.print("Informe o semestre: ");
            s = ent.nextInt();
            while (s <= 0 || s > 6) {
                System.out.println("Semestre inválido.\nInforme o semestre corretamente: ");
                s = ent.nextInt();
            }

            System.out.print("Informe a categoria (Obrigatoria ou Opcional): ");
            categ = ent.next(); 

            //REVER A VERIFICACAO DO WHILE

            while(categ != "Obrigatoria" && categ != "Opcional"){
                System.out.print("Categoria errada. Informe corretamente: ");
                categ = ent.next();
            }

            String sql = "insert into componente_curricular (codigo_comp, nome, carga_horaria, semestre) values ('" + c + "','" + n + "','" + ch + "','" + s + "' '" + categ + "')";

            ComponenteCurricular disc = new ComponenteCurricular(c, n, escolha, s, categ);
            System.out.println(listaDisciplinas.add(disc));

            try {
                stm.executeUpdate(sql);
                System.out.println("\nComponente cadastrado.");
                System.out.print("Deseja cadastrar mais algum componente?\n0 - NÃO\n1 - SIM\n-> ");
                escolha = ent.nextInt();
                esvaziarBuffer(ent);
            } catch (SQLException e) {
                ent.close();
                System.out.println("Falha no cadastro do componente.");
                e.printStackTrace();
            }
        } while (escolha != 0);

    }

    public static void excluirComponente(Statement stm) {
        Scanner ent = new Scanner(System.in);
        int escolha = 1;
        do {
            System.out.println("Excluir Componente Curricular ");
            System.out.print("Informe o código do componente que se deseja excluir: ");
            String c = ent.next();

            String sql = "delete from componente_curricular where codigo_comp = '" + c + "'";

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
        } while (escolha != 0);

    }

    public static ComponenteCurricular buscarComponente(Statement stm) {
        Scanner ent = new Scanner(System.in);
        System.out.println("Buscar Componente Curricular ");
        System.out.print("Informe o código do componente que se deseja buscar: ");
        String c = ent.next();
        ent.close();

        return buscarComponente(stm, c);
    }

    public static ComponenteCurricular buscarComponente(Statement stm, String codigo) {
        // verificar se ele ta nas disciplinas
        String sql = "select * from componente_curricular where codigo_comp = '" + codigo + "'";

        try {
            ResultSet result = stm.executeQuery(sql);
            String cod = "";
            String nome = "";
            String categ = "";
            int car_hor = 0;
            int sem = 0;

            while (result.next()) {
                System.out.println("Componente encontrado.");
                cod = result.getString("codigo_comp");
                nome = result.getString("nome");
                car_hor = result.getInt("carga_horaria");
                sem = result.getInt("semestre");
                categ = result.getString("categoria");
                System.out.println(result.getString("codigo_comp") + " | " + result.getString("nome") + " | " + result.getString("carga_horaria") + " | " + result.getString("semestre") + " | " + result.getString("categoria"));
            }

            if(cod == ""){
                return null;
            } else {
                ComponenteCurricular cc = new ComponenteCurricular(codigo, nome, car_hor, sem, categ);
                return cc;
            }

        } catch (SQLException e) {
            System.out.println("Falha na busca do componente");
            e.printStackTrace();
            return null;
        }
    }

    public static void listaComponente(Statement stm) {
        System.out.println("Listar Componente Curricular ");
        String sql = "select * from componente_curricular";

        try {
            ResultSet result = stm.executeQuery(sql);

            while (result.next()) {
                System.out.println("\n" + result.getString("codigo_comp") + " | " + result.getString("nome") + " | " + result.getString("carga_horaria") + " | " + result.getString("semestre") + " | " + result.getString("categoria"));
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
        String nome = "";

        System.out.print("Informe o código do componente que se deseja editar: ");
        codEnt = ent.next();

        try {
            do {
                System.out.print(
                        "MENU\n1 - Nome\n2 - Carga Horária\n3 - Semestre\nUse qualquer outra tecla para encerrar\n");
                System.out.print("Informe o atributo que se deseja editar: ");
                escolha = ent.nextInt();
                esvaziarBuffer(ent);

                String alterInit = "update componente_curricular set ";

                switch (escolha) {
                    case 1:
                        System.out.println("Informe o novo nome: ");
                        nome = ent.nextLine();
                        alterInit += "nome = '" + nome + "' where codigo_comp = '" + codEnt + "'";
                        stm.executeUpdate(alterInit);
                        break;

                    case 2:
                        System.out.println("Informe a nova carga horária: ");
                        int ch = ent.nextInt();

                        if (ch != 30 && ch != 60 && ch != 90) {
                            System.out.println("Carga Horária inválida.\nInforme a carga horária corretamente: ");
                            ch = ent.nextInt();
                        }

                        alterInit += "carga_horaria = '" + ch + "' where codigo_comp = '" + codEnt + "'";
                        stm.executeUpdate(alterInit);
                        break;

                    case 3:
                        System.out.println("Informe o novo semestre: ");
                        int s = ent.nextInt();

                        if (s < 0 || s > 6) {
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
            } while (escolha >= 0 && escolha < 4);
        } catch (SQLException e) {
            System.out.println("Falha na edição do componente");
            ent.close();
            e.printStackTrace();
        }
    }

    public static void HorasAulas(int carga_horaria) {
        int aux;
        for (int i = 1; i < carga_horaria; i++) {
            aux = carga_horaria / i;
            if (aux == 15) {
                System.out.println(i + " horas/aulas semanal");
            }
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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    private static void esvaziarBuffer(Scanner ent) {
        if (ent.hasNextLine()) {
            ent.nextLine();
        }
    }

    @Override
    public String toString() {
        return "ComponenteCurricular [codigo_comp=" + codigo_comp + ", nome=" + nome + ", carga_horaria="
                + carga_horaria + ", semestre=" + semestre + "]";
    }
}
