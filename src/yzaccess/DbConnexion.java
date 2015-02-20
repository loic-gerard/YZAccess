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
	    Log.log("Erreur de connecxion à " + DBPath);
	} catch (SQLException sqlException) {
	    sqlException.printStackTrace();
	    Log.log("Erreur de connecxion à " + DBPath);
	}
    }

    public void close() {
	try {
	    connection.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }

    

    public void setScanned(int billet, String type) {
	String txtDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.FRANCE).format(new Date());

	String query = "INSERT INTO tb_scan (fk_billet, tt_type, dt_datetime) VALUES (" + Integer.toString(billet) + ",'" + type + "', '" + txtDate + "')";
	try {
	    Statement statement = connection.createStatement();
	    statement.executeUpdate(query);
	} catch (SQLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }


    /*
     * Retourne :
     * 1 : valide (GREEN)
     * 2 : déjà scanné aujourd'hui (RED)
     * 3 : non valide aujourd'hui (RED)
     * 4 : billet annulé (RED)
     * 5 : billet introuvable (RED)
     * 6 : erreur inconnue (RED)
     */
    public int isBilletValide(int billet) {
	//Voir si valide aujourd'hui et trouvable
	ResultSet rsb = getBillet(billet);

	try {
	    rsb.next();
	    //billet invalide
	    if (rsb.getInt("in_valide") == 0) {
		return 4;   //Billet invalide
	    }

	    String typebillet = rsb.getString("tt_codetypebillet");
	    if (!Config.isTypeBilletAccepte(typebillet)){
		return 3;
	    }
	    
	} catch (Exception e) {
	    e.printStackTrace();
	    return 6;	//Erreur inconnue
	}

	//Voir si déjà scanné aujourd'hui
	String query = "SELECT * FROM tb_scan AS s "
		+ "JOIN tb_billet AS b ON b.pk_billet = s.fk_billet "
		+ "WHERE fk_billet = " + billet;

	ResultSet rs = query(query);
	int rc = getCount(rs);

	if (rc > 0) {
	    return 2; //Déjà scanné aujourd'hui
	}

	return 1; //OK !

    }

    public ResultSet getBillet(String code) {
	ResultSet rs = query(""
		+ "SELECT * FROM tb_billet "
		+ "WHERE tt_codebarre='" + code + "'");


	return rs;
    }

    public ResultSet getBillet(int id) {
	ResultSet rs = query(""
		+ "SELECT * FROM tb_billet "
		+ "WHERE pk_billet=" + id + "");

	return rs;
    }


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
