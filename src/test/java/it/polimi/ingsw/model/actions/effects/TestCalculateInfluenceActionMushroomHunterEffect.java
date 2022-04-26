package it.polimi.ingsw.model.actions.effects;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.controller.User;
import it.polimi.ingsw.controller.exceptions.WrongMessageContentException;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.actions.CalculateInfluenceAction;
import it.polimi.ingsw.model.actions.SetUpTwoAndFourPlayersAction;
import it.polimi.ingsw.model.game_components.IslandTile;
import it.polimi.ingsw.model.game_components.PawnColor;
import it.polimi.ingsw.model.game_components.ProfessorPawn;
import it.polimi.ingsw.model.game_components.StudentDisc;
import it.polimi.ingsw.model.managers.CommonManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class TestCalculateInfluenceActionMushroomHunterEffect {

    static GameEngine gameEngine;
    static CalculateInfluenceActionMushroomHunterEffect calculateInfluenceActionMushroomHunterEffect;
    static SetUpTwoAndFourPlayersAction setUpTwoAndFourPlayersAction;
    static CalculateInfluenceAction calculateInfluenceAction;

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
        calculateInfluenceActionMushroomHunterEffect = new CalculateInfluenceActionMushroomHunterEffect(gameEngine, calculateInfluenceAction);
        setUpTwoAndFourPlayersAction = new SetUpTwoAndFourPlayersAction(gameEngine);
        assertDoesNotThrow(() -> setUpTwoAndFourPlayersAction.act());
    }

    @Test
    void calculateInfluences() {
        ArrayList<StudentDisc> studentDiscs = assertDoesNotThrow(() -> gameEngine.getTable().getBag().drawStudents(20));
        for (StudentDisc studentDisc : studentDiscs)
            assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, gameEngine.getIslandManager().getMotherNatureIslandId()).addStudent(studentDisc));

        int motherNatureIslandId = assertDoesNotThrow(() -> gameEngine.getIslandManager().getMotherNatureIslandId());
        if (motherNatureIslandId != 12) {
            assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, motherNatureIslandId).setTower(gameEngine.getTeams().get(0).popTower()));
            assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, motherNatureIslandId + 1).setTower(gameEngine.getTeams().get(0).popTower()));
        } else {
            assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, motherNatureIslandId).setTower(gameEngine.getTeams().get(0).popTower()));
            assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, motherNatureIslandId - 1).setTower(gameEngine.getTeams().get(0).popTower()));
        }

        assertDoesNotThrow(() -> gameEngine.getIslandManager().unifyPossibleIslands());

        int influence1 = 2;
        int influence2 = 0;
        PawnColor color = PawnColor.RED;

        Random rand = new Random();

        IslandTile motherNatureIsland = assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, gameEngine.getIslandManager().getMotherNatureIslandId()));
        List<ArrayList<IslandTile>> islandGroups = assertDoesNotThrow(() -> gameEngine.getTable().getIslandTiles().stream().filter(islandGroup -> islandGroup.contains(motherNatureIsland)).toList());

        for (ProfessorPawn professorPawn : assertDoesNotThrow(() -> gameEngine.getTable().getAvailableProfessorPawns())) {
            int index = rand.nextInt(0, 2);
            gameEngine.getTeams().get(index).addProfessorPawn(professorPawn);
            if (!professorPawn.getColor().equals(color)) {
                if (index == 0)
                    for (IslandTile islandTile : islandGroups.get(0))
                        influence1 += assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, islandTile.getId()).peekStudents().stream().filter(studentDisc -> studentDisc.getColor().equals(professorPawn.getColor())).collect(Collectors.reducing(0, e -> 1, Integer::sum)));
                else if (index == 1)
                    for (IslandTile islandTile : islandGroups.get(0))
                        influence2 += assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, islandTile.getId()).peekStudents().stream().filter(studentDisc -> studentDisc.getColor().equals(professorPawn.getColor())).collect(Collectors.reducing(0, e -> 1, Integer::sum)));
            }
        }

        Map<Integer, Integer> influences = new HashMap<>();
        Map<String, String> options = new HashMap<>();
        options.put("hello", "red");
        assertThrows(Exception.class, () -> calculateInfluenceActionMushroomHunterEffect.setOptions(options));
        options.put("color", "red");
        assertDoesNotThrow(() -> calculateInfluenceActionMushroomHunterEffect.setOptions(options));
        assertDoesNotThrow(() -> calculateInfluenceActionMushroomHunterEffect.calculateInfluences(influences, islandGroups.get(0)));
        assertEquals(influence1, influences.get(1));
        assertEquals(influence2, influences.get(2));
    }

    @Test
    void setOptions() {
        HashMap<String, String> options = new HashMap<>();
        // No id
        assertThrows(WrongMessageContentException.class, () -> calculateInfluenceActionMushroomHunterEffect.setOptions(options));

        // Value error
        options.put(ModelConstants.ACTION_ON_SELECTION_OF_WIZARD_OPTIONS_KEY_COLOR, "a");
        assertThrows(WrongMessageContentException.class, () -> calculateInfluenceActionMushroomHunterEffect.setOptions(options));

        // OK
        options.put(ModelConstants.ACTION_ON_SELECTION_OF_WIZARD_OPTIONS_KEY_COLOR,"red");
        assertDoesNotThrow(() -> calculateInfluenceActionMushroomHunterEffect.setOptions(options));
        options.put(ModelConstants.ACTION_ON_SELECTION_OF_WIZARD_OPTIONS_KEY_COLOR,"YeLLoW");
        assertDoesNotThrow(() -> calculateInfluenceActionMushroomHunterEffect.setOptions(options));
    }
}