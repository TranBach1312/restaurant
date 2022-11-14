package repository;

import JDBC.JDBCConnect;
import entity.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

public class OrderRepository {


    public static Dish getDish(ResultSet rs) {
        Dish dish = new Dish();
        try {
            dish.setId(rs.getInt("id"));
            dish.setName(rs.getString("name"));
            dish.setPrice(rs.getDouble("price"));
            dish.setCategory(getDishCategory(rs.getInt("category_id")).get());
            InputStream inputStream = rs.getBinaryStream("image");
            Image image = new Image(inputStream);
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(150);
            imageView.setFitHeight(150);
            dish.setImage(imageView);
            dish.setTotalSold(rs.getInt("total_sold"));
            dish.setAverageCostPrice(rs.getDouble("average_cost_price"));
            dish.setSoldOut(rs.getBoolean("sold_out"));
            dish.setDeleted(rs.getBoolean("deleted"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dish;
    }

    public static Optional<Dish> getDish(int id) {
        Connection conn = JDBCConnect.JDBCConnector();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM `dish` WHERE `id` = ?";
        Optional<Dish> dish = Optional.empty();
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                dish = Optional.of(getDish(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JDBCConnect.closeResultSet(rs);
            JDBCConnect.closePreparedStatement(ps);
            JDBCConnect.closeConnection(conn);
        }
        return dish;
    }

    public static boolean insetNewDish(Dish dish) {
        Connection conn = JDBCConnect.JDBCConnector();
        PreparedStatement ps = null;
        String sql = "INSERT INTO `dish`(`name`, `price`, `image`, `category_id`, `average_cost_price`, `deleted`, `sold_out`)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, dish.getName());
            ps.setDouble(2, dish.getPrice());
            String path = dish.getImage().getImage().getUrl();
            File file = new File(path);
            InputStream is = null;
            try {
                is = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                is = new FileInputStream(file.getPath().replaceAll("%20", " ").replaceAll("file:", ""));
            }
            ps.setBlob(3, is);
            ps.setInt(4, dish.getCategory().getId());
            ps.setDouble(5, dish.getAverageCostPrice());
            ps.setBoolean(6, false);
            ps.setBoolean(7, false);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            JDBCConnect.closePreparedStatement(ps);
            JDBCConnect.closeConnection(conn);
        }
        return false;
    }

    public static boolean updateDish(Dish dish) {
        Connection conn = JDBCConnect.JDBCConnector();
        PreparedStatement ps = null;
        String sql = "UPDATE `dish` set `name` = ?, `price` = ?, `category_id` = ?, `average_cost_price` = ?, `deleted` = ?, `sold_out` = ? WHERE `id` = ?";

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, dish.getName());
            ps.setDouble(2, dish.getPrice());
            String path = dish.getImage().getImage().getUrl();
            if (path != null) {
                updateImageDish(dish);
            }
            ps.setInt(3, dish.getCategory().getId());
            ps.setDouble(4, dish.getAverageCostPrice());
            ps.setBoolean(5, dish.isDeleted());
            ps.setBoolean(6, dish.isSoldOut());
            ps.setInt(7, dish.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCConnect.closePreparedStatement(ps);
            JDBCConnect.closeConnection(conn);
        }
        return false;
    }

    public static void updateImageDish(Dish dish) {
        Connection conn = JDBCConnect.JDBCConnector();
        PreparedStatement ps = null;
        String sql = "UPDATE `dish` set `image` = ? WHERE `id` = ?";

        try {
            String path = dish.getImage().getImage().getUrl();
            ps = conn.prepareStatement(sql);
            File file = new File(path);
            InputStream is = null;
            try {
                is = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                is = new FileInputStream(file.getPath().replaceAll("%20", " ").replaceFirst("file:", ""));
            }
            if (is != null) {
                ps.setBlob(1, is);
            }

            ps.setInt(2, dish.getId());
            ps.executeUpdate();
        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            JDBCConnect.closePreparedStatement(ps);
            JDBCConnect.closeConnection(conn);
        }
    }

    public static void updateTotalSold(Dish dish) {
        Connection conn = JDBCConnect.JDBCConnector();
        PreparedStatement ps = null;
        String sql = "UPDATE `dish` set `total_sold` = ? WHERE `id` = ?";

        try {
            String path = dish.getImage().getImage().getUrl();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, dish.getTotalSold());

            ps.setInt(2, dish.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCConnect.closePreparedStatement(ps);
            JDBCConnect.closeConnection(conn);
        }
    }

    public static ObservableList<Dish> getDishList(String search, int curPage, int limit) {
        Connection conn = JDBCConnect.JDBCConnector();
        PreparedStatement ps = null;
        ResultSet rs = null;
        int begin = limit * (curPage - 1);
        String sql = "SELECT * FROM `dish` WHERE `name` like ? LIMIT ?, ?";
        String searchString = "%" + search + "%";
        ObservableList<Dish> dishes = FXCollections.observableArrayList();
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, searchString);
            ps.setInt(2, begin);
            ps.setInt(3, limit);
            rs = ps.executeQuery();
            while (rs.next()) {
                dishes.add(getDish(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCConnect.closeResultSet(rs);
            JDBCConnect.closePreparedStatement(ps);
            JDBCConnect.closeConnection(conn);
        }
        return dishes;
    }
    public static ObservableList<Dish> getDishList(DishCategory dishCategory, String search, int curPage, int limit) {
        Connection conn = JDBCConnect.JDBCConnector();
        PreparedStatement ps = null;
        ResultSet rs = null;
        int begin = limit * (curPage - 1);
        String sql = "SELECT * FROM `dish` WHERE `category_id` = ? and `name` like ? LIMIT ?, ?";
        String searchString = "%" + search + "%";
        ObservableList<Dish> dishes = FXCollections.observableArrayList();
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, dishCategory.getId());
            ps.setString(2, searchString);
            ps.setInt(3, begin);
            ps.setInt(4, limit);
            rs = ps.executeQuery();
            while (rs.next()) {
                dishes.add(getDish(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCConnect.closeResultSet(rs);
            JDBCConnect.closePreparedStatement(ps);
            JDBCConnect.closeConnection(conn);
        }
        return dishes;
    }
    public static ObservableList<Dish> getDishList(DishCategory dishCategory) {
        Connection conn = JDBCConnect.JDBCConnector();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM `dish` WHERE `category_id` = ?";
        ObservableList<Dish> dishes = FXCollections.observableArrayList();
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, dishCategory.getId());
            rs = ps.executeQuery();
            while (rs.next()) {
                dishes.add(getDish(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JDBCConnect.closeResultSet(rs);
            JDBCConnect.closePreparedStatement(ps);
            JDBCConnect.closeConnection(conn);
        }
        return dishes;
    }


    public static int getDishCount(){
        Connection conn = JDBCConnect.JDBCConnector();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT COUNT(`id`) as `dish_count` FROM `dish`";
        int dishCount = 0;
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                dishCount = rs.getInt("dish_count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCConnect.closeResultSet(rs);
            JDBCConnect.closePreparedStatement(ps);
            JDBCConnect.closeConnection(conn);
        }
        return dishCount;
    }
    public static DishCategory getDishCategory(ResultSet rs) {
        DishCategory dishCategory = new DishCategory();
        try {
            dishCategory.setId(rs.getInt("id"));
            dishCategory.setName(rs.getString("name"));
//                dishCategory.setDescription(rs.getString("description"));
//                dishCategory.setDeleted(rs.getBoolean("deleted"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dishCategory;
    }

    public static ObservableList<DishCategory> getDishCategoryList() {
        Connection conn = JDBCConnect.JDBCConnector();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM `dish_category`";
        ObservableList<DishCategory> dishCategories = FXCollections.observableArrayList();
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                dishCategories.add(getDishCategory(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JDBCConnect.closeResultSet(rs);
            JDBCConnect.closePreparedStatement(ps);
            JDBCConnect.closeConnection(conn);
        }
        return dishCategories;
    }

    public static Optional<DishCategory> getDishCategory(int id) {
        Connection conn = JDBCConnect.JDBCConnector();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM `dish_category` WHERE `id` = ?";
        Optional<DishCategory> dishCategory = Optional.empty();
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                dishCategory = Optional.of(getDishCategory(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JDBCConnect.closeResultSet(rs);
            JDBCConnect.closePreparedStatement(ps);
            JDBCConnect.closeConnection(conn);
        }
        return dishCategory;
    }

    public static DinnerTable getDinnerTable(ResultSet rs) {
        DinnerTable dinnerTable = new DinnerTable();
        try {
            dinnerTable.setId(rs.getInt("id"));
            dinnerTable.setName(rs.getString("table_name"));
            dinnerTable.setServingQuantity(rs.getInt("serving_quantity"));
            dinnerTable.setInUse(rs.getBoolean("in_use"));
            dinnerTable.setSurcharge(0);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dinnerTable;
    }

    public static Optional<DinnerTable> getDinnerTable(int id) {
        Connection conn = JDBCConnect.JDBCConnector();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Optional<DinnerTable> dinnerTableOptional = Optional.empty();
        String sql = "SELECT * FROM `dinner_table` " +
                " WHERE `id` = ?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                dinnerTableOptional = Optional.of(getDinnerTable(rs));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCConnect.closeResultSet(rs);
            JDBCConnect.closePreparedStatement(ps);
            JDBCConnect.closeConnection(conn);
        }
        return dinnerTableOptional;
    }

    public static ObservableList<DinnerTable> getDinnerTableList() {
        Connection conn = JDBCConnect.JDBCConnector();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM `dinner_table`";
        ObservableList<DinnerTable> dinnerTables = FXCollections.observableArrayList();
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                dinnerTables.add(getDinnerTable(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JDBCConnect.closeResultSet(rs);
            JDBCConnect.closePreparedStatement(ps);
            JDBCConnect.closeConnection(conn);
        }
        return dinnerTables;
    }

    public static boolean insertNewOrderItem(ArrayList<OrderItem> orderItemArrayList, int orderId) {
        Connection conn = JDBCConnect.JDBCConnector();
        PreparedStatement ps = null;
        String deleteSql = "Delete from `order_item` where `order_id` = ?";
        StringBuilder sql = new StringBuilder("INSERT INTO `order_item`(`order_id`, `dish_id`, `quantity`, `total_money`)" +
                "VALUES (?, ?, ?, ?)");
        boolean flag = false;
        for (int i = 1; i < orderItemArrayList.size(); i++) {
            sql.append(", (?, ?, ?, ?)");
        }
        try {
            ps = conn.prepareStatement(deleteSql);
            ps.setInt(1, orderId);
            ps.executeUpdate();
            ps = conn.prepareStatement(sql.toString());
            for (int i = 0; i < (orderItemArrayList.size()) * 4; i += 4) {
                OrderItem orderItem = orderItemArrayList.get((i / 4));
                ps.setInt(i + 1, orderId);
                ps.setInt(i + 2, orderItem.getDish().getId());
                ps.setInt(i + 3, orderItem.getQuantity());
                ps.setDouble(i + 4, orderItem.getTotalMoney());
            }
            if (ps.executeUpdate() > 0) {
                flag = true;
            } else {
                System.out.println("False");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCConnect.closePreparedStatement(ps);
            JDBCConnect.closeConnection(conn);
        }
        return flag;
    }

    public static boolean insertNewOrder(Order order) {
        Connection conn = JDBCConnect.JDBCConnector();
        PreparedStatement ps = null;
        Boolean flag = false;
        String sql = "INSERT INTO `order`(`table_id`, `total_discount`, `total_money`, `status`, `update_at`)" +
                "VALUE(?, ?, ?, ?, ?)";
        try {
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, order.getTable().getId());
            ps.setDouble(2, 0.0);
            ps.setDouble(3, order.getTotalMoney());
            ps.setBoolean(4, order.isStatus());
            ps.setTimestamp(5, order.getUpdateAt());
            if (ps.executeUpdate() > 0) {
                System.out.println("Create table success!");
                flag = true;
            } else {
                System.out.println("False");
            }
            ResultSet rsGetId = ps.getGeneratedKeys();
            int generatedKey = 0;
            if (rsGetId.next()) {
                generatedKey = rsGetId.getInt(1);
                order.setId(generatedKey);
            }
            insertNewOrderItem(order.getOrderItems(), order.getId());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCConnect.closePreparedStatement(ps);
            JDBCConnect.closeConnection(conn);
        }
        return flag;
    }

    public static boolean updateOrder(Order order) {
        Connection conn = JDBCConnect.JDBCConnector();
        PreparedStatement ps = null;
        Boolean flag = false;
        String sql = "UPDATE `order` set `table_id` = ?, `total_discount` = ?, `total_money` = ?, `status` = ?, `update_at` = ?" +
                "WHERE id = ?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, order.getTable().getId());
            ps.setDouble(2, 0.0);
            ps.setDouble(3, order.getTotalMoney());
            ps.setBoolean(4, order.isStatus());
            ps.setTimestamp(5, order.getUpdateAt());
            ps.setInt(6, order.getId());
            if (ps.executeUpdate() > 0) {
                flag = true;
            } else {
                System.out.println("False");
            }
            insertNewOrderItem(order.getOrderItems(), order.getId());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCConnect.closePreparedStatement(ps);
            JDBCConnect.closeConnection(conn);
        }
        return flag;
    }

    public static Optional<Order> getOrder(int id) {
        Connection conn = JDBCConnect.JDBCConnector();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Optional<Order> orderOptional = Optional.empty();
        String sqlGetOrder = "SELECT * FROM `order` " +
                " WHERE `id` = ?";
        try {
            ps = conn.prepareStatement(sqlGetOrder);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                Order order = new Order();
                order.setId(rs.getInt("id"));
                getDinnerTable(rs.getInt("table_id")).ifPresent(e -> {
                    order.setTable(e);
                });
                order.setOrderItems(getOrderItemFromOrder(order));
                order.setUpdateAt(rs.getTimestamp("update_at"));
                order.setStatus(rs.getBoolean("status"));
                order.setTotalMoney(rs.getDouble("total_money"));
                orderOptional = Optional.of(order);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCConnect.closeResultSet(rs);
            JDBCConnect.closePreparedStatement(ps);
            JDBCConnect.closeConnection(conn);
        }
        return orderOptional;
    }

    public static Optional<Order> getOrderFromTable(DinnerTable dinnerTable) {
        Connection conn = JDBCConnect.JDBCConnector();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Optional<Order> orderOptional = Optional.empty();
        String sqlGetOrder = "SELECT * FROM `order` " +
                " WHERE `table_id` = ? and `status` = 0" +
                " LIMIT 1";

        try {
            ps = conn.prepareStatement(sqlGetOrder);
            ps.setInt(1, dinnerTable.getId());
            rs = ps.executeQuery();
            if (rs.next()) {
                Order order = new Order();
                order.setId(rs.getInt("id"));
                order.setTable(dinnerTable);
                order.setOrderItems(getOrderItemFromOrder(order));
                order.setUpdateAt(rs.getTimestamp("update_at"));
                order.setStatus(rs.getBoolean("status"));
                order.setTotalMoney(rs.getDouble("total_money"));
                orderOptional = Optional.of(order);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCConnect.closeResultSet(rs);
            JDBCConnect.closePreparedStatement(ps);
            JDBCConnect.closeConnection(conn);
        }
        return orderOptional;
    }

    public static ArrayList<OrderItem> getOrderItemFromOrder(Order order) {
        Connection conn = JDBCConnect.JDBCConnector();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<OrderItem> orderItems = new ArrayList<>();
        String sql = "SELECT * FROM `order_item`" +
                "WHERE `order_id` = ?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, order.getId());
            rs = ps.executeQuery();
            while (rs.next()) {
                OrderItem orderItem = new OrderItem();
                orderItem.setId(rs.getInt("id"));
                if (getDish(rs.getInt("dish_id")).isPresent()) {
                    orderItem.setDish(getDish(rs.getInt("dish_id")).get());
                }
                orderItem.setQuantity(rs.getInt("quantity"));
                orderItem.setTotalMoney(rs.getDouble("total_money"));
                orderItems.add(orderItem);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCConnect.closeResultSet(rs);
            JDBCConnect.closePreparedStatement(ps);
            JDBCConnect.closeConnection(conn);
        }
        return orderItems;
    }


    public static void updateTable(DinnerTable dinnerTable) {
        Connection conn = JDBCConnect.JDBCConnector();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "UPDATE `dinner_table` set `in_use` = ? WHERE `id` = ?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setBoolean(1, dinnerTable.isInUse());
            ps.setInt(2, dinnerTable.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCConnect.closePreparedStatement(ps);
            JDBCConnect.closeConnection(conn);
            JDBCConnect.closeConnection(conn);
        }
    }

    public static Optional<Customer> getCustomer(String phone) {
        Connection conn = JDBCConnect.JDBCConnector();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Optional<Customer> customer = Optional.empty();
        String sql = "SELECT * FROM `customer` " +
                " WHERE `phone` = ? ";

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, phone);
            rs = ps.executeQuery();
            if (rs.next()) {
                Customer customerTmp = new Customer();
                customerTmp.setId(rs.getInt("id"));
                customerTmp.setPhone(rs.getString("phone"));
                customerTmp.setMemberLevel(rs.getInt("member_level"));
                customer = Optional.of(customerTmp);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCConnect.closeResultSet(rs);
            JDBCConnect.closePreparedStatement(ps);
            JDBCConnect.closeConnection(conn);
        }
        return customer;
    }

    public static Optional<Customer> getCustomer(int id) {
        Connection conn = JDBCConnect.JDBCConnector();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Optional<Customer> customer = Optional.empty();
        String sql = "SELECT * FROM `customer` " +
                " WHERE `id` = ? ";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                Customer customerTmp = new Customer();
                customerTmp.setId(rs.getInt("id"));
                customerTmp.setPhone(rs.getString("phone"));
                customerTmp.setMemberLevel(rs.getInt("member_level"));
                customer = Optional.of(customerTmp);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCConnect.closeResultSet(rs);
            JDBCConnect.closePreparedStatement(ps);
            JDBCConnect.closeConnection(conn);
        }
        return customer;
    }

    public static boolean insertNewCustomer(Customer customer) {
        Connection conn = JDBCConnect.JDBCConnector();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Boolean flag = false;
        String sql = "INSERT INTO `customer`(name, phone, member_level)" +
                "VALUE(?, ?, ?)";
        try {
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getPhone());
            ps.setInt(3, 0);
            flag = ps.executeUpdate() > 0;
            ResultSet rsGetId = ps.getGeneratedKeys();
            int generatedKey = 0;
            if (rsGetId.next()) {
                generatedKey = rsGetId.getInt(1);
                customer.setId(generatedKey);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCConnect.closePreparedStatement(ps);
            JDBCConnect.closeConnection(conn);
            JDBCConnect.closeConnection(conn);
        }
        return flag;
    }

    public static boolean updateLevelMember(Customer customer) {
        Connection conn = JDBCConnect.JDBCConnector();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Boolean flag = false;
        String sql = "UPDATE `customer` set `member_level` = ? WHERE `id` = ?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, customer.getMemberLevel() + 1);
            ps.setInt(2, customer.getId());
            if (ps.executeUpdate() > 0) {
                flag = true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCConnect.closePreparedStatement(ps);
            JDBCConnect.closeConnection(conn);
            JDBCConnect.closeConnection(conn);
        }
        return flag;
    }

    public static boolean insertNewBill(Bill bill) {
        Connection conn = JDBCConnect.JDBCConnector();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "INSERT INTO `bill`(`bill_number`, `total_discount`, `total_money`, `created_at`, `order_id`, `customer_id`)" +
                "VALUE(?, ?, ?, ?, ?, ?)";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, bill.getBillNumber());
            ps.setDouble(2, bill.getTotalDiscount());
            ps.setDouble(3, bill.getTotalMoney());
            ps.setTimestamp(4, bill.getCreatedAt());
            ps.setInt(5, bill.getOrder().getId());
            int cusId = -1;
            if (bill.getCustomer() != null) {
                cusId = bill.getCustomer().getId();
            }
            ps.setInt(6, cusId);

            return ps.executeUpdate() > 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCConnect.closePreparedStatement(ps);
            JDBCConnect.closeConnection(conn);
            JDBCConnect.closeConnection(conn);
        }
        return false;
    }

    public static Bill getBill(ResultSet rs) {
        Bill bill = new Bill();

        try {
            bill.setId(rs.getInt("id"));
            bill.setBillNumber(rs.getString("bill_number"));
            getCustomer(rs.getInt("customer_id")).ifPresent(bill::setCustomer);
            getOrder(rs.getInt("order_id")).ifPresent(bill::setOrder);
            bill.setTotalDiscount(rs.getDouble("total_discount"));
            bill.setTotalMoney(rs.getDouble("total_money"));
            bill.setCreatedAt(rs.getTimestamp("created_at"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bill;
    }

    public static ObservableList<Bill> getBillList(LocalDate localDate) {
        Connection conn = JDBCConnect.JDBCConnector();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM `bill` WHERE `created_at` > ? and `created_at` < ?";
        ObservableList<Bill> bills = FXCollections.observableArrayList();
        try {
            ps = conn.prepareStatement(sql);
            ps.setTimestamp(1, Timestamp.valueOf(localDate.atStartOfDay()));
            ps.setTimestamp(2, Timestamp.valueOf(localDate.plusDays(1).atStartOfDay()));
            rs = ps.executeQuery();
            while (rs.next()) {
                bills.add(getBill(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JDBCConnect.closeResultSet(rs);
            JDBCConnect.closePreparedStatement(ps);
            JDBCConnect.closeConnection(conn);
        }
        return bills;
    }
}
