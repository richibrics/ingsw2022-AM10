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

import static org.junit.jupiter.api.Assertions.*;

class TestDrawFromBagToCloudTwoFourPlayersAction {
    static DrawFromBagToCloudTwoFourPlayersAction drawFromBagToCloudTwoPlayersAction;
    static DrawFromBagToCloudTwoFourPlayersAction drawFromBagToCloudFourPlayersAction;
    static GameEngine gameEngine;

    /**
     * Tests only exception throw or not.
     */
    @Test
    void setOptions() {
    }

    /**
     * Tests correct number of students added to the clouds with 2 players.
     */
    @Test
    void actTwoPlayers() {
        GameEngine gameEngine = setUpTwoPlayers();
        assertDoesNotThrow(() -> drawFromBagToCloudTwoPlayersAction.setOptions(new HashMap<>()));
        drawFromBagToCloudTwoPlayersAction.setPlayerId(ModelConstants.NO_PLAYER);

        // Check cloud empty before
        for (CloudTile cloudTile: assertDoesNotThrow(()->gameEngine.getTable().getCloudTiles())) {
            assertEquals(0, cloudTile.peekStudents().size());
        }

        // Fill them with the action
        assertDoesNotThrow(()->drawFromBagToCloudTwoPlayersAction.act());

        // Check clouds filled
        for (CloudTile cloudTile: assertDoesNotThrow(()->gameEngine.getTable().getCloudTiles())) {
            assertEquals(ModelConstants.ACTION_DRAW_FROM_BAG_TO_CLOUD_TWO_PLAYERS_STUDENTS_NUMBER, cloudTile.peekStudents().size());
        }
    }


    private GameEngine setUpTwoPlayers() {
        User user1 = new User("1", 2);
        User user2 = new User("2", 2);
        Player player1 = new Player(user1, 1, 3);
        Player player2 = new Player(user2, 2, 3);
        ArrayList<Player> players1 = new ArrayList<>();
        players1.add(player1);
        Team team1 = new Team(1, players1);
        ArrayList<Player> players2 = new ArrayList<>();
        players2.add(player2);
        Team team2 = new Team(2, players2);
        ArrayList<Team> teams = new ArrayList<>();
        teams.add(team1);
        teams.add(team2);
        gameEngine = new GameEngine(teams);
        SetUpTwoAndFourPlayersAction setUpTwoAndFourPlayersAction = new SetUpTwoAndFourPlayersAction(gameEngine);
        assertDoesNotThrow(()->setUpTwoAndFourPlayersAction.act());

        drawFromBagToCloudTwoPlayersAction = new DrawFromBagToCloudTwoFourPlayersAction(gameEngine);
        return gameEngine;
    }


    /**
     * Tests correct number of students added to the clouds with 4 players.
     */
    @Test
    void actFourPlayers() {
        GameEngine gameEngine = setUpFourPlayers();
        assertDoesNotThrow(() -> drawFromBagToCloudFourPlayersAction.setOptions(new HashMap<>()));
        drawFromBagToCloudFourPlayersAction.setPlayerId(ModelConstants.NO_PLAYER);

        // Check cloud empty before
        for (CloudTile cloudTile: assertDoesNotThrow(()->gameEngine.getTable().getCloudTiles())) {
            assertEquals(0, cloudTile.peekStudents().size());
        }

        // Fill them with the action
        assertDoesNotThrow(()->drawFromBagToCloudFourPlayersAction.act());

        // Check clouds filled
        for (CloudTile cloudTile: assertDoesNotThrow(()->gameEngine.getTable().getCloudTiles())) {
            assertEquals(ModelConstants.ACTION_DRAW_FROM_BAG_TO_CLOUD_FOUR_PLAYERS_STUDENTS_NUMBER, cloudTile.peekStudents().size());
        }
    }


    private GameEngine setUpFourPlayers() {
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
        ArrayList<Player> players2 = new ArrayList<>();
        players2.add(player2);
        players1.add(player3);
        players2.add(player4);
        ArrayList<Team> teams = new ArrayList<>();
        Team team1 = new Team(1, players1);
        Team team2 = new Team(2, players2);
        teams.add(team1);
        teams.add(team2);
        gameEngine = new GameEngine(teams);
        SetUpTwoAndFourPlayersAction setUpTwoAndFourPlayersAction = new SetUpTwoAndFourPlayersAction(gameEngine);
        assertDoesNotThrow(()->setUpTwoAndFourPlayersAction.act());

        drawFromBagToCloudFourPlayersAction = new DrawFromBagToCloudTwoFourPlayersAction(gameEngine);
        return gameEngine;
    }


}