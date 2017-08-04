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
      _socket = clientSocket;
      InputStreamReader inReader = new InputStreamReader(_socket.getInputStream());
      _reader = new BufferedReader(inReader);
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
        Message receivedMsg = new Message(stream);
        Message outMsg = null;

        switch (receivedMsg.getType()) {
          case Chat:
            _mainFrame.sendToAllUsers(receivedMsg.getMessage());
            break;
          case Connect:
            outMsg = new Message(MsgType.Chat, receivedMsg.getUsername(), receivedMsg.getContent());
            _mainFrame.sendToAllUsers(outMsg.getMessage());
            _mainFrame.addUser(receivedMsg.getUsername());
            break;
          case Disconnect:
            outMsg = new Message(MsgType.Chat, receivedMsg.getUsername(), "rozłączył się.");
            _mainFrame.sendToAllUsers(outMsg.getMessage());
            _mainFrame.removeUser(receivedMsg.getUsername());
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
  private Socket _socket;
  private PrintWriter _client;
  private BufferedReader _reader;
}
