package adamperserver;

import java.util.*;
import java.io.*;
import java.net.*;

import javax.swing.text.*;
import java.awt.*;

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

  public void appendMsg(String message) {
    StyledDocument doc = mainTextArea.getStyledDocument();
    message = message.trim() + "\n";
    try {
      doc.insertString(doc.getLength(), message, null);
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  public void sendToAllUsers(String message) {
    Iterator it = _outputStreams.iterator();

    while (it.hasNext()) {
      try {
        PrintWriter writer = (PrintWriter) it.next();
        writer.println(message);
        appendMsg("Sending: " + message);
        writer.flush();
        mainTextArea.setCaretPosition(mainTextArea.getDocument().getLength());

      } catch (Exception ex) {
        appendMsg("Error telling everyone.");
      }
    }
  }

  public void initialiseUsersList() {
    _usersList = new ArrayList();
  }

  public void addUser(String data) {
    String message, add = ": :Connect", done = "Server: :Done", name = data;
    _usersList.add(name);
    String[] tempList = new String[(_usersList.size())];
    _usersList.toArray(tempList);

    for (String token : tempList) {
      message = (token + add);
      sendToAllUsers(message);
    }
    sendToAllUsers(done);
  }

  public void removeUser(String data) {
    String message, add = ": :Connect", done = "Server: :Done", name = data;
    _usersList.remove(name);
    String[] tempList = new String[(_usersList.size())];
    _usersList.toArray(tempList);

    for (String token : tempList) {
      message = (token + add);
      sendToAllUsers(message);
    }
    sendToAllUsers(done);
  }

  public void initialiseOutputStreams() {
    _outputStreams = new ArrayList();
  }

  public void addUserToOutputStreams(PrintWriter user) {
    _outputStreams.add(user);
  }

  public void removeUserFromOutputStreams(PrintWriter user) {
    _outputStreams.remove(user);
  }

  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jScrollPane2 = new javax.swing.JScrollPane();
    jEditorPane1 = new javax.swing.JEditorPane();
    startServerBtn = new javax.swing.JButton();
    stopServerBtn = new javax.swing.JButton();
    clearScreenBtn = new javax.swing.JButton();
    displayAllUsersBtn = new javax.swing.JButton();
    messageToAllTextField = new javax.swing.JTextField();
    sendToAllBtn = new javax.swing.JButton();
    messageToAllLabel = new javax.swing.JLabel();
    jScrollPane1 = new javax.swing.JScrollPane();
    mainTextArea = new javax.swing.JTextPane();

    jScrollPane2.setViewportView(jEditorPane1);

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setTitle("Server - Adamper");
    setMinimumSize(new java.awt.Dimension(510, 360));
    setPreferredSize(new java.awt.Dimension(500, 350));

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
    sendToAllBtn.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        sendToAllBtnActionPerformed(evt);
      }
    });

    messageToAllLabel.setText("Wyślij wiadomość do wszystkich użytkowników");

    jScrollPane1.setViewportView(mainTextArea);

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
            .addComponent(startServerBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(stopServerBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGroup(layout.createSequentialGroup()
            .addComponent(clearScreenBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(displayAllUsersBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addComponent(messageToAllLabel))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(startServerBtn)
          .addComponent(stopServerBtn))
        .addGap(10, 10, 10)
        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE)
        .addGap(10, 10, 10)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
          .addComponent(displayAllUsersBtn)
          .addComponent(clearScreenBtn))
        .addGap(10, 10, 10)
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
    appendMsg("Server stopping...");

    mainTextArea.setText("");
  }//GEN-LAST:event_stopServerBtnActionPerformed

  private void clearScreenBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearScreenBtnActionPerformed
    mainTextArea.setText("");
  }//GEN-LAST:event_clearScreenBtnActionPerformed

  private void displayAllUsersBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_displayAllUsersBtnActionPerformed
    appendMsg("\n Online users :");
    for (String current_user : _usersList) {
      appendMsg(current_user);
      appendMsg("\n");
    }
  }//GEN-LAST:event_displayAllUsersBtnActionPerformed

  private void startServerBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startServerBtnActionPerformed
    ServerStart tempServerStart = new ServerStart(this);
    Thread starter = new Thread(tempServerStart);
    starter.start();

    appendMsg("Server started...");
  }//GEN-LAST:event_startServerBtnActionPerformed

  private void sendToAllBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendToAllBtnActionPerformed
    String nothing = "";
    if ((messageToAllTextField.getText()).equals(nothing)) {
      messageToAllTextField.setText("");
      messageToAllTextField.requestFocus();
    } else {
      sendToAllUsers("SERVER ADMIN:" + messageToAllTextField.getText() + ":" + "Chat");
      messageToAllTextField.setText("");
      messageToAllTextField.requestFocus();
    }

    messageToAllTextField.setText("");
    messageToAllTextField.requestFocus();
  }//GEN-LAST:event_sendToAllBtnActionPerformed

  private ArrayList<PrintWriter> _outputStreams;
  private ArrayList<String> _usersList;

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton clearScreenBtn;
  private javax.swing.JButton displayAllUsersBtn;
  private javax.swing.JEditorPane jEditorPane1;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JScrollPane jScrollPane2;
  private javax.swing.JTextPane mainTextArea;
  private javax.swing.JLabel messageToAllLabel;
  private javax.swing.JTextField messageToAllTextField;
  private javax.swing.JButton sendToAllBtn;
  private javax.swing.JButton startServerBtn;
  private javax.swing.JButton stopServerBtn;
  // End of variables declaration//GEN-END:variables
}
