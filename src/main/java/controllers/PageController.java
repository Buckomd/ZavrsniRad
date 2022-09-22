package controllers;

import helpers.DBHelper;
import helpers.ValidationHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;
import org.json.JSONObject;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;


public class PageController {

    private DBHelper dbHelper = new DBHelper();

    public PageController(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }


    //Get list of all regions
    public ArrayList<JSONObject> getListOfRegionsForPage() {

        ArrayList<JSONObject> listOfRegions = new ArrayList<>();

        try {
            ResultSet rs = dbHelper.getRegions();
            while (rs.next()) {
                JSONObject obj = new JSONObject();
                obj.put("id", rs.getInt("region_id"));
                obj.put("name", rs.getString("region_name"));
                listOfRegions.add(obj);
            }

        } catch (SQLException e) {
            System.out.println("Failed to read the region!");
        }
        return listOfRegions;
    }

    //Get list of all trasnport types
    public ArrayList<JSONObject> getListofTrasnportation() {

        ArrayList<JSONObject> listOfTrasnport = new ArrayList<>();

        try {
            ResultSet rs = dbHelper.getTranportation();
            while (rs.next()) {
                JSONObject obj = new JSONObject();
                obj.put("id", rs.getInt("transport_id"));
                obj.put("name", rs.getString("transport_mehod"));
                listOfTrasnport.add(obj);
            }
        } catch (SQLException e) {
            System.out.println("Failed to read the trasnport types!");
        }
        return listOfTrasnport;
    }

    //Get list of all guides
    public ArrayList<JSONObject> getAllGuides() {

        ArrayList<JSONObject> listOfGuides = new ArrayList<>();

        try {
            ResultSet rs = dbHelper.getGuides();
            while (rs.next()) {
                JSONObject obj = new JSONObject();
                obj.put("id", rs.getInt("user_id"));
                obj.put("name", rs.getString("user_name"));
                obj.put("lastName", rs.getString("user_lastName"));
                listOfGuides.add(obj);
            }
        } catch (SQLException e) {
            System.out.println("Failed to read guides!");
        }
        return listOfGuides;
    }

    //Get all countries
    public ArrayList<JSONObject> getAllCountries() {

        ArrayList<JSONObject> listOfCountries = new ArrayList<>();

        try {
            ResultSet rs = dbHelper.getCountries();
            while (rs.next()) {
                JSONObject obj = new JSONObject();
                obj.put("id", rs.getInt("country_id"));
                obj.put("name", rs.getString("country_country"));
                listOfCountries.add(obj);
            }
        } catch (SQLException e) {
            System.out.println("Failed to read countires!");
        }
        return listOfCountries;
    }

    //Images for new tour
    public ArrayList<Blob> getAllImageFromInputFIle(HttpServletRequest request, HttpServletResponse response) {

        ArrayList<Blob> listaBlobova = new ArrayList<>();
        Blob blob = null;
        byte[] byteArray;

        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (!isMultipart) {
        } else {
            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            List items = null;
            try {
                items = upload.parseRequest(new ServletRequestContext(request));
            } catch (FileUploadException e) {
                e.printStackTrace();
            }
            Iterator itr = items.iterator();
            while (itr.hasNext()) {
                FileItem item = (FileItem) itr.next();
                try {
                    byte[] sourceBytes = IOUtils.toByteArray(item.getInputStream());
                    blob = new SerialBlob(sourceBytes);
                    listaBlobova.add(blob);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (IOException ex) {
                    System.out.println("Tlalalal");
                }
            }
        }
        return listaBlobova;
    }

    //Get images for home page
    public ArrayList<JSONObject> getPicturesForHomePage() {
        ArrayList<JSONObject> listOfBlobs = new ArrayList<>();

        try {
            ResultSet rs = dbHelper.getPictures();

            while (rs.next()) {
                JSONObject obj = new JSONObject();
                Blob blob = rs.getBlob("homepic_picture");
                int blobLength = (int) blob.length();
                byte[] ba = blob.getBytes(1, blobLength);
                String photo64 = Base64.getEncoder().encodeToString(ba);
                String picsName = rs.getString("homepic_naziv");
                String picsRegion = rs.getString("region_name");
                int id = rs.getInt("homepic_id");
                obj.put("id", id);
                obj.put("string64", photo64);
                obj.put("picsname", picsName);
                obj.put("picsregion", picsRegion);
                listOfBlobs.add(obj);
            }
        } catch (SQLException e) {
            System.out.println("Read image for home page error!");
        }
        return listOfBlobs;
    }

    //Get reservation
    public ArrayList<JSONObject> getReservationForUser(int userID){

        ArrayList<JSONObject> listOfReservation = new ArrayList<>();

        try {
            ResultSet rs = dbHelper.getReservationForUser(userID);

            while (rs.next()) {
                JSONObject obj = new JSONObject();
                obj.put("tourtitle", rs.getString("tours_title"));
                obj.put("date", rs.getString("tours_date"));
                obj.put("price", rs.getString("tours_price"));
                listOfReservation.add(obj);
            }
        } catch (SQLException e) {
            System.out.println("Read image for home page error!");
        }
        return listOfReservation;
    }


}



