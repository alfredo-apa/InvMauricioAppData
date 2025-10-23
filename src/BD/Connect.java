package BD;

import javax.swing.*;
import java.io.*;
import java.nio.file.*;
import java.sql.*;

public class Connect {

    private static final String RESOURCE_DB = "BD/invmauricio.db";
    private static Path appDbPath;

    /** Cross-platform app data directory */
    public static Path getAppDataDir() {
        String os = System.getProperty("os.name").toLowerCase();
        Path base;
        if (os.contains("win")) {
            String localAppData = System.getenv("LOCALAPPDATA");
            base = (localAppData != null)
                    ? Paths.get(localAppData)
                    : Paths.get(System.getProperty("user.home"), "AppData", "Local");
        } else if (os.contains("mac")) {
            base = Paths.get(System.getProperty("user.home"), "Library", "Application Support");
        } else {
            base = Paths.get(System.getProperty("user.home"), ".local", "share");
        }
        // Top folder InvMNB, then InvMauricio
        return base.resolve("InvMNB").resolve("InvMauricio");
    }

    /** Copy template DB from JAR into AppData if it doesn't exist yet. */
    public static synchronized Path ensureWritableDb() throws IOException {
        if (appDbPath != null) return appDbPath;
        Path appDir = getAppDataDir();
        Files.createDirectories(appDir);
        appDbPath = appDir.resolve("invmauricio.db");

        if (Files.notExists(appDbPath)) {
            try (InputStream in = Connect.class.getClassLoader().getResourceAsStream(RESOURCE_DB)) {
                if (in == null) {
                    // If no template inside JAR, create an empty DB file
                    Files.createFile(appDbPath);
                } else {
                    Files.copy(in, appDbPath);
                }
            }
        }
        return appDbPath;
    }

    /** Open a writable SQLite connection in AppData, enabling safe pragmas. */
    public static Connection open() throws Exception {
        Path db = ensureWritableDb();
        String url = "jdbc:sqlite:" + db.toAbsolutePath();
        Connection conn = DriverManager.getConnection(url);
        try (Statement st = conn.createStatement()) {
            st.execute("PRAGMA journal_mode=WAL");
            st.execute("PRAGMA busy_timeout=5000");
            st.execute("PRAGMA foreign_keys=ON");
        }
        return conn;
    }

    /** Optional quick test */
    public static void connect() {
        try (var conn = open()) {
            JOptionPane.showMessageDialog(null, "Conectado a: " + getAppDataDir());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.toString(), "DB error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        connect();
    }
}
