package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.controller.User;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.game_components.*;
import it.polimi.ingsw.model.managers.CommonManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class TestCalculateInfluenceAction {

    static CalculateInfluenceAction calculateInfluenceAction;
    static SetUpTwoAndFourPlayersAction setUpTwoAndFourPlayersAction;
    static GameEngine gameEngine;

    @BeforeEach
    void setUp() {
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
        players1.add(player2);
        Team team1 = new Team(1, players1);
        ArrayList<Player> players2 = new ArrayList<>();
        players2.add(player3);
        players2.add(player4);
        Team team2 = new Team(2, players2);
        ArrayList<Team> teams = new ArrayList<>();
        teams.add(team1);
        teams.add(team2);
        gameEngine = new GameEngine(teams);
        calculateInfluenceAction = new CalculateInfluenceAction(gameEngine);
        setUpTwoAndFourPlayersAction = new SetUpTwoAndFourPlayersAction(gameEngine);
        assertDoesNotThrow(() -> setUpTwoAndFourPlayersAction.act());
    }

    @Test
    void calculateInfluences() {
        ArrayList<StudentDisc> studentDiscs = assertDoesNotThrow(() -> gameEngine.getTable().getBag().drawStudents(18));
        for (StudentDisc studentDisc : studentDiscs)
            assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, gameEngine.getIslandManager().getMotherNatureIslandId()).addStudent(studentDisc));
        assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, gameEngine.getIslandManager().getMotherNatureIslandId()).setTower(gameEngine.getTeams().get(0).popTower()));
        int influence1 = 1;
        int influence2 = 0;
        Random rand = new Random();
        for (ProfessorPawn professorPawn : assertDoesNotThrow(() -> gameEngine.getTable().getAvailableProfessorPawns())) {
            int index = rand.nextInt(0, 2);
            gameEngine.getTeams().get(index).addProfessorPawn(professorPawn);
            if (index == 0)
                influence1 += assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, gameEngine.getIslandManager().getMotherNatureIslandId()).peekStudents().stream().filter(studentDisc -> studentDisc.getColor().equals(professorPawn.getColor())).collect(Collectors.reducing(0, e -> 1, Integer::sum)));
            else if (index == 1)
                influence2 += assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, gameEngine.getIslandManager().getMotherNatureIslandId()).peekStudents().stream().filter(studentDisc -> studentDisc.getColor().equals(professorPawn.getColor())).collect(Collectors.reducing(0, e -> 1, Integer::sum)));
        }

        Map<Integer, Integer> influences = new HashMap<>();
        IslandTile motherNatureIsland = assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, gameEngine.getIslandManager().getMotherNatureIslandId()));
        List<ArrayList<IslandTile>> islandGroups = assertDoesNotThrow(() -> gameEngine.getTable().getIslandTiles().stream().filter(islandGroup -> islandGroup.contains(motherNatureIsland)).toList());
        assertDoesNotThrow(() -> calculateInfluenceAction.calculateInfluences(influences, islandGroups.get(0)));
        assertEquals(influence1, influences.get(1));
        assertEquals(influence2, influences.get(2));
    }

    @RepeatedTest(20)
    void act() {
        ArrayList<StudentDisc> studentDiscs = assertDoesNotThrow(() -> gameEngine.getTable().getBag().drawStudents(18));
        for (StudentDisc studentDisc : studentDiscs)
            assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, gameEngine.getIslandManager().getMotherNatureIslandId()).addStudent(studentDisc));
        int motherNatureIslandId = assertDoesNotThrow(() -> gameEngine.getIslandManager().getMotherNatureIslandId());
        if (motherNatureIslandId != 1 && motherNatureIslandId != 12) {
            assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, motherNatureIslandId).setTower(gameEngine.getTeams().get(0).popTower()));
            assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, motherNatureIslandId + 1).setTower(gameEngine.getTeams().get(0).popTower()));
            assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, motherNatureIslandId - 1).setTower(gameEngine.getTeams().get(0).popTower()));
        } else {
            assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, motherNatureIslandId).setTower(gameEngine.getTeams().get(0).popTower()));
            assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, motherNatureIslandId == 1 ? motherNatureIslandId + 1 : motherNatureIslandId - 1).setTower(gameEngine.getTeams().get(0).popTower()));
            assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, motherNatureIslandId == 1 ? motherNatureIslandId + 2 : motherNatureIslandId - 2).setTower(gameEngine.getTeams().get(0).popTower()));

        }
        assertDoesNotThrow(() -> gameEngine.getIslandManager().unifyPossibleIslands());

        for (ProfessorPawn professorPawn : assertDoesNotThrow(() -> gameEngine.getTable().getAvailableProfessorPawns()))
            gameEngine.getTeams().get(1).addProfessorPawn(professorPawn);
        assertDoesNotThrow(() -> calculateInfluenceAction.act());
        if (motherNatureIslandId != 1 && motherNatureIslandId != 12) {
            assertEquals(assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, motherNatureIslandId).getTower().getColor()), assertDoesNotThrow(() -> gameEngine.getTeams().get(1).getTeamTowersColor()));
            assertEquals(assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, motherNatureIslandId + 1).getTower().getColor()), assertDoesNotThrow(() -> gameEngine.getTeams().get(1).getTeamTowersColor()));
            assertEquals(assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, motherNatureIslandId - 1).getTower().getColor()), assertDoesNotThrow(() -> gameEngine.getTeams().get(1).getTeamTowersColor()));
        } else {
            assertEquals(assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, motherNatureIslandId).getTower().getColor()), assertDoesNotThrow(() -> gameEngine.getTeams().get(1).getTeamTowersColor()));
            assertEquals(assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, motherNatureIslandId == 1 ? motherNatureIslandId + 1 : motherNatureIslandId - 1).getTower().getColor()), assertDoesNotThrow(() -> gameEngine.getTeams().get(1).getTeamTowersColor()));
            assertEquals(assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, motherNatureIslandId == 1 ? motherNatureIslandId + 2 : motherNatureIslandId - 2).getTower().getColor()), assertDoesNotThrow(() -> gameEngine.getTeams().get(1).getTeamTowersColor()));
        }

        MotherNature motherNature = assertDoesNotThrow(() -> gameEngine.getTable().getMotherNature());
        List<ArrayList<IslandTile>> islandTiles = assertDoesNotThrow(() -> gameEngine.getTable().getIslandTiles().stream().filter(islandGroup -> islandGroup.stream().filter(islandTile -> motherNature.getIslandTile().getId() == islandTile.getId()).count() == 0).toList());
        int newMotherNatureIslandId = islandTiles.get(0).get(0).getId();
        motherNature.modifyIsland(assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, newMotherNatureIslandId)));
        ArrayList<StudentDisc> students = new ArrayList<>();

        if (newMotherNatureIslandId != (motherNatureIslandId + 6) % 12) {
            if (motherNature.getIslandTile().peekStudents().get(0).getColor().equals(PawnColor.RED)) {
                students.add(new StudentDisc(1, PawnColor.RED));
                students.add(new StudentDisc(2, PawnColor.RED));
                students.add(new StudentDisc(4, PawnColor.PINK));
                students.add(new StudentDisc(5, PawnColor.PINK));
                students.add(new StudentDisc(6, PawnColor.PINK));
            } else if (motherNature.getIslandTile().peekStudents().get(0).getColor().equals(PawnColor.PINK)) {
                students.add(new StudentDisc(1, PawnColor.RED));
                students.add(new StudentDisc(2, PawnColor.RED));
                students.add(new StudentDisc(3, PawnColor.RED));
                students.add(new StudentDisc(5, PawnColor.PINK));
                students.add(new StudentDisc(6, PawnColor.PINK));
            }
        }
        else {
            students.add(new StudentDisc(1, PawnColor.RED));
            students.add(new StudentDisc(1, PawnColor.RED));
            students.add(new StudentDisc(2, PawnColor.RED));
            students.add(new StudentDisc(4, PawnColor.PINK));
            students.add(new StudentDisc(5, PawnColor.PINK));
            students.add(new StudentDisc(6, PawnColor.PINK));
        }

        for (StudentDisc studentDisc : students)
            assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, gameEngine.getIslandManager().getMotherNatureIslandId()).addStudent(studentDisc));

        ProfessorPawn redProfessor = null;
        ProfessorPawn pinkProfessor = null;
        for (PawnColor color : PawnColor.values()) {
            if (color == PawnColor.RED)
                redProfessor = gameEngine.getTeams().get(1).removeProfessorPawn(color);
            else if (color == PawnColor.PINK)
                pinkProfessor = gameEngine.getTeams().get(1).removeProfessorPawn(color);
            else
                gameEngine.getTeams().get(1).removeProfessorPawn(color);
        }

        gameEngine.getTeams().get(0).addProfessorPawn(redProfessor);
        gameEngine.getTeams().get(1).addProfessorPawn(pinkProfessor);
        assertDoesNotThrow(() -> calculateInfluenceAction.act());
        assertFalse(assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, newMotherNatureIslandId)).hasTower());

        students = new ArrayList<>();
        students.add(new StudentDisc(7, PawnColor.BLUE));
        students.add(new StudentDisc(8, PawnColor.GREEN));
        students.add(new StudentDisc(9, PawnColor.RED));

        for (StudentDisc studentDisc : students)
            assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, gameEngine.getIslandManager().getMotherNatureIslandId()).addStudent(studentDisc));

        assertDoesNotThrow(() -> calculateInfluenceAction.act());
        assertEquals(assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, newMotherNatureIslandId).getTower().getColor()), assertDoesNotThrow(() -> CommonManager.takeTeamById(gameEngine, 1).getTeamTowersColor()));
    }

    @Test
    void modifyRoundAndActionList() {
        assertDoesNotThrow(()->calculateInfluenceAction.modifyRoundAndActionList());
        assertEquals(gameEngine.getRound().getPossibleActions().get(0), 3);
        assertEquals(gameEngine.getRound().getPossibleActions().get(1), 6);
    }

    @Test
    void setOptions() {
    }
}