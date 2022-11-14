package controller;

import entity.Dish;
import entity.DishCategory;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import repository.OrderRepository;
import util.CheckFormat;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;


public class AddDishController implements Initializable {
    @FXML
    private Button addButton;

    @FXML
    private HBox addDishHBox;

    @FXML
    private TextField averageCostPriceTextField;

    @FXML
    private ComboBox<DishCategory> categoryComboBox;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField priceTextField;

    @FXML
    private Button selectImageButton;

    @FXML
    private ImageView selectedImageView;

    @FXML
    private HBox checkHBox;

    @FXML
    private CheckBox activeCheckBox;

    @FXML
    private CheckBox soldOutCheckBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        checkHBox.setVisible(false);
        //image
        selectImageButton.setOnMouseClicked(mouseEvent -> {
            Stage stage = (Stage) addDishHBox.getScene().getWindow();
            FileChooser fc = new FileChooser();
            FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Images File", "*.jpg", "*.png");
            fc.setInitialDirectory(new File("C:\\Users\\bacht\\Pictures\\p2"));
            fc.setTitle("Choose Image");
            fc.getExtensionFilters().add(imageFilter);
            File file = fc.showOpenDialog(stage);

            if (file != null) {
                Image image = null;
                try {
                    image = new Image(file.toURI().toURL().toExternalForm());
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
                selectedImageView.setImage(image);
            }
        });


        //dish category
        ObservableList<DishCategory> dishCategories = OrderRepository.getDishCategoryList();
        categoryComboBox.getItems().addAll(dishCategories);


        addButton.setOnMouseClicked(mouseEvent -> {
            Boolean flag = true;
            Dish dish = new Dish();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Notification");
            alert.setHeaderText("Add Notice");

            if (priceTextField.getText() != null && CheckFormat.isNumeric(priceTextField.getText())) {
                if (Double.valueOf(priceTextField.getText()) >= 0) {
                    dish.setPrice(Double.valueOf(priceTextField.getText()));
                } else {
                    flag = false;
                    alert.setContentText("Please check Price!");
                }
            } else {
                flag = false;
                alert.setContentText("Please check Price!");
            }
            if (averageCostPriceTextField.getText() != null && CheckFormat.isNumeric(averageCostPriceTextField.getText())) {
                if (Double.valueOf(averageCostPriceTextField.getText()) >= 0) {
                    dish.setAverageCostPrice(Double.valueOf(averageCostPriceTextField.getText()));
                } else {
                    alert.setContentText("Please check Average cost price!");
                    flag = false;
                }
            } else {
                alert.setContentText("Please check Average cost price!");
                flag = false;
            }
            if (categoryComboBox.getSelectionModel().getSelectedItem() != null) {
                dish.setCategory(categoryComboBox.getSelectionModel().getSelectedItem());
            } else {
                alert.setContentText("Please check category!");
                flag = false;
            }
            if (!nameTextField.getText().isEmpty()) {
                dish.setName(nameTextField.getText());
            } else {
                alert.setContentText("Please check Name!");
                flag = false;
            }

            if (selectedImageView.getImage() != null) {
                dish.setImage(selectedImageView);
            } else {
                Image image = new Image("/resources/default.png");
                dish.setImage(new ImageView(image));
            }

            if (flag) {
                if(OrderRepository.insetNewDish(dish)){
                    alert.setContentText("Add success!");
                }
                else {
                    alert.setContentText("Error!");
                    flag = false;
                }
            }
            alert.showAndWait();
            if(flag){
                Stage stage = (Stage) addDishHBox.getScene().getWindow();
                stage.close();
            }
        });
    }

}
