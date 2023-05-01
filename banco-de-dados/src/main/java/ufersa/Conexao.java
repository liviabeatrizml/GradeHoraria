package ufersa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    public static Connection inicializaConexao() {
        try {
            Connection conexao = DriverManager.getConnection("jdbc:postgresql://localhost:5432/gradehoraria","postgres", "123456");
            if (conexao != null) {
                System.out.println("Banco de dados conectado.");
                return conexao;
            } else {
                System.out.println("Erro de conexão.");
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
