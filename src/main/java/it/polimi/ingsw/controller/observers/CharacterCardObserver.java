package it.polimi.ingsw.controller.observers;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.game_components.Character;
import it.polimi.ingsw.model.managers.CommonManager;
import it.polimi.ingsw.network.MessageTypes;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.server.ServerClientConnection;

import java.util.ArrayList;

public class CharacterCardObserver implements Observer {

    ArrayList<ServerClientConnection> serverClientConnections;
    GameEngine gameEngine;

    public CharacterCardObserver(ArrayList<ServerClientConnection> serverClientConnections, GameEngine gameEngine) {
        this.serverClientConnections = serverClientConnections;
        this.gameEngine = gameEngine;
    }

    /**
     * Warns the players about a card use.
     */
    @Override
    public void notifyClients() {
        if (this.gameEngine.getLastPlayedCharacterCard()[0] != null && this.gameEngine.getLastPlayedCharacterCard()[0] != null) {
            int playerId = this.gameEngine.getLastPlayedCharacterCard()[0];
            int cardId = this.gameEngine.getLastPlayedCharacterCard()[1];
            String playerName = CommonManager.takePlayerById(this.gameEngine, playerId).getUsername();
            String cardName = Character.values()[cardId-1].name();

            for (ServerClientConnection serverClientConnection : this.serverClientConnections) {
                if (serverClientConnection != null)
                    serverClientConnection.sendMessage(new Message(MessageTypes.INFORMATION, playerName + " used the " + cardName + " card"));
            }
        }
    }
}