package it.polimi.ingsw.model.actions.effects;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.controller.User;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.actions.OnSelectionOfAssistantsCardAction;
import it.polimi.ingsw.model.actions.SetUpThreePlayersAction;
import it.polimi.ingsw.model.managers.CommonManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TestMailmanEffectAction {
    static GameEngine gameEngine;
    static MailmanEffectAction mailmanEffectAction;
    static OnSelectionOfAssistantsCardAction onSelectionOfAssistantsCardAction;

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
        SetUpThreePlayersAction setUpThreePlayersAction = new SetUpThreePlayersAction(gameEngine);
        assertDoesNotThrow(() -> setUpThreePlayersAction.act());

        mailmanEffectAction = new MailmanEffectAction(gameEngine);
        onSelectionOfAssistantsCardAction = new OnSelectionOfAssistantsCardAction(gameEngine);
        gameEngine.getAssistantManager().setWizard(1, 1);
    }

    /**
     * Tests only exception throw or not.
     */
    @Test
    void setOptions() {
    }


    /**
     * Tests property set to the correct AssistantCard.
     */
    @Test
    void act() {
        HashMap<String, String> options = new HashMap<>();

        // Player 1 acts
        options.put(ModelConstants.ACTION_ON_SELECTION_OF_ASSISTANTS_CARD_OPTIONS_KEY_ASSISTANT, "2");
        onSelectionOfAssistantsCardAction.setPlayerId(1);
        assertDoesNotThrow(() -> onSelectionOfAssistantsCardAction.setOptions(options));
        assertDoesNotThrow(() -> onSelectionOfAssistantsCardAction.act());
        int movements = assertDoesNotThrow(() -> CommonManager.takePlayerById(gameEngine, 1).getActiveAssistantCard().getMovements());

        // Player 1 uses mailmanEffect
        mailmanEffectAction.setPlayerId(1);
        assertDoesNotThrow(() -> mailmanEffectAction.act());

        //Checks if the max number of movements if incremented
        assertEquals(movements + 2, assertDoesNotThrow(() -> gameEngine.getAssistantManager().getMovementsOfAssistantCardInHand(1)));
    }
}