import java.net.*;
import java.io.*;

public class Client {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 3300);
        BufferedReader keyboard =new BufferedReader(new InputStreamReader(System.in));
        BufferedReader in =new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        // Send filename
        System.out.print("Enter filename: ");
        out.println(keyboard.readLine());

        // Receive and display file
        String line;
        while ((line = in.readLine()) != null) {
            System.out.println(line);
        }

        socket.close();
    }
}
