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

    try{
      ServerSocket serverSock = new ServerSocket(_port);

      while(true) {
        Socket clientSock = serverSock.accept();
        PrintWriter writer = new PrintWriter(clientSock.getOutputStream());

        Thread listener = new Thread(new ComingClientsMsgRunnable(clientSock, writer, _mainFrame));
        listener.start();
        _mainFrame.appendMsg("Uzyskano połaczenie...");
      }
    } catch (Exception e) {
      _mainFrame.appendError("Błąd podczas zestawiania połączenia...");
    }
  }

  private AdamperServer _mainFrame;
  private int _port;
}
