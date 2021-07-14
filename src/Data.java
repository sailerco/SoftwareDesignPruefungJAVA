import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;

public class Data {
    
    private static JSONArray load(String datayType){
        try {
            // read already existing data
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(datayType));
            JSONArray jsonObject = (JSONArray) obj;
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
   
    public static int searchSurvey(String titleSearch, UUID userID) {
        // Search
        Scanner sc = new Scanner(System.in);
        try {
            JSONArray jsonObject = load("data/survey.json");
            int numberOfMatch = 0;
            ArrayList<Integer> titleThatMatched = new ArrayList<Integer>();
            ArrayList<Integer> inaccesible = new ArrayList<Integer>();
            for (int i = 0; i <= jsonObject.size() - 1; i++) {
                JSONObject survey = (JSONObject) jsonObject.get(i);
                String titleToMatch = (String) survey.get("title");
                if (titleToMatch.startsWith(titleSearch)) {
                    if (numberOfMatch == 0)
                        System.out.println("Choose if you want to play");
                    numberOfMatch++;
                    System.out.println(numberOfMatch + ") " + survey.get("title"));
                    titleThatMatched.add(i);
                    //TODO: Aulagern
                    if(Data.isInaccesible(userID, survey)){
                        inaccesible.add(numberOfMatch);
                        continue;
                    }
                    /* if(Data.ownSurvey(userID, survey.get("username").toString())){
                        inaccesible.add(numberOfMatch);
                        System.out.format("\t \t ("+ Color.RED +"Inaccessible."+ Color.RESET +" You created this survey "
                                + survey.get("username") + ")\n");
                        continue;
                    }
                    if(Data.takenAlready(userID, survey.get("title").toString())){
                        inaccesible.add(numberOfMatch);
                        System.out.format("\t \t ("+ Color.RED + "Inaccessible. "+ Color.RESET +"You took it already | by "
                                + survey.get("username") + ")\n");
                        continue;
                    }
                    if (Data.checkDate(survey.get("startDate"), true)) {
                        System.out.format("\t \t ("+ Color.RED + "Inaccessible,"+ Color.RESET +" start date: " + survey.get("startDate") + " | by "
                                + survey.get("username") + ")\n");
                        inaccesible.add(numberOfMatch);
                        continue;
                    }
                    if (Data.checkDate(survey.get("endDate"), false)) {
                        System.out.format("\t \t ("+ Color.RED + "Inaccessible, "+ Color.RESET +"end date: " + survey.get("endDate") + " | by "
                                + survey.get("username") + " | " + survey.get("timesTaken") + "x taken)\n");
                        inaccesible.add(numberOfMatch);
                        continue;
                    } */
                    System.out.format("\t \t ("+ Color.GREEN +"Avaible "+ Color.RESET + " from " + survey.get("startDate") + " - " + survey.get("endDate")
                            + " | by " + survey.get("username") + " | " + survey.get("timesTaken") + "x taken)\n");
                    
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
                    return -1; // hier muss ich etwas anderes zurück geben
                } else {
                    return -2; // return again
                }
            } else {
                int search = sc.nextInt();
                while(inaccesible.contains(search)){
                    System.out.println("It's inaccessible. Choose another one");
                    search = sc.nextInt();
                }
                if (search == numberOfMatch + 1) // search again
                    return -1;
                else if (search == numberOfMatch + 2) // return to Menu
                    return -2;
                else
                    return titleThatMatched.get(search);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -2;
        }
    }

    public static int chooseSurveyFromData(UUID userID) {
        Scanner sc = new Scanner(System.in);
        int choose = -1;
        try {
            JSONArray jsonObject = load("data/survey.json");
            int surveyNumbers = jsonObject.size();
            ArrayList<Integer> inaccesible = new ArrayList<Integer>();

            if (jsonObject.size() > 9) // show latest 10 or all that exist
                surveyNumbers = 10;

            for (int i = 0; i <= surveyNumbers - 1; i++) {
                JSONObject survey = (JSONObject) jsonObject.get(jsonObject.size() - 1 - i);
                System.out.println(i + 1 + ") " + survey.get("title"));
                //Check if it is accessible
                if(Data.isInaccesible(userID, survey)){
                    inaccesible.add(i+1);
                    continue;
                }
                /* if(Data.ownSurvey(userID, survey.get("username").toString())){
                    inaccesible.add(i + 1);
                    System.out.format("\t \t ("+ Color.RED +"Inaccessible."+ Color.RESET +" You created this survey"
                            + survey.get("username") + ")\n");
                    continue;
                }
                if(Data.takenAlready(userID, survey.get("title").toString())){
                    inaccesible.add(i + 1);
                    System.out.format("\t \t ("+ Color.RED + "Inaccessible,"+ Color.RESET +" You took it already | by "
                            + survey.get("username") + ")\n");
                    continue;
                }
                if (Data.checkDate(survey.get("startDate"), true)) {
                    System.out.format("\t \t ("+ Color.RED +"Inaccessible,"+ Color.RESET +" start date: " + survey.get("startDate") + " | by "
                            + survey.get("username") + ")\n");
                    inaccesible.add(i + 1);
                    continue;
                }
                if (Data.checkDate(survey.get("endDate"), false)) {
                    System.out.format("\t \t ("+ Color.RED +"Inaccessible,"+ Color.RESET +" end date: " + survey.get("endDate") + " | by "
                            + survey.get("username") + " | " + survey.get("timesTaken") + "x taken)\n");
                    inaccesible.add(i + 1);
                    continue;
                } */
                System.out.format("\t \t ("+ Color.GREEN +"Avaible "+ Color.RESET + "from "  + survey.get("startDate") + " - " + survey.get("endDate")
                        + " | by " + survey.get("username") + " | " + survey.get("timesTaken") + "x taken)\n");
            }
            choose = sc.nextInt();
            while (inaccesible.contains(choose)) {
                System.out.println("It's inaccessible. Choose another one");
                choose = sc.nextInt();
            }
            choose = surveyNumbers - choose;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return choose;
    }
    public static boolean isInaccesible(UUID userID, JSONObject survey){
        if(Data.ownSurvey(userID, survey.get("username").toString())){
            System.out.format("\t \t ("+ Color.RED +"Inaccessible."+ Color.RESET +" You created this survey"
                    + survey.get("username") + ")\n");
            return true;
        }
        if(Data.takenAlready(userID, survey.get("title").toString())){
            System.out.format("\t \t ("+ Color.RED + "Inaccessible,"+ Color.RESET +" You took it already | by "
                    + survey.get("username") + ")\n");
            return true;
        }
        if (Data.checkDate(survey.get("startDate"), true)) {
            System.out.format("\t \t ("+ Color.RED +"Inaccessible,"+ Color.RESET +" start date: " + survey.get("startDate") + " | by "
                    + survey.get("username") + ")\n");
            return true;
        }
        if (Data.checkDate(survey.get("endDate"), false)) {
            System.out.format("\t \t ("+ Color.RED +"Inaccessible,"+ Color.RESET +" end date: " + survey.get("endDate") + " | by "
                    + survey.get("username") + " | " + survey.get("timesTaken") + "x taken)\n");
            return true;
        }
        return false;
    }
    public static boolean takenAlready(UUID userID, String title){
        try {
            String id = userID.toString();
            JSONArray jsonObject = load("data/userStats.json");
            for(int i = 0; i <= jsonObject.size()-1; i++){
                JSONObject user = (JSONObject)jsonObject.get(i);
                if(user.get("id").toString().equals(id)){
                    JSONObject surveys = (JSONObject)user.get("takenSurveys");
                    if(surveys.containsValue(title))
                        return true;
                }
            }
        } catch (Exception e) {
            //TODO: handle exception
        }
        return false;
    }
    public static boolean ownSurvey(UUID userID, String nameToCheck){
        try {
            String id = userID.toString();
            JSONArray jsonObject = load("data/user.json");
            for(int i = 0; i <= jsonObject.size()-1; i++){
                JSONObject user = (JSONObject)jsonObject.get(i);
                if(user.get("id").toString().equals(id)){
                    String username = (String)user.get("username");
                    if(username.equals(nameToCheck))
                        return true;
                }
            }
        } catch (Exception e) {
            //TODO: handle exception
        }
        return false;
    }
    public static JSONObject getSurvey(int surveyNumber) {
        JSONArray jsonObject = null;
        try {
            jsonObject = load("data/survey.json");// read already existing data
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (JSONObject) jsonObject.get(surveyNumber);
    }
  
    private static void save(JSONArray object, String datatype){
        try (FileWriter file = new FileWriter(datatype)) {
            file.write(object.toJSONString());
            //System.out.println("Data were successfully saved");
        } catch (IOException e) {
            System.out.println("Error initializing stream  ");
        }
    }
   
    public static void saveSurveyData(String title, String author, String currentDate, String startDate, String endDate,
            ArrayList<Questions> questionArray) {
        try {
            JSONArray jsonObject = load("data/survey.json"); // read already existing data
            // String, Object statt String, String, weil es bei questions ein Array befindet
            Map<String, Object> surveyObject = new LinkedHashMap<String, Object>(); // Map, instead of JSONObject, so it
                                                                                    // will preserve order
            surveyObject.put("title", title);
            surveyObject.put("username", author);
            surveyObject.put("dateOfCreation", currentDate);
            surveyObject.put("startDate", startDate);
            surveyObject.put("endDate", endDate);
            surveyObject.put("timesTaken", 0);
            JSONArray questions = new JSONArray();
            for (Questions q : questionArray) {
                int x = 1;
                Map<String, Object> questionBlock = new LinkedHashMap<String, Object>();
                Map<String, String> answerBlock = new LinkedHashMap<String, String>();
                Map<String, Integer> statsBlock = new LinkedHashMap<String, Integer>();
                questionBlock.put("title", q.questionTitle);

                for (String answer : q.array) {
                    answerBlock.put(x + "_answer", answer);
                    statsBlock.put(x + "_answer", 0);
                    x++;
                }

                questionBlock.put("answers", answerBlock);
                questionBlock.put("stats", statsBlock);
                questions.add(questionBlock);
            }
            surveyObject.put("questions", questions);
            jsonObject.add(surveyObject);
            Data.save(jsonObject, "data/survey.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveUserData(UserRegistered user) {
        try {
            // read already existing data
            JSONArray jsonObject = load("data/user.json");
            // insert new data
            Map<String, Object> object = new LinkedHashMap<String, Object>();
            object.put("username", user.getUsername());
            object.put("password", user.getPassword());

            for(int i = 0; i <= jsonObject.size()-1; i++){
                JSONObject u = (JSONObject)jsonObject.get(i);
                if(u.get("id").toString().equals(user.getUuid().toString())){
                    user.randomUUID();
                    i = 0;
                }
            }

            object.put("id", user.getUuid().toString());
            // save data
            jsonObject.add(object);
            Data.save(jsonObject, "data/user.json");
        } catch (Exception e) {
            // create new user.json? maybe???
            e.printStackTrace();
        }
    }

    public static void saveUserDataForStats(User user){
        try {
            JSONArray jsonObject = load("data/userStats.json");
            // insert new data
            // JSONObject object = new JSONObject();
            Map<String, Object> object = new LinkedHashMap<String, Object>();
            object.put("id", user.getUuid().toString());
            object.put("numberOfTakenSurvey", 0);
            Map<String, Integer> titleblock = new LinkedHashMap<String, Integer>();
            object.put("takenSurveys", titleblock);
            jsonObject.add(object);
            Data.save(jsonObject, "data/userStats.json");
        } catch (Exception e) {
            //TODO: handle exception
        }
    }
    
    public static void saveSurveyStats(int surveyNumber, ArrayList<Integer> newStats) {
        try {
            JSONArray jsonObject = load("data/survey.json");
            JSONObject surveyStats = (JSONObject) jsonObject.get(surveyNumber);
            int timesTaken = Integer.parseInt(surveyStats.get("timesTaken").toString());
            surveyStats.replace("timesTaken", timesTaken + 1);
            //System.out.println(jsonObject);
            JSONArray allQuestions = (JSONArray) surveyStats.get("questions");

            for (int i = 0; i <= allQuestions.size() - 1; i++) {
                JSONObject question = (JSONObject) allQuestions.get(i);
                JSONObject stats = (JSONObject) question.get("stats");
                int x = Integer.parseInt(stats.get(newStats.get(i) + "_answer").toString()) + 1;
                stats.replace(newStats.get(i) + "_answer", x);
            }
            Data.save(jsonObject, "data/survey.json");
            // SAVE SURVEY STATS
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Integer> showOwnSurveys(String username) {
        ArrayList<Integer> mySurveys = new ArrayList<Integer>();
        try {
            System.out.println("Your Surveys:");
            JSONArray jsonObject = load("data/surveyStats.json");
            int surveyNumbers = jsonObject.size();

            int x = 0;
            for (int i = 0; i <= surveyNumbers - 1; i++) {
                JSONObject survey = (JSONObject) jsonObject.get(i);
                if (survey.get("username").toString().equals(username) && Integer.parseInt(survey.get("timesTaken").toString()) != 0) {
                    x++;
                    System.out.println(x + ") " + survey.get("title"));
                    mySurveys.add(i);
                }
            }
            if (x == 0)
                System.out.println("You don't have any Surveys yet or they weren't taken yet");
        } catch (Exception e) {
            // TODO: handle exception
        }
        return mySurveys;
    }

    public static void showSurveyStats(int choosenSurvey) {
        try {
            System.out.println("Stats of Survey:");
            JSONArray jsonObject = load("data/survey.json");
            JSONObject survey = (JSONObject) jsonObject.get(choosenSurvey);
            int timesTaken = Integer.parseInt(survey.get("timesTaken").toString());
            JSONArray allQuestions = (JSONArray) survey.get("questions");
            for (int i = 0; i <= allQuestions.size() - 1; i++) {
                JSONObject question = (JSONObject) allQuestions.get(i);
                JSONObject stats = (JSONObject) question.get("stats");
                JSONObject answer = (JSONObject) question.get("answers");
                System.out.println("\n" + question.get("title"));
                for (int a = 0; a <= answer.size() - 1; a++) {
                    double y = 0;
                    y = Double.parseDouble(stats.get(a + 1 + "_answer").toString());
                    y = (y / timesTaken) * 100;
                    System.out.format(answer.get(a + 1 + "_answer") + "\t\t (" + y + "%%) \n"); // double %% to
                                                                                                // visualize a % symbol
                                                                                                // in print
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
   
    public static void showUserStats(UUID uuid){
        try {
            JSONArray jsonObject = load("data/userStats.json");
            for(int i = 0; i <= jsonObject.size(); i++){
                JSONObject user = (JSONObject)jsonObject.get(i);
                String id = user.get("id").toString();
                if(uuid.toString().equals(id)){
                    JSONObject x = (JSONObject)user.get("takenSurveys");
                    if(x.size() == 0){
                        System.out.println("You haven't taken any surveys yet.");
                        break;
                    }
                    for(int u = 0; u <= x.size()-1; u++){
                        System.out.println(x.get(u+1+"_title"));
                    }
                }
            }
        } catch (Exception e) {
        }
    }
    
    public static boolean checkDate(Object dateToCheck, boolean endOrStart) {
        try {
            Calendar calendar = new GregorianCalendar();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            Date currentDate = calendar.getTime();

            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
            Date date = formatter.parse(dateToCheck.toString());
            if (currentDate.before(date) && endOrStart == true)
                return true;
            if (currentDate.after(date) && endOrStart == false)
                return true;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return false;
    }
    
    public static UUID getUuid(String username){
        try {
            JSONArray jsonObject = load("data/user.json");
            for(int i = 0; i <= jsonObject.size(); i++){
                JSONObject user = (JSONObject)jsonObject.get(i);
                if(user.get("username").toString().equals(username))
                    return UUID.fromString(user.get("id").toString());
            }
        } catch (Exception e) {
            //TODO: handle exception
        }
        return null;
    }
    
    public static void saveUserStats(int surveyNumber, UUID id){
        try {
            String userID = id.toString();
            JSONArray jsonObject = load("data/userStats.json");
            JSONObject user = null;
            for(int i = 0; i <= jsonObject.size()-1; i++){
                user = (JSONObject)jsonObject.get(i);
                if(user.get("id").toString().equals(userID))
                    break;
            }
            int timesTaken = Integer.parseInt(user.get("numberOfTakenSurvey").toString());
            user.replace("numberOfTakenSurvey", timesTaken + 1);
            JSONObject surveys = (JSONObject) user.get("takenSurveys");
            int size = surveys.size();
            String nameOfSurvey = Data.getSurveyTitle(surveyNumber);
            surveys.put(size+1+"_title", nameOfSurvey);
            Data.save(jsonObject, "data/userStats.json");
            // SAVE SURVEY STATS
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static String getSurveyTitle(int surveyNumber){
        try {
            JSONArray jsonObject = load("data/survey.json");
            JSONObject survey = (JSONObject) jsonObject.get(surveyNumber);
            return survey.get("title").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
