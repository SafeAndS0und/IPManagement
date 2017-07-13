import javax.xml.crypto.Data;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.sql.*;

public class IPManagement {

    private static Connection con;
    private static PreparedStatement prepstmt;
    private static PreparedStatement prepstmt2;
    private static ResultSet rs;

    public static void addAddress(String ipaddress) {

        con = Database.connect();
        String query = "INSERT INTO addresses (ipaddress, name)" +
                "VALUES (?,?)";

        try {
            String adres = InetAddress.getByName(ipaddress).getHostAddress();
            String nazwa = InetAddress.getByName(adres).getHostName();
            prepstmt = con.prepareStatement(query);
            prepstmt.setString(1, adres);
            prepstmt.setString(2, nazwa);
            prepstmt.execute();
        } catch (Exception e) {
            System.out.println(e);
        }


    }

    public static void checkConnection(int addressID, int delay) {
        con = Database.connect();
        String query = "INSERT INTO connection (delay, isReachable, time, addressID, reachableAt)" +
                "VALUES (?,?,?,?,?)";

        String query2 = "SELECT * FROM addresses WHERE addresses.ID=? ";

        try {

            while (true) {
                prepstmt2 = con.prepareStatement(query2);
                prepstmt2.setInt(1, addressID);
                rs = prepstmt2.executeQuery();
                String adres;

                if (rs.next()) {
                    adres = rs.getString("addresses.ipaddress");
                    boolean isReachable = InetAddress.getByName(adres).isReachable(delay);
                    int reachableAt = 0;

                    //petla wykonujaca polecenie PING
                    for (int i = 1; i < 300; i++) {
                        boolean isTrue = InetAddress.getByName(adres).isReachable(i);
                        if (isTrue) {
                            reachableAt = i;
                            break;
                        }
                    }

                    Timestamp date = new Timestamp(new java.util.Date().getTime());
                    prepstmt = con.prepareStatement(query);
                    prepstmt.setInt(1, delay);
                    prepstmt.setString(2, isReachable ? "YES" : "NO");
                    prepstmt.setTimestamp(3, date);
                    prepstmt.setInt(4, addressID);
                    prepstmt.setInt(5, reachableAt);
                    prepstmt.execute();


                } else {
                    throw new RuntimeException("Adres nie istnieje");
                }

                Thread.sleep(3500);
            }


        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public static void truncateConnection() {
        con = Database.connect();
        String query = "TRUNCATE connection";

        try{
            prepstmt = con.prepareStatement(query);
            prepstmt.execute();
        }catch (Exception e){
            System.out.println(e);
        }
    }




}
