import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;

public class Data {
    public static String searchSurvey(String titleSearch) {
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
                    System.out.println(i + 1 + ") " + survey.get("title"));
                    System.out.format("\t \t (Avaible from " + survey.get("startDate") + " - " + survey.get("endDate")
                            + " | by " + survey.get("username") + ")\n");
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

    public static int chooseSurveyFromData() {
        Scanner sc = new Scanner(System.in);
        int choose = -1;
        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader("data/survey.json"));
            JSONArray jsonObject = (JSONArray) obj;
            int surveyNumbers = jsonObject.size();
            if (jsonObject.size() > 9)
                surveyNumbers = 10;
            for (int i = 0; i <= surveyNumbers - 1; i++) {
                JSONObject survey = (JSONObject) jsonObject.get(i);
                System.out.println(i + 1 + ") " + survey.get("title"));
                System.out.format("\t \t (Avaible from " + survey.get("startDate") + " - " + survey.get("endDate")
                        + " | by " + survey.get("username") + ")\n");
            }
            choose = sc.nextInt();
        } catch (Exception e) {
            // TODO: handle exception
        }
        return choose-1;
    }

    public static JSONObject getSurvey(int surveyNumber) {
        JSONArray jsonObject = null;
        try {
            JSONParser parser = new JSONParser();
            Object obj;
            obj = parser.parse(new FileReader("data/survey.json"));
            jsonObject = (JSONArray) obj; // read already existing data
            System.out.println(jsonObject.get(surveyNumber));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (JSONObject)jsonObject.get(surveyNumber);
    }
    
    public static void saveSurveyData(String title, String author, String currentDate, String startDate, String endDate, ArrayList<Questions> questionArray, String dataType) {
        try {
            JSONParser parser = new JSONParser();
            System.out.println("data/" + dataType);
            Object obj = parser.parse(new FileReader("data/" + dataType));
            JSONArray jsonObject = (JSONArray) obj; // read already existing data
            //String, Object statt String, String, weil es bei questions ein Array befindet
            Map<String, Object> surveyObject = new LinkedHashMap<String, Object>(); //Map, instead of JSONObject, so it will preserve order
            surveyObject.put("title", title);
            surveyObject.put("username", author);
            surveyObject.put("dateOfCreation", currentDate);
            surveyObject.put("startDate", startDate);
            surveyObject.put("endDate", endDate);
            JSONArray questions = new JSONArray();
            for (Questions q : questionArray) {
                int x = 1;
                Map<String, Object> questionBlock = new LinkedHashMap<String, Object>(); 
                Map<String, Object> answerBlock = new LinkedHashMap<String, Object>(); 
                questionBlock.put("title", q.questionTitle);

                for (String answer : q.array) {
                    if(dataType.equals("survey.json"))
                        answerBlock.put(x + "_answer", answer);
                    else
                        answerBlock.put(x + "_answer", 0);
                    x++;
                }
                questionBlock.put("answers", answerBlock);
                questions.add(questionBlock);
            }
            surveyObject.put("questions", questions);
            jsonObject.add(surveyObject);
            try (FileWriter file = new FileWriter("data/" + dataType)) { //ABSPEICHERN IN JSON FILE "data/survey.json" oder "data/surveyStats.json"
                file.write(jsonObject.toJSONString());
                System.out.println("Data were successfully saved");
            } catch (IOException e) {
                System.out.println("Error initializing stream  ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveUserData(UserRegistered user) {
        try {
            // read already existing data
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader("data/user.json"));
            JSONArray jsonObject = (JSONArray) obj;
            // insert new data
            JSONObject object = new JSONObject();
            object.put("username", user.getUsername());
            object.put("password", user.getPassword());

            // save data
            jsonObject.add(object);
            try (FileWriter file = new FileWriter("data/user.json")) {
                file.write(jsonObject.toJSONString());
                System.out.println("Data were successfully saved");
            } catch (IOException e) {
                System.out.println("Error initializing stream");
            }
        } catch (Exception e) {
            // create new user.json? maybe???
            e.printStackTrace();
        }
    }

    public static void saveSurveyStats(int surveyNumber, ArrayList<Integer> newStats){
        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader("data/surveyStats.json"));
            JSONArray jsonObject = (JSONArray) obj; 
            JSONObject surveyStats = (JSONObject)jsonObject.get(surveyNumber);
            JSONArray allQuestions = (JSONArray)surveyStats.get("questions");
            for(int i = 0; i <= allQuestions.size()-1; i++){
                JSONObject question = (JSONObject)allQuestions.get(i);
                JSONObject answers = (JSONObject)question.get("answers");
                int x = Integer.parseInt(answers.get(newStats.get(i) + "_answer").toString()) + 1;
                answers.replace(newStats.get(i) + "_answer", x);
            }
            try (FileWriter file = new FileWriter("data/surveyStats.json")) { 
            file.write(jsonObject.toJSONString());
            System.out.println("Data were successfully saved");
            } catch (IOException e) {
                System.out.println("Error initializing stream  ");
            }
            //SAVE SURVEY STATS
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
