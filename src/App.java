import java.util.Scanner;
import java.util.UUID;

public class App {
    private static User currentUser;

    public static void main(String[] args) throws Exception {
        UserFactory userfactory = new UserFactory();
        Scanner sc = new Scanner(System.in);

        System.out.println(Color.BLUE_BOLD + "\u2605 Welcome to the Survey App \u2605" + Color.RESET);
        System.out.println("Type any number between 1 and 3");
        System.out.println("1) LogIn");
        System.out.println("2) SignUp");
        System.out.println("3) Continue as guest");
        System.out.println("4) End");

        int selection = sc.nextInt();
        switch (selection) {
            case 1:
                currentUser = userfactory.getUser(0);
                mainMenu();
                break;
            case 2:
                currentUser = userfactory.getUser(1);
                mainMenu();
                break;
            case 3:
                currentUser = userfactory.getUser(2);
                guestMenu();
                break;
            case 4:
                break;
        }
    }

    public static void mainMenu() {
        Scanner sc = new Scanner(System.in);
        int select = 0;
        UserRegistered registeredUser = (UserRegistered) currentUser;
        UUID id = registeredUser.getUuid();
        System.out.println("__________________________________\n");
        System.out.println("Mainmenu");
        System.out.println("Type any number between 1 and 5");
        System.out.println("1) Create Survey");
        System.out.println("2) Choose Survey");
        System.out.println("3) Search Survey");
        System.out.println("4) Show my Surveys and their Stats");
        System.out.println("5) Show Stats");
        select = sc.nextInt();
        
        switch (select) {
            case 1:
                Survey survey = new Survey();
                survey.author = registeredUser.getUsername();
                survey.createSurvey();
                break;
            case 2:
                Survey existingSurvey = new Survey();
                existingSurvey.takeSurvey(Data.chooseSurveyFromData(id), id);
                break;
            case 3:
                Survey searchSurvey = new Survey();
                searchSurvey.search(id);
                break;
            case 4:
                registeredUser.seeSurveyStats();
                break;
            case 5:
                registeredUser.showStats();
                break;
        }
        mainMenu();
    }

    public static void guestMenu() {
        GuestUser guestUser = (GuestUser) currentUser;
        UUID id = guestUser.getUuid();
        Scanner sc = new Scanner(System.in);
        System.out.println("__________________________________\n");
        System.out.println("Mainmenu"); // FÜR GÄSTE
        System.out.println("Type any number between 1 and 3");
        System.out.println("1) Choose Survey");
        System.out.println("2) Search Survey");
        System.out.println("3) Show Stats");
        System.out.println("4) Create an Account");
        int guestSelect = sc.nextInt();
        switch (guestSelect) {
            case 1:
                Survey existingSurvey = new Survey();
                
                existingSurvey.takeSurvey(Data.chooseSurveyFromData(id), id);
                guestMenu();
                break;
            case 2:
                Survey searchSurvey = new Survey();
                searchSurvey.search(id);
                guestMenu();
                break;
            case 3:
                guestUser.showStats();
                guestMenu();
                break;
            case 4:
                UserFactory userfactory = new UserFactory();
                currentUser = userfactory.getUser(1);
                mainMenu();
                break;
        }
    }
}
