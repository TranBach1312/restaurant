package main;

import account.LoggedAccount;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class RestaurantApplication extends Application {
    private Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.stage = primaryStage;
        Parent root = null;
        if(LoggedAccount.getUserLogged().isPresent()){
            root = new FXMLLoader().load(this.getClass().getResource("../view/Profile.fxml"));
        }
        else{
            root = new FXMLLoader().load(this.getClass().getResource("../view/Login.fxml"));
        }
        Image logoIcon = new Image("resources/logo.jpg");
        primaryStage.getIcons().add(logoIcon);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


}
