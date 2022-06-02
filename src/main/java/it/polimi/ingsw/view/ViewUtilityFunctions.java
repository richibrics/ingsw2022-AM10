package it.polimi.ingsw.view;

import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.view.exceptions.IllegalGameModeException;
import it.polimi.ingsw.view.exceptions.IllegalUserPreferenceException;
import it.polimi.ingsw.view.game_objects.ClientTeams;
import it.polimi.ingsw.view.cli.CliConstants;
import it.polimi.ingsw.controller.ControllerConstants;

public class ViewUtilityFunctions {

    /**
     * Returns the index in the ClientTable of the player's schoolBoard
     *
     * @param playerId    the id of the player
     * @param clientTeams the teams received from the server
     * @return the index in the ClientTable of the player's schoolBoard
     */
    public static int getPlayerSchoolBoardIndex(int playerId, ClientTeams clientTeams) {
        // Get the id of the player's schoolBoard through the player id
        int indexOfTeam = clientTeams.getTeams().indexOf(clientTeams.getTeams().
                stream().
                filter(clientTeam -> clientTeam.getPlayers().
                        stream().
                        filter(clientPlayer -> clientPlayer.getPlayerId() == playerId).count() == 1).
                toList().get(0));

        int indexOfPlayer = clientTeams.getTeams().get(indexOfTeam).getPlayers().indexOf(clientTeams.getTeams().get(indexOfTeam).getPlayers().
                stream().
                filter(clientPlayer -> clientPlayer.getPlayerId() == playerId).
                toList().get(0));

        // Now shift the index of the player based on the index of the team
        int numberOfPlayers = (int) clientTeams.getTeams().stream().mapToLong(clientTeam -> clientTeam.getPlayers().size()).sum();

        switch (numberOfPlayers) {
            case ModelConstants.TWO_PLAYERS, ModelConstants.THREE_PLAYERS -> {
                return indexOfTeam;
            }
            case ModelConstants.FOUR_PLAYERS -> {
                return indexOfPlayer + (indexOfTeam == 1 ? 2 : 0);
            }
        }
        return -1;
    }

    /**
     * Converts the tuple ({@code gameMode}, {@code preference}) to the corresponding user preference (see controller constants).
     * @param gameMode the game mode
     * @param numberOfPlayers the number of players in the game
     * @return a user preference
     * @throws IllegalGameModeException if ({@code gameMode}, {@code preference}) cannot be converted
     * @see ControllerConstants
     */

    public static int getPreferenceFromGameModeAndClientPreference(String gameMode, int numberOfPlayers) throws IllegalGameModeException {
        switch (gameMode) {
            case CliConstants.EXPERT_GAME -> {
                switch (numberOfPlayers) {
                    case ViewConstants.TWO_PLAYERS_GAME -> {
                        return ControllerConstants.TWO_PLAYERS_PREFERENCE_EXPERT;
                    }
                    case ViewConstants.THREE_PLAYERS_GAME -> {
                        return ControllerConstants.THREE_PLAYERS_PREFERENCE_EXPERT;
                    }
                    case ViewConstants.FOUR_PLAYERS_GAME -> {
                        return ControllerConstants.FOUR_PLAYERS_PREFERENCE_EXPERT;
                    }
                }
            }
            case CliConstants.BASIC_GAME -> {
                switch (numberOfPlayers) {
                    case ViewConstants.TWO_PLAYERS_GAME -> {
                        return ControllerConstants.TWO_PLAYERS_PREFERENCE_EASY;
                    }
                    case ViewConstants.THREE_PLAYERS_GAME -> {
                        return ControllerConstants.THREE_PLAYERS_PREFERENCE_EASY;
                    }
                    case ViewConstants.FOUR_PLAYERS_GAME -> {
                        return ControllerConstants.FOUR_PLAYERS_PREFERENCE_EASY;
                    }
                }
            }
        }
        throw new IllegalGameModeException();
    }

    /**
     * Converts user preference {@code preference} to the corresponding tuple (gameMode, number of players).
     * @param preference the user preference
     * @return an array with game mode and number of players
     * @throws IllegalUserPreferenceException if the user preference cannot be converted
     * @see ControllerConstants
     */

    public static String[] getGameModeAndNumberOfPlayersFromPreference(int preference) throws IllegalUserPreferenceException {
        String[] returnValue = new String[2];
        switch(preference) {
            case ControllerConstants.TWO_PLAYERS_PREFERENCE_EXPERT -> {
                returnValue[0] = String.valueOf(ViewConstants.TWO_PLAYERS_GAME);
                returnValue[1] = CliConstants.EXPERT_GAME_COMPLETE;
            }
            case ControllerConstants.THREE_PLAYERS_PREFERENCE_EXPERT -> {
                returnValue[0] = String.valueOf(ViewConstants.THREE_PLAYERS_GAME);
                returnValue[1] = CliConstants.EXPERT_GAME_COMPLETE;
            }
            case ControllerConstants.FOUR_PLAYERS_PREFERENCE_EXPERT -> {
                returnValue[0] = String.valueOf(ViewConstants.FOUR_PLAYERS_GAME);
                returnValue[1] = CliConstants.EXPERT_GAME_COMPLETE;
            }
            case ControllerConstants.TWO_PLAYERS_PREFERENCE_EASY -> {
                returnValue[0] = String.valueOf(ViewConstants.TWO_PLAYERS_GAME);
                returnValue[1] = CliConstants.BASIC_GAME_COMPLETE;
            }
            case ControllerConstants.THREE_PLAYERS_PREFERENCE_EASY -> {
                returnValue[0] = String.valueOf(ViewConstants.THREE_PLAYERS_GAME);
                returnValue[1] = CliConstants.BASIC_GAME_COMPLETE;
            }
            case ControllerConstants.FOUR_PLAYERS_PREFERENCE_EASY -> {
                returnValue[0] = String.valueOf(ViewConstants.FOUR_PLAYERS_GAME);
                returnValue[1] = CliConstants.BASIC_GAME_COMPLETE;
            }
        }

        if (returnValue[0] == null && returnValue[1] == null)
            throw new IllegalUserPreferenceException();

        return returnValue;
    }
}
