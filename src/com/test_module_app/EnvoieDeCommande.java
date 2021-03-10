import java.sql.*;
import java.util.Date;
import java.util.logging.*;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.table.*;

/**
 *
 * @author adouj
 */
@SuppressWarnings("InitializerMayBeStatic")
public class EnvoieDeCommande extends javax.swing.JFrame {
    private static final long serialVersionUID = 1L;
           //connection a la dabase ComMod 
          Connection connCombo ;
          Connection serverconn;
          Connection serverconn3;
          
          
          PreparedStatement pst =null,pst2 =null, pst3 =null, pst4=null, pst5=null;
          ResultSet rst = null, rst2 = null, rst3 = null, rst4 = null, rst5 = null;
          
          databaseConnection  dbConn = new databaseConnection();  
           databaseConnection  dbConnMaster = new databaseConnection();
           databaseConnection  dbConnGestafricgasoil = new databaseConnection();
           
           
           // optention de la date 
            Date ddate = null;
           
            
                            
           
          
          

    /**
     * Creates new form EnvoieDeCommande
     */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    // recuperation de la reponse 
    public void setJTextRepons(String reponse){
    this.jTextFieldReponse.setText(reponse);
    }
   
    
   
    public EnvoieDeCommande() {
         this.setSize(600, 400);
       this.setLocationRelativeTo(null);
           initComponents();
          
         // connection a la base de donnee locale
        connCombo = dbConn.databaseConn();
        
        //connection a la base de donnée Master 
        serverconn = dbConnMaster.databaseConnServer2();
        
        //connection a gestafricgasoil
        serverconn3 = dbConnGestafricgasoil.databaseConnServer3();
         
        this.clientComboboxSelect(); 
        
        // la date 
        java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            java.util.Date date = new java.util.Date();
                            String dateActu=formater.format( date );
                            jTextFieldDate.setText(dateActu);
    
    }

    // fonction de selection des clients dans la base de donnée 
    public void clientComboboxSelect(){
        
    
          try {
              
              String SqlSelclients = "SELECT c_nom FROM clients;"; // code SQL utilisable 
              pst = serverconn3.prepareStatement(SqlSelclients);
              rst = pst.executeQuery();
              jComboBoxclients.removeAllItems();
              
              
              while(rst.next()){
                   String clientStr = rst.getString("c_nom");
                   jComboBoxclients.addItem(clientStr);
                   
              }                   
          } catch (SQLException sQLException) {
              //Logger.getLogger(EnvoieDeCommande.class.getName()).log(Level.SEVERE, null, sQLException);
          } catch(Exception sQLException){
              //System.err.println(sQLException.getMessage());
            
          }
          

 }
    
    // fonction affichants les vehicules du clients selectionnés 
    public void chargevehicules(String clients ){

          try {
              // code SQL utilisable 
              String sqlch = "SELECT  v_nom FROM vehicules v JOIN clients  cl on v.v_client = cl.c_id WHERE c_nom = '"+clients+"'";
              
              pst2 = serverconn3.prepareStatement(sqlch);
              rst2 = pst2.executeQuery();
              jComboBoxVehicules.removeAllItems();
              
              
              while(rst2.next()){
                   String vehiculesStr = rst2.getString("v_nom");
                   jComboBoxVehicules.addItem(vehiculesStr);
                   
              }                   
          } catch (SQLException ex) {
              Logger.getLogger(EnvoieDeCommande.class.getName()).log(Level.SEVERE, null, ex);
          } catch(Exception error){
            // affiche le erreurs d'eception 
              //System.err.println(error);
          }
          
          
          

 }
    
    // fonction affichant les infos dans les labaels 
    public void chargeJLabel ( ){

          try {
              // code SQL utilisable 
              String sqlLb = "SELECT mo_type, mo_serie, t_libelle, mo_id  FROM ((clients  cl JOIN  vehicules v on v.v_client = cl.c_id) JOIN  modules mo on v.v_module = mo.mo_id ) JOIN type_module t on mo.mo_type = t.t_id \n"+"WHERE  c_nom = '"+jComboBoxclients.getSelectedItem().toString()+"' AND v_nom = '"+jComboBoxVehicules.getSelectedItem().toString()+"'";
              
              pst3 = serverconn3.prepareStatement(sqlLb);
              rst3 = pst3.executeQuery();
              
             
              
              while(rst3.next()){
                   String vehInfosStr = rst3.getString("mo_serie");
                   
                   jLabelIMEI.setText(vehInfosStr);
                   jLabelIMEI1.setText(vehInfosStr);
                    vehInfosStr = rst3.getString("t_libelle");
                   jLabelTypeBalise.setText(vehInfosStr);
                   
                   // le tableau es imediatement remplis apres afficharge du mo_serie et t_libelle
                  this.chargejTableCommandes(rst3.getInt("mo_type"));
                  
              }                   
          } catch (SQLException ex) {
              Logger.getLogger(EnvoieDeCommande.class.getName()).log(Level.SEVERE, null, ex);
          } catch(Exception error){
            // affiche le erreurs d'eception 
             // System.err.println(error);
          }
          

 }
    
    // fonction affichant la liste des commande en fonction du type de module selectionnés 
     public void chargejTableCommandes(Integer a){
         //public void chargejTableCommandes(){

          try {
              String sqlCom = "SELECT cm_libelle,cm_code, cm_data FROM \"Commandes\" WHERE cm_type_module = "+a;
              pst4 = serverconn3.prepareStatement(sqlCom);
              rst4 = pst4.executeQuery();
              // cette petite  ligne permet de debeurguer  System.out.println(sqlCom);
                 DefaultTableModel dftable =  (DefaultTableModel)jTableCommandes.getModel();
                 jTableCommandes.getModel();
                 dftable.setRowCount(0);
                 
              while(rst4.next()){
                   String commande_libelle = rst4.getString("cm_libelle");
                   String commande_code = rst4.getString("cm_code"); 
                   Boolean data = rst4.getBoolean("cm_data");
                    Object[] obj = {commande_libelle, commande_code, data};
                    dftable.addRow(obj);
             
                   
                
                  
              }                   
          } catch (SQLException sQLException) {
              Logger.getLogger(EnvoieDeCommande.class.getName()).log(Level.SEVERE, null, sQLException);
          } catch(Exception sQLException){
              //ont poura afficher l'erreur 
              //System.err.println(sQLException);
          }
          

 }
   
     // fonction de recuperation de la reponse dans la table commandemodule tous le try sera recopir dans le thred 
     public void chargeReponse() {
          try {
              // code SQL utilisable 
              String sqlLb = "SELECT reponse FROM commandemodule where flag = 1 AND numserie = '"+jLabelIMEI1+"' AND ddate ='"+jTextFieldDate+"' ORDER BY commande DESC LIMIT 1;";
              
              pst5 = serverconn.prepareStatement(sqlLb);
              rst5 = pst5.executeQuery(); 
              while(rst5.next()){
                   String reponsStr = rst5.getString("reponse");
                   jTextFieldReponse.setText(reponsStr);
 
                  
              }                   
          } catch (SQLException sQLException) {
              Logger.getLogger(EnvoieDeCommande.class.getName()).log(Level.SEVERE, null, sQLException);
          } catch(Exception sQLException){
            // affiche le erreurs d'eception 
              //System.err.println(sQLException);
          }
          
     
     }
     
     /* Fonction de conversion 
      *de l'hexa decimal en tableau de Bytes
      */
     public synchronized byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
  
     
/**
 sachant que message doit etre en hexa ont met hexaMessahe a true (booelean)
 * CRC_CCITT_16 
 * le polynome est 1021 
 * le mesage de fin est 0xFFFF
 * /
 */
  /**
     * @param inputStr
     * @param Polynome
     * @param CRC
     * @param HexMessage
     * @return 
 */
    
    public static String CRC_CCITT_16(String inputStr, int Polynome,  int CRC, boolean HexMessage) {
      
    int strLen = inputStr.length();     
    int[] intArray;
    if (HexMessage) {
          if (strLen % 2 != 0) {
             
         inputStr = inputStr.substring(0, strLen - 1) + "0"  + inputStr.substring(strLen - 1, strLen);
                   
            strLen++;
        }

           intArray = new int[strLen / 2];
           int    ctr = 0;
        for (int n = 0; n < strLen; n += 2) {
            intArray[ctr] = Integer.valueOf(inputStr.substring(n, n + 2), 16);
            ctr++;
        }
        
    } else {
        intArray = new int[inputStr.getBytes().length];
        int ctr=0;
        for(byte b : inputStr.getBytes()){
            intArray[ctr] = b;
            ctr++;
        }
         return inputStr;
    }

    // calcule du CRC-CCITT-16 bite tous en respectant le princite du registe à bit 
    for (int b : intArray) {
        for (int i = 0; i < 8; i++) {
            boolean bit = ((b >> (7 - i) & 1) == 1);
            boolean c15 = ((CRC >> 15 & 1) == 1);
            CRC <<= 1;
            if (c15 ^ bit) {
                CRC ^= Polynome;
            }
        }
    }

    CRC &= 0xFFFF;
    return Integer.toHexString(CRC).toUpperCase();
} 
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jMenu1 = new javax.swing.JMenu();
        jPanel = new javax.swing.JPanel();
        jComboBoxclients = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldReponse = new javax.swing.JTextField();
        jComboBoxVehicules = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldcm_code = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabelIMEI = new javax.swing.JLabel();
        jTextFieldDate = new javax.swing.JTextField();
        jLabelTypeBalise = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldData = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabelIMEI1 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableCommandes = new javax.swing.JTable();
        jLabel11 = new javax.swing.JLabel();

        jMenu1.setText("jMenu1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Test Modules");
        setSize(new java.awt.Dimension(400, 350));

        jComboBoxclients.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxclientsItemStateChanged(evt);
            }
        });
        jComboBoxclients.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxclientsActionPerformed(evt);
            }
        });

        jLabel2.setText("Vehicules");

        jTextFieldReponse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldReponseActionPerformed(evt);
            }
        });

        jComboBoxVehicules.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxVehiculesItemStateChanged(evt);
            }
        });
        jComboBoxVehicules.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxVehiculesActionPerformed(evt);
            }
        });

        jLabel7.setText("Le code de cette commande est  :");

        jLabel3.setText("                                                         Commandes ");

        jTextFieldcm_code.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldcm_codeActionPerformed(evt);
            }
        });

        jButton1.setText("ENVOYEZ");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel8.setText("Data");

        jTextFieldDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldDateActionPerformed(evt);
            }
        });

        jLabel9.setText("Date et Heure ");

        jLabel4.setText("IMEI  Module");

        jTextFieldData.setEditable(false);
        jTextFieldData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldDataActionPerformed(evt);
            }
        });

        jLabel5.setText("Type  Module ");

        jLabel6.setText("Reponse");

        jLabel10.setText("Use for checksum");

        jLabel1.setText("Clients");

        jTableCommandes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Libelle commandes ", "Code de la commandes ", "data"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTableCommandes.addContainerListener(new java.awt.event.ContainerAdapter() {
            public void componentAdded(java.awt.event.ContainerEvent evt) {
                jTableCommandesComponentAdded(evt);
            }
        });
        jTableCommandes.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                jTableCommandesAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        jTableCommandes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableCommandesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableCommandes);

        jLabel11.setText("IMEI");

        javax.swing.GroupLayout jPanelLayout = new javax.swing.GroupLayout(jPanel);
        jPanel.setLayout(jPanelLayout);
        jPanelLayout.setHorizontalGroup(
            jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanelLayout.createSequentialGroup()
                        .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jComboBoxVehicules, 0, 209, Short.MAX_VALUE)
                            .addComponent(jComboBoxclients, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelLayout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(52, 52, 52))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelLayout.createSequentialGroup()
                                .addComponent(jTextFieldDate, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(32, 32, 32))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelLayout.createSequentialGroup()
                        .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanelLayout.createSequentialGroup()
                                .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelLayout.createSequentialGroup()
                                        .addGap(8, 8, 8)
                                        .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addGroup(jPanelLayout.createSequentialGroup()
                                                .addComponent(jLabel10)
                                                .addGap(0, 22, Short.MAX_VALUE)))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanelLayout.createSequentialGroup()
                                        .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabelIMEI1, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabelIMEI, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabelTypeBalise, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(35, 35, 35))
                                    .addGroup(jPanelLayout.createSequentialGroup()
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 433, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelLayout.createSequentialGroup()
                                .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanelLayout.createSequentialGroup()
                                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(184, 184, 184)
                                        .addComponent(jTextFieldData, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanelLayout.createSequentialGroup()
                                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jTextFieldcm_code, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(319, 322, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextFieldReponse)))
                        .addContainerGap())))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelLayout.setVerticalGroup(
            jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLayout.createSequentialGroup()
                .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBoxclients, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanelLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(7, 7, 7)
                .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jComboBoxVehicules, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(48, 48, 48)
                .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabelIMEI, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                        .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabelTypeBalise, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelIMEI1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldcm_code, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelLayout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanelLayout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jTextFieldReponse)
                        .addContainerGap())))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBoxclientsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxclientsItemStateChanged
        this.chargevehicules(jComboBoxclients.getSelectedItem().toString());

    }//GEN-LAST:event_jComboBoxclientsItemStateChanged

    private void jComboBoxclientsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxclientsActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_jComboBoxclientsActionPerformed

    private void jTextFieldReponseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldReponseActionPerformed
        // TODO add your handling code here:
        this.chargeReponse();
    }//GEN-LAST:event_jTextFieldReponseActionPerformed

    private void jComboBoxVehiculesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxVehiculesItemStateChanged
        // TODO add your handling code here:
        this.chargeJLabel();
    }//GEN-LAST:event_jComboBoxVehiculesItemStateChanged

    private void jComboBoxVehiculesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxVehiculesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxVehiculesActionPerformed

    private void jTextFieldcm_codeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldcm_codeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldcm_codeActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        /*se code sera rediger en fonction du premier
        *programe realisé par henock utilisant le checksum   en prenant en compte
        * les nouvelles modifications
        */
          
          if((jLabelTypeBalise.getText().equals("VT600")) || (jLabelTypeBalise.getText().equals("VT900")) || (jLabelTypeBalise.getText().equals("VT310"))){ 
          //System.out.println("je fais le clcules des VT600");
           if ((jLabelIMEI.getText().length() < 14)){
                      int a = 14-  jLabelIMEI.getText().length();
                      int i;
                      for (i=0; i<a; i++){
                      jLabelIMEI.setText(jLabelIMEI.getText()+"F");
                  }
           }
                     // pour calculer la longeur de la chaine  
                  String chaine = "40400011"+jLabelIMEI.getText()+jTextFieldcm_code.getText()+jTextFieldData.getText()+"FFFF0D0A";
                  int Longueur_chaine  = chaine.length()/2;
                  //conversion de la chaine et hexa et mise en grand caractere 
                  String valeur_Longueur_chaine=Integer.toHexString(Longueur_chaine).toUpperCase();
                 
                  //afficharge pour le déburguing 
                  System.err.println("La longueur de cette chaine est:"+Longueur_chaine+"="+ valeur_Longueur_chaine);
       
                   // A reprensente toute la chaine sans le byte de fin 0D0A
                  String A= ("404000"+valeur_Longueur_chaine+jLabelIMEI.getText()+jTextFieldcm_code.getText()+jTextFieldData.getText().toUpperCase());
                  // le byte de fin 
                  String B = "0D0A";
               // le checksum se calcule uniquement avec le A @@+L+ID+data+4103    
           String checkSum = CRC_CCITT_16(A, 0x1021, 0xFFFF, true);
       
       String memessageAndChecksum = (A + checkSum + B).toUpperCase();
      
      String messageFinal = memessageAndChecksum;
         System.out.println("Le Checksum est :"+checkSum+" "+A);
        System.out.println("le message a envoyer dans la table commnademodule est :"+messageFinal);
        JOptionPane.showMessageDialog(null, "Ce message"+" "+messageFinal+" "+" sera envoyé à:"+jLabelIMEI1.getText());
                     // Enregistrement de la commande dans la base dans la table commandemodule 
         
             try {
                  // optention de la date  directement dans la base de donnée 
                     java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            java.util.Date date = new java.util.Date();
                            String dateActu=formater.format( date );
                   
                  // permet de recuperer le libelle de la commande 
                  TableModel model = jTableCommandes.getModel(); 
                  
                  // Initialise le message 
                   // String messageFinal ;
                  
                  // Scrip sql permetant de recuperer la reponse dans la table commandemodule
                   String sqlAddcommandmodule = "INSERT INTO commandemodule (numserie,typemodule,ddate,commande,reponse,flag)"+"VALUES(?,?,'"+jTextFieldDate.getText()+"','"+messageFinal+"','',0)";
                 
                   PreparedStatement ps = serverconn.prepareStatement(sqlAddcommandmodule);
                   //debeuggin System.err.println(sqlAddcommandmodule);
                   
                   ps.setDouble(1,Double.parseDouble(jLabelIMEI1.getText())); // IMEI
                   ps.setString(2,jLabelTypeBalise.getText()); // type de module
                   
                   ps.executeUpdate(); //ajoute les information de cette requete dans la base 
                   JOptionPane.showMessageDialog(null, "Message envoyé ...  ");
          
                    CustomRunnable customRunnable = new CustomRunnable();
                     //start avec initilisation des varibles
                     customRunnable.init(jLabelIMEI1, jTextFieldDate, jTextFieldReponse, serverconn);
                     customRunnable.init2(this);
                     new Thread(customRunnable ).start();
                    
                    // Tanque la repons  est num affiche : wait please ...
                             } catch (SQLException e) {
                  //System.out.println(e.getMessage());    
             }
             
           
         } else {
             // System.out.println("je fais le clcules des VT202");
              // variable invariante 
                      String header = "TRV";
                      String ending = "#";
                      
                  // recuperation des 6 dernier caractere du jLabelIMEI1
                  String  dernierChiffreImei = jLabelIMEI1.getText();
                    int n = 6; // nbre de caractères
                    int length = dernierChiffreImei.length();
                   String resultPourMsg = dernierChiffreImei.substring(length -n, length);
                   
                   // Formulation deu message 
                   String messageFinalVt202 = header+jTextFieldcm_code.getText()+resultPourMsg+jTextFieldData.getText()+ending;
                   String messageFinal = messageFinalVt202;
                      System.err.println("les  6 derniers chiffre de l'IMEI de ce module sont :"+ resultPourMsg);
                      System.out.println("le message a envoyer dans la table commnademodule est :"+messageFinal);
                     JOptionPane.showMessageDialog(null, "Ce message"+" "+messageFinal+" "+" sera envoyé à:"+jLabelIMEI1.getText());
                     // Enregistrement de la commande dans la base dans la table commandemodule 
                     
                      try {
                          
                          
                            // optention de la date  directement dans la base de donnée 
                              java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            java.util.Date date = new java.util.Date();
                            String dateActu=formater.format( date );
                   
                            // permet de recuperer le libelle de la commande 
                             TableModel model = jTableCommandes.getModel(); 
                  
                  
                  // Scrip sql permetant de recuperer la reponse dans la table commandemodule
                  
                   String sqlAddcommandmodule = "INSERT INTO commandemodule (numserie,typemodule,ddate,commande,reponse,flag)"+"VALUES(?,?,'"+jTextFieldDate.getText()+"','"+messageFinal+"','',0)";
                 
                   PreparedStatement ps = serverconn.prepareStatement(sqlAddcommandmodule);
                   //debeuggin System.err.println(sqlAddcommandmodule);
                   
                   ps.setDouble(1,Double.parseDouble(jLabelIMEI1.getText())); // IMEI
                   ps.setString(2,jLabelTypeBalise.getText()); // type de module
                   
                   ps.executeUpdate(); //ajoute les information de cette requete dans la base 
                   JOptionPane.showMessageDialog(null, "Message envoyé ...  ");
          
                    CustomRunnable customRunnable = new CustomRunnable();
                     //start avec initilisation des varibles
                     customRunnable.init(jLabelIMEI1, jTextFieldDate, jTextFieldReponse, serverconn);
                     customRunnable.init2(this);
                     new Thread(customRunnable ).start();
                    
                    // Tanque la repons  est num affiche : wait please ...
                             } catch (SQLException e) {
                  //System.out.println(e.getMessage());
                          
                   
                  }     
                
                      
          }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextFieldDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldDateActionPerformed

        //this.jTextFieldDate(ddate);

    }//GEN-LAST:event_jTextFieldDateActionPerformed

    private void jTextFieldDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldDataActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldDataActionPerformed

    private void jTableCommandesComponentAdded(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_jTableCommandesComponentAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_jTableCommandesComponentAdded

    private void jTableCommandesAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_jTableCommandesAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_jTableCommandesAncestorAdded

    private void jTableCommandesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableCommandesMouseClicked

        // selection du code de la commande
        int a = jTableCommandes.getSelectedRow();
        TableModel model = jTableCommandes.getModel();
        jTextFieldcm_code.setText(model.getValueAt(a, 1).toString());

        // cette connection permet de saisir la DATA dans le jTextFieldData
        if (model.getValueAt(a, 2).toString().compareTo("true")==0){
            jTextFieldData.setEditable(true);

        }

    }//GEN-LAST:event_jTableCommandesMouseClicked
       
    /**
     * Fonction principale du programme EnvoiDeCommande0
     * @param args the command line arguments
     */
    public static void main(String args[]) throws UnsupportedLookAndFeelException {
             /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(EnvoieDeCommande.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EnvoieDeCommande.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EnvoieDeCommande.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EnvoieDeCommande.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
                UIManager.setLookAndFeel(new NimbusLookAndFeel());
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EnvoieDeCommande().setVisible(true);  
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<String> jComboBoxVehicules;
    private javax.swing.JComboBox<String> jComboBoxclients;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelIMEI;
    private javax.swing.JLabel jLabelIMEI1;
    private javax.swing.JLabel jLabelTypeBalise;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JPanel jPanel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTableCommandes;
    private javax.swing.JTextField jTextFieldData;
    private javax.swing.JTextField jTextFieldDate;
    private javax.swing.JTextField jTextFieldReponse;
    private javax.swing.JTextField jTextFieldcm_code;
    // End of variables declaration//GEN-END:variables

}
