package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.controller.User;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.game_components.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TestSetUpThreePlayersAction {

    static SetUpThreePlayersAction setUpThreePlayersAction;
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
        setUpThreePlayersAction = new SetUpThreePlayersAction(gameEngine);
    }

    @Test
    void modifyRound() throws Exception{
        setUpThreePlayersAction.modifyRound();
        ArrayList<Integer> orderOfPlay = setUpThreePlayersAction.getGameEngine().getRound().getOrderOfPlay();
        assertEquals(orderOfPlay.size(),3);
        Collections.sort(orderOfPlay);
        int index = 1;
        for (Integer integer : orderOfPlay) {
            assertEquals(integer, index);
            index++;
        }
    }

    @Test
    void setUpCloudTiles() {
        ArrayList<CloudTile> cloudTiles = new ArrayList<>();
        setUpThreePlayersAction.setUpCloudTiles(cloudTiles);
        assertEquals(cloudTiles.size(), 3);
        assertEquals(cloudTiles.get(0).getId(), 1);
        assertEquals(cloudTiles.get(1).getId(), 2);
        assertEquals(cloudTiles.get(2).getId(), 3);
    }

    @Test
    void setUpSchoolBoards() {
        ArrayList<SchoolBoard> schoolBoards = new ArrayList<>();
        setUpThreePlayersAction.setUpSchoolBoards(schoolBoards);
        assertEquals(schoolBoards.size(), 3);
        assertEquals(assertDoesNotThrow(()->setUpThreePlayersAction.getGameEngine().getTeams().get(0).getPlayers().get(0).getSchoolBoard()), schoolBoards.get(0));
        assertEquals(assertDoesNotThrow(()->setUpThreePlayersAction.getGameEngine().getTeams().get(1).getPlayers().get(0).getSchoolBoard()), schoolBoards.get(1));
        assertEquals(assertDoesNotThrow(()->setUpThreePlayersAction.getGameEngine().getTeams().get(2).getPlayers().get(0).getSchoolBoard()), schoolBoards.get(2));
    }

    @Test
    void setUpTowers() {
        setUpThreePlayersAction.setUpTowers();
        assertEquals(setUpThreePlayersAction.getGameEngine().getTeams().get(0).getTowers().size(), 6);
        assertEquals(setUpThreePlayersAction.getGameEngine().getTeams().get(0).getTowers().get(0).getColor(), TowerColor.WHITE);
        assertEquals(setUpThreePlayersAction.getGameEngine().getTeams().get(1).getTowers().size(), 6);
        assertEquals(setUpThreePlayersAction.getGameEngine().getTeams().get(1).getTowers().get(0).getColor(), TowerColor.BLACK);
        assertEquals(setUpThreePlayersAction.getGameEngine().getTeams().get(2).getTowers().size(), 6);
        assertEquals(setUpThreePlayersAction.getGameEngine().getTeams().get(2).getTowers().get(0).getColor(), TowerColor.GREY);
    }

    @Test
    void drawStudentsAndPlaceOnEntrance() {
        ArrayList<StudentDisc> studentDiscs = setUpThreePlayersAction.generateStudentDiscs();
        Bag bag = new Bag();
        setUpThreePlayersAction.putRemainingStudentsInBag(bag, studentDiscs);
        ArrayList<SchoolBoard> schoolBoards = new ArrayList<>();
        setUpThreePlayersAction.setUpSchoolBoards(schoolBoards);
        assertDoesNotThrow(()->setUpThreePlayersAction.drawStudentsAndPlaceOnEntrance(bag));
        for (Team team : setUpThreePlayersAction.getGameEngine().getTeams())
            for (Player player : team.getPlayers())
                assertEquals(assertDoesNotThrow(()->player.getSchoolBoard().getEntrance().size()), 9);
        assertEquals(assertDoesNotThrow(()->bag.getNumberOfStudents()), 130 - 27);
    }
}