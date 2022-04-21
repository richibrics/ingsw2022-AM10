package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.controller.User;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.game_components.PawnColor;
import it.polimi.ingsw.model.game_components.StudentDisc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TestAssignProfessorsAction {

    static GameEngine gameEngine;
    static AssignProfessorsAction assignProfessorsAction;
    static SetUpThreePlayersAction setUpThreePlayersAction;

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
        assignProfessorsAction = new AssignProfessorsAction(gameEngine);

        setUpThreePlayersAction = new SetUpThreePlayersAction(gameEngine);
        assertDoesNotThrow(()->setUpThreePlayersAction.act());
    }

    @Test
    void getKey() {
        Map<Integer, Long> studentsOfPlayer = new HashMap<>();
        studentsOfPlayer.put(1, (long) 5);
        studentsOfPlayer.put(2, (long) 4);
        studentsOfPlayer.put(3, (long) 3);
        assertEquals(assignProfessorsAction.getKey(studentsOfPlayer), 1);
    }

    @Test
    void checkMoveProfessorCondition() {
        Team winningTeam = gameEngine.getTeams().get(0);
        assertDoesNotThrow(()->winningTeam.addProfessorPawn(gameEngine.getTable().popProfessorPawn(PawnColor.GREEN)));
        assertDoesNotThrow(()->winningTeam.addProfessorPawn(gameEngine.getTable().popProfessorPawn(PawnColor.PINK)));
        Map<Integer, Long> studentsOfPlayer = new HashMap<>();
        studentsOfPlayer.put(1, (long) 5);
        studentsOfPlayer.put(2, (long) 4);
        studentsOfPlayer.put(3, (long) 3);
        assertTrue(assignProfessorsAction.checkMoveProfessorCondition(PawnColor.RED, winningTeam, studentsOfPlayer));
        assertDoesNotThrow(()->winningTeam.addProfessorPawn(gameEngine.getTable().popProfessorPawn(PawnColor.RED)));
        assertFalse(assignProfessorsAction.checkMoveProfessorCondition(PawnColor.RED, winningTeam, studentsOfPlayer));
        gameEngine.getSchoolPawnManager().moveProfessor(3,1,PawnColor.RED);
        studentsOfPlayer.put(2, (long) 5);
        assertFalse(assignProfessorsAction.checkMoveProfessorCondition(PawnColor.RED, winningTeam, studentsOfPlayer));
        assertFalse(assignProfessorsAction.checkMoveProfessorCondition(PawnColor.GREEN, winningTeam, studentsOfPlayer));
    }

    @Test
    void act() {

        assertDoesNotThrow(()->gameEngine.getTeams().get(1).addProfessorPawn(gameEngine.getTable().popProfessorPawn(PawnColor.RED)));
        assertDoesNotThrow(()->gameEngine.getTeams().get(2).addProfessorPawn(gameEngine.getTable().popProfessorPawn(PawnColor.GREEN)));
        assertDoesNotThrow(()->gameEngine.getTeams().get(0).addProfessorPawn(gameEngine.getTable().popProfessorPawn(PawnColor.YELLOW)));

        for (int i = 127; i < 130; i++) {
            int finalI = i;
            assertDoesNotThrow(()->gameEngine.getTeams().get(0).getPlayers().get(0).getSchoolBoard().addStudentToDiningRoom(new StudentDisc(finalI, PawnColor.RED)));
        }

        for (int i = 130; i < 132; i++) {
            int finalI = i;
            assertDoesNotThrow(()->gameEngine.getTeams().get(1).getPlayers().get(0).getSchoolBoard().addStudentToDiningRoom(new StudentDisc(finalI, PawnColor.RED)));
        }

        for (int i = 133; i < 136; i++) {
            int finalI = i;
            assertDoesNotThrow(()->gameEngine.getTeams().get(1).getPlayers().get(0).getSchoolBoard().addStudentToDiningRoom(new StudentDisc(finalI, PawnColor.GREEN)));
        }

        for (int i = 136; i < 139; i++) {
            int finalI = i;
            assertDoesNotThrow(() -> gameEngine.getTeams().get(2).getPlayers().get(0).getSchoolBoard().addStudentToDiningRoom(new StudentDisc(finalI, PawnColor.GREEN)));
        }

        for (int i = 139; i < 142; i++) {
            int finalI = i;
            assertDoesNotThrow(()->gameEngine.getTeams().get(0).getPlayers().get(0).getSchoolBoard().addStudentToDiningRoom(new StudentDisc(finalI, PawnColor.YELLOW)));
        }

        for (int i = 142; i < 145; i++) {
            int finalI = i;
            assertDoesNotThrow(() -> gameEngine.getTeams().get(1).getPlayers().get(0).getSchoolBoard().addStudentToDiningRoom(new StudentDisc(finalI, PawnColor.YELLOW)));
        }

        for (int i = 145; i < 147; i++) {
            int finalI = i;
            assertDoesNotThrow(() -> gameEngine.getTeams().get(2).getPlayers().get(0).getSchoolBoard().addStudentToDiningRoom(new StudentDisc(finalI, PawnColor.YELLOW)));
        }

        assertDoesNotThrow(()->assignProfessorsAction.act());
        assertEquals(gameEngine.getTeams().get(0).getProfessorTable().get(1).getColor(), PawnColor.RED);
        assertEquals(gameEngine.getTeams().get(0).getProfessorTable().get(0).getColor(), PawnColor.YELLOW);
        assertEquals(gameEngine.getTeams().get(2).getProfessorTable().get(0).getColor(), PawnColor.GREEN);

        assertDoesNotThrow(() -> gameEngine.getTeams().get(1).getPlayers().get(0).getSchoolBoard().addStudentToDiningRoom(new StudentDisc(147, PawnColor.GREEN)));
        assertDoesNotThrow(()->assignProfessorsAction.act());
        assertEquals(gameEngine.getTeams().get(1).getProfessorTable().get(0).getColor(), PawnColor.GREEN);

        assertDoesNotThrow(() -> gameEngine.getTeams().get(2).getPlayers().get(0).getSchoolBoard().addStudentToDiningRoom(new StudentDisc(148, PawnColor.YELLOW)));
        assertDoesNotThrow(()->assignProfessorsAction.act());
        assertEquals(gameEngine.getTeams().get(0).getProfessorTable().get(0).getColor(), PawnColor.YELLOW);
    }

    /**
     * Not implemented.
     */

    @Test
    void setOptions() { }

    /**
     * Not implemented.
     */

    @Test
    void modifyRound() { }
}