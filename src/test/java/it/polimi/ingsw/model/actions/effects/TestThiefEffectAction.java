package it.polimi.ingsw.model.actions.effects;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.controller.User;
import it.polimi.ingsw.controller.exceptions.WrongMessageContentException;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.actions.SetUpThreePlayersAction;
import it.polimi.ingsw.model.game_components.PawnColor;
import it.polimi.ingsw.model.game_components.StudentDisc;
import it.polimi.ingsw.model.managers.CommonManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class TestThiefEffectAction {
    static ThiefEffectAction thiefEffectAction;
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
        SetUpThreePlayersAction setUpThreePlayersAction = new SetUpThreePlayersAction(gameEngine);
        assertDoesNotThrow(() -> setUpThreePlayersAction.act());

        thiefEffectAction = new ThiefEffectAction(gameEngine);
    }

    /**
     * Tests only exception throw or not.
     */
    @Test
    void setOptions() {
        HashMap<String, String> options = new HashMap<>();

        // No color
        assertThrows(WrongMessageContentException.class, () -> thiefEffectAction.setOptions(options));

        // Parse error
        options.clear();
        options.put(ModelConstants.ACTION_THIEF_OPTIONS_KEY_COLOR, "a");
        assertThrows(WrongMessageContentException.class, () -> thiefEffectAction.setOptions(options));

        //Ok
        options.clear();
        options.put(ModelConstants.ACTION_THIEF_OPTIONS_KEY_COLOR, "red");
        assertDoesNotThrow(() -> thiefEffectAction.setOptions(options));

    }

    /**
     * Tests property set to the correct color.
     */
    @Test
    void act() {
        int students1;
        int students2;
        int students3;
        HashMap<String, String> options = new HashMap<>();
        options.put(ModelConstants.ACTION_THIEF_OPTIONS_KEY_COLOR, "red");
        assertDoesNotThrow(() -> thiefEffectAction.setOptions(options));
        for (int i = 126; i < 129; i++) {
            int finalI = i;
            assertDoesNotThrow(() -> gameEngine.getTeams().get(0).getPlayers().get(0).getSchoolBoard().addStudentToDiningRoom(new StudentDisc(finalI, PawnColor.RED)));
        }

        for (int i = 129; i < 132; i++) {
            int finalI = i;
            assertDoesNotThrow(() -> gameEngine.getTeams().get(1).getPlayers().get(0).getSchoolBoard().addStudentToDiningRoom(new StudentDisc(finalI, PawnColor.RED)));
        }

        for (int i = 133; i < 136; i++) {
            int finalI = i;
            assertDoesNotThrow(() -> gameEngine.getTeams().get(2).getPlayers().get(0).getSchoolBoard().addStudentToDiningRoom(new StudentDisc(finalI, PawnColor.RED)));
        }

        students1 = assertDoesNotThrow(() -> CommonManager.takeSchoolBoardByPlayerId(gameEngine, 1).getDiningRoomColor(PawnColor.RED).size());
        students2 = assertDoesNotThrow(() -> CommonManager.takeSchoolBoardByPlayerId(gameEngine, 2).getDiningRoomColor(PawnColor.RED).size());
        students3 = assertDoesNotThrow(() -> CommonManager.takeSchoolBoardByPlayerId(gameEngine, 3).getDiningRoomColor(PawnColor.RED).size());
        assertDoesNotThrow(() -> thiefEffectAction.act());

        assertEquals(students1 - 3, assertDoesNotThrow(() -> CommonManager.takeSchoolBoardByPlayerId(gameEngine, 1).getDiningRoomColor(PawnColor.RED).size()));
        assertEquals(students2 - 3, assertDoesNotThrow(() -> CommonManager.takeSchoolBoardByPlayerId(gameEngine, 2).getDiningRoomColor(PawnColor.RED).size()));
        assertEquals(students3 - 3, assertDoesNotThrow(() -> CommonManager.takeSchoolBoardByPlayerId(gameEngine, 3).getDiningRoomColor(PawnColor.RED).size()));
    }
}