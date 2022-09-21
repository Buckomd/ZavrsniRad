package controllers;

import helpers.DBHelper;
import helpers.ValidationHelper;
import org.json.JSONObject;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;

public class UserController {
    private DBHelper dbHelper = new DBHelper();
    private ValidationHelper validationHelper = new ValidationHelper();

    public UserController(DBHelper dbHelper, ValidationHelper validationHelper) {
        this.dbHelper = dbHelper;
        this.validationHelper = validationHelper;
    }

    // Login
    public Boolean login(String email, String password) {

        if (!validationHelper.validateEmail(email) || !validationHelper.validatePassword(password)) {
            return false;
        }

        // Encode Password
        String encodedString = Base64.getEncoder().encodeToString(password.getBytes());

        // Check in db
        ResultSet rs = dbHelper.getUserByEmailAndPassword(email, encodedString);
        try {
            // if exists -> ok
            if (rs.next()) {
                System.out.println("User exists! Login.");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Login failed");
        }
        return false;
    }

    //Get user role
    public ArrayList<JSONObject> userRole(String email, String password){

        ArrayList<JSONObject> userIDroleID = new ArrayList<>();
        JSONObject obj = new JSONObject();

        // Encode Password
        String encodedString = Base64.getEncoder().encodeToString(password.getBytes());

        // Check in db
        ResultSet rs = dbHelper.getUserByEmailAndPassword(email, encodedString);
        try {
            // if exists -> ok
            if (rs.next()) {

                System.out.println("User exists! Login.");
                int user_id = rs.getInt("user_id");
                obj.put("userID", user_id);
               ResultSet rs1 = dbHelper.getUserRole(user_id);
               if(rs1.next()){
                   int roleId = rs1.getInt("roles_id");
                   obj.put("roleID", roleId);

               }
            }
        } catch (SQLException e) {
            System.out.println("Login failed");
        }
        userIDroleID.add(obj);

        return userIDroleID;
    }

    //Get single user
    public ArrayList<JSONObject> singleUser(int id ){

        ArrayList<JSONObject> singleUSer = new ArrayList<>();

        try {
            ResultSet rs = dbHelper.getUserByID(id);

            while (rs.next()) {
                JSONObject obj = new JSONObject();
                obj.put("name", rs.getString("user_name"));
                obj.put("lastname", rs.getString("user_lastName"));
                obj.put("birth", rs.getString("user_dateBirth"));
                obj.put("country", rs.getString("country_country"));
                singleUSer.add(obj);
            }
        } catch (SQLException e) {
            System.out.println("Read single user fail!");
        }
        return singleUSer;
    }

    // Register
    public Boolean register(String name, String lastname, String email, String password, String country, String dateString) {

        //Validation check
       if(!validationHelper.validateName(name) || !validationHelper.validateLastname(lastname) || !validationHelper.validateEmail(email) || !validationHelper.validatePassword(password)) {
           System.out.println("Bad validation!");
           return false;
       }

       //Check in db
       ResultSet rs = dbHelper.getUserByEmail(email);

        try {
            if(rs.next()){
                System.out.println("Email alredy exist!");
                return false;
            } else {
                //Save user into db
                if(dbHelper.saveUser(name, lastname, email, password, country, dateString)){
                    System.out.println("User successfully registered");
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("Registration failed");
        }

        if(dbHelper.saveUser(name, lastname, email, password,country,dateString)){
            return true;
        }
        return false;
    }


}