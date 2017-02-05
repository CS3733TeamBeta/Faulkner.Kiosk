package Controller;

/**
 * Created by Pattop on 2/1/2017.
 */

//TODO should be visible only to AdminList class in final version

public class AdminProfile
{
    String username;
    String password;
    //maybe add a last logged in time?
    //maybe add a history of some kind

    public AdminProfile(String username, String password){
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
