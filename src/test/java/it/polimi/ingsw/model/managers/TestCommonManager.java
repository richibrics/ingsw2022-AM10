package it.polimi.ingsw.model.managers;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.controller.User;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.exceptions.SchoolBoardNotSetException;
import it.polimi.ingsw.model.game_components.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class TestCommonManager {

    static GameEngine gameEngine;

    @BeforeEach
    void setUp()
    {
        User user1 = new User("1", 2);
        Player player1 = new Player(user1, 1, 3);
        ArrayList<Player> players1 = new ArrayList<Player>();
        players1.add(player1);
        Team team1 = new Team(1, players1);
        team1.addTower(new Tower(TowerColor.WHITE));

        User user2 = new User("2", 2);
        Player player2 = new Player(user2, 2, 3);
        ArrayList<Player> players2 = new ArrayList<Player>();
        players2.add(player2);
        Team team2 = new Team(2, players2);

        ArrayList<Team> teams = new ArrayList<>();
        teams.add(team1);
        teams.add(team2);
        gameEngine = new GameEngine(teams);

        SchoolBoard schoolBoard1 = new SchoolBoard();
        SchoolBoard schoolBoard2 = new SchoolBoard();
        ArrayList<SchoolBoard> schoolBoards = new ArrayList<>();
        player1.setSchoolBoard(schoolBoard1);
        player2.setSchoolBoard(schoolBoard2);
        schoolBoards.add(schoolBoard1);
        schoolBoards.add(schoolBoard2);

        StudentDisc student1 = new StudentDisc(1, PawnColor.BLUE);
        StudentDisc student2 = new StudentDisc(2, PawnColor.PINK);
        StudentDisc student3 = new StudentDisc(3, PawnColor.RED);
        StudentDisc student4 = new StudentDisc(4, PawnColor.GREEN);
        StudentDisc student5 = new StudentDisc(5, PawnColor.BLUE);
        StudentDisc student6 = new StudentDisc(6, PawnColor.YELLOW);
        ArrayList<StudentDisc> students = new ArrayList<>();
        students.add(student1);
        students.add(student2);
        students.add(student3);
        students.add(student4);
        students.add(student5);
        students.add(student6);
        Bag bag = new Bag();
        bag.pushStudents(students);

        CloudTile cloud1 = new CloudTile(1);
        CloudTile cloud2 = new CloudTile(2);
        ArrayList<CloudTile> clouds = new ArrayList<>();
        clouds.add(cloud1);
        clouds.add(cloud2);

        ArrayList<ArrayList<IslandTile>> islands = new ArrayList<>();
        for(int i = 1; i <= 12; i++) {
            IslandTile island = new IslandTile(i);
            ArrayList<IslandTile> islandGroup = new ArrayList<>();
            islandGroup.add(island);
            islands.add(islandGroup);
        }

        MotherNature motherNature = new MotherNature(islands.get(6).get(0));

        ArrayList<ProfessorPawn> professorPawns = new ArrayList<>();

        Map<Integer, CharacterCard> characterCards = new HashMap<>();

        Table table = new Table(schoolBoards, bag, clouds, motherNature, islands, professorPawns, characterCards);

        gameEngine.setTable(table);
    }

    @Test
    void takeSchoolBoardByPlayerId() throws SchoolBoardNotSetException {
        SchoolBoard schoolBoard = assertDoesNotThrow(() -> CommonManager.takeSchoolBoardByPlayerId(gameEngine, 1));
        assertEquals(assertDoesNotThrow(() ->gameEngine.getTeams().get(0).getPlayers().get(0).getSchoolBoard()), schoolBoard);
        assertThrows(NoSuchElementException.class, () -> CommonManager.takeSchoolBoardByPlayerId(gameEngine, 4));
    }

    @Test
    void takeIslandTileById() {
        IslandTile islandTile = assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, 5));
        assertEquals(assertDoesNotThrow(() -> gameEngine.getTable().getIslandTiles().get(4).get(0)), islandTile);
        assertThrows(NoSuchElementException.class, () -> CommonManager.takeIslandTileById(gameEngine, 15));
    }

    @Test
    void takeTeamIdByPlayerId() {
        assertEquals(gameEngine.getTeams().get(0).getId(), CommonManager.takeTeamIdByPlayerId(gameEngine,gameEngine.getTeams().get(0).getPlayers().get(0).getPlayerId()));
    }

    @Test
    void takeTeamById() {
        assertEquals(gameEngine.getTeams().get(1),CommonManager.takeTeamById(gameEngine,2));
    }

    @Test
    void takeTeamIdByTowerColor() {
        assertEquals(1, assertDoesNotThrow(()->CommonManager.takeTeamIdByTowerColor(gameEngine, TowerColor.WHITE)));
    }

    /**
     * Tests if the Player asked to the CommonManager is the one present in the second team, as added in setUp
     */
    @Test
    void takePlayerById() {
        assertEquals(gameEngine.getTeams().get(1).getPlayers().get(0), assertDoesNotThrow(()->CommonManager.takePlayerById(gameEngine,2)));
    }
}