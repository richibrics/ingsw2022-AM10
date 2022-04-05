package it.polimi.ingsw.model.managers;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.controller.User;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.game_components.IslandTile;
import it.polimi.ingsw.model.game_components.PawnColor;
import it.polimi.ingsw.model.game_components.SchoolBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class TestActionManager {

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
    }

    @Test
    void generateActions() {
        ActionManager actionManager = new ActionManager(gameEngine);
        assertDoesNotThrow(()->actionManager.generateActions());
        assertEquals(assertDoesNotThrow(()->actionManager.getGameEngine().getTable().getIslandTiles().size()), 12);
        int motherNatureIslandId = assertDoesNotThrow(()->actionManager.getGameEngine().getTable().getMotherNature().getIslandTile().getId());
        assertTrue(motherNatureIslandId <= 12 && motherNatureIslandId >= 1);
        for (ArrayList<IslandTile> islandGroup : assertDoesNotThrow(()->actionManager.getGameEngine().getTable().getIslandTiles()))
            for (IslandTile islandTile : islandGroup) {
                if (islandTile.getId() != motherNatureIslandId && islandTile.getId() != ((motherNatureIslandId + 6) % 12 == 0 ? 12 : (motherNatureIslandId + 6) % 12))
                    assertEquals(islandTile.peekStudents().size(), 1);
                else
                    assertEquals(islandTile.peekStudents().size(), 0);
            }
        assertEquals(assertDoesNotThrow(()->actionManager.getGameEngine().getTable().getCloudTiles().size()), 3);
        assertEquals(assertDoesNotThrow(()->actionManager.getGameEngine().getTable().getSchoolBoards().size()), 3);
        for (SchoolBoard schoolBoard : assertDoesNotThrow(()->actionManager.getGameEngine().getTable().getSchoolBoards()))
            assertEquals(schoolBoard.getEntrance().size(), 9);
        assertEquals(assertDoesNotThrow(()->actionManager.getGameEngine().getTable().getAvailableProfessorPawns().size()), 5);
        for (PawnColor color : PawnColor.values())
            assertEquals(assertDoesNotThrow(()->actionManager.getGameEngine().getTable().getAvailableProfessorPawns().stream().filter(professorPawn -> professorPawn.getColor() == color).collect(Collectors.toList()).size()), 1);
        for (Team team : actionManager.getGameEngine().getTeams())
            assertEquals(team.getTowers().size(), 6);
        assertEquals(assertDoesNotThrow(()->actionManager.getGameEngine().getTable().getBag().getNumberOfStudents()), 130 - 37);
    }

    @Test
    void executeAction() {
    }

    @Test
    void notifyAllObservers() {
    }
}