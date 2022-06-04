package it.polimi.ingsw.view;

import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.view.cli.drawers.DrawersConstant;
import it.polimi.ingsw.view.exceptions.*;
import it.polimi.ingsw.view.game_objects.ClientPawnColor;
import it.polimi.ingsw.view.game_objects.ClientTeams;
import it.polimi.ingsw.view.cli.CliConstants;
import it.polimi.ingsw.controller.ControllerConstants;
import it.polimi.ingsw.view.gui.GUIConstants;

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

    public static String convertCharacterIdToImagePath(int id) throws IllegalCharacterIdException {
        switch (id) {
            case 1:
                return GUIConstants.SCENE_TABLE_FRIAR_IMAGE_PATH;
            case 2:
                return GUIConstants.SCENE_TABLE_COOK_IMAGE_PATH;
            case 3:
                return GUIConstants.SCENE_TABLE_AMBASSADOR_IMAGE_PATH;
            case 4:
                return GUIConstants.SCENE_TABLE_MAILMAN_IMAGE_PATH;
            case 5:
                return GUIConstants.SCENE_TABLE_HERBALIST_IMAGE_PATH;
            case 6:
                return GUIConstants.SCENE_TABLE_CENTAUR_IMAGE_PATH;
            case 7:
                return GUIConstants.SCENE_TABLE_JESTER_IMAGE_PATH;
            case 8:
                return GUIConstants.SCENE_TABLE_KNIGHT_IMAGE_PATH;
            case 9:
                return GUIConstants.SCENE_TABLE_MUSHROOM_HUNTER_IMAGE_PATH;
            case 10:
                return GUIConstants.SCENE_TABLE_MINSTREL_IMAGE_PATH;
            case 11:
                return GUIConstants.SCENE_TABLE_LADY_IMAGE_PATH;
            case 12:
                return GUIConstants.SCENE_TABLE_THIEF_IMAGE_PATH;
            default:
                throw new IllegalCharacterIdException();
        }
    }

    public static int convertAssistantStringToId (String assistant) throws  IllegalAssistantException {
        switch (assistant) {
            case GUIConstants.ASSISTANT_1:
                return ModelConstants.MIN_VALUE_OF_ASSISTANT_CARD;
            case GUIConstants.ASSISTANT_2:
                return ModelConstants.MIN_VALUE_OF_ASSISTANT_CARD + ModelConstants.OFFSET_BETWEEN_ASSISTANT_CARDS;
            case GUIConstants.ASSISTANT_3:
                return ModelConstants.MIN_VALUE_OF_ASSISTANT_CARD + 2 * ModelConstants.OFFSET_BETWEEN_ASSISTANT_CARDS;
            case GUIConstants.ASSISTANT_4:
                return ModelConstants.MIN_VALUE_OF_ASSISTANT_CARD + 3 * ModelConstants.OFFSET_BETWEEN_ASSISTANT_CARDS;
            case GUIConstants.ASSISTANT_5:
                return ModelConstants.MIN_VALUE_OF_ASSISTANT_CARD + 4 * ModelConstants.OFFSET_BETWEEN_ASSISTANT_CARDS;
            case GUIConstants.ASSISTANT_6:
                return ModelConstants.MIN_VALUE_OF_ASSISTANT_CARD + 5 * ModelConstants.OFFSET_BETWEEN_ASSISTANT_CARDS;
            case GUIConstants.ASSISTANT_7:
                return ModelConstants.MIN_VALUE_OF_ASSISTANT_CARD + 6 * ModelConstants.OFFSET_BETWEEN_ASSISTANT_CARDS;
            case GUIConstants.ASSISTANT_8:
                return ModelConstants.MIN_VALUE_OF_ASSISTANT_CARD + 7 * ModelConstants.OFFSET_BETWEEN_ASSISTANT_CARDS;
            case GUIConstants.ASSISTANT_9:
                return ModelConstants.MIN_VALUE_OF_ASSISTANT_CARD + 8 * ModelConstants.OFFSET_BETWEEN_ASSISTANT_CARDS;
            case GUIConstants.ASSISTANT_10:
                return ModelConstants.MIN_VALUE_OF_ASSISTANT_CARD + 9 * ModelConstants.OFFSET_BETWEEN_ASSISTANT_CARDS;
            default:
                throw new IllegalAssistantException();
        }
    }

    public static int convertIdOfImageOfStudentDisc(String id) {
        return Integer.parseInt(id.replace(GUIConstants.STUDENT_DISC_NAME, ""));
    }

    public static int convertStudentIdToIdOfColor(int studentId) throws IllegalStudentIdException {
        if (studentId >= DrawersConstant.STARTING_ID_YELLOW && studentId <= DrawersConstant.ENDING_ID_YELLOW)
            return ClientPawnColor.YELLOW.getId();
        else if (studentId >= DrawersConstant.STARTING_ID_BLUE && studentId <= DrawersConstant.ENDING_ID_BLUE)
            return ClientPawnColor.BLUE.getId();
        else if (studentId >= DrawersConstant.STARTING_ID_GREEN && studentId <= DrawersConstant.ENDING_ID_GREEN)
            return ClientPawnColor.GREEN.getId();
        else if (studentId >= DrawersConstant.STARTING_ID_RED && studentId <= DrawersConstant.ENDING_ID_RED)
            return ClientPawnColor.RED.getId();
        else if (studentId >= DrawersConstant.STARTING_ID_PINK && studentId <= DrawersConstant.ENDING_ID_PINK)
            return ClientPawnColor.PINK.getId();
        else throw new IllegalStudentIdException();
    }

    public static String convertStudentIdToStudentIdForImageView(int studentId) {
        return GUIConstants.STUDENT_DISC_NAME + String.valueOf(studentId);
    }

    public static int convertPawnColorIndexToLaneIndex(int pawnColorIndex) throws IllegalLaneException {
        if (pawnColorIndex == ClientPawnColor.YELLOW.getId())
            return GUIConstants.INDEX_YELLOW_LANE;
        else if (pawnColorIndex == ClientPawnColor.BLUE.getId())
            return GUIConstants.INDEX_BLUE_LANE;
        else if (pawnColorIndex == ClientPawnColor.GREEN.getId())
            return GUIConstants.INDEX_GREEN_LANE;
        else if (pawnColorIndex == ClientPawnColor.RED.getId())
            return GUIConstants.INDEX_RED_LANE;
        else if (pawnColorIndex == ClientPawnColor.PINK.getId())
            return GUIConstants.INDEX_PINK_LANE;
        else throw new IllegalLaneException();
    }

    public static String convertProfessorIdToProfessorIdForImageView(int professorId) {
        return GUIConstants.PROFESSOR_PAWN_NAME + String.valueOf(professorId);
    }

    public static int convertIdOfImageOfProfessorPawn(String id) {
        return Integer.parseInt(id.replace(GUIConstants.PROFESSOR_PAWN_NAME, ""));
    }
}