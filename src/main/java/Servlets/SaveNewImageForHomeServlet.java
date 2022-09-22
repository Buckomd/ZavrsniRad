package Servlets;

import helpers.DBHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;

@WebServlet("/saveimage")
public class SaveNewImageForHomeServlet extends HttpServlet {

    private DBHelper dbHelper = new DBHelper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        String base64 = req.getParameter("base64");
        String title = req.getParameter("title");
        String id = req.getParameter("id");

        byte[] byteArray = Base64.getDecoder().decode(base64.getBytes());

        try {
            Blob blob = new SerialBlob(byteArray);
            if(dbHelper.saveNewHomePicture(blob, title, Integer.parseInt(id))){
                System.out.println("Dobro je");
            } else {
                System.out.println("Nije dobro");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}
