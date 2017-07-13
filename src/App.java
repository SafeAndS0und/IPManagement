import java.net.InetAddress;

public class App {
    public static void main(String[] args) throws Exception {

        IPManagement.truncateConnection();
        IPManagement.checkConnection(4, 15);

    }
}
