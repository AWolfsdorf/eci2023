import java.net.*;

public class FileServerThread extends Thread {
  private Socket socket;

  public FileServerThread(Socket socket) {
    this.socket = socket;
  }

  public void run() {
    try {
      FileServer server = new FileServer();
      if (!server.start(socket)) {
        System.out.println("Could not start server!");
        return;
      }

      System.out.println("File server started!");
      while (server.hasRequest()) {
        String fileName = server.readFileName();
        if (!server.fileExists(fileName)) {
          server.sendFileEnd();
          server.close();
          return;
        }

        char[] fileContent = "DEAD\nBEEF".toCharArray();

        if (fileContent == null) {
          server.sendFileEnd();
          server.close();
          return;
        }

        for (char b : fileContent) {
          server.sendByte((byte) b);
        }

        server.sendFileEnd();      
      }

      server.close();
      return;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
