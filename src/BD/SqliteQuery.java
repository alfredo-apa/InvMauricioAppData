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
        try (var conn = BD.Connect.open();
             var pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1,data[0]);
            pstmt.setString(2,data[1]);
            pstmt.setString(3,data[2]);
            pstmt.setString(4,data[3]);
            pstmt.setString(5,data[4]);

            pstmt.executeUpdate();
            
            JOptionPane.showMessageDialog(null,"Proceso Terminado");

        /*    for(int i = 0; i < 3; i++){
                pstmt.setString(1, names[i]);
                pstmt.setDouble(2, capacities[i]);
                pstmt.executeUpdate();
            }
        */
        } catch (SQLException e) {
            System.out.println("dbFuntions");
            System.err.println(e.getMessage());
            System.err.println(e.getStackTrace().toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    
    public void insertOnGabinete(String[] data){
        String sql = "INSERT INTO hardware_gabinete(resguardante, marca, modelo, no_inventario, numero_serie, componentes, status, observaciones) VALUES(?,?,?,?,?,?,?,?)";
        try (var conn = BD.Connect.open();
             var stmt = conn.createStatement();
             var pstmt = conn.prepareStatement(sql)) {

            ResultSet chk = stmt.executeQuery("SELECT id FROM datos_resguardante where nombre='"+data[0]+"'");
            //chk.getRow();
            int id = chk.getInt(1);
            System.out.println(id);

            pstmt.setInt(1,id);
            pstmt.setString(2,data[1]);
            pstmt.setString(3,data[2]);
            pstmt.setString(4,data[3]);
            pstmt.setString(5,data[4]);
            pstmt.setString(6,data[5]);
            pstmt.setString(7,data[6]);
            pstmt.setString(8,data[7]);

            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null,"Proceso Terminado");

        /*    for(int i = 0; i < 3; i++){
                pstmt.setString(1, names[i]);
                pstmt.setDouble(2, capacities[i]);
                pstmt.executeUpdate();
            }
        */
        } catch (SQLException e) {
            System.out.println("dbFuntions");
            System.err.println(e.getMessage());
            System.err.println(e.getStackTrace().toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

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
