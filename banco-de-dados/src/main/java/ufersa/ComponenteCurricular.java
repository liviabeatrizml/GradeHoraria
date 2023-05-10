package ufersa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;
import org.postgresql.util.PSQLException;

public class ComponenteCurricular {
    private String codigo_comp;
    private String nome;
    private int carga_horaria;
    private int semestre;
    private String categoria;
    private static ArrayList<ComponenteCurricular> listaDisciplinas = new ArrayList<>();
    private static Scanner ent = new Scanner(System.in);

    public ComponenteCurricular(String codigo_comp, String nome, int carga_horaria, int semestre, String categoria) {
        this.codigo_comp = codigo_comp;
        this.nome = nome;
        this.carga_horaria = carga_horaria;
        this.semestre = semestre;
        this.categoria = categoria;
    }

    public static void cadastrarComponente(Statement stm) {
        //INICIALIZAÇÃO DAS VARIÁVEIS
        String codigo_comp = "";
        String nome = "";
        String categoria = "";
        int carga_horaria = 0;
        int semestre = 0;
        int escolha = 1;

        //ESTRUTURA DE REPETIÇÃO PARA CASO O USUARIO QUEIRA REPETIR O PROCESSO DE CADASTRO
        do {
            System.out.println("Cadastrar Componente Curricular");
            System.out.print("Informe o código: ");
            codigo_comp = ent.next().toUpperCase();

            //VERIFICAÇÃO DE EXISTÊNCIA DE COMPONENTE PARA CASO ELE JÁ EXISTA NÃO TER COMO CADASTRAR
            while (buscarComponente(stm, codigo_comp) != null) {
                System.out.println("Componente já existe.");
                System.out.print("Informe um novo código: ");
                codigo_comp = ent.next().toUpperCase();
            }

            //CÓDIGO DO COMPONENTE SÓ É PERMITIDO 7 CARACTERE
            while (codigo_comp.length() != 7) {
                System.out.println("Código inválido.\nInforme o código corretamente: ");
                codigo_comp = ent.next().toUpperCase();
            }

            esvaziarBuffer(ent);

            System.out.print("Informe o nome: ");
            nome = ent.nextLine().toUpperCase();

            System.out.print("Informe a carga horária: ");
            carga_horaria = ent.nextInt();
                
            //VERIFICAÇÃO DE CARGA HORÁRIA, ACEITANDO APENAS 30, 60 E 90 HORAS/AULAS
            while (carga_horaria != 30 && carga_horaria != 60 && carga_horaria != 90) {
                System.out.println("Carga Horária inválida.\nInforme a carga horária corretamente: ");
                carga_horaria = ent.nextInt();
            }

            System.out.print("Informe o semestre: ");
            semestre = ent.nextInt();

            //VERIFICA SE O SEMESTRE ESTÁ ENTRE 1 E 6, CASO CONTRÁRIO NÃO ACEITA
            while (semestre <= 0 || semestre > 6) {
                System.out.println("Semestre inválido.\nInforme o semestre corretamente: ");
                semestre = ent.nextInt();
            }

            System.out.print("Informe a categoria (OBRIGATORIA OU OPCIONAL): ");
            categoria = ent.next().toUpperCase();

            while (!categoria.equals("OBRIGATORIA") && !categoria.equals("OPCIONAL")) {
                System.out.print("Categoria errada. Informe corretamente: ");
                categoria = ent.next().toUpperCase();
            }

            //COMANDO DE INSERÇÃO NO BANCO DE DADOS
            String sql = "insert into componente_curricular (codigo_comp, nome, carga_horaria, semestre, categoria) values ('"
                    + codigo_comp + "','" + nome + "'," + carga_horaria + "," + semestre + ", '" + categoria + "')";
            
            //CRIAÇÃO DE OBJETO ADICIONANDO AO ARRAY
             ComponenteCurricular disc = new ComponenteCurricular(codigo_comp, nome, carga_horaria, semestre, categoria);
            listaDisciplinas.add(disc);

            try {
                stm.executeUpdate(sql);
                System.out.println("\n-- Componente cadastrado --\n");
                System.out.print("Deseja cadastrar mais algum componente?\n0 - NÃO\n1 - SIM\n-> ");
                escolha = ent.nextInt();

                esvaziarBuffer(ent);

            } catch (SQLException e) {
                System.out.println("Falha no cadastro do componente.");
                e.printStackTrace();
            }
        } while (escolha != 0);

        System.out.println("Cadastro encerrado.");
    }

    public static ComponenteCurricular buscarComponente(Statement stm, String codigo_comp) {
        //METODO SEM EXIBIÇÃO
        //COMANDO DE BUSCA NO BANCO DE DADOS DE UM COMPONENTE PASSANDO UM CÓDIGO ESPECIFICO
        String sql = "select * from componente_curricular where codigo_comp = '" + codigo_comp + "'";

        try {
            ResultSet result = stm.executeQuery(sql);
            String codigo = "";
            String nome = "";
            String categoria = "";
            int carga_horaria = 0;
            int semestre = 0;

            //PERCORRENDO A SELEÇÃO FEITA E PEGANDO CADA ATRIBUTO PASSADO NELA
            while (result.next()) {
                codigo = result.getString("codigo_comp");
                nome = result.getString("nome");
                carga_horaria = result.getInt("carga_horaria");
                semestre = result.getInt("semestre");
                categoria = result.getString("categoria");
            }

            //SE O CÓDIGO FOR VAZIO O COMPONENTE NÃO EXISTE, CASO CONTRARIO CRIA-SE UM COMPONENTE TEMPORARIA QUE É PASSADO COMO RETORNO DA FUNÇÃO
            if (codigo == "") {
                return null;
            } else {
                ComponenteCurricular componenteTemporario = new ComponenteCurricular(codigo_comp, nome, carga_horaria, semestre, categoria);
                return componenteTemporario;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ComponenteCurricular verComponente(Statement stm) {
        System.out.println("Buscar Componente Curricular ");
        System.out.print("Informe o código do componente que se deseja buscar: ");
        String codigo_comp = ent.next().toUpperCase();
        return verComponente(stm, codigo_comp);
    }

    public static ComponenteCurricular verComponente(Statement stm, String codigo_comp) {
        //METODO COM EXIBIÇÃO
        //COMANDO DE BUSCA NO BANCO DE DADOS DE UM COMPONENTE PASSANDO UM CÓDIGO ESPECIFICO
        String sql = "select * from componente_curricular where codigo_comp = '" + codigo_comp + "'";

        try {
            ResultSet result = stm.executeQuery(sql);
            String codigo = "";
            String nome = "";
            String categoria = "";
            int carga_horaria = 0;
            int semestre = 0;

            //PERCORRENDO A SELEÇÃO FEITA E PEGANDO CADA ATRIBUTO PASSADO NELA
            //MOSTRA AS INFORMAÇÕES DO COMPONENTE ENCONTRADO
            while (result.next()) {
                System.out.println("\n-- Componente encontrado --");
                codigo = result.getString("codigo_comp");
                nome = result.getString("nome");
                carga_horaria = result.getInt("carga_horaria");
                semestre = result.getInt("semestre");
                categoria = result.getString("categoria");
                System.out.println("[ " + codigo + " | " + nome + " | " + carga_horaria + " | " + semestre + " | "
                        + categoria + " ]");
            }

            //SE NÃO EXISTIR CÓDIGO O COMPONENTE NÃO EXISTE, CASO CONTRARIO CRIA-SE UM COMPONENTE TEMPORARIA QUE É PASSADO COMO RETORNO DA FUNÇÃO
            if (codigo == "") {
                System.out.println("Componente não encontrado.");
                return null;
            } else {
                ComponenteCurricular componenteTemporario = new ComponenteCurricular(codigo_comp, nome, carga_horaria,
                        semestre, categoria);
                return componenteTemporario;
            }

        } catch (SQLException e) {
            System.out.println("Falha na busca do componente.");
            e.printStackTrace();
            return null;
        }
    }

    public static void listarComponente(Statement stm) {
        //METODO COM EXIBIÇÃO
        //LISTA TODOS OS COMPONENTES CURRICULARES CADASTRADO NO BANCO DE DADOS
        System.out.println("Listar Componente Curricular ");
        String sql = "select * from componente_curricular";

        try {
            ResultSet result = stm.executeQuery(sql);

            while (result.next()) {
                System.out.println("\n[ " + result.getString("codigo_comp") + " | " + result.getString("nome") + " | "+ result.getString("carga_horaria") + " | " + result.getString("semestre") + " | "+ result.getString("categoria") + " ]");
            }

        } catch (SQLException e) {
            System.out.println("Falha na listagem dos componentes.");
            e.printStackTrace();
        }
    }

    public static ArrayList<ComponenteCurricular> listaSemestre(Statement stm, int num_semestre) {
        //COMANDO DE BUSCA NO BANCO DE DADOS DE UM COMPONENTE PASSANDO UM SEMESTRE ESPECIFICO
        String sql = "select * from componente_curricular where semestre = " + num_semestre;
        ResultSet result;

        try {
            String codigo_comp = "";
            String nome = "";
            String categoria = "";
            int carga_horaria = 0;
            int semestre = 0;
            //ARRAY PARA GUARDAR OS COMPONENTES DO SEMESTRE ESPECIFICO
            ArrayList<ComponenteCurricular> disc = new ArrayList<>();

            result = stm.executeQuery(sql);

            //PERCORRENDO A SELEÇÃO FEITA E PEGA CADA ATRIBUTO PASSADO NELA
            while (result.next()) {
                codigo_comp = result.getString("codigo_comp");
                nome = result.getString("nome");
                carga_horaria = result.getInt("carga_horaria");
                semestre = result.getInt("semestre");
                categoria = result.getString("categoria");

                //SE O CÓDIGO FOR DIFERENTE DE VAZIO, EXISTE COMPONENTE REGISTRADO NAQUELE SEMESTRE E ESSE É COLOCADO NO ARRAY DE DISCIPLINAS DO SEMESTRE
                if (codigo_comp != "") {
                    ComponenteCurricular componenteTemporario = new ComponenteCurricular(codigo_comp, nome,
                            carga_horaria, semestre, categoria);
                    disc.add(componenteTemporario);
                }
            }

            //SE O ARRAY NÃO ESTIVER VAZIO, RETORNA O ARRAY PREENCHIDO
            if (!disc.isEmpty()) {
                return disc;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        //CASO NÃO EXISTA COMPONENTE REGISTRADO DAQUELE SEMESTRE RETORNA NULL
        return null;
    }

    public static void editarComponente(Statement stm) {
        //INICIALIZANDO AS VARIÁVEIS
        String codigo_ent = "";
        String nome = "";
        String categoria = "";
        int escolha = 0;
        int carga_horaria = 0;
        int semestre = 0;
        
        System.out.println("Editar Componente Curricular ");
        System.out.print("Informe o código do componente que se deseja editar: ");
        codigo_ent = ent.next().toUpperCase();

        //VERIFICA SE O COMPONENTE EXISTE PARA PODER ASSIM FAZER A EDIÇÃO
        if (verComponente(stm, codigo_ent) != null) {
            try {
                //ESTRUTURA DE REPETIÇÃO PARA CASO O USUARIO QUEIRA EDITAR MAIS DE UM ATRIBUTO DO MESMO COMPONENTE
                do {
                    System.out.print("-- MENU --\n1 - Nome\n2 - Carga Horária\n3 - Semestre\n4 - Categoria\n5 - Sair\n");
                    System.out.print("Informe o que se deseja editar: ");
                    escolha = ent.nextInt();

                    esvaziarBuffer(ent);

                    //COMANDO INICIAL DE ATUALIZAÇÃO DO BANCO DE DADOS
                    String alterInit = "update componente_curricular set ";

                    //VALIDA A ESCOLHA E ENTRA NO CASO ESCOLHIDO PARA FAZER A EDIÇÃO
                    switch (escolha) {
                        case 1:
                            System.out.print("Informe o novo nome: ");
                            nome = ent.nextLine().toUpperCase();
                            alterInit += "nome = '" + nome + "' where codigo_comp = '" + codigo_ent + "'";
                            stm.executeUpdate(alterInit);
                            break;
                        case 2:
                            System.out.print("Informe a nova carga horária: ");
                            carga_horaria = ent.nextInt();
                            while (carga_horaria != 30 && carga_horaria != 60 && carga_horaria != 90) {
                                System.out.print("Carga Horária inválida.\nInforme a carga horária corretamente: ");
                                carga_horaria = ent.nextInt();
                            }
                            alterInit += "carga_horaria = " + carga_horaria + " where codigo_comp = '" + codigo_ent+ "'";
                            stm.executeUpdate(alterInit);
                            break;
                        case 3:
                            System.out.print("Informe o novo semestre: ");
                            semestre = ent.nextInt();
                            while (semestre <= 0 || semestre > 6) {
                                System.out.print("Semestre inválido.\nInforme o semestre corretamente: ");
                                semestre = ent.nextInt();
                            }
                            alterInit += "semestre = " + semestre + " where codigo_comp = '" + codigo_ent + "'";
                            stm.executeUpdate(alterInit);
                            break;
                        case 4:
                            System.out.print("Informe a nova categoria: ");
                            categoria = ent.next().toUpperCase();
                            while (!categoria.equals("OBRIGATORIA") && !categoria.equals("OPCIONAL")) {
                                System.out.print("Categoria errada. Informe corretamente: ");
                                categoria = ent.next().toUpperCase();
                            }
                            alterInit += "categoria = '" + categoria + "' where codigo_comp = '" + codigo_ent + "'";
                            stm.executeUpdate(alterInit);
                            break;
                        default:
                            System.out.println("Edição encerrada.");
                            break;
                    }
                } while (escolha >= 1 && escolha < 5);

            } catch (SQLException e) {
                System.out.println("Falha na edição do componente.");
                e.printStackTrace();
            } 
        }
    }

    public static void excluirComponente(Statement stm) {
        String codigo_comp = "";
        int escolha = 1;

        //ESTRUTURA DE REPETIÇÃO PARA CASO O USUARIO QUEIRA REPETIR O PROCESSO DE EXCLUSÃO
        do {
            System.out.println("Excluir Componente Curricular ");
            System.out.print("Informe o código do componente que se deseja excluir: ");
            codigo_comp = ent.next().toUpperCase();

            //VERIFICA SE O COMPONENTE EXISTE PARA PODER FAZER A EXCLUSÃO
            if (verComponente(stm, codigo_comp) != null) {
                //COMANDO DE EXCLUSÃO NO BANCO DE DADOS DE UM COMPONENTE PASSANDO UM CÓDIGO ESPECIFICO
                String sql = "delete from componente_curricular where codigo_comp = '" + codigo_comp + "'";

                try {
                    stm.executeUpdate(sql);
                    System.out.println("\n-- Componente excluído --");
                    System.out.print("\nDeseja excluir mais algum componente?\n0 - NÃO\n1 - SIM\n-> ");
                    escolha = ent.nextInt();

                    esvaziarBuffer(ent);

                    //TRATAMENTO DE EXCEÇÃO CASO ELE EXCLUA UM COMPONENTE QUE TENHA REGISTRADO TURMA
                } catch (PSQLException e) {
                    System.out.println(
                            "Falha ao excluir componente, pois o código do componente viola a restrição de chave estrangeira em outra tabela.\n");            
                } catch (SQLException e) {
                    System.out.println("Falha na exclusão do componente.\n");
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

    @Override
    public String toString() {
        return "ComponenteCurricular [codigo_comp=" + codigo_comp + ", nome=" + nome + ", carga_horaria="
                + carga_horaria + ", semestre=" + semestre + "]";
    }
}