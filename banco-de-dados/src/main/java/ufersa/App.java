package ufersa;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class App {
    public static void main( String[] args ){
        try {
            Connection bancodedados = Conexao.inicializaConexao();
            Statement stm = bancodedados.createStatement();

            //COMPONENTE CURRICULAR
            ComponenteCurricular.editarComponente(stm);

            //PROFESSOR
            //Professor.cadastrarProfessor(stm);
            //Professor.excluirProfessor(stm);
            //Professor.buscarProfessor(stm);
            //Professor.listaProfessor(stm);
            //

            //TURMA
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}