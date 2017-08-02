package adamperserver;

import java.util.*;
import java.io.*;
import java.net.*;

public class ClientHandler implements Runnable {

  public ClientHandler(Socket clientSocket, PrintWriter user, AdamperServer frame) {

    mainFrame = frame;

    client = user;
    try {
      sock = clientSocket;
      InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
      reader = new BufferedReader(isReader);
    } catch (Exception ex) {
      mainFrame.appendToScreen("Unexpected error... \n");
    }

  }

  @Override
  public void run() {
    String message, connect = "Connect", disconnect = "Disconnect", chat = "Chat";
    String[] data;

    try {
      while ((message = reader.readLine()) != null) {
        mainFrame.appendToScreen("Received: " + message + "\n");
        data = message.split(":");

        for (String token : data) {
          mainFrame.appendToScreen(token + "\n");
        }

        if (data[2].equals(connect)) {
          mainFrame.sendToAllUsers((data[0] + ":" + data[1] + ":" + chat));
          mainFrame.addUser(data[0]);
        } else if (data[2].equals(disconnect)) {
          mainFrame.sendToAllUsers((data[0] + ":has disconnected." + ":" + chat));
          mainFrame.removeUser(data[0]);
        } else if (data[2].equals(chat)) {
          mainFrame.sendToAllUsers(message);
        } else {
          mainFrame.appendToScreen("No Conditions were met. \n");
        }
      }
    } catch (Exception ex) {
      mainFrame.appendToScreen("Lost a connection. \n");
      ex.printStackTrace();
      mainFrame.removeUserFromOutputStreams(client);
    }
  }

  private AdamperServer mainFrame;
  private Socket sock;
  private PrintWriter client;
  private BufferedReader reader;
}
