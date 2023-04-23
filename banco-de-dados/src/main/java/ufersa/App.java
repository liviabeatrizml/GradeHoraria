package ufersa;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class App {
    public static void main( String[] args ){
        try {
            Connection bancodedados = Conexao.inicializaConexao();
            Statement stm = bancodedados.createStatement();

            Professor.cadastrarProfessor(stm);
            //Professor.excluirProfessor(stm);
            //Professor.buscarProfessor(stm);
            //Professor.listaProfessor(stm);
            

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
