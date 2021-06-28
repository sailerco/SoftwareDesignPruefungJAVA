import java.util.*;

public class Questions {
    private int numberOfAnswers = 0;
    public String questionTitle;
    public ArrayList<String> array = new ArrayList<String>();

    public void createQuestions() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Question Titel: ");
        String title = sc.nextLine();
        this.questionTitle = title;
    }

    public boolean addAnswers() {
        this.numberOfAnswers++;
        Answer answer = new Answer();
        this.array.add(answer.createAnswer());
        switch (this.numberOfAnswers) {
            case 1:
                return true;
            case 10:
                return false;
            default:
                return Survey.addMore("Answers");
        }
    }

}
