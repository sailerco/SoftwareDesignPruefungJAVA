import java.util.Random;

public class GuestUser implements User{
    public int userID;

    public void takeSurvey(){};

    public void showStats(int _userID){};

    public void searchSurveys(){};

    public void chooseSurvey(){};

    public void setUserID(){
        this.userID = 1;
    }
}
