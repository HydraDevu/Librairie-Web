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

        <div class="container">

            <a href="controller?payer=">payer ici</a>



            <h1 class="page-header text-center">mon catalogue</h1>
            
            <c:if test="${param.search != null}">
                <div class="alert alert-success text-center">nombre d'article trouvé: ${nbr_article_trouve}</div>
            </c:if>


            <form class="formSearch" method="controller">
                <div class="input-group input-group-lg">
                    <span class="input-group-addon">
                        <span class="glyphicon glyphicon-search"></span>
                    </span>
                    <input class="form-control" type="text" placeholder="Entrez votre recherche" name="search">
                </div>

                <input type="hidden" name="catalog" value=""/>

                <button type="submit" name="doIt" class="btn btn-default">Submit</button>
            </form>
            


            <div class="row">


                <%-- sommaire cote --%>

                <div class="col-md-2">

                    <ul class="sommaire nav nav-pills nav-stacked ">

                        <c:forEach var="theme" items="${ListeTheme}">
                            <li class="theme dropdown">
                                <a aria-expanded="false" class="dropdown-toggle" data-toggle="dropdown" href="#">
                                    ${theme.nomTheme} <span class="caret"></span>
                                </a>

                                <ul class="dropdown-menu">

                                    <c:forEach var="soustheme" items="${theme.getSousThemeTheme()}">

                                        <li><a href="controller?catalog&categorie=${soustheme.getNomSousTheme()}">${soustheme.getNomSousTheme()}</a></li>

                                    </c:forEach>

                                </ul>

                            </li>
                        </c:forEach>
                    </ul>



                </div>



                <%-- article catalogue --%>

                <div class="col-md-10">



                    <table class="table table-striped">



                        <c:forEach var="livre" items="${listeLivre}">

                            <%-- resume du livre en mode substring --%>
                            <c:set var="resume" value="${livre.resumeLivre}"/>
                            <c:set var="cutResume" value="${fn:substring(resume, 0, 150)}" />


                            <tr class="livreGrille">

                            <div class="row">

                                <td class="col-md-2"> <img src="http://lorempixel.com/400/200/"></td>
                                <td class="col-md-6 contenu">

                                    <h4 class="title text-center text-primary">${livre.titreLivre} <small> - ${livre.sousTitreLivre}</small></h4>
                                    <p>ecrit par: <a href="controller?auteurInf=${livre.auteurLivre.idAuteur}">${livre.auteurLivre.nomAuteur} ${livre.auteurLivre.prenomAuteur}</a></p>
                                    <p class="resume">Résumé: ${cutResume}...</p>
                                    <p class="text-center">
                                        <c:forEach var="motCle" items="${livre.motsClefs}">
                                            <a href="">${motCle.nomMotsClefs}</a>
                                        </c:forEach>
                                    </p>

                                </td>
                                <td class="shop col-md-2">


                                    <p class="price">prix : ${livre.prixHTLivre}€ HT </p>

                                    <c:if test="${livre.quantiteLivre > 200}">
                                        <p class="text-success">En stock</p>
                                    </c:if>

                                    <c:if test="${livre.quantiteLivre <= 200}">
                                        <p class="text-danger">Plus en stock</p>
                                    </c:if>
                                    <a href="controller?sendToPanier=${livre.idLivre}"><button class="btn btn-success">Add   <span class="glyphicon glyphicon-shopping-cart" aria-hidden="true"></span></button></a>
                                    <a href="controller?detailLivre=${livre.idLivre}"><button class="btn btn-default"><span class="glyphicon glyphicon-zoom-in" aria-hidden="true"></span></button></a>






                                </td>

                            </div>

                            </tr>


                        </c:forEach>




                    </table>

                </div>
            </div>


            <h2 class="page-header text-center">Pagination, la VRAIE !!</h2>


            <nav>
                <ul class="pagination">


                    <c:forEach varStatus="i" begin="1" end="${nbrPage + 1}">
                        <c:if test="${nbrPage >=1}">
                        <li><a href="controller?${page}&page=${i.index}">${i.index}</a></li>  
                        </c:if>

                    </c:forEach>


                </ul>
            </nav>



        </div>

    </body>

    <a href="${pageContext.request.requestURL}?${pageContext.request.queryString}&page=2">page en cour</a> <br>
    <a href="/&page=3">page test</a>

    <%@include file="jspFooter.jsp" %>
