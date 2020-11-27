import domain.Config;
import factory.ConfigFactory;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.Receiver;
import util.File;

import java.io.IOException;
import java.net.*;
import java.util.List;
import java.util.Objects;

public class Node{
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
            JChannel channel = new JChannel();
            channel.connect("prog-dist-cluster");
            channel.setReceiver(new Receiver() {
                @Override
                public void receive(Message msg) {
                    startProcessing();
                }
            });
        } catch (Exception e) {
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
        System.out.println("START");
        /*if(ready){
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
        }*/
    }

}
