package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.controller.User;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.game_components.CloudTile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TestDrawFromBagToCloudThreePlayersAction {
    static DrawFromBagToCloudThreePlayersAction drawFromBagToCloudThreePlayersAction;
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

        drawFromBagToCloudThreePlayersAction = new DrawFromBagToCloudThreePlayersAction(gameEngine);
    }


    /**
     * Tests only exception throw or not.
     */
    @Test
    void setOptions() {
    }

    /**
     * Tests correct number of students added to the clouds.
     */
    @Test
    void act() {
        assertDoesNotThrow(() -> drawFromBagToCloudThreePlayersAction.setOptions(new HashMap<>()));
        drawFromBagToCloudThreePlayersAction.setPlayerId(ModelConstants.NO_PLAYER);

        // Check cloud empty before
        for (CloudTile cloudTile : assertDoesNotThrow(() -> gameEngine.getTable().getCloudTiles())) {
            assertEquals(0, cloudTile.peekStudents().size());
        }

        // Fill them with the action
        assertDoesNotThrow(() -> drawFromBagToCloudThreePlayersAction.act());

        // Check clouds filled
        for (CloudTile cloudTile : assertDoesNotThrow(() -> gameEngine.getTable().getCloudTiles())) {
            assertEquals(ModelConstants.ACTION_DRAW_FROM_BAG_TO_CLOUD_THREE_PLAYERS_STUDENTS_NUMBER, cloudTile.peekStudents().size());
        }
    }
}