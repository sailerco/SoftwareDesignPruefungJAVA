import java.util.UUID;

public class UserFactory {
    public User getUser(int _userType, UUID _id) {
        SecurityService security = SecurityService.getInstance();
        switch (_userType) {
            case 0:
                System.out.println("Log In");
                String[] userdata = security.logIn();
                if (userdata != null) {
                    UserRegistered alreadyRegisteredUser = new UserRegistered(userdata[0], userdata[1]);
                    alreadyRegisteredUser.setUuid(Data.getUuidFromData(userdata[0]));
                    return alreadyRegisteredUser;
                }
            case 1:
                System.out.println("Type in a new alphanumeric username and a password");
                String[] user = security.signUp();
                UserRegistered newUser = new UserRegistered(user[0], user[1]);
                Data.saveUserDataForStats(newUser);
                Data.saveUserData(newUser);
                return newUser;
            case 2:
                GuestUser guestUser = new GuestUser();
                guestUser.setRandomUuid();
                Data.saveUserDataForStats(guestUser);
                return guestUser;
            case 3: // guest User decided to signUp
                String[] formerGuest = security.signUp();
                UserRegistered guestToRegistered = new UserRegistered(formerGuest[0], formerGuest[1]);
                guestToRegistered.setUuid(_id);
                Data.saveUserData(guestToRegistered);
                return guestToRegistered;
        }
        return null;
    }
}
