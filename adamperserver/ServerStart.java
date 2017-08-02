package adamperserver;

import java.util.*;
import java.io.*;
import java.net.*;

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
        mainFrame.appendToScreen("Got a connection. \n");
      }
    } catch (Exception ex) {
      mainFrame.appendToScreen("Error making a connection. \n");
    }
  }

  private AdamperServer mainFrame;
}
