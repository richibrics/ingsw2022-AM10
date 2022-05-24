package it.polimi.ingsw.network.client;

import it.polimi.ingsw.view.gui.LaunchGUI;

import static javafx.application.Application.launch;

public class ClientMain {

    public static void main(String[] args) {
        if (args.length > 2 && (args[2].equals("--cli") || args[2].equals("-c"))) {
            // CLI
            Client client = new Client(Integer.parseInt(args[1]), args[0]);
            client.start();
        } else {
            // GUI
            launch(LaunchGUI.class, args);
        }
    }
}