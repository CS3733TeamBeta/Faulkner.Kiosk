package Controller;

/**
 * Created by Pattop on 2/1/2017.
 */

//TODO should be visible only to adminList class in final version

public class adminProfile {
    String username;
    String password;
    //maybe add a last logged in time?
    //maybe add a history of some kind

    public adminProfile(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername(){
        return this.username;
    }

    public String getPassword(){
        return this.password;
    }

    public int changeUsername(String newUsername){
        this.username = newUsername;
        return 0;
    }

    public int changePassword(String newPassword){
        this.password = newPassword;
        return 0;
    }
}
