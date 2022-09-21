package Servlets;

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

@WebServlet("/showallimages")
public class ShowAllImagesForSingleTourServlet extends HttpServlet {

    private DBHelper dbHelper = new DBHelper();
    private TourController tourController = new TourController(dbHelper);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String id = req.getParameter("id");

        ArrayList<JSONObject> tourimages = tourController.getAllPictureForSingleTour(Integer.parseInt(id));

        resp.setContentType("application/json");
        try(PrintWriter out = resp.getWriter()) {
            JSONObject jsonResult = new JSONObject();
            jsonResult.put("allimages", tourimages);
            out.write(jsonResult.toString());
        }
    }
}

