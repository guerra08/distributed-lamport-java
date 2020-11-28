import algorithm.Lamport;
import domain.Config;
import domain.Event;
import factory.ConfigFactory;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.Receiver;
import util.File;
import util.Packet;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Node{

    private final Config config;
    private final List<Config> otherConfigs;
    private final String id;
    private boolean ready = false;
    private DatagramSocket receivingSocket;
    private DatagramSocket sendingSocket;
    private final Lamport lamport;
    private final int EVENT_COUNT = 100;
    private final List<Event> events;

    public Node(String file, String id){
        this.id = id;
        this.config = new Config(Objects.requireNonNull(File.getConfig(file, id)));
        this.otherConfigs = ConfigFactory.buildOtherConfigsList(File.getAllConfigsFromFile(file), id);
        this.lamport = new Lamport(id);
        this.events = new ArrayList<>();
    }

    public void start(){
        try{
            receivingSocket = new DatagramSocket(config.getPort(), InetAddress.getByName(config.getHost()));
            sendingSocket = new DatagramSocket();
            Thread t = new Thread(this::receivePackets);
            t.start();
            JChannel channel = new JChannel();
            channel.setReceiver(new Receiver(){
                public void receive(Message msg){
                    channel.disconnect();
                    channel.close();
                    ready = true;
                    startProcessing();
                }
            });
            channel.connect("prog-dist-cluster");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void receivePackets() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                receivingSocket.setSoTimeout(1000);
                byte[] buf = new byte[1024];
                DatagramPacket dp = new DatagramPacket(buf, buf.length);
                receivingSocket.receive(dp);
                Event receivedPacket = Packet.receivePacket(buf);
                System.out.println(receivedPacket.getClock() + " received. Local Clock: " + lamport.getCounter());
                lamport.updateClock(receivedPacket.getClock());
                System.out.println("New lamport: " + lamport.getCounter());
            } catch (IOException ignored){ }
        }
    }

    private void startProcessing(){
        if(ready){
            try {
                for (int i = 0; i < 5; i++) {
                    Config randomConfig = File.getRandomConfigFromFile(otherConfigs, Integer.parseInt(id));
                    DatagramPacket packet = Packet.createPacket(lamport, randomConfig);
                    sendingSocket.send(packet);
                    System.out.println("Sending packet");
                    Thread.sleep(getRandomLong(500, 1000));
                }
            }catch (IOException | InterruptedException e){ }
        }
    }

    private long getRandomLong(int min, int max) {
        Random random = new Random();
        return (long) random.nextInt(max - min) + min;
    }

}
