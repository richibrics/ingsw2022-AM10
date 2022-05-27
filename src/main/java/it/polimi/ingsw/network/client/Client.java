package it.polimi.ingsw.network.client;

import it.polimi.ingsw.view.ViewInterface;
import it.polimi.ingsw.view.cli.Cli;
import it.polimi.ingsw.view.gui.GUI;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    private int port;
    private String serverIp;
    private boolean isCli;

    public Client(boolean isCli, int port, String serverIp) {
        this.isCli = isCli;
        this.port = port;
        this.serverIp = serverIp;
    }

    /**
     * Connects to the server; creates a new ClientServerConnection object and starts it; creates a new ClientStillAliveChecker
     * object and starts it.
     *
     * @see ClientServerConnection
     * @see ClientStillAliveChecker
     */

    public void start() {
        try {
            ViewInterface view;
            System.out.println("Connecting to server...");
            Socket serverSocket = new Socket(this.serverIp, this.port);

            view = this.isCli ? new Cli() : new GUI();

            ClientServerConnection clientServerConnection = new ClientServerConnection(serverSocket, view);
            view.setClientServerConnection(clientServerConnection);
            new Thread(clientServerConnection).start();
            new Thread(new ClientStillAliveChecker(clientServerConnection)).start();


        } catch (UnknownHostException e) {
            System.err.println("Unable to connect to server");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error when connecting to server " + this.serverIp);
            e.printStackTrace();
        }
    }
}