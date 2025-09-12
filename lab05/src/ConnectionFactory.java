
import java.sql.*;
public class ConnectionFactory {
    private ConnectionFactory (){

    }
    public  static Connection getConnection(String url){
        try {
            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println("Erro ao obter conexão!");
            return null;
        }
    }
}
