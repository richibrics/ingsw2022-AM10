package it.polimi.ingsw.model.actions.effects;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.controller.User;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.actions.CalculateInfluenceAction;
import it.polimi.ingsw.model.actions.SetUpThreePlayersAction;
import it.polimi.ingsw.model.game_components.IslandTile;
import it.polimi.ingsw.model.game_components.PawnColor;
import it.polimi.ingsw.model.game_components.ProfessorPawn;
import it.polimi.ingsw.model.game_components.StudentDisc;
import it.polimi.ingsw.model.managers.CommonManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TestCalculateInfluenceActionCentaurEffect {

    static GameEngine gameEngine;
    static CalculateInfluenceActionCentaurEffect calculateInfluenceActionCentaurEffect;
    static SetUpThreePlayersAction setUpThreePlayersAction;
    static CalculateInfluenceAction calculateInfluenceAction;

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
        calculateInfluenceAction = new CalculateInfluenceAction(gameEngine);
        calculateInfluenceActionCentaurEffect = new CalculateInfluenceActionCentaurEffect(gameEngine, calculateInfluenceAction);

        setUpThreePlayersAction = new SetUpThreePlayersAction(gameEngine);
        assertDoesNotThrow(() -> setUpThreePlayersAction.act());
    }

    @Test
    void getCalculateInfluenceAction() {
        assertEquals(calculateInfluenceActionCentaurEffect.getCalculateInfluenceAction(), calculateInfluenceAction);
    }

    @Test
    void calculateInfluences() {
        ArrayList<StudentDisc> studentDiscs = assertDoesNotThrow(() -> gameEngine.getTable().getBag().drawStudents(10));
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

        int influence1 = 0;
        int influence2 = 0;
        int influence3 = 0;

        Random rand = new Random();

        IslandTile motherNatureIsland = assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, gameEngine.getIslandManager().getMotherNatureIslandId()));
        List<ArrayList<IslandTile>> islandGroups = assertDoesNotThrow(() -> gameEngine.getTable().getIslandTiles().stream().filter(islandGroup -> islandGroup.contains(motherNatureIsland)).toList());

        for (ProfessorPawn professorPawn : assertDoesNotThrow(() -> gameEngine.getTable().getAvailableProfessorPawns())) {
            int index = rand.nextInt(0, 3);
            gameEngine.getTeams().get(index).addProfessorPawn(professorPawn);
            if (index == 0)
                for (IslandTile islandTile : islandGroups.get(0))
                    influence1 += assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, islandTile.getId()).peekStudents().stream().filter(studentDisc -> studentDisc.getColor().equals(professorPawn.getColor())).collect(Collectors.reducing(0, e -> 1, Integer::sum)));
            else if (index == 1)
                for (IslandTile islandTile : islandGroups.get(0))
                    influence2 += assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, islandTile.getId()).peekStudents().stream().filter(studentDisc -> studentDisc.getColor().equals(professorPawn.getColor())).collect(Collectors.reducing(0, e -> 1, Integer::sum)));
            else if (index == 2)
                for (IslandTile islandTile : islandGroups.get(0))
                    influence3 += assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, islandTile.getId()).peekStudents().stream().filter(studentDisc -> studentDisc.getColor().equals(professorPawn.getColor())).collect(Collectors.reducing(0, e -> 1, Integer::sum)));
        }

        Map<Integer, Integer> influences = new HashMap<>();

        assertDoesNotThrow(() -> calculateInfluenceActionCentaurEffect.calculateInfluences(influences, islandGroups.get(0)));
        assertEquals(influence1, influences.get(1));
        assertEquals(influence2, influences.get(2));
        assertEquals(influence3, influences.get(3));
    }

    @RepeatedTest(10)
    void act() {
        int motherNatureIslandId = assertDoesNotThrow(() -> gameEngine.getIslandManager().getMotherNatureIslandId());
        assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, motherNatureIslandId).setTower(gameEngine.getTeams().get(0).popTower()));

        ArrayList<StudentDisc> students = new ArrayList<>();
        students.add(new StudentDisc(1, PawnColor.RED));
        students.add(new StudentDisc(2, PawnColor.RED));
        students.add(new StudentDisc(3, PawnColor.YELLOW));
        students.add(new StudentDisc(4, PawnColor.YELLOW));
        students.add(new StudentDisc(5, PawnColor.YELLOW));

        for (StudentDisc studentDisc : students)
            assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, gameEngine.getIslandManager().getMotherNatureIslandId()).addStudent(studentDisc));

        ProfessorPawn yellowProfessor = assertDoesNotThrow(() -> gameEngine.getTable().popProfessorPawn(PawnColor.YELLOW));
        ProfessorPawn redProfessor = assertDoesNotThrow(() -> gameEngine.getTable().popProfessorPawn(PawnColor.RED));
        gameEngine.getTeams().get(1).addProfessorPawn(yellowProfessor);
        gameEngine.getTeams().get(0).addProfessorPawn(redProfessor);

        assertDoesNotThrow(() -> calculateInfluenceActionCentaurEffect.act());
        assertEquals(assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, motherNatureIslandId).getTower().getColor()), assertDoesNotThrow(() -> CommonManager.takeTeamById(gameEngine, 2).getTeamTowersColor()));
    }

    @Test
    void modifyRoundAndActionList() {
        gameEngine.getActionManager().getActions()[ModelConstants.ACTION_CALCULATE_INFLUENCE_ID] = calculateInfluenceActionCentaurEffect;
        assertDoesNotThrow(() -> calculateInfluenceActionCentaurEffect.modifyRoundAndActionList());
        assertEquals(gameEngine.getRound().getPossibleActions().get(0), 3);
        assertEquals(gameEngine.getRound().getPossibleActions().get(1), 6);
        assertEquals(gameEngine.getActionManager().getActions()[ModelConstants.ACTION_CALCULATE_INFLUENCE_ID], calculateInfluenceAction);
    }

    @Test
    void setOptions() {
    }
}