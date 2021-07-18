import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;

public class Data {

    public static JSONArray loadJSON(String _dataType) {
        try {
            // read already existing JSON data
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(_dataType));
            JSONArray jsonArray = (JSONArray) obj;
            return jsonArray;
        } catch (Exception e) { // if file doesn't exist
            try {
                File file = new File(_dataType);
                file.createNewFile();
                FileWriter writer = new FileWriter(file);
                writer.append("[]");
                writer.close();
                return loadJSON(_dataType);
            } catch (IOException x) {
                x.printStackTrace();
                System.exit(1);
            }
        }
        return null;
    }
    // return -1 if you want to search again, return -2 if u wanna go back to menu.
    // searches survey by title
    public static int searchSurvey(String _titleSearch, UUID _userID) {
        // Search
        System.out.println("__________________________________\n");
        System.out.println("Type in one of the numbers!");
        Scanner sc = new Scanner(System.in);
        try {
            JSONArray jsonArray = loadJSON("data/survey.json");
            int numberOfMatch = 0;
            ArrayList<Integer> titleThatMatched = new ArrayList<Integer>();
            ArrayList<Integer> inaccesible = new ArrayList<Integer>();
            for (int i = 0; i <= jsonArray.size() - 1; i++) {
                JSONObject survey = (JSONObject) jsonArray.get(i);
                String titleToMatch = (String) survey.get("title");
                if (titleToMatch.toLowerCase().startsWith(_titleSearch.toLowerCase())) {
                    if (numberOfMatch == 0)
                        System.out.println("Choose if you want to play");
                    numberOfMatch++;
                    System.out.println(numberOfMatch + ") " + survey.get("title"));
                    titleThatMatched.add(i);
                    if (Data.isInaccesible(_userID, survey))
                        inaccesible.add(numberOfMatch);
                    else
                        System.out.format("\t \t ( Avaible from " + survey.get("startDate") + " - "
                                + survey.get("endDate") + " | by " + survey.get("username") + " | "
                                + survey.get("timesTaken") + "x taken)\n");
                }
                if (numberOfMatch != 0 && i == jsonArray.size() - 1) {
                    System.out.println(numberOfMatch + 1 + ") Search again");
                    System.out.println(numberOfMatch + 2 + ") Go back to menu");
                }
            }
            if (numberOfMatch == 0) {
                System.out.println("No Survey found. Do you wanna search again?");
                System.out.println("1) Search Again");
                System.out.println("2) Go back to menu");
                int search = sc.nextInt();
                if (search == 1)
                    return -1;
                else
                    return -2;
            } else {
                int search = sc.nextInt();
                while (inaccesible.contains(search)) {
                    System.out.println("It's inaccessible. Choose another one");
                    search = sc.nextInt();
                }
                if (search == numberOfMatch + 1)
                    return -1;
                if (search == numberOfMatch + 2)
                    return -2;
                return titleThatMatched.get(search-1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -2;
        }
    }
    // shows latest (up to) 10 surveys
    public static Integer chooseSurveyFromData(UUID _userID) {
        System.out.println("__________________________________\n");
        System.out.println("Type in one of the numbers!");
        Scanner sc = new Scanner(System.in);
        int choose = -1;
        try {
            JSONArray jsonArray = loadJSON("data/survey.json");
            int surveyNumbers = jsonArray.size();
            int difference = 0;
            ArrayList<Integer> inaccesible = new ArrayList<Integer>(); 
            if (jsonArray.size() > 9){ // show latest 10 or all, if there are less than 10
                difference = surveyNumbers - 10;
                surveyNumbers = 10;
            }

            for (int i = 0; i <= surveyNumbers - 1; i++) {
                JSONObject survey = (JSONObject) jsonArray.get(jsonArray.size() - 1 - i);
                System.out.println(i + 1 + ") " + survey.get("title"));

                if (Data.isInaccesible(_userID, survey))// Check if it is accessible
                    inaccesible.add(i + 1);
                else
                    System.out.format("\t \t (Avaible from " + survey.get("startDate") + " - " + survey.get("endDate")
                            + " | by " + survey.get("username") + " | " + survey.get("timesTaken") + "x taken)\n");
            }
            System.out.println(surveyNumbers + 1 + ") Back to menu");
            choose = sc.nextInt();
            if (choose == surveyNumbers + 1)
                return null;
            while (inaccesible.contains(choose)) {
                System.out.println("It's inaccessible. Choose another one");
                choose = sc.nextInt();
            }
            /* if(surveyNumbers == 10)  */
                choose = surveyNumbers + difference - choose;
            /* else
                choose = surveyNumbers - choose; */
            } catch (Exception e) {
            e.printStackTrace();
        }
        return choose;
    }

    public static boolean isOwnSurvey(UUID _userID, String _nameToCheck) {
        try {
            String id = _userID.toString();
            JSONArray jsonArray = loadJSON("data/user.json");
            for (int i = 0; i <= jsonArray.size() - 1; i++) {
                JSONObject user = (JSONObject) jsonArray.get(i);
                if (user.get("id").toString().equals(id)) {
                    String username = (String) user.get("username");
                    if (username.equals(_nameToCheck))
                        return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static JSONObject getSurvey(int _surveyNumber) {
        JSONArray jsonObject = null;
        try {
            jsonObject = loadJSON("data/survey.json");// read already existing data
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (JSONObject) jsonObject.get(_surveyNumber);
    }

    public static void saveSurveyData(Survey _survey, ArrayList<Question> _questionArray) {
        try {
            JSONArray jsonArray = loadJSON("data/survey.json"); // read already existing data
            Map<String, Object> surveyObject = new LinkedHashMap<String, Object>(); // Map, instead of JSONObject, so it
                                                                                    // should preserve order
            surveyObject.put("title", _survey.title);
            surveyObject.put("username", _survey.creator);
            surveyObject.put("dateOfCreation", _survey.currentDate.toString());
            surveyObject.put("startDate", _survey.startDate);
            surveyObject.put("endDate", _survey.endDate);
            surveyObject.put("timesTaken", 0);
            JSONArray questions = new JSONArray();
            for (Question q : _questionArray) {
                int counter = 1;
                Map<String, Object> questionBlock = new LinkedHashMap<String, Object>();
                Map<String, String> answerBlock = new LinkedHashMap<String, String>();
                Map<String, Integer> statsBlock = new LinkedHashMap<String, Integer>();
                questionBlock.put("title", q.questionTitle);

                for (String answer : q.array) {
                    answerBlock.put(counter + "_answer", answer);
                    statsBlock.put(counter + "_answer", 0);
                    counter++;
                }

                questionBlock.put("answers", answerBlock);
                questionBlock.put("stats", statsBlock);
                questions.add(questionBlock);
            }
            surveyObject.put("questions", questions);
            jsonArray.add(surveyObject);
            Data.saveToJSON(jsonArray, "data/survey.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveUserData(UserRegistered _user) {
        try {
            // read already existing data
            JSONArray jsonObject = loadJSON("data/user.json");
            // insert new data
            Map<String, Object> object = new LinkedHashMap<String, Object>();
            object.put("username", _user.getUsername());
            object.put("password", _user.getPassword());
            object.put("id", _user.getUuid().toString());
            // save data
            jsonObject.add(object);
            Data.saveToJSON(jsonObject, "data/user.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveUserDataForStats(User _user) { // save date so we can update it when they took a survey
        try {
            JSONArray jsonArray = loadJSON("data/userStats.json");
            Map<String, Object> object = new LinkedHashMap<String, Object>();
            while (!checkIfUuidIsUnique(_user.getUuid(), jsonArray)) {
                _user.setRandomUuid();
            }
            object.put("id", _user.getUuid().toString());
            object.put("numberOfTakenSurvey", 0);
            Map<String, Integer> titleblock = new LinkedHashMap<String, Integer>();
            object.put("takenSurveys", titleblock);
            jsonArray.add(object);
            Data.saveToJSON(jsonArray, "data/userStats.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveSurveyStats(int _surveyNumber, ArrayList<Integer> _newStats) {
        try {
            JSONArray jsonArray = loadJSON("data/survey.json");
            JSONObject surveyStats = (JSONObject) jsonArray.get(_surveyNumber);
            int timesTaken = Integer.parseInt(surveyStats.get("timesTaken").toString());
            surveyStats.replace("timesTaken", timesTaken + 1);
            JSONArray allQuestions = (JSONArray) surveyStats.get("questions");

            for (int i = 0; i <= allQuestions.size() - 1; i++) {
                JSONObject question = (JSONObject) allQuestions.get(i);
                JSONObject stats = (JSONObject) question.get("stats");
                int statOfAnswer = Integer.parseInt(stats.get(_newStats.get(i) + "_answer").toString()) + 1;
                stats.replace(_newStats.get(i) + "_answer", statOfAnswer);
            }
            Data.saveToJSON(jsonArray, "data/survey.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Integer> getAndShowOwnSurveys(String _username) {
        ArrayList<Integer> mySurveys = new ArrayList<Integer>();
        try {
            System.out.println("Your Surveys:");
            JSONArray jsonArray = loadJSON("data/survey.json");
            int surveyNumbers = jsonArray.size();

            int numberOfmatches = 0;
            for (int i = 0; i <= surveyNumbers - 1; i++) {
                JSONObject survey = (JSONObject) jsonArray.get(i);
                int timesTaken = Integer.parseInt(survey.get("timesTaken").toString());
                if (survey.get("username").toString().equals(_username)) {
                    numberOfmatches++;
                    System.out
                            .println(numberOfmatches + ") " + survey.get("title") + " | " + timesTaken + " times taken");
                    mySurveys.add(i);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mySurveys;
    }

    public static void showSurveyStats(int _choosenSurvey) {
        try {
            System.out.println("__________________________________\n");
            System.out.println("Stats of Survey:");
            JSONArray jsonArray = loadJSON("data/survey.json");
            JSONObject survey = (JSONObject) jsonArray.get(_choosenSurvey);
            int timesTaken = Integer.parseInt(survey.get("timesTaken").toString());
            if (timesTaken == 0) {
                System.out.println("This Survey hasn't been taken yet, and doesn't have any stats");
                return;
            }
            JSONArray allQuestions = (JSONArray) survey.get("questions");
            for (int i = 0; i <= allQuestions.size() - 1; i++) {
                JSONObject question = (JSONObject) allQuestions.get(i);
                JSONObject stats = (JSONObject) question.get("stats");
                JSONObject answer = (JSONObject) question.get("answers");
                System.out.println("\n" + question.get("title"));
                for (int a = 0; a <= answer.size() - 1; a++) {
                    double percentage = 0;
                    percentage = Double.parseDouble(stats.get(a + 1 + "_answer").toString());
                    percentage = Math.round((percentage / timesTaken) * 100);
                    System.out.format(answer.get(a + 1 + "_answer") + "\t\t\t (" + percentage + "%%) \n");
                    // double %% to visualize a % symbol in print
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showUserStats(UUID _uuid) {
        try {
            System.out.println("__________________________________\n");
            JSONArray jsonArray = loadJSON("data/userStats.json");
            for (int i = 0; i <= jsonArray.size(); i++) {
                JSONObject user = (JSONObject) jsonArray.get(i);
                String id = user.get("id").toString();
                if (_uuid.toString().equals(id)) {
                    JSONObject x = (JSONObject) user.get("takenSurveys");
                    if (x.size() == 0) {
                        System.out.println("You haven't taken any surveys yet.");
                        break;
                    }
                    if (x.size() == 1)
                        System.out.println("So far you took 1 Survey:");
                    if (x.size() > 1)
                        System.out.println("You took following " + x.size() + " Surveys:");
                    for (int u = 0; u <= x.size() - 1; u++) {
                        System.out.println(x.get(u + 1 + "_title"));
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    public static boolean checkDate(Object _dateToCheck, boolean _endOrStart) {
        try {
            Calendar calendar = Calendar.getInstance();
            Date currentDate = calendar.getTime();

            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
            Date date = formatter.parse(_dateToCheck.toString());
            if (currentDate.before(date) && _endOrStart == true)
                return true;
            if (currentDate.after(date) && _endOrStart == false)
                return true;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static UUID getUuidFromData(String _username) {
        try {
            JSONArray jsonArray = loadJSON("data/user.json");
            for (int i = 0; i <= jsonArray.size(); i++) {
                JSONObject user = (JSONObject) jsonArray.get(i);
                if (user.get("username").toString().equals(_username))
                    return UUID.fromString(user.get("id").toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void saveUserStats(int _surveyNumber, UUID _id) {
        try {
            String userID = _id.toString();
            JSONArray jsonArray = loadJSON("data/userStats.json");
            JSONObject user = null;
            for (int i = 0; i <= jsonArray.size() - 1; i++) {
                user = (JSONObject) jsonArray.get(i);
                if (user.get("id").toString().equals(userID))
                    break;
            }
            int timesTaken = Integer.parseInt(user.get("numberOfTakenSurvey").toString());
            user.replace("numberOfTakenSurvey", timesTaken + 1);
            JSONObject surveys = (JSONObject) user.get("takenSurveys");
            int size = surveys.size();
            String nameOfSurvey = Data.getSurveyTitle(_surveyNumber);
            surveys.put(size + 1 + "_title", nameOfSurvey);
            Data.saveToJSON(jsonArray, "data/userStats.json");
            // SAVE SURVEY STATS
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getSurveyTitle(int _surveyNumber) {
        try {
            JSONArray jsonObject = loadJSON("data/survey.json");
            JSONObject survey = (JSONObject) jsonObject.get(_surveyNumber);
            return survey.get("title").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private static void saveToJSON(JSONArray object, String datatype) {
        try (FileWriter file = new FileWriter(datatype)) {
            file.write(object.toJSONString());
        } catch (IOException e) {
            System.out.println("Error initializing stream  ");
        }
    }
    
    private static boolean checkIfUuidIsUnique(UUID _id, JSONArray _jsonArray) {
        for (int i = 0; i <= _jsonArray.size() - 1; i++) {
            JSONObject u = (JSONObject) _jsonArray.get(i);
            if (u.get("id").toString().equals(_id.toString()))
                return false;
        }
        return true;
    }
    
    private static boolean isSurveyAlreadyTaken(UUID _userID, String _title) {
        try {
            String id = _userID.toString();
            JSONArray jsonArray = loadJSON("data/userStats.json");
            for (int i = 0; i <= jsonArray.size() - 1; i++) {
                JSONObject user = (JSONObject) jsonArray.get(i);
                if (user.get("id").toString().equals(id)) {
                    JSONObject surveys = (JSONObject) user.get("takenSurveys");
                    if (surveys.containsValue(_title))
                        return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private static boolean isInaccesible(UUID _userID, JSONObject _survey) {
        if (Data.isOwnSurvey(_userID, _survey.get("username").toString())) {
            System.out.format("\t \t (Inaccessible, You created this survey)\n");
            return true;
        }
        if (Data.isSurveyAlreadyTaken(_userID, _survey.get("title").toString())) {
            System.out.format("\t \t (Inaccessible, You took it already | by " + _survey.get("username") + ")\n");
            return true;
        }
        if (Data.checkDate(_survey.get("startDate"), true)) {
            System.out.format("\t \t (Inaccessible, start date: " + _survey.get("startDate") + " | by "
                    + _survey.get("username") + ")\n");
            return true;
        }
        if (Data.checkDate(_survey.get("endDate"), false)) {
            System.out.format("\t \t (Inaccessible, end date: " + _survey.get("endDate") + " | by "
                    + _survey.get("username") + " | " + _survey.get("timesTaken") + "x taken)\n");
            return true;
        }
        return false;
    }
}