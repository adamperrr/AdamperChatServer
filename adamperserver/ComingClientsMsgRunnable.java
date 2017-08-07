package adamperserver;

import java.util.*;
import java.io.*;
import java.net.*;

import msg.*;

public class ComingClientsMsgRunnable implements Runnable {

  public ComingClientsMsgRunnable(Socket clientSocket, PrintWriter writer, AdamperServer frame) {
    _mainFrame = frame;
    _writer = writer;
    
    try {
      _socket = clientSocket;
      InputStreamReader inReader = new InputStreamReader(_socket.getInputStream());
      _reader = new BufferedReader(inReader);
    } catch (Exception e) {
      _mainFrame.appendError("Nieoczekiwany błąd...");
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
            if(receivedMsg.getTo().equals("all")) {
              outMsg = new Message(MsgType.Chat, receivedMsg.getUsername(), receivedMsg.getContent());
              _mainFrame.sendToAllUsers(outMsg.getMessage());
            } else {
              outMsg = new Message(MsgType.Chat, receivedMsg.getUsername(), receivedMsg.getTo(), receivedMsg.getContent());
              _mainFrame.sendToOneUser(outMsg.getTo(), outMsg.getUsername(), _writer, outMsg.getMessage());
            }
            break;
          case Connect:
            outMsg = new Message(MsgType.Chat, receivedMsg.getUsername(), receivedMsg.getContent());
            _mainFrame.addUser(receivedMsg.getUsername(), _writer);
            _mainFrame.sendToAllUsers(outMsg.getMessage());
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
    } catch(java.net.SocketException e) {
      // Thrown after every user disconnection
    } catch (Exception e) {
      _mainFrame.appendError("Comming... - run: " + e.toString());
      _mainFrame.appendError("Utracono połączenie...");
      _mainFrame.removeUserByPrintWriter(_writer);
    }
  }

  private AdamperServer _mainFrame;

  private Socket _socket;
  private PrintWriter _writer;
  private BufferedReader _reader;
}
