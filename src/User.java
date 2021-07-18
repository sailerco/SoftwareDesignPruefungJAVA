import java.util.UUID;

public interface User {

    public void searchSurvey();

    public void chooseSurvey();

    public void showStats();

    public UUID getUuid();

    public void setRandomUuid();
}
