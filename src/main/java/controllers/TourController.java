package controllers;

import helpers.DBHelper;
import helpers.ValidationHelper;
import org.json.JSONObject;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;

public class TourController {

    private DBHelper dbHelper = new DBHelper();

    public TourController(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    //Create new tour
    public boolean createNewTour(String title, String description, String tourDate, String maxNumOfPassengers,
                                 String toursPrice, String trasnport, String region, String guide) {


        if (title == null || description == null || tourDate == null || maxNumOfPassengers == null ||
                toursPrice == null || trasnport == null || region == null || guide == null) {
            return false;
        } else
            if (dbHelper.createTours(title, description, tourDate, maxNumOfPassengers, toursPrice, trasnport, region, guide)) {
                return true;
            }

        return false;
    }

    //Save images for new tour
    public boolean saveImagesForNewTour( ArrayList<Blob> listOfBlobs){


        int index = 0;

        try {
            ResultSet rs = dbHelper.getAllTours();
            while (rs.next()) {
              index = rs.getInt("tours_id");
            }
        } catch (SQLException e) {
            System.out.println("Failed to read the last tour id!");
        }

        if(index == 0 || listOfBlobs.size() == 0) {
            return false;
        } else
            if(dbHelper.saveImagesForSingleTour(index,listOfBlobs)){
                return true;
            }
        return false;
    }

    //Get all tours
    public ArrayList<JSONObject> getAllTours(){


        ArrayList<JSONObject> listOfTours = new ArrayList<>();

        try {
            ResultSet rs = dbHelper.getAllTours();
            while (rs.next()) {
                JSONObject obj = new JSONObject();
                int id =  rs.getInt("tours_id");
                obj.put("id", id);
                obj.put("title", rs.getString("tours_title"));
                obj.put("desc", rs.getString("tours_description"));
                obj.put("tourdate", rs.getDate("tours_date"));
                obj.put("numoftravelers", rs.getInt("tours_maxNumOfTravelers"));
                obj.put("tourprice", rs.getDouble("tours_price"));
                obj.put("transport", rs.getString("transport_mehod"));
                obj.put("region", rs.getString("region_name"));
                obj.put("guideName", rs.getString("user_name"));
                obj.put("guideLastname", rs.getString("user_lastName"));


                ResultSet rsimage = dbHelper.getSiglePicture(id);

                while (rsimage.next()){
                    Blob blob = rsimage.getBlob("img_image");
                    int blobLength = (int) blob.length();
                    byte[] ba = blob.getBytes(1, blobLength);
                    String photo64 = Base64.getEncoder().encodeToString(ba);
                    obj.put("string64", photo64);
                }
                listOfTours.add(obj);
            }
        } catch (SQLException e) {
            System.out.println("Failed to read guides!");
        }
        return listOfTours;
    }

    //Get single tour
    public ArrayList<JSONObject> getSingleTour(int ids){

        ArrayList<JSONObject> singletour = new ArrayList<>();

        try {
            ResultSet rs = dbHelper.getSignleTour(ids);
            while (rs.next()) {
                JSONObject obj = new JSONObject();
                int id =  rs.getInt("tours_id");
                obj.put("id", id);
                obj.put("title", rs.getString("tours_title"));
                obj.put("desc", rs.getString("tours_description"));
                obj.put("tourdate", rs.getDate("tours_date"));
                obj.put("numoftravelers", rs.getInt("tours_maxNumOfTravelers"));
                obj.put("tourprice", rs.getDouble("tours_price"));
                obj.put("transport", rs.getString("transport_mehod"));
                obj.put("region", rs.getString("region_name"));
                obj.put("guideName", rs.getString("user_name"));
                obj.put("guideLastname", rs.getString("user_lastName"));


                ResultSet rsimage = dbHelper.getSiglePicture(id);

                while (rsimage.next()){
                    Blob blob = rsimage.getBlob("img_image");
                    int blobLength = (int) blob.length();
                    byte[] ba = blob.getBytes(1, blobLength);
                    String photo64 = Base64.getEncoder().encodeToString(ba);
                    obj.put("string64", photo64);
                }
                singletour.add(obj);
            }
        } catch (SQLException e) {
            System.out.println("Failed to read guides!");
        }
        return singletour;
    }

    //Get all images for single tour
    public ArrayList<JSONObject> getAllPictureForSingleTour(int id){

        ArrayList<JSONObject> listOfBlobs = new ArrayList<>();

        try {
            ResultSet rs = dbHelper.getAllPicturesForSigleTour(id);

            while (rs.next()) {
                JSONObject obj = new JSONObject();
                Blob blob = rs.getBlob("img_image");
                int blobLength = (int) blob.length();
                byte[] ba = blob.getBytes(1, blobLength);
                String photo64 = Base64.getEncoder().encodeToString(ba);

                int ids = rs.getInt("img_id");
                obj.put("id", ids);
                obj.put("string64", photo64);
                listOfBlobs.add(obj);
            }
        } catch (SQLException e) {
            System.out.println("Read image for home page error!");
        }
        return listOfBlobs;
    }




}
