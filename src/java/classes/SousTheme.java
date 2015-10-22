
package classes;

public class SousTheme {
    
        private int idSousTheme;
        private String nomSousTheme;
        private Theme ThemeSousTheme;

        
    public SousTheme(){
        this.ThemeSousTheme = new Theme();
    } 

        
    public SousTheme(int idSousTheme, String nomSousTheme, Theme ThemeSousTheme) {
        this.idSousTheme = idSousTheme;
        this.nomSousTheme = nomSousTheme;
        this.ThemeSousTheme = ThemeSousTheme;
    }

    public SousTheme(int idSousTheme, String nomSousTheme) {
        this.idSousTheme = idSousTheme;
        this.nomSousTheme = nomSousTheme;
    }
     
    public int getIdSousTheme() {
        return idSousTheme;
    }

    public void setIdSousTheme(int idSousTheme) {
        this.idSousTheme = idSousTheme;
    }

    public String getNomSousTheme() {
        return nomSousTheme;
    }

    public void setNomSousTheme(String nomSousTheme) {
        this.nomSousTheme = nomSousTheme;
    }

    public Theme getThemeSousTheme() {
        return ThemeSousTheme;
    }

    public void setThemeSousTheme(Theme ThemeSousTheme) {
        this.ThemeSousTheme = ThemeSousTheme;
    }

    @Override
    public String toString() {
        return nomSousTheme;
    }
        
        
    
}
