package repository;

import JDBC.JDBCConnect;
import entity.Dish;
import entity.Staff;
import entity.StaffPosition;
import entity.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.DateTimeUtil;
import util.SHA256;

import java.sql.*;
import java.util.Optional;

public class StaffRepository {

    public static Boolean addUser(String username, String password, Staff staff){
        Connection conn = JDBCConnect.JDBCConnector();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Boolean flag = false;
        String sql = "INSERT INTO `user`(`username`, `password`, `staff_id`, `is_manager`)" +
                "VALUES(?, ?, ?, ?)";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, SHA256.getSHA256(password));
            ps.setInt(3, staff.getId());
            ps.setBoolean(4, (staff.getStaffPosition().getId() == 1));
            if (ps.executeUpdate() > 0) {
                flag = true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCConnect.closePreparedStatement(ps);
            JDBCConnect.closeConnection(conn);
        }

        return flag;
    }
    public static User getUser(String username, String password) {
        Connection conn = JDBCConnect.JDBCConnector();
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = null;
        String sql = "SELECT * FROM `user` WHERE `username` = ? and `password` = ?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, username.trim());
            ps.setString(2, password.trim());
            rs = ps.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                System.out.println(1);
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setStaffId(rs.getInt("staff_id"));
                user.setActive(rs.getBoolean("is_active"));
                user.setManager(rs.getBoolean("is_manager"));
//                user.setCreatedAt(new DateTimeUtil().getDate(rs.getString("create_at")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCConnect.closeResultSet(rs);
            JDBCConnect.closePreparedStatement(ps);
            JDBCConnect.closeConnection(conn);
        }
        return user;
    }
    public static boolean getUser(String username) {
        Connection conn = JDBCConnect.JDBCConnector();
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = null;
        String sql = "SELECT * FROM `user` WHERE `username` = ?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, username.trim());
            rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCConnect.closeResultSet(rs);
            JDBCConnect.closePreparedStatement(ps);
            JDBCConnect.closeConnection(conn);
        }
        return false;
    }public static boolean getUser(Staff staff) {
        Connection conn = JDBCConnect.JDBCConnector();
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = null;
        String sql = "SELECT * FROM `user` WHERE `staff_id` = ?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, staff.getId());
            rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCConnect.closeResultSet(rs);
            JDBCConnect.closePreparedStatement(ps);
            JDBCConnect.closeConnection(conn);
        }
        return false;
    }

    public static Staff getStaff(ResultSet rs) throws SQLException {
        Staff staff = new Staff();
        staff.setId(rs.getInt("id"));
        staff.setName(rs.getString("name"));
        staff.setBirthDate(rs.getDate("birth_date"));
        staff.setPhone(rs.getString("phone"));
        staff.setAddress(rs.getString("address"));
        staff.setEmail(rs.getString("email"));
        staff.setStaffPosition(getStaffPosition(rs.getInt("staff_position_id")));
        staff.setStartWorkedAt(rs.getDate("started_work_at"));
        staff.setDeleted(rs.getBoolean("deleted"));
        return staff;
    }

    public static Optional<Staff> getStaffFromUser(User user) {
        Connection conn = JDBCConnect.JDBCConnector();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Optional<Staff> staffOptional = Optional.empty();
        String sql = "SELECT * FROM `staff` WHERE `id` = ?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, user.getStaffId());
            rs = ps.executeQuery();
            if (rs.next()) {
                staffOptional = Optional.of(getStaff(rs));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCConnect.closeResultSet(rs);
            JDBCConnect.closePreparedStatement(ps);
            JDBCConnect.closeConnection(conn);
        }
        return staffOptional;
    }

    public static Boolean updateUser(User user) {
        Connection conn = JDBCConnect.JDBCConnector();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Boolean flag = false;
        String sql = "UPDATE user SET `password` = ? WHERE `id` = ?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, SHA256.getSHA256(user.getPassword().trim()));
            ps.setInt(2, user.getStaffId());
            if (ps.executeUpdate() > 0) {
                flag = true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCConnect.closePreparedStatement(ps);
            JDBCConnect.closeConnection(conn);
        }
        return flag;
    }

    public static Boolean updateStaff(Staff staff) {
        Connection conn = JDBCConnect.JDBCConnector();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Boolean flag = false;
        String sql = "UPDATE staff SET `phone` = ?, `email` = ? WHERE `id` = ?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, staff.getPhone());
            ps.setString(2, staff.getEmail());
            ps.setInt(3, staff.getId());
            if (ps.executeUpdate() > 0) {
                flag = true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCConnect.closePreparedStatement(ps);
            JDBCConnect.closeConnection(conn);
        }

        return flag;
    }
    public static Boolean updateFullStaff(Staff staff) {
        Connection conn = JDBCConnect.JDBCConnector();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Boolean flag = false;
        String sql = "UPDATE `staff` SET `name` = ?, `birth_date` = ?, `phone` = ?, `email` = ?, `address` = ?, `staff_position_id` = ? WHERE `id` = ?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, staff.getName());
            ps.setDate(2, staff.getBirthDate());
            ps.setString(3, staff.getPhone());
            ps.setString(4, staff.getEmail());
            ps.setString(5, staff.getAddress());
            ps.setInt(6, staff.getStaffPosition().getId());
            ps.setInt(7, staff.getId());
            if (ps.executeUpdate() > 0) {
                flag = true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCConnect.closePreparedStatement(ps);
            JDBCConnect.closeConnection(conn);
        }

        return flag;
    }

    public static boolean addStaff(Staff staff){
        Connection conn = JDBCConnect.JDBCConnector();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Boolean flag = false;
        String sql = "INSERT INTO `staff`(`name`, `birth_date`, `phone`, `email`, `address`, `staff_position_id`, `started_work_at`)" +
                "VALUES(?, ?, ?, ?, ?, ?, ?)";
        try {
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, staff.getName());
            ps.setDate(2, staff.getBirthDate());
            ps.setString(3, staff.getPhone());
            ps.setString(4, staff.getEmail());
            ps.setString(5, staff.getAddress());
            ps.setInt(6, staff.getStaffPosition().getId());
            Date date = new Date(new java.util.Date().getTime());
            ps.setDate(7, date);
            if (ps.executeUpdate() > 0) {
                flag = true;
            }
            ResultSet rsGetId = ps.getGeneratedKeys();
            int generatedKey = 0;
            if (rsGetId.next()) {
                generatedKey = rsGetId.getInt(1);
                staff.setId(generatedKey);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCConnect.closePreparedStatement(ps);
            JDBCConnect.closeConnection(conn);
        }

        return flag;
    }
    public static int getCountStaff(){
        Connection conn = JDBCConnect.JDBCConnector();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT COUNT(`id`) as `staff_count` FROM `staff`";
        int staffCount = 0;
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                staffCount = rs.getInt("staff_count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCConnect.closeResultSet(rs);
            JDBCConnect.closePreparedStatement(ps);
            JDBCConnect.closeConnection(conn);
        }
        return staffCount;
    }
    public static StaffPosition getStaffPosition(int id) {
        Connection conn = JDBCConnect.JDBCConnector();
        PreparedStatement ps = null;
        ResultSet rs = null;
        StaffPosition staffPosition = new StaffPosition();
        String sql = "SELECT * FROM `staff_position` WHERE `id` = ?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            rs = ps.executeQuery();
            if (rs.next()) {
                staffPosition.setId(rs.getInt("id"));
                staffPosition.setName(rs.getString("name"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCConnect.closeResultSet(rs);
            JDBCConnect.closePreparedStatement(ps);
            JDBCConnect.closeConnection(conn);
        }
        return staffPosition;
    }

    public static ObservableList<StaffPosition> getStaffPosition() {
        Connection conn = JDBCConnect.JDBCConnector();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ObservableList<StaffPosition> staffPositions= FXCollections.observableArrayList();
        String sql = "SELECT * FROM `staff_position`";
        try {
            ps = conn.prepareStatement(sql);

            rs = ps.executeQuery();
            while (rs.next()) {
                StaffPosition staffPosition = new StaffPosition();
                staffPosition.setId(rs.getInt("id"));
                staffPosition.setName(rs.getString("name"));
                staffPositions.add(staffPosition);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCConnect.closeResultSet(rs);
            JDBCConnect.closePreparedStatement(ps);
            JDBCConnect.closeConnection(conn);
        }
        return staffPositions;
    }

    public static ObservableList<Staff> getStaffList(String search, int curPage, int limit) {
        Connection conn = JDBCConnect.JDBCConnector();
        PreparedStatement ps = null;
        ResultSet rs = null;
        int begin = limit * (curPage - 1);
        String sql = "SELECT * FROM `staff` WHERE `name` like ? LIMIT ?, ?";
        String searchString = "%" + search + "%";
        ObservableList<Staff> staff = FXCollections.observableArrayList();
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, searchString);
            ps.setInt(2, begin);
            ps.setInt(3, limit);
            rs = ps.executeQuery();
            while (rs.next()) {
                staff.add(getStaff(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCConnect.closeResultSet(rs);
            JDBCConnect.closePreparedStatement(ps);
            JDBCConnect.closeConnection(conn);
        }
        return staff;
    }

}
