package adamperserver;

import java.util.*;
import java.io.*;
import java.net.*;

import msg.*;

public class ServerStart implements Runnable {

  public ServerStart(AdamperServer frame, int port) {
    _mainFrame = frame;
    _port = port;
  }

  @Override
  public void run() {
    _mainFrame.initialiseOutputStreams();
    _mainFrame.initialiseUsersList();

    try{
      ServerSocket serverSock = new ServerSocket(_port);

      while(true) {
        Socket clientSock = serverSock.accept();
        PrintWriter writer = new PrintWriter(clientSock.getOutputStream());
        _mainFrame.addUserToOutputStreams(writer);

        Thread listener = new Thread(new ClientHandler(clientSock, writer, _mainFrame));
        listener.start();
        _mainFrame.appendMsg("Uzyskano połaczenie...");
      }
    } catch (Exception ex) {
      _mainFrame.appendMsg("Błąd podczas zestawiania połączenia...");
    }
  }

  private AdamperServer _mainFrame;
  private int _port;
}
