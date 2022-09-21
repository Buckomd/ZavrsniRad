package Servlets;

import helpers.DBHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.HTTP;

import java.io.IOException;

@WebServlet("/makereservation")
public class MakeReservationServlet extends HttpServlet {

    private DBHelper dbHelper = new DBHelper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String userID = req.getParameter("userID");
        String toursID = req.getParameter("toursID");

        if(dbHelper.saveReservatio(Integer.parseInt(toursID),Integer.parseInt(userID))){
            resp.setStatus(200);
        } else {
            resp.setStatus(400);
        }
    }
}
