package dao;
import model.Expense;
import util.DBConnection;
import java.sql.*;
import java.util.*;

public class ExpenseDAO {
    public void addExpense(Expense e) throws SQLException {
        String query = "INSERT INTO expenses (amount, category, description, date) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDouble(1, e.getAmount());
            stmt.setString(2, e.getCategory());
            stmt.setString(3, e.getDescription());
            stmt.setDate(4, new java.sql.Date(e.getDate().getTime()));
            stmt.executeUpdate();
        }
    }

    public List<Expense> getAllExpenses() throws SQLException {
        List<Expense> list = new ArrayList<>();
        String query = "SELECT * FROM expenses ORDER BY date DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Expense e = new Expense(
                        rs.getDouble("amount"),
                        rs.getString("category"),
                        rs.getString("description"),
                        rs.getDate("date")
                );
                e.setId(rs.getInt("id"));
                list.add(e);
            }
        }
        return list;
    }

    public void updateExpense(Expense e) throws SQLException {
        String query = "UPDATE expenses SET amount = ?, category = ?, description = ?, date = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDouble(1, e.getAmount());
            stmt.setString(2, e.getCategory());
            stmt.setString(3, e.getDescription());
            stmt.setDate(4, new java.sql.Date(e.getDate().getTime()));
            stmt.setInt(5, e.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteExpense(int id) throws SQLException {
        String query = "DELETE FROM expenses WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Expense> getExpensesByCategory(String category) throws SQLException {
        List<Expense> list = new ArrayList<>();
        String query = "SELECT * FROM expenses WHERE category LIKE ? ORDER BY date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "%" + category + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Expense e = new Expense(
                        rs.getDouble("amount"),
                        rs.getString("category"),
                        rs.getString("description"),
                        rs.getDate("date")
                );
                e.setId(rs.getInt("id"));
                list.add(e);
            }
        }
        return list;
    }

    // Fixed: Explicitly specify java.util.Date for parameters
    public List<Expense> getExpensesByDateRange(java.util.Date startDate, java.util.Date endDate) throws SQLException {
        List<Expense> list = new ArrayList<>();
        String query = "SELECT * FROM expenses WHERE date BETWEEN ? AND ? ORDER BY date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDate(1, new java.sql.Date(startDate.getTime()));
            stmt.setDate(2, new java.sql.Date(endDate.getTime()));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Expense e = new Expense(
                        rs.getDouble("amount"),
                        rs.getString("category"),
                        rs.getString("description"),
                        rs.getDate("date")
                );
                e.setId(rs.getInt("id"));
                list.add(e);
            }
        }
        return list;
    }

    public List<String> getAllCategories() throws SQLException {
        List<String> categories = new ArrayList<>();
        String query = "SELECT DISTINCT category FROM expenses ORDER BY category";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                categories.add(rs.getString("category"));
            }
        }
        return categories;
    }
}