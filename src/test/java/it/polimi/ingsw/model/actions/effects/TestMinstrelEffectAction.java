package it.polimi.ingsw.model.actions.effects;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.controller.User;
import it.polimi.ingsw.controller.exceptions.WrongMessageContentException;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.actions.SetUpTwoAndFourPlayersAction;
import it.polimi.ingsw.model.exceptions.IllegalGameActionException;
import it.polimi.ingsw.model.exceptions.IllegalStudentDiscMovementException;
import it.polimi.ingsw.model.game_components.SchoolBoard;
import it.polimi.ingsw.model.game_components.StudentDisc;
import it.polimi.ingsw.model.managers.CommonManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class TestMinstrelEffectAction {
    static GameEngine gameEngine;
    static MinstrelEffectAction minstrelEffectAction;

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
        SchoolBoard schoolBoard1 = new SchoolBoard();
        SchoolBoard schoolBoard2 = new SchoolBoard();
        ArrayList<SchoolBoard> schoolBoards = new ArrayList<>();
        player1.setSchoolBoard(schoolBoard1);
        player2.setSchoolBoard(schoolBoard2);
        schoolBoards.add(schoolBoard1);
        schoolBoards.add(schoolBoard2);


        gameEngine = new GameEngine(teams);
        assertDoesNotThrow(() -> gameEngine.getActionManager().generateActions());
        SetUpTwoAndFourPlayersAction setUpTwoAndFourPlayersAction = new SetUpTwoAndFourPlayersAction(gameEngine);
        assertDoesNotThrow(() -> setUpTwoAndFourPlayersAction.act());

        minstrelEffectAction = new MinstrelEffectAction(gameEngine);
    }

    /**
     * Tests only exception throw or not.
     */
    @Test
    void setOptions() {
        HashMap<String, String> options = new HashMap<>();

        // No students Ids
        assertThrows(WrongMessageContentException.class, () -> minstrelEffectAction.setOptions(options));
        options.put(ModelConstants.ACTION_MINSTREL_OPTIONS_KEY_STUDENT_IN_DINING_ROOM1, "1");
        assertThrows(WrongMessageContentException.class, () -> minstrelEffectAction.setOptions(options));
        options.put(ModelConstants.ACTION_MINSTREL_OPTIONS_KEY_STUDENT_IN_ENTRANCE1, "1");
        options.put(ModelConstants.ACTION_MINSTREL_OPTIONS_KEY_STUDENT_IN_DINING_ROOM2, "5");
        assertThrows(WrongMessageContentException.class, () -> minstrelEffectAction.setOptions(options));

        // Parse error

        options.clear();
        options.put(ModelConstants.ACTION_MINSTREL_OPTIONS_KEY_STUDENT_IN_DINING_ROOM1, "c");
        assertThrows(WrongMessageContentException.class, () -> minstrelEffectAction.setOptions(options));
        options.put(ModelConstants.ACTION_MINSTREL_OPTIONS_KEY_STUDENT_IN_DINING_ROOM1, "3");
        options.put(ModelConstants.ACTION_MINSTREL_OPTIONS_KEY_STUDENT_IN_ENTRANCE1, "a");
        assertThrows(WrongMessageContentException.class, () -> minstrelEffectAction.setOptions(options));
        options.put(ModelConstants.ACTION_MINSTREL_OPTIONS_KEY_STUDENT_IN_ENTRANCE1, "2");
        options.put(ModelConstants.ACTION_MINSTREL_OPTIONS_KEY_STUDENT_IN_DINING_ROOM2, "d");
        assertThrows(WrongMessageContentException.class, () -> minstrelEffectAction.setOptions(options));
        options.put(ModelConstants.ACTION_MINSTREL_OPTIONS_KEY_STUDENT_IN_DINING_ROOM2, "4");
        options.put(ModelConstants.ACTION_MINSTREL_OPTIONS_KEY_STUDENT_IN_ENTRANCE2, "b");
        assertThrows(WrongMessageContentException.class, () -> minstrelEffectAction.setOptions(options));

        //Ok
        options.clear();
        options.put(ModelConstants.ACTION_MINSTREL_OPTIONS_KEY_STUDENT_IN_DINING_ROOM1, "3");
        options.put(ModelConstants.ACTION_MINSTREL_OPTIONS_KEY_STUDENT_IN_ENTRANCE1, "2");
        assertDoesNotThrow(() -> minstrelEffectAction.setOptions(options));

        //Ok
        options.clear();
        options.put(ModelConstants.ACTION_MINSTREL_OPTIONS_KEY_STUDENT_IN_DINING_ROOM1, "3");
        options.put(ModelConstants.ACTION_MINSTREL_OPTIONS_KEY_STUDENT_IN_ENTRANCE1, "2");
        options.put(ModelConstants.ACTION_MINSTREL_OPTIONS_KEY_STUDENT_IN_DINING_ROOM2, "5");
        options.put(ModelConstants.ACTION_MINSTREL_OPTIONS_KEY_STUDENT_IN_ENTRANCE2, "6");
        assertDoesNotThrow(() -> minstrelEffectAction.setOptions(options));
    }


    /**
     * Tests property set to the correct entrance and diningRoom students.
     * A lot of tests are essential because there are a lot of random possibilities of drawing form bag.
     */
    @RepeatedTest(100)
    void act() {
        HashMap<String, String> options = new HashMap<>();
        int studentEntrance1;
        int studentEntrance2;
        int student3;
        int student4;


        //The studentInDiningRoom2 is in the last position of the table

        student3 = assertDoesNotThrow(() -> CommonManager.takePlayerById(gameEngine, 1).getSchoolBoard().getEntrance().get(0).getId());
        student4 = assertDoesNotThrow(() -> CommonManager.takePlayerById(gameEngine, 1).getSchoolBoard().getEntrance().get(4).getId());
        assertDoesNotThrow(() -> gameEngine.getSchoolPawnManager().moveStudentFromEntranceToDiningRoom(1, student3));
        assertDoesNotThrow(() -> gameEngine.getSchoolPawnManager().moveStudentFromEntranceToDiningRoom(1, student4));

        options.put(ModelConstants.ACTION_MINSTREL_OPTIONS_KEY_STUDENT_IN_DINING_ROOM1, String.valueOf(student3));
        options.put(ModelConstants.ACTION_MINSTREL_OPTIONS_KEY_STUDENT_IN_DINING_ROOM2, String.valueOf(student4));


        studentEntrance1 = assertDoesNotThrow(() -> CommonManager.takePlayerById(gameEngine, 1).getSchoolBoard().getEntrance().get(1).getId());
        options.put(ModelConstants.ACTION_MINSTREL_OPTIONS_KEY_STUDENT_IN_ENTRANCE1, String.valueOf(studentEntrance1));

        studentEntrance2 = assertDoesNotThrow(() -> CommonManager.takePlayerById(gameEngine, 1).getSchoolBoard().getEntrance().get(2).getId());
        options.put(ModelConstants.ACTION_MINSTREL_OPTIONS_KEY_STUDENT_IN_ENTRANCE2, String.valueOf(studentEntrance2));


        minstrelEffectAction.setPlayerId(1);
        assertDoesNotThrow(() -> minstrelEffectAction.setOptions(options));
        assertDoesNotThrow(() -> minstrelEffectAction.act());

        assertTrue(checkStudentIdInEntrance(1, student3));
        assertTrue(checkStudentIdInDiningRoom(1, studentEntrance1));
        assertTrue(checkStudentIdInEntrance(1, student4));
        assertTrue(checkStudentIdInDiningRoom(1, studentEntrance2));

        //The entrance student requested isn't in the entrance

        options.clear();
        int std1 = assertDoesNotThrow(() -> CommonManager.takePlayerById(gameEngine, 1).getSchoolBoard().getEntrance().get(4).getId());
        int std2 = assertDoesNotThrow(() -> CommonManager.takePlayerById(gameEngine, 1).getSchoolBoard().getEntrance().get(1).getId());
        assertDoesNotThrow(() -> gameEngine.getSchoolPawnManager().moveStudentFromEntranceToDiningRoom(1, std1));
        assertDoesNotThrow(() -> gameEngine.getSchoolPawnManager().moveStudentFromEntranceToDiningRoom(1, std2));
        options.put(ModelConstants.ACTION_MINSTREL_OPTIONS_KEY_STUDENT_IN_DINING_ROOM1, String.valueOf(std1));
        options.put(ModelConstants.ACTION_MINSTREL_OPTIONS_KEY_STUDENT_IN_ENTRANCE1, String.valueOf(std1));
        options.put(ModelConstants.ACTION_MINSTREL_OPTIONS_KEY_STUDENT_IN_DINING_ROOM1, String.valueOf(std2));
        options.put(ModelConstants.ACTION_MINSTREL_OPTIONS_KEY_STUDENT_IN_ENTRANCE1, String.valueOf(std1));
        minstrelEffectAction.setPlayerId(1);
        assertDoesNotThrow(() -> minstrelEffectAction.setOptions(options));
        assertThrows(IllegalGameActionException.class, () -> minstrelEffectAction.act());


        //The diningRoom student isn't in the diningRoom

        options.clear();
        int s1 = assertDoesNotThrow(() -> CommonManager.takePlayerById(gameEngine, 1).getSchoolBoard().getEntrance().get(0).getId());
        int s2 = assertDoesNotThrow(() -> CommonManager.takePlayerById(gameEngine, 1).getSchoolBoard().getEntrance().get(1).getId());
        options.put(ModelConstants.ACTION_MINSTREL_OPTIONS_KEY_STUDENT_IN_DINING_ROOM1, String.valueOf(s1));
        options.put(ModelConstants.ACTION_MINSTREL_OPTIONS_KEY_STUDENT_IN_DINING_ROOM2, String.valueOf(s1));
        options.put(ModelConstants.ACTION_MINSTREL_OPTIONS_KEY_STUDENT_IN_ENTRANCE1, String.valueOf(s1));
        options.put(ModelConstants.ACTION_MINSTREL_OPTIONS_KEY_STUDENT_IN_ENTRANCE2, String.valueOf(s2));
        minstrelEffectAction.setPlayerId(1);
        assertDoesNotThrow(() -> minstrelEffectAction.setOptions(options));
        assertThrows(IllegalGameActionException.class, () -> minstrelEffectAction.act());

        //The studentInDiningRoom1 is in the last position of the table

        options.clear();
        int studt1 = assertDoesNotThrow(() -> CommonManager.takePlayerById(gameEngine, 1).getSchoolBoard().getEntrance().get(0).getId());
        int studt2 = assertDoesNotThrow(() -> CommonManager.takePlayerById(gameEngine, 1).getSchoolBoard().getEntrance().get(1).getId());
        assertDoesNotThrow(() -> gameEngine.getSchoolPawnManager().moveStudentFromEntranceToDiningRoom(1, studt2));
        assertDoesNotThrow(() -> gameEngine.getSchoolPawnManager().moveStudentFromEntranceToDiningRoom(1, studt1));
        int studt3 = assertDoesNotThrow(() -> CommonManager.takePlayerById(gameEngine, 1).getSchoolBoard().getEntrance().get(0).getId());

        // Add a student in the entrance to test the multi movement
        assertDoesNotThrow(()->gameEngine.getSchoolPawnManager().moveStudentsFromBagToCloud(4,1));
        assertDoesNotThrow(()->gameEngine.getSchoolPawnManager().moveStudentsFromCloudTileToEntrance(1, 1));

        int studt4 = assertDoesNotThrow(() -> CommonManager.takePlayerById(gameEngine, 1).getSchoolBoard().getEntrance().get(1).getId());
        options.put(ModelConstants.ACTION_MINSTREL_OPTIONS_KEY_STUDENT_IN_DINING_ROOM2, String.valueOf(studt2));
        options.put(ModelConstants.ACTION_MINSTREL_OPTIONS_KEY_STUDENT_IN_DINING_ROOM1, String.valueOf(studt1));
        options.put(ModelConstants.ACTION_MINSTREL_OPTIONS_KEY_STUDENT_IN_ENTRANCE1, String.valueOf(studt3));
        options.put(ModelConstants.ACTION_MINSTREL_OPTIONS_KEY_STUDENT_IN_ENTRANCE2, String.valueOf(studt4));
        minstrelEffectAction.setPlayerId(1);
        assertDoesNotThrow(() -> minstrelEffectAction.setOptions(options));
        assertDoesNotThrow(() -> minstrelEffectAction.act());

        //The two diningRoom students aren't in the last position

        options.clear();
        int st1 = assertDoesNotThrow(() -> CommonManager.takePlayerById(gameEngine, 1).getSchoolBoard().getEntrance().get(0).getId());
        int st2 = assertDoesNotThrow(() -> CommonManager.takePlayerById(gameEngine, 1).getSchoolBoard().getEntrance().get(1).getId());
        int st3 = assertDoesNotThrow(() -> CommonManager.takePlayerById(gameEngine, 1).getSchoolBoard().getEntrance().get(2).getId());
        int st4 = assertDoesNotThrow(() -> CommonManager.takePlayerById(gameEngine, 1).getSchoolBoard().getEntrance().get(3).getId());
        StudentDisc studentDisc = new StudentDisc(1000, assertDoesNotThrow(() -> CommonManager.takePlayerById(gameEngine, 1).getSchoolBoard().getEntrance().get(2).getColor()));
        StudentDisc studentDisc1 = new StudentDisc(1001, assertDoesNotThrow(() -> CommonManager.takePlayerById(gameEngine, 1).getSchoolBoard().getEntrance().get(3).getColor()));

        assertDoesNotThrow(() -> gameEngine.getSchoolPawnManager().moveStudentFromEntranceToDiningRoom(1, st3));
        assertDoesNotThrow(() -> gameEngine.getSchoolPawnManager().moveStudentFromEntranceToDiningRoom(1, st4));
        assertDoesNotThrow(() -> CommonManager.takePlayerById(gameEngine, 1).getSchoolBoard().addStudentToDiningRoom(studentDisc));
        assertDoesNotThrow(() -> CommonManager.takePlayerById(gameEngine, 1).getSchoolBoard().addStudentToDiningRoom(studentDisc1));
        options.put(ModelConstants.ACTION_MINSTREL_OPTIONS_KEY_STUDENT_IN_DINING_ROOM1, String.valueOf(st3));
        options.put(ModelConstants.ACTION_MINSTREL_OPTIONS_KEY_STUDENT_IN_ENTRANCE1, String.valueOf(st1));
        options.put(ModelConstants.ACTION_MINSTREL_OPTIONS_KEY_STUDENT_IN_DINING_ROOM2, String.valueOf(st4));
        options.put(ModelConstants.ACTION_MINSTREL_OPTIONS_KEY_STUDENT_IN_ENTRANCE2, String.valueOf(st2));
        minstrelEffectAction.setPlayerId(1);
        assertDoesNotThrow(() -> minstrelEffectAction.setOptions(options));
        assertThrows(IllegalStudentDiscMovementException.class, () -> minstrelEffectAction.act());


        //I have only a studentInEntrance and a studentInDiningRoom, and I have no problems
        options.clear();
        int stud1 = assertDoesNotThrow(() -> CommonManager.takePlayerById(gameEngine, 1).getSchoolBoard().getEntrance().get(0).getId());
        assertDoesNotThrow(() -> gameEngine.getSchoolPawnManager().moveStudentFromEntranceToDiningRoom(1, stud1));
        int stud2 = assertDoesNotThrow(() -> CommonManager.takePlayerById(gameEngine, 1).getSchoolBoard().getEntrance().get(0).getId());
        options.put(ModelConstants.ACTION_MINSTREL_OPTIONS_KEY_STUDENT_IN_DINING_ROOM1, String.valueOf(stud1));
        options.put(ModelConstants.ACTION_MINSTREL_OPTIONS_KEY_STUDENT_IN_ENTRANCE1, String.valueOf(stud2));

        minstrelEffectAction.setPlayerId(1);
        assertDoesNotThrow(() -> minstrelEffectAction.setOptions(options));
        assertDoesNotThrow(() -> minstrelEffectAction.act());

    }

    private boolean checkStudentIdInDiningRoom(int playerId, int studentId) {
        for (ArrayList<StudentDisc> diningTable : assertDoesNotThrow(() -> CommonManager.takePlayerById(gameEngine, playerId).getSchoolBoard().getDiningRoom())) {
            for (StudentDisc studentDisc : diningTable) {
                if (studentDisc.getId() == studentId)
                    return true;
            }
        }
        return false;
    }

    private boolean checkStudentIdInEntrance(int playerId, int studentId) {
        for (StudentDisc studentDisc : assertDoesNotThrow(() -> CommonManager.takePlayerById(gameEngine, playerId).getSchoolBoard().getEntrance())) {
            if (studentDisc.getId() == studentId)
                return true;
        }
        return false;
    }


}