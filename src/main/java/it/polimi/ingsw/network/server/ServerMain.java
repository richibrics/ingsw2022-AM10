package it.polimi.ingsw.network.server;

import java.util.ArrayList;

public class ServerMain {

    public static void main(String[] args) {
        ArrayList<ServerClientConnection> serverClientConnections = new ArrayList<>();
        Server server;
        if (args.length >= 1)
            server = new Server(serverClientConnections, Integer.parseInt(args[0])); // Pass the port, if present
        else
            server = new Server(serverClientConnections); // Use default port
        Thread thread = new Thread(server);
        thread.start();
    }
}