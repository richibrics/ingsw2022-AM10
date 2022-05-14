package it.polimi.ingsw.network.server;

import java.util.ArrayList;

public class ServerMain {

    public static void main(String[] args) {
        System.out.println("Server started.");
        ArrayList<ServerClientConnection> serverClientConnections = new ArrayList<>();
        Server server = new Server(serverClientConnections);
        Thread thread = new Thread(server);
        thread.start();
    }
}