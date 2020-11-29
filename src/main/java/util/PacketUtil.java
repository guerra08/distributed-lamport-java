package util;

import algorithm.Lamport;
import domain.Config;
import domain.Event;

import java.io.*;
import java.net.DatagramPacket;
import java.net.InetAddress;

public class PacketUtil {


    public static DatagramPacket createPacket(Lamport lamp, Config config, Integer id) {
        try {
            Event event = new Event(lamp.getCounter(), lamp.getId(), id);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(bos);
            os.writeObject(event);
            os.flush();
            byte[] sendData = bos.toByteArray();
            os.close();
            bos.close();
            return new DatagramPacket(sendData,
                    sendData.length,
                    InetAddress.getByName(config.getHost()),
                    config.getPort());
        } catch(IOException ignored) {}
        return null;
    }

    public static Event packetBufferToEvent(byte[] buf) {
        try {
            ByteArrayInputStream byteStream = new ByteArrayInputStream(buf);
            ObjectInputStream is = new ObjectInputStream(new BufferedInputStream(byteStream));
            return (Event) is.readObject();
        } catch (IOException | ClassNotFoundException ignored) {}
        return null;
    }
}
