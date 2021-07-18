import java.util.Scanner;

public class Answer {
    private String answer;

    public String createAnswer() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Answer: ");
        this.answer = sc.nextLine();
        return this.answer;
    }
}
