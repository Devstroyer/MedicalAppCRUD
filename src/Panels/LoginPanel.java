package Panels;

import Database.DatabaseConnection;
import com.sun.glass.events.KeyEvent;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.input.KeyCode;
import javax.swing.JFrame;

public class LoginPanel extends javax.swing.JPanel {
    
    private NewJFrame jf;

    public LoginPanel(NewJFrame main) {
        this.jf= main;
        initComponents();  
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPasswordField1 = new javax.swing.JPasswordField();
        jToggleButton1 = new javax.swing.JToggleButton();
        jLabel4 = new javax.swing.JLabel();

        jLabel1.setText("Login");

        jLabel2.setText("Hasło");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Logowanie");

        jToggleButton1.setText("Zaloguj");
        jToggleButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jToggleButton1MouseClicked(evt);
            }
        });
        jToggleButton1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jToggleButton1KeyPressed(evt);
            }
        });

        jLabel4.setForeground(new java.awt.Color(180, 50, 50));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(165, 165, 165)
                .addComponent(jToggleButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(168, 168, 168))
            .addGroup(layout.createSequentialGroup()
                .addGap(121, 121, 121)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField1)
                    .addComponent(jPasswordField1)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(119, 119, 119))
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addGap(28, 28, 28)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(jToggleButton1)
                .addContainerGap(93, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jToggleButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jToggleButton1MouseClicked
       login();
    }//GEN-LAST:event_jToggleButton1MouseClicked

    private void jToggleButton1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jToggleButton1KeyPressed
        if(evt.getKeyCode()== KeyEvent.VK_ENTER){
            login();
        }      
    }//GEN-LAST:event_jToggleButton1KeyPressed

    private void login(){
        DatabaseConnection dbc = DatabaseConnection.getInstance();
              
        if(jTextField1.getText().length()>0 && String.valueOf(jPasswordField1.getPassword()).length()>0){          
            String username=jTextField1.getText();
            String hashedPassword;
            char[]password=jPasswordField1.getPassword();
            hashedPassword=hashPassword(password);
            if(dbc.tryLogin(username,hashedPassword)){
                jf.logged();
            }
            else{
                jLabel4.setText("Podano złe dane logowania");
            }
        }
        else{
            jLabel4.setText("Wprowadź dane");
        }       
    }
    
    private String hashPassword(char[] password){
        MessageDigest cript;
        String hashedPassword="";
        try {
            cript = MessageDigest.getInstance("SHA-1");
            cript.update(String.valueOf(password).getBytes("utf8"));
            byte[] digestBytes = cript.digest();
            hashedPassword = javax.xml.bind.DatatypeConverter.printHexBinary(digestBytes);
        } catch (Exception ex) {
            Logger.getLogger(LoginPanel.class.getName()).log(Level.SEVERE, null, ex);
        }           
        return hashedPassword;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JToggleButton jToggleButton1;
    // End of variables declaration//GEN-END:variables
}
