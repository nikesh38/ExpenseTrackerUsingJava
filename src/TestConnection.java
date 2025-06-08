import util.DBConnection;
import java.sql.*;

public class TestConnection {
    public static void main(String[] args) {
        System.out.println("Testing MySQL Connection...");

        // Check if driver is available
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ MySQL Driver found!");
        } catch (ClassNotFoundException e) {
            System.out.println("❌ MySQL Driver NOT found!");
            return;
        }

        try {
            Connection conn = DBConnection.getConnection();
            System.out.println("✅ Database connection successful!");

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT 1");
            if (rs.next()) {
                System.out.println("✅ Query test successful!");
            }

            conn.close();
        } catch (SQLException e) {
            System.out.println("❌ Database connection failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}