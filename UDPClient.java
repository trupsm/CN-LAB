import java.net.*;
import java.util.Scanner;
class UDPClient {
    public static void main(String[] args) throws Exception {
        DatagramSocket s = new DatagramSocket();
        Scanner sc = new Scanner(System.in);
        InetAddress ip = InetAddress.getByName("localhost");
        while (true) {
            String msg = sc.nextLine();
            s.send(new DatagramPacket(msg.getBytes(), msg.length(), ip, 5454));
            if (msg.equals("exit")) break;
            byte[] b = new byte[100];
            DatagramPacket p = new DatagramPacket(b, b.length);
            s.receive(p);
            System.out.println(new String(p.getData(), 0, p.getLength()));
        }
        s.close();
    }
}
