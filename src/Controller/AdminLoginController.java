package Controller;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class AdminLoginController
{
    @FXML
    Button btnBack;
    @FXML
    Button btnLogin;
    @FXML
    TextField txtUsername;
    @FXML
    PasswordField txtPassword;

    @FXML
    private void clickedLogin()
    {
        Main.thisStage.setScene(Main.adminWelcome);
    }

    @FXML
    private void clickedBack()
    {

    }
}
