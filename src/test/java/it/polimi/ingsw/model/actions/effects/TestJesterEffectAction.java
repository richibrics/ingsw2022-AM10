package it.polimi.ingsw.model.actions.effects;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.controller.User;
import it.polimi.ingsw.controller.exceptions.WrongMessageContentException;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.actions.SetUpThreePlayersAction;
import it.polimi.ingsw.model.exceptions.IllegalGameActionException;
import it.polimi.ingsw.model.game_components.Bag;
import it.polimi.ingsw.model.game_components.Character;
import it.polimi.ingsw.model.game_components.CharacterCard;
import it.polimi.ingsw.model.game_components.StudentDisc;
import it.polimi.ingsw.model.managers.CommonManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TestJesterEffectAction {
    static GameEngine gameEngine;
    static JesterEffectAction jesterEffectAction;

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
        SetUpThreePlayersAction setUpThreePlayersAction = new SetUpThreePlayersAction(gameEngine) {
            @Override
            protected void drawCharacters(Map<Integer, CharacterCard> characterCards, Bag bag) throws Exception {
                ArrayList<Character> characters = new ArrayList<>();
                characters.add(Character.JESTER);
                for (Character character : characters) {
                    characterCards.put(character.getId(), new CharacterCard(character));
                }
                for (CharacterCard characterCard : characterCards.values()) {
                    this.getGameEngine().getCharacterManager().generateAction(characterCard);
                    this.getGameEngine().getCharacterManager().setupCardStorage(characterCard, bag);
                }
            }
        };
        assertDoesNotThrow(() -> setUpThreePlayersAction.act());

        jesterEffectAction = new JesterEffectAction(gameEngine);
    }

    /**
     * Tests only exception throw or not.
     */
    @Test
    void setOptions() {
        HashMap<String, String> options = new HashMap<>();

        // No entrance students and card storage students

        assertThrows(WrongMessageContentException.class, () -> jesterEffectAction.setOptions(options));
        options.put(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_ENTRANCE1, "1");
        assertThrows(WrongMessageContentException.class, () -> jesterEffectAction.setOptions(options));
        options.put(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_STORAGE1, "1");
        options.put(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_ENTRANCE2, "5");
        assertThrows(WrongMessageContentException.class, () -> jesterEffectAction.setOptions(options));

        // No entrance students and card storage students

        options.clear();
        assertThrows(WrongMessageContentException.class, () -> jesterEffectAction.setOptions(options));
        options.put(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_ENTRANCE1, "1");
        assertThrows(WrongMessageContentException.class, () -> jesterEffectAction.setOptions(options));
        options.put(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_STORAGE1, "1");
        options.put(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_ENTRANCE2, "5");
        assertThrows(WrongMessageContentException.class, () -> jesterEffectAction.setOptions(options));
        options.put(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_STORAGE2, "2");
        options.put(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_ENTRANCE3, "3");
        assertThrows(WrongMessageContentException.class, () -> jesterEffectAction.setOptions(options));

        // Parse error

        options.clear();
        options.put(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_ENTRANCE1, "a");
        assertThrows(WrongMessageContentException.class, () -> jesterEffectAction.setOptions(options));
        options.put(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_ENTRANCE1, "1");
        options.put(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_STORAGE1, "a");
        assertThrows(WrongMessageContentException.class, () -> jesterEffectAction.setOptions(options));
        options.put(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_STORAGE1, "1");
        options.put(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_ENTRANCE2, "b");
        assertThrows(WrongMessageContentException.class, () -> jesterEffectAction.setOptions(options));
        options.put(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_ENTRANCE2, "2");
        options.put(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_STORAGE2, "b");
        assertThrows(WrongMessageContentException.class, () -> jesterEffectAction.setOptions(options));


        // Parse error

        options.clear();
        options.put(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_ENTRANCE1, "a");
        assertThrows(WrongMessageContentException.class, () -> jesterEffectAction.setOptions(options));
        options.put(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_ENTRANCE1, "1");
        options.put(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_STORAGE1, "a");
        assertThrows(WrongMessageContentException.class, () -> jesterEffectAction.setOptions(options));
        options.put(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_STORAGE1, "1");
        options.put(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_ENTRANCE2, "b");
        assertThrows(WrongMessageContentException.class, () -> jesterEffectAction.setOptions(options));
        options.put(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_ENTRANCE2, "2");
        options.put(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_STORAGE2, "b");
        assertThrows(WrongMessageContentException.class, () -> jesterEffectAction.setOptions(options));
        options.put(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_STORAGE2, "2");
        options.put(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_ENTRANCE3, "c");
        assertThrows(WrongMessageContentException.class, () -> jesterEffectAction.setOptions(options));
        options.put(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_ENTRANCE3, "3");
        options.put(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_STORAGE3, "c");
        assertThrows(WrongMessageContentException.class, () -> jesterEffectAction.setOptions(options));


        //Ok

        options.clear();
        options.put(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_ENTRANCE1, "1");
        options.put(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_STORAGE1, "1");
        options.put(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_ENTRANCE2, "4");
        options.put(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_STORAGE2, "3");
        options.put(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_ENTRANCE3, "9");
        options.put(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_STORAGE3, "4");
        assertDoesNotThrow(() -> jesterEffectAction.setOptions(options));

        //Ok

        options.clear();
        options.put(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_ENTRANCE1, "1");
        options.put(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_STORAGE1, "1");
        options.put(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_ENTRANCE2, "4");
        options.put(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_STORAGE2, "3");
        assertDoesNotThrow(() -> jesterEffectAction.setOptions(options));


        //Ok

        options.clear();
        options.put(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_ENTRANCE1, "1");
        options.put(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_STORAGE1, "1");
        assertDoesNotThrow(() -> jesterEffectAction.setOptions(options));

    }

    /**
     * Tests property set to the correct entrance and card storage students.
     */
    @Test
    void act() {
        HashMap<String, String> options = new HashMap<>();

        int entranceStudent1;
        entranceStudent1 = assertDoesNotThrow(() -> CommonManager.takePlayerById(gameEngine, 1).getSchoolBoard().getEntrance().get(0)).getId();
        options.put(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_ENTRANCE1, String.valueOf(entranceStudent1));
        int storageStudent1;
        storageStudent1 = assertDoesNotThrow(() -> gameEngine.getTable().getCharacterCards().get(Character.JESTER.getId()).getStudentsStorage().get(1).getId());
        options.put(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_STORAGE1, String.valueOf(storageStudent1));
        int entranceStudent2;
        entranceStudent2 = assertDoesNotThrow(() -> CommonManager.takePlayerById(gameEngine, 1).getSchoolBoard().getEntrance().get(1)).getId();
        options.put(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_ENTRANCE2, String.valueOf(entranceStudent2));
        int storageStudent2;
        storageStudent2 = assertDoesNotThrow(() -> gameEngine.getTable().getCharacterCards().get(Character.JESTER.getId()).getStudentsStorage().get(2).getId());
        options.put(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_STORAGE2, String.valueOf(storageStudent2));
        int entranceStudent3;
        entranceStudent3 = assertDoesNotThrow(() -> CommonManager.takePlayerById(gameEngine, 1).getSchoolBoard().getEntrance().get(2)).getId();
        options.put(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_ENTRANCE3, String.valueOf(entranceStudent3));
        int storageStudent3;
        storageStudent3 = assertDoesNotThrow(() -> gameEngine.getTable().getCharacterCards().get(Character.JESTER.getId()).getStudentsStorage().get(3).getId());
        options.put(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_STORAGE3, String.valueOf(storageStudent3));
        jesterEffectAction.setPlayerId(1);
        assertDoesNotThrow(() -> jesterEffectAction.setOptions(options));
        assertDoesNotThrow(() -> jesterEffectAction.act());

        assertTrue(checkStudentIdInStorage(entranceStudent1));
        assertTrue(checkStudentIdInEntrance(1, storageStudent1));
        assertTrue(checkStudentIdInStorage(entranceStudent2));
        assertTrue(checkStudentIdInEntrance(1, storageStudent2));
        assertTrue(checkStudentIdInStorage(entranceStudent3));
        assertTrue(checkStudentIdInEntrance(1, storageStudent3));

        //An entranceStudent requested isn't in player's entrance
        options.clear();
        options.put(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_ENTRANCE1, String.valueOf(storageStudent1));
        options.put(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_STORAGE1, String.valueOf(entranceStudent1));
        options.put(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_ENTRANCE2, String.valueOf(storageStudent2));
        options.put(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_STORAGE2, String.valueOf(entranceStudent2));
        options.put(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_ENTRANCE3, String.valueOf(entranceStudent3));
        options.put(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_STORAGE3, String.valueOf(entranceStudent3));
        jesterEffectAction.setPlayerId(1);
        assertDoesNotThrow(() -> jesterEffectAction.setOptions(options));
        assertThrows(IllegalGameActionException.class, () -> jesterEffectAction.act());


        //A storageStudent requested isn't in card storage

        options.clear();
        options.put(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_ENTRANCE1, String.valueOf(entranceStudent1));
        options.put(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_STORAGE1, String.valueOf(storageStudent1));
        options.put(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_ENTRANCE2, String.valueOf(entranceStudent2));
        options.put(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_STORAGE2, String.valueOf(storageStudent2));
        options.put(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_ENTRANCE3, String.valueOf(entranceStudent3));
        options.put(ModelConstants.ACTION_JESTER_OPTIONS_KEY_STUDENT_STORAGE3, String.valueOf(storageStudent3));
        jesterEffectAction.setPlayerId(1);
        assertDoesNotThrow(() -> jesterEffectAction.setOptions(options));
        assertThrows(IllegalGameActionException.class, () -> jesterEffectAction.act());
    }

    private boolean checkStudentIdInEntrance(int playerId, int studentId) {
        for (StudentDisc studentDisc : assertDoesNotThrow(() -> CommonManager.takePlayerById(gameEngine, playerId).getSchoolBoard().getEntrance())) {
            if (studentDisc.getId() == studentId)
                return true;
        }
        return false;
    }

    private boolean checkStudentIdInStorage(int studentId) {
        for (StudentDisc studentDisc : assertDoesNotThrow(() -> gameEngine.getTable().getCharacterCards().get(Character.JESTER.getId()).getStudentsStorage())) {
            if (studentDisc.getId() == studentId)
                return true;
        }
        return false;
    }
}