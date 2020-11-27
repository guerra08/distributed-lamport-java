import java.util.Date;

public class App {

    public static void main(String[] args) {
        if(args[0].equals("multicast")){
            (new Thread(new Multicast())).start();
        }
        else{
            Node n = new Node();
            n.multicast(new Date().toString());
        }
    }

}
