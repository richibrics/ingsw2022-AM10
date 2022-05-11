package it.polimi.ingsw.network.client;

public class ClientMain {

    public static void main(String[] args) {
        System.out.println("Client started.");
        Client client = new Client(Integer.parseInt(args[0]), args[1]);
        client.start();
    }
}