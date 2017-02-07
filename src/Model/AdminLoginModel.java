package Model;

import Controller.Admin.AdminList;

/**
 * Created by Lukezebrowski on 2/4/17.
 */
public class AdminLoginModel
{
    public static AdminList admins;    // For testing purposes
    // constructor for AdminLoginModel
    public AdminLoginModel()
    {
        admins = new AdminList();

        admins.addAdmin("ADMIN", "ADMIN");
        //admins.setDevEnabled(true);
    }


}
