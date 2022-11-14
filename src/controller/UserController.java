package controller;

import account.LoggedAccount;
import entity.Staff;
import entity.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import repository.StaffRepository;
import util.RegexTemplate;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class UserController implements Initializable {
    @FXML
    private VBox profileVBox;
    @FXML
    private Label staffName;
    @FXML
    private Label dob;
    @FXML
    private Label address;
    @FXML
    private Label position;
    @FXML
    private TextField phone;
    @FXML
    private TextField email;
    @FXML
    private Button saveButton;
    @FXML
    private Button accountSettingButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Optional<User> user = LoggedAccount.getUserLogged();
        StaffRepository sr = new StaffRepository();
        Optional<Staff> staff = Optional.empty();
        if (user.isPresent()) {
            staff = sr.getStaffFromUser(user.get());
            if (staff.isPresent()) {
                staffName.setText(staff.get().getName());
                dob.setText(staff.get().getBirthDate().toString());
                address.setText(staff.get().getAddress());
                position.setText(String.valueOf((StaffRepository.getStaffPosition(staff.get().getStaffPosition().getId()))));
                phone.setText(staff.get().getPhone());
                email.setText(staff.get().getEmail());
            }
        }

        Optional<Staff> finalStaff = staff;
        saveButton.setOnMouseClicked(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Notification");
            alert.setHeaderText("Update Notice");
            if(finalStaff.isPresent()){
                finalStaff.get().setPhone(phone.getText());
                finalStaff.get().setEmail(email.getText());
                Boolean isUpdate = sr.updateStaff(finalStaff.get());
                if(isUpdate){
                    alert.setContentText("Update Information Success!");
                }
                else{
                    alert.setContentText("Update Information Fail!");
                }
                alert.showAndWait();
            }
        });
        saveButton.setDisable(true);
        phone.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.matches(RegexTemplate.PHONE_REGEX)){
                saveButton.setDisable(false);
            }
            else{
                saveButton.setDisable(true);
            }
        });
        email.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.matches(RegexTemplate.EMAIL_REGEX)){
                saveButton.setDisable(false);
            }else {
                saveButton.setDisable(true);
            }
        });

        accountSettingButton.setOnAction(actionEvent -> {
            Stage thisStage = (Stage) profileVBox.getScene().getWindow();
            Parent signUpRoot = null;
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("../view/AccountSetting.fxml"));
                AccountSettingController controller = new AccountSettingController();
                controller.setThisUser(LoggedAccount.getUserLogged().get());
                loader.setController(controller);
                signUpRoot = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Scene scene = new Scene(signUpRoot);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setX(300);
            stage.setY(50);
            stage.initOwner(thisStage);
            stage.showAndWait();
        });
    }
}
