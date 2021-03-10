import java.sql.*;
import java.util.logging.*;
import javax.swing.*;


/**
 *
 * @author adouj
 */
public class databaseConnection {
    //variable de connetion local
    Connection Conn = null;
    String url = "jdbc:postgresql:";
    String user = "postgres";
    String password = "sq";
// fonction de connection loclae 
    public Connection databaseConn(){
     
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(databaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            Conn = DriverManager.getConnection(url, user, password);
           // JOptionPane.showMessageDialog(null, "Vous etre connecté à Commode");
        } catch (SQLException ex) {
            Logger.getLogger(databaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch(Exception error){
            // affiche le erreurs d'eception 
              System.err.println(error);
          }
        
         return Conn;
        
    }
    
    // variable de connection au server 2
    Connection Conn_server_2 = null;
    String url_server_2 = "jdbc:postgresql:";
    String user_server_2 = "postgres";
    String password_server_2 = "sq";
    
    // fonction de connection au serveur 2
    public Connection databaseConnServer2(){
     
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ex) {
            //Logger.getLogger(databaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            Conn_server_2 = DriverManager.getConnection(url_server_2, user_server_2, password_server_2);
            //JOptionPane.showMessageDialog(null, "Connexion Master succdes ");
        } catch (SQLException ex) {
            Logger.getLogger(databaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch(Exception error){
            // affiche le erreurs d'eception 
              System.err.println(error);
          }
        
         return Conn_server_2;
        
    }
    
    // variable de connection au server 3
    Connection Conn_server_3 = null;
    String url_server_3 = "jdbc:postgresql:";
    String user_server_3 = "postgres";
    String password_server_3 = "Tk@Oh2017#";
    
    
    // fonction de connection au serveur 3
    public Connection databaseConnServer3(){
     
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ex) {
            //Logger.getLogger(databaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            Conn_server_3 = DriverManager.getConnection(url_server_3, user_server_3, password_server_3);
            // affichera qu'il ya eu connection au Master et GESTAFRICGAZOIL
            JOptionPane.showMessageDialog(null, "Connexion Master and GESTAFRICGAZOIL succes");
        } catch (SQLException ex) {
            Logger.getLogger(databaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch(Exception error){
            // affiche le erreurs d'eception 
              //System.err.println(error);
          }
        
         return Conn_server_3;
        
    }
//  fonction principale 
    /*
    public static void main(String[] arg){
       
        // premier connection en locale pendant la phase test 
     databaseConnection connDatabase = new databaseConnection();
     connDatabase.databaseConn();
     
     //seconde connection au Master
     databaseConnection databaseConnServer= new databaseConnection();
     databaseConnServer.databaseConnServer2();
     
     //seconde connection au GESTAFRICGAZOIL
     databaseConnection ConnServerdatabase= new databaseConnection();
     ConnServerdatabase.databaseConnServer3();
    
    } 
*/
}
