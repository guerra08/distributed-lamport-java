import domain.Config;
import factory.ConfigFactory;
import util.File;

import java.io.IOException;
import java.net.*;
import java.util.List;
import java.util.Objects;

public class Node {
    private DatagramSocket socket;
    private InetAddress group;
    private byte[] buf;
    private final int sendPort = 4446;
    private Config config;
    private List<Config> otherConfigs;
    private String id;

    public Node(String file, String id){
        this.id = id;
        this.config = new Config(Objects.requireNonNull(File.getConfig(file, id)));
        this.otherConfigs = ConfigFactory.buildOtherConfigsList(File.getAllConfigsFromFile(file), id);
        System.out.println(this.config.getPort());
        System.out.println(this.otherConfigs.size());
    }

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
