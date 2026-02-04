import java.net.*;
import java.io.*;

public class Server {
    public static void main(String[] args) throws Exception {

        ServerSocket server = new ServerSocket(3300);
        System.out.println("Server waiting...");
        Socket socket = server.accept();
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        // Receive filename
        String fileName = in.readLine();

        // Read file and send content
        BufferedReader file =new BufferedReader(new FileReader(fileName));
      
        String line;
        while ((line = file.readLine()) != null) {
            out.println(line);
        }
        file.close();
        socket.close();
        server.close();
    }
}
