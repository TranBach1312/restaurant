package controller;

import account.LoggedAccount;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {
    @FXML
    private BorderPane profileBorderPane;
    @FXML
    private Button userButton;
    @FXML
    private Button productManageButton;
    @FXML
    private Button staffManageButton;
    @FXML
    private Button statsButton;
    @FXML
    private Button tradingButton;

    @FXML
    private Button orderHistoryButton;

    @FXML
    private Button logOutButton;
    @Override
    public void initialize(URL location, ResourceBundle resources) {


        //user page
        Parent userRoot = null;
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/User.fxml"));
            userRoot = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent finalUserRoot = userRoot;
        userButton.setOnMouseClicked(event -> {
            profileBorderPane.setCenter(finalUserRoot);
            changeBackgroundColor(userButton);
        });

        //product manage page
        Parent productRoot = null;
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/Product.fxml"));
            productRoot = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent finalProductRoot = productRoot;
        productManageButton.setOnMouseClicked(event -> {
            profileBorderPane.setCenter(finalProductRoot);
            changeBackgroundColor(productManageButton);
        });
        // staff manage page
        Parent staffRoot = null;
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/StaffManage.fxml"));
            staffRoot = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent finalStaffRoot = staffRoot;
        if(!LoggedAccount.getUserLogged().get().isManager()){
            staffManageButton.setDisable(true);
        }
        staffManageButton.setOnMouseClicked(event -> {
            profileBorderPane.setCenter(finalStaffRoot);
            changeBackgroundColor(staffManageButton);
        });

        //stats manage page
        Parent statsRoot = null;
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/Stats.fxml"));
            statsRoot = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent finalStatsRoot = statsRoot;
        statsButton.setOnMouseClicked(event -> {
            profileBorderPane.setCenter(finalStatsRoot);
            changeBackgroundColor(statsButton);
        });

        //trading manage page
        Parent tradingRoot = null;
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/DinnerTable.fxml"));
            tradingRoot = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent finalTradingRoot = tradingRoot;
        tradingButton.setOnMouseClicked(event -> {
            profileBorderPane.setCenter(finalTradingRoot);
            changeBackgroundColor(tradingButton);
        });

        //bill history
        orderHistoryButton.setOnMouseClicked(event -> {
            Parent billRoot = null;
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("../view/BillHistory.fxml"));
                billRoot = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            profileBorderPane.setCenter(billRoot);
            changeBackgroundColor(orderHistoryButton);
        });


        profileBorderPane.setCenter(finalTradingRoot);
        changeBackgroundColor(tradingButton);

        logOutButton.setOnAction(actionEvent -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            ButtonType buttonTypeYes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().set(0, buttonTypeYes);
            alert.getButtonTypes().set(1, buttonTypeCancel);
            alert.setTitle("Log Out!!");
            alert.setContentText("Are you sure?");

            if(alert.showAndWait().get() == buttonTypeYes){
                LoggedAccount.logOut();
                ((Stage)profileBorderPane.getScene().getWindow()).close();
            }
        });
    }

    public void changePage(String resource, Object controller) {
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(resource));
            if(controller != null){
                loader.setController(controller);
            }
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        profileBorderPane.setCenter(root);
    }
    public void changeBackgroundColor(Node node){

        orderHistoryButton.getStyleClass().remove("active");
        userButton.getStyleClass().remove("active");
        tradingButton.getStyleClass().remove("active");
        productManageButton.getStyleClass().remove("active");
        staffManageButton.getStyleClass().remove("active");
        statsButton.getStyleClass().remove("active");

        node.getStyleClass().add("active");
    }
}
