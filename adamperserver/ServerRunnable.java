package adamperserver;

import java.util.*;
import java.io.*;
import java.net.*;
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
      ServerSocket serverSocket = new ServerSocket(_port);

      while (true && _mainFrame.getServerStarted()) {
        Socket clientSock = serverSocket.accept();
        if (Thread.interrupted() || !_mainFrame.getServerStarted()) {
          serverSocket.close();
          break;
        }
        PrintWriter writer = new PrintWriter(clientSock.getOutputStream());

        Thread listener = new Thread(new ComingClientsMsgRunnable(clientSock, writer, _mainFrame));
        listener.setName("ComingClientsMsgRunnable");
        listener.start();
        _mainFrame.appendMsg("Uzyskano połaczenie...");
      }

      serverSocket.close();
      
    } catch (Exception e) {
      _mainFrame.appendError(e.toString());
      _mainFrame.appendError("Błąd podczas zestawiania połączenia...");
    }
  }

  private AdamperServer _mainFrame;
  private int _port;
}
