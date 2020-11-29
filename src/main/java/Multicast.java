import org.jgroups.JChannel;
import java.util.Scanner;

public class Multicast{

    public void start(){
        try {
            Scanner sc = new Scanner(System.in);
            JChannel channel = new JChannel("src/main/resources/jgroups.xml");
            channel.connect("prog-dist-cluster");

            System.out.println("Start distributed system: ");

            sc.nextLine();

            System.out.println("Starting...");

            channel.send(null, "OK");
            channel.disconnect();
            channel.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
