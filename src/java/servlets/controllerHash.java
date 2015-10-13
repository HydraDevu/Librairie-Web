package servlets;

import beans.beanLivre;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import sql.ReqLivre;
import sql.ReqLivreHash;


@WebServlet(name = "controller", urlPatterns = {"/controller"})
public class controllerHash extends HttpServlet {

    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String url ="/WEB-INF/jspCatalogHash.jsp";
        
//        mes variable de scope application et session
        
        ServletContext application = this.getServletContext();
        HttpSession session = request.getSession();
        
//        creation bean livre ds le scope application
        beanLivre livre = (beanLivre) application.getAttribute("livre");
        
//        si le bean livre n'existe pas encore, on le cree puis le set dans scope application
        if(livre == null) {
            livre = new beanLivre();
            application.setAttribute("livre", livre);
        }
        
//        instance ma methode sql listant tout mes livre
        ReqLivreHash reqListeLivreHash = new ReqLivreHash();
        
//        envoie de ma liste ds ma jsp via la metode request. nom de ma variable 'listeLivre'
        request.setAttribute("listeLivre", null);
        try {
            request.setAttribute("listeLivre", reqListeLivreHash.getListeLivre());
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
        
        
        //page du detail du livre
        if(request.getParameter("detailLivre") != null) {
            url="/WEB-INF/detailLivre.jsp";
            //        instance ma methode sql listant tout mes livre
        ReqLivre reqLivreDetail = new ReqLivre();
        
        //envoie de mon livre ds ma jsp via la metode request. nom de ma variable 'detailLivre'
        request.setAttribute("detailLivre", null);
        try {
            request.setAttribute("detailLivre", reqLivreDetail.getLivre(request.getParameter("detailLivre")));
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
        }
        
        request.getRequestDispatcher(url).include(request, response);
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}