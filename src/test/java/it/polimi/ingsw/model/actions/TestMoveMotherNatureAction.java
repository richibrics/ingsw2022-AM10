package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.controller.User;
import it.polimi.ingsw.controller.exceptions.IllegalGameStateException;
import it.polimi.ingsw.controller.exceptions.WrongMessageContentException;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.exceptions.IllegalGameActionException;
import it.polimi.ingsw.model.game_components.Bag;
import it.polimi.ingsw.model.game_components.Character;
import it.polimi.ingsw.model.game_components.CharacterCard;
import it.polimi.ingsw.model.managers.CommonManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TestMoveMotherNatureAction {
    static MoveMotherNatureAction moveMotherNatureAction;
    static GameEngine gameEngine;

    @BeforeEach
    void setUp() {
        User user1 = new User("1", 3);
        User user2 = new User("2", 3);
        User user3 = new User("3", 3);
        Player player1 = new Player(user1, 1, 3);
        Player player2 = new Player(user2, 2, 3);
        Player player3 = new Player(user3, 3, 3);
        ArrayList<Player> players1 = new ArrayList<>();
        players1.add(player1);
        Team team1 = new Team(1, players1);
        ArrayList<Player> players2 = new ArrayList<>();
        players2.add(player2);
        Team team2 = new Team(2, players2);
        ArrayList<Player> players3 = new ArrayList<>();
        players3.add(player3);
        Team team3 = new Team(3, players3);
        ArrayList<Team> teams = new ArrayList<>();
        teams.add(team1);
        teams.add(team2);
        teams.add(team3);
        gameEngine = new GameEngine(teams);
        assertDoesNotThrow(() -> gameEngine.getActionManager().generateActions());


        SetUpThreePlayersAction setUpThreePlayersAction = new SetUpThreePlayersAction(gameEngine);
        assertDoesNotThrow(() -> setUpThreePlayersAction.act());

        moveMotherNatureAction = new MoveMotherNatureAction(gameEngine);

        gameEngine.getAssistantManager().setWizard(1,1);
        gameEngine.getAssistantManager().setWizard(2,2);
        gameEngine.getAssistantManager().setWizard(3,3);

        ArrayList<Integer> order = new ArrayList<>();
        order.add(1);
        order.add(2);
        order.add(3);
        gameEngine.getRound().setOrderOfPlay(order);

        ArrayList<Integer> nextActions = new ArrayList<>();
        nextActions.add(ModelConstants.ACTION_MOVE_MOTHER_NATURE_ID);
        gameEngine.getRound().setPossibleActions(nextActions);
    }

    /**
     * Tests the correct parsing of the options
     */
    @Test
    void setOptions() {
        HashMap<String, String> options = new HashMap<>();

        // No id
        assertThrows(WrongMessageContentException.class, () -> moveMotherNatureAction.setOptions(options));

        // Parse error
        options.put(ModelConstants.ACTION_MOVE_MOTHER_NATURE_OPTIONS_KEY_ISLAND, "a");
        assertThrows(WrongMessageContentException.class, () -> moveMotherNatureAction.setOptions(options));

        // < 1
        options.put(ModelConstants.ACTION_MOVE_MOTHER_NATURE_OPTIONS_KEY_ISLAND, "0");
        assertThrows(WrongMessageContentException.class, () -> moveMotherNatureAction.setOptions(options));

        // > 12
        options.put(ModelConstants.ACTION_MOVE_MOTHER_NATURE_OPTIONS_KEY_ISLAND, "13");
        assertThrows(WrongMessageContentException.class, () -> moveMotherNatureAction.setOptions(options));

        // ok
        options.put(ModelConstants.ACTION_MOVE_MOTHER_NATURE_OPTIONS_KEY_ISLAND, "1");
        assertDoesNotThrow(() -> moveMotherNatureAction.setOptions(options));

        // ok
        options.put(ModelConstants.ACTION_MOVE_MOTHER_NATURE_OPTIONS_KEY_ISLAND, "12");
        assertDoesNotThrow(() -> moveMotherNatureAction.setOptions(options));
    }

    /**
     * Tests a MM movement declared by the options.
     * Then asks for an illegal movement.
     * Ends with table and assistant card unset to test the illegal game state.
     */
    @Test
    void act() {
        HashMap<String, String> options = new HashMap<>();
        int nextIslandId;
        gameEngine.getAssistantManager().setAssistantCard(1, 2);

        // Move MM to the island with id = current id + 1: operation okay
        nextIslandId = (assertDoesNotThrow(()->gameEngine.getIslandManager().getMotherNatureIslandId()) % 12) + 1;
        options.put(ModelConstants.ACTION_MOVE_MOTHER_NATURE_OPTIONS_KEY_ISLAND, String.valueOf(nextIslandId));
        moveMotherNatureAction.setPlayerId(1);
        assertDoesNotThrow(() -> moveMotherNatureAction.setOptions(options));
        assertDoesNotThrow(() -> moveMotherNatureAction.act());

        // Move MM where the player can't place it (6 step forward, user has max 1 movement)
        nextIslandId = ((nextIslandId + 6) % 12) + 1;
        options.put(ModelConstants.ACTION_MOVE_MOTHER_NATURE_OPTIONS_KEY_ISLAND, String.valueOf(nextIslandId));
        moveMotherNatureAction.setPlayerId(1);
        assertDoesNotThrow(() -> moveMotherNatureAction.setOptions(options));
        assertThrows(IllegalGameActionException.class, () -> moveMotherNatureAction.act()); // Exception thrown

        // Unset the assistant, should throw
        assertDoesNotThrow(()-> CommonManager.takePlayerById(gameEngine,1).popActiveAssistantCard());
        assertThrows(IllegalGameStateException.class, () -> moveMotherNatureAction.act()); // Exception thrown

        // Unset the table (set a new assistant card), should throw
        gameEngine.getAssistantManager().setAssistantCard(1, 3);
        gameEngine.setTable(null);
        assertThrows(IllegalGameStateException.class, () -> moveMotherNatureAction.act()); // Exception thrown
    }

    /**
     * Checks that MoveMotherNatureAction is removed from the Round and that Calculate influence had been executed.
     * Also checks that if I run again modifyRoundAndActionList, an exception is thrown because this action isn't
     * in round actions list
     */
    @Test
    void modifyRoundAndActionList() {
        // State check to assert that Calculate Influence will insert FromCloudTileToEntranceAction to the round Actions
        assertFalse(gameEngine.getRound().getPossibleActions().contains(ModelConstants.ACTION_FROM_CLOUD_TILE_TO_ENTRANCE_ID));

        // Check before was in round actions list and after it isn't
        assertTrue(gameEngine.getRound().getPossibleActions().contains(ModelConstants.ACTION_MOVE_MOTHER_NATURE_ID));
        assertDoesNotThrow(()->moveMotherNatureAction.modifyRoundAndActionList());
        assertFalse(gameEngine.getRound().getPossibleActions().contains(ModelConstants.ACTION_MOVE_MOTHER_NATURE_ID));

        // Test exception if I try another execution
        assertThrows(IllegalGameStateException.class, moveMotherNatureAction::modifyRoundAndActionList);

        // Test Calculate influence run: it should have added to Round Action list the FromCloudTileToEntranceAction
        assertTrue(gameEngine.getRound().getPossibleActions().contains(ModelConstants.ACTION_FROM_CLOUD_TILE_TO_ENTRANCE_ID));
    }
}