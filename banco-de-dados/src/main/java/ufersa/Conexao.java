package ufersa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    public static Connection inicializaConexao() {
        try {
            //CONNECTION --> DEFINE OS METODOS PARA EXECUTAR UMA QUERY
            //DRIVERMANAGER --> COMUNICAÇÃO COM TODOS OS DRIVERS
            ///FAZ A PONTE DA PROGRAMAÇÃO COM O BANCO DE DADOS
            Connection conexao = DriverManager.getConnection("jdbc:postgresql://localhost:5432/gradehoraria","postgres", "123456");
            
            //VERIFICA SE A CONEXÃO DEU CERTO
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