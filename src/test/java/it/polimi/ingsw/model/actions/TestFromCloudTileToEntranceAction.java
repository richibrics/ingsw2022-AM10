package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.controller.User;
import it.polimi.ingsw.controller.exceptions.IllegalGameStateException;
import it.polimi.ingsw.controller.exceptions.WrongMessageContentException;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.exceptions.IllegalGameActionException;
import it.polimi.ingsw.model.managers.CommonManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TestFromCloudTileToEntranceAction {

    static FromCloudTileToEntranceAction fromCloudTileToEntranceAction;
    static SetUpTwoAndFourPlayersAction setUpTwoAndFourPlayersAction;
    static DrawFromBagToCloudAction drawFromBagToCloudAction;
    static GameEngine gameEngine;

    @BeforeEach
    void setUp() {
        User user1 = new User("1", 4);
        User user2 = new User("2", 4);
        User user3 = new User("3", 4);
        User user4 = new User("4", 4);
        Player player1 = new Player(user1, 1, 3);
        Player player2 = new Player(user2, 2, 3);
        Player player3 = new Player(user3, 3, 3);
        Player player4 = new Player(user4, 4, 3);
        ArrayList<Player> players1 = new ArrayList<>();
        players1.add(player1);
        players1.add(player2);
        Team team1 = new Team(1, players1);
        ArrayList<Player> players2 = new ArrayList<>();
        players2.add(player3);
        players2.add(player4);
        Team team2 = new Team(2, players2);
        ArrayList<Team> teams = new ArrayList<>();
        teams.add(team1);
        teams.add(team2);
        gameEngine = new GameEngine(teams);
        assertDoesNotThrow(() -> gameEngine.getActionManager().generateActions());

        fromCloudTileToEntranceAction = new FromCloudTileToEntranceAction(gameEngine);
        drawFromBagToCloudAction = new DrawFromBagToCloudTwoFourPlayersAction(gameEngine);
        setUpTwoAndFourPlayersAction = new SetUpTwoAndFourPlayersAction(gameEngine);
        assertDoesNotThrow(() -> setUpTwoAndFourPlayersAction.act());

        gameEngine.getAssistantManager().setWizard(1, 1);
        gameEngine.getAssistantManager().setWizard(2, 2);
        gameEngine.getAssistantManager().setWizard(3, 3);
        gameEngine.getAssistantManager().setWizard(4, 4);
    }

    @Test
    void setOptions() {
        HashMap<String, String> options = new HashMap<>();
        // No id
        assertThrows(WrongMessageContentException.class, () -> fromCloudTileToEntranceAction.setOptions(options));

        // Value error
        options.put(ModelConstants.ACTION_FROM_CLOUD_TILE_TO_ENTRANCE_KEY_CLOUD_ID, "a");
        assertThrows(WrongMessageContentException.class, () -> fromCloudTileToEntranceAction.setOptions(options));

        // OK
        options.put(ModelConstants.ACTION_FROM_CLOUD_TILE_TO_ENTRANCE_KEY_CLOUD_ID, "2");
        assertDoesNotThrow(() -> fromCloudTileToEntranceAction.setOptions(options));
        options.put(ModelConstants.ACTION_FROM_CLOUD_TILE_TO_ENTRANCE_KEY_CLOUD_ID, "3");
        assertDoesNotThrow(() -> fromCloudTileToEntranceAction.setOptions(options));
    }

    /**
     * Checks that if round ended, the check end match condition run;
     * if round not ended, it's turn of somebody else, and I set the correct actions in round.
     */
    @Test
    void modifyRoundAndActionList() {
        ArrayList<Integer> orderOfPlay = new ArrayList<>();
        orderOfPlay.add(2);
        orderOfPlay.add(3);
        orderOfPlay.add(1);
        orderOfPlay.add(4);
        assertDoesNotThrow(() -> gameEngine.getRound().setOrderOfPlay(orderOfPlay));

        // First player turn
        assertDoesNotThrow(() -> fromCloudTileToEntranceAction.modifyRoundAndActionList());

        // Second player turn
        assertEquals(2, fromCloudTileToEntranceAction.getGameEngine().getRound().getPossibleActions().size());
        assertTrue(fromCloudTileToEntranceAction.getGameEngine().getRound().getPossibleActions().contains(ModelConstants.ACTION_ON_SELECTION_OF_CHARACTER_CARD_ID));
        assertTrue(fromCloudTileToEntranceAction.getGameEngine().getRound().getPossibleActions().contains(ModelConstants.ACTION_MOVE_STUDENTS_FROM_ENTRANCE_ID));
        assertDoesNotThrow(() -> fromCloudTileToEntranceAction.modifyRoundAndActionList());

        // Third player turn
        assertEquals(2, fromCloudTileToEntranceAction.getGameEngine().getRound().getPossibleActions().size());
        assertTrue(fromCloudTileToEntranceAction.getGameEngine().getRound().getPossibleActions().contains(ModelConstants.ACTION_ON_SELECTION_OF_CHARACTER_CARD_ID));
        assertTrue(fromCloudTileToEntranceAction.getGameEngine().getRound().getPossibleActions().contains(ModelConstants.ACTION_MOVE_STUDENTS_FROM_ENTRANCE_ID));
        assertDoesNotThrow(() -> fromCloudTileToEntranceAction.modifyRoundAndActionList());

        // Fourth player turn
        assertEquals(2, fromCloudTileToEntranceAction.getGameEngine().getRound().getPossibleActions().size());
        assertTrue(fromCloudTileToEntranceAction.getGameEngine().getRound().getPossibleActions().contains(ModelConstants.ACTION_ON_SELECTION_OF_CHARACTER_CARD_ID));
        assertTrue(fromCloudTileToEntranceAction.getGameEngine().getRound().getPossibleActions().contains(ModelConstants.ACTION_MOVE_STUDENTS_FROM_ENTRANCE_ID));
        assertDoesNotThrow(() -> fromCloudTileToEntranceAction.modifyRoundAndActionList());

        // Round ended, check end match condition run which should call DrawFromBagToCloud which adds the OnSelectionOfAssistantsCard the round actions list
        assertFalse(fromCloudTileToEntranceAction.getGameEngine().getRound().getPossibleActions().contains(ModelConstants.ACTION_ON_SELECTION_OF_CHARACTER_CARD_ID));
        assertFalse(fromCloudTileToEntranceAction.getGameEngine().getRound().getPossibleActions().contains(ModelConstants.ACTION_MOVE_STUDENTS_FROM_ENTRANCE_ID));
        assertTrue(fromCloudTileToEntranceAction.getGameEngine().getRound().getPossibleActions().contains(ModelConstants.ACTION_ON_SELECTION_OF_ASSISTANTS_CARD_ID));
    }

    @Test
    void act() {
        drawFromBagToCloudAction.setPlayerId(ModelConstants.NO_PLAYER);
        assertDoesNotThrow(() -> drawFromBagToCloudAction.act()); // Fill the clouds

        fromCloudTileToEntranceAction.setPlayerId(2);
        Map<String, String> options = new HashMap<>();
        options.put(ModelConstants.ACTION_FROM_CLOUD_TILE_TO_ENTRANCE_KEY_CLOUD_ID, "2");
        assertDoesNotThrow(() -> fromCloudTileToEntranceAction.setOptions(options));

        /* Remove students from the entrance of player 2 */
        Integer[] studentIds = assertDoesNotThrow(() -> CommonManager.takePlayerById(gameEngine, 2).getSchoolBoard().getEntrance().stream().map(studentDisc -> studentDisc.getId()).toList().toArray(new Integer[0]));
        for (int i = 0; i < studentIds.length; i++) {
            int finalI = i;
            assertDoesNotThrow(() -> gameEngine.getSchoolPawnManager().moveStudentFromEntranceToDiningRoom(2, studentIds[finalI]));
        }
        assertDoesNotThrow(() -> fromCloudTileToEntranceAction.act());

        /* Check if the second player has 3 students in the entrance */
        assertEquals(ModelConstants.ACTION_MOVE_STUDENTS_FROM_ENTRANCE_NUMBER_OF_MOVEMENTS_TWO_FOUR_PLAYERS, assertDoesNotThrow(() -> CommonManager.takePlayerById(gameEngine, 2).getSchoolBoard().getEntrance().size()));

        /* Check if there are no students on the cloud 2 */
        assertEquals(assertDoesNotThrow(() -> gameEngine.getTable().getCloudTiles().get(1).peekStudents().size()), 0);

        // Now I test the exceptions
        options.clear();
        options.put(ModelConstants.ACTION_FROM_CLOUD_TILE_TO_ENTRANCE_KEY_CLOUD_ID, "-1");
        assertDoesNotThrow(() -> fromCloudTileToEntranceAction.setOptions(options));
        assertThrows(IllegalGameActionException.class, () -> fromCloudTileToEntranceAction.act());

        // Ask for cloud which has already been selected (hasn't students on it)
        options.clear();
        options.put(ModelConstants.ACTION_FROM_CLOUD_TILE_TO_ENTRANCE_KEY_CLOUD_ID, "2");
        assertDoesNotThrow(() -> fromCloudTileToEntranceAction.setOptions(options));
        assertThrows(IllegalGameActionException.class, () -> fromCloudTileToEntranceAction.act());

        // Unset table and check IllegalGameState
        gameEngine.setTable(null);
        assertThrows(IllegalGameStateException.class, () -> fromCloudTileToEntranceAction.act());
    }
}