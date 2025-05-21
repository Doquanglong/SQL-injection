import java.io.IOException;
import java.nio.file.*;
import java.sql.*;
import java.util.Scanner;

public class SafeLogin {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            // 1. Get input
            System.out.print("Username: ");
            String username = sc.nextLine();
            System.out.print("Password: ");
            String password = sc.nextLine();

            // 2. Read SQL template from file
            String sqlTemplate = new String(Files.readAllBytes(Paths.get("safelogin.sql")));

            // 3. Connect to SQLite DB
            Connection conn = DriverManager.getConnection("jdbc:sqlite:users.db");

            // 4. Prepare SQL query
        
            PreparedStatement sqllogin = conn.prepareStatement(sqlTemplate);
            sqllogin.setString(1, username);
            sqllogin.setString(2, password);

            // 5. Execute query
            ResultSet rs = sqllogin.executeQuery();

            // 6. Check results
            if (rs.next()) {
                System.out.println("Login successful: " + rs.getString("username"));
            } else {
                System.out.println("Login failed.");
            }

            // 7. Clean up
            rs.close();
            sqllogin.close();
            conn.close();

        } catch (IOException e) {
            System.out.println("Error reading SQL file: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }

        sc.close();
    }
}
