package adamperserver;

import java.util.*;
import java.io.*;
import java.net.*;

import msg.*;

public class ComingClientRunnable implements Runnable {

  public ComingClientRunnable(Socket socket, PrintWriter writer, AdamperServer frame) {
    _mainFrame = frame;
    _writer = writer;

    try {
      _socket = socket;
      _reader = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
    } catch (Exception e) {
      _mainFrame.appendError("Nieoczekiwany błąd.");
    }
  }

  @Override
  public void run() {
    String line = null;
    try {
      while ((line = _reader.readLine()) != null && _mainFrame.getServerStarted()) {

        _mainFrame.appendMsg("Otrzymano: " + line);
        Message receivedMsg = new Message(line);
        Message outMsg = null; // Server sends messages with date and time receiving a message, NOT date of sending by client.

        switch (receivedMsg.getType()) {
          case Chat:
            chatMsgResp(receivedMsg, outMsg);
            break;
          case Login:
            loginMsgResp(receivedMsg, outMsg);
            break;
          case Connect:
            connectMsgResp(receivedMsg, outMsg);
            break;
          case Disconnect:
            disconnectMsgResp(receivedMsg, outMsg);
            break;
          default:
            _mainFrame.appendError("Comming...: Błąd w wiadomości.");
            break;
        }
      }

    } catch (java.net.SocketException e) {
      // Thrown after every user disconnection
    } catch (Exception e) {
      _mainFrame.appendError("Comming. - run: " + e.toString());
      _mainFrame.appendError("Utracono połączenie.");
      _mainFrame.removeUserByPrintWriter(_writer);
    } finally {
      try {
        _socket.close();
        _reader.close();
        _writer.close();
      } catch (IOException e) {
        _mainFrame.appendError(e.toString());
      }
    }
  }

  private void chatMsgResp(Message receivedMsg, Message outMsg) throws Exception {
    if (receivedMsg.getTo().equals("all")) {
      outMsg = new Message(MsgType.Chat, receivedMsg.getFrom(), receivedMsg.getContent());
      _mainFrame.sendToAllUsers(outMsg.getMessage());
    } else {
      outMsg = new Message(MsgType.Chat, receivedMsg.getFrom(), receivedMsg.getTo(), receivedMsg.getContent());
      _mainFrame.sendToOneUser(outMsg.getTo(), outMsg.getFrom(), _writer, outMsg.getMessage());
    }
  }

  private void loginMsgResp(Message receivedMsg, Message outMsg) throws Exception {
    if (_mainFrame.userAlreadyExists(receivedMsg.getFrom())) {
      outMsg = new Message(MsgType.Disconnect, "ADMINISTRATOR", receivedMsg.getFrom(), "Taka nazwa użytkownika już istnieje");
    } else {
      outMsg = new Message(MsgType.Connect, "ADMINISTRATOR", receivedMsg.getFrom(), "Zalogowano");
    }

    _writer.println(outMsg.getMessage());
    _mainFrame.appendMsg("Wysłano: " + outMsg.getMessage());
    _writer.flush();
  }

  private void connectMsgResp(Message receivedMsg, Message outMsg) throws Exception {
    outMsg = new Message(MsgType.Chat, receivedMsg.getFrom(), receivedMsg.getContent());
    _mainFrame.addUser(receivedMsg.getFrom(), _writer);
    _mainFrame.sendToAllUsers(outMsg.getMessage());
  }

  private void disconnectMsgResp(Message receivedMsg, Message outMsg) throws Exception {
    outMsg = new Message(MsgType.Chat, receivedMsg.getFrom(), "rozłączył się.");
    _mainFrame.sendToAllUsers(outMsg.getMessage());
    _mainFrame.removeUser(receivedMsg.getFrom());
  }

  private AdamperServer _mainFrame = null;

  private Socket _socket = null;
  private PrintWriter _writer = null;
  private BufferedReader _reader = null;
}
