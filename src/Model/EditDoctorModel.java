package Model;

/**
 * Created by Lukezebrowski on 2/4/17.
 */
public class EditDoctorModel
{
    private String txtUsername;
    public EditDoctorModel(String u)
    {
        txtUsername = u;
    }
    // get username
    public String getTxtUsername() {
        return txtUsername;
    }
    // set username
    public void setTxtUsername(String txtUsername) {
        this.txtUsername = txtUsername;
    }
}
