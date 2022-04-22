package it.polimi.ingsw.model.actions.effects;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.controller.User;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.actions.AssignProfessorsAction;
import it.polimi.ingsw.model.actions.SetUpTwoAndFourPlayersAction;
import it.polimi.ingsw.model.game_components.PawnColor;
import it.polimi.ingsw.model.game_components.StudentDisc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TestAssignProfessorActionCook {

    static GameEngine gameEngine;
    static AssignProfessorsAction assignProfessorsAction;
    static AssignProfessorActionCookEffect assignProfessorActionCookEffect;
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
        assignProfessorsAction = new AssignProfessorsAction(gameEngine);
        assignProfessorActionCookEffect = new AssignProfessorActionCookEffect(gameEngine, assignProfessorsAction);
        setUpTwoAndFourPlayersAction  = new SetUpTwoAndFourPlayersAction(gameEngine);
        assertDoesNotThrow(()->setUpTwoAndFourPlayersAction.act());
    }

    @Test
    void getAssignProfessorAction() {
        assertEquals(assignProfessorsAction, assignProfessorActionCookEffect.getAssignProfessorAction());
    }

    @Test
    void getKey() {
        assignProfessorActionCookEffect.setPlayerId(2);
        Map<Integer, Long> studentsOfPlayer = new HashMap<>();
        studentsOfPlayer.put(1, (long) 5);
        studentsOfPlayer.put(2, (long) 4);
        assertEquals(assignProfessorActionCookEffect.getKey(studentsOfPlayer), 1);
        studentsOfPlayer.put(2, (long) 5);
        assertEquals(assignProfessorActionCookEffect.getKey(studentsOfPlayer), 2);
    }

    @Test
    void checkMoveProfessorCondition() {
        /* The condition is in the form F = A and (B or (C and D)) */
        assignProfessorActionCookEffect.setPlayerId(1);
        Team winningTeam = gameEngine.getTeams().get(0);
        assertDoesNotThrow(()->winningTeam.addProfessorPawn(gameEngine.getTable().popProfessorPawn(PawnColor.GREEN)));
        assertDoesNotThrow(()->winningTeam.addProfessorPawn(gameEngine.getTable().popProfessorPawn(PawnColor.PINK)));
        Map<Integer, Long> studentsOfPlayer = new HashMap<>();
        studentsOfPlayer.put(1, (long) 5);
        studentsOfPlayer.put(2, (long) 4);
        /* A = TRUE, B = TRUE, C = TRUE, D = TRUE, F = TRUE */
        assertTrue(assignProfessorActionCookEffect.checkMoveProfessorCondition(PawnColor.RED, winningTeam, studentsOfPlayer));

        assertDoesNotThrow(()->winningTeam.addProfessorPawn(gameEngine.getTable().popProfessorPawn(PawnColor.RED)));
        /* A = FALSE, B = TRUE, C = TRUE, D = TRUE, F = FALSE */
        assertFalse(assignProfessorActionCookEffect.checkMoveProfessorCondition(PawnColor.RED, winningTeam, studentsOfPlayer));

        gameEngine.getSchoolPawnManager().moveProfessor(2,1,PawnColor.RED);
        studentsOfPlayer.put(2, (long) 5);
        /* A = TRUE, B = FALSE, C = TRUE, D = TRUE, F = TRUE */
        assertTrue(assignProfessorActionCookEffect.checkMoveProfessorCondition(PawnColor.RED, winningTeam, studentsOfPlayer));

        Team newWinningTeam = gameEngine.getTeams().get(1);
        /* A = TRUE, B = FALSE, C = FALSE, D = TRUE, F = FALSE */
        assertFalse(assignProfessorActionCookEffect.checkMoveProfessorCondition(PawnColor.GREEN, newWinningTeam, studentsOfPlayer));

        studentsOfPlayer.put(1, (long) 0);
        studentsOfPlayer.put(2, (long) 0);
        /* A = TRUE, B = FALSE, C = TRUE, D = FALSE, F = FALSE */
        assertFalse(assignProfessorActionCookEffect.checkMoveProfessorCondition(PawnColor.YELLOW, winningTeam, studentsOfPlayer));
    }

    @Test
    void act() {
        assignProfessorActionCookEffect.setPlayerId(1);
        assertDoesNotThrow(()->gameEngine.getTeams().get(0).addProfessorPawn(gameEngine.getTable().popProfessorPawn(PawnColor.RED)));
        assertDoesNotThrow(()->gameEngine.getTeams().get(1).addProfessorPawn(gameEngine.getTable().popProfessorPawn(PawnColor.GREEN)));
        assertDoesNotThrow(()->gameEngine.getTeams().get(0).addProfessorPawn(gameEngine.getTable().popProfessorPawn(PawnColor.YELLOW)));

        for (int i = 126; i < 129; i++) {
            int finalI = i;
            assertDoesNotThrow(()->gameEngine.getTeams().get(0).getPlayers().get(0).getSchoolBoard().addStudentToDiningRoom(new StudentDisc(finalI, PawnColor.RED)));
        }

        for (int i = 129; i < 132; i++) {
            int finalI = i;
            assertDoesNotThrow(()->gameEngine.getTeams().get(1).getPlayers().get(0).getSchoolBoard().addStudentToDiningRoom(new StudentDisc(finalI, PawnColor.RED)));
        }

        for (int i = 133; i < 136; i++) {
            int finalI = i;
            assertDoesNotThrow(()->gameEngine.getTeams().get(0).getPlayers().get(0).getSchoolBoard().addStudentToDiningRoom(new StudentDisc(finalI, PawnColor.GREEN)));
        }

        for (int i = 136; i < 139; i++) {
            int finalI = i;
            assertDoesNotThrow(() -> gameEngine.getTeams().get(1).getPlayers().get(0).getSchoolBoard().addStudentToDiningRoom(new StudentDisc(finalI, PawnColor.GREEN)));
        }

        for (int i = 139; i < 142; i++) {
            int finalI = i;
            assertDoesNotThrow(()->gameEngine.getTeams().get(0).getPlayers().get(0).getSchoolBoard().addStudentToDiningRoom(new StudentDisc(finalI, PawnColor.YELLOW)));
        }

        for (int i = 142; i < 146; i++) {
            int finalI = i;
            assertDoesNotThrow(() -> gameEngine.getTeams().get(1).getPlayers().get(0).getSchoolBoard().addStudentToDiningRoom(new StudentDisc(finalI, PawnColor.YELLOW)));
        }

        assertDoesNotThrow(()->assignProfessorActionCookEffect.act());
        /* Both players have the same number of red students. The professor should be assigned to the first team */
        assertEquals(gameEngine.getTeams().get(0).getProfessorTable().get(0).getColor(), PawnColor.RED);
        /* Both players have the same number of green students. The professor should be assigned to the first team even
        * though the professor is in the professor table of the second team */
        assertEquals(gameEngine.getTeams().get(0).getProfessorTable().get(1).getColor(), PawnColor.GREEN);
        /* The second player has 4 yellow students, while the first player has 3 yellow students. The professor
         * should be assigned to the second team */
        assertEquals(gameEngine.getTeams().get(1).getProfessorTable().get(0).getColor(), PawnColor.YELLOW);

        assertDoesNotThrow(() -> gameEngine.getTeams().get(1).getPlayers().get(0).getSchoolBoard().addStudentToDiningRoom(new StudentDisc(147, PawnColor.GREEN)));
        assertDoesNotThrow(()->assignProfessorActionCookEffect.act());
        /* Now the second player has more green students than the first player */
        assertEquals(gameEngine.getTeams().get(1).getProfessorTable().get(1).getColor(), PawnColor.GREEN);
    }

    @Test
    void setOptions() {
    }

    @Test
    void modifyRoundAndActionList() {
        gameEngine.getActionManager().getActions()[ModelConstants.ACTION_ASSIGN_PROFESSORS_ID] = assignProfessorActionCookEffect;
        assertDoesNotThrow(()->assignProfessorActionCookEffect.modifyRoundAndActionList());
        assertEquals(gameEngine.getActionManager().getActions()[ModelConstants.ACTION_ASSIGN_PROFESSORS_ID], assignProfessorsAction);
    }
}