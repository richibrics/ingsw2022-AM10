package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exceptions.InterruptedGameException;
import it.polimi.ingsw.controller.exceptions.UserNotFoundException;
import it.polimi.ingsw.network.server.ServerClientConnection;

import java.util.HashMap;
import java.util.Map;

public class LobbyHandler {

    private Map<Integer, Map<User, ServerClientConnection>> clientsWaiting;
    private static LobbyHandler lobbyHandler;

    private LobbyHandler() {
        this.clientsWaiting = new HashMap<>();
    }

    /**
     * Returns the instance of the LobbyHandler.
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
     * @param user the user to add
     * @param serverClientConnection the server-client connection associated with the user
     * @throws IllegalArgumentException if the preference of the user is invalid
     * @throws InterruptedGameException if an error was encountered during match creation.
     * @see User
     * @see ServerClientConnection
     */

    public void addClient(User user, ServerClientConnection serverClientConnection) throws IllegalArgumentException, InterruptedGameException {
        switch (user.getPreference()) {
            case ControllerConstants.TWO_PLAYERS_PREFERENCE:
                if (!this.clientsWaiting.containsKey(ControllerConstants.TWO_PLAYERS_PREFERENCE)) {
                    Map<User, ServerClientConnection> clients = new HashMap<>();
                    clients.put(user, serverClientConnection);
                    this.clientsWaiting.put(ControllerConstants.TWO_PLAYERS_PREFERENCE, clients);
                } else {
                    this.clientsWaiting.get(ControllerConstants.TWO_PLAYERS_PREFERENCE).put(user, serverClientConnection);
                    if (this.clientsWaiting.get(ControllerConstants.TWO_PLAYERS_PREFERENCE).size() == ControllerConstants.TWO_PLAYERS_PREFERENCE)
                        this.generateGame(ControllerConstants.TWO_PLAYERS_PREFERENCE);
                }
                break;

            case ControllerConstants.THREE_PLAYERS_PREFERENCE:
                if (!this.clientsWaiting.containsKey(ControllerConstants.THREE_PLAYERS_PREFERENCE)) {
                    Map<User, ServerClientConnection> clients = new HashMap<>();
                    clients.put(user, serverClientConnection);
                    this.clientsWaiting.put(ControllerConstants.THREE_PLAYERS_PREFERENCE, clients);
                } else {
                    this.clientsWaiting.get(ControllerConstants.THREE_PLAYERS_PREFERENCE).put(user, serverClientConnection);
                    if (this.clientsWaiting.get(ControllerConstants.THREE_PLAYERS_PREFERENCE).size() == ControllerConstants.THREE_PLAYERS_PREFERENCE)
                        this.generateGame(ControllerConstants.THREE_PLAYERS_PREFERENCE);
                }
                break;

            case ControllerConstants.FOUR_PLAYERS_PREFERENCE:
                if (!this.clientsWaiting.containsKey(ControllerConstants.FOUR_PLAYERS_PREFERENCE)) {
                    Map<User, ServerClientConnection> clients = new HashMap<>();
                    clients.put(user, serverClientConnection);
                    this.clientsWaiting.put(ControllerConstants.FOUR_PLAYERS_PREFERENCE, clients);
                } else {
                    this.clientsWaiting.get(ControllerConstants.FOUR_PLAYERS_PREFERENCE).put(user, serverClientConnection);
                    if (this.clientsWaiting.get(ControllerConstants.FOUR_PLAYERS_PREFERENCE).size() == ControllerConstants.FOUR_PLAYERS_PREFERENCE)
                        this.generateGame(ControllerConstants.FOUR_PLAYERS_PREFERENCE);
                }
                break;

            default:
                throw new IllegalArgumentException("The preference of the user is incorrect");
        }
    }

    /**
     * Changes the preference of the user with username {@code userId}.
     * @param userId the username of the user
     * @param newPreference the new preference
     * @throws IllegalArgumentException if the preference is invalid
     * @throws UserNotFoundException if the user with username {@code userId} could not be found
     * @see User
     */

    public void changePreference(String userId, int newPreference) throws IllegalArgumentException, UserNotFoundException {
        if (newPreference != ControllerConstants.TWO_PLAYERS_PREFERENCE
                && newPreference != ControllerConstants.THREE_PLAYERS_PREFERENCE
                && newPreference != ControllerConstants.FOUR_PLAYERS_PREFERENCE)
            throw new IllegalArgumentException("Invalid preference");
        else {
            User user = this.getUserById(userId);
            user.changePreference(newPreference);
        }
    }

    /**
     * Gets the user with {@code userId}.
     * @param userId the username of the user
     * @return the user with username {@code userId}
     * @throws UserNotFoundException if the user could not be found
     * @see User
     */

    private User getUserById(String userId) throws UserNotFoundException {
        User userToReturn = null;
        for (User user : this.clientsWaiting.values().stream().flatMap(map->map.keySet().stream()).toList())
            if (user.getId().equals(userId))
                userToReturn = user;
        if (userToReturn == null)
            throw new UserNotFoundException();
        return userToReturn;
    }

    /**
     * Generates the game when the number of users with same preference equals the preference. This method is called by
     * addUser.
     * @param preference the number of players participating in the game
     * @throws InterruptedGameException if an error was encountered during match creation.
     */

    private void generateGame(int preference) throws InterruptedGameException {
        Map<User, ServerClientConnection> map = new HashMap<>(this.clientsWaiting.get(preference));
        this.clientsWaiting.get(preference).clear();
        GameController gameController = new GameController(map);

        for (ServerClientConnection serverClientConnection : map.values())
            serverClientConnection.setGameController(gameController);

        gameController.startGame();
    }

    /**
     * Checks if a username is used by a user.
     * @param userId the username
     * @return true if the username is used by a user, false otherwise
     * @see User
     */

    public boolean checkIfUsernameIsUsed(String userId) {
        for (User user : this.clientsWaiting.values().stream().flatMap(map->map.keySet().stream()).toList())
            if (user.getId().equals(userId))
                return true;
        return false;
    }

    /**
     * Gets a copy of the map with the clients waiting for a game to start.
     * @return the map with the clients waiting
     */

    public Map<Integer, Map<User, ServerClientConnection>> getClientsWaiting() {
        return new HashMap<>(this.clientsWaiting);
    }
}