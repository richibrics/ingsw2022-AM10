package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.exceptions.IllegalGameActionException;
import it.polimi.ingsw.model.exceptions.PlayerOrderNotSetException;
import it.polimi.ingsw.network.messages.ActionMessage;
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
    private final Map<User, ServerClientConnection> serverClientConnections;

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
     */
    public void startGame() {
        this.gameEngine = new GameEngine(this.createPlayersAndTeams());
        try {
            this.gameEngine.startGame();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            // TODO Work with exceptions, this exceptions isn't for client
        }
    }

    /**
     * Method called when a message with an action command is received from the network.
     * Runs the action through the GameEngine, passing {@code actionMessage} data.
     * This method checks if the player has the turn and that the requested action is inside Round,
     * else an error has been made by the client and must be warned.
     *
     * @param actionMessage
     */
    public void resumeGame(int playerId, ActionMessage actionMessage) {
        // TODO Error management
        // Player with turn check
        try {
            if (playerId != gameEngine.getRound().getCurrentPlayer())
                throw new IllegalGameActionException("The player hasn't the rights to perform the requested Action");
        } catch (PlayerOrderNotSetException e) {
            e.printStackTrace();
            return;
        } catch (IllegalGameActionException e) {
            // Warn the player
            return;
        }

        // Asked action check
        if(!gameEngine.getRound().getPossibleActions().contains(actionMessage.getActionId())) {
            // Warn the player
            return;
        }

        // Player has rights to perform that action
        try {
            getGameEngine().resumeGame(actionMessage.getActionId(), playerId, actionMessage.getOptions());
        } catch (Exception e) {
            e.printStackTrace();
            // TODO Work with exceptions, this exception may be for client
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
}
