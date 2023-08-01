import java.net.*;
import java.io.*;
import jatyc.lib.Typestate;

@Typestate("FileClient")
public class FileClient {
  private Socket socket;
  protected OutputStream out;
  protected BufferedReader in;
  protected int lastByte;

  public boolean start() {
    try {
      socket = new Socket("localhost", 1234);
      out = socket.getOutputStream();
      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public void request(String filename) throws Exception {
    out.write("REQUEST\n".getBytes());
    out.write((filename+"\n").getBytes());
  }

  public boolean readNextByte() throws Exception {
    lastByte = in.read();
    if (lastByte != 0) {
      System.out.print((char) lastByte);
    } else {
      System.out.println("\nEof");
    }

    return lastByte != 0;
  }

  public void close() throws Exception {
    out.write("CLOSE\n".getBytes());
    socket.close();
    in.close();
    out.close();
  }

  public static void main(String[] args) throws Exception {
    Console cnsl = System.console();
    if (cnsl == null) {
      System.out.println("No console available");
      return;
    }
    
    FileClient client = new FileClient();
    if (!client.start()) {
      System.out.println("Could not start client!");
      return;
    }

    System.out.println("File client started!");
    System.out.println("Please enter a filename");
    System.out.println("Reading byte by byte");
    String filename = cnsl.readLine();
    if (filename == null) {
      filename = "";
    }
    
    client.request(filename);
    while(client.readNextByte()) {}
    client.close();
  }
}
