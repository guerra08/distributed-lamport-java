import java.io.IOException;
import java.net.*;

public class Multicast implements Runnable{

    protected DatagramSocket socket;
    protected byte[] buf = new byte[256];
    protected final int nodeCount = 2;
    protected int connected = 0;

    public void run(){
        try{
            socket = new DatagramSocket(6666, InetAddress.getByName("192.168.0.246"));
            while(true){
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                connected++;
                System.out.println("received");
                if(connected == nodeCount) {
                    sendToMulticast();
                    break;
                }
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public void sendToMulticast(){
        try {
            InetAddress group = InetAddress.getByName("230.0.0.0");
            DatagramSocket sender = new DatagramSocket();
            String message = "OK";
            DatagramPacket packet = new DatagramPacket(message.getBytes(), message.getBytes().length, group, 4446);
            sender.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
