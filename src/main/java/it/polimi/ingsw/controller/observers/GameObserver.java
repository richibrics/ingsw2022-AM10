package it.polimi.ingsw.controller.observers;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.controller.Serializer;
import it.polimi.ingsw.model.exceptions.TableNotSetException;
import it.polimi.ingsw.network.server.ServerClientConnection;

import java.util.ArrayList;

/**
 * Observer used to warn the players about game changes.
 */
public class GameObserver implements Observer {

    ArrayList<ServerClientConnection> serverClientConnections;
    GameEngine gameEngine;

    public GameObserver(ArrayList<ServerClientConnection> serverClientConnections, GameEngine gameEngine) {
        this.serverClientConnections = serverClientConnections;
        this.gameEngine = gameEngine;
    }

    /**
     * Sends the update data with the current state to the clients.
     */
    @Override
    public void notifyClients() {
        for (ServerClientConnection serverClientConnection : this.serverClientConnections) {
            try {
                if (serverClientConnection != null) {
                    serverClientConnection.sendMessage(Serializer.generateTableMessage(this.gameEngine));
                    serverClientConnection.sendMessage(Serializer.generateTeamsMessage(this.gameEngine));
                    serverClientConnection.sendMessage(Serializer.generateRoundMessage(this.gameEngine));
                }
            } catch (TableNotSetException e) {
                return;
                // If table isn't set yet, I don't want to send to the clients anything.
            }
        }
    }
}
