package it.polimi.ingsw.model.actions.effects;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.controller.User;
import it.polimi.ingsw.controller.exceptions.WrongMessageContentException;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.actions.OnSelectionOfAssistantsCardAction;
import it.polimi.ingsw.model.actions.SetUpThreePlayersAction;
import it.polimi.ingsw.model.exceptions.IllegalGameActionException;
import it.polimi.ingsw.model.managers.CommonManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class TestHerbalistEffectAction {

    static HerbalistEffectAction herbalistEffectAction;
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
        assertDoesNotThrow(()->setUpThreePlayersAction.act());

        herbalistEffectAction = new HerbalistEffectAction(gameEngine);
    }


    /**
     * Tests only exception throw or not.
     */
    @Test
    void setOptions() {
        HashMap<String, String> options = new HashMap<>();

        // No id
        assertThrows(WrongMessageContentException.class, () -> herbalistEffectAction.setOptions(options));

        // Parse error
        options.put(ModelConstants.ACTION_HERBALIST_OPTIONS_KEY_ISLAND,"a");
        assertThrows(WrongMessageContentException.class, () -> herbalistEffectAction.setOptions(options));

        // Out of bound
        options.put(ModelConstants.ACTION_HERBALIST_OPTIONS_KEY_ISLAND,"13");
        assertThrows(WrongMessageContentException.class, () -> herbalistEffectAction.setOptions(options));
        options.put(ModelConstants.ACTION_HERBALIST_OPTIONS_KEY_ISLAND,"0");
        assertThrows(WrongMessageContentException.class, () -> herbalistEffectAction.setOptions(options));

        // OK
        options.put(ModelConstants.ACTION_HERBALIST_OPTIONS_KEY_ISLAND,"12");
        assertDoesNotThrow(() -> herbalistEffectAction.setOptions(options));
        options.put(ModelConstants.ACTION_HERBALIST_OPTIONS_KEY_ISLAND,"5");
        assertDoesNotThrow(() -> herbalistEffectAction.setOptions(options));
        options.put(ModelConstants.ACTION_HERBALIST_OPTIONS_KEY_ISLAND,"1");
        assertDoesNotThrow(() -> herbalistEffectAction.setOptions(options));
    }

    /**
     * Tests property set to the correct island group.
     */
    @Test
    void act() {
        HashMap<String, String> options = new HashMap<>();
        options.put(ModelConstants.ACTION_HERBALIST_OPTIONS_KEY_ISLAND, "1");
        assertDoesNotThrow(() -> herbalistEffectAction.setOptions(options));

        // Before false
        assertEquals(false, assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, 1).hasNoEntry()));

        assertDoesNotThrow(() -> herbalistEffectAction.act());

        // After true
        assertEquals(true, assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, 1).hasNoEntry()));

        // Now set the no entry tile again and ensure it throws
        assertThrows(IllegalGameActionException.class, () -> herbalistEffectAction.act());

        // Now set the no entry tiles (should have 3 now) to other islands and check I cannot set the fifth
        options.put(ModelConstants.ACTION_HERBALIST_OPTIONS_KEY_ISLAND, "2");
        assertDoesNotThrow(() -> herbalistEffectAction.setOptions(options));
        assertDoesNotThrow(() -> herbalistEffectAction.act());
        options.put(ModelConstants.ACTION_HERBALIST_OPTIONS_KEY_ISLAND, "3");
        assertDoesNotThrow(() -> herbalistEffectAction.setOptions(options));
        assertDoesNotThrow(() -> herbalistEffectAction.act());
        options.put(ModelConstants.ACTION_HERBALIST_OPTIONS_KEY_ISLAND, "4");
        assertDoesNotThrow(() -> herbalistEffectAction.setOptions(options));
        assertDoesNotThrow(() -> herbalistEffectAction.act());

        // Here I should have troubles
        options.put(ModelConstants.ACTION_HERBALIST_OPTIONS_KEY_ISLAND, "5");
        assertDoesNotThrow(() -> herbalistEffectAction.setOptions(options));
        assertThrows(IllegalGameActionException.class, () -> herbalistEffectAction.act());
    }
}