package Model;

/**
 * Created by Lukezebrowski on 2/4/17.
 */
public class ChangingDirectoryModel {
    private String txtUsername;
    // constructor for AdminLoginModel
    public ChangingDirectoryModel(String u)
    {
        txtUsername = u;
    }
    // Recieves username
    public String getTxtUsername() {
        return txtUsername;
    }
    // Sets username
    public void setTxtUsername(String txtUsername) {
        this.txtUsername = txtUsername;
    }
}
