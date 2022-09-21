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
import java.sql.Blob;
import java.util.ArrayList;


@WebServlet("/upload")
public class UploadNewTourServlet extends HttpServlet {

    private DBHelper dbHelper = new DBHelper();
    private TourController tourController = new TourController(dbHelper);
    PageController pageController = new PageController(dbHelper);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ArrayList<Blob> listaBlobova = new ArrayList<>();

        String title = request.getParameter("title");
        String desc = request.getParameter("desc");
        String tourdate = request.getParameter("tourdate");
        String numofpassengers = request.getParameter("numofpassengers");
        String tourprice = request.getParameter("tourprice");
        String guide = request.getParameter("guide");
        String transport = request.getParameter("transport");
        String region = request.getParameter("region");

        listaBlobova = pageController.getAllImageFromInputFIle(request,response);

        if(tourController.createNewTour(title,desc,tourdate,numofpassengers,tourprice,transport,region, guide))
        {
            System.out.println("Podaci uspeno upisani");
            response.setStatus(200);
        } else if(tourController.saveImagesForNewTour(listaBlobova)){
            response.setStatus(200);
            System.out.println("USPESNOO sacuvane slike");
        } else {
            System.out.println("Nije dobro tebra");
        }




    }

}
