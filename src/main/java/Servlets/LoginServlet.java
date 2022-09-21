package Servlets;

import controllers.UserController;
import helpers.DBHelper;
import helpers.ValidationHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private DBHelper dbHelper = new DBHelper();
    private ValidationHelper validationHelper = new ValidationHelper();
    UserController userController = new UserController(dbHelper, validationHelper);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        if(userController.login(email,password)){
            ArrayList<JSONObject> userIDroleID = userController.userRole(email,password);

            resp.setContentType("application/json");
            try(PrintWriter out = resp.getWriter()) {
                JSONObject jsonResult = new JSONObject();
                jsonResult.put("userIDroleID", userIDroleID);
                out.write(jsonResult.toString());
            }
            resp.setStatus(200);
        } else {
            resp.setStatus(403);
        }
    }
}
