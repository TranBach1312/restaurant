package controller;

import entity.Bill;
import entity.Dish;
import entity.OrderItem;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import repository.OrderRepository;

import java.awt.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class StatsController implements Initializable {

    @FXML
    private LineChart<String, Double> moneyLineChart;

    @FXML
    private Label totalRevenueLabel;

    @FXML
    private Label interestRateLabel;

    @FXML
    private Label totalFundsLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        LocalDate today = LocalDate.now();
        ObservableList<Bill> bills = OrderRepository.getBillList(today.minusDays(7), today);

        AtomicReference<Double> totalRevenue = new AtomicReference<>(0.0);
        AtomicReference<Double> totalFunds = new AtomicReference<>(0.0);
        bills.forEach(bill -> {
            ArrayList<OrderItem> orderItems = OrderRepository.getOrderItemFromOrder(bill.getOrder());
            orderItems.forEach(orderItem -> {
                totalFunds.updateAndGet(v -> v + (orderItem.getDish().getAverageCostPrice() * orderItem.getQuantity()));
            });
            totalRevenue.updateAndGet(v -> v + bill.getTotalMoney());
        });

        totalRevenueLabel.setText("$" + String.format("%,.2f", totalRevenue.get()));
        totalFundsLabel.setText("$" + String.format("%,.2f", totalFunds.get()));
        Double interestRate = (totalRevenue.get() / totalFunds.get()) - 1;
        interestRateLabel.setText(String.format("%,.3f",interestRate * 100) + "%");

        XYChart.Series<String, Double> series = new XYChart.Series<>();
        XYChart.Data<String, Double> monday = new XYChart.Data<>("Monday", bills.get(0).getTotalMoney());
        XYChart.Data<String, Double> tuesday = new XYChart.Data<>("Tuesday", bills.get(1).getTotalMoney());
        XYChart.Data<String, Double> wednesday = new XYChart.Data<>("Wednesday", bills.get(2).getTotalMoney());
        XYChart.Data<String, Double> thursday = new XYChart.Data<>("Thursday", bills.get(3).getTotalMoney());
        XYChart.Data<String, Double> friday = new XYChart.Data<>("Friday", bills.get(4).getTotalMoney());
        XYChart.Data<String, Double> saturday = new XYChart.Data<>("Saturday", bills.get(5).getTotalMoney());
        XYChart.Data<String, Double> sunday = new XYChart.Data<>("Sunday", bills.get(6).getTotalMoney());

        series.getData().addAll(monday, tuesday, wednesday, thursday, friday, saturday, sunday);
        series.setName("Revenue for the week");
        moneyLineChart.getData().add(series);
        moneyLineChart.getXAxis().setLabel("Day of Week");
        moneyLineChart.getYAxis().setLabel("Revenue");
    }
}

