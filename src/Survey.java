import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;

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

    public void finishSurvey() {
        printSurvey();
        saveData();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void chooseSurvey() {
    }

    public String search(String titleSearch) {
        // Search
        Scanner sc = new Scanner(System.in);
        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader("data/survey.json"));
            JSONArray jsonObject = (JSONArray) obj;
            int numberOfMatch = 0;
            ArrayList<String> titleThatMatched = new ArrayList<String>();
            for (int i = 0; i <= jsonObject.size() - 1; i++) {
                JSONObject survey = (JSONObject) jsonObject.get(i);
                String titleToMatch = (String) survey.get("title");
                if (titleToMatch.startsWith(titleSearch)) {
                    if (numberOfMatch == 0)
                        System.out.println("Choose if you want to play");
                    numberOfMatch++;
                    System.out.format(
                            numberOfMatch + ") " + survey.get("title") + "\t \t (by " + survey.get("author") + ")\n");
                    titleThatMatched.add(titleToMatch);
                }
                if (numberOfMatch != 0 && i == jsonObject.size() - 1) {
                    System.out.println(numberOfMatch + 1 + ") Search again");
                    System.out.println(numberOfMatch + 2 + ") Go back to menu");
                }
            }
            if (numberOfMatch == 0) {
                System.out.println("No Survey found. Do you wanna search again?");
                System.out.println("1) Search Again");
                System.out.println("2) Go back to menu");
                int search = sc.nextInt();
                if (search == 1) {
                    return null; // hier muss ich etwas anderes zurück geben
                } else {
                    return null; // return again
                }
            } else {
                int search = sc.nextInt();
                if (search == titleThatMatched.size() + 1) // search again
                    return null;
                else if (search == titleThatMatched.size() + 2) // return to Menu
                    return null;
                else
                    return titleThatMatched.get(search);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
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

    public void saveData() {
        try {
            JSONParser parser = new JSONParser();
            Object obj;
            obj = parser.parse(new FileReader("data/survey.json"));
            JSONArray jsonObject = (JSONArray) obj; // read already existing data
            // insert new data
            JSONObject surveyObject = new JSONObject();
            surveyObject.put("title", this.title);
            surveyObject.put("username", this.author);
            surveyObject.put("dateOfCreation", this.currentDate);
            surveyObject.put("startDate", this.startDate);
            surveyObject.put("endDate", this.endDate);
            JSONArray questions = new JSONArray();
            for (Questions q : questionArray) {
                int x = 1;
                JSONObject questionBlock = new JSONObject();
                JSONObject answerBlock = new JSONObject();
                questionBlock.put("title", q.questionTitle);

                for (String answer : q.array) {
                    answerBlock.put(x + "_answer", answer);
                    x++;
                }
                questionBlock.put("answers", answerBlock);
                questions.add(questionBlock);
            }
            surveyObject.put("questions", questions);
            jsonObject.add(surveyObject);
            try (FileWriter file = new FileWriter("data/survey.json")) {
                file.write(jsonObject.toJSONString());
                System.out.println("Data were successfully saved");
            } catch (IOException e) {
                System.out.println("Error initializing stream  ");
            }
        } catch (Exception e) {
            e.printStackTrace();
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
}