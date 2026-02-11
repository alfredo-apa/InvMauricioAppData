package BD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlConnect {

    private static String envOrProp(String key, String fallback) {
        String v = System.getenv(key);
        if (v != null && !v.isBlank()) {
            return v;
        }
        v = System.getProperty(key);
        if (v != null && !v.isBlank()) {
            return v;
        }
        return fallback;
    }

    public static Connection open() throws Exception {
        String host = envOrProp("MYSQL_HOST", "localhost");
        String port = envOrProp("MYSQL_PORT", "3306");
        String db = envOrProp("MYSQL_DB", "invmauricio");
        String user = envOrProp("MYSQL_USER", null);
        String pass = envOrProp("MYSQL_PASSWORD", "");

        if (user == null || user.isBlank()) {
            throw new SQLException("MySQL user not set. Configure MYSQL_USER and MYSQL_PASSWORD.");
        }

        String url = "jdbc:mysql://" + host + ":" + port + "/" + db
                + "?useSSL=false&serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8";
        return DriverManager.getConnection(url, user, pass);
    }
}
