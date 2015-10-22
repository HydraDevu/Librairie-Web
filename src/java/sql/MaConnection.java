package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;


public class MaConnection {
    
   private static MaConnection mc = null;
    private Connection cnt;
    
    public static synchronized MaConnection getInstance() throws ClassNotFoundException, SQLException{
        if(mc ==null){
            mc = new MaConnection();
        }    
        return mc;
    }
    
       
    private MaConnection() throws ClassNotFoundException, SQLException{
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String url = "jdbc:sqlserver://localhost:1433;"
                    + "databaseName=Librairie";
            String user = "sa";
            String mdp = "sa";
            cnt = DriverManager.getConnection(url, user, mdp);
            
            
    }
    
    
    public Statement createStatement() throws SQLException {
        Statement stm = cnt.createStatement();
        return stm;
    }

    public PreparedStatement prepareStatement(String req) throws SQLException {
        PreparedStatement pstm = cnt.prepareStatement(req);
        return pstm;
    }
    
}
