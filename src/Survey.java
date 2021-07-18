import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Survey {
    public String title;
    public String creator;
    public String currentDate;
    public String startDate;
    public String endDate;
    private int numberOfQuestions = 0;
    public ArrayList<Question> questionArray = new ArrayList<Question>();

    public Survey() {
        this.setDate();
    }

    public void initializeSurvey() { //title, date, 
        System.out.println("__________________________________\n");
        Scanner sc = new Scanner(System.in);
        System.out.print("title: ");
        String title = sc.nextLine();
        setTitle(title);

        String startDate, endDate;
        do {
            System.out.print("Enter Start Date (dd.MM.yyyy): ");
            startDate = sc.nextLine();
        } while (!validateDate(startDate, null));
        this.startDate = startDate;
        do {
            System.out.print("Enter End Date (dd.MM.yyyy): ");
            endDate = sc.nextLine();
        } while (!validateDate(startDate, endDate));
        this.endDate = endDate;
        // ADD QUESTIONS
        while (addQuestions());
        finishSurvey();
    }

    private boolean addQuestions() {
        // add question
        this.numberOfQuestions++;
        System.out.println(this.numberOfQuestions + ". Question:");
        Question question = new Question();
        question.createQuestions();
        while (question.addAnswers());
        questionArray.add(question);
        if (this.numberOfQuestions < 5) // At least 5 questions
            return true;
        else {
            return addMore("Questions");
        }
    }

    public static boolean addMore(String _type) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Do you want to add more " + _type + "? Enter Y or N ");
        String yesOrNo = sc.nextLine();
        if (yesOrNo.equalsIgnoreCase("N")) {
            return false;
        } else {
            return true;
        }
    }

    public void finishSurvey() {
        printSurvey();
        Data.saveSurveyData(this, questionArray);
    }
    // #region date
    private boolean validateDate(String _startDate, String _endDate) {
        try {
            if (!this.dateExists(_startDate))
                return false;
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
            Date inputStartDate = formatter.parse(_startDate);
            Date currentDate = formatter.parse(this.currentDate);
            if (_endDate == null) {
                if (compareDate(currentDate, inputStartDate))
                    return true;
            } else {
                Date inputEndDate = formatter.parse(_endDate);
                if (!this.dateExists(_endDate))
                    return false;
                if (compareDate(inputStartDate, inputEndDate))
                    return true;
            }
        } catch (ParseException e) {
            System.out.println("Date Format is not valid");
        }
        return false;
    }

    private boolean compareDate(Date _startDate, Date _endDate) {
        if (_startDate.equals(_endDate) || _startDate.before(_endDate))
            return true;
        else {
            System.out.println("The Date shouldn't be in the past");
            return false;
        }
    }

    private boolean dateExists(String _date) {
        int daysOfMonth[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
        int day = Integer.parseInt(_date.substring(0, 2));
        int month = Integer.parseInt(_date.substring(3, 5));
        if (month > 0 && month <= 12) {
            if (day > 0 && day <= daysOfMonth[month - 1]) {
                return true;
            }
        }
        System.out.println("Date doesn't exist");
        return false;
    }

    private void setDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat simpleformat = new SimpleDateFormat("dd.MM.yyyy");
        String formattedDate = simpleformat.format(cal.getTime());
        this.currentDate = formattedDate;
    }

    // #endregion
    public void setTitle(String _title) {
        this.title = _title;
    }

    public void printSurvey() {
        for (Question q : questionArray) {
            System.out.println(q.questionTitle + " answers: ");
            int surveyCounter = 1;
            for (String answer : q.array) {
                System.out.println(surveyCounter + ") " + answer);
                surveyCounter++;
            }
        }
    }

    public void search(UUID _id) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Search... ");
        String search = scanner.nextLine();
        int surveyNumber = Data.searchSurvey(search, _id);
        if (surveyNumber > -1)
            this.takeSurvey(surveyNumber, _id);
        else if (surveyNumber == -1) // search again
            this.search(_id);
    }

    public void takeSurvey(int _surveyNumber, UUID _id) {
        Scanner sc = new Scanner(System.in);
        JSONObject choosenSurvey = Data.getSurvey(_surveyNumber);
        JSONArray allQuestions = (JSONArray) choosenSurvey.get("questions");
        ArrayList<Integer> saveForStats = new ArrayList<Integer>();
        for (int i = 0; i <= allQuestions.size() - 1; i++) { // iterate through all questions
            JSONObject question = (JSONObject) allQuestions.get(i);
            JSONObject answers = (JSONObject) question.get("answers");
            System.out.println(question.get("title"));
            for (int a = 0; a <= answers.size() - 1; a++) {
                System.out.println(a + 1 + ") " + answers.get(a + 1 + "_answer"));
            }
            int choose = sc.nextInt();
            saveForStats.add(choose);
        }
        Data.saveSurveyStats(_surveyNumber, saveForStats);
        Data.saveUserStats(_surveyNumber, _id);
    }
}
