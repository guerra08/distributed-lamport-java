import domain.Config;
import factory.ConfigFactory;
import util.File;

import java.io.IOException;
import java.net.*;
import java.util.List;
import java.util.Objects;

public class Node implements Runnable{
    private final int sendPort = 6666;
    private Config config;
    private List<Config> otherConfigs;
    private String id;
    private volatile boolean ready = false;

    public Node(String file, String id){
        this.id = id;
        this.config = new Config(Objects.requireNonNull(File.getConfig(file, id)));
        this.otherConfigs = ConfigFactory.buildOtherConfigsList(File.getAllConfigsFromFile(file), id);
    }

    public void start(){
        try{
            Thread t = new Thread(this);
            t.start();
            byte[] message = "connection".getBytes();
            DatagramSocket socket = new DatagramSocket();
            InetAddress host = InetAddress.getByName("192.168.0.246");
            DatagramPacket packet = new DatagramPacket(message, message.length, host, 6666);
            socket.send(packet);
            t.join();
            //starts processing
            startProcessing();
        }catch (IOException | InterruptedException e){
            System.out.println(e.getMessage());
        }
    }

    public void run(){
        try {
            MulticastSocket multicastSocket = new MulticastSocket(4446);
            multicastSocket.joinGroup(InetAddress.getByName("230.0.0.0"));
            byte[] bytes = new byte[256];
            DatagramPacket received = new DatagramPacket(bytes, bytes.length);
            multicastSocket.receive(received);
            System.out.println(new String(received.getData(), 0, received.getLength()));
            ready = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startProcessing(){
        if(ready){
            System.out.println("Implements");
        }
    }

}
