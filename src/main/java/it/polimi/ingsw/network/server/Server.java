package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.NetworkConstants;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements Runnable {

    private ArrayList<ServerClientConnection> listOfServerClientConnections;
    private ServerSocket serverSocket;
    private boolean firstConnection;

    public Server(ArrayList<ServerClientConnection> serverClientConnections) {
        this.listOfServerClientConnections = serverClientConnections;
        this.firstConnection = true;
    }

    /**
     * Accepts connections from clients; starts the thread that runs the StillAliveChecker, which checks if the clients
     * are still alive and sends messages to notify the clients that the server is running; creates a new instance of
     * ServerClientConnection and adds it to the list of server-client connections.
     */

    @Override
    public void run() {
        try {
            this.serverSocket = new ServerSocket(NetworkConstants.PORT);
        } catch (IOException e) {
            System.out.println("Server unable to create the socket.");
            e.printStackTrace();
        }

        while (true) {
            Socket clientSocket;
            try {
                System.out.println("Waiting for connection.");
                clientSocket = this.serverSocket.accept();
                System.out.println("Connection accepted.");

                synchronized (this.listOfServerClientConnections) {
                    ServerClientConnection serverClientConnection = new ServerClientConnection(clientSocket);
                    new Thread(serverClientConnection).start();
                    this.listOfServerClientConnections.add(serverClientConnection);
                }

                if (this.firstConnection) {
                    new Thread(new StillAliveChecker(this.listOfServerClientConnections)).start();
                    this.firstConnection = false;
                }
            }
            catch (IOException e) {
                System.out.println("Server unable to accept the connection.");
                e.printStackTrace();
            }
        }
    }
}