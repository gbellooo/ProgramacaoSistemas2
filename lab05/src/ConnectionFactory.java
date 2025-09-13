import java.sql.*;

public class ConnectionFactory {
    private ConnectionFactory(){}

    public static Connection getConnection(String url){
        try {
            // Para Postgres, o driver é carregado automaticamente nas versões novas.
            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println("Erro ao obter conexão! " + e.getMessage());
            return null;
        }
    }
}
