package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.controller.User;
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

import static org.junit.jupiter.api.Assertions.*;

class TestOnSelectionOfWizardAction {
    static OnSelectionOfWizardAction onSelectionOfWizardAction;
    static GameEngine gameEngine;

    @BeforeEach
    void setUp() {
        User user1 = new User("1", 3);
        User user2 = new User("2", 3);
        User user3 = new User("3", 3);
        Player player1 = new Player(user1, 1, 3);
        Player player2 = new Player(user2, 2, 3);
        Player player3 = new Player(user3, 3,3);
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
        SetUpThreePlayersAction setUpThreePlayersAction = new SetUpThreePlayersAction(gameEngine);
        onSelectionOfWizardAction = new OnSelectionOfWizardAction(gameEngine);

        ArrayList<Integer> order = new ArrayList<>();
        order.add(1);
        order.add(2);
        order.add(3);
        gameEngine.getRound().setOrderOfPlay(order);
    }

    /**
     * Test only exception throw or not
     */
    @Test
    void setOptions() {
        HashMap<String, String> options = new HashMap<>();

        // No wizard
        assertThrows(WrongMessageContentException.class, () -> onSelectionOfWizardAction.setOptions(options));

        // Parse error
        options.put(ModelConstants.ACTION_ON_SELECTION_OF_WIZARD_OPTIONS_KEY_WIZARD,"a");
        assertThrows(WrongMessageContentException.class, () -> onSelectionOfWizardAction.setOptions(options));

        // OK
        options.put(ModelConstants.ACTION_ON_SELECTION_OF_WIZARD_OPTIONS_KEY_WIZARD,"12");
        assertDoesNotThrow(() -> onSelectionOfWizardAction.setOptions(options));
    }


    /**
     * Test correct behaviour (card set when it can be set and not when it cannot be set (already used))
     * If everything is alright, then the option is set correctly in the attribute.
     */
    @Test
    void act() {
        HashMap<String, String> options = new HashMap<>();

        // Player 1 acts
        options.put(ModelConstants.ACTION_ON_SELECTION_OF_WIZARD_OPTIONS_KEY_WIZARD, "4");
        onSelectionOfWizardAction.setPlayerId(1);
        assertDoesNotThrow(()->onSelectionOfWizardAction.setOptions(options));
        assertDoesNotThrow(()->onSelectionOfWizardAction.act());

        // Check okay
        assertEquals(4, assertDoesNotThrow(()-> CommonManager.takePlayerById(gameEngine,1).getWizard().getId()));

        // Check player 2 can't use the same wizard (wizard 4 already in options, so I use them)
        onSelectionOfWizardAction.setPlayerId(2);
        assertThrows(IllegalGameActionException.class, ()->onSelectionOfWizardAction.act());
    }

    /**
     * Call modifyRoundAndActionListAndActionList 3 times (for three players), check round works correctly and then
     * that the order (same as before) is built.
     * Check also next action is correct.
     */
    @Test
    void modifyRoundAndActionList() {
        assertEquals(1, assertDoesNotThrow(()->gameEngine.getRound().getCurrentPlayer()));

        assertDoesNotThrow(()->onSelectionOfWizardAction.modifyRoundAndActionList());

        // Check round is going forward
        assertEquals(2, assertDoesNotThrow(()->gameEngine.getRound().getCurrentPlayer()));

        assertDoesNotThrow(()->onSelectionOfWizardAction.modifyRoundAndActionList());

        // Check round is going forward
        assertEquals(3, assertDoesNotThrow(()->gameEngine.getRound().getCurrentPlayer()));

        // This will make the new order
        assertDoesNotThrow(()->onSelectionOfWizardAction.modifyRoundAndActionList());

        // Check new order is the same as before
        assertEquals(1, assertDoesNotThrow(()->gameEngine.getRound().getOrderOfPlay().get(0)));
        assertEquals(2, assertDoesNotThrow(()->gameEngine.getRound().getOrderOfPlay().get(1)));
        assertEquals(3, assertDoesNotThrow(()->gameEngine.getRound().getOrderOfPlay().get(2)));

        // Check next action
        assertEquals(1, gameEngine.getRound().getPossibleActions().size());
        assertTrue(gameEngine.getRound().getPossibleActions().contains(ModelConstants.ACTION_ON_SELECTION_OF_ASSISTANTS_CARD_ID));
    }
}