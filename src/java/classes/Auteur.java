package classes;

import beans.beanLivre;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Auteur {

    private int idAuteur;
    private String nomAuteur;
    private String prenomAuteur;
    private String biographieAuteur;
    private String dateNaissanceAuteur;
    private String dateDecesAuteur;

    private HashMap<String, beanLivre> livreAuteur = new HashMap();

    public Auteur() {
    }

    public Auteur(int idAuteur, String nomAuteur, String prenomAuteur, String biographieAuteur, String dateNaissanceAuteur, String dateDecesAuteur) {
        this();
        this.idAuteur = idAuteur;
        this.nomAuteur = nomAuteur;
        this.prenomAuteur = prenomAuteur;
        this.biographieAuteur = biographieAuteur;
        this.dateNaissanceAuteur = dateNaissanceAuteur;
        this.dateDecesAuteur = dateDecesAuteur;

    }

    public int getIdAuteur() {
        return idAuteur;
    }

    public void setIdAuteur(int idAuteur) {
        this.idAuteur = idAuteur;
    }

    public String getNomAuteur() {
        return nomAuteur;
    }

    public void setNomAuteur(String nomAuteur) {
        this.nomAuteur = nomAuteur;
    }

    public String getPrenomAuteur() {
        return prenomAuteur;
    }

    public void setPrenomAuteur(String prenomAuteur) {
        this.prenomAuteur = prenomAuteur;
    }

    public String getBiographieAuteur() {
        return biographieAuteur;
    }

    public void setBiographieAuteur(String biographieAuteur) {
        this.biographieAuteur = biographieAuteur;
    }

    public String getDateNaissanceAuteur() {
        return dateNaissanceAuteur;
    }

    public void setDateNaissanceAuteur(String dateNaissanceAuteur) {
        this.dateNaissanceAuteur = dateNaissanceAuteur;
    }

    public String getDateDecesAuteur() {
        return dateDecesAuteur;
    }

    public void setDateDecesAuteur(String dateDecesAuteur) {
        this.dateDecesAuteur = dateDecesAuteur;
    }

    public HashMap getLivreAuteur() {
        return livreAuteur;
    }

    public void setLivreAuteur(HashMap livreAuteur) {
        this.livreAuteur = livreAuteur;
    }

    @Override
    public String toString() {
        return nomAuteur + " " + prenomAuteur;
    }

    public Collection<beanLivre> Coll() {
        return livreAuteur.values();
    }

}
