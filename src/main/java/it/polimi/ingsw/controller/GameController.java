package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exceptions.IllegalGameStateException;
import it.polimi.ingsw.controller.exceptions.InterruptedGameException;
import it.polimi.ingsw.controller.exceptions.WrongMessageContentException;
import it.polimi.ingsw.controller.observers.GameObserver;
import it.polimi.ingsw.controller.observers.Observer;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.exceptions.IllegalGameActionException;
import it.polimi.ingsw.model.exceptions.PlayerOrderNotSetException;
import it.polimi.ingsw.network.messages.ActionMessage;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.network.server.ServerClientConnection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Class that manages the match from outside.
 * Creates the match using Users passed with the ServerClientConnections.
 * Waits for ActionMessages to trigger internal Actions.
 */
public class GameController {
    private GameEngine gameEngine;
    private Observer gameObserver;
    private Map<User, ServerClientConnection> serverClientConnections;

    public GameController(Map<User, ServerClientConnection> serverClientConnections) {
        this.serverClientConnections = new HashMap<>(serverClientConnections);
        this.gameEngine = null;
    }

    /**
     * Returns the GameEngine
     *
     * @return the GameEngine
     */
    public GameEngine getGameEngine() {
        return gameEngine;
    }

    /**
     * Creates the players and the teams using the users present in the serverClientConnections map.
     * Then the GameEngine is created with those teams.
     * With the new GameEngine, the match is prepared using GameEngine start game method.
     *
     * @throws InterruptedGameException if an error was encountered during match creation.
     */
    public void startGame() throws InterruptedGameException {
        this.gameEngine = new GameEngine(this.createPlayersAndTeams());
        this.gameObserver = new GameObserver(new ArrayList<>(this.serverClientConnections.values()), this.gameEngine);
        try {
            this.gameEngine.startGame();
            this.gameObserver.notifyClients();
        } catch (Exception e) {
            // Match crashed during its creation: end the match
            e.printStackTrace();
            this.interruptGame("Error during match creation");
            throw new InterruptedGameException();
        }
    }

    /**
     * Method called when a message with an action command is received from the network.
     * Runs the action through the GameEngine, passing {@code actionMessage} data.
     * This method checks if the player has the turn and that the requested action is inside Round,
     * else an error has been made by the client and must be warned.
     *
     * @param actionMessage
     * @return the success value, useful to avoid warning the client of a success if I had an error
     */
    public void resumeGame(int playerId, ActionMessage actionMessage) throws InterruptedGameException, IllegalGameActionException, WrongMessageContentException {
        // Player with turn check
        try {
            if (playerId != gameEngine.getRound().getCurrentPlayer())
                throw new IllegalGameActionException("The player hasn't the rights to perform the requested Action");
        } catch (NullPointerException e) {
            // Game engine unavailable, was destroyed from this.interruptGame()
            System.out.println("Error during game resume: game engine not available anymore");
            e.printStackTrace();
            // Send information outside
            this.interruptGame("Internal error during action perform");
            throw new InterruptedGameException();
        } catch (PlayerOrderNotSetException e) {
            // Can't get here: to resume a game, the game must be started with start game.
            // Starting the game the round is set and after that it cannot be set to null in any way.
        }

        // Asked action check
        if (!gameEngine.getRound().getPossibleActions().contains(actionMessage.getActionId())) {
            // Warn the player
            throw new IllegalGameActionException("The requested action is not available");
        }

        // Player has rights to perform that action
        try {
            getGameEngine().resumeGame(actionMessage.getActionId(), playerId, actionMessage.getOptions());
            this.gameObserver.notifyClients();
        } catch (WrongMessageContentException e) {
            throw e;
        } catch (IllegalGameActionException e) {
            throw e;
        } catch (IllegalGameStateException e) {
            // Local error print
            System.out.println("Error during game resume: game state error during action run");
            e.printStackTrace();
            // Send information outside
            this.interruptGame("Internal error during action perform");
            throw new InterruptedGameException();
        } catch (Exception e) {
            // Unknown exception - debug purposes, in fact this is unreachable from tests.
            System.out.println("Error during game resume: action error");
            e.printStackTrace();
            return;
        }
    }

    /**
     * Using the Users, create Players and Teams.
     * If I have 4 users, the teams are only 2; otherwise number of teams = number of players
     *
     * @return an ArrayList with teams containing the players.
     */
    private ArrayList<Team> createPlayersAndTeams() {
        // Create Players
        AtomicInteger playerId = new AtomicInteger(ModelConstants.MIN_ID_OF_PLAYER);
        AtomicInteger teamId = new AtomicInteger(ModelConstants.MIN_ID_OF_TEAM);
        ArrayList<Player> players = this.serverClientConnections.keySet().stream().map(user -> new Player(user, playerId.getAndIncrement(), ModelConstants.INITIAL_PLAYER_COINS)).collect(Collectors.toCollection(ArrayList::new));
        ArrayList<Team> teams;

        // Create teams
        if (players.size() < ModelConstants.FOUR_PLAYERS) { // Each player in a team
            teams = players.stream().map(player -> {
                ArrayList<Player> teamPlayers = new ArrayList<>();
                teamPlayers.add(player);
                return new Team(teamId.getAndIncrement(), teamPlayers);
            }).collect(Collectors.toCollection(ArrayList::new));
        } else { // Split players in 2 teams
            ArrayList<Player> playersFirstTeam = players.stream().filter(player -> player.getPlayerId() % 2 == 1).collect(Collectors.toCollection(ArrayList::new));
            ArrayList<Player> playersSecondTeam = players.stream().filter(player -> player.getPlayerId() % 2 == 0).collect(Collectors.toCollection(ArrayList::new));
            teams = new ArrayList<>();
            teams.add(new Team(ModelConstants.MIN_ID_OF_TEAM, playersFirstTeam));
            teams.add(new Team(ModelConstants.MIN_ID_OF_TEAM + 1, playersSecondTeam));
        }

        return teams;
    }

    /**
     * Stops the game, warns all the players with a message and closes all the connections.
     * Removes also the GameController from the active game controllers in the lobby (so the players in this game can play again).
     */
    public void interruptGame(String playersMessage) {
        for (ServerClientConnection serverClientConnection : this.serverClientConnections.values()) {
            if (serverClientConnection != null) { // in the tests I have null server client connections
                serverClientConnection.notifyError(playersMessage);
                serverClientConnection.askToCloseConnection();
            }
        }
        this.gameObserver = null;
        this.gameEngine = null;

        LobbyHandler.getLobbyHandler().removeActiveGame(this);
    }

    /**
     * Gets the map of serverClientConnections.
     * @return
     */

    public Map<User, ServerClientConnection> getServerClientConnections () {
        return this.serverClientConnections;
    }
}
