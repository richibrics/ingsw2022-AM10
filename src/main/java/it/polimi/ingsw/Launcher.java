package it.polimi.ingsw;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ClientMain;
import it.polimi.ingsw.network.server.ServerMain;
import it.polimi.ingsw.view.gui.LaunchGUI;

import static javafx.application.Application.launch;

/**
 * Main class that is launched to run the server or the client.
 * If only one argument is passed, the server will start using it as port (but also this argument is optional).
 * Otherwise, the first argument will be the address of the server and the second the port of the server for the client mode.
 *
 * Server:
 * java -jar filename.jar [optional: PORT - default=12987]
 *
 * Client:
 * java -jar filename.jar SERVER_ADDRESS SERVER_PORT
 */
public class Launcher {
    public static void main(String[] args) {
        if (args.length >= 2) {
            // Client
            System.out.println("Client mode.");
            ClientMain.main(args);
        } else {
            // Server
            System.out.println("Server mode.");
            ServerMain.main(args);
        }
    }
}
