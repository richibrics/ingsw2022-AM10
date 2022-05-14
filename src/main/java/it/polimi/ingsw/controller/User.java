package it.polimi.ingsw.controller;

/**
 * Class used by the {@code LobbyHandler} to manage the users in the lobby with their username ({@code id}) and their
 * match preference ({@code preference}), which is the number of players they want their match will have.
 * Used also in {@link it.polimi.ingsw.model.Player} to match the Player with the username
 */
public class User {
    final private String id;
    private int preference;

    public User(String id, int preference) {
        this.id = id;
        this.preference = preference;
    }

    /**
     * Returns the id of the User
     *
     * @return the id of the User
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the preference of the User
     *
     * @return the preference of the User
     */
    public int getPreference() {
        return preference;
    }

    /**
     * Changes the preference of the user .
     * @param preference the new preference of the user
     */

    public void changePreference(int preference) { this.preference = preference; }
}
