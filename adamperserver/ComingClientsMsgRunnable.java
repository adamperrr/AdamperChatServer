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
      while ((stream = _reader.readLine()) != null && _mainFrame.getServerStarted() ) {

        _mainFrame.appendMsg("Otrzymano: " + stream);
        Message receivedMsg = new Message(stream);
        Message outMsg = null;
        // Server sends messages with date and time receiving a message, NOT date of sending by client.
        
        if (Thread.interrupted() || !_mainFrame.getServerStarted()) {
          _writer.close();
          _reader.close();         
          _socket.close();
          break;
        }
        
        switch (receivedMsg.getType()) {
          case Chat:
            if (receivedMsg.getTo().equals("all")) {
              outMsg = new Message(MsgType.Chat, receivedMsg.getFrom(), receivedMsg.getContent());
              _mainFrame.sendToAllUsers(outMsg.getMessage());
            } else {
              outMsg = new Message(MsgType.Chat, receivedMsg.getFrom(), receivedMsg.getTo(), receivedMsg.getContent());
              _mainFrame.sendToOneUser(outMsg.getTo(), outMsg.getFrom(), _writer, outMsg.getMessage());
            }
            break;
          case Login:
            if (_mainFrame.userAlreadyExists(receivedMsg.getFrom())) {
              outMsg = new Message(MsgType.Disconnect, "ADMINISTRATOR", receivedMsg.getFrom(), "Taka nazwa użytkownika już istnieje");
            } else {
              outMsg = new Message(MsgType.Connect, "ADMINISTRATOR", receivedMsg.getFrom(), "Zalogowano");
            }

            _writer.println(outMsg.getMessage());
            _mainFrame.appendMsg("Wysłano: " + outMsg.getMessage());
            _writer.flush();
            break;
          case Connect:
            outMsg = new Message(MsgType.Chat, receivedMsg.getFrom(), receivedMsg.getContent());
            _mainFrame.addUser(receivedMsg.getFrom(), _writer);
            _mainFrame.sendToAllUsers(outMsg.getMessage());
            break;
          case Disconnect:
            outMsg = new Message(MsgType.Chat, receivedMsg.getFrom(), "rozłączył się.");
            _mainFrame.sendToAllUsers(outMsg.getMessage());
            _mainFrame.removeUser(receivedMsg.getFrom());
            break;
          default:
            _mainFrame.appendMsg("Błąd w wiadomości...");
            break;
        }
      }
      
      _writer.close();
      _reader.close();         
      _socket.close();      
    } catch (java.net.SocketException e) {
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
