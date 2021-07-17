import java.util.*;

public class UserRegistered implements User{

    private UUID uuid;
    private String username;
    private String password;

    public UserRegistered(String username, String password){
        this.username = username;
        this.password = password;
    }
    public void showStats(){
        System.out.println(this.getUuid());
        Data.showUserStats(this.getUuid());
    }
    public void seeSurveyStats(){
        ArrayList<Integer> mySurveys = Data.getAndShowOwnSurveys(this.username);
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
    public void setUuid(UUID id){
        this.uuid = id;
    }
    
    public String getUsername(){
        return this.username;
    }
    public String getPassword(){
        return this.password;
    }
    public UUID getUuid(){
        if(this.uuid == null)
            this.randomUUID();
        return this.uuid;
    }
    
    public void randomUUID(){
        this.uuid = UUID.randomUUID();
    }
}
