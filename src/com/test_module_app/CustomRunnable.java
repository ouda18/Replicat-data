import static java.lang.Thread.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.*;

// classe utilisant une interface Runnable pour effectuer le Thread 
public class CustomRunnable implements Runnable{ 
          // les variables prises en compte par la class EnvoieDeCommnade 
          private javax.swing.JLabel jLabelIMEI1;
          private javax.swing.JTextField jTextFieldDate;
          private javax.swing.JTextField jTextFieldReponse;
          private EnvoieDeCommande oudkaf;
          Connection serverconn; // server 2 en production 
          Connection connCombo; // Server local utiliser pendans le test 
          // il n'a pas ete utilisé 
          PreparedStatement pst5=null; 
          
          Statement pst= null;
          ResultSet rst5 = null;
          
          //Initialisation des variable 
          public void init(javax.swing.JLabel IMEIE, javax.swing.JTextField dateActu, javax.swing.JTextField t_repons, Connection con  ){
          this.jLabelIMEI1 = IMEIE;
          this.jTextFieldDate = dateActu;
          this.jTextFieldReponse = t_repons;
          this.serverconn = con ;
          
          }
          //methode permettant de recupperer la reponse et de l'afficher '
          public void init2(EnvoieDeCommande ouda){
          oudkaf =ouda;
          
          }
          
          // redefinition de la methode run prenant en compte une requete attendant une reponse 
    @Override
    public void run() {
        /*
         int index = 100;
         for(int i = 0; i < index; i++) apres 100 tentative il vas plus ecoute la reponse 
         for(;;) tous le temps in vas ecouter la reponse
        */
        
        //int index = 500;
        try {
            
            for(;;) {
               try {
                   //
                   
              // code SQL utilisé pour ecouter la table de reponse
              String sqlLb = "SELECT * FROM commandemodule where numserie = "+jLabelIMEI1.getText()+" AND ddate ='"+jTextFieldDate.getText()+"' ORDER BY commande DESC LIMIT 1;";
              
              // test durant la phase test
              System.out.println(sqlLb);
              
              pst = this.serverconn.createStatement(rst5.TYPE_SCROLL_SENSITIVE, 0);
              rst5 = pst.executeQuery(sqlLb);
              rst5.beforeFirst();
              while(rst5.next()){
                  // nous posons une condition sur le flage pour obtenir la repon 
                   int reponsflag = rst5.getInt("flag"); // initialisation du flag
                   
                   if (reponsflag == 0) { // quand flag = 0
                       String resert = "";
                    oudkaf.setJTextRepons("Traitement en cours ..."); // affichera dans le jTextFleid
                  }else{ 
                       String reponsStr=  rst5.getString("reponse"); // affichera succes 
                      //oudkaf.setJTextRepons(reponsStr);                   
                      
                      //deh que le flague est a 1 et que la reponse dans la table est à jours  
                      
                      if( reponsStr==" "){
                          oudkaf.setJTextRepons("Aucune repones !!!");
                      }
                      
                        
                       //au cas ou le flag dans le reponse est 01 la reponse est succès
                       String sousReponse2 = reponsStr.substring(14);  // pour les VT202
                       if ((sousReponse2.equals("1")) ) {
                          oudkaf.setJTextRepons(reponsStr +"  "+ "Commande executé mais elle a echoué");
                            
                       } else{
                             oudkaf.setJTextRepons(reponsStr +"  "+ " Commande executé avec succes");
                           
                       }
                       
                         //au cas ou le flag dans le reponse est 01 la reponse est succès
                        String sousReponse = reponsStr.substring(24, 26); // pout les VT310/600/1000/900
                       if((sousReponse.equals("01"))) {
                            oudkaf.setJTextRepons(reponsStr +"  "+ "Commande executé mais elle a echoué");
                      
                             } else{
                           oudkaf.setJTextRepons(reponsStr +"  "+ " Commande executé avec succes");
                          
                       }
                          System.err.println(sousReponse);  
                   break;
                 
                   }
              } 
            
          } catch (SQLException e) {
              Logger.getLogger(EnvoieDeCommande.class.getName()).log(Level.SEVERE, null, e);
          } catch(Exception e){
            // affiche le erreurs d'eception 
              System.err.println(e.getMessage());
          }
               
                // temps accordé au processeur pour se reposer est de 5 s
                sleep(100);
                //break;
                
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void stopThread(){
        
    }
}
















































