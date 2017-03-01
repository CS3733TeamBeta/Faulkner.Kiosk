package main.Directory;

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

    public boolean changeUsername(String newUsername){
        this.username = newUsername;
        return true;
    }

    public boolean changePassword(String newPassword){
        this.password = newPassword;
        return true;
    }
}
