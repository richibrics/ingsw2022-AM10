package it.polimi.ingsw.view;

import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.view.game_objects.ClientTeams;

public class ViewUtilityFunctions {

    /**
     * Returns the index in the ClientTable of the player's schoolboard
     *
     * @param playerId    the id of the player
     * @param clientTeams the teams received from the server
     * @return the index in the ClientTable of the player's schoolboard
     */
    public static int getPlayerSchoolboardIndex(int playerId, ClientTeams clientTeams) {
        // Get the id of the player's schoolboard through the player id
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
}
