package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.NetworkConstants;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class that runs a thread which accepts incoming connections from the clients.
 */
public class Server implements Runnable {

    private ArrayList<ServerClientConnection> listOfServerClientConnections;
    private ServerSocket serverSocket;
    private boolean firstConnection;
    private int port;

    public Server(ArrayList<ServerClientConnection> serverClientConnections, int port) {
        this(serverClientConnections);
        this.port = port;
    }

    public Server(ArrayList<ServerClientConnection> serverClientConnections) {
        this.port = NetworkConstants.PORT;
        this.listOfServerClientConnections = serverClientConnections;
        this.firstConnection = true;
    }

    /**
     * Accepts connections from clients; starts the thread that runs the ServerStillAliveChecker, which checks if the clients
     * are still alive and sends messages to notify the clients that the server is running; creates a new instance of
     * ServerClientConnection and adds it to the list of server-client connections.
     */

    @Override
    public void run() {
        try {
            this.serverSocket = new ServerSocket(this.port);
        } catch (IOException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Server unable to create the socket.");
            e.printStackTrace();
        }

        while (true) {
            Socket clientSocket;
            try {
                Logger.getAnonymousLogger().log(Level.INFO, "Waiting for connection.");
                clientSocket = this.serverSocket.accept();
                Logger.getAnonymousLogger().log(Level.INFO, "Connection accepted.");

                synchronized (this.listOfServerClientConnections) {
                    ServerClientConnection serverClientConnection = new ServerClientConnection(clientSocket);
                    new Thread(serverClientConnection).start();
                    this.listOfServerClientConnections.add(serverClientConnection);
                }

                if (this.firstConnection) {
                    new Thread(new ServerStillAliveChecker(this.listOfServerClientConnections)).start();
                    this.firstConnection = false;
                }
            }
            catch (IOException e) {
                Logger.getAnonymousLogger().log(Level.SEVERE, "Server unable to accept the connection.");
                e.printStackTrace();
            }
        }
    }
}