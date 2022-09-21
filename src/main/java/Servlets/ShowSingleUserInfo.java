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

@WebServlet("/showsigleuser")
public class ShowSingleUserInfo extends HttpServlet {

    private DBHelper dbHelper = new DBHelper();
    private ValidationHelper validationHelper = new ValidationHelper();
    UserController userController = new UserController(dbHelper, validationHelper);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String userID = req.getParameter("userID");

        ArrayList<JSONObject> sigleUSer = userController.singleUser(Integer.parseInt(userID));

        resp.setContentType("application/json");
        try(PrintWriter out = resp.getWriter()) {
            JSONObject jsonResult = new JSONObject();
            jsonResult.put("sigleUSer", sigleUSer);
            out.write(jsonResult.toString());
        }


    }
}
