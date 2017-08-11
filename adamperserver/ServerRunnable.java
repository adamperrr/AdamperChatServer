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
    _mainFrame.initialiseUsersMap();

    try {
      _serverSocket = new ServerSocket(_port);

      while (_mainFrame.getServerStarted()) {
        Socket clientSock = _serverSocket.accept();

        PrintWriter writer = new PrintWriter(clientSock.getOutputStream());

        Thread listener = new Thread(new ComingClientRunnable(clientSock, writer, _mainFrame));
        listener.start();
        _mainFrame.appendMsg("Uzyskano połaczenie...");
      }
    } catch (Exception e) {
      _mainFrame.appendError(e.toString());
      _mainFrame.appendError("Błąd podczas zestawiania połączenia...");
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

  private AdamperServer _mainFrame;
  private int _port;
  ServerSocket _serverSocket = null;
}
