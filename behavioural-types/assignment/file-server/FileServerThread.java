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

      byte[] fileContent = {
        (byte) 0xDE,
        (byte) 0xAD,
        (byte) 0xBE,
        (byte) 0xEF,
      };

        for (byte b : fileContent) {
          server.sendByte(b);
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
