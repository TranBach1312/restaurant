package model;

import account.LoggedAccount;
import entity.User;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import repository.StaffRepository;
import util.SHA256;

public class StaffModel {

    public static boolean checkLogin(TextField username, PasswordField password){
        boolean flag = false;
        String passwordHashed = SHA256.getSHA256(password.getText());

        User user = StaffRepository.getUser(username.getText(), passwordHashed);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notification");
        alert.setHeaderText("Login Notice");
        if(user == null){
            alert.setContentText("Login failed, check your username and password!");
        }
        else if(!user.isActive()){
            alert.setContentText("Your account has been disabled!");
        }
        else{
            alert.setContentText("Login Successful!");
            flag = true;
            setUserLogin(user);
        }
        alert.showAndWait();
        return flag;
    }
    public static void setUserLogin(User user){
        LoggedAccount.changeLoggedUser(user);
    }
}
