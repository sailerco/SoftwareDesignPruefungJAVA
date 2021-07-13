import java.util.*;

public class UserRegistered implements User{

    public UUID uuid;
    private String username;
    private String password;


    public void showStats(int _userID){};


    public void createSurvey(){}
    public void showStats(){
        System.out.println(this.getUuid());
        Data.showUserStats(this.getUuid());
    }
    public void seeSurveyStats(){
        ArrayList<Integer> mySurveys = Data.showOwnSurveys(this.username);
        if(!mySurveys.isEmpty()){
            Scanner sc = new Scanner(System.in);
            int select = sc.nextInt();
            Data.showSurveyStats(mySurveys.get(select-1));
        }
    }
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
    public UUID getUuid(){
        return this.uuid;
    }
    public void setUuid(UUID id){
        this.uuid = id;
    }
    public void randomUUID(){
        this.uuid = UUID.randomUUID();
    }
}
