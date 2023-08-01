import java.net.*;
import java.io.*;
import jatyc.lib.Typestate;

@Typestate("FileClient2")
public class FileClient2 extends FileClient {

  public boolean readNextLine() throws Exception {
    int lastReadByte = -1;
    while (lastReadByte != 0 && (char) lastReadByte != '\n') {
      lastReadByte = in.read();
      if (lastReadByte != 0) {
        System.out.print((char) lastReadByte);
      } else {
        System.out.println("\nEof");
      }
    }

    return lastReadByte != 0;
  }

  public static void main(String[] args) throws Exception {
    Console cnsl = System.console();
    if (cnsl == null) {
      System.out.println("No console available");
      return;
    }
    
    FileClient2 client = new FileClient2();
    if (!client.start()) {
      System.out.println("Could not start client!");
      return;
    }

    System.out.println("File client started!");
    System.out.println("Please enter a filename");    
    System.out.println("Reading line by line");
    String filename = cnsl.readLine();
    if (filename == null) {
      filename = "";
    }
    
    client.request(filename);
    while(client.readNextLine()) {}
    client.close();
  }
}
