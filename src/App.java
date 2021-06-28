import java.util.Scanner;

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
                System.out.println("LogIn");
                currentUser = userfactory.getUser(0);
                mainMenu();
                break;
            case 2:
                currentUser = userfactory.getUser(1);
                System.out.println("lol");
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
        System.out.println("Mainmenu");
        System.out.println("Type any number between 1 and 5");
        System.out.println("1) Create Survey");
        System.out.println("2) Choose Survey");
        System.out.println("3) Search Survey");
        System.out.println("4) Show my Surveys");
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
                existingSurvey.createSurvey();
                break;
            case 3:
                System.out.print("Search... ");
                String search = sc.nextLine();
                Survey searchingSurvey = new Survey();
                searchingSurvey.search(search);
                break;
            case 4:
                break;
            case 5:
                break;
        }
    }

    public static void guestMenu() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Mainmenu"); // FÜR GÄSTE
        System.out.println("Type any number between 1 and 3");
        System.out.println("1) Choose Survey");
        System.out.println("2) Search Survey");
        System.out.println("3) Show Stats");
        int guestSelect = sc.nextInt();
        switch (guestSelect) {
            case 1:
                Survey existingSurvey = new Survey();
                existingSurvey.chooseSurvey();
                break;
            case 2:
                Scanner scanner = new Scanner(System.in);
                System.out.print("search: ");
                String search = scanner.nextLine();
                Survey searchingSurvey = new Survey();
                String s = searchingSurvey.search(search);
                if (s == null) {
                    guestMenu();
                }
                break;
            case 3:
                break;
        }
    }
}
