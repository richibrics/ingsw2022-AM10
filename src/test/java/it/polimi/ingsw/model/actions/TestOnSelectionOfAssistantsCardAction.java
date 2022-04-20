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

class TestOnSelectionOfAssistantsCardAction {
    static OnSelectionOfAssistantsCardAction onSelectionOfAssistantsCardAction;
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
        onSelectionOfAssistantsCardAction = new OnSelectionOfAssistantsCardAction(gameEngine);

        gameEngine.getAssistantManager().setWizard(1,1);
        gameEngine.getAssistantManager().setWizard(2,2);
        gameEngine.getAssistantManager().setWizard(3,3);

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

        // No assistant
        assertThrows(WrongMessageContentException.class, () -> onSelectionOfAssistantsCardAction.setOptions(options));

        // Parse error
        options.put("assistant","a");
        assertThrows(WrongMessageContentException.class, () -> onSelectionOfAssistantsCardAction.setOptions(options));

        // OK
        options.put("assistant","12");
        assertDoesNotThrow(() -> onSelectionOfAssistantsCardAction.setOptions(options));
    }

    @Test
    void act() {
        HashMap<String, String> options = new HashMap<>();

        // Player 1 acts
        options.put("assistant", "2");
        onSelectionOfAssistantsCardAction.setPlayerId(1);
        assertDoesNotThrow(()->onSelectionOfAssistantsCardAction.setOptions(options));
        assertDoesNotThrow(()->onSelectionOfAssistantsCardAction.act());

        // Check okay
        assertEquals(2, assertDoesNotThrow(()->CommonManager.takePlayerById(gameEngine,1).getActiveAssistantCard().getId()));

        // Check can't play the same card again
        assertThrows(IllegalGameActionException.class, ()->onSelectionOfAssistantsCardAction.act());
    }

    /**
     * Set 3 different card and call modifyRound 3 times (for three players), check round works correctly and then that the order is built.
     * Then (when player 3 only has one card) select the same card of another player and check that the
     * new order keeps track of the previous order when more than one player plays the same card.
     * Check also next actions are correct
     */
    @Test
    void modifyRound() {
        assertEquals(1, assertDoesNotThrow(()->gameEngine.getRound().getCurrentPlayer()));

        // Set the assistant cards
        HashMap<String, String> options = new HashMap<>();

        // Player 1
        options.put("assistant", "2"); // value = 2
        onSelectionOfAssistantsCardAction.setPlayerId(1);
        assertDoesNotThrow(()->onSelectionOfAssistantsCardAction.setOptions(options));
        assertDoesNotThrow(()->onSelectionOfAssistantsCardAction.act());

        assertDoesNotThrow(()->onSelectionOfAssistantsCardAction.modifyRound());
        // Check round is going forward
        assertEquals(2, assertDoesNotThrow(()->gameEngine.getRound().getCurrentPlayer()));

        // Player 2
        options.put("assistant", "11"); // value = 1
        onSelectionOfAssistantsCardAction.setPlayerId(2);
        assertDoesNotThrow(()->onSelectionOfAssistantsCardAction.setOptions(options));
        assertDoesNotThrow(()->onSelectionOfAssistantsCardAction.act());

        assertDoesNotThrow(()->onSelectionOfAssistantsCardAction.modifyRound());
        // Check round is going forward
        assertEquals(3, assertDoesNotThrow(()->gameEngine.getRound().getCurrentPlayer()));

        // Player 3
        options.put("assistant", "23"); // value = 3
        onSelectionOfAssistantsCardAction.setPlayerId(3);
        assertDoesNotThrow(()->onSelectionOfAssistantsCardAction.setOptions(options));
        assertDoesNotThrow(()->onSelectionOfAssistantsCardAction.act());

        // This will make the new order
        assertDoesNotThrow(()->onSelectionOfAssistantsCardAction.modifyRound());

        // Check new order (order made by id of assistant cards set above)
        assertEquals(2, assertDoesNotThrow(()->gameEngine.getRound().getOrderOfPlay().get(0)));
        assertEquals(1, assertDoesNotThrow(()->gameEngine.getRound().getOrderOfPlay().get(1)));
        assertEquals(3, assertDoesNotThrow(()->gameEngine.getRound().getOrderOfPlay().get(2)));

        // Now I remove all the cards from player 3 and I leave only the card with value = 1, that is the same of player 2.
        // Player 3 now can play card 2 and the order will change
        assertDoesNotThrow(()-> gameEngine.getAssistantManager().setAssistantCard(3,22));
        assertDoesNotThrow(()-> gameEngine.getAssistantManager().setAssistantCard(3,24));
        assertDoesNotThrow(()-> gameEngine.getAssistantManager().setAssistantCard(3,25));
        assertDoesNotThrow(()-> gameEngine.getAssistantManager().setAssistantCard(3,26));
        assertDoesNotThrow(()-> gameEngine.getAssistantManager().setAssistantCard(3,27));
        assertDoesNotThrow(()-> gameEngine.getAssistantManager().setAssistantCard(3,28));
        assertDoesNotThrow(()-> gameEngine.getAssistantManager().setAssistantCard(3,29));
        assertDoesNotThrow(()-> gameEngine.getAssistantManager().setAssistantCard(3,30));

        // Now he has only card value = 1: play it, get the new order and check it
        options.put("assistant", "21"); // value = 1 - same card of player 2!
        onSelectionOfAssistantsCardAction.setPlayerId(3);
        assertDoesNotThrow(()->onSelectionOfAssistantsCardAction.setOptions(options));
        assertDoesNotThrow(()->onSelectionOfAssistantsCardAction.act());

        assertDoesNotThrow(()->onSelectionOfAssistantsCardAction.modifyRound());
        assertDoesNotThrow(()->onSelectionOfAssistantsCardAction.modifyRound());
        assertDoesNotThrow(()->onSelectionOfAssistantsCardAction.modifyRound()); // This generates the new order

        // Check the order
        assertEquals(2, assertDoesNotThrow(()->gameEngine.getRound().getOrderOfPlay().get(0)));
        assertEquals(3, assertDoesNotThrow(()->gameEngine.getRound().getOrderOfPlay().get(1)));
        assertEquals(1, assertDoesNotThrow(()->gameEngine.getRound().getOrderOfPlay().get(2)));

        // Check next actions
        assertEquals(2, gameEngine.getRound().getPossibleActions().size());
        assertTrue(gameEngine.getRound().getPossibleActions().contains(ModelConstants.ACTION_ID_ON_SELECTION_OF_CHARACTER_CARD));
        assertTrue(gameEngine.getRound().getPossibleActions().contains(ModelConstants.ACTION_ID_MOVE_STUDENTS_FROM_ENTRANCE));
    }
}