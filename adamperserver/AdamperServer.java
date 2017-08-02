package adamperserver;

import java.util.*;
import java.io.*;
import java.net.*;

/**
 *
 * @author adamp
 */
public class AdamperServer extends javax.swing.JFrame {

  /**
   * @param args the command line arguments
   */
  public static void main(String args[]) {
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
      java.util.logging.Logger.getLogger(AdamperServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(AdamperServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(AdamperServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(AdamperServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new AdamperServer().setVisible(true);
      }
    });
  }

  /**
   * Creates new form AdamperServer
   */
  public AdamperServer() {
    initComponents();
  }

  public void appendToScreen(String message) {
    mainTextArea.append(message);
  }

  public void sendToAllUsers(String message) {
    Iterator it = outputStreams.iterator();

    while (it.hasNext()) {
      try {
        PrintWriter writer = (PrintWriter) it.next();
        writer.println(message);
        mainTextArea.append("Sending: " + message + "\n");
        writer.flush();
        mainTextArea.setCaretPosition(mainTextArea.getDocument().getLength());

      } catch (Exception ex) {
        mainTextArea.append("Error telling everyone. \n");
      }
    }
  }

  public void initialiseUsersList() {
    usersList = new ArrayList();
  }

  public void addUser(String data) {
    String message, add = ": :Connect", done = "Server: :Done", name = data;
    mainTextArea.append("Before " + name + " added. \n");
    usersList.add(name);
    mainTextArea.append("After " + name + " added. \n");
    String[] tempList = new String[(usersList.size())];
    usersList.toArray(tempList);

    for (String token : tempList) {
      message = (token + add);
      sendToAllUsers(message);
    }
    sendToAllUsers(done);
  }

  public void removeUser(String data) {
    String message, add = ": :Connect", done = "Server: :Done", name = data;
    usersList.remove(name);
    String[] tempList = new String[(usersList.size())];
    usersList.toArray(tempList);

    for (String token : tempList) {
      message = (token + add);
      sendToAllUsers(message);
    }
    sendToAllUsers(done);
  }

  public void initialiseOutputStreams() {
    outputStreams = new ArrayList();
  }

  public void addUserToOutputStreams(PrintWriter user) {
    outputStreams.add(user);
  }

  public void removeUserFromOutputStreams(PrintWriter user) {
    outputStreams.remove(user);
  }

  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    startServerBtn = new javax.swing.JButton();
    stopServerBtn = new javax.swing.JButton();
    jScrollPane1 = new javax.swing.JScrollPane();
    mainTextArea = new javax.swing.JTextArea();
    clearScreenBtn = new javax.swing.JButton();
    displayAllUsersBtn = new javax.swing.JButton();
    messageToAllTextField = new javax.swing.JTextField();
    sendToAllBtn = new javax.swing.JButton();
    messageToAllLabel = new javax.swing.JLabel();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setTitle("Server - Adamper");

    startServerBtn.setText("Uruchom serwer");
    startServerBtn.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        startServerBtnActionPerformed(evt);
      }
    });

    stopServerBtn.setText("Zatrzymaj serwer");
    stopServerBtn.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        stopServerBtnActionPerformed(evt);
      }
    });

    mainTextArea.setEditable(false);
    mainTextArea.setColumns(20);
    mainTextArea.setRows(5);
    jScrollPane1.setViewportView(mainTextArea);

    clearScreenBtn.setText("Wyczyść ekran");
    clearScreenBtn.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        clearScreenBtnActionPerformed(evt);
      }
    });

    displayAllUsersBtn.setText("Wyświetl wszystkich użytkowników");
    displayAllUsersBtn.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        displayAllUsersBtnActionPerformed(evt);
      }
    });

    sendToAllBtn.setText("Wyślij");

    messageToAllLabel.setText("Wyślij wiadomość do wszystkich użytkowników");

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jScrollPane1)
          .addGroup(layout.createSequentialGroup()
            .addComponent(messageToAllTextField)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(sendToAllBtn))
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(layout.createSequentialGroup()
                .addComponent(startServerBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(stopServerBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE))
              .addGroup(layout.createSequentialGroup()
                .addComponent(clearScreenBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(displayAllUsersBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE))
              .addComponent(messageToAllLabel))
            .addGap(0, 0, Short.MAX_VALUE)))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(startServerBtn)
          .addComponent(stopServerBtn))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
          .addComponent(displayAllUsersBtn)
          .addComponent(clearScreenBtn))
        .addGap(24, 24, 24)
        .addComponent(messageToAllLabel)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addComponent(messageToAllTextField)
          .addComponent(sendToAllBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE))
        .addContainerGap())
    );

    pack();
    setLocationRelativeTo(null);
  }// </editor-fold>//GEN-END:initComponents

  private void stopServerBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopServerBtnActionPerformed
    try {
      Thread.sleep(5000);                 //5000 milliseconds is five second.
    } catch (InterruptedException ex) {
      Thread.currentThread().interrupt();
    }

    sendToAllUsers("Server:is stopping and all users will be disconnected.\n:Chat");
    mainTextArea.append("Server stopping... \n");

    mainTextArea.setText("");
  }//GEN-LAST:event_stopServerBtnActionPerformed

  private void clearScreenBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearScreenBtnActionPerformed
    mainTextArea.setText("");
  }//GEN-LAST:event_clearScreenBtnActionPerformed

  private void displayAllUsersBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_displayAllUsersBtnActionPerformed
    mainTextArea.append("\n Online users : \n");
    for (String current_user : usersList) {
      mainTextArea.append(current_user);
      mainTextArea.append("\n");
    }
  }//GEN-LAST:event_displayAllUsersBtnActionPerformed

  private void startServerBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startServerBtnActionPerformed
    ServerStart tempServerStart = new ServerStart(this);
    Thread starter = new Thread(tempServerStart);
    starter.start();

    mainTextArea.append("Server started...\n");
  }//GEN-LAST:event_startServerBtnActionPerformed

  private ArrayList<PrintWriter> outputStreams;
  private ArrayList<String> usersList;

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton clearScreenBtn;
  private javax.swing.JButton displayAllUsersBtn;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JTextArea mainTextArea;
  private javax.swing.JLabel messageToAllLabel;
  private javax.swing.JTextField messageToAllTextField;
  private javax.swing.JButton sendToAllBtn;
  private javax.swing.JButton startServerBtn;
  private javax.swing.JButton stopServerBtn;
  // End of variables declaration//GEN-END:variables
}
