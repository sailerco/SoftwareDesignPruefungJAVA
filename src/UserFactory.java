public class UserFactory {
    public User getUser(int _userType){                
        SecurityService security = SecurityService.getInstance();
        switch(_userType){
            case 0:
                System.out.println("Log In");
                String[] userdata = security.logIn();
                if(userdata != null){
                    UserRegistered alreadyRegisteredUser = new UserRegistered();
                    alreadyRegisteredUser.setUsername(userdata[0]);
                    alreadyRegisteredUser.setPassword(userdata[1]);
                    return alreadyRegisteredUser;
                }             
            case 1:
                System.out.println("Type in a new username and an alphanumeric password");
                UserRegistered newUser = new UserRegistered();
                String[] user = security.signUp();
                System.out.println(user[1]);
                newUser.setUsername(user[0]);
                newUser.setPassword(user[1]);
                Data.saveUserData(newUser);
                return newUser;
            case 2:
                GuestUser guestUser = new GuestUser();
                guestUser.setUserID();
                return guestUser;
        }
        return null;
    }
}
