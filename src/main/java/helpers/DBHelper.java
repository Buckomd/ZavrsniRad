package helpers;

import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

public class DBHelper {
    private Connection conn = null;
    private String server_ip = "127.0.0.1";
    private String shema = "zavrsniradbaza";
    private String url = "jdbc:mysql://" + server_ip + "/" + shema;

    //Opening connection
    public void openConnection() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        if (conn == null) {
            try {
                conn = DriverManager.getConnection(url, "root", "Aleksandar.94");
                System.out.println("Uspesna konekcija");
            } catch (SQLException e) {
                System.out.println("DB connection error");
                throw new RuntimeException(e);
            }
        }
    }

    //Check for user in db by email
    public ResultSet getUserByEmail(String email) {

        ResultSet rs = null;
        openConnection();
        if (conn == null) {
            System.out.println("Get user by email failed");
            return null;
        } else {
            try {
                rs = conn.createStatement().executeQuery("SELECT * FROM users WHERE user_email = '" + email + "'");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return rs;
    }

    //Check for user in db by id
    public ResultSet getUserByID(int id) {

        ResultSet rs = null;
        openConnection();
        if (conn == null) {
            System.out.println("Get user by email failed");
            return null;
        } else {
            try {
                rs = conn.createStatement().executeQuery("SELECT * FROM users u INNER JOIN country c ON u.country_id = c.country_id WHERE user_id = '" + id + "'");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return rs;
    }

    //Get user role
    public ResultSet getUserRole(int id){
        ResultSet rs = null;
        openConnection();
        if (conn == null) {
            System.out.println("Get user by email failed");
            return null;
        } else {
            try {
                String q = "SELECT roles_id FROM user_roles WHERE user_id = '" + id + "'";
                rs = conn.createStatement().executeQuery(q);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return rs;
    }

    //Check for user in db by email and password (login)
    public ResultSet getUserByEmailAndPassword(String email, String password) {

        ResultSet rs = null;
        openConnection();
        if (conn == null) {
            System.out.println("Get user by email failed");
            return null;
        } else {
            try {
                String q = "SELECT * FROM users WHERE user_email = '" + email + "' AND user_password ='" + password + "'";
                rs = conn.createStatement().executeQuery(q);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return rs;
    }

    //Save user into db
    public boolean saveUser(String name, String lastname, String email, String password, String country, String dateString) {

        java.util.Date date = null;
        java.sql.Date dataBaseDate = null;
        int roleId = 2;

        int countryId = Integer.parseInt(country);
        String encodedString = Base64.getEncoder().encodeToString(password.getBytes());

        String insertUser = "INSERT INTO users (user_name,user_lastName,user_email,user_password,user_dateBirth,country_id) VALUES (?,?,?,?,?,?)";

        PreparedStatement ps = null;

        openConnection();

        if (conn == null) {
            System.out.println("Registration failed! Lost connection");
            return false;
        } else {
            try {
                // Converting date for db
                String[] datesArray = dateString.split("-");
                String newDateString = datesArray[2] + "/" + datesArray[1] + "/" + datesArray[0];
                date = new SimpleDateFormat("dd/MM/yyyy").parse(newDateString);
                dataBaseDate = new java.sql.Date(date.getTime());

                ps = conn.prepareStatement(insertUser);
                ps.setString(1, name);
                ps.setString(2, lastname);
                ps.setString(3, email);
                ps.setString(4, encodedString);
                ps.setDate(5, dataBaseDate);
                ps.setInt(6, countryId);
                ps.executeUpdate();


                ResultSet rs = getUserByEmail(email);

                while (rs.next()){
                    int idUser = rs.getInt("user_id");

                    Statement st = conn.createStatement();
                    String insertRole = "INSERT INTO user_roles (user_id, roles_id) VALUES ('" + idUser + "', '" + roleId + "')";
                   st.executeUpdate(insertRole);
                    System.out.println("Rola upisana");
                }

                return true;
            } catch (SQLException e) {
                //throw new RuntimeException(e);
                System.out.println("Save user error!");
            } catch (ParseException pe) {
                System.out.println("Parse exception!");
            }
        }
        return false;
    }

    //Get ResultSet all Tours
    public ResultSet getAllTours(){
        ResultSet rs = null;
        openConnection();
        if (conn == null) {
            System.out.println("Load tours failed! Bad connection!");
            return null;
        } else {
            try {
                String q = "SELECT * FROM tours t LEFT JOIN transportation tr on t.transport_id = tr.transport_id LEFT JOIN region r on t.region_id = r.region_id LEFT JOIN users u on t.user_id = u.user_id";
                rs = conn.createStatement().executeQuery(q);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return rs;
    }

    //Delete single tour
    public boolean deleteTour(int id){
        Statement st = null;
        openConnection();
        if (conn == null) {
            System.out.println("Load tours failed! Bad connection!");
            return false;
        } else {
            try {
                String deleteImageForTour = "DELETE FROM images WHERE tours_id = '" + id + "'";
                String deleteTour = "DELETE FROM tours WHERE tours_id = '" + id + "'";
                st = conn.createStatement();
                st.executeUpdate(deleteImageForTour);
                st.executeUpdate(deleteTour);
                return true;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //Get single tour
    public ResultSet getSignleTour(int id){
        ResultSet rs = null;
        openConnection();
        if (conn == null) {
            System.out.println("Load tours failed! Bad connection!");
            return null;
        } else {
            try {
                String q = "SELECT * FROM tours t LEFT JOIN transportation tr on t.transport_id = tr.transport_id LEFT JOIN region r on t.region_id = r.region_id LEFT JOIN users u on t.user_id = u.user_id WHERE tours_id = '" + id + "'";
                rs = conn.createStatement().executeQuery(q);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return rs;
    }

    //Get sigle picture for tour
    public ResultSet getSiglePicture(int id){
        ResultSet rs = null;
        openConnection();
        if (conn == null) {
            System.out.println("Load images for tour failed! Bad connection!");
            return null;
        } else {
            try {
                String q = "SELECT * FROM images WHERE tours_id = '" + id + "' LIMIT 1";
                rs = conn.createStatement().executeQuery(q);
            } catch (SQLException e) {
                System.out.println("Can't load single images from database!");
            }
        }
        return rs;
    }

    //Get ResultSet all images for sigle tour
    public ResultSet getAllPicturesForSigleTour(int id){
        ResultSet rs = null;
        openConnection();
        if (conn == null) {
            System.out.println("Load images for tour failed! Bad connection!");
            return null;
        } else {
            try {
                String q = "SELECT * FROM images WHERE tours_id = '" + id + "'";
                rs = conn.createStatement().executeQuery(q);
            } catch (SQLException e) {
                System.out.println("Can't load images from database!");
            }
        }
        return rs;
    }

    //Create new tour
    public boolean createTours(String title, String description, String tourDate, String maxNumOfPassengers, String toursPrice, String trasnport, String region, String guide) {

        java.util.Date date = null;
        java.sql.Date dataBaseDate = null;

        String[] datesArray = tourDate.split("-");
        String newDateString = datesArray[2] + "/" + datesArray[1] + "/" + datesArray[0];
        int transportId = Integer.parseInt(trasnport);
        int regionId = Integer.parseInt(region);
        int guideId = Integer.parseInt(guide);
        int passengersNumber = Integer.parseInt(maxNumOfPassengers);
        double price = Double.parseDouble(toursPrice);
        long newTourId = 0;

        openConnection();
        PreparedStatement ps = null;
        if (conn == null) {
            System.out.println("Load images for sigle tour failed! Bad connection!");
            return false;
        } else {
            try {

                String sql = "INSERT INTO tours (tours_title, tours_description, tours_date, tours_maxNumOfTravelers, tours_price, transport_id, region_id, user_id) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

                date = new SimpleDateFormat("dd/MM/yyyy").parse(newDateString);
                dataBaseDate = new java.sql.Date(date.getTime());

                ps = conn.prepareStatement(sql);
                ps.setString(1,title);
                ps.setString(2,description);
                ps.setDate(3,dataBaseDate);
                ps.setInt(4,passengersNumber);
                ps.setDouble(5,price);
                ps.setInt(6,transportId);
                ps.setInt(7,regionId);
                ps.setInt(8,guideId);

                ps.executeUpdate();
                return true;
            } catch (SQLException e) {
                System.out.println("Create tours error!");
            } catch (ParseException pe) {
                System.out.println("Parse exception!");
            }
        }
        return false;
    }

    //INSERT IMAGES FOR ONE TOUR
    public boolean saveImagesForSingleTour(int id, ArrayList<Blob> listOfBlobs){

        ResultSet rs = null;
        openConnection();
        if (conn == null) {
            System.out.println("Save images for tous failed! Bad connection!");
            return false;
        } else {
            try {
                String sql = "INSERT INTO images(img_image, tours_id) VALUES(?,?)";

               for(int i = 0; i < listOfBlobs.size(); i++){
                   PreparedStatement ps = conn.prepareStatement(sql);
                   ps.setBlob(1, listOfBlobs.get(i));
                   ps.setInt(2, id);
                   ps.executeUpdate();
               }
               return true;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //Get ResultSet of countries
    public ResultSet getCountries(){

        ResultSet rs = null;
        openConnection();
        if (conn == null) {
            System.out.println("Load country failed! Bad connection!");
            return null;
        } else {
            try {
                String q = "SELECT * FROM country";
                rs = conn.createStatement().executeQuery(q);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return rs;
    }

    //Save reservation
    public boolean saveReservatio(int tours_id, int user_id){

        ResultSet rs = null;
        openConnection();
        if (conn == null) {
            System.out.println("Save reservatio  failed! Bad connection!");
            return false;
        } else {
            try {
                String sql = "INSERT INTO reservation(tours_id, user_id) VALUES(?,?)";

                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setInt(1,tours_id );
                    ps.setInt(2, user_id);
                    ps.executeUpdate();
                return true;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //Get reservation for user
    public ResultSet getReservationForUser(int user_id){
        ResultSet rs = null;
        openConnection();
        if (conn == null) {
            System.out.println("Load reservation failed! Bad connection!");
            return null;
        } else {
            try {
                String q = "SELECT * FROM reservation r INNER JOIN tours t on r.tours_id = t.tours_id WHERE r.user_id =  '" + user_id + "'";
                rs = conn.createStatement().executeQuery(q);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return rs;
    }

    //Get ResultSet of regions
    public ResultSet getRegions(){

        ResultSet rs = null;
        openConnection();
        if (conn == null) {
            System.out.println("Load region failed. Bad connection!");
            return null;
        } else {
            try {
                String q = "SELECT * FROM region";
                rs = conn.createStatement().executeQuery(q);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return rs;
    }

    //Get ResultSet of tranportations
    public ResultSet getTranportation(){

        ResultSet rs = null;
        openConnection();
        if (conn == null) {
            System.out.println("Load tranportations failed! Bad connection!");
            return null;
        } else {
            try {
                String q = "SELECT * FROM transportation";
                rs = conn.createStatement().executeQuery(q);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return rs;
    }

    //Get ResultSet guides
    public ResultSet getGuides(){
        int roleId = 3;

        ResultSet rs;
        openConnection();
        if (conn == null) {
            System.out.println("Load guides failed! Bad connection!");
            return null;
        } else {
            try {
                String q = "SELECT user_id, user_name, user_lastName FROM users WHERE roles_id = '" + roleId + "'";
                rs = conn.createStatement().executeQuery(q);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return rs;
    }

    //Get ResultSet ofpics for home page
    public ResultSet getPictures(){

        ResultSet rs;
        openConnection();
        if (conn == null) {
            System.out.println("Load home picture failed! Bad connection!");
            return null;
        } else {
            try {
                String q = "SELECT * FROM homepic h inner join region r on h.region_id = r.region_id";
                rs = conn.createStatement().executeQuery(q);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return rs;
    }




}
