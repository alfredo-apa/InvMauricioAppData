package invmauricio;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

public class MacAddressUtil {

    
    public static String getMacAddress() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            NetworkInterface networkInterface = NetworkInterface.getByInetAddress(localHost);

            if (networkInterface == null) {
                return "Network interface not found.";
            }

            byte[] macAddressBytes = networkInterface.getHardwareAddress();

            if (macAddressBytes == null) {
                return "MAC address not available.";
            }

            StringBuilder macAddressStr = new StringBuilder();
            for (int i = 0; i < macAddressBytes.length; i++) {
                macAddressStr.append(String.format("%02X%s", macAddressBytes[i],
                        (i < macAddressBytes.length - 1) ? "-" : ""));
            }

            return macAddressStr.toString();

        } catch (UnknownHostException | SocketException e) {
            return "Error retrieving MAC address: " + e.getMessage();
        }
    }

    // Optional: for quick testing
    /*public static void main(String[] args) {
        System.out.println("MAC Address: " + getMacAddress());
    }*/
}
