package adamperserver;

import java.util.*;
import java.io.*;
import java.net.*;

import msg.*;

public class ClientHandler implements Runnable {

  public ClientHandler(Socket clientSocket, PrintWriter user, AdamperServer frame) {

    _mainFrame = frame;

    _client = user;
    try {
      _sock = clientSocket;
      InputStreamReader isReader = new InputStreamReader(_sock.getInputStream());
      _reader = new BufferedReader(isReader);
    } catch (Exception ex) {
      _mainFrame.appendMsg("Nieoczekiwany błąd...");
    }

  }

  @Override
  public void run() {
    String stream;
   
    try {
      while ((stream = _reader.readLine()) != null) {
        
        _mainFrame.appendMsg("Otrzymano: " + stream);
        Message tempMsg = new Message(stream);

        switch(tempMsg.getType()) {
          case Chat:
            _mainFrame.sendToAllUsers(tempMsg.getMessage());
          break;
          case Connect:
            Message tempMsg1 = new Message(MsgType.Chat, tempMsg.getUsername(), tempMsg.getContent());
            _mainFrame.sendToAllUsers(tempMsg1.getMessage());
            _mainFrame.addUser(tempMsg.getUsername());
          break;
          case Disconnect:
            Message tempMsg2 = new Message(MsgType.Chat, tempMsg.getUsername(), "rozłączył się.");
            _mainFrame.sendToAllUsers(tempMsg2.getMessage());
            _mainFrame.removeUser(tempMsg.getUsername());
          break;
          default:
            _mainFrame.appendMsg("Błąd w wiadomości...");
          break;
        }        
      }
    } catch (Exception ex) {
      _mainFrame.appendMsg("Utracono połączenie...");
      ex.printStackTrace();
      _mainFrame.removeUserFromOutputStreams(_client);
    }
  }

  private AdamperServer _mainFrame;
  private Socket _sock;
  private PrintWriter _client;
  private BufferedReader _reader;
}
