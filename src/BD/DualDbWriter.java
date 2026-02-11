package BD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DualDbWriter {

    public static final class Result {
        public boolean sqliteOk;
        public boolean mysqlOk;
        public Exception sqliteError;
        public Exception mysqlError;
    }

    @FunctionalInterface
    public interface SqlBinder {
        void bind(PreparedStatement ps) throws SQLException;
    }

    public static Result executeUpdate(String sql, SqlBinder binder) {
        return executeUpdate(sql, binder, binder);
    }

    public static Result executeUpdate(String sql, SqlBinder sqliteBinder, SqlBinder mysqlBinder) {
        Result result = new Result();

        try (Connection sqlite = Connect.open();
             PreparedStatement ps = sqlite.prepareStatement(sql)) {
            sqliteBinder.bind(ps);
            ps.executeUpdate();
            result.sqliteOk = true;
        } catch (Exception e) {
            result.sqliteOk = false;
            result.sqliteError = e;
        }

        try (Connection mysql = MySqlConnect.open();
             PreparedStatement ps = mysql.prepareStatement(sql)) {
            mysqlBinder.bind(ps);
            ps.executeUpdate();
            result.mysqlOk = true;
        } catch (Exception e) {
            result.mysqlOk = false;
            result.mysqlError = e;
        }

        return result;
    }

    public static Integer resolveResguardanteIdByName(Connection conn, String nombre) {
        if (nombre == null || nombre.isEmpty() || "Seleciona un Resguardante".equals(nombre)) {
            return null;
        }
        final String sqlFind = "SELECT id FROM datos_resguardante WHERE nombre = ? LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sqlFind)) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException ex) {
            System.err.println("resolveResguardanteIdByName error: " + ex.getMessage());
        }
        return null;
    }
}
