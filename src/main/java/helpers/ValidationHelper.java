package helpers;

import java.util.regex.Pattern;

public class ValidationHelper {

    public Boolean validateEmail(String email) {
        String emailRegex = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}";
        return Pattern.matches(emailRegex, email);
    }

    public Boolean validateName(String name) {
        String nameRegex = "^[A-Z][a-z]{2,15}$";
        return Pattern.matches(nameRegex, name);
    }

    public Boolean validateLastname(String lastname) {
        String lastnameRegex = "^[A-Z][a-z]{2,15}$";
        return Pattern.matches(lastnameRegex, lastname);
    }

    public Boolean validatePassword(String password) {
      // String passwordRegex = "^(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{6,16}$";
        String passwordRegex = "^[A-Z][a-z]{2,15}$";
        return Pattern.matches(passwordRegex, password);
    }
}

