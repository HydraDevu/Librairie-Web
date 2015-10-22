<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" href="css/bootstrap-3.3.5-dist/css/bootstrap.min.css"/>
        <link rel="stylesheet" href="css/style.css"/>



    </head>


    <body>

        <%@include file="jspHeader.jsp" %>

        <div class="container-fluid">

            <h1 class="jumbotron page-header text-center" >${auteurInf.nomAuteur} ${auteurInf.prenomAuteur}<br>
                <small>${auteurInf.dateNaissanceAuteur}</small><br>    
                    <c:if test="${auteurInf.dateDecesAuteur != '1900-01-01'}">
                        <small>${auteurInf.dateDecesAuteur}</small>
                    </c:if>
                </h1>
        </div>

        <div class="container">
            <div class="auteur">
                
                <h3>Biographie : </h3>
                <blockquote> ${auteurInf.biographieAuteur}</blockquote>
                
            </div>
                Autre livre de cet auteur :<br>
                <br>
                <hr>
                
                <c:forEach var="l" items="${auteurInf.Coll()}">
                    <div class="col-lg-4" style="padding: 0 100px; border-right: #eee 1px solid;">
                    <a href="controller?detailLivre=${l.idLivre}"><img style="width: 150px;" src="http://lorempixel.com/400/200/"></a>
                    <a href="controller?detailLivre=${l.idLivre}">${l.imageLivre} ${l.titreLivre} ${l.sousTitreLivre}</a><br>
                    </div>
                </c:forEach>
                   
                    
                    <%--
            <button id="click" class="btn btn-primary">click me</button>
--%>
</div>

    </body>

<%--
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
    <script src="css/bootstrap-3.3.5-dist/js/bootstrap.min.js"></script>
    <script>

        $(document).ready(function () {
            console.log('ibrahim');
            $('.auteur').hide();
            $('#click').click(function () {
                /*
                 }
                 if($('.auteur').is(":visible")) {
                 $('.auteur').slideUp(1000);
                 } else {
                 $('.auteur').slideDown(1000);
                 }
                 */
                $('.auteur').slideToggle(2000);
            });


        });

    </script>
--%>
</html>

</html>
