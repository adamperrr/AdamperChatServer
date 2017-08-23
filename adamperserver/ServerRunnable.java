package adamperserver;

import java.util.*;
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

import msg.*;

public class ServerRunnable implements Runnable {

  public ServerRunnable(AdamperServer frame, int port) {
    _mainFrame = frame;
    _port = port;
  }

  @Override
  public void run() {
    try {
      _mainFrame.initialiseUsersMap();
      _serverSocket = new ServerSocket(_port);

      while (_mainFrame.getServerStarted()) {
        Socket clientSock = _serverSocket.accept();
        PrintWriter writer = new PrintWriter(clientSock.getOutputStream());

        Thread client = new Thread(new ComingClientRunnable(clientSock, writer, _mainFrame));
        client.start();
        _mainFrame.appendMsg("Nawiązano połaczenie z nowym użytkownikiem.");
      }
    } catch (Exception e) {
      _mainFrame.appendError("Serwer został zatrzymany lub wystąpił błąd podczas zestawiania połączenia.");
    } finally {
      try {
        _serverSocket.close();
      } catch (IOException e) {
        _mainFrame.appendError(e.toString());
      }
    }
  }
  
    public void stop() {
      try {
        _serverSocket.close();
      } catch (IOException e) {
        _mainFrame.appendError(e.toString());
      }
    }

  private AdamperServer _mainFrame = null;
  private int _port = 1995;
  ServerSocket _serverSocket = null;
}
