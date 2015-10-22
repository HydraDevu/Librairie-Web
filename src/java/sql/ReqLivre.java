/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sql;

import beans.beanLivre;
import classes.Auteur;
import classes.MotsClefs;
//import classes.MotsClefs;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReqLivre {

    public List<beanLivre> getListeLivre(int depart, int nextItem) throws ClassNotFoundException, SQLException {

        List<beanLivre> liste = new ArrayList();

        MaConnection mc = MaConnection.getInstance();

        String req = "SELECT l.isbn13Livre, "
                + "quantiteLivre, "
                + "dateLivre, "
                + " poidsLivre, "
                + "imageLivre, "
                + "prixLivre, "
                + "titreLivre, "
                + " sousTitreLivre, "
                + "resumeLivre, "
                + "edit.idEditeur as idEdit, edit.nomEditeur, " //dans livre
                + "a.idAuteur, a.nomAuteur, a.prenomAuteur, a.biographieAuteur, a.dateDecesAuteur, a.dateNaissanceAuteur, "
                + "sst.idSousTheme, sst.nomSousTheme, "
                + "tva.idTva, tva.nomTauxTva, tva.valeurTauxTva "
                + "FROM Livre as l "
                //editeur
                + "LEFT JOIN Editeur as edit "
                + "ON edit.idEditeur = l.idEditeur "
                //fin editeur

                //auteur
                + "LEFT JOIN Ecriture as e "
                + "ON e.isbn13Livre = l.isbn13Livre "
                + "LEFT JOIN Auteur as a "
                + "ON a.idAuteur = e.idAuteur "
                //fin auteur

                //tva
                + "LEFT JOIN ApplicationTva as ap "
                + "ON ap.isbn13Livre = l.isbn13Livre "
                + "LEFT JOIN Tva as tva "
                + "ON tva.idTva = ap.idTva "
                //fin tva

                //sous theme
                + "LEFT JOIN Appartenance as app "
                + "ON l.isbn13Livre = app.isbn13Livre "
                + "LEFT JOIN SousTheme as sst "
                + "ON sst.idSousTheme = app.idSousTheme "
                //fin sous theme
                + "WHERE actifLivre = 1 " //  AND l.isbn13Livre = '9782011691699'
                + "ORDER BY titreLivre asc offset ? rows fetch next ? rows only";

        System.out.println(req);

        PreparedStatement pstm = mc.prepareStatement(req);

        pstm.setInt(1, depart);
        pstm.setInt(2, nextItem);

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {

            beanLivre livre = new beanLivre();

            livre.setIdLivre(rs.getString("isbn13Livre"));
            livre.setTitreLivre(rs.getString("titreLivre"));
            livre.setSousTitreLivre(rs.getString("sousTitreLivre"));
            livre.setResumeLivre(rs.getString("resumeLivre"));
            livre.setPoidsLivre(rs.getFloat("poidsLivre"));
            livre.setDateLivre(rs.getString("dateLivre"));
            livre.setImageLivre(rs.getString("imageLivre"));
            livre.setQuantiteLivre(rs.getInt("quantiteLivre"));
            livre.setPrixHTLivre(rs.getFloat("prixLivre"));
            livre.getEditeurLivre().setIdEditeur(rs.getInt("idEdit"));
            livre.getEditeurLivre().setNomEditeur(rs.getString("nomEditeur"));
            livre.getAuteurLivre().setIdAuteur(rs.getInt("idAuteur"));
            livre.getAuteurLivre().setNomAuteur(rs.getString("nomAuteur"));
            livre.getAuteurLivre().setPrenomAuteur(rs.getString("prenomAuteur"));
            livre.getAuteurLivre().setBiographieAuteur(rs.getString("biographieAuteur"));
            livre.getAuteurLivre().setDateNaissanceAuteur(rs.getString("dateNaissanceAuteur"));
            livre.getAuteurLivre().setDateDecesAuteur(rs.getString("dateDecesAuteur"));
            livre.getSousThemeLivre().setIdSousTheme(rs.getInt("idSousTheme"));
            livre.getSousThemeLivre().setNomSousTheme(rs.getString("nomSousTheme"));
            livre.getTvaLivre().setIdTva(rs.getInt("idTva"));
            livre.getTvaLivre().setNomTauxTva(rs.getString("nomTauxTva"));
            livre.getTvaLivre().setValeurTauxTva(rs.getFloat("valeurTauxTva"));

            liste.add(livre);

        }

//        rs.close();
//        pstm.close();
        System.out.println("fin requete liste");
//        list mot clé a inserer ds livre

        String reqMotCle = "select mc.idMotsClefs AS idMotCle, mc.nomMotsClefs, carac.idMotsClefs, carac.isbn13Livre, l.isbn13Livre "
                + "from MotsClefs mc "
                + "join Caracterisation carac on carac.idMotsClefs = mc.idMotsClefs "
                + "join Livre l on l.isbn13Livre = carac.isbn13Livre "
                + "where l.isbn13Livre = ?";

        System.out.println(reqMotCle);

        PreparedStatement pstm01 = mc.prepareStatement(reqMotCle);

        for (beanLivre l : liste) {
            pstm01.setString(1, l.getIdLivre());
            ResultSet rs01 = pstm01.executeQuery();
            System.out.println("avant le while");
            while (rs01.next()) {
                MotsClefs motCl = new MotsClefs();
                motCl.setIdMotsClefs(rs01.getInt("idMotsClefs"));
                motCl.setNomMotsClefs(rs01.getString("nomMotsClefs"));

                l.getMotsClefs().add(motCl);
            }
            rs01.close();
        }
        pstm01.close();

        return liste;

    }

    public int nbrTotalArticle() throws ClassNotFoundException, SQLException {

        int nbrTotal = 0;

        MaConnection mc = MaConnection.getInstance();

        String req = "select COUNT(*) AS nbrTotalArt from Livre ";

        Statement stm = mc.createStatement();

        ResultSet rs = stm.executeQuery(req);

        while (rs.next()) {
            nbrTotal = rs.getInt("nbrTotalArt");
        }

        return nbrTotal;
    }

    public List<beanLivre> getListeLivreByCat(int depart, int nextItem, String sousTheme) throws ClassNotFoundException, SQLException {

        List<beanLivre> liste = new ArrayList();

        MaConnection mc = MaConnection.getInstance();

        String req = "SELECT l.isbn13Livre, "
                + "quantiteLivre, "
                + "dateLivre, "
                + " poidsLivre, "
                + "imageLivre, "
                + "prixLivre, "
                + "titreLivre, "
                + " sousTitreLivre, "
                + "resumeLivre, "
                + "edit.idEditeur as idEdit, edit.nomEditeur, " //dans livre
                + "a.idAuteur, a.nomAuteur, a.prenomAuteur, a.biographieAuteur, a.dateDecesAuteur, a.dateNaissanceAuteur, "
                + "sst.idSousTheme, sst.nomSousTheme, "
                + "tva.idTva, tva.nomTauxTva, tva.valeurTauxTva "
                + "FROM Livre as l "
                //editeur
                + "LEFT JOIN Editeur as edit "
                + "ON edit.idEditeur = l.idEditeur "
                //fin editeur

                //auteur
                + "LEFT JOIN Ecriture as e "
                + "ON e.isbn13Livre = l.isbn13Livre "
                + "LEFT JOIN Auteur as a "
                + "ON a.idAuteur = e.idAuteur "
                //fin auteur

                //tva
                + "LEFT JOIN ApplicationTva as ap "
                + "ON ap.isbn13Livre = l.isbn13Livre "
                + "LEFT JOIN Tva as tva "
                + "ON tva.idTva = ap.idTva "
                //fin tva

                //sous theme
                + "LEFT JOIN Appartenance as app "
                + "ON l.isbn13Livre = app.isbn13Livre "
                + "LEFT JOIN SousTheme as sst "
                + "ON sst.idSousTheme = app.idSousTheme "
                //fin sous theme
                + "WHERE actifLivre = 1 AND sst.nomSousTheme = ? " //  AND l.isbn13Livre = '9782011691699'
                + "ORDER BY titreLivre asc offset ? rows fetch next ? rows only";

        System.out.println(req);

        PreparedStatement pstm = mc.prepareStatement(req);

        pstm.setInt(2, depart);
        pstm.setInt(3, nextItem);
        pstm.setString(1, sousTheme);

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {

            beanLivre livre = new beanLivre();

            livre.setIdLivre(rs.getString("isbn13Livre"));
            livre.setTitreLivre(rs.getString("titreLivre"));
            livre.setSousTitreLivre(rs.getString("sousTitreLivre"));
            livre.setResumeLivre(rs.getString("resumeLivre"));
            livre.setPoidsLivre(rs.getFloat("poidsLivre"));
            livre.setDateLivre(rs.getString("dateLivre"));
            livre.setImageLivre(rs.getString("imageLivre"));
            livre.setQuantiteLivre(rs.getInt("quantiteLivre"));
            livre.setPrixHTLivre(rs.getFloat("prixLivre"));
            livre.getEditeurLivre().setIdEditeur(rs.getInt("idEdit"));
            livre.getEditeurLivre().setNomEditeur(rs.getString("nomEditeur"));
            livre.getAuteurLivre().setIdAuteur(rs.getInt("idAuteur"));
            livre.getAuteurLivre().setNomAuteur(rs.getString("nomAuteur"));
            livre.getAuteurLivre().setPrenomAuteur(rs.getString("prenomAuteur"));
            livre.getAuteurLivre().setBiographieAuteur(rs.getString("biographieAuteur"));
            livre.getAuteurLivre().setDateNaissanceAuteur(rs.getString("dateNaissanceAuteur"));
            livre.getAuteurLivre().setDateDecesAuteur(rs.getString("dateDecesAuteur"));
            livre.getSousThemeLivre().setIdSousTheme(rs.getInt("idSousTheme"));
            livre.getSousThemeLivre().setNomSousTheme(rs.getString("nomSousTheme"));
            livre.getTvaLivre().setIdTva(rs.getInt("idTva"));
            livre.getTvaLivre().setNomTauxTva(rs.getString("nomTauxTva"));
            livre.getTvaLivre().setValeurTauxTva(rs.getFloat("valeurTauxTva"));

            liste.add(livre);

        }

        rs.close();
        pstm.close();

        String reqMotCle = "select mc.idMotsClefs AS idMotCle, mc.nomMotsClefs, carac.idMotsClefs, carac.isbn13Livre, l.isbn13Livre "
                + "from MotsClefs mc "
                + "join Caracterisation carac on carac.idMotsClefs = mc.idMotsClefs "
                + "join Livre l on l.isbn13Livre = carac.isbn13Livre "
                + "where l.isbn13Livre = ?";

        System.out.println(reqMotCle);

        PreparedStatement pstm01 = mc.prepareStatement(reqMotCle);

        for (beanLivre l : liste) {
            pstm01.setString(1, l.getIdLivre());
            ResultSet rs01 = pstm01.executeQuery();
            System.out.println("avant le while");
            while (rs01.next()) {
                MotsClefs motCl = new MotsClefs();
                motCl.setIdMotsClefs(rs01.getInt("idMotsClefs"));
                motCl.setNomMotsClefs(rs01.getString("nomMotsClefs"));

                l.getMotsClefs().add(motCl);
            }
            rs01.close();
        }
        pstm01.close();

        return liste;

    }

    public int nbrTotalArticleByCat(String sousTheme) throws ClassNotFoundException, SQLException {

        int nbrTotal = 0;

        MaConnection mc = MaConnection.getInstance();

        String req = "select COUNT(*) AS nbrTotalArt "
                + "from Livre l "
                + "join Appartenance app on app.isbn13Livre = l.isbn13Livre "
                + "join SousTheme sst on sst.idSousTheme = app.idSousTheme "
                + "where sst.nomSousTheme = ?";

        PreparedStatement pstm = mc.prepareStatement(req);

        pstm.setString(1, sousTheme);

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            nbrTotal = rs.getInt("nbrTotalArt");
        }

        return nbrTotal;
    }

    public List<beanLivre> getListeLivreByTitre(String titre, int depart, int nextItem) throws ClassNotFoundException, SQLException {

        List<beanLivre> liste = new ArrayList();

        MaConnection mc = MaConnection.getInstance();

        String req = "SELECT l.isbn13Livre, "
                + "quantiteLivre, "
                + "dateLivre, "
                + " poidsLivre, "
                + "imageLivre, "
                + "prixLivre, "
                + "titreLivre, "
                + " sousTitreLivre, "
                + "resumeLivre, "
                + "edit.idEditeur as idEdit, edit.nomEditeur, " //dans livre
                + "a.idAuteur, a.nomAuteur, a.prenomAuteur, a.biographieAuteur, a.dateDecesAuteur, a.dateNaissanceAuteur, "
                + "sst.idSousTheme, sst.nomSousTheme, "
                + "tva.idTva, tva.nomTauxTva, tva.valeurTauxTva "
                + "FROM Livre as l "
                //editeur
                + "LEFT JOIN Editeur as edit "
                + "ON edit.idEditeur = l.idEditeur "
                //fin editeur

                //auteur
                + "LEFT JOIN Ecriture as e "
                + "ON e.isbn13Livre = l.isbn13Livre "
                + "LEFT JOIN Auteur as a "
                + "ON a.idAuteur = e.idAuteur "
                //fin auteur

                //tva
                + "LEFT JOIN ApplicationTva as ap "
                + "ON ap.isbn13Livre = l.isbn13Livre "
                + "LEFT JOIN Tva as tva "
                + "ON tva.idTva = ap.idTva "
                //fin tva

                //sous theme
                + "LEFT JOIN Appartenance as app "
                + "ON l.isbn13Livre = app.isbn13Livre "
                + "LEFT JOIN SousTheme as sst "
                + "ON sst.idSousTheme = app.idSousTheme "
                //fin sous theme
                //                + "WHERE actifLivre = 1 AND sst.nomSousTheme = ? " //  AND l.isbn13Livre = '9782011691699'
                //                + "ORDER BY titreLivre asc offset ? rows fetch next ? rows only";
                + "WHERE actifLivre = 1 AND titreLivre LIKE ? "
                + "ORDER BY titreLivre asc offset ? rows fetch next ? rows only";

        System.out.println(req);

        PreparedStatement pstm = mc.prepareStatement(req);

        pstm.setString(1, "%" + titre + "%");
        pstm.setInt(2, depart);
        pstm.setInt(3, nextItem);

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {

            beanLivre livre = new beanLivre();

            livre.setIdLivre(rs.getString("isbn13Livre"));
            livre.setTitreLivre(rs.getString("titreLivre"));
            livre.setSousTitreLivre(rs.getString("sousTitreLivre"));
            livre.setResumeLivre(rs.getString("resumeLivre"));
            livre.setPoidsLivre(rs.getFloat("poidsLivre"));
            livre.setDateLivre(rs.getString("dateLivre"));
            livre.setImageLivre(rs.getString("imageLivre"));
            livre.setQuantiteLivre(rs.getInt("quantiteLivre"));
            livre.setPrixHTLivre(rs.getFloat("prixLivre"));
            livre.getEditeurLivre().setIdEditeur(rs.getInt("idEdit"));
            livre.getEditeurLivre().setNomEditeur(rs.getString("nomEditeur"));
            livre.getAuteurLivre().setIdAuteur(rs.getInt("idAuteur"));
            livre.getAuteurLivre().setNomAuteur(rs.getString("nomAuteur"));
            livre.getAuteurLivre().setPrenomAuteur(rs.getString("prenomAuteur"));
            livre.getAuteurLivre().setBiographieAuteur(rs.getString("biographieAuteur"));
            livre.getAuteurLivre().setDateNaissanceAuteur(rs.getString("dateNaissanceAuteur"));
            livre.getAuteurLivre().setDateDecesAuteur(rs.getString("dateDecesAuteur"));
            livre.getSousThemeLivre().setIdSousTheme(rs.getInt("idSousTheme"));
            livre.getSousThemeLivre().setNomSousTheme(rs.getString("nomSousTheme"));
            livre.getTvaLivre().setIdTva(rs.getInt("idTva"));
            livre.getTvaLivre().setNomTauxTva(rs.getString("nomTauxTva"));
            livre.getTvaLivre().setValeurTauxTva(rs.getFloat("valeurTauxTva"));

            liste.add(livre);

        }

        rs.close();
        pstm.close();

        String reqMotCle = "select mc.idMotsClefs AS idMotCle, mc.nomMotsClefs, carac.idMotsClefs, carac.isbn13Livre, l.isbn13Livre "
                + "from MotsClefs mc "
                + "join Caracterisation carac on carac.idMotsClefs = mc.idMotsClefs "
                + "join Livre l on l.isbn13Livre = carac.isbn13Livre "
                + "where l.isbn13Livre = ?";

        System.out.println(reqMotCle);

        PreparedStatement pstm01 = mc.prepareStatement(reqMotCle);

        for (beanLivre l : liste) {
            pstm01.setString(1, l.getIdLivre());
            ResultSet rs01 = pstm01.executeQuery();
            System.out.println("avant le while");
            while (rs01.next()) {
                MotsClefs motCl = new MotsClefs();
                motCl.setIdMotsClefs(rs01.getInt("idMotsClefs"));
                motCl.setNomMotsClefs(rs01.getString("nomMotsClefs"));

                l.getMotsClefs().add(motCl);
            }
            rs01.close();
        }
        pstm01.close();

        return liste;

    }

    public int nbrTotalArticleByTitre(String titre) throws ClassNotFoundException, SQLException {

        int nbrTotal = 0;

        MaConnection mc = MaConnection.getInstance();

        String req = "select COUNT(*) AS nbrTotalArt "
                + "from Livre "
                + "WHERE titreLivre LIKE ? ";

        PreparedStatement pstm = mc.prepareStatement(req);

        pstm.setString(1, "%" + titre + "%");

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            nbrTotal = rs.getInt("nbrTotalArt");
        }

        return nbrTotal;
    }

    public List<beanLivre> getListeLivreByIsbn(String numIsbn, int depart, int nextItem) throws ClassNotFoundException, SQLException {

        List<beanLivre> liste = new ArrayList();

        MaConnection mc = MaConnection.getInstance();

        String req = "SELECT l.isbn13Livre, "
                + "quantiteLivre, "
                + "dateLivre, "
                + " poidsLivre, "
                + "imageLivre, "
                + "prixLivre, "
                + "titreLivre, "
                + " sousTitreLivre, "
                + "resumeLivre, "
                + "edit.idEditeur as idEdit, edit.nomEditeur, " //dans livre
                + "a.idAuteur, a.nomAuteur, a.prenomAuteur, a.biographieAuteur, a.dateDecesAuteur, a.dateNaissanceAuteur, "
                + "sst.idSousTheme, sst.nomSousTheme, "
                + "tva.idTva, tva.nomTauxTva, tva.valeurTauxTva "
                + "FROM Livre as l "
                //editeur
                + "LEFT JOIN Editeur as edit "
                + "ON edit.idEditeur = l.idEditeur "
                //fin editeur

                //auteur
                + "LEFT JOIN Ecriture as e "
                + "ON e.isbn13Livre = l.isbn13Livre "
                + "LEFT JOIN Auteur as a "
                + "ON a.idAuteur = e.idAuteur "
                //fin auteur

                //tva
                + "LEFT JOIN ApplicationTva as ap "
                + "ON ap.isbn13Livre = l.isbn13Livre "
                + "LEFT JOIN Tva as tva "
                + "ON tva.idTva = ap.idTva "
                //fin tva

                //sous theme
                + "LEFT JOIN Appartenance as app "
                + "ON l.isbn13Livre = app.isbn13Livre "
                + "LEFT JOIN SousTheme as sst "
                + "ON sst.idSousTheme = app.idSousTheme "
                //fin sous theme
                + "WHERE actifLivre = 1 AND l.isbn13Livre LIKE ? " //  AND l.isbn13Livre = '9782011691699'
                + "ORDER BY titreLivre asc offset ? rows fetch next ? rows only";

        System.out.println(req);

        PreparedStatement pstm = mc.prepareStatement(req);

        pstm.setString(1, "%" + numIsbn + "%");
        pstm.setInt(2, depart);
        pstm.setInt(3, nextItem);

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {

            beanLivre livre = new beanLivre();

            livre.setIdLivre(rs.getString("isbn13Livre"));
            livre.setTitreLivre(rs.getString("titreLivre"));
            livre.setSousTitreLivre(rs.getString("sousTitreLivre"));
            livre.setResumeLivre(rs.getString("resumeLivre"));
            livre.setPoidsLivre(rs.getFloat("poidsLivre"));
            livre.setDateLivre(rs.getString("dateLivre"));
            livre.setImageLivre(rs.getString("imageLivre"));
            livre.setQuantiteLivre(rs.getInt("quantiteLivre"));
            livre.setPrixHTLivre(rs.getFloat("prixLivre"));
            livre.getEditeurLivre().setIdEditeur(rs.getInt("idEdit"));
            livre.getEditeurLivre().setNomEditeur(rs.getString("nomEditeur"));
            livre.getAuteurLivre().setIdAuteur(rs.getInt("idAuteur"));
            livre.getAuteurLivre().setNomAuteur(rs.getString("nomAuteur"));
            livre.getAuteurLivre().setPrenomAuteur(rs.getString("prenomAuteur"));
            livre.getAuteurLivre().setBiographieAuteur(rs.getString("biographieAuteur"));
            livre.getAuteurLivre().setDateNaissanceAuteur(rs.getString("dateNaissanceAuteur"));
            livre.getAuteurLivre().setDateDecesAuteur(rs.getString("dateDecesAuteur"));
            livre.getSousThemeLivre().setIdSousTheme(rs.getInt("idSousTheme"));
            livre.getSousThemeLivre().setNomSousTheme(rs.getString("nomSousTheme"));
            livre.getTvaLivre().setIdTva(rs.getInt("idTva"));
            livre.getTvaLivre().setNomTauxTva(rs.getString("nomTauxTva"));
            livre.getTvaLivre().setValeurTauxTva(rs.getFloat("valeurTauxTva"));

            liste.add(livre);

        }

        rs.close();
        pstm.close();

        String reqMotCle = "select mc.idMotsClefs AS idMotCle, mc.nomMotsClefs, carac.idMotsClefs, carac.isbn13Livre, l.isbn13Livre "
                + "from MotsClefs mc "
                + "join Caracterisation carac on carac.idMotsClefs = mc.idMotsClefs "
                + "join Livre l on l.isbn13Livre = carac.isbn13Livre "
                + "where l.isbn13Livre = ?";

        System.out.println(reqMotCle);

        PreparedStatement pstm01 = mc.prepareStatement(reqMotCle);

        for (beanLivre l : liste) {
            pstm01.setString(1, l.getIdLivre());
            ResultSet rs01 = pstm01.executeQuery();
            System.out.println("avant le while");
            while (rs01.next()) {
                MotsClefs motCl = new MotsClefs();
                motCl.setIdMotsClefs(rs01.getInt("idMotsClefs"));
                motCl.setNomMotsClefs(rs01.getString("nomMotsClefs"));

                l.getMotsClefs().add(motCl);
            }
            rs01.close();
        }
        pstm01.close();

        return liste;

    }

    public int nbrTotalArticleByIsbn(String numIsbn) throws ClassNotFoundException, SQLException {

        int nbrTotal = 0;

        MaConnection mc = MaConnection.getInstance();

        String req = "select COUNT(*) AS nbrTotalArt "
                + "from Livre "
                + "WHERE numIsbn LIKE ? ";

        PreparedStatement pstm = mc.prepareStatement(req);

        pstm.setString(1, "%" + numIsbn + "%");

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            nbrTotal = rs.getInt("nbrTotalArt");
        }

        return nbrTotal;
    }

    public List<beanLivre> getListeLivreByAuteur(String nomAuteur, int depart, int nextItem) throws ClassNotFoundException, SQLException {

        List<beanLivre> liste = new ArrayList();

        MaConnection mc = MaConnection.getInstance();

        String req = "SELECT l.isbn13Livre, "
                + "quantiteLivre, "
                + "dateLivre, "
                + " poidsLivre, "
                + "imageLivre, "
                + "prixLivre, "
                + "titreLivre, "
                + " sousTitreLivre, "
                + "resumeLivre, "
                + "edit.idEditeur as idEdit, edit.nomEditeur, " //dans livre
                + "a.idAuteur, a.nomAuteur, a.prenomAuteur, a.biographieAuteur, a.dateDecesAuteur, a.dateNaissanceAuteur, "
                + "sst.idSousTheme, sst.nomSousTheme, "
                + "tva.idTva, tva.nomTauxTva, tva.valeurTauxTva "
                + "FROM Livre as l "
                //editeur
                + "LEFT JOIN Editeur as edit "
                + "ON edit.idEditeur = l.idEditeur "
                //fin editeur

                //auteur
                + "LEFT JOIN Ecriture as e "
                + "ON e.isbn13Livre = l.isbn13Livre "
                + "LEFT JOIN Auteur as a "
                + "ON a.idAuteur = e.idAuteur "
                //fin auteur

                //tva
                + "LEFT JOIN ApplicationTva as ap "
                + "ON ap.isbn13Livre = l.isbn13Livre "
                + "LEFT JOIN Tva as tva "
                + "ON tva.idTva = ap.idTva "
                //fin tva

                //sous theme
                + "LEFT JOIN Appartenance as app "
                + "ON l.isbn13Livre = app.isbn13Livre "
                + "LEFT JOIN SousTheme as sst "
                + "ON sst.idSousTheme = app.idSousTheme "
                //fin sous theme
                + "WHERE actifLivre = 1 AND nomAuteur LIKE ? " //  AND l.isbn13Livre = '9782011691699'
                + "ORDER BY titreLivre asc offset ? rows fetch next ? rows only";

        System.out.println(req);

        PreparedStatement pstm = mc.prepareStatement(req);

        pstm.setString(1, "%" + nomAuteur + "%");
        pstm.setInt(2, depart);
        pstm.setInt(3, nextItem);

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {

            beanLivre livre = new beanLivre();

            livre.setIdLivre(rs.getString("isbn13Livre"));
            livre.setTitreLivre(rs.getString("titreLivre"));
            livre.setSousTitreLivre(rs.getString("sousTitreLivre"));
            livre.setResumeLivre(rs.getString("resumeLivre"));
            livre.setPoidsLivre(rs.getFloat("poidsLivre"));
            livre.setDateLivre(rs.getString("dateLivre"));
            livre.setImageLivre(rs.getString("imageLivre"));
            livre.setQuantiteLivre(rs.getInt("quantiteLivre"));
            livre.setPrixHTLivre(rs.getFloat("prixLivre"));
            livre.getEditeurLivre().setIdEditeur(rs.getInt("idEdit"));
            livre.getEditeurLivre().setNomEditeur(rs.getString("nomEditeur"));
            livre.getAuteurLivre().setIdAuteur(rs.getInt("idAuteur"));
            livre.getAuteurLivre().setNomAuteur(rs.getString("nomAuteur"));
            livre.getAuteurLivre().setPrenomAuteur(rs.getString("prenomAuteur"));
            livre.getAuteurLivre().setBiographieAuteur(rs.getString("biographieAuteur"));
            livre.getAuteurLivre().setDateNaissanceAuteur(rs.getString("dateNaissanceAuteur"));
            livre.getAuteurLivre().setDateDecesAuteur(rs.getString("dateDecesAuteur"));
            livre.getSousThemeLivre().setIdSousTheme(rs.getInt("idSousTheme"));
            livre.getSousThemeLivre().setNomSousTheme(rs.getString("nomSousTheme"));
            livre.getTvaLivre().setIdTva(rs.getInt("idTva"));
            livre.getTvaLivre().setNomTauxTva(rs.getString("nomTauxTva"));
            livre.getTvaLivre().setValeurTauxTva(rs.getFloat("valeurTauxTva"));

            liste.add(livre);

        }

        rs.close();
        pstm.close();

        String reqMotCle = "select mc.idMotsClefs AS idMotCle, mc.nomMotsClefs, carac.idMotsClefs, carac.isbn13Livre, l.isbn13Livre "
                + "from MotsClefs mc "
                + "join Caracterisation carac on carac.idMotsClefs = mc.idMotsClefs "
                + "join Livre l on l.isbn13Livre = carac.isbn13Livre "
                + "where l.isbn13Livre = ?";

        System.out.println(reqMotCle);

        PreparedStatement pstm01 = mc.prepareStatement(reqMotCle);

        for (beanLivre l : liste) {
            pstm01.setString(1, l.getIdLivre());
            ResultSet rs01 = pstm01.executeQuery();
            System.out.println("avant le while");
            while (rs01.next()) {
                MotsClefs motCl = new MotsClefs();
                motCl.setIdMotsClefs(rs01.getInt("idMotsClefs"));
                motCl.setNomMotsClefs(rs01.getString("nomMotsClefs"));

                l.getMotsClefs().add(motCl);
            }
            rs01.close();
        }
        pstm01.close();

        return liste;

    }

    public int nbrTotalArticleByAuteur(String nomAuteur) throws ClassNotFoundException, SQLException {

        int nbrTotal = 0;

        MaConnection mc = MaConnection.getInstance();

        String req = "select COUNT(*) AS nbrTotalArt "
                + "from Livre "
                + "WHERE nomAuteur LIKE ? ";

        PreparedStatement pstm = mc.prepareStatement(req);

        pstm.setString(1, "%" + nomAuteur + "%");

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            nbrTotal = rs.getInt("nbrTotalArt");
        }

        return nbrTotal;
    }

    public List<beanLivre> getListeLivreBySearch(String nom, int depart, int nextItem) throws ClassNotFoundException, SQLException {

        List<beanLivre> liste = new ArrayList();

        MaConnection mc = MaConnection.getInstance();

        String req = "SELECT l.isbn13Livre, "
                + "quantiteLivre, "
                + "dateLivre, "
                + " poidsLivre, "
                + "imageLivre, "
                + "prixLivre, "
                + "titreLivre, "
                + " sousTitreLivre, "
                + "resumeLivre, "
                + "edit.idEditeur as idEdit, edit.nomEditeur, " //dans livre
                + "a.idAuteur, a.nomAuteur, a.prenomAuteur, a.biographieAuteur, a.dateDecesAuteur, a.dateNaissanceAuteur, "
                + "sst.idSousTheme, sst.nomSousTheme, "
                + "tva.idTva, tva.nomTauxTva, tva.valeurTauxTva "
                + "FROM Livre as l "
                //editeur
                + "LEFT JOIN Editeur as edit "
                + "ON edit.idEditeur = l.idEditeur "
                //fin editeur

                //auteur
                + "LEFT JOIN Ecriture as e "
                + "ON e.isbn13Livre = l.isbn13Livre "
                + "LEFT JOIN Auteur as a "
                + "ON a.idAuteur = e.idAuteur "
                //fin auteur

                //tva
                + "LEFT JOIN ApplicationTva as ap "
                + "ON ap.isbn13Livre = l.isbn13Livre "
                + "LEFT JOIN Tva as tva "
                + "ON tva.idTva = ap.idTva "
                //fin tva

                //sous theme
                + "LEFT JOIN Appartenance as app "
                + "ON l.isbn13Livre = app.isbn13Livre "
                + "LEFT JOIN SousTheme as sst "
                + "ON sst.idSousTheme = app.idSousTheme "
                //fin sous theme
                //                + "WHERE actifLivre = 1 AND sst.nomSousTheme = ? " //  AND l.isbn13Livre = '9782011691699'
                //                + "ORDER BY titreLivre asc offset ? rows fetch next ? rows only";
                + "WHERE actifLivre = 1 AND titreLivre LIKE ? "
                + "OR nomAuteur LIKE ? "
                + "OR l.isbn13Livre LIKE ? "
                + "ORDER BY titreLivre asc offset ? rows fetch next ? rows only";

        System.out.println(req);

        PreparedStatement pstm = mc.prepareStatement(req);

        pstm.setString(1, "%" + nom + "%");
        pstm.setString(2, "%" + nom + "%");
        pstm.setString(3, "%" + nom + "%");
        pstm.setInt(4, depart);
        pstm.setInt(5, nextItem);

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {

            beanLivre livre = new beanLivre();

            livre.setIdLivre(rs.getString("isbn13Livre"));
            livre.setTitreLivre(rs.getString("titreLivre"));
            livre.setSousTitreLivre(rs.getString("sousTitreLivre"));
            livre.setResumeLivre(rs.getString("resumeLivre"));
            livre.setPoidsLivre(rs.getFloat("poidsLivre"));
            livre.setDateLivre(rs.getString("dateLivre"));
            livre.setImageLivre(rs.getString("imageLivre"));
            livre.setQuantiteLivre(rs.getInt("quantiteLivre"));
            livre.setPrixHTLivre(rs.getFloat("prixLivre"));
            livre.getEditeurLivre().setIdEditeur(rs.getInt("idEdit"));
            livre.getEditeurLivre().setNomEditeur(rs.getString("nomEditeur"));
            livre.getAuteurLivre().setIdAuteur(rs.getInt("idAuteur"));
            livre.getAuteurLivre().setNomAuteur(rs.getString("nomAuteur"));
            livre.getAuteurLivre().setPrenomAuteur(rs.getString("prenomAuteur"));
            livre.getAuteurLivre().setBiographieAuteur(rs.getString("biographieAuteur"));
            livre.getAuteurLivre().setDateNaissanceAuteur(rs.getString("dateNaissanceAuteur"));
            livre.getAuteurLivre().setDateDecesAuteur(rs.getString("dateDecesAuteur"));
            livre.getSousThemeLivre().setIdSousTheme(rs.getInt("idSousTheme"));
            livre.getSousThemeLivre().setNomSousTheme(rs.getString("nomSousTheme"));
            livre.getTvaLivre().setIdTva(rs.getInt("idTva"));
            livre.getTvaLivre().setNomTauxTva(rs.getString("nomTauxTva"));
            livre.getTvaLivre().setValeurTauxTva(rs.getFloat("valeurTauxTva"));

            liste.add(livre);

        }

        rs.close();
        pstm.close();

        String reqMotCle = "select mc.idMotsClefs AS idMotCle, mc.nomMotsClefs, carac.idMotsClefs, carac.isbn13Livre, l.isbn13Livre "
                + "from MotsClefs mc "
                + "join Caracterisation carac on carac.idMotsClefs = mc.idMotsClefs "
                + "join Livre l on l.isbn13Livre = carac.isbn13Livre "
                + "where l.isbn13Livre = ?";

        System.out.println(reqMotCle);

        PreparedStatement pstm01 = mc.prepareStatement(reqMotCle);

        for (beanLivre l : liste) {
            pstm01.setString(1, l.getIdLivre());
            ResultSet rs01 = pstm01.executeQuery();
            System.out.println("avant le while");
            while (rs01.next()) {
                MotsClefs motCl = new MotsClefs();
                motCl.setIdMotsClefs(rs01.getInt("idMotsClefs"));
                motCl.setNomMotsClefs(rs01.getString("nomMotsClefs"));

                l.getMotsClefs().add(motCl);
            }
            rs01.close();
        }
        pstm01.close();

        return liste;

    }

    public int nbrTotalArticleBySearch(String nom) throws ClassNotFoundException, SQLException {

        int nbrTotal = 0;

        MaConnection mc = MaConnection.getInstance();

        String req = "select COUNT(*) AS nbrTotalArt "
                + "from Livre l "
                + "LEFT JOIN Ecriture as e "
                + "ON e.isbn13Livre = l.isbn13Livre "
                + "LEFT JOIN Auteur as a "
                + "ON a.idAuteur = e.idAuteur "
                + "WHERE titreLivre LIKE ? "
                + "OR nomAuteur LIKE ? "
                + "OR l.isbn13Livre LIKE ?";

        PreparedStatement pstm = mc.prepareStatement(req);

        pstm.setString(1, "%" + nom + "%");
        pstm.setString(2, "%" + nom + "%");
        pstm.setString(3, "%" + nom + "%");

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            nbrTotal = rs.getInt("nbrTotalArt");
        }

        return nbrTotal;
    }

    public void getLivreAuteurInf(Auteur a) throws SQLException, ClassNotFoundException {
        MaConnection mc = MaConnection.getInstance();
        
        String req = "Select l.isbn13Livre, "
                + "titreLivre, "
                + "sousTitreLivre, "
                + "prixLivre, "
                + "imageLivre "
                + "From LIVRE as l "
                + "LEFT JOIN Ecriture as e "
                + "on l.isbn13Livre = e.isbn13Livre "
                + "LEFT JOIN Auteur as a "
                + "on e.idAuteur = a.idAuteur "
                + "Where a.idAuteur = ?";

        PreparedStatement pstm = mc.prepareStatement(req);
        pstm.setInt(1, a.getIdAuteur());
        ResultSet rs = pstm.executeQuery();
        
        beanLivre livre = null;
        while (rs.next()) {
            livre = new beanLivre();
            livre.setIdLivre(rs.getString("isbn13Livre"));
            livre.setTitreLivre(rs.getString("titreLivre"));
            livre.setImageLivre(rs.getString("imageLivre"));
            livre.setSousTitreLivre(rs.getString("sousTitreLivre"));
            a.getLivreAuteur().put(livre.getIdLivre(), livre);
        }
        
        rs.close();
        pstm.close();
    }

    public beanLivre getLivre(String isbn) throws SQLException, ClassNotFoundException {

        beanLivre livre = new beanLivre();

        MaConnection mc = MaConnection.getInstance();

        String req = "SELECT l.isbn13Livre, "
                + "quantiteLivre, "
                + "dateLivre, "
                + " poidsLivre, "
                + "imageLivre, "
                + "prixLivre, "
                + "titreLivre, "
                + " sousTitreLivre, "
                + "resumeLivre, "
                + "edit.idEditeur as idEdit, edit.nomEditeur, " //dans livre
                + "a.idAuteur, a.nomAuteur, a.prenomAuteur, a.biographieAuteur, a.dateDecesAuteur, a.dateNaissanceAuteur, "
                + "sst.idSousTheme, sst.nomSousTheme, "
                + "tva.idTva, tva.nomTauxTva, tva.valeurTauxTva "
                + "FROM Livre as l "
                //editeur
                + "LEFT JOIN Editeur as edit "
                + "ON edit.idEditeur = l.idEditeur "
                //fin editeur

                //auteur
                + "LEFT JOIN Ecriture as e "
                + "ON e.isbn13Livre = l.isbn13Livre "
                + "LEFT JOIN Auteur as a "
                + "ON a.idAuteur = e.idAuteur "
                //fin auteur

                //tva
                + "LEFT JOIN ApplicationTva as ap "
                + "ON ap.isbn13Livre = l.isbn13Livre "
                + "LEFT JOIN Tva as tva "
                + "ON tva.idTva = ap.idTva "
                //fin tva

                //sous theme
                + "LEFT JOIN Appartenance as app "
                + "ON l.isbn13Livre = app.isbn13Livre "
                + "LEFT JOIN SousTheme as sst "
                + "ON sst.idSousTheme = app.idSousTheme "
                //fin sous theme
                + "WHERE actifLivre = 1 AND l.isbn13Livre = ? "
                + "ORDER BY titreLivre";

        System.out.println(req);

        PreparedStatement pstm = mc.prepareStatement(req);

        pstm.setString(1, isbn);

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {

            livre.setIdLivre(rs.getString("isbn13Livre"));
            livre.setTitreLivre(rs.getString("titreLivre"));
            livre.setSousTitreLivre(rs.getString("sousTitreLivre"));
            livre.setResumeLivre(rs.getString("resumeLivre"));
            livre.setPoidsLivre(rs.getFloat("poidsLivre"));
            livre.setDateLivre(rs.getString("dateLivre"));
            livre.setImageLivre(rs.getString("imageLivre"));
            livre.setQuantiteLivre(rs.getInt("quantiteLivre"));
            livre.setPrixHTLivre(rs.getFloat("prixLivre"));
            livre.getEditeurLivre().setIdEditeur(rs.getInt("idEdit"));
            livre.getEditeurLivre().setNomEditeur(rs.getString("nomEditeur"));
            livre.getAuteurLivre().setIdAuteur(rs.getInt("idAuteur"));
            livre.getAuteurLivre().setNomAuteur(rs.getString("nomAuteur"));
            livre.getAuteurLivre().setPrenomAuteur(rs.getString("prenomAuteur"));
            livre.getAuteurLivre().setBiographieAuteur(rs.getString("biographieAuteur"));
            livre.getAuteurLivre().setDateNaissanceAuteur(rs.getString("dateNaissanceAuteur"));
            livre.getAuteurLivre().setDateDecesAuteur(rs.getString("dateDecesAuteur"));
            livre.getSousThemeLivre().setIdSousTheme(rs.getInt("idSousTheme"));
            livre.getSousThemeLivre().setNomSousTheme(rs.getString("nomSousTheme"));
            livre.getTvaLivre().setIdTva(rs.getInt("idTva"));
            livre.getTvaLivre().setNomTauxTva(rs.getString("nomTauxTva"));
            livre.getTvaLivre().setValeurTauxTva(rs.getFloat("valeurTauxTva"));

        }

        rs.close();
        pstm.close();

        String reqMotCle = "select mc.idMotsClefs AS idMotCle, mc.nomMotsClefs, carac.idMotsClefs, carac.isbn13Livre, l.isbn13Livre "
                + "from MotsClefs mc "
                + "join Caracterisation carac on carac.idMotsClefs = mc.idMotsClefs "
                + "join Livre l on l.isbn13Livre = carac.isbn13Livre "
                + "where l.isbn13Livre = ?";

        System.out.println(reqMotCle);

        PreparedStatement pstm01 = mc.prepareStatement(reqMotCle);

        pstm01.setString(1, livre.getIdLivre());
        ResultSet rs01 = pstm01.executeQuery();
        System.out.println("avant le while");
        while (rs01.next()) {
            MotsClefs motCl = new MotsClefs();
            motCl.setIdMotsClefs(rs01.getInt("idMotsClefs"));
            motCl.setNomMotsClefs(rs01.getString("nomMotsClefs"));

            livre.getMotsClefs().add(motCl);
        }
        rs01.close();

        pstm01.close();

        return livre;

    }

//    select * from Livre l left join Caracterisation carac on l.isbn13Livre = carac.isbn13Livre left join MotsClefs mc on carac.idMotsClefs = mc.idMotsClefs where mc.nomMotsClefs = 'Aventure'
    public List<beanLivre> getListeLivreByMotCle(String motCle, int depart, int nextItem) throws ClassNotFoundException, SQLException {

        List<beanLivre> liste = new ArrayList();

        MaConnection mc = MaConnection.getInstance();

        String req = "SELECT l.isbn13Livre, "
                + "quantiteLivre, "
                + "dateLivre, "
                + " poidsLivre, "
                + "imageLivre, "
                + "prixLivre, "
                + "titreLivre, "
                + " sousTitreLivre, "
                + "resumeLivre, "
                + "edit.idEditeur as idEdit, edit.nomEditeur, " //dans livre
                + "a.idAuteur, a.nomAuteur, a.prenomAuteur, a.biographieAuteur, a.dateDecesAuteur, a.dateNaissanceAuteur, "
                + "sst.idSousTheme, sst.nomSousTheme, "
                + "tva.idTva, tva.nomTauxTva, tva.valeurTauxTva "
                + "FROM Livre as l "
                //editeur
                + "LEFT JOIN Editeur as edit "
                + "ON edit.idEditeur = l.idEditeur "
                //fin editeur

                //auteur
                + "LEFT JOIN Ecriture as e "
                + "ON e.isbn13Livre = l.isbn13Livre "
                + "LEFT JOIN Auteur as a "
                + "ON a.idAuteur = e.idAuteur "
                //fin auteur

                //tva
                + "LEFT JOIN ApplicationTva as ap "
                + "ON ap.isbn13Livre = l.isbn13Livre "
                + "LEFT JOIN Tva as tva "
                + "ON tva.idTva = ap.idTva "
                //fin tva

                //sous theme
                + "LEFT JOIN Appartenance as app "
                + "ON l.isbn13Livre = app.isbn13Livre "
                + "LEFT JOIN SousTheme as sst "
                + "ON sst.idSousTheme = app.idSousTheme "
                //mot cle
                + "left join Caracterisation carac on l.isbn13Livre = carac.isbn13Livre "
                + "left join MotsClefs mc on carac.idMotsClefs = mc.idMotsClefs "
                + "WHERE actifLivre = 1 AND mc.nomMotsClefs ? " //  AND l.isbn13Livre = '9782011691699'
                + "ORDER BY titreLivre asc offset ? rows fetch next ? rows only";

        System.out.println(req);

        PreparedStatement pstm = mc.prepareStatement(req);

        pstm.setString(1, motCle);
        pstm.setInt(2, depart);
        pstm.setInt(3, nextItem);

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {

            beanLivre livre = new beanLivre();

            livre.setIdLivre(rs.getString("isbn13Livre"));
            livre.setTitreLivre(rs.getString("titreLivre"));
            livre.setSousTitreLivre(rs.getString("sousTitreLivre"));
            livre.setResumeLivre(rs.getString("resumeLivre"));
            livre.setPoidsLivre(rs.getFloat("poidsLivre"));
            livre.setDateLivre(rs.getString("dateLivre"));
            livre.setImageLivre(rs.getString("imageLivre"));
            livre.setQuantiteLivre(rs.getInt("quantiteLivre"));
            livre.setPrixHTLivre(rs.getFloat("prixLivre"));
            livre.getEditeurLivre().setIdEditeur(rs.getInt("idEdit"));
            livre.getEditeurLivre().setNomEditeur(rs.getString("nomEditeur"));
            livre.getAuteurLivre().setIdAuteur(rs.getInt("idAuteur"));
            livre.getAuteurLivre().setNomAuteur(rs.getString("nomAuteur"));
            livre.getAuteurLivre().setPrenomAuteur(rs.getString("prenomAuteur"));
            livre.getAuteurLivre().setBiographieAuteur(rs.getString("biographieAuteur"));
            livre.getAuteurLivre().setDateNaissanceAuteur(rs.getString("dateNaissanceAuteur"));
            livre.getAuteurLivre().setDateDecesAuteur(rs.getString("dateDecesAuteur"));
            livre.getSousThemeLivre().setIdSousTheme(rs.getInt("idSousTheme"));
            livre.getSousThemeLivre().setNomSousTheme(rs.getString("nomSousTheme"));
            livre.getTvaLivre().setIdTva(rs.getInt("idTva"));
            livre.getTvaLivre().setNomTauxTva(rs.getString("nomTauxTva"));
            livre.getTvaLivre().setValeurTauxTva(rs.getFloat("valeurTauxTva"));

            liste.add(livre);

        }

        rs.close();
        pstm.close();

        String reqMotCle = "select mc.idMotsClefs AS idMotCle, mc.nomMotsClefs, carac.idMotsClefs, carac.isbn13Livre, l.isbn13Livre "
                + "from MotsClefs mc "
                + "join Caracterisation carac on carac.idMotsClefs = mc.idMotsClefs "
                + "join Livre l on l.isbn13Livre = carac.isbn13Livre "
                + "where l.isbn13Livre = ?";

        System.out.println(reqMotCle);

        PreparedStatement pstm01 = mc.prepareStatement(reqMotCle);

        for (beanLivre l : liste) {
            pstm01.setString(1, l.getIdLivre());
            ResultSet rs01 = pstm01.executeQuery();
            System.out.println("avant le while");
            while (rs01.next()) {
                MotsClefs motCl = new MotsClefs();
                motCl.setIdMotsClefs(rs01.getInt("idMotsClefs"));
                motCl.setNomMotsClefs(rs01.getString("nomMotsClefs"));

                l.getMotsClefs().add(motCl);
            }
            rs01.close();
        }
        pstm01.close();

        return liste;

    }

    public int nbrTotalArticleByMotCLe(String motCle) throws ClassNotFoundException, SQLException {

        int nbrTotal = 0;

        MaConnection mc = MaConnection.getInstance();

        String req = "select COUNT(*) AS nbrTotalArt "
                + "from Livre l "
                + "left join Caracterisation carac on l.isbn13Livre = carac.isbn13Livre "
                + "left join MotsClefs mc on carac.idMotsClefs = mc.idMotsClefs "
                + "WHERE actifLivre = 1 AND mc.nomMotsClefs = ? ";

        PreparedStatement pstm = mc.prepareStatement(req);

        pstm.setString(1, motCle);

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            nbrTotal = rs.getInt("nbrTotalArt");
        }

        return nbrTotal;
    }
//    

    public static void main(String[] args) {
        ReqLivre req = new ReqLivre();

        //        try {
//            for(beanLivre l : req.getListeLivreBySearch("on",0, 10 )) {
//
//                System.out.println(l.toString());
//
//            }
//
//
//            
//        } catch (ClassNotFoundException | SQLException ex) {
//            ex.printStackTrace();
//        }
//    }
        try {
            System.out.println(req.nbrTotalArticleByMotCLe("Aventure"));
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ReqLivre.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
