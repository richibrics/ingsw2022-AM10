package it.polimi.ingsw.model.actions.effects;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.controller.User;
import it.polimi.ingsw.controller.exceptions.WrongMessageContentException;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.actions.SetUpTwoAndFourPlayersAction;
import it.polimi.ingsw.model.game_components.*;
import it.polimi.ingsw.model.managers.CommonManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class TestAmbassadorEffectAction {

    static AmbassadorEffectAction ambassadorEffectAction;
    static SetUpTwoAndFourPlayersAction setUpTwoAndFourPlayersAction;
    static GameEngine gameEngine;

    @BeforeEach
    void setUp() {
        User user1 = new User("1", 4);
        User user2 = new User("2", 4);
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
        ambassadorEffectAction = new AmbassadorEffectAction(gameEngine);
        setUpTwoAndFourPlayersAction = new SetUpTwoAndFourPlayersAction(gameEngine);
        assertDoesNotThrow(() -> gameEngine.getActionManager().generateActions());
    }

    /**
     * Tests only exception throw or not.
     */
    @Test
    void setOptions() {
        HashMap<String, String> options = new HashMap<>();
        // no key
        assertThrows(WrongMessageContentException.class, () -> ambassadorEffectAction.setOptions(options));
        // Valid islandId
        options.put(ModelConstants.ACTION_CALCULATE_INFLUENCE_OPTIONS_KEY_ISLAND, "hello");
        assertDoesNotThrow(() -> ambassadorEffectAction.setOptions(options));
        options.put(ModelConstants.ACTION_CALCULATE_INFLUENCE_OPTIONS_KEY_ISLAND, "-1");
        assertDoesNotThrow(() -> ambassadorEffectAction.setOptions(options));
    }

    @Test
    void modifyRoundAndActionList() {
    }

    @RepeatedTest(20)
    void act() {
        /* This test considers the case this.islandId != -1. See TestCalculateInfluenceAction for this.islandId == -1 */
        ArrayList<StudentDisc> studentDiscs = assertDoesNotThrow(() -> gameEngine.getTable().getBag().drawStudents(18));

        /* Add student discs to island tile with id 3 */
        for (StudentDisc studentDisc : studentDiscs)
            assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, 3).addStudent(studentDisc));
        /* Add the professor pawns to the second team */
        for (ProfessorPawn professorPawn : assertDoesNotThrow(() -> gameEngine.getTable().getAvailableProfessorPawns()))
            gameEngine.getTeams().get(1).addProfessorPawn(professorPawn);
        /* Set islandId of Ambassador Effect */
        HashMap<String, String> options = new HashMap<>();
        options.put(ModelConstants.ACTION_AMBASSADOR_OPTIONS_KEY_ISLAND, "3");
        assertDoesNotThrow(() -> ambassadorEffectAction.setOptions(options));
        /* Set no entry tile on island with id 3 */
        assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, 3).setNoEntry(true));
        assertDoesNotThrow(() -> ambassadorEffectAction.act());
        /* Changes should have been applied even though the island has a no entry tile */
        assertEquals(assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, 3).getTower().getColor()), assertDoesNotThrow(() -> gameEngine.getTeams().get(1).getTeamTowersColor()));
        assertTrue(assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, 3).hasNoEntry()));

        /* Check if it works with a different calculator of influence */
        CalculateInfluenceActionMushroomHunterEffect calculateInfluenceActionMushroomHunterEffect = new CalculateInfluenceActionMushroomHunterEffect(gameEngine, gameEngine.getActionManager().getActions()[ModelConstants.ACTION_CALCULATE_INFLUENCE_ID]);
        gameEngine.getActionManager().getActions()[ModelConstants.ACTION_CALCULATE_INFLUENCE_ID] = calculateInfluenceActionMushroomHunterEffect;
        HashMap<String, String> options1 = new HashMap<>();
        options1.put(ModelConstants.ACTION_CALCULATE_INFLUENCE_MUSHROOM_HUNTER_OPTIONS_KEY_COLOR, "red");
        assertDoesNotThrow(() -> calculateInfluenceActionMushroomHunterEffect.setOptions(options1));
        HashMap<String, String> options2 = new HashMap<>();
        options2.put(ModelConstants.ACTION_AMBASSADOR_OPTIONS_KEY_ISLAND, "4");
        assertDoesNotThrow(() -> ambassadorEffectAction.setOptions(options2));

        ArrayList<StudentDisc> students = new ArrayList<>();
        int motherNatureIslandId = assertDoesNotThrow(() -> gameEngine.getIslandManager().getMotherNatureIslandId());
        IslandTile islandTile = assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, 4));
        /* Place no entry tile on island 4 */
        islandTile.setNoEntry(true);
        if (motherNatureIslandId != 4 && ((motherNatureIslandId + ModelConstants.NUMBER_OF_ISLAND_TILES / 2) % ModelConstants.NUMBER_OF_ISLAND_TILES) != 4) {
            /* The island with id 4 already has a student disc if it is not the island with mother nature or the island opposite
            to the island with mother nature */
            if (islandTile.peekStudents().get(0).getColor().equals(PawnColor.RED)) {
                /* 2 red students and 3 pink students if the new island already has a red student */
                students.add(new StudentDisc(1, PawnColor.RED));
                students.add(new StudentDisc(2, PawnColor.RED));
                students.add(new StudentDisc(4, PawnColor.PINK));
                students.add(new StudentDisc(5, PawnColor.PINK));
                students.add(new StudentDisc(6, PawnColor.PINK));
            } else if (islandTile.peekStudents().get(0).getColor().equals(PawnColor.PINK)) {
                /* 3 red students and 2 pink students if the new island already has a pink student */
                students.add(new StudentDisc(1, PawnColor.RED));
                students.add(new StudentDisc(2, PawnColor.RED));
                students.add(new StudentDisc(3, PawnColor.RED));
                students.add(new StudentDisc(5, PawnColor.PINK));
                students.add(new StudentDisc(6, PawnColor.PINK));
            }
        } else {

            students.add(new StudentDisc(1, PawnColor.RED));
            students.add(new StudentDisc(1, PawnColor.RED));
            students.add(new StudentDisc(2, PawnColor.RED));
            students.add(new StudentDisc(4, PawnColor.PINK));
            students.add(new StudentDisc(5, PawnColor.PINK));
            students.add(new StudentDisc(6, PawnColor.PINK));
        }
        /* Add students to island 4 */
        for (StudentDisc studentDisc : students)
            assertDoesNotThrow(() -> islandTile.addStudent(studentDisc));
        /* Remove professor pawns from second team */
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
        /* Assign red professor to first team and pink professor to second team */
        gameEngine.getTeams().get(0).addProfessorPawn(redProfessor);
        gameEngine.getTeams().get(1).addProfessorPawn(pinkProfessor);

        /**
         * There are 2 cases with the next statement:
         *  - If Mother Nature isn't on Island 4: influence is calculated on another island so the NoEntry tile
         *  remains on the island 4
         *  - If Mother Nature is on Island 4: influence should be calculated on island 4 but there's a NoEntry tile on it,
         *  so it is removed and no Tower is set.
         *  Currently I have a Black Tower on Island 3 (which has a No Entry Tile) and nothing on island 4.
         *  When I will use the Ambassador effect in the following statement (passing islandId = 4), the "Calculate Influence"
         *  will run on island 4, but with the MushroomHunter effect activated before, so in this situation the Island 4
         *  has influence for Black Tower.
         *  With the black towers on islands 3 and 4, there's a merge and this makes the NoEntry tile spread from Island 3
         *  to the Island 4.
         *  So, also in this situation, I have a NoEntry tile on Island 4 after the next 2 statements.
         */

        assertDoesNotThrow(()->gameEngine.getActionManager().prepareAndExecuteAction(ModelConstants.ACTION_CALCULATE_INFLUENCE_ID, -1, options1, false));
        assertDoesNotThrow(()->ambassadorEffectAction.act());

        assertTrue(islandTile.hasNoEntry());
    }
}