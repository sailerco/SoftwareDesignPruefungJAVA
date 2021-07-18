import java.util.Scanner;
import java.util.UUID;

public class Questionary {
    private static User currentUser;

    public static void main(String[] args) throws Exception {
        UserFactory userfactory = new UserFactory();
        Scanner sc = new Scanner(System.in);

        System.out.println("Welcome to the Questionary App");
        System.out.println("Type any number between 1 and 4");
        System.out.println("1) LogIn");
        System.out.println("2) SignUp");
        System.out.println("3) Continue as guest");
        System.out.println("4) End");

        int selection = sc.nextInt();
        switch (selection) {
            case 1:
                currentUser = userfactory.getUser(0, null);
                mainMenu();
                break;
            case 2:
                currentUser = userfactory.getUser(1, null);
                mainMenu();
                break;
            case 3:
                currentUser = userfactory.getUser(2, null);
                guestMenu();
                break;
            case 4:
                sc.close();
                break;
        }
    }

    public static void mainMenu() {
        Scanner sc = new Scanner(System.in);
        int select = 0;
        UserRegistered registeredUser = (UserRegistered) currentUser;
        System.out.println("__________________________________\n");
        System.out.println("Mainmenu");
        System.out.println("Type any number between 1 and 6");
        System.out.println("1) Create Survey");
        System.out.println("2) Choose Survey");
        System.out.println("3) Search Survey");
        System.out.println("4) Show my Surveys and their Stats");
        System.out.println("5) Show Stats");
        System.out.println("6) End");
        select = sc.nextInt();

        switch (select) {
            case 1:
                registeredUser.createSurvey();
                break;
            case 2:
                registeredUser.chooseSurvey();
                break;
            case 3:
                registeredUser.searchSurvey();
                break;
            case 4:
                registeredUser.seeSurveyStats();
                break;
            case 5:
                registeredUser.showStats();
                break;
            case 6:
                sc.close();
                System.exit(0);
                break;
            default:
                mainMenu();
        }
        mainMenu();
    }

    public static void guestMenu() {
        GuestUser guestUser = (GuestUser) currentUser;
        UUID id = guestUser.getUuid();
        Scanner sc = new Scanner(System.in);
        System.out.println("__________________________________\n");
        System.out.println("Mainmenu");
        System.out.println("Type any number between 1 and 5");
        System.out.println("1) Choose Survey");
        System.out.println("2) Search Survey");
        System.out.println("3) Show Stats");
        System.out.println("4) Create an Account");
        System.out.println("5) End");
        int guestSelect = sc.nextInt();
        switch (guestSelect) {
            case 1:
                guestUser.chooseSurvey();
                guestMenu();
                break;
            case 2:
                guestUser.searchSurvey();
                guestMenu();
                break;
            case 3:
                guestUser.showStats();
                guestMenu();
                break;
            case 4:
                UserFactory userfactory = new UserFactory();
                currentUser = userfactory.getUser(3, id);
                mainMenu();
                break;
            case 5:
                sc.close();
                System.exit(0);
                break;
            default:
                guestMenu();
        }
    }
}
