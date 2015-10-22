
package sql;

import classes.SousTheme;
import classes.Theme;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ReqTheme {

    
    public List<Theme> getTheme() throws ClassNotFoundException, SQLException {
        
        Map<String, List<String>> map = new HashMap();
        
        List<Theme> listTheme = new ArrayList();
        
        MaConnection mc = MaConnection.getInstance();
        
        String req= "select idTheme, nomTheme from Theme";
        
        Statement stm = mc.createStatement();
        
        ResultSet rs = stm.executeQuery(req);
        
        while(rs.next()) {
            
            Theme t = new Theme();
            
            t.setIdTheme(rs.getInt("idTheme"));
            t.setNomTheme(rs.getString("nomTheme"));
            
            listTheme.add(t);
            
            
        }
        
        rs.close();
        stm.close();
        
//        ma partie sous theme :
        
        
String reqSousTheme = "select sst.idSousTheme, sst.idTheme AS idThemeSst, sst.nomSousTheme , t.idTheme, t.nomTheme "
                    + "from SousTheme sst "
                    + "join theme t on sst.idTheme = t.idTheme "
                    + "where t.idTheme = ? ";

//Statement stm = mc.createStatement();

PreparedStatement pstm = mc.prepareStatement(reqSousTheme);

        for (Theme t : listTheme) {
            pstm.setInt(1, t.getIdTheme());
            ResultSet rs01 = pstm.executeQuery();
            while(rs01.next()){
                SousTheme sst  = new SousTheme();
                sst.setIdSousTheme(rs01.getInt("idSousTheme"));
                sst.setNomSousTheme(rs01.getString("nomSousTheme"));
                sst.getThemeSousTheme().setIdTheme(rs01.getInt("idThemeSst"));
                sst.getThemeSousTheme().setNomTheme(rs01.getString("nomSousTheme"));
                
                t.getSousThemeTheme().add(sst);
            }
            rs01.close();
        }
            pstm.close();

        
        
        
        return listTheme;
        

        
    }
    
    public List<SousTheme> getSousTheme(int id) throws ClassNotFoundException, SQLException {
        
        
        
        List<SousTheme> listsousTheme = new ArrayList();
        
        MaConnection mc = MaConnection.getInstance();
        
        
String reqSousTheme = "select sst.idSousTheme, sst.idTheme AS idThemeSst, sst.nomSousTheme , t.idTheme, t.nomTheme "
                    + "from SousTheme sst "
                    + "join theme t on sst.idTheme = t.idTheme "
                    + "where t.idTheme = ? ";

//Statement stm = mc.createStatement();

PreparedStatement pstm = mc.prepareStatement(reqSousTheme);

pstm.setInt(1, id);

//ResultSet rs01 = stm.executeQuery(reqSousTheme);
ResultSet rs01 = pstm.executeQuery();

while(rs01.next()){
                    SousTheme sst  = new SousTheme();
                    sst.setIdSousTheme(rs01.getInt("idSousTheme"));
                    sst.setNomSousTheme(rs01.getString("nomSousTheme"));
                    sst.getThemeSousTheme().setIdTheme(rs01.getInt("idThemeSst"));
                    sst.getThemeSousTheme().setNomTheme(rs01.getString("nomTheme"));
                    
                   listsousTheme.add(sst);
                }
            
        
        return listsousTheme;
    }
    
    public static void main(String[] args) {

        
        System.out.println("THEME");
        ReqTheme req = new ReqTheme();
        try {
            for(Theme t : req.getTheme()) {
                
                System.out.println("theme : " + t.getNomTheme() + "\n");
                
                System.out.println("sous theme --------------- \n");
                for (Iterator it = t.getSousThemeTheme().iterator(); it.hasNext();) {
                    SousTheme sst = (SousTheme) it.next();
                    System.out.println(sst.getNomSousTheme());
                }
                System.out.println("fin sous theme --------------- \n");
                System.out.println("THEME");

            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ReqTheme.class.getName()).log(Level.SEVERE, null, ex);
        }
        
//        System.out.println("SOUS THEME");
//        ReqTheme req01 = new ReqTheme();
//        try {
//            for(SousTheme sst : req01.getSousTheme(1)) {
//                System.out.println("nom Theme");
//                 System.out.println(sst.getThemeSousTheme().getNomTheme() + "\n");
//                 System.out.println("nom sous Theme");
//                System.out.println(sst.getNomSousTheme() + "\n");
//                System.out.println("id Theme");
//                System.out.println(sst.getThemeSousTheme().getIdTheme() + "\n");
//               
//                
//            }
//        } catch (ClassNotFoundException | SQLException ex) {
//            Logger.getLogger(ReqTheme.class.getName()).log(Level.SEVERE, null, ex);
//        }
        
    }
    
}
