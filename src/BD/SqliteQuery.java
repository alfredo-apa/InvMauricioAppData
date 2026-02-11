/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BD;

/**
 *
 * @author barce
 */

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class SqliteQuery {

    final ArrayList<String> pass = new ArrayList<String>(){{add("Polaris-19");add("Zaramirez11");add("Cuniadito77");}};

    public SqliteQuery() {
        //this.pass = new ArrayList[]{, "Zaramirez11"};
        //pass.add("Polaris-19");
        //pass.add("Zaramirez11");
        //pass.add("Cuniadito77");
    }

    public void Query(String q){

        try(var conn = BD.Connect.open()) {
            var stmt = conn.createStatement();


            stmt.executeQuery(q);

        }catch (Exception e){
            System.out.println(e);
        }
    }

    public Boolean getPass(String p) {
        //String s = "";
        
        //for(String s1 : pass) s= s+ s1 +" ";
        
        //return pass.get(0);
        return pass.contains(p);
    }
    
    
    
    public void dbConnect(){
        try (var conn = BD.Connect.open()) {
            //JOptionPane.showMessageDialog(null,"db");
            if (conn != null) {
                var meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
                //JOptionPane.showMessageDialog(null,"db00");

            }else {
                System.out.println("Connected");
                //JOptionPane.showMessageDialog(null,"db0");
            }
            createdbTable();
        } catch (SQLException e) {
            System.out.println("2");
            System.err.println(e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void createdbTable(){
        //var url = "jdbc:sqlite:src/BD/invmauricio.db";

        // SQL statement for creating a new table
        /*var sql = "CREATE TABLE IF NOT EXISTS registro ("
                + "	id INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + "	folio varchar(50) NOT NULL,"
                + "	fecha date,"
                + " beneficiario text,"
                + " banco text,"
                + " cuenta text,"
                + " cantidad double,"
                + " concepto text,"
                + " formulo text,"
                + " revisor text,"
                + " autorizo text"
                + ");";*/
        var sqlresg = "CREATE TABLE IF NOT EXISTS datos_resguardante ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "nombre VARCHAR(120) NOT NULL," 
                    + "departamento VARCHAR(120) NOT NULL," 
                    + "direccion VARCHAR(200) NULL," 
                    + "cargo VARCHAR(120) NULL," 
                    + "ubicacion_fisica VARCHAR(200) NULL," 
                    + "extension VARCHAR(20)  NULL," 
                    + "correo VARCHAR(180) NULL)" ;
                    //+ "PRIMARY KEY (id))";
        var sqlgab = "CREATE TABLE IF NOT EXISTS hardware_gabinete ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "resguardante INT NULL,"                    //-- → datos_resguardante.id" 
                    + "marca VARCHAR(80) NULL," 
                    + "modelo VARCHAR(120) NULL," 
                    + "no_inventario VARCHAR(60) NULL," 
                    + "numero_serie VARCHAR(120) NULL," 
                    + "componentes VARCHAR(255) NULL,"
                    + "status VARCHAR(255) NULL,"
                    + "observaciones TEXT NULL," 
                    //+ "PRIMARY KEY (id),"
                    //+ "KEY idx_gabinete_resguardante (resguardante),"
                    //+ "CONSTRAINT fk_gabinete_resguardante"
                    + "FOREIGN KEY (resguardante)" 
                    + "REFERENCES datos_resguardante(id))";
                    //+ "ON UPDATE CASCADE"
                    //+ "ON DELETE SET NULL)";

        try (var conn = BD.Connect.open();
             var stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sqlresg);
            stmt.execute(sqlgab);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.err.println(Arrays.toString(e.getStackTrace()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public void insertOnResguardante(String[] data){
        String sql = "INSERT INTO datos_resguardante(nombre, departamento, direccion, cargo, ubicacion_fisica) VALUES(?,?,?,?,?)";
        // TODO: Re-enable DualDbWriter (SQLite + MySQL) workflow when ready.
        /*
        BD.DualDbWriter.Result res = BD.DualDbWriter.executeUpdate(
                sql,
                ps -> {
                    ps.setString(1, data[0]);
                    ps.setString(2, data[1]);
                    ps.setString(3, data[2]);
                    ps.setString(4, data[3]);
                    ps.setString(5, data[4]);
                }
        );

        if (res.sqliteOk) {
            JOptionPane.showMessageDialog(null,"Proceso Terminado");
            if (!res.mysqlOk && res.mysqlError != null) {
                JOptionPane.showMessageDialog(null, "MySQL no guardado: " + res.mysqlError.getMessage());
            }
        } else if (res.sqliteError != null) {
            JOptionPane.showMessageDialog(null, res.sqliteError);
        }
        */

        try (var conn = BD.Connect.open();
             var pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, data[0]);
            pstmt.setString(2, data[1]);
            pstmt.setString(3, data[2]);
            pstmt.setString(4, data[3]);
            pstmt.setString(5, data[4]);

            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null,"Proceso Terminado");

        } catch (Exception e) {
            System.out.println("dbFuntions");
            System.err.println(e.getMessage());
            System.err.println(e.getStackTrace().toString());
        }

    }
    
    public void insertOnGabinete(String[] data){
        String sql = "INSERT INTO hardware_gabinete(resguardante, marca, modelo, no_inventario, numero_serie, componentes, status, observaciones) VALUES(?,?,?,?,?,?,?,?)";
        // TODO: Re-enable DualDbWriter (SQLite + MySQL) workflow when ready.
        /*
        Integer sqliteResguardanteId = null;
        Integer mysqlResguardanteId = null;
        try (var conn = BD.Connect.open()) {
            sqliteResguardanteId = BD.DualDbWriter.resolveResguardanteIdByName(conn, data[0]);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        try (var conn = BD.MySqlConnect.open()) {
            mysqlResguardanteId = BD.DualDbWriter.resolveResguardanteIdByName(conn, data[0]);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "MySQL lookup error: " + e.getMessage());
        }

        BD.DualDbWriter.Result res = BD.DualDbWriter.executeUpdate(
                sql,
                ps -> bindGabinete(ps, sqliteResguardanteId, data),
                ps -> bindGabinete(ps, mysqlResguardanteId, data)
        );

        if (res.sqliteOk) {
            JOptionPane.showMessageDialog(null,"Proceso Terminado");
            if (!res.mysqlOk && res.mysqlError != null) {
                JOptionPane.showMessageDialog(null, "MySQL no guardado: " + res.mysqlError.getMessage());
            }
        } else if (res.sqliteError != null) {
            JOptionPane.showMessageDialog(null, res.sqliteError);
        }
        */

        try (var conn = BD.Connect.open();
             var pstmt = conn.prepareStatement(sql)) {

            Integer resguardanteId = resolveResguardanteIdByName(conn, data[0]);
            if (resguardanteId == null) {
                pstmt.setNull(1, java.sql.Types.INTEGER);
            } else {
                pstmt.setInt(1, resguardanteId);
            }
            pstmt.setString(2, data[1]);
            pstmt.setString(3, data[2]);
            pstmt.setString(4, data[3]);
            pstmt.setString(5, data[4]);
            pstmt.setString(6, data[5]);
            pstmt.setString(7, data[6]);
            pstmt.setString(8, data[7]);

            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null,"Proceso Terminado");

        } catch (Exception e) {
            System.out.println("dbFuntions");
            System.err.println(e.getMessage());
            System.err.println(e.getStackTrace().toString());
        }

    }

    public java.util.List<String> listInventarios() throws Exception {
        java.util.ArrayList<String> inventarios = new java.util.ArrayList<>();
        final String sql =
                "SELECT DISTINCT TRIM(no_inventario) AS no_inventario " +
                "FROM hardware_gabinete " +
                "WHERE no_inventario IS NOT NULL AND TRIM(no_inventario) <> '' " +
                "ORDER BY no_inventario";
        try (var conn = BD.Connect.open();
             var stmt = conn.createStatement();
             var rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String inv = rs.getString("no_inventario");
                if (inv != null && !inv.isBlank()) {
                    inventarios.add(inv.trim());
                }
            }
        }
        return inventarios;
    }

    public java.util.List<String> listResguardanteNamesWithInventario() throws Exception {
        java.util.ArrayList<String> nombres = new java.util.ArrayList<>();
        final String sql =
                "SELECT DISTINCT TRIM(r.nombre) AS nombre " +
                "FROM hardware_gabinete h " +
                "JOIN datos_resguardante r ON r.id = h.resguardante " +
                "WHERE r.nombre IS NOT NULL AND TRIM(r.nombre) <> '' " +
                "AND h.no_inventario IS NOT NULL AND TRIM(h.no_inventario) <> '' " +
                "ORDER BY nombre";
        try (var conn = BD.Connect.open();
             var stmt = conn.createStatement();
             var rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String nombre = rs.getString("nombre");
                if (nombre != null && !nombre.isBlank()) {
                    nombres.add(nombre.trim());
                }
            }
        }
        return nombres;
    }

    public java.util.List<String> listInventariosByResguardanteName(String nombre) throws Exception {
        java.util.ArrayList<String> inventarios = new java.util.ArrayList<>();
        if (nombre == null || nombre.isBlank()) {
            return inventarios;
        }
        final String sql =
                "SELECT DISTINCT TRIM(h.no_inventario) AS no_inventario " +
                "FROM hardware_gabinete h " +
                "JOIN datos_resguardante r ON r.id = h.resguardante " +
                "WHERE TRIM(r.nombre) = ? " +
                "AND h.no_inventario IS NOT NULL AND TRIM(h.no_inventario) <> '' " +
                "ORDER BY no_inventario";
        try (var conn = BD.Connect.open();
             var ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre.trim());
            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    String inv = rs.getString("no_inventario");
                    if (inv != null && !inv.isBlank()) {
                        inventarios.add(inv.trim());
                    }
                }
            }
        }
        return inventarios;
    }

    public GabineteRecord getGabineteByInventario(String inventario) throws Exception {
        if (inventario == null || inventario.isBlank()) {
            return null;
        }
        final String sql =
                "SELECT h.id, h.resguardante, h.marca, h.modelo, h.no_inventario, h.numero_serie, " +
                "h.componentes, h.status, h.observaciones, r.nombre " +
                "FROM hardware_gabinete h " +
                "LEFT JOIN datos_resguardante r ON r.id = h.resguardante " +
                "WHERE TRIM(h.no_inventario) = ? " +
                "LIMIT 1";
        try (var conn = BD.Connect.open();
             var ps = conn.prepareStatement(sql)) {
            ps.setString(1, inventario.trim());
            try (var rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                return new GabineteRecord(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("marca"),
                        rs.getString("modelo"),
                        rs.getString("no_inventario"),
                        rs.getString("numero_serie"),
                        rs.getString("componentes"),
                        rs.getString("status"),
                        rs.getString("observaciones")
                );
            }
        }
    }

    public void updateGabinete(int gabineteId,
                               String resguardanteNombre,
                               String marca,
                               String modelo,
                               String noInventario,
                               String numeroSerie,
                               String componentes,
                               String status,
                               String observaciones) throws Exception {
        final String sqlUpdate =
                "UPDATE hardware_gabinete " +
                "SET resguardante = ?, marca = ?, modelo = ?, no_inventario = ?, numero_serie = ?, " +
                "componentes = ?, status = ?, observaciones = ? " +
                "WHERE id = ?";
        try (var conn = BD.Connect.open();
             var ps = conn.prepareStatement(sqlUpdate)) {
            Integer resguardanteId = resolveResguardanteIdByName(conn, resguardanteNombre);
            if (resguardanteId == null) {
                ps.setNull(1, java.sql.Types.INTEGER);
            } else {
                ps.setInt(1, resguardanteId);
            }
            ps.setString(2, marca);
            ps.setString(3, modelo);
            ps.setString(4, noInventario);
            ps.setString(5, numeroSerie);
            ps.setString(6, componentes);
            ps.setString(7, status);
            ps.setString(8, observaciones);
            ps.setInt(9, gabineteId);
            ps.executeUpdate();
        }
    }

    public static class GabineteRecord {
        public final int id;
        public final String resguardanteNombre;
        public final String marca;
        public final String modelo;
        public final String noInventario;
        public final String numeroSerie;
        public final String componentes;
        public final String status;
        public final String observaciones;

        public GabineteRecord(int id,
                              String resguardanteNombre,
                              String marca,
                              String modelo,
                              String noInventario,
                              String numeroSerie,
                              String componentes,
                              String status,
                              String observaciones) {
            this.id = id;
            this.resguardanteNombre = resguardanteNombre;
            this.marca = marca;
            this.modelo = modelo;
            this.noInventario = noInventario;
            this.numeroSerie = numeroSerie;
            this.componentes = componentes;
            this.status = status;
            this.observaciones = observaciones;
        }
    }

    // TODO: Restore helper for DualDbWriter binding when DualDbWriter is re-enabled.
    /*
    private void bindGabinete(java.sql.PreparedStatement ps, Integer resguardanteId, String[] data) throws SQLException {
        if (resguardanteId == null) {
            ps.setNull(1, java.sql.Types.INTEGER);
        } else {
            ps.setInt(1, resguardanteId);
        }
        ps.setString(2, data[1]);
        ps.setString(3, data[2]);
        ps.setString(4, data[3]);
        ps.setString(5, data[4]);
        ps.setString(6, data[5]);
        ps.setString(7, data[6]);
        ps.setString(8, data[7]);
    }
    */

    /**
     * Looks up the datos_resguardante.id by the given name.
     * Returns null if the name is null, blank, or not found.
     */
    private Integer resolveResguardanteIdByName(java.sql.Connection conn, String nombre) {
        if (nombre == null || nombre.isEmpty() || "Seleciona un Resguardante".equals(nombre)) {
            return null;
        }

        final String sqlFind = "SELECT id FROM datos_resguardante WHERE nombre = ? LIMIT 1";
        try (var ps = conn.prepareStatement(sqlFind)) {
            ps.setString(1, nombre);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (java.sql.SQLException ex) {
            System.err.println("resolveResguardanteIdByName error: " + ex.getMessage());
        }
        return null;
    }
    
    
    /*public void insertOnTable(String folio, String fecha, String beneficiario, String banco, String cuenta, double monto, String concepto, String formulo, String revisor, String autorizo){
        //var names = new String[] {"Raw Materials", "Semifinished Goods", "Finished Goods"};
        //var capacities = new int[] {3000,4000,5000};

        String sql = "INSERT INTO registro(folio, fecha, beneficiario, banco, cuenta, cantidad, concepto, formulo, revisor, autorizo) VALUES(?,?,?,?,?,?,?,?,?,?)";

        try (var conn = BD.Connect.open();
             var pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1,folio);
            pstmt.setString(2,fecha);
            pstmt.setString(3,beneficiario);
            pstmt.setString(4,banco);
            pstmt.setString(5,cuenta);
            pstmt.setDouble(6,monto);
            pstmt.setString(7,concepto);
            pstmt.setString(8,formulo);
            pstmt.setString(9,revisor);
            pstmt.setString(10,autorizo);

            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null,"Proceso Terminado");

        /*    for(int i = 0; i < 3; i++){
                pstmt.setString(1, names[i]);
                pstmt.setDouble(2, capacities[i]);
                pstmt.executeUpdate();
            }
        /
        } catch (SQLException e) {
            System.out.println("dbFuntions");
            System.err.println(e.getMessage());
            System.err.println(e.getStackTrace().toString());
        }

    }*/
}
