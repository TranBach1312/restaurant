package controller;

import entity.Bill;
import entity.Customer;
import entity.DinnerTable;
import entity.Dish;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import repository.OrderRepository;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class BillHistoryController implements Initializable {

    @FXML
    private VBox billHistoryVBox;

    @FXML
    private TableColumn<Bill, String> billNumberColumn;

    @FXML
    private TableView<Bill> billTableView;

    @FXML
    private TableColumn<Bill, Timestamp> createdAtColumn;

    @FXML
    private TableColumn<Bill, Customer> customerColumn;

    @FXML
    private TableColumn<Bill, DinnerTable> dinnerTableColumn;

    @FXML
    private TableColumn<Bill, Double> totalDiscountColumn;

    @FXML
    private TableColumn<Bill, Double> totalMoneyColumn;

    @FXML
    private DatePicker dateBillPicker;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        LocalDate today = LocalDate.now();

        dateBillPicker.setValue(today);
        dateBillPicker.setConverter(new StringConverter<LocalDate>() {
            String pattern = "yyyy-MM-dd";
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });

        billNumberColumn.setCellValueFactory(new PropertyValueFactory<Bill, String>("billNumber"));
        customerColumn.setCellValueFactory(new PropertyValueFactory<Bill, Customer>("customer"));
        dinnerTableColumn.setCellValueFactory(new PropertyValueFactory<Bill, DinnerTable>("dinnerTable"));
        totalDiscountColumn.setCellValueFactory(new PropertyValueFactory<Bill, Double>("totalDiscount"));
        totalMoneyColumn.setCellValueFactory(new PropertyValueFactory<Bill, Double>("totalMoney"));
        createdAtColumn.setCellValueFactory(new PropertyValueFactory<Bill, Timestamp>("createdAt"));
        ObservableList<Bill> bills = OrderRepository.getBillList(today);
        billTableView.setItems(bills);
        dateBillPicker.setOnAction(actionEvent -> {
            LocalDate localDate = dateBillPicker.getValue();
            billTableView.setItems(OrderRepository.getBillList(localDate));
        });

        billTableView.setRowFactory(tr -> {
            TableRow<Bill> row = new TableRow<>();
            row.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getClickCount() == 2 && !row.isEmpty()) {
                    if (row.getItem() != null) {
                        Bill bill = row.getItem();
                        detailBill(bill);
                    }
                }
            });
            return row;
        });
    }

    public void detailBill(Bill bill) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("../view/Bill.fxml"));
        BillViewOnlyController controller = new BillViewOnlyController();
        controller.setThisBill(bill);
        loader.setController(controller);
        try {
            Parent billRoot = loader.load();
            Scene scene = new Scene(billRoot);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setX(500);
            stage.setY(50);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

