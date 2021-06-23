import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        UserFactory userfactory = new UserFactory();
        Scanner sc = new Scanner(System.in);

        System.out.println("Welcome to the Survey App");
        System.out.println("Menu : ");
        System.out.println("Type any number between 1 and 3");
        System.out.println("1) LogIn");
        System.out.println("2) SignUp");
        System.out.println("3) Continue as guest");

        int selection = sc.nextInt();
        switch(selection){
            case 1:
                System.out.println("LogIn");
                userfactory.getUser(0);
                break;
            case 2:
                userfactory.getUser(1);
                System.out.println("lol");
                break;
            case 3:
                userfactory.getUser(2);
                System.out.println("x2x");
                break;
        }
    }
}
