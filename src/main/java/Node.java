import java.io.IOException;
import java.net.*;

public class Node {
    private DatagramSocket socket;
    private InetAddress group;
    private byte[] buf;
    private final int sendPort = 4446;

    public void multicast(String message){
        try{
            socket = new DatagramSocket();
            group = InetAddress.getByName("230.0.0.0");
            buf = message.getBytes();

            DatagramPacket packet = new DatagramPacket(buf, buf.length, group, sendPort);
            socket.send(packet);
            socket.close();

            socket = new DatagramSocket();
            group = InetAddress.getByName("230.0.0.0");

            byte[] receivedBytes = new byte[256];
            DatagramPacket received = new DatagramPacket(receivedBytes, buf.length);
            socket.receive(received);
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

}
