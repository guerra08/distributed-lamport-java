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
    private DatagramSocket socket;
    private Lamport lamport;

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
            socket = new DatagramSocket(config.getPort(), InetAddress.getByName("192.168.56.1"));
            InetAddress host = InetAddress.getByName("192.168.56.1");
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
            startProcessing();
            receivePackets();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receivePackets() {
        System.out.println("Ready to receive packets");

        while (!Thread.currentThread().isInterrupted()) {
            try {
                socket.setSoTimeout(1000);
                byte[] buf = new byte[1024];
                DatagramPacket dp = new DatagramPacket(buf, buf.length);
                socket.receive(dp);
                String clock = new String(dp.getData(), 0, dp.getLength());
                lamport.updateClock(Integer.parseInt(clock.charAt(clock.length()-1)+""));
                System.out.println(lamport.getCounter());
                return;
            } catch (IOException e){
                System.out.println(e.getMessage());
            }

        }
    }

    private void startProcessing(){
        if(ready){
            lamport = new Lamport(id);

            try {
                if (id.equals("1")) {
                    String message = "ID: " + lamport.getId() + " Clock: " + lamport.getCounter();
                    byte[] byteMessage = message.getBytes();
                    InetAddress host = InetAddress.getByName("192.168.56.1");
                    DatagramPacket packet = new DatagramPacket(byteMessage, byteMessage.length, host, 5543);
                    socket.send(packet);
                    System.out.println("send");
                }

            }catch (IOException e){
            System.out.println(e.getMessage());
            }
        }
    }

}
