
        <div class="header">

        <div class="container">

            <ul class="menu-bar nav nav-tabs">

                <li class="text-center active"><a class="" href="controller" >Home</a></li>
                <li class="text-center"><a class="" href="controller?catalog" >Catalogue</a></li>
                
            
                <%-- si il n'est pas connect� : --%>
            <c:if test="${auth == null}">
                <li class="text-center"><a class="" href="controller?log" >Se connecter</a></li>
                <li class="text-center"><a class=" " href="#profile" >S'inscrire</a></li>
                 </c:if>

                <%-- si il est d�j� connect� : --%>
            <c:if test="${auth != null}">
                <li class="text-center dropdown">
                    <a class="menu-item dropdown-toggle" data-toggle="dropdown" href="#" aria-expanded="false">
                        Mon compte <span class="caret"></span>
                    </a>
                    <ul class="dropdown-menu">
                        <li><a href="controller?deconnection" >Se d�connecter</a></li>
                        <li class="divider"></li>
                        <li><a href="controller?account" >Voir mon compte</a></li>
                    </ul>
                </li>
            
            </c:if>

            </ul>
            
           <c:if test="${param.deconnection != null}">
                <div style="margin: 50px" class="alert alert-warning text-center">Vous avez �t� d�connect� ! </div>
            </c:if>

        </div>
            
            </div>

