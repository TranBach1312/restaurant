package controller;

import entity.DinnerTable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import repository.OrderRepository;

import java.net.URL;
import java.util.ResourceBundle;

public class DinnerTableController implements Initializable {
    @FXML
    private GridPane dinnerTableGridPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dinnerTableGridPane.setHgap(10);
        dinnerTableGridPane.setVgap(10);
        setDinnerTableGridPane();
    }

    public void setDinnerTableGridPane() {

        ObservableList<DinnerTable> dinnerTableList = OrderRepository.getDinnerTableList();
        int col = 0;
        int row = 0;

        for (int i = 0; i < dinnerTableList.size(); i++) {
            StackPane stackPane = new StackPane();
            Button button = new Button();
            button.setPrefWidth(150);
            button.setPrefHeight(150);
            button.setContentDisplay(ContentDisplay.BOTTOM);
            DinnerTable dinnerTable = new DinnerTable();
            dinnerTable = dinnerTableList.get(i);

            Label label = new Label("Blank");
            label.setTextFill(Paint.valueOf("white"));
            button.getStyleClass().add("dinner-table");
            if(dinnerTable.isInUse()){
                label.setText("In use");
                label.setTextFill(Paint.valueOf("white"));
                button.getStyleClass().add("in-use");
            }


            button.setText(dinnerTable.getName());
            button.setGraphic(label);
            DinnerTable finalDinnerTable = dinnerTable;
            button.setOnAction((ActionEvent event) -> {
                try {
                    Stage stage = new Stage();
                    stage.setTitle(finalDinnerTable.getName());
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(this.getClass().getResource("../view/Order.fxml"));
                    OrderController controller = new OrderController();
                    controller.setDinnerTable(finalDinnerTable);
                    loader.setController(controller);
                    Parent root = loader.load();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.setResizable(false);
                    Stage curStage = (Stage) dinnerTableGridPane.getScene().getWindow();
                    stage.initOwner(curStage);
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setOnHidden(e -> {
                        setDinnerTableGridPane();
                    });
                    stage.showAndWait();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            stackPane.getChildren().add(button);
            dinnerTableGridPane.add(stackPane, col, row);
            if (i % 2 == 0) {
                col++;
            } else {
                col = 0;
                row++;
            }
        }
    }
}
