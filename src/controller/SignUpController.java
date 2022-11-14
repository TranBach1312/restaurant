package controller;

import entity.Staff;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import repository.StaffRepository;

import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {

    @FXML
    private AnchorPane loginPane;

    @FXML
    private ImageView logoImageView;

    @FXML
    private PasswordField password;

    @FXML
    private PasswordField rePassword;

    @FXML
    private Button signUpButton;

    @FXML
    private TextField username;

    private Staff thisStaff;

    public void setThisStaff(Staff thisStaff) {
        this.thisStaff = thisStaff;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image image = new Image("resources/logo.jpg");
        logoImageView.setImage(image);
        signUpButton.setDisable(true);
        rePassword.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals(password.getText())) {
                signUpButton.setDisable(false);
            } else {
                signUpButton.setDisable(true);
            }
        });
        rePassword.setOnKeyPressed(event -> {
            if(password.getText().equals(rePassword.getText())){
                if (event.getCode().equals(KeyCode.ENTER)) {
                    checkSignUp();
                }
            }
        });
        signUpButton.setOnAction(event -> {
            if(checkSignUp()){
                ((Stage) loginPane.getScene().getWindow()).close();
            }
        });
    }

    public boolean checkSignUp(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sign Up Notification");
        Boolean flag = true;
        if(username.getText().trim().isEmpty()){
            alert.setContentText("Enter username!!");
            return false;
        }
        if(StaffRepository.getUser(username.getText())){
            alert.setContentText("This username is already in use");
            return false;
        }
        else{
            Staff staff = new Staff();
            staff.setId(thisStaff.getId());
            if(StaffRepository.addUser(username.getText(), password.getText(), staff)){
                alert.setContentText("Sign Up Successful!!");
                flag = true;
            }
            else {
                alert.setContentText("Error!!");
                flag = false;
            }
        }
        alert.showAndWait();
        return flag;
    }
}
