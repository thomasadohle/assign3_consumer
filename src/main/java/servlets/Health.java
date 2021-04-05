package servlets;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Health extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response){
        response.setStatus(200);
    }
}
