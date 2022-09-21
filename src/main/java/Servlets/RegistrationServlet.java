package Servlets;

import controllers.UserController;
import helpers.DBHelper;
import helpers.ValidationHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/registration")
public class RegistrationServlet extends HttpServlet {

    private DBHelper dbHelper = new DBHelper();
    private ValidationHelper validationHelper = new ValidationHelper();
    UserController userController = new UserController(dbHelper, validationHelper);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println("Pozvan api registracija");

        String name = req.getParameter("name");
        String lastname = req.getParameter("lastname");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String country = req.getParameter("country");
        String dateString = req.getParameter("dateBirth");

        if(userController.register(name, lastname,email,password,country,dateString)){
            resp.setStatus(200);
        } else {
            resp.setStatus(400);
        }
    }
}
