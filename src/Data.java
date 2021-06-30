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
                    System.out.format(numberOfMatch + ") " + survey.get("title") + "\t \t (by " + survey.get("username") + ")\n");
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
    public static void saveSurveyData(String title, String author, String currentDate, String startDate, String endDate, ArrayList<Questions> questionArray){
        try {
            JSONParser parser = new JSONParser();
            Object obj;
            obj = parser.parse(new FileReader("data/survey.json"));
            JSONArray jsonObject = (JSONArray) obj; // read already existing data
            // insert new data
            JSONObject surveyObject = new JSONObject();
            surveyObject.put("title", title);
            surveyObject.put("username", author);
            surveyObject.put("dateOfCreation", currentDate);
            surveyObject.put("startDate", startDate);
            surveyObject.put("endDate", endDate);
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
}
