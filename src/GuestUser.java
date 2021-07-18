import java.util.UUID;

public class GuestUser implements User {
    private UUID uuid = UUID.randomUUID();

    public void searchSurvey() {
        Survey searchSurvey = new Survey();
        searchSurvey.search(this.uuid);
    }

    public void chooseSurvey() {
        Survey existingSurvey = new Survey();
        existingSurvey.takeSurvey(Data.chooseSurveyFromData(this.uuid), this.uuid);
    }

    public void showStats() {
        Data.showUserStats(this.getUuid());
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public void setRandomUuid() {
        this.uuid = UUID.randomUUID();
    }
}
