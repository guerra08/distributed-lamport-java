public class App {

    public static void main(String[] args) {
        // java -jar jar-name multicast
        if(args.length == 1 && args[0].equals("multicast")){
            (new Multicast()).start();
        }
        // java -jar jar-name node config.txt 1 || 2 ...
        else if(args.length == 3 && args[0].equals("node")){
            Node n = new Node(args[1], args[2]);
            n.start();
        }
    }

}
