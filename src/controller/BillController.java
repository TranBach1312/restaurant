package controller;

import entity.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ScrollEvent;
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

public class BillController implements Initializable {
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

    public BillController() {
    }

    public void setThisOrder(Order thisOrder) {
        this.thisOrder = thisOrder;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //initial bill
        thisBill.setOrder(thisOrder);
        thisBill.setTotalMoney(thisOrder.getTotalMoney());
        //ordered table
        dishNameColumn.setCellValueFactory(new PropertyValueFactory<OrderItem, Dish>("dish"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<OrderItem, Double>("price"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<OrderItem, Integer>("quantity"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<OrderItem, Double>("totalMoney"));
        ObservableList<OrderItem> orderItems = FXCollections.observableList(thisOrder.getOrderItems());
        dishTableView.addEventFilter(ScrollEvent.ANY, scrollEvent -> {
            dishTableView.refresh();

            // close text box
            dishTableView.edit(-1, null);
        });
        dishTableView.setItems(orderItems);

        //initial total and discount
        discount.setText("0%");
        grandTotal.setText(String.valueOf(thisOrder.getTotalMoney()));

        //discount
        AtomicReference<Boolean> flagCustomer = new AtomicReference<>(false);
        AtomicReference<String> phoneNumberTmp = new AtomicReference<>();
        phone.textProperty().addListener((observable, oldValue, newValue) -> {
            Optional<Customer> customer = Optional.empty();
            if (newValue.matches(RegexTemplate.PHONE_REGEX)) {
                flagCustomer.set(true);
                customer = OrderRepository.getCustomer(newValue);
                if (customer.isPresent()) {
                    phoneNumberTmp.set(null);
                    thisBill.setCustomer(customer.get());
                    Double discountOnCustomer = 0.0;
                    if (Double.valueOf(customer.get().getMemberLevel() * (0.1 / 100)) < ((double) 5 / 100)) {
                        discountOnCustomer = Math.floor(Double.valueOf(customer.get().getMemberLevel() * (0.1 / 100)) * 1000) / 1000;
                    } else {
                        discountOnCustomer = (double) 5 / 100;
                    }
                    thisBill.setTotalDiscount(discountOnCustomer);
                    DecimalFormat df = new DecimalFormat("#.##");

                    discount.setText(df.format(thisBill.getTotalDiscount() * 100) + "%");
                    thisBill.setTotalMoney(thisOrder.getTotalMoney() * ((100 - discountOnCustomer * 100) / 100));
                    grandTotal.setText(String.valueOf(df.format(thisBill.getTotalMoney())));
                } else {
                    phoneNumberTmp.set(newValue);
                    thisBill.setCustomer(null);
                    thisBill.setTotalDiscount(0);
                    discount.setText(thisBill.getTotalDiscount() + "%");
                    thisBill.setTotalMoney(thisOrder.getTotalMoney());
                    grandTotal.setText(String.valueOf(thisBill.getTotalMoney()));
                }
            } else {
                phoneNumberTmp.set(null);
                flagCustomer.set(false);
                thisBill.setCustomer(null);
                thisBill.setTotalDiscount(0);
                discount.setText(thisBill.getTotalDiscount() + "%");
                thisBill.setTotalMoney(thisOrder.getTotalMoney());
                grandTotal.setText(String.valueOf(thisBill.getTotalMoney()));
            }
        });

        //date
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        thisBill.setCreatedAt(new Timestamp(date.getTime()));

        String billDateText = dateFormat.format(new Date());
        billDate.setText(billDateText);

        //bill number
        String billNumber = billDateText.replace("-", "") + "-" + ((int) Math.floor(Math.random() * 1000)) + ((int) Math.floor(Math.random() * 1000));
        thisBill.setBillNumber(billNumber);
        billId.setText(thisBill.getBillNumber());

        //finish

        finishButton.setOnMouseClicked(event -> {
            if (flagCustomer.get()) {
                if (thisBill.getCustomer() == null) {
                    Customer tmpCustomer = new Customer();
                    tmpCustomer.setPhone(phoneNumberTmp.get());
                    tmpCustomer.setName(billTo.getText());
                    OrderRepository.insertNewCustomer(tmpCustomer);
                    flagCustomer.set(false);
                } else {
                    OrderRepository.updateLevelMember(thisBill.getCustomer());
                }
            }
            thisBill.getOrder().setStatus(true);
            thisBill.getOrder().getOrderItems().forEach(e -> {
                e.getDish().setTotalSold(e.getDish().getTotalSold() + e.getQuantity());
                OrderRepository.updateTotalSold(e.getDish());
            });
            OrderRepository.updateOrder(thisBill.getOrder());
            OrderRepository.updateTable(thisBill.getOrder().getTable());
            OrderRepository.insertNewBill(thisBill);
            Stage curStage = (Stage) (billBorderPane.getScene().getWindow());
            curStage.close();

        });
    }
}
