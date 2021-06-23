import java.util.Scanner;

public class SecurityService {
    // create an object of SecurityService
    private static SecurityService instance = new SecurityService();

    // make the constructor private so that this class cannot be
    // instantiated
    private SecurityService() {
    }

    // Get the only object available
    public static SecurityService getInstance() {
        return instance;
    }

    public void signUp() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Username: ");
        String username = sc.nextLine();
        boolean isValid = checkAlphaNumeric(username);
        if (isValid == false) {
            System.out.println("The username must be alphanumeric. No Special Characters");
            signUp();
        }
        System.out.print("Password: ");
        String password = sc.nextLine();
    }

    public void logIn() {
        System.out.println("hello");
    }

    public boolean checkAlphaNumeric(String _username) {
        for (int i = 0; i <= _username.length() - 1; i++) {
            String character = String.valueOf(_username.charAt(i));
            if (character.matches("\\W")) {
                return false;
            }
        }
        return true;
    }

    public void checkSignUpData() {
        System.out.println("hello");
        //does Username already exist?
    }

    public void checkLogInData() {
        System.out.println("hello");
    }

}
