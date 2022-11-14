package controller;

import entity.Staff;
import entity.StaffPosition;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import repository.OrderRepository;
import repository.StaffRepository;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class StaffManagerController implements Initializable {

    @FXML
    private TableView<Staff> staffTableView;

    @FXML
    private TableColumn<Staff, Date> startedWorkColumn;

    @FXML
    private TableColumn<Staff, String> addressColumn;

    @FXML
    private TableColumn<Staff, Date> dobColumn;

    @FXML
    private TableColumn<Staff, String> emailColumn;

    @FXML
    private TableColumn<Staff, Integer> idColumn;

    @FXML
    private TableColumn<Staff, String> nameColumn;

    @FXML
    private TableColumn<Staff, String> phoneColumn;

    @FXML
    private TableColumn<Staff, StaffPosition> positionColumn;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchTextField;

    @FXML
    private VBox staffManagerVBox;

    @FXML
    private Button addNewButton;

    @FXML
    private Button prevButton;
    @FXML
    private Button nextButton;
    @FXML
    private HBox pageHBox;

    private IntegerProperty curPage = new SimpleIntegerProperty();

    private int limit = 20;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idColumn.setCellValueFactory(new PropertyValueFactory<Staff, Integer>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Staff, String>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<Staff, String>("email"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<Staff, String>("address"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<Staff, String>("phone"));
        dobColumn.setCellValueFactory(new PropertyValueFactory<Staff, Date>("birthDate"));
        positionColumn.setCellValueFactory(new PropertyValueFactory<Staff, StaffPosition>("staffPosition"));
        startedWorkColumn.setCellValueFactory(new PropertyValueFactory<Staff, Date>("startWorkedAt"));

        staffTableView.setRowFactory(tr -> {
            TableRow<Staff> row = new TableRow<>();
            row.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getClickCount() == 2 && !row.isEmpty()) {
                    if (row.getItem() != null) {
                        Staff staff = row.getItem();
                        toStaff(staff, true);
                    }
                }
            });
            return row;
        });
        staffManagerVBox.addEventFilter(ScrollEvent.ANY, scrollEvent -> {
            staffTableView.refresh();

            // close text box
            staffTableView.edit(-1, null);
        });

        addNewButton.setOnAction(actionEvent -> {
            toStaff(null, false);
        });


        int staffCount = StaffRepository.getCountStaff();
        int numberOfPage = (int) Math.ceil((double) staffCount / limit);
        for (int i = 1; i <= numberOfPage; i++) {
            Button button = new Button(i + "");
            button.setPrefHeight(30);
            button.setPrefWidth(30);
            int finalI = i;
            button.setOnAction(actionEvent -> {
                curPage.set(finalI);
                staffTableView.setItems(StaffRepository.getStaffList("", curPage.get(), limit));
                staffTableView.refresh();

                // close text box
                staffTableView.edit(-1, null);
            });

            pageHBox.getChildren().add(button);
        }
        curPage.addListener((observableValue, number, t1) -> {

            System.out.println(numberOfPage);
            if (t1.equals(1)) {
                prevButton.setDisable(true);
            } else {
                prevButton.setDisable(false);
            }
            if (t1.equals(numberOfPage)) {
                nextButton.setDisable(true);
            } else {
                nextButton.setDisable(false);
            }
        });

        prevButton.setOnAction(actionEvent -> {
            curPage.set(curPage.get() - 1);
            staffTableView.setItems(StaffRepository.getStaffList(searchTextField.getText(), curPage.get(), limit));
            staffTableView.refresh();

            // close text box
            staffTableView.edit(-1, null);
        });

        nextButton.setOnAction(actionEvent -> {
            curPage.set(curPage.get() + 1);
            staffTableView.setItems(StaffRepository.getStaffList(searchTextField.getText(), curPage.get(), limit));
            staffTableView.refresh();

            // close text box
            staffTableView.edit(-1, null);
        });


        curPage.set(1);
        staffTableView.setItems(StaffRepository.getStaffList("", curPage.get(), limit));
        staffTableView.refresh();

        // close text box
        staffTableView.edit(-1, null);
    }


    public void toStaff(Staff staff, boolean view) {
        Stage thisStage = (Stage) staffManagerVBox.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("../view/Staff.fxml"));
        DetailStaffController controller = new DetailStaffController();
        controller.setStaff(staff);
        controller.setView(view);
        loader.setController(controller);
        try {
            Parent billRoot = loader.load();
            Scene scene = new Scene(billRoot);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setX(300);
            stage.setY(50);
            stage.setScene(scene);
            stage.initOwner(thisStage);
            stage.setOnHidden(windowEvent -> {
                staffTableView.setItems(StaffRepository.getStaffList(searchTextField.getText(), curPage.get(), limit));

                staffTableView.refresh();

                // close text box
                staffTableView.edit(-1, null);
            });
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

