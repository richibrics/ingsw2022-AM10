package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.controller.User;
import it.polimi.ingsw.model.ModelConstants;
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

    /**
     * Tests round with next players and next actions is set correctly
     * @throws Exception
     */
    @Test
    void modifyRoundAndActionList() throws Exception{
        assertEquals(ModelConstants.THREE_PLAYERS, gameEngine.getNumberOfPlayers());
        
        setUpThreePlayersAction.modifyRoundAndActionList();
        ArrayList<Integer> orderOfPlay = setUpThreePlayersAction.getGameEngine().getRound().getOrderOfPlay();
        assertEquals(gameEngine.getNumberOfPlayers(), orderOfPlay.size());
        Collections.sort(orderOfPlay);
        int index = 1;
        for (Integer integer : orderOfPlay) {
            assertEquals(integer, index);
            index++;
        }
        // Check also next action is correct
        assertEquals(1, gameEngine.getRound().getPossibleActions().size());
        assertEquals(ModelConstants.ACTION_ON_SELECTION_OF_WIZARD_ID, gameEngine.getRound().getPossibleActions().get(0));
    }


    /**
     * Checks I have a cloud tile per player
     */
    @Test
    void setUpCloudTiles() {
        ArrayList<CloudTile> cloudTiles = new ArrayList<>();
        setUpThreePlayersAction.setUpCloudTiles(cloudTiles);
        assertEquals(gameEngine.getNumberOfPlayers(), cloudTiles.size());
        for (int i = 0; i < gameEngine.getNumberOfPlayers(); i++) {
            assertEquals(i+1, cloudTiles.get(i).getId());
        }
    }

    /**
     * Checks I have a schoolboard per player
     */
    @Test
    void setUpSchoolBoards() {
        ArrayList<SchoolBoard> schoolBoards = new ArrayList<>();
        setUpThreePlayersAction.setUpSchoolBoards(schoolBoards);
        assertEquals(gameEngine.getNumberOfPlayers(), schoolBoards.size());
        for (int i = 0; i < gameEngine.getNumberOfPlayers(); i++) {
            int finalI = i;
            assertEquals(schoolBoards.get(i), assertDoesNotThrow(()->setUpThreePlayersAction.getGameEngine().getTeams().get(finalI).getPlayers().get(0).getSchoolBoard()));
        }
    }

    /**
     * Checks I have generated correctly the towers
     */
    @Test
    void setUpTowers() {
        setUpThreePlayersAction.setUpTowers();
        for (int i = 0; i < gameEngine.getNumberOfPlayers(); i++) {
            assertEquals(ModelConstants.NUMBER_OF_TOWERS_THREE_PLAYERS, setUpThreePlayersAction.getGameEngine().getTeams().get(i).getTowers().size());
            assertEquals(TowerColor.values()[i], setUpThreePlayersAction.getGameEngine().getTeams().get(i).getTowers().get(0).getColor());
        }
    }

    /**
     * Checks I have placed correctly the students in the entrance
     */
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
                assertEquals(ModelConstants.INITIAL_NUMBER_OF_STUDENTS_IN_ENTRANCE_THREE_PLAYERS, assertDoesNotThrow(()->player.getSchoolBoard().getEntrance().size()));
        assertEquals(ModelConstants.INITIAL_NUMBER_OF_STUDENTS_PER_COLOR * PawnColor.values().length - ModelConstants.INITIAL_NUMBER_OF_STUDENTS_IN_ENTRANCE_THREE_PLAYERS * gameEngine.getNumberOfPlayers()
                , assertDoesNotThrow(()->bag.getNumberOfStudents()));
    }

    /**
     * This action does not parse options
     */
    @Test
    void setOptions() {
    }

}