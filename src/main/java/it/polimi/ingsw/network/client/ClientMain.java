package it.polimi.ingsw.network.client;

/**
 * Class that starts the client.
 */

public class ClientMain {

    public static void main(String[] args) {
        Client client;
        if (args.length > 2 && (args[2].equals("--cli") || args[2].equals("-c"))) {
            // CLI
            client = new Client(true, Integer.parseInt(args[1]), args[0]);
        } else {
            // GUI
            client = new Client(false, Integer.parseInt(args[1]), args[0]);
        }
        client.start();
    }
}