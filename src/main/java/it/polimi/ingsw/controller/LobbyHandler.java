package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exceptions.InterruptedGameException;
import it.polimi.ingsw.controller.exceptions.UserNotFoundException;
import it.polimi.ingsw.controller.observers.LobbyObserver;
import it.polimi.ingsw.controller.observers.Observer;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.managers.CommonManager;
import it.polimi.ingsw.network.MessageTypes;
import it.polimi.ingsw.network.NetworkConstants;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.server.ServerClientConnection;

import java.util.*;

/**
 * Class that manages the client in the lobby and the new match creation.
 */
public class LobbyHandler {

    private Map<Integer, Map<User, ServerClientConnection>> clientsWaiting;
    private static LobbyHandler lobbyHandler;
    private Observer lobbyObserver;

    private List<GameController> activeGames;

    private LobbyHandler() {
        this.clientsWaiting = new HashMap<>();
        this.lobbyObserver = new LobbyObserver(this.clientsWaiting);
        this.activeGames = new ArrayList<>();
    }

    /**
     * Returns the instance of the LobbyHandler.
     *
     * @return the instance of LobbyHandler
     */

    public static LobbyHandler getLobbyHandler() {
        if (lobbyHandler == null)
            lobbyHandler = new LobbyHandler();
        return lobbyHandler;
    }

    /**
     * Adds a user to the list of clients waiting for a game to start. After the addition of the user this method starts
     * a game if the number of users with same preference equals the preference.
     *
     * @param user                   the user to add
     * @param serverClientConnection the server-client connection associated with the user
     * @throws IllegalArgumentException if the preference of the user is invalid
     * @throws InterruptedGameException if something bad happens during match creation
     * @see User
     * @see ServerClientConnection
     */

    public void addClient(User user, ServerClientConnection serverClientConnection) throws IllegalArgumentException, InterruptedGameException {
        switch (user.getPreference()) {
            case ControllerConstants.TWO_PLAYERS_PREFERENCE_EXPERT:
                this.checkStateOfClientsWaiting(ControllerConstants.TWO_PLAYERS_PREFERENCE_EXPERT, user, serverClientConnection);
                break;

            case ControllerConstants.THREE_PLAYERS_PREFERENCE_EXPERT:
                this.checkStateOfClientsWaiting(ControllerConstants.THREE_PLAYERS_PREFERENCE_EXPERT, user, serverClientConnection);
                break;

            case ControllerConstants.FOUR_PLAYERS_PREFERENCE_EXPERT:
                this.checkStateOfClientsWaiting(ControllerConstants.FOUR_PLAYERS_PREFERENCE_EXPERT, user, serverClientConnection);
                break;

            case ControllerConstants.TWO_PLAYERS_PREFERENCE_EASY:
                this.checkStateOfClientsWaiting(ControllerConstants.TWO_PLAYERS_PREFERENCE_EASY, user, serverClientConnection);
                break;

            case ControllerConstants.THREE_PLAYERS_PREFERENCE_EASY:
                this.checkStateOfClientsWaiting(ControllerConstants.THREE_PLAYERS_PREFERENCE_EASY, user, serverClientConnection);
                break;

            case ControllerConstants.FOUR_PLAYERS_PREFERENCE_EASY:
                this.checkStateOfClientsWaiting(ControllerConstants.FOUR_PLAYERS_PREFERENCE_EASY, user, serverClientConnection);
                break;

            default:
                throw new IllegalArgumentException("The preference of the user is incorrect");
        }
        // Arrived here, the user has been added: notify all the clients about the new lobby state
        this.lobbyObserver.notifyClients();
    }

    /**
     * Checks if the key {@code gameMode} is already in the map which contains the clients waiting for a game to start.
     * If the key is not present, {@code gameMode} is added to the map; if the key is in the map, {@code user} is added to
     * the lobby. After the addition of the user, this method starts a game if the number of users with same preference
     * matches the game mode.
     *
     * @param gameMode the game mode
     * @param user the user
     * @param serverClientConnection the server-client connection associated with the user
     * @throws InterruptedGameException if something bad happens during match creation
     */

    private void checkStateOfClientsWaiting(int gameMode, User user, ServerClientConnection serverClientConnection) throws InterruptedGameException {
        if (!this.clientsWaiting.containsKey(gameMode)) {
            Map<User, ServerClientConnection> clients = new HashMap<>();
            clients.put(user, serverClientConnection);
            this.clientsWaiting.put(gameMode, clients);
        } else {
            this.clientsWaiting.get(gameMode).put(user, serverClientConnection);
            if (this.clientsWaiting.get(gameMode).size() == (gameMode > 0 ? gameMode : - gameMode))
                this.generateGame(gameMode);
        }
    }

    /**
     * Changes the preference of the user with username {@code userId}.
     *
     * @param userId        the username of the user
     * @param newPreference the new preference
     * @throws IllegalArgumentException if the preference is invalid
     * @throws UserNotFoundException    if the user with username {@code userId} could not be found
     * @see User
     */

    public void changePreference(String userId, int newPreference) throws IllegalArgumentException, UserNotFoundException, InterruptedGameException {
        if (newPreference != ControllerConstants.TWO_PLAYERS_PREFERENCE_EXPERT
                && newPreference != ControllerConstants.THREE_PLAYERS_PREFERENCE_EXPERT
                && newPreference != ControllerConstants.FOUR_PLAYERS_PREFERENCE_EXPERT
                && newPreference != ControllerConstants.TWO_PLAYERS_PREFERENCE_EASY
                && newPreference != ControllerConstants.THREE_PLAYERS_PREFERENCE_EASY
                && newPreference != ControllerConstants.FOUR_PLAYERS_PREFERENCE_EASY)
            throw new IllegalArgumentException("Invalid preference");
        else {
            // Get from clientsWaiting the original user
            User user = this.getUserById(userId);
            // Now get his ServerClientConnection that we will move with its user in the new preference list
            ServerClientConnection serverClientConnection = this.clientsWaiting.get(user.getPreference()).get(user);
            // Remove the user from the clientsWaiting list of the oldPreference
            this.clientsWaiting.get(user.getPreference()).remove(user);
            // Change user preference
            user.changePreference(newPreference);
            // Add the user to the new list with the newPreference
            this.addClient(user, serverClientConnection);
        }
    }

    /**
     * Gets the user with {@code userId}.
     *
     * @param userId the username of the user
     * @return the user with username {@code userId}
     * @throws UserNotFoundException if the user could not be found
     * @see User
     */

    private User getUserById(String userId) throws UserNotFoundException {
        User userToReturn = null;
        for (User user : this.clientsWaiting.values().stream().flatMap(map -> map.keySet().stream()).toList())
            if (user.getId().equals(userId))
                userToReturn = user;
        if (userToReturn == null)
            throw new UserNotFoundException();
        return userToReturn;
    }

    /**
     * Generates the game when the number of users with same preference equals the preference. This method is called by
     * addUser.
     * The generated game then is added to the active games list, which is used to check
     * if a User's name is already in use.
     *
     * @param preference the number of players participating in the game
     * @throws InterruptedGameException if an error was encountered during match creation.
     */

    private void generateGame(int preference) throws InterruptedGameException {
        Map<User, ServerClientConnection> map = new HashMap<>(this.clientsWaiting.get(preference));
        this.clientsWaiting.get(preference).clear();
        GameController gameController = new GameController(map);

        for (ServerClientConnection serverClientConnection : map.values())
            serverClientConnection.setGameController(gameController);

        this.activeGames.add(gameController);
        // Determine if the game is an expert game or an easy game
        boolean expertGame;
        if (preference > 0 )
            expertGame = true;
        else
            expertGame = false;

        gameController.startGame(expertGame);
    }

    /**
     * Checks if a username is used by a user.
     *
     * @param userId the username
     * @return true if the username is used by a user, false otherwise
     * @see User
     */

    public boolean checkIfUsernameIsUsed(String userId) {
        // Search in lobby
        for (User user : this.clientsWaiting.values().stream().flatMap(map -> map.keySet().stream()).toList())
            if (user.getId().equals(userId))
                return true;
        // Search in active games
        for (GameController gameController: this.activeGames) {
            for (Team team: gameController.getGameEngine().getTeams()) {
                for (Player player: team.getPlayers()) {
                    if (player.getUsername().equals(userId))
                            return true;
                }
            }
        }
        return false;
    }

    /**
     * Gets a copy of the map with the clients waiting for a game to start.
     *
     * @return the map with the clients waiting
     */

    public Map<Integer, Map<User, ServerClientConnection>> getClientsWaiting() {
        return new HashMap<>(this.clientsWaiting);
    }

    /**
     * Clears the map. Method used by tests in TestSerializer.
     */
    public void emptyMap() {
        for (Map<User, ServerClientConnection> map : this.clientsWaiting.values())
            map.clear();
    }

    /**
     * Removes the active game from the activeGames list (if present: this could be called for an error during game
     * start when I don't have the game in the list yet), because it ended.
     * The interruptGame in GameController calls this.
     *
     * @param gameController the game controller of the match
     */
    public void removeActiveGame(GameController gameController) {
        activeGames.remove(gameController);
    }

    /**
     * Removes the active game from the activeGames list, because it ended.
     * Will be called by the CheckEndMatchCondition when the match ends (that action can only pass a username, doesn't know
     * what a GameController is, so this method is necessary).
     *
     * @param username the username of a player in that game.
     * @throws NoSuchElementException if the username could not be found in any active game
     */

    public void removeActiveGameAndCommunicateWinners(String username, Integer[] winners) throws NoSuchElementException {
        GameController gameController = null;
        for (int i = 0; i < this.activeGames.size(); i++) {
            for (Team team : this.activeGames.get(i).getGameEngine().getTeams()) {
                for (Player player : team.getPlayers()) {
                    if (player.getUsername().equals(username))
                        gameController = this.activeGames.get(i);
                }
            }
        }
        if (gameController == null)
            throw new NoSuchElementException("The requested Game could not be found");

        // Communicate winners
        for (Map.Entry<User, ServerClientConnection> entry : gameController.getServerClientConnections().entrySet()) {
            GameController finalGameController = gameController;
            if (Arrays.stream(winners).filter(id -> id == CommonManager.getTeamIdByUsername(finalGameController.getGameEngine(), entry.getKey().getId())).count() == 1) {
                entry.getValue().sendMessage(new Message(MessageTypes.END_GAME, NetworkConstants.MESSAGE_FOR_WINNERS));
            }
            else
                entry.getValue().sendMessage(new Message(MessageTypes.END_GAME, NetworkConstants.MESSAGE_FOR_LOSERS));
        }

        // Remove the game controller
        activeGames.remove(gameController);
    }

    /**
     * Removes the user and his ServerClientConnection from the users in lobby.
     * Clients in lobby are notified for this lobby edit.
     *
     * @param user the user that has to be removed
     * @throws NoSuchElementException if the User could not be found in waiting users
     * @see User
     */
    public void removeDisconnectedUser(User user) throws NoSuchElementException {
        for (Map<User, ServerClientConnection> lobby : this.clientsWaiting.values()) {
            if (lobby.containsKey(user)) {
                lobby.remove(user);
                lobbyObserver.notifyClients();
                return;
            }
        }
        throw new NoSuchElementException("The requested User could not be found in the lobby");
    }
}