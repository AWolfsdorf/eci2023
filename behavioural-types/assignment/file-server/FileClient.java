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
    Integer integer = Integer.valueOf(lastByte);
    if (lastByte != 0) {
      System.out.print(String.format("%02X", integer.byteValue()));
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
    FileClient client = new FileClient();
    if (client.start()) {
      System.out.println("File client started!");
      System.out.println("Please enter a filename");
      String filename = System.console().readLine();
      client.request(filename);
      while(client.readNextByte()) {}
      client.close();
    } else {
      System.out.println("Could not start client!");
    }
  }
}
