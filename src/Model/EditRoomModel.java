package Model;

/**
 * Created by benhylak on 2/4/17.
 */
public class EditRoomModel {
    private String txtUsername;
    public EditRoomModel(String u)
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
