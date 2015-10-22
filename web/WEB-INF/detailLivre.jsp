<%-- 
    Document   : detailLivre
    Created on : 13 oct. 2015, 09:36:45
    Author     : cdi418
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" href="css/bootstrap-3.3.5-dist/css/bootstrap.min.css"/>
    </head>
    <body>
        
        <div class="container">
        
        <h1>detail detailLivre !</h1>

        <img src="http://lorempixel.com/400/200/"> <br>

        titre ${detailLivre.titreLivre} <br>
        sous titre ${detailLivre.sousTitreLivre} <br>


        resume ${detailLivre.resumeLivre}<br>
        prix ht ${detailLivre.prixHTLivre}<br>
        valeur tva ${detailLivre.tvaLivre.valeurTauxTva} <br>
        nom taux tva ${detailLivre.tvaLivre.nomTauxTva}<br>

        <h2>Carateristiques détaillées</h2>

        <table class="table">
            <tr>

                <td>auteur nom-prenom</td>
                <td>${detailLivre.auteurLivre.nomAuteur} ${detailLivre.auteurLivre.prenomAuteur}</td>

            </tr>

            <tr>

                <td>nom editeur</td>
                <td>${detailLivre.editeurLivre.nomEditeur}</td>

            </tr>

            <tr>

                <td>date parution </td>
                <td>${detailLivre.dateLivre}</td>

            </tr>

            <tr>

                <td>num isbn</td>
                <td>${detailLivre.idLivre}</td>

            </tr>

        </table>
                
            <%-- envoie au panier de l'article via son ID, je met un champ hidden panier pour que mon controller le detecte --%>
        <form method="get" action="controller">
            <input type="hidden" name="panier" value="${detailLivre.idLivre}" />
            <input class="btn btn-success" type="submit" name="sendToPanier" value="Ajouter" />
            
            <div/>
            
        </form>
    </body>
</html>
