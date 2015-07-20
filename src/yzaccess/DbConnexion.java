/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yzaccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DbConnexion {

    private String DBPath = "";
    private Connection connection = null;

    public DbConnexion(String dBPath) {
        DBPath = dBPath;
    }

    public void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + DBPath);
            Log.log("OK >> Connexion a " + DBPath + " avec succès");
        } catch (ClassNotFoundException notFoundException) {
            notFoundException.printStackTrace();
            Log.log("Erreur de connexion à " + DBPath + "(ClassNotFoundException)");
            new CrashError("Impossible de se connecter à la base de données", notFoundException, null);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            Log.log("Erreur de connecxion à " + DBPath + "SQLException");
            new CrashError("Impossible de se connecter à la base de données", sqlException, null);
        }
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void populateBillet(Billet billet, String code) {
        ResultSet rs = query(""
                + "SELECT * FROM tb_billet "
                + "WHERE tt_codebarre='" + code + "'");
        int id = 0;
        
        try {
            while (rs.next()) {
                billet.exists = true;
                billet.codeBarre = code;

                billet.pk = rs.getInt("pk_billet");
                id = billet.pk;
                if (rs.getInt("in_valide") == 1) {
                    billet.valide = true;
                }
                billet.type = rs.getString("tt_type");
            }
        } catch (Exception e) {
            e.printStackTrace();
            new CrashError("Erreur populate billet : " + e.getMessage(), null, null);
        }
        
        ResultSet rs2 = query(""
                + "SELECT * FROM tb_scan "
                + "WHERE fk_billet=" + id + " "
                + "ORDER BY dt_datetime DESC");
        
        try {
            while (rs2.next()) {
                BilletHistory bh = new BilletHistory();
                bh.datetime = rs2.getString("dt_datetime");
                bh.type = rs2.getString("tt_type");
                billet.history.add(bh);
            }
        } catch (Exception e) {
            e.printStackTrace();
            new CrashError("Erreur populate billet : " + e.getMessage(), null, null);
        }
    }

    public void populateBillet(Billet billet, int pk) {
        ResultSet rs = query(""
                + "SELECT * FROM tb_billet "
                + "WHERE pk_billet=" + pk + "");
        int id = 0;
        
        try {
            while (rs.next()) {
                billet.exists = true;
                billet.pk = pk;
                id = billet.pk;
                
                billet.codeBarre = rs.getString("tt_codebarre");
                if (rs.getInt("in_valide") == 1) {
                    billet.valide = true;
                }
                billet.type = rs.getString("tt_type");
            }
        } catch (Exception e) {
            e.printStackTrace();
            new CrashError("Erreur populate billet : " + e.getMessage(), null, null);
        }
        
        ResultSet rs2 = query(""
                + "SELECT * FROM tb_scan "
                + "WHERE fk_billet=" + id + " "
                + "ORDER BY dt_datetime DESC");
        
        try {
            while (rs2.next()) {
                BilletHistory bh = new BilletHistory();
                bh.datetime = rs2.getString("dt_datetime");
                bh.type = rs2.getString("tt_type");
                billet.history.add(bh);
            }
        } catch (Exception e) {
            e.printStackTrace();
            new CrashError("Erreur populate billet : " + e.getMessage(), null, null);
        }
    }
        

    public void setScanned(int billet, String type) {
        String txtDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.FRANCE).format(new Date());
        String txtDay = new SimpleDateFormat("dd", Locale.FRANCE).format(new Date());
        String txtHour = new SimpleDateFormat("HH", Locale.FRANCE).format(new Date());
        
        String query = "INSERT INTO tb_scan (fk_billet, tt_type, dt_datetime, in_day, in_hour) VALUES (" + Integer.toString(billet) + ",'" + type + "', '" + txtDate + "', " + txtDay +", " + txtHour + ")";
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    

    public static int getCount(ResultSet rs) {
        int rowNum = 0;
        try {
            while (rs.next()) {
                rowNum++;
            }
        } catch (Exception ex) {
        }
        return rowNum;
    }

    ////////////////////////////////////////////////////////////////////////////
    

    //USED
    public ResultSet getBilletByNumero(String numero) {
        ResultSet rs = query(""
                + "SELECT * FROM tb_billet "
                + "WHERE in_numero='" + numero + "'");

        return rs;
    }

    public ResultSet getHistorique(int id) {
        ResultSet rs = query(""
                + "SELECT * FROM tb_scan "
                + "WHERE fk_billet=" + id + " "
                + "ORDER BY dt_datetime DESC");

        return rs;
    }

    public ResultSet getSearched(String numero, String datecommande, String code, String numerocommande, String client) {

        System.out.println("NUMERO : " + numero);

        String query = "SELECT *, (SELECT COUNT(*) AS nb FROM tb_scan WHERE fk_billet=pk_billet) AS checks FROM tb_billet WHERE 1=1";

        boolean v = false;

        if (numero.length() > 0) {
            v = true;
            query += " AND in_numero='" + numero + "'";
        }

        if (code.length() > 0) {
            v = true;
            query += " AND tt_codebarre LIKE '%" + code + "%'";
        }

        if (numerocommande.length() > 0) {
            v = true;
            query += " AND in_numero_commande='" + numerocommande + "'";
        }

        if (client.length() > 0) {
            v = true;
            query += " AND tt_client LIKE '%" + client + "%'";
        }

        if (!v) {
            query += " AND 1=0";
        }

        System.out.print(query);

        ResultSet rs = query(query);
        return rs;
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

    private static String protect(String string) {
        return string.replaceAll("'", " ");
    }

    private ResultSet query(String requet) {

        ResultSet resultat = null;
        try {
            Statement statement = connection.createStatement();
            resultat = statement.executeQuery(requet);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur dans la requete : " + requet);
        }
        return resultat;

    }

    private boolean queryWithoutResult(String requet) {

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(requet);

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur dans la requete : " + requet);

            return false;
        }
    }

}
