package it.polimi.ingsw.network.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    private int port;
    private String serverIp;

    public Client(int port, String serverIp) {
        this.port = port;
        this.serverIp = serverIp;
    }

    /**
     * Connects to the server; creates a new ClientServerConnection object and starts it; creates a new ClientStillAliveChecker
     * object and starts it.
     * @see ClientServerConnection
     * @see ClientStillAliveChecker
     */

    public void start() {
        try {
            System.out.println("Connecting to server...");
            Socket serverSocket = new Socket(this.serverIp, this.port);
            ClientServerConnection clientServerConnection = new ClientServerConnection(serverSocket);
            new Thread(clientServerConnection).start();
            new Thread(new ClientStillAliveChecker(clientServerConnection)).start();


        } catch (UnknownHostException e) {
            System.err.println("Unable to connect to server");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error when connecting to server" + this.serverIp);
            e.printStackTrace();
        }
    }
}