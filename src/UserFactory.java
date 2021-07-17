public class UserFactory {
    public User getUser(int userType){                
        SecurityService security = SecurityService.getInstance();
        switch(userType){
            case 0:
                System.out.println("Log In");
                String[] userdata = security.logIn();
                if(userdata != null){
                    UserRegistered alreadyRegisteredUser = new UserRegistered(userdata[0], userdata[1]);
                    alreadyRegisteredUser.setUuid(Data.getUuid(userdata[0]));
                    return alreadyRegisteredUser;
                }             
            case 1:
                System.out.println("Type in a new username and an alphanumeric password");
                String[] user = security.signUp();
                UserRegistered newUser = new UserRegistered(user[0], user[1]);
                Data.saveUserData(newUser);
                Data.saveUserDataForStats(newUser);
                return newUser;
            case 2:
                GuestUser guestUser = new GuestUser();
                guestUser.randomUUID();
                Data.saveUserDataForStats(guestUser);
                return guestUser;
        }
        return null;
    }
}
