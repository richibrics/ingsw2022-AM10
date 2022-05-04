package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.controller.User;
import it.polimi.ingsw.controller.exceptions.IllegalGameStateException;
import it.polimi.ingsw.controller.exceptions.WrongMessageContentException;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.exceptions.IllegalGameActionException;
import it.polimi.ingsw.model.game_components.StudentDisc;
import it.polimi.ingsw.model.managers.CommonManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class TestMoveStudentsFromEntranceAction {
    static MoveStudentsFromEntranceAction moveStudentsFromEntranceAction;
    static GameEngine gameEngine;

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
        assertDoesNotThrow(() -> gameEngine.getActionManager().generateActions());
        SetUpThreePlayersAction setUpThreePlayersAction = new SetUpThreePlayersAction(gameEngine);
        assertDoesNotThrow(() -> setUpThreePlayersAction.act());

        moveStudentsFromEntranceAction = new MoveStudentsFromEntranceAction(gameEngine);

        ArrayList<Integer> order = new ArrayList<>();
        order.add(1);
        order.add(2);
        order.add(3);
        gameEngine.getRound().setOrderOfPlay(order);
    }

    /**
     * Tests options are parsed correctly and accepted only if valid.
     */
    @Test
    void setOptions() {
        HashMap<String, String> options = new HashMap<>();

        // No student id
        assertThrows(WrongMessageContentException.class, () -> moveStudentsFromEntranceAction.setOptions(options));

        // Student id parse error
        options.put(ModelConstants.ACTION_MOVE_STUDENTS_FROM_ENTRANCE_OPTIONS_KEY_STUDENT, "a");
        assertThrows(WrongMessageContentException.class, () -> moveStudentsFromEntranceAction.setOptions(options));

        // No student position
        options.put(ModelConstants.ACTION_MOVE_STUDENTS_FROM_ENTRANCE_OPTIONS_KEY_STUDENT, "12");
        assertThrows(WrongMessageContentException.class, () -> moveStudentsFromEntranceAction.setOptions(options));

        // Student position parse error
        options.put(ModelConstants.ACTION_MOVE_STUDENTS_FROM_ENTRANCE_OPTIONS_KEY_POSITION, "a");
        assertThrows(WrongMessageContentException.class, () -> moveStudentsFromEntranceAction.setOptions(options));

        // Student position not valid
        options.put(ModelConstants.ACTION_MOVE_STUDENTS_FROM_ENTRANCE_OPTIONS_KEY_POSITION, "-2");
        assertThrows(WrongMessageContentException.class, () -> moveStudentsFromEntranceAction.setOptions(options));

        // Student position not valid
        options.put(ModelConstants.ACTION_MOVE_STUDENTS_FROM_ENTRANCE_OPTIONS_KEY_POSITION, "0");
        assertThrows(WrongMessageContentException.class, () -> moveStudentsFromEntranceAction.setOptions(options));

        // Student position not valid
        options.put(ModelConstants.ACTION_MOVE_STUDENTS_FROM_ENTRANCE_OPTIONS_KEY_POSITION, "13");
        assertThrows(WrongMessageContentException.class, () -> moveStudentsFromEntranceAction.setOptions(options));

        // ok - on island tile 1
        options.put(ModelConstants.ACTION_MOVE_STUDENTS_FROM_ENTRANCE_OPTIONS_KEY_POSITION, "1");
        assertDoesNotThrow(() -> moveStudentsFromEntranceAction.setOptions(options));

        // ok - on island tile 12
        options.put(ModelConstants.ACTION_MOVE_STUDENTS_FROM_ENTRANCE_OPTIONS_KEY_POSITION, "12");
        assertDoesNotThrow(() -> moveStudentsFromEntranceAction.setOptions(options));

        // ok - in dining room
        options.put(ModelConstants.ACTION_MOVE_STUDENTS_FROM_ENTRANCE_OPTIONS_KEY_POSITION, String.valueOf(ModelConstants.ACTION_MOVE_STUDENTS_FROM_ENTRANCE_OPTIONS_KEY_POSITION_VALUE_DINING_ROOM));
        assertDoesNotThrow(() -> moveStudentsFromEntranceAction.setOptions(options));
    }

    @Test
    void act() {
        // Get a student of player 1 and move it to dining room
        int entranceStudent;

        entranceStudent = assertDoesNotThrow(() -> CommonManager.takePlayerById(gameEngine, 1).getSchoolBoard().getEntrance().get(0)).getId();
        HashMap<String, String> options = new HashMap<>();

        // Check pre-action state
        assertTrue(checkStudentIdInEntrance(1, entranceStudent));
        assertFalse(checkStudentIdInDiningRoom(1, entranceStudent));

        options.put(ModelConstants.ACTION_MOVE_STUDENTS_FROM_ENTRANCE_OPTIONS_KEY_STUDENT, String.valueOf(entranceStudent));
        options.put(ModelConstants.ACTION_MOVE_STUDENTS_FROM_ENTRANCE_OPTIONS_KEY_POSITION, String.valueOf(ModelConstants.ACTION_MOVE_STUDENTS_FROM_ENTRANCE_OPTIONS_KEY_POSITION_VALUE_DINING_ROOM));
        moveStudentsFromEntranceAction.setPlayerId(1);
        assertDoesNotThrow(() -> moveStudentsFromEntranceAction.setOptions(options));
        assertDoesNotThrow(() -> moveStudentsFromEntranceAction.act());

        // Check post-action state
        assertFalse(checkStudentIdInEntrance(1, entranceStudent));
        assertTrue(checkStudentIdInDiningRoom(1, entranceStudent));


        // Now move to an island tile: island 5
        entranceStudent = assertDoesNotThrow(() -> CommonManager.takePlayerById(gameEngine, 1).getSchoolBoard().getEntrance().get(0)).getId();

        // Check pre-action state
        assertTrue(checkStudentIdInEntrance(1, entranceStudent));
        assertFalse(checkStudentIdOnIslandId(5, entranceStudent));

        options.put(ModelConstants.ACTION_MOVE_STUDENTS_FROM_ENTRANCE_OPTIONS_KEY_STUDENT, String.valueOf(entranceStudent));
        options.put(ModelConstants.ACTION_MOVE_STUDENTS_FROM_ENTRANCE_OPTIONS_KEY_POSITION, "5");
        assertDoesNotThrow(() -> moveStudentsFromEntranceAction.setOptions(options));
        assertDoesNotThrow(() -> moveStudentsFromEntranceAction.act());

        // Check post-action state
        assertFalse(checkStudentIdInEntrance(1, entranceStudent));
        assertTrue(checkStudentIdOnIslandId(5, entranceStudent));


        // Now ensure it throws correctly
        // Repeat the move (student no longer in entrance): Exception
        assertThrows(IllegalGameActionException.class, () -> moveStudentsFromEntranceAction.act());

        // Now unlink the schoolboard and assert I have an IllegalGameStateException
        assertDoesNotThrow(() -> CommonManager.takePlayerById(gameEngine, 1)).setSchoolBoard(null);
        assertThrows(IllegalGameStateException.class, () -> moveStudentsFromEntranceAction.act());

        // Now relink the schoolboard but remove the table and assert I have an IllegalGameStateException
        assertDoesNotThrow(() -> CommonManager.takePlayerById(gameEngine, 1).setSchoolBoard(gameEngine.getTable().getSchoolBoards().get(0)));
        gameEngine.setTable(null);
        assertThrows(IllegalGameStateException.class, () -> moveStudentsFromEntranceAction.act());
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

    private boolean checkStudentIdOnIslandId(int islandId, int studentId) {
        for (StudentDisc studentDisc : assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, islandId).peekStudents())) {
            if (studentDisc.getId() == studentId)
                return true;
        }
        return false;
    }

    /**
     * Checks if at third movement, the action is removes from round actions list and not before.
     * Also checks that at third movement, the MoveProfessor action is run.
     * Tests also that if it's called again after 3 new movements, it throws an exception.
     */
    @Test
    void modifyRoundAndActionList() {
        ArrayList<Integer> nextActions = new ArrayList<>();
        nextActions.add(ModelConstants.ACTION_MOVE_STUDENTS_FROM_ENTRANCE_ID);
        gameEngine.getRound().setPossibleActions(nextActions);

        // Check that the action id is removed only after 3 calls to modifyRoundAndActionList
        assertDoesNotThrow(() -> moveStudentsFromEntranceAction.modifyRoundAndActionList());
        assertTrue(gameEngine.getRound().getPossibleActions().contains(ModelConstants.ACTION_MOVE_STUDENTS_FROM_ENTRANCE_ID));
        assertDoesNotThrow(() -> moveStudentsFromEntranceAction.modifyRoundAndActionList());
        assertTrue(gameEngine.getRound().getPossibleActions().contains(ModelConstants.ACTION_MOVE_STUDENTS_FROM_ENTRANCE_ID));
        assertDoesNotThrow(() -> moveStudentsFromEntranceAction.modifyRoundAndActionList());
        assertFalse(gameEngine.getRound().getPossibleActions().contains(ModelConstants.ACTION_MOVE_STUDENTS_FROM_ENTRANCE_ID));

        // Check next Assign professor started (will set to next actions the MoveMotherNature action)
        assertTrue(gameEngine.getRound().getPossibleActions().contains(ModelConstants.ACTION_MOVE_MOTHER_NATURE_ID));

        // Action not in round anymore, at third movement when I remove it there's an exception
        assertDoesNotThrow(() -> moveStudentsFromEntranceAction.modifyRoundAndActionList());
        assertDoesNotThrow(() -> moveStudentsFromEntranceAction.modifyRoundAndActionList());
        assertThrows(IllegalGameStateException.class, () -> moveStudentsFromEntranceAction.modifyRoundAndActionList());
    }
}