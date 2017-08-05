package adamperserver;

import java.util.*;
import java.io.*;
import java.net.*;

import msg.*;

public class ComingClientsMsgRunnable implements Runnable {

  public ComingClientsMsgRunnable(Socket clientSocket, PrintWriter user, AdamperServer frame) {
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
        // Server sends messages with date and time receiving a message, NOT date of sending by client.

        switch (receivedMsg.getType()) {
          case Chat:
            outMsg = new Message(MsgType.Chat, receivedMsg.getUsername(), receivedMsg.getContent());
            _mainFrame.sendToAllUsers(outMsg.getMessage());
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
      _mainFrame.appendError(ex.toString());
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
