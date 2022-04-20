package it.polimi.ingsw.model.actions.effects;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.controller.User;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.actions.CalculateInfluenceAction;
import it.polimi.ingsw.model.actions.SetUpTwoAndFourPlayersAction;
import it.polimi.ingsw.model.game_components.IslandTile;
import it.polimi.ingsw.model.game_components.ProfessorPawn;
import it.polimi.ingsw.model.game_components.StudentDisc;
import it.polimi.ingsw.model.managers.CommonManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class TestCalculateInfluenceActionKnightEffectDecorator {

    static GameEngine gameEngine;
    static CalculateInfluenceActionKnightEffectDecorator calculateInfluenceActionKnightEffectDecorator;
    static CalculateInfluenceAction calculateInfluenceAction;
    static SetUpTwoAndFourPlayersAction setUpTwoAndFourPlayersAction;

    @BeforeEach
    void setUp() {
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
        calculateInfluenceActionKnightEffectDecorator = new CalculateInfluenceActionKnightEffectDecorator(gameEngine);
        calculateInfluenceAction = new CalculateInfluenceAction(gameEngine);
        setUpTwoAndFourPlayersAction = new SetUpTwoAndFourPlayersAction(gameEngine);
        assertDoesNotThrow(()->setUpTwoAndFourPlayersAction.act());
    }

    @Test
    void calculateInfluences() {
        calculateInfluenceActionKnightEffectDecorator.setCalculateInfluenceAction(calculateInfluenceAction);
        ArrayList<StudentDisc> studentDiscs = assertDoesNotThrow(() -> gameEngine.getTable().getBag().drawStudents(15));
        for (StudentDisc studentDisc : studentDiscs)
            assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, gameEngine.getIslandManager().getMotherNatureIslandId()).addStudent(studentDisc));

        int motherNatureIslandId = assertDoesNotThrow(()->gameEngine.getIslandManager().getMotherNatureIslandId());
        if (motherNatureIslandId != 12) {
            assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, motherNatureIslandId).setTower(gameEngine.getTeams().get(0).popTower()));
            assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, motherNatureIslandId + 1).setTower(gameEngine.getTeams().get(0).popTower()));
        }
        else {
            assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, motherNatureIslandId).setTower(gameEngine.getTeams().get(0).popTower()));
            assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, motherNatureIslandId - 1).setTower(gameEngine.getTeams().get(0).popTower()));
        }

        assertDoesNotThrow(()->gameEngine.getIslandManager().unifyPossibleIslands());

        int influence1 = 4;
        int influence2 = 0;

        Random rand = new Random();

        IslandTile motherNatureIsland = assertDoesNotThrow(()->CommonManager.takeIslandTileById(gameEngine, gameEngine.getIslandManager().getMotherNatureIslandId()));
        List<ArrayList<IslandTile>> islandGroups = assertDoesNotThrow(()->gameEngine.getTable().getIslandTiles().stream().filter(islandGroup-> islandGroup.contains(motherNatureIsland)).toList());

        for (ProfessorPawn professorPawn : assertDoesNotThrow(()->gameEngine.getTable().getAvailableProfessorPawns())) {
            int index = rand.nextInt(0, 2);
            gameEngine.getTeams().get(index).addProfessorPawn(professorPawn);
            if (index == 0)
                for (IslandTile islandTile : islandGroups.get(0))
                    influence1 += assertDoesNotThrow(()->CommonManager.takeIslandTileById(gameEngine, islandTile.getId()).peekStudents().stream().filter(studentDisc -> studentDisc.getColor().equals(professorPawn.getColor())).collect(Collectors.reducing(0, e -> 1, Integer::sum)));
            else if (index == 1)
                for (IslandTile islandTile : islandGroups.get(0))
                    influence2 += assertDoesNotThrow(()->CommonManager.takeIslandTileById(gameEngine, islandTile.getId()).peekStudents().stream().filter(studentDisc -> studentDisc.getColor().equals(professorPawn.getColor())).collect(Collectors.reducing(0, e -> 1, Integer::sum)));
        }

        Map<Integer, Integer> influences = new HashMap<>();

        calculateInfluenceActionKnightEffectDecorator.setPlayerId(1);
        assertDoesNotThrow(()->calculateInfluenceActionKnightEffectDecorator.calculateInfluences(influences, islandGroups.get(0)));
        assertEquals(influence1, influences.get(1));
        assertEquals(influence2, influences.get(2));
    }

    @Test
    void setOptions() {
    }
}