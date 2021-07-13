import java.io.FileReader;
import java.util.Scanner;
import org.json.simple.*;
import org.json.simple.parser.*;

public class SecurityService {
    // create an object of SecurityService
    private static SecurityService instance = new SecurityService();
    // make the constructor private so that this class cannot be
    // instantiated
    private SecurityService() {}
    // Get the only object available
    public static SecurityService getInstance() {
        return instance;
    }

    public String[] signUp() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Username: ");
        String username = sc.nextLine();
        boolean isValid = checkAlphaNumeric(username);
        boolean usernameNotAnDuplicate = checkSignUpData(username);
        if (isValid == false || usernameNotAnDuplicate == false) {
            System.out.println(Color.RED);
            if (!isValid)
                System.out.println("The username must be alphanumeric. No Special Characters");
            else
                System.out.println("The username is already in use");
            System.out.println(Color.RESET);
            return signUp();
        } else {
            System.out.print("Password: ");
            String password = sc.nextLine();
            String[] s = { username, password };
            return s;
        }
    }

    public String[] logIn() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Username: ");
        String username = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();
        boolean isValid = checkLogInData(username, password);
        if(!isValid){
            System.out.println(Color.RED);
            System.out.println("username or password was invalid");
            System.out.println(Color.RESET);
            System.out.println("Do you wanna try again or create an Account?");
            System.out.println("1 Try again");
            System.out.println("2 Create an Account");
            int deciscion = sc.nextInt();
            if(deciscion == 1){
                return logIn();
            }else{
                return null;
            }
        }
        String[] userData = { username, password };
        return userData;
    }

    public boolean checkAlphaNumeric(String username) {
        for (int i = 0; i <= username.length() - 1; i++) {
            String character = String.valueOf(username.charAt(i));
            if (character.matches("\\W")) {
                return false;
            }
        }
        return true;
    }

    public boolean checkSignUpData(String username) {
        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader("data/user.json"));
            JSONArray jsonObject = (JSONArray) obj; //save all users
            for (int i = 0; i <= jsonObject.size()-1; i++) { //go through all users and compare names
                JSONObject h = (JSONObject) jsonObject.get(i);
                String x = (String) h.get("username");
                if (x.equalsIgnoreCase(username)) {
                    return false;
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return true;
    }

    public boolean checkLogInData(String username, String password) {
        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader("data/user.json"));
            JSONArray jsonObject = (JSONArray) obj;
            for (int i = 0; i <= jsonObject.size()-1; i++) {
                JSONObject userData = (JSONObject) jsonObject.get(i);
                String validateUsername = (String) userData.get("username");
                String validatePassword = (String) userData.get("password");
                if (validateUsername.equals(username) && validatePassword.equals(password)) {
                    return true;
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return false;
    }
}
