package it.polimi.ingsw.view.cli.drawers;

import it.polimi.ingsw.view.game_objects.ClientAssistantCard;
import it.polimi.ingsw.view.game_objects.ClientPlayer;
import it.polimi.ingsw.view.game_objects.ClientTeam;
import it.polimi.ingsw.view.game_objects.ClientTeams;

public class AssistantCardDrawer {

    /**
     * Generates and fills the matrix which contains the assistant cards of the player with index {@code indexOfPlayer}
     * of the team with index {@code indexOfTeam}.
     * @param clientTeams the ClientTeams object
     * @param indexOfTeam the index of the player's team
     * @param indexOfPlayer the index of the player
     * @return the matrix containing the representation of the assistant cards
     */

    public static String[][] generateAndFillTemplate(ClientTeams clientTeams, int indexOfTeam, int indexOfPlayer) {

        // Determine dimension of template
        int height = clientTeams.getTeams().get(indexOfTeam).getPlayers().get(indexOfPlayer).getAssistantCards().size() *
                (DrawersConstant.SPACE_BETWEEN_ASSISTANT_CARDS + 1) + DrawersConstant.SPACE_BETWEEN_LAST_CARD_OF_PLAYER_AND_ACTIVE_CARDS + 4;

        int numOfPlayersWithActiveAssistantCard = 0;
        for (ClientTeam clientTeam : clientTeams.getTeams())
            for (ClientPlayer clientPlayer : clientTeam.getPlayers())
                if (clientPlayer.getLastPlayedAssistantCard() != null)
                    numOfPlayersWithActiveAssistantCard++;

        if (numOfPlayersWithActiveAssistantCard > 0)
            height += DrawersConstant.SPACE_BETWEEN_TITLE_AND_FIRST_ACTIVE_CARD + 1 +
                    numOfPlayersWithActiveAssistantCard * (2 + DrawersConstant.SPACE_BETWEEN_USERNAME_AND_ACTIVE_CARD) +
                    (numOfPlayersWithActiveAssistantCard - 1) * DrawersConstant.SPACE_BETWEEN_ACTIVE_ASSISTANT_CARDS;

        String[][] template = new String[height][DrawersConstant.LENGTH_OF_ASSISTANT_CARD];
        CliDrawersUtilityFunctions.removeNullAndAddSingleSpace(template);
        fillTemplate(template, clientTeams, indexOfTeam, indexOfPlayer);
        return template;
    }

    /**
     * Fills the template with the assistant cards. The template contains the unused assistant cards of the player and the
     * active assistant cards of all the players in the game.
     * @param template the template with the representation of the assistant cards
     * @param clientTeams the ClientTeams object
     * @param indexOfTeam the index of the team containing the player
     * @param indexOfPlayer the index of the player
     */

    private static void fillTemplate(String[][] template, ClientTeams clientTeams, int indexOfTeam, int indexOfPlayer) {

        int heightOffset = 1;
        int lengthOffset;
        writeString(template, "Available assistant cards:".toCharArray(), heightOffset, 0);
        heightOffset+=2;
        for (ClientAssistantCard clientAssistantCard : clientTeams.getTeams().get(indexOfTeam).getPlayers().get(indexOfPlayer).getAssistantCards()) {
            lengthOffset = 0;

            template[heightOffset][lengthOffset] = DrawersConstant.RESET;
            lengthOffset++;

            // Write card value
            lengthOffset = writeString(template, ("value: " + clientAssistantCard.getCardValue()).toCharArray(), heightOffset, lengthOffset) +
                    DrawersConstant.SPACE_BETWEEN_LABELS;

            // Write movements of mother nature
            writeString(template, ("movements: " + clientAssistantCard.getMovements()).toCharArray(), heightOffset, lengthOffset);

            heightOffset += DrawersConstant.SPACE_BETWEEN_ASSISTANT_CARDS + 1;
        }

        if (clientTeams.getTeams().get(indexOfTeam).getPlayers().get(indexOfPlayer).getAssistantCards().size() != 0) {

            heightOffset = heightOffset - DrawersConstant.SPACE_BETWEEN_ASSISTANT_CARDS + DrawersConstant.SPACE_BETWEEN_LAST_CARD_OF_PLAYER_AND_ACTIVE_CARDS;

            writeString(template, "Active assistant cards:".toCharArray(), heightOffset, 0);

            heightOffset += DrawersConstant.SPACE_BETWEEN_TITLE_AND_FIRST_ACTIVE_CARD + 1;

            for (ClientTeam clientTeam : clientTeams.getTeams())
                for (ClientPlayer clientPlayer : clientTeam.getPlayers())
                    if (clientPlayer.getLastPlayedAssistantCard() != null) {
                        // Write username
                        writeString(template, ("username: " + clientPlayer.getUsername()).toCharArray(), heightOffset, 0);

                        heightOffset += DrawersConstant.SPACE_BETWEEN_USERNAME_AND_ACTIVE_CARD + 1;

                        lengthOffset = 0;

                        // Write card value
                        lengthOffset = writeString(template, ("value: " + clientPlayer.getLastPlayedAssistantCard().getCardValue()).toCharArray(), heightOffset, lengthOffset) +
                                DrawersConstant.SPACE_BETWEEN_LABELS;

                        // Write movements of mother nature
                        writeString(template, ("movements: " + clientPlayer.getLastPlayedAssistantCard().getMovements()).toCharArray(), heightOffset, lengthOffset);

                        heightOffset += DrawersConstant.SPACE_BETWEEN_ACTIVE_ASSISTANT_CARDS + 1;
                    }
        }
    }

    /**
     * Writes the characters of {@code label} in {@code template} starting from ({@code height}, {@code startingLength}).
     * The height does not change.
     * @param template the template containing the representation of the assistant cards
     * @param label the array with the characters that are inserted into {@code template}
     * @param height the row
     * @param startingLength the starting length
     * @return the length at the end of the process
     */

    private static int writeString(String[][] template, char[] label, int height, int startingLength) {
        for (char c : label) {
            template[height][startingLength] = String.valueOf(c);
            startingLength++;
        }
        // Return new length
        return startingLength;
    }
}