package it.polimi.ingsw.model.actions.effects;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.controller.User;
import it.polimi.ingsw.controller.exceptions.WrongMessageContentException;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.actions.SetUpThreePlayersAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TestThiefEffectAction {
    static ThiefEffectAction thiefEffectAction;
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
        SetUpThreePlayersAction setUpThreePlayersAction = new SetUpThreePlayersAction(gameEngine);
        assertDoesNotThrow(() -> setUpThreePlayersAction.act());

        thiefEffectAction = new ThiefEffectAction(gameEngine);
    }

    /**
     * Tests only exception throw or not.
     */
    @Test
    void setOptions() {
        HashMap<String, String> options = new HashMap<>();

        // No color
        assertThrows(WrongMessageContentException.class, () -> thiefEffectAction.setOptions(options));

        // Parse error
        options.clear();
        options.put(ModelConstants.ACTION_THIEF_OPTIONS_KEY_COLOR, "a");
        assertThrows(WrongMessageContentException.class, () -> thiefEffectAction.setOptions(options));

        //Ok
        options.clear();
        options.put(ModelConstants.ACTION_THIEF_OPTIONS_KEY_COLOR, "red");
        assertDoesNotThrow(() -> thiefEffectAction.setOptions(options));

    }

    /**
     * Tests property set to the correct color.
     */
    @Test
    void act() {
    }
}