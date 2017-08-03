package adamperserver;

import java.util.*;
import java.io.*;
import java.net.*;

import msg.*;

public class ServerStart implements Runnable {

  public ServerStart(AdamperServer frame) {
    mainFrame = frame;
  }

  @Override
  public void run() {
    mainFrame.initialiseOutputStreams();
    mainFrame.initialiseUsersList();

    try {
      ServerSocket serverSock = new ServerSocket(2222);

      while (true) {
        Socket clientSock = serverSock.accept();
        PrintWriter writer = new PrintWriter(clientSock.getOutputStream());
        mainFrame.addUserToOutputStreams(writer);

        Thread listener = new Thread(new ClientHandler(clientSock, writer, mainFrame));
        listener.start();
        mainFrame.appendMsg("Uzyskano połaczenie...");
      }
    } catch (Exception ex) {
      mainFrame.appendMsg("Błąd podczas zestawiania połączenia...");
    }
  }

  private AdamperServer mainFrame;
}
