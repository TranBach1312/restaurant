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


public class DetailDishController implements Initializable {

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
    private CheckBox deleteCheckBox;

    @FXML
    private CheckBox soldOutCheckBox;

    private Dish thisDish;

    public void setThisDish(Dish thisDish) {
        this.thisDish = thisDish;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ObservableList<DishCategory> dishCategories = OrderRepository.getDishCategoryList();
        categoryComboBox.getItems().addAll(dishCategories);

        //show data
        selectedImageView.setImage(thisDish.getImage().getImage());

        nameTextField.setText(thisDish.getName());

        categoryComboBox.getSelectionModel().select(thisDish.getCategory());

        averageCostPriceTextField.setText(String.valueOf(thisDish.getAverageCostPrice()));

        priceTextField.setText(String.valueOf(thisDish.getPrice()));

        deleteCheckBox.setSelected(thisDish.isDeleted());

        soldOutCheckBox.setSelected(thisDish.isSoldOut());

        selectImageButton.setOnMouseClicked(mouseEvent -> {
            Stage stage = (Stage) addDishHBox.getScene().getWindow();
            FileChooser fc = new FileChooser();
            FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Images File", "*.jpg", "*.png");
            fc.setTitle("Choose Image");
            fc.getExtensionFilters().add(imageFilter);
            fc.setInitialDirectory(new File("C:\\Users\\bacht\\Pictures\\p2"));
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


        addButton.setText("Save");
        addButton.setOnMouseClicked(mouseEvent -> {
            Boolean flag = true;
            thisDish.setDeleted(deleteCheckBox.isSelected());
            thisDish.setSoldOut(soldOutCheckBox.isSelected());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Notification");
            alert.setHeaderText("Add Notice");

            if (priceTextField.getText() != null && CheckFormat.isNumeric(priceTextField.getText())) {
                if (Double.valueOf(priceTextField.getText()) >= 0) {
                    thisDish.setPrice(Double.valueOf(priceTextField.getText()));
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
                    thisDish.setAverageCostPrice(Double.valueOf(averageCostPriceTextField.getText()));
                } else {
                    alert.setContentText("Please check Average cost price!");
                    flag = false;
                }
            } else {
                alert.setContentText("Please check Average cost price!");
                flag = false;
            }
            if (categoryComboBox.getSelectionModel().getSelectedItem() != null) {
                thisDish.setCategory(categoryComboBox.getSelectionModel().getSelectedItem());
            } else {
                alert.setContentText("Please check category!");
                flag = false;
            }
            if (!nameTextField.getText().isEmpty()) {
                thisDish.setName(nameTextField.getText());
            } else {
                alert.setContentText("Please check Name!");
                flag = false;
            }

            if (selectedImageView.getImage() != null) {
                thisDish.setImage(selectedImageView);
            } else {
                Image image = new Image("/resources/default.png");
                thisDish.setImage(new ImageView(image));
            }

            if (flag) {
                if (OrderRepository.updateDish(thisDish)) {
                    alert.setContentText("Save success!");
                } else {
                    alert.setContentText("Error!");
                    flag = false;
                }
            }
            alert.showAndWait();
            if (flag) {
                Stage stage = (Stage) addDishHBox.getScene().getWindow();
                stage.close();
            }
        });
    }
}