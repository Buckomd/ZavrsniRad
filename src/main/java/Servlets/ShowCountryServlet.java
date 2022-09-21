package Servlets;

import controllers.PageController;
import helpers.DBHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.HTTP;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet("/loadCountry")
public class ShowCountryServlet extends HttpServlet {

    private DBHelper dbHelper = new DBHelper();
    private PageController pageController = new PageController(dbHelper);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ArrayList<JSONObject> listOfCountries = pageController.getAllCountries();

        resp.setContentType("application/json");
        try(PrintWriter out = resp.getWriter()) {
            JSONObject jsonResult = new JSONObject();
            jsonResult.put("listOfCountries", listOfCountries);
            out.write(jsonResult.toString());
        }
    }
}
