package controller;

import entity.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import repository.OrderRepository;
import util.RegexTemplate;

import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class BillViewOnlyController implements Initializable {
    @FXML
    private BorderPane billBorderPane;
    @FXML
    private TableView<OrderItem> dishTableView;

    @FXML
    private TableColumn<OrderItem, Integer> noColumn;

    @FXML
    private TableColumn<OrderItem, Dish> dishNameColumn;

    @FXML
    private TableColumn<OrderItem, Double> priceColumn;

    @FXML
    private TableColumn<OrderItem, Integer> quantityColumn;

    @FXML
    private TableColumn<OrderItem, Double> totalColumn;

    @FXML
    private TextField phone;

    @FXML
    private Label grandTotal;

    @FXML
    private Label billId;

    @FXML
    private TextField billTo;
    @FXML
    private Label discount;

    @FXML
    private Label billDate;

    @FXML
    private Label customerName;

    @FXML
    private Button finishButton;


    private Order thisOrder;
    private Bill thisBill = new Bill();

    public BillViewOnlyController() {
    }

    public void setThisBill(Bill bill) {
        this.thisBill = bill;
        setThisOrder(thisBill.getOrder());
    }

    public void setThisOrder(Order thisOrder) {
        this.thisOrder = thisOrder;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //ordered table
        dishNameColumn.setCellValueFactory(new PropertyValueFactory<OrderItem, Dish>("dish"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<OrderItem, Double>("price"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<OrderItem, Integer>("quantity"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<OrderItem, Double>("totalMoney"));
        ObservableList<OrderItem> orderItems = FXCollections.observableArrayList(thisOrder.getOrderItems());
        dishTableView.setItems(orderItems);

        billTo.setText(String.valueOf(thisBill.getCustomer().getName()));
        phone.setText(String.valueOf(thisBill.getCustomer().getPhone()));

        //initial total and discount
        grandTotal.setText(String.valueOf(thisBill.getTotalMoney()));

        //discount
        discount.setText(String.valueOf(thisBill.getTotalDiscount()));

        //date
        Date date = new Date(thisBill.getCreatedAt().getTime());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String billDateText = dateFormat.format(date);
        billDate.setText(billDateText);

        //bill number
        billId.setText(thisBill.getBillNumber());
        //finish

        finishButton.setVisible(false);
    }
}
