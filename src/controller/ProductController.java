package controller;

import entity.Dish;
import entity.DishCategory;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import repository.OrderRepository;
import repository.StaffRepository;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProductController implements Initializable {

    @FXML
    private VBox productVBox;
    @FXML
    private Button addButton;

    @FXML
    private TableView<Dish> dishTableView;


    @FXML
    private TableColumn<Dish, String> nameColumn;

    @FXML
    private TableColumn<Dish, Double> priceColumn;

    @FXML
    private TableColumn<Dish, Double> averageCostPriceColumn;

    @FXML
    private TableColumn<Dish, DishCategory> categoryColumn;


    @FXML
    private TableColumn<Dish, Integer> totalSoldColumn;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchTextField;

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

        nameColumn.setCellValueFactory(new PropertyValueFactory<Dish, String>("name"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<Dish, DishCategory>("category"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Dish, Double>("price"));
        averageCostPriceColumn.setCellValueFactory(new PropertyValueFactory<Dish, Double>("averageCostPrice"));
        totalSoldColumn.setCellValueFactory(new PropertyValueFactory<Dish, Integer>("totalSold"));


        searchButton.setOnMouseClicked(mouseEvent -> {
            ObservableList<Dish> dishes = OrderRepository.getDishList(searchTextField.getText().trim(), curPage.get(), limit);
            dishTableView.setItems(dishes);
        });

        addButton.setOnMouseClicked(mouseEvent -> {
            Stage thisStage = (Stage) productVBox.getScene().getWindow();
            Stage stage = new Stage();
            stage.setTitle("Add a new Dish");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource("../view/DishManage.fxml"));
            AddDishController controller = new AddDishController();
            loader.setController(controller);
            try {
                Parent addDishRoot = loader.load();
                Scene scene = new Scene(addDishRoot);
                stage.setScene(scene);
                stage.initOwner(thisStage);
                stage.setOnHidden(windowEvent -> {
                    resetTable();
                });
                stage.showAndWait();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });
        searchTextField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                curPage.set(1);
                ObservableList<Dish> dishes = OrderRepository.getDishList(searchTextField.getText().trim(), curPage.get(), limit);
                dishTableView.setItems(dishes);

                dishTableView.refresh();

                // close text box
                dishTableView.edit(-1, null);
            }
        });

        dishTableView.addEventFilter(ScrollEvent.ANY, scrollEvent -> {
            dishTableView.refresh();

            // close text box
            dishTableView.edit(-1, null);
        });
        dishTableView.setRowFactory(tr -> {
            TableRow<Dish> row = new TableRow<>() {
                @Override
                protected void updateItem(Dish item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null) {

                    } else if (item.isDeleted()) {
                        setStyle("-fx-background-color: #353b48;");
                        setTextFill(Paint.valueOf("white"));
                    } else if (item.isSoldOut())
                        setStyle("-fx-background-color: #bdc3c7;");
                    else
                        setStyle("-fx-background-color: #3498db");
                }
            };
            row.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getClickCount() == 2 && !row.isEmpty()) {
                    Dish dish = row.getItem();
                    detailDish(dish);
                }
            });
            return row;
        });

        int dishCount = OrderRepository.getDishCount();
        int numberOfPage = (int) Math.ceil((double) dishCount / limit);
        for (int i = 1; i <= numberOfPage; i++) {
            Button button = new Button(i + "");
            button.setPrefHeight(30);
            button.setPrefWidth(30);
            int finalI = i;
            button.setOnAction(actionEvent -> {
                curPage.set(finalI);
                dishTableView.setItems(OrderRepository.getDishList(searchTextField.getText(), curPage.get(), limit));

                dishTableView.refresh();

                // close text box
                dishTableView.edit(-1, null);
            });

            pageHBox.getChildren().add(button);
        }

        curPage.addListener((observableValue, number, t1) -> {
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
            dishTableView.setItems(OrderRepository.getDishList(searchTextField.getText(), curPage.get(), limit));
            dishTableView.refresh();

            // close text box
            dishTableView.edit(-1, null);
        });

        nextButton.setOnAction(actionEvent -> {
            curPage.set(curPage.get() + 1);
            dishTableView.setItems(OrderRepository.getDishList(searchTextField.getText(), curPage.get(), limit));
            dishTableView.refresh();

            // close text box
            dishTableView.edit(-1, null);
        });


        curPage.set(1);
        resetTable();
    }

    public void detailDish(Dish dish) {
        Stage thisStage = (Stage) productVBox.getScene().getWindow();
        Stage stage = new Stage();
        stage.setTitle("Dish Detail");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("../view/DishManage.fxml"));
        DetailDishController controller = new DetailDishController();
        controller.setThisDish(dish);
        loader.setController(controller);
        try {
            Parent addDishRoot = loader.load();
            Scene scene = new Scene(addDishRoot);
            stage.setScene(scene);
            stage.initOwner(thisStage);
            stage.setOnHidden(windowEvent -> {
                resetTable();
            });
            stage.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void resetTable() {
        ObservableList<Dish> dishObservableList = OrderRepository.getDishList("", curPage.get(), limit);
        dishTableView.setItems(dishObservableList);

        dishTableView.refresh();

        // close text box
        dishTableView.edit(-1, null);
    }
}
