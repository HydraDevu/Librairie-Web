
package classes;

import java.io.Serializable;

public class MotsClefs implements Serializable{
    
    private int idMotsClefs;
    private String nomMotsClefs;

    public int getIdMotsClefs() {
        return idMotsClefs;
    }

    public void setIdMotsClefs(int idMotsClefs) {
        this.idMotsClefs = idMotsClefs;
    }

    public String getNomMotsClefs() {
        return nomMotsClefs;
    }

    public void setNomMotsClefs(String nomMotsClefs) {
        this.nomMotsClefs = nomMotsClefs;
    }
    
    
    
}
