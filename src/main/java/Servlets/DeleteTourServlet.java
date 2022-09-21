package Servlets;

import helpers.DBHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/deleteTour")
public class DeleteTourServlet extends HttpServlet {

    private DBHelper dbHelper = new DBHelper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String id = req.getParameter("id");

        if(dbHelper.deleteTour(Integer.parseInt(id))){
            System.out.println("Obrisana tura");
            resp.setStatus(200);
        } else {
            System.out.println("Nije obrisana");
        }
    }
}
