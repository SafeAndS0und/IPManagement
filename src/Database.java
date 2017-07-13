import java.sql.Connection;
import java.sql.DriverManager;

public class Database {

    private static Connection con;

    public static Connection connect() {
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/connection", "student", "student");
            return con;

        } catch (Exception e) {
            System.out.println(e);
        }
        return con;
    }
}
