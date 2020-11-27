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
import java.util.Random;

public class Node{

    private final Config config;
    private final List<Config> otherConfigs;
    private final String id;
    private boolean ready = false;
    private DatagramSocket receivingSocket;
    private DatagramSocket sendingSocket;
    private Lamport lamport;
    private final int EVENT_COUNT = 100;

    public Node(String file, String id){
        this.id = id;
        this.config = new Config(Objects.requireNonNull(File.getConfig(file, id)));
        this.otherConfigs = ConfigFactory.buildOtherConfigsList(File.getAllConfigsFromFile(file), id);
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
                //String clock = new String(dp.getData(), 0, dp.getLength());
                //lamport.updateClock(Integer.parseInt(clock.charAt(clock.length()-1)+""));
                System.out.println("Received");
            } catch (IOException e){ }
        }
    }

    private void startProcessing(){
        if(ready){
            lamport = new Lamport(id);
            try {
                if (id.equals("1")) {
                    for (int i = 0; i < 5; i++) {
                        String message = "ID: " + lamport.getId() + " Clock: " + lamport.getCounter();
                        byte[] byteMessage = message.getBytes();
                        DatagramPacket packet = new DatagramPacket(byteMessage,
                                byteMessage.length,
                                InetAddress.getByName(otherConfigs.get(0).getHost()),
                                otherConfigs.get(0).getPort());
                        sendingSocket.send(packet);
                        System.out.println("Sending packet");
                        Thread.sleep(getRandomLong(500, 1000));
                    }
                }
            }catch (IOException | InterruptedException e){ }
        }
    }

    private long getRandomLong(int min, int max) {
        Random random = new Random();
        return (long) random.nextInt(max - min) + min;
    }

}
