package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Round;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.exceptions.TableNotSetException;
import it.polimi.ingsw.model.game_components.*;
import it.polimi.ingsw.model.managers.ActionManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class TestGameEngine {
    static ArrayList<Team> teams;
    static Table table;

    @BeforeAll
    static void beforeAll() {
        teams = new ArrayList<>();
        ArrayList<Player> playersTeam1 = new ArrayList<>();
        ArrayList<Player> playersTeam2 = new ArrayList<>();
        playersTeam1.add(new Player(new User("test1",2), 0,1));
        playersTeam1.add(new Player(new User("test2",2), 1,1));
        playersTeam2.add(new Player(new User("test3",2), 2,1));
        playersTeam2.add(new Player(new User("test4",2), 3,1));
        Team team1 = new Team(1,playersTeam1);
        Team team2 = new Team(2,playersTeam2);
        teams.add(team1);
        teams.add(team2);

        table = new Table(new ArrayList<>(), new Bag(),new ArrayList<>(),new MotherNature(new IslandTile(0)),new ArrayList<>(), new ArrayList<>(), new HashMap<Integer,CharacterCard>());
    }



    @Test
    void setTable() {
        GameEngine gameEngine = new GameEngine(this.teams);
        assertThrows(TableNotSetException.class, ()->gameEngine.getTable());
        gameEngine.setTable(table);
        assertEquals(table, assertDoesNotThrow(()->gameEngine.getTable()));
    }

    @Test
    void getTable() {
        GameEngine gameEngine = new GameEngine(this.teams);
        assertThrows(TableNotSetException.class, ()->gameEngine.getTable());
        gameEngine.setTable(table);
        assertEquals(assertDoesNotThrow(()->gameEngine.getTable()), table);
    }

    @Test
    void getTeams() {
        GameEngine gameEngine = new GameEngine(this.teams);
        assertEquals(this.teams, gameEngine.getTeams());
    }

    @Test
    void getNumberOfPlayers() {
        GameEngine gameEngine = new GameEngine(this.teams);
        assertEquals(4, gameEngine.getNumberOfPlayers());
    }

    @Test
    void getRound() {
        GameEngine gameEngine = new GameEngine(this.teams);
        assertInstanceOf(Round.class, gameEngine.getRound());
        assertEquals(teams.stream().flatMap(team -> team.getPlayers().stream()).toList().size(), 4);
    }

    /**
     * Test Manager instantiated correctly so not null
     */
    @Test
    void getSchoolPawnManager() {
        GameEngine gameEngine = new GameEngine(this.teams);
        assertNotNull(gameEngine.getSchoolPawnManager());
    }

    /**
     * Test Manager instantiated correctly so not null
     */
    @Test
    void getAssistantManager() {
        GameEngine gameEngine = new GameEngine(this.teams);
        assertNotNull(gameEngine.getAssistantManager());
    }

    /**
     * Test Manager instantiated correctly so not null
     */
    @Test
    void getIslandManager() {
        GameEngine gameEngine = new GameEngine(this.teams);
        assertNotNull(gameEngine.getIslandManager());
    }

    /* Valid test only when Manager will be instantiated in the constructor
    @Test
    void getActionManager() {
        GameEngine gameEngine = new GameEngine(this.teams);
        assertNotNull(gameEngine.getActionManager());
    }

    @Test
    void getCharacterManager() {
        GameEngine gameEngine = new GameEngine(this.teams);
        assertNotNull(gameEngine.getCharacterManager());
    }
    */

    @Test
    void resumeGame() {

    }

    @Test
    void startGame() {

    }
}