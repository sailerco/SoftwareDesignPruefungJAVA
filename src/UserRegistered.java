import java.util.*;

public class UserRegistered implements User {

    private UUID uuid;
    private String username;
    private String password;

    public UserRegistered(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void createSurvey() {
        Survey survey = new Survey();
        survey.creator = this.username;
        survey.initializeSurvey();
    }

    public void searchSurvey() {
        Survey searchSurvey = new Survey();
        searchSurvey.search(this.uuid);
    }

    public void chooseSurvey() {
        Survey existingSurvey = new Survey();
        Integer choosen = Data.chooseSurveyFromData(this.uuid);
        if (choosen != null)
            existingSurvey.takeSurvey(choosen, this.uuid);
    }

    public void showStats() {
        Data.showUserStats(this.getUuid());
    }

    public void seeSurveyStats() {
        ArrayList<Integer> mySurveys = Data.getAndShowOwnSurveys(this.username);
        if (!mySurveys.isEmpty()) {
            Scanner sc = new Scanner(System.in);
            int select = sc.nextInt();
            Data.showSurveyStats(mySurveys.get(select - 1));
        }
    }

    public void setUuid(UUID _id) {
        this.uuid = _id;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public UUID getUuid() {
        if (this.uuid == null)
            this.setRandomUuid();
        return this.uuid;
    }

    public void setRandomUuid() {
        this.uuid = UUID.randomUUID();
    }
}
