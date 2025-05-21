import java.io.IOException;            // For JDBC (Connection, Statement, ResultSet)
import java.nio.file.*;     // For user input
import java.sql.*;       // For reading the SQL file
import java.util.Scanner;   // For handling file read errors

public class VulnerableLogin {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            // 1. Get input
            System.out.print("Username: ");
            String username = sc.nextLine();
            System.out.print("Password: ");
            String password = sc.nextLine();

            // 2. Read SQL template from file
            String sqlTemplate = new String(Files.readAllBytes(Paths.get("vulnlogin.sql")));

            // 3. Replace placeholders with user input
            String sqllogin = sqlTemplate.replace("{{username}}", username)
                                         .replace("{{password}}", password);

            System.out.println("Running: " + sqllogin); // Show final query

            // 4. Connect to SQLite DB
            Connection conn = DriverManager.getConnection("jdbc:sqlite:users.db");

            // 5. Create and execute SQL query
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqllogin);

            // 6. Check results
            if (rs.next()) {
                System.out.println("Login successful: " + rs.getString("username"));
            } else {
                System.out.println("Login failed.");
            }

            // 7. Clean up
            rs.close();
            stmt.close();
            conn.close();

        } catch (IOException e) {
            System.out.println("Error reading SQL file: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }

        sc.close();
    }
}
