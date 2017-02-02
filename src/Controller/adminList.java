package Controller;

import java.util.HashMap;

/**
 * Created by Pattop on 2/1/2017.
 */
public class adminList {
    HashMap<String, adminProfile> listOfAdmins = new HashMap<String, adminProfile>();

    Boolean devEnabled = false;

    public adminList(){
        listOfAdmins.clear();
    }

    public int addAdmin(String username, String password){
        System.out.println("Adding admin with the following credentials: " + username + " " + password);
        adminProfile newProfile = new adminProfile(username, password);
        System.out.println("Adding admin with the following credentials: " + newProfile.username + " " + newProfile.password);
        listOfAdmins.put(username, newProfile);
        return 0;
    }

    public int changeUsername(String oldUsername, String newUsername){
        if (listOfAdmins.containsKey(oldUsername)){
            //get the info associated with the old username
            adminProfile oldProfileInfo = listOfAdmins.get(oldUsername);
            //set the username equal to the new one
            oldProfileInfo.username = newUsername;
            listOfAdmins.put(newUsername, oldProfileInfo);
            //remove the old reference
            listOfAdmins.remove(oldUsername);
        }
        else{
            System.out.println("ERROR. TRYING TO CHANGE ADMIN USERNAME FOR PROFILE THAT DOES NOT EXIST");
            return 1;
        }
        return 0;
    }

    public int changePassword(String username, String oldPassword, String newPassword){
        if (listOfAdmins.containsKey(username)){
            //get the info associated with the old username
            adminProfile profileInfo = listOfAdmins.get(username);
            //validate the change
            if(profileInfo.getPassword().equals(oldPassword)){
                //update the struct with the new password
                profileInfo.password = newPassword;
            }
            else{
                System.out.println("ERROR. ADMIN TRIED TO CHANGE PASSWORD WITH INCORRECT VALIDATION");
                return 1;
            }
        }
        else{
            System.out.println("ERROR. TRYING TO CHANGE ADMIN PASSWORD FOR PROFILE THAT DOES NOT EXIST");
            return 1;
        }
        return 0;
    }

    public Boolean checkValidity(String username, String password){

        //here now to allow for easy debugging
        //TODO REMOVE THIS FROM THE RELEASE VERSION
        if(devEnabled){
            return true;
        }

        if (listOfAdmins.containsKey(username)){
            //check if the passwords match
            if(listOfAdmins.get(username).getPassword().equals(password)){
                return true;
            }
        }
        else{
            return false;
        }
        return false;
    }

}
