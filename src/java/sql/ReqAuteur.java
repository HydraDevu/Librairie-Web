package sql;

import classes.Auteur;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReqAuteur {

public static Auteur getAuteurInf(String id) throws ClassNotFoundException, SQLException{

    Auteur a = new Auteur();
    
    MaConnection mc = MaConnection.getInstance();
    
    String req = "Select * from Auteur"
                + " Where idAuteur = ?";
    
    PreparedStatement pstm = mc.prepareStatement(req);
    pstm.setString(1, id);
    ResultSet rs = pstm.executeQuery();
    
    while(rs.next()){
    a.setIdAuteur(rs.getInt("idAuteur"));
    a.setNomAuteur(rs.getString("NomAuteur"));
    a.setPrenomAuteur(rs.getString("PrenomAuteur"));
    a.setBiographieAuteur(rs.getString("BiographieAuteur"));
    a.setDateNaissanceAuteur(rs.getString("DateNaissanceAuteur"));
    a.setDateDecesAuteur(rs.getString("DateDecesAuteur"));
    }
    System.out.println(a);
    return a;
              
}
    
   
    public ReqAuteur() {
       
    }
    
    
}
