public class App {

    public static void main(String[] args) {
        // java App multicast
        if(args[0].equals("multicast")){
            (new Thread(new Multicast())).start();
        }
        // java App node config.txt 1 || 2 ...
        else{
            Node n = new Node(args[1], args[2]);
            n.start();
        }
    }

}
