package Servlets;

import controllers.PageController;
import controllers.TourController;
import helpers.DBHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet("/showtours")
public class ShowToursServlet extends HttpServlet {

    private DBHelper dbHelper = new DBHelper();
    private TourController tourController = new TourController(dbHelper);


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        ArrayList<JSONObject> toursList = tourController.getAllTours();

        resp.setContentType("application/json");
        try(PrintWriter out = resp.getWriter()) {
            JSONObject jsonResult = new JSONObject();
            jsonResult.put("listOfTours", toursList);
            out.write(jsonResult.toString());
        }
    }
}
