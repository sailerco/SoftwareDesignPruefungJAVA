import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Survey {
    public String title;
    public String author;
    public String currentDate;
    public String startDate;
    public String endDate;
    public int numberOfQuestions = 0;
    public ArrayList<Questions> questionArray = new ArrayList<Questions>();

    public void createSurvey() {
        // TITEL
        Scanner sc = new Scanner(System.in);
        System.out.print("title: ");
        String title = sc.nextLine();
        setTitle(title);

        // DATE
        // get current Date without Time, SRC:
        // https://stackoverflow.com/questions/9629636/get-todays-date-in-java-at-midnight-time
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date currentDate = calendar.getTime();
        this.currentDate = currentDate.toString();
        String startDate, endDate;
        do {
            System.out.print("Enter Start Date (dd.MM.yyyy): ");
            startDate = sc.nextLine();
        } while (!validateDate(currentDate, startDate, null));
        this.startDate = startDate;
        do {
            System.out.print("Enter End Date (dd.MM.yyyy): ");
            endDate = sc.nextLine();
        } while (!validateDate(currentDate, startDate, endDate));
        this.endDate = endDate;
        // ADD QUESTIONS
        while (addQuestions());
        finishSurvey();
    }

    public boolean addQuestions() {
        // add question
        this.numberOfQuestions++;
        System.out.println(this.numberOfQuestions);
        Questions question = new Questions();
        question.createQuestions();
        while (question.addAnswers());
        questionArray.add(question);
        System.out.println(questionArray);
        // Check if there are at least 5 Questions, and ask if we should add more
        if (this.numberOfQuestions < 2)
            return true;
        else {
            return addMore("Questions");
        }
    }
    
    public static boolean addMore(String type) {    
        Scanner sc = new Scanner(System.in);
        System.out.println("Do you want to add more " + type + "? Enter Y or N ");
        String yesOrNo = sc.nextLine();
        if (yesOrNo.equalsIgnoreCase("N"))
            return false;
        else
            return true;
    }
    
    public void finishSurvey() {
        printSurvey();
        Data.saveSurveyData(this.title, this.author, this.currentDate, this.startDate, this.endDate, questionArray, "survey.json");

        Data.saveSurveyData(this.title, this.author, this.currentDate, this.startDate, this.endDate, questionArray, "surveyStats.json");
    }
 
    public boolean validateDate(Date currentDate, String startDate, String endDate) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
            Date inputStartDate = formatter.parse(startDate);
            if (endDate == null) {
                if (compareDate(currentDate, inputStartDate))
                    return true;
            } else {
                Date inputEndDate = formatter.parse(endDate);
                if (compareDate(inputStartDate, inputEndDate))
                    return true;
            }
        } catch (ParseException e) {
            System.out.println(Color.RED + "Date Format is not valid" + Color.RESET);
        }
        return false;
    }

    public boolean compareDate(Date start, Date end) {
        if (start.equals(end) || start.before(end))
            return true;
        else {
            System.out.println(Color.RED + "The Date shouldn't be in the past" + Color.RESET);
            return false;
        }
    }

    public void printSurvey() {
        for (Questions q : questionArray) {
            System.out.println(q.questionTitle + " answers: ");
            int x = 1;
            for (String answer : q.array) {
                System.out.println(x + ".)" + answer);
                x++;
            }
        }
    }

    public void takeSurvey(int surveyNumber){
        Scanner sc = new Scanner(System.in);
        JSONObject choosenSurvey = Data.getSurvey(surveyNumber);
        JSONArray allQuestions = (JSONArray)choosenSurvey.get("questions");
        ArrayList<Integer> saveForStats = new ArrayList<Integer>();
        for(int i = 0; i <= allQuestions.size()-1; i++){ //iterate through all questions
            JSONObject question = (JSONObject)allQuestions.get(i);
            JSONObject answers = (JSONObject)question.get("answers");
            System.out.println(question.get("title"));
            for(int a = 0; a <= answers.size()-1; a++){
                System.out.println(a+1 + " ) " + answers.get( a+1 + "_answer"));
            }
            int choose = sc.nextInt();
            saveForStats.add(choose);
        }
        Data.saveSurveyStats(surveyNumber, saveForStats);
    }
    public void setTitle(String title) {
        this.title = title;
    }
}
