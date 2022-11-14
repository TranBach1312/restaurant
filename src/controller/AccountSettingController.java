package controller;

import entity.StaffPosition;
import entity.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import repository.StaffRepository;

import java.net.URL;
import java.util.ResourceBundle;

public class AccountSettingController implements Initializable {

    @FXML
    private AnchorPane accountSettingPane;

    @FXML
    private Button changePasswordButton;

    @FXML
    private ImageView logoImageView;

    @FXML
    private PasswordField password;

    @FXML
    private PasswordField rePassword;

    @FXML
    private TextField username;

    private User thisUser;

    public void setThisUser(User thisUser) {
        this.thisUser = thisUser;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        username.setText(thisUser.getUsername());
        username.setDisable(true);

        rePassword.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals(password.getText())) {
                changePasswordButton.setDisable(false);
            } else {
                changePasswordButton.setDisable(true);
            }
        });
        changePasswordButton.setOnAction(actionEvent -> {
            updatePassword();
        });
        rePassword.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                if (password.getText().equals(rePassword.getText())) {
                    updatePassword();
                }
            }
        });
    }

    public void updatePassword(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sign Up Notification");
        Boolean flag = true;
        User user = thisUser;
        user.setPassword(password.getText());
        if(StaffRepository.updateUser(user)){
            alert.setContentText("Change password successful!");
        }
        else {
            alert.setContentText("Error!");
            flag = false;
        }
        alert.showAndWait();
        if(flag){
            Stage stage = (Stage) accountSettingPane.getScene().getWindow();
            stage.close();
        }
    }
}
