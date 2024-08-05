package pl.bnb.services;

import org.springframework.stereotype.Service;

@Service
public class PasswordService {
    private String message = "";

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean checkPassword(String password, String samePassword) {
        int upperCaseCounter = 0;
        int digitCounter = 0;
        if (!(password.length() <= 20 && password.length() >= 8)) {
            setMessage("Password length should be between 8 and 20 characters");
            return false;
        }
        for (char c : password.toCharArray()) {
            if (Character.isWhitespace(c)) {
                setMessage("Password contains invalid characters");
                return false;
            }
            if (Character.isDigit(c)) {
                digitCounter++;
            }
            if (Character.isUpperCase(c)) {
                upperCaseCounter++;
            }
        }
        if (upperCaseCounter < 1 || digitCounter < 1) {
            setMessage("Password has to be at least 2 upper case letter and 2 number");
            return false;
        }
        if (!samePassword.equals(password)) {
            setMessage("Password does not match");
            return false;
        }
        setMessage("Password is correct, you can join to us right now.");
        return true;
    }
}
