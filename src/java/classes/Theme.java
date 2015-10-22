package classes;

import java.util.ArrayList;
import java.util.List;

public class Theme {
    
    private int idTheme;
    private String nomTheme;
    
    private List<SousTheme> sousThemeTheme;
    
        
    public Theme() {
        this.sousThemeTheme = new ArrayList<SousTheme>();
    }

    public Theme(int idTheme, String nomTheme) {
        this();
        this.idTheme = idTheme;
        this.nomTheme = nomTheme;
        
    }


    public int getIdTheme() {
        return idTheme;
    }

    public void setIdTheme(int idTheme) {
        this.idTheme = idTheme;
    }

    public String getNomTheme() {
        return nomTheme;
    }

    public void setNomTheme(String nomTheme) {
        this.nomTheme = nomTheme;
    }

    public List getSousThemeTheme() {
        return sousThemeTheme;
    }

    public void setSousThemeTheme(List sousThemeTheme) {
        this.sousThemeTheme = sousThemeTheme;
    }

    @Override
    public String toString() {
        return nomTheme;
    }


    
    
    
}
