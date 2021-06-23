public class UserFactory {
    public void getUser(int _userType){                
        SecurityService security = SecurityService.getInstance();
        switch(_userType){
            case 0:
                UserRegistered alreadyRegisteredUser = new UserRegistered();
                security.logIn();
            case 1:
                UserRegistered newUser = new UserRegistered();
                security.signUp();
            case 2:
                GuestUser guestUser = new GuestUser();
        }
    }
}
