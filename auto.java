import java.nio.file.*;
import java.sql.*;

public class auto {
    public static void main(String[] args) {
        // Injection payloads to test
        String[] payloads = {
            "' OR '1'='1",
            "' OR '1'='1' --",
            "' OR '1'='1' /*",
            "' OR 1=1--",
            "' OR 1=1#",
            "admin' --",
            "' OR '' = '",
            "' OR 1=1 LIMIT 1 --"
        };

        try {
            // Load SQL templates
            String safeSql = new String(Files.readAllBytes(Paths.get("safelogin.sql")));
            String vulnSql = new String(Files.readAllBytes(Paths.get("vulnlogin.sql")));

            // Connect to SQLite DB
            Connection conn = DriverManager.getConnection("jdbc:sqlite:users.db");

            for (String payload : payloads) {
                System.out.println("\n=== Testing payload: " + payload + " ===");

                // 🔓 Vulnerable Login
                try {
                    String vulnlogin = vulnSql.replace("{{username}}", payload)
                                              .replace("{{password}}", payload);
                    System.out.println("[VULN] Running: " + vulnlogin);

                    Statement stmt1 = conn.createStatement();
                    ResultSet rs1 = stmt1.executeQuery(vulnlogin);

                    if (rs1.next()) {
                        System.out.println("🚨 Vulnerable login successful: " + rs1.getString("username"));
                    } else {
                        System.out.println("✅ Vulnerable login failed.");
                    }

                    rs1.close();
                    stmt1.close();
                } catch (SQLException e) {
                    System.out.println("⚠️ Error in vulnerable login: " + e.getMessage());
                }

                // 🔐 Safe Login
                try {
                    PreparedStatement stmt2 = conn.prepareStatement(safeSql);
                    stmt2.setString(1, payload);
                    stmt2.setString(2, payload);
                    System.out.println("[SAFE] Executing prepared statement with payload");

                    ResultSet rs2 = stmt2.executeQuery();

                    if (rs2.next()) {
                        System.out.println("🚨 Safe login successful: " + rs2.getString("username"));
                    } else {
                        System.out.println("✅ Safe login failed.");
                    }

                    rs2.close();
                    stmt2.close();
                } catch (SQLException e) {
                    System.out.println("⚠️ Error in safe login: " + e.getMessage());
                }
            }

            conn.close();
        } catch (Exception e) {
            System.out.println("❌ Error setting up: " + e.getMessage());
        }
    }
}
