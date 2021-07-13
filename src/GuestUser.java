import java.util.UUID;

public class GuestUser implements User{
    public UUID uuid = UUID.randomUUID();

    public void showStats(int _userID){};

    public void showStats(){
        System.out.println(this.getUuid());
        Data.showUserStats(this.getUuid());
    }
    
    public UUID getUuid(){
        return this.uuid;
    }
    public void setUuid(UUID id){
        this.uuid = UUID.randomUUID();
    }
    public void randomUUID(){
        this.uuid = UUID.randomUUID();
    }
}
