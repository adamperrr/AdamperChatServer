package adamperserver;

import java.util.*;
import java.io.*;
import java.net.*;

import javax.swing.text.*;
import java.awt.*;

import msg.*;

public class AdamperServer extends javax.swing.JFrame {

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
    } catch (ClassNotFoundException e) {
      java.util.logging.Logger.getLogger(AdamperServer.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
    } catch (InstantiationException e) {
      java.util.logging.Logger.getLogger(AdamperServer.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
    } catch (IllegalAccessException e) {
      java.util.logging.Logger.getLogger(AdamperServer.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
    } catch (javax.swing.UnsupportedLookAndFeelException e) {
      java.util.logging.Logger.getLogger(AdamperServer.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
    }
    //</editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new AdamperServer().setVisible(true);
      }
    });
  }

  public AdamperServer() {
    initComponents();
    setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/adamperserver/icon.png")));

    loadProperties();
    _serverStarted = false;
    setButtons();
  }

  public void appendMsg(String inputText) {
    StyledDocument doc = mainTextArea.getStyledDocument();
    inputText = inputText.trim() + "\n";
    try {
      doc.insertString(doc.getLength(), inputText, null);
      scroolDown();
    } catch (Exception e) {
      appendError("appendMsg: " + e.toString());
    }
  }

  public void appendError(String inputText) {
    StyledDocument doc = mainTextArea.getStyledDocument();
    inputText = inputText.trim() + "\n";

    SimpleAttributeSet textStyles = new SimpleAttributeSet();
    StyleConstants.setForeground(textStyles, Color.RED);
    StyleConstants.setBold(textStyles, true);

    try {
      doc.insertString(doc.getLength(), inputText, textStyles);
      scroolDown();
    } catch (Exception e) {
      appendMsg("appendError: " + e.toString());
    }
  }

  public void sendToAllUsers(String messageText) {
    PrintWriter writer = null;
    for (Map.Entry<String, PrintWriter> entry : _usersMap.entrySet()) {
      try {
        writer = (PrintWriter) entry.getValue();
        writer.println(messageText);
        appendMsg("Wysłano: " + messageText);
        writer.flush();

      } catch (Exception e) {
        appendError("Błąd wiadomości do wszystkich... ");
      }
    }
  }

  public void sendToOneUser(String to, String from, PrintWriter writerFrom, String messageText) {
    PrintWriter writerTo = _usersMap.get(to);

    if (writerTo != null) {
      try {
        writerTo.println(messageText);
        writerFrom.println(messageText);
        appendMsg("Wysłano od: " + from + " do " + to + ": " + messageText);
        writerTo.flush();
        writerFrom.flush();

      } catch (Exception e) {
        appendError("Błąd wiadomości do użytkownika: " + to + "...");
      }
    } else {
      writerFrom.println(messageText);
      writerFrom.flush();
    }
  }

  public void sendToOneUserFromAdmin(String to, String messageText) {
    PrintWriter writerTo = _usersMap.get(to);

    if (writerTo != null) {
      try {
        writerTo.println(messageText);
        appendMsg("Wysłano od: ADMINISTRATOR do " + to + ": " + messageText);
        writerTo.flush();
      } catch (Exception e) {
        appendError("Błąd wiadomości do użytkownika: " + to + "...");
      }
    } else {
      appendError("Błąd wiadomości do użytkownika: " + to + "...");
    }
  }

  public void initialiseUsersMap() {
    _usersMap = Collections.synchronizedMap(new HashMap());
  }

  public synchronized void addUser(String name, PrintWriter writerObj) {
    _usersMap.put(name, writerObj);

    Message tempMessage = null;
    try {
      for (Map.Entry<String, PrintWriter> entry : _usersMap.entrySet()) {
        tempMessage = new Message(MsgType.Connect, entry.getKey(), "FromServerConnectMsg");
        sendToAllUsers(tempMessage.getMessage());
      }

      tempMessage = new Message(MsgType.Done, "Serwer", "DoneMsg");
      sendToAllUsers(tempMessage.getMessage());

    } catch (Exception e) {
      appendError("addUser: " + e.toString());
    }
  }

  public synchronized void removeUser(String username) {
    _usersMap.remove(username);

    Message tempMessage = null;
    try {
      for (Map.Entry<String, PrintWriter> entry : _usersMap.entrySet()) {
        tempMessage = new Message(MsgType.Connect, entry.getKey(), "FromServerConnectMsg");
        sendToAllUsers(tempMessage.getMessage());
      }

      tempMessage = new Message(MsgType.Done, "Serwer", "DoneMsg");
      sendToAllUsers(tempMessage.getMessage());

    } catch (Exception e) {
      appendError("removeUser: " + e.toString());
    }
  }

  public synchronized void removeUserByPrintWriter(PrintWriter writer) {
    String searchedKey = "";
    boolean found = false;

    for (Map.Entry<String, PrintWriter> entry : _usersMap.entrySet()) {
      if (entry.getValue() == writer) {
        found = true;
        searchedKey = entry.getKey();
        break;
      }
    }

    if (found) {
      _usersMap.remove(searchedKey);
    }
  }

  public synchronized boolean userAlreadyExists(String username) {
    PrintWriter writerTo = null;
    writerTo = _usersMap.get(username);
    return writerTo != null;
  }

  public synchronized boolean getServerStarted() {
    return _serverStarted;
  }

  private void setButtons() {
    stopServerBtn.setEnabled(_serverStarted);
    displayOnlineUsersBtn.setEnabled(_serverStarted);
    messageTextField.setEnabled(_serverStarted);
    sendBtn.setEnabled(_serverStarted);

    startServerBtn.setEnabled(!_serverStarted);
  }

  private void startServer() {
    if (_serverStarted) {
      return;
    }

    _serverThread = new ServerRunnable(this, _port);
    Thread starter = new Thread(_serverThread);
    starter.setName("ServerRunnable");
    _serverStarted = true;
    starter.start();

    appendMsg("Serwer włączony...");
    setButtons();
  }

  private void stopServer() {
    if (!_serverStarted) {
      return;
    }

    try {
      Message tempMessage = new Message(MsgType.Chat, "Serwer", "zostanie zatrzymany - wszyscy użytkownicy zostaną wylogowani.");
      sendToAllUsers(tempMessage.getMessage());

      tempMessage = new Message(MsgType.Disconnect, "Serwer", "zostanie zatrzymany - wszyscy użytkownicy zostaną wylogowani.");
      sendToAllUsers(tempMessage.getMessage());

    } catch (Exception e) {
      appendError("stopServerBtnActionPerformed: " + e.toString());
    }
    
    _serverThread.stop();
    _serverStarted = false;
    _usersMap.clear();
    setButtons();

    appendMsg("Serwer zatrzymany...");
    mainTextArea.setText("");
  }

  private void sendMsg() {
    if (!_serverStarted) {
      return;
    }

    if (!messageTextField.getText().equals("")) {
      try {

        Message tempMessage = new Message(MsgType.Chat, "ADMINISTRATOR", messageTextField.getText());

        if (tempMessage.getTo().equals("all")) {
          sendToAllUsers(tempMessage.getMessage());
        } else {
          sendToOneUserFromAdmin(tempMessage.getTo(), tempMessage.getMessage());
        }

      } catch (Exception e) {
        appendError("sendBtnActionPerformed: " + e.toString());
      }
    }

    clearMsgField();
    messageTextField.requestFocus();
  }

  private void displayOnlineUsers() {
    if (!_serverStarted) {
      return;
    }

    appendMsg("\n Użytkownicy online :");
    for (Map.Entry<String, PrintWriter> entry : _usersMap.entrySet()) {
      appendMsg("\t" + entry.getKey());
    }
  }

  private void loadProperties() {
    Properties prop = new Properties();
    InputStream input = null;

    try {
      input = this.getClass().getResourceAsStream("/adamperserver/config.properties");

      // load a properties file
      prop.load(input);

      // get the property value and print it out
      _port = Integer.parseInt(prop.getProperty("port"));

    } catch (IOException ex) {
      ex.printStackTrace();
    } finally {
      if (input != null) {
        try {
          input.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private void clearScreen() {
    mainTextArea.setText("");
  }

  private void clearMsgField() {
    messageTextField.setText("");
  }

  private void scroolDown() {
    mainTextArea.setCaretPosition(mainTextArea.getDocument().getLength());
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
    displayOnlineUsersBtn = new javax.swing.JButton();
    messageTextField = new javax.swing.JTextField();
    sendBtn = new javax.swing.JButton();
    messageLabel = new javax.swing.JLabel();
    jScrollPane1 = new javax.swing.JScrollPane();
    mainTextArea = new javax.swing.JTextPane();

    jScrollPane2.setViewportView(jEditorPane1);

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setTitle("AdamperServer");
    setMinimumSize(new java.awt.Dimension(510, 360));
    setPreferredSize(new java.awt.Dimension(500, 350));
    addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(java.awt.event.WindowEvent evt) {
        formWindowClosing(evt);
      }
    });

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

    displayOnlineUsersBtn.setText("Wyświetl użytkowników online");
    displayOnlineUsersBtn.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        displayOnlineUsersBtnActionPerformed(evt);
      }
    });

    sendBtn.setText("Wyślij");
    sendBtn.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        sendBtnActionPerformed(evt);
      }
    });

    messageLabel.setText("Wyślij wiadomość do wszystkich użytkowników");

    mainTextArea.setEditable(false);
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
            .addComponent(messageTextField)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(sendBtn))
          .addGroup(layout.createSequentialGroup()
            .addComponent(startServerBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(stopServerBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGroup(layout.createSequentialGroup()
            .addComponent(clearScreenBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(displayOnlineUsersBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addComponent(messageLabel))
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
          .addComponent(displayOnlineUsersBtn)
          .addComponent(clearScreenBtn))
        .addGap(10, 10, 10)
        .addComponent(messageLabel)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addComponent(messageTextField)
          .addComponent(sendBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE))
        .addContainerGap())
    );

    pack();
    setLocationRelativeTo(null);
  }// </editor-fold>//GEN-END:initComponents

  private void stopServerBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopServerBtnActionPerformed
    stopServer();
  }//GEN-LAST:event_stopServerBtnActionPerformed

  private void clearScreenBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearScreenBtnActionPerformed
    clearScreen();
  }//GEN-LAST:event_clearScreenBtnActionPerformed

  private void displayOnlineUsersBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_displayOnlineUsersBtnActionPerformed
    displayOnlineUsers();
  }//GEN-LAST:event_displayOnlineUsersBtnActionPerformed

  private void startServerBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startServerBtnActionPerformed
    startServer();
  }//GEN-LAST:event_startServerBtnActionPerformed

  private void sendBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendBtnActionPerformed
    sendMsg();
  }//GEN-LAST:event_sendBtnActionPerformed

  private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
    stopServer();
  }//GEN-LAST:event_formWindowClosing

  private Map<String, PrintWriter> _usersMap;
  private boolean _serverStarted = false;
  private int _port = 1995; // Default value - loaded from properties
  
  private ServerRunnable _serverThread = null;

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton clearScreenBtn;
  private javax.swing.JButton displayOnlineUsersBtn;
  private javax.swing.JEditorPane jEditorPane1;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JScrollPane jScrollPane2;
  private javax.swing.JTextPane mainTextArea;
  private javax.swing.JLabel messageLabel;
  private javax.swing.JTextField messageTextField;
  private javax.swing.JButton sendBtn;
  private javax.swing.JButton startServerBtn;
  private javax.swing.JButton stopServerBtn;
  // End of variables declaration//GEN-END:variables
}
