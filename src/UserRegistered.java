public class UserRegistered implements User{

    public int userID = 1;
    private String username;
    private String password;
    public void takeSurvey(){};

    public void showStats(int _userID){};

    public void searchSurveys(){};

    public void chooseSurvey(){};

    public void createSurvey(){}
    public void seeSurveyStats(){}
    public void setUsername(String username){
        this.username = username; 
    }
    public void setPassword(String password){
        this.password = password;
    }
    public String getUsername(){
        return this.username;
    }
    public String getPassword(){
        return this.password;
    }

}
