package controller;

import entity.Staff;
import entity.StaffPosition;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import repository.StaffRepository;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

public class DetailStaffController implements Initializable {
    @FXML
    private TextField addressTextField;

    @FXML
    private DatePicker birthDatePicker;

    @FXML
    private CheckBox disableCheckbox;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField phoneTextField;

    @FXML
    private ComboBox<StaffPosition> positionCombobox;

    @FXML
    private Button saveButton;

    @FXML
    private BorderPane staffDetailBorderPane;

    @FXML
    private Label startWorkLabel;

    @FXML
    private HBox startWorkHBox;
    @FXML
    private HBox disableHBox;

    @FXML
    private Button signUpButton;
    private Staff staff;

    private boolean view;

    public void setView(boolean view) {
        this.view = view;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<StaffPosition> staffPositions = StaffRepository.getStaffPosition();
        positionCombobox.setItems(staffPositions);

        signUpButton.setDisable(true);
        signUpButton.setVisible(false);
        if (staff != null) {
            if (!StaffRepository.getUser(staff) && staff.getStaffPosition().getId() < 3) {
                signUpButton.setDisable(false);
                signUpButton.setVisible(true);
            }
        }

        if (view) {
            positionCombobox.getSelectionModel().select(staff.getStaffPosition());
            nameTextField.setText(staff.getName());
            addressTextField.setText(staff.getAddress());
            emailTextField.setText(staff.getEmail());
            phoneTextField.setText(staff.getPhone());
            birthDatePicker.setValue(staff.getBirthDate().toLocalDate());
            startWorkLabel.setText(staff.getStartWorkedAt().toString());
            if(staff.getId() == 1){
                disableHBox.setVisible(false);
            }
            else {
                disableCheckbox.setSelected(staff.isDeleted());
            }
        } else {
            disableHBox.setVisible(false);
            startWorkHBox.setVisible(false);
        }

        saveButton.setOnAction(actionEvent -> {
            Boolean flag = true;
            Staff staff1 = new Staff();
            if (view) {
                staff1.setId(staff.getId());
            }
            staff1.setDeleted(disableCheckbox.isSelected());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Notification");
            alert.setHeaderText("Add Notice");

            if (addressTextField.getText() == null || addressTextField.getText().trim().isEmpty()) {
                alert.setContentText("Please check address");
                flag = false;
            } else {
                staff1.setAddress(addressTextField.getText().trim());
            }

            if (phoneTextField.getText() == null || phoneTextField.getText().trim().isEmpty()) {
                alert.setContentText("Please check phone");
                flag = false;
            } else {
                staff1.setPhone(phoneTextField.getText());
            }

            if (emailTextField.getText() == null || emailTextField.getText().trim().isEmpty()) {
                alert.setContentText("Please check email");
            } else {
                staff1.setEmail(emailTextField.getText());
            }

            if (birthDatePicker.getValue() == null) {
                alert.setContentText("Please check date of birth");
                flag = false;
            } else {
                staff1.setBirthDate(Date.valueOf(birthDatePicker.getValue()));
            }

            if (nameTextField.getText() == null || nameTextField.getText().trim().isEmpty()) {
                alert.setContentText("Please check name");
                flag = false;
            } else {
                staff1.setName(nameTextField.getText());
            }

            if (positionCombobox.getValue() == null) {
                alert.setContentText("Please check position");
                flag = false;
            } else {
                staff1.setStaffPosition(positionCombobox.getValue());
            }
            if (flag) {
                if (view) {
                    if (StaffRepository.updateFullStaff(staff1)) {
                        alert.setContentText("Save Complete!!");
                    } else {
                        alert.setContentText("Error!!");
                        flag = false;
                    }
                } else {
                    if (StaffRepository.addStaff(staff1)) {
                        alert.setContentText("Save Complete!!");
                    } else {
                        alert.setContentText("Error!!");
                        flag = false;
                    }
                }
            }
            alert.showAndWait();
            Stage stage = (Stage) staffDetailBorderPane.getScene().getWindow();
            if (!view) {
                if (staff1.getStaffPosition().getId() < 3) {
                    Parent signUpRoot = null;
                    try {
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("../view/SignUp.fxml"));
                        SignUpController controller = new SignUpController();
                        controller.setThisStaff(staff1);
                        loader.setController(controller);
                        signUpRoot = loader.load();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Scene scene = new Scene(signUpRoot);
                    stage.setScene(scene);
                } else if (flag) {
                    stage.close();
                }
            }
            else {
                stage.close();
            }
        });

        signUpButton.setOnAction(actionEvent -> {
            Parent signUpRoot = null;
            Stage stage = (Stage) staffDetailBorderPane.getScene().getWindow();
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("../view/SignUp.fxml"));
                SignUpController controller = new SignUpController();
                controller.setThisStaff(staff);
                loader.setController(controller);
                signUpRoot = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Scene scene = new Scene(signUpRoot);
            stage.setScene(scene);
        });

    }
}
