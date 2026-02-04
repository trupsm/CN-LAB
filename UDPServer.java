import java.net.*;
class UDPServer {
    public static void main(String[] args) throws Exception {
        DatagramSocket s = new DatagramSocket(5454);
        byte[] b = new byte[100];
        while (true) {
            DatagramPacket p = new DatagramPacket(b, b.length);
            s.receive(p);
            String msg = new String(p.getData(), 0, p.getLength());
            if (msg.equals("exit")) break;
            msg = msg.toUpperCase();
            s.send(new DatagramPacket(msg.getBytes(),msg.length(), p.getAddress(), p.getPort()));
        }
        s.close();
    }
}
