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
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TestLadyEffectAction {
    static GameEngine gameEngine;
    static LadyEffectAction ladyEffectAction;

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
                characters.add(Character.LADY);
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

        ladyEffectAction = new LadyEffectAction(gameEngine);
    }

    /**
     * Tests only exception throw or not.
     */
    @Test
    void setOptions() {
        HashMap<String, String> options = new HashMap<>();

        // No student id
        assertThrows(WrongMessageContentException.class, () -> ladyEffectAction.setOptions(options));

        // Parse error
        options.clear();
        options.put(ModelConstants.ACTION_LADY_OPTIONS_KEY_STUDENT, "a");
        assertThrows(WrongMessageContentException.class, () -> ladyEffectAction.setOptions(options));


        //Ok
        options.clear();
        options.put(ModelConstants.ACTION_LADY_OPTIONS_KEY_STUDENT, "1");
        assertDoesNotThrow(() -> ladyEffectAction.setOptions(options));
        options.put(ModelConstants.ACTION_LADY_OPTIONS_KEY_STUDENT, "4");
        assertDoesNotThrow(() -> ladyEffectAction.setOptions(options));
        options.put(ModelConstants.ACTION_LADY_OPTIONS_KEY_STUDENT, "3");
        assertDoesNotThrow(() -> ladyEffectAction.setOptions(options));

    }

    /**
     * Tests property set to the correct student.
     */
    @RepeatedTest(100)
    void act() {
        HashMap<String, String> options = new HashMap<>();


        int storageStudent;

        storageStudent = assertDoesNotThrow(() -> gameEngine.getTable().getCharacterCards().get(Character.LADY.getId()).getStudentsStorage().get(1).getId());


        options.put(ModelConstants.ACTION_LADY_OPTIONS_KEY_STUDENT, String.valueOf(storageStudent));
        ladyEffectAction.setPlayerId(1);
        assertDoesNotThrow(() -> ladyEffectAction.setOptions(options));
        assertDoesNotThrow(() -> ladyEffectAction.act());

        assertTrue(checkStudentIdInDiningRoom(1, storageStudent));

        //The student requested isn't in the card storage

        options.clear();
        options.put(ModelConstants.ACTION_LADY_OPTIONS_KEY_STUDENT, String.valueOf(storageStudent));
        ladyEffectAction.setPlayerId(1);
        assertDoesNotThrow(() -> ladyEffectAction.setOptions(options));
        assertThrows(IllegalGameActionException.class, () -> ladyEffectAction.act());

        //Bag empty

        assertDoesNotThrow(() -> gameEngine.getTable().getBag().drawStudents(gameEngine.getTable().getBag().getNumberOfStudents()));

        options.clear();
        int storageStudent1;
        storageStudent1 = assertDoesNotThrow(() -> gameEngine.getTable().getCharacterCards().get(Character.LADY.getId()).getStudentsStorage().get(2).getId());

        options.put(ModelConstants.ACTION_LADY_OPTIONS_KEY_STUDENT, String.valueOf(storageStudent1));

        ladyEffectAction.setPlayerId(1);
        assertDoesNotThrow(() -> ladyEffectAction.setOptions(options));
        assertDoesNotThrow(() -> ladyEffectAction.act());

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
}