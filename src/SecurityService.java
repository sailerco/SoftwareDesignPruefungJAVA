import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Scanner;
import org.json.simple.*;
import org.json.simple.parser.*;

public class SecurityService {
    // create an object of SecurityService
    private static SecurityService instance = new SecurityService();

    // make the constructor private so that this class cannot be
    private SecurityService() {
    }

    public static SecurityService getInstance() {
        return instance;
    }

    public String[] signUp() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Username: ");
        String username = sc.nextLine();
        boolean isValid = checkAlphaNumeric(username);
        boolean usernameNotAnDuplicate = checkSignUpData(username);
        if (!isValid || !usernameNotAnDuplicate) {
            if (!isValid)
                System.out.println("The username must be alphanumeric. No Special Characters");
            else
                System.out.println("The username is already in use");
            return signUp();
        } else {
            System.out.print("Password: ");
            String password = sc.nextLine();
            while (password.equals("")) {
                System.out.print("Password shouldn't be nothing, enter a new one: ");
                password = sc.nextLine();
            }
            password = hashPassword(password);
            String[] userdata = { username, password };
            return userdata;
        }
    }

    public String[] logIn() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Username: ");
        String username = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();
        password = hashPassword(password);
        boolean isValid = checkLogInData(username, password);
        if (!isValid) {
            System.out.println("username or password was invalid");
            System.out.println("Do you wanna try again or create an Account?");
            System.out.println("1 Try again");
            System.out.println("2 Create an Account");
            int deciscion = sc.nextInt();
            if (deciscion == 1) {
                return logIn();
            } else {
                return null;
            }
        }
        String[] userData = { username, password };
        return userData;
    }

    public boolean checkAlphaNumeric(String _username) {
        for (int i = 0; i <= _username.length() - 1; i++) {
            String character = String.valueOf(_username.charAt(i));
            if (!character.matches("[a-zA-Z1-9]+")) {
                return false;
            }
        }
        if (_username.equals(""))
            return false;
        return true;
    }

    public boolean checkSignUpData(String _username) {
        try {
            JSONArray jsonArray = Data.loadJSON("data/user.json"); // save all users
            for (int i = 0; i <= jsonArray.size() - 1; i++) { // go through all users and compare names
                JSONObject h = (JSONObject) jsonArray.get(i);
                String x = (String) h.get("username");
                if (x.equalsIgnoreCase(_username)) {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private boolean checkLogInData(String _username, String _password) {
        try {
            JSONArray jsonArray = Data.loadJSON("data/user.json");
            for (int i = 0; i <= jsonArray.size() - 1; i++) {
                JSONObject userData = (JSONObject) jsonArray.get(i);
                String validateUsername = (String) userData.get("username");
                String validatePassword = (String) userData.get("password");
                if (validateUsername.equals(_username) && validatePassword.equals(_password)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private String hashPassword(String _passwordToHash) {
        // hashing of password, so it's more secure
        // https://www.baeldung.com/sha-256-hashing-java
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(_passwordToHash.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder(2 * encodedhash.length);
            for (int i = 0; i < encodedhash.length; i++) {
                String hex = Integer.toHexString(0xff & encodedhash[i]);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
