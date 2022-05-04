package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.controller.User;
import it.polimi.ingsw.controller.exceptions.IllegalGameStateException;
import it.polimi.ingsw.controller.exceptions.WrongMessageContentException;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.exceptions.IllegalGameActionException;
import it.polimi.ingsw.model.game_components.Bag;
import it.polimi.ingsw.model.game_components.Character;
import it.polimi.ingsw.model.game_components.CharacterCard;
import it.polimi.ingsw.model.managers.CommonManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TestOnSelectionOfCharacterCardAction {
    static OnSelectionOfCharacterCardAction onSelectionOfCharacterCardAction;
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

        // Special setup that sets the active cards to [Herbalist] for testing purposes
        SetUpThreePlayersAction setUpThreePlayersAction = new SetUpThreePlayersAction(gameEngine) {
            @Override
            protected void drawCharacters(Map<Integer, CharacterCard> characterCards, Bag bag) throws Exception {
                ArrayList<Character> characters = new ArrayList<>();
                characters.add(Character.HERBALIST);
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

        onSelectionOfCharacterCardAction = new OnSelectionOfCharacterCardAction(gameEngine);

        // Place OnSelectCharacterCard in round actions list
        ArrayList<Integer> nextActions = gameEngine.getRound().getPossibleActions();
        nextActions.add(ModelConstants.ACTION_ON_SELECTION_OF_CHARACTER_CARD_ID);
        gameEngine.getRound().setPossibleActions(nextActions);
    }

    /**
     * Checks that the character card id is correctly taken, checks also all throws
     */

    @Test
    void setOptions() {
        HashMap<String, String> options = new HashMap<>();

        // No id
        assertThrows(WrongMessageContentException.class, () -> onSelectionOfCharacterCardAction.setOptions(options));

        // Parse error
        options.put(ModelConstants.ACTION_ON_SELECTION_OF_CHARACTER_CARD_OPTIONS_KEY_CHARACTER, "a");
        assertThrows(WrongMessageContentException.class, () -> onSelectionOfCharacterCardAction.setOptions(options));

        // OK
        options.put(ModelConstants.ACTION_ON_SELECTION_OF_CHARACTER_CARD_OPTIONS_KEY_CHARACTER, String.valueOf(Character.HERBALIST.getId()));
        assertDoesNotThrow(() -> onSelectionOfCharacterCardAction.setOptions(options));
    }

    /**
     * Tests the action card is run (herbalist) or that the correct exceptions are thrown
     */
    @Test
    void act() {
        /**
         * Prepare a card request, for an herbalist, so add to options the herbalist card id and the options for the herbalist action
         */
        HashMap<String, String> options = new HashMap<>();
        options.put(ModelConstants.ACTION_ON_SELECTION_OF_CHARACTER_CARD_OPTIONS_KEY_CHARACTER, String.valueOf(Character.HERBALIST.getId()));
        options.put(ModelConstants.ACTION_HERBALIST_OPTIONS_KEY_ISLAND, "1"); // Option for the character card action
        onSelectionOfCharacterCardAction.setPlayerId(1);
        assertDoesNotThrow(() -> onSelectionOfCharacterCardAction.setOptions(options));
        assertFalse(assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, 1)).hasNoEntry()); // Check before
        assertDoesNotThrow(() -> onSelectionOfCharacterCardAction.act());
        assertTrue(assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, 1)).hasNoEntry()); // Check after

        // Now player 1 asks to play again that card: no money !
        options.clear();
        options.put(ModelConstants.ACTION_ON_SELECTION_OF_CHARACTER_CARD_OPTIONS_KEY_CHARACTER, String.valueOf(Character.HERBALIST.getId()));
        options.put(ModelConstants.ACTION_HERBALIST_OPTIONS_KEY_ISLAND, "2"); // Option for the character card action
        onSelectionOfCharacterCardAction.setPlayerId(1);
        assertDoesNotThrow(() -> onSelectionOfCharacterCardAction.setOptions(options));
        assertThrows(IllegalGameActionException.class, () -> onSelectionOfCharacterCardAction.act());

        // Now a wrong thing to action (add another entry tile to the same island) to the card action and get the exception
        options.clear();
        options.put(ModelConstants.ACTION_ON_SELECTION_OF_CHARACTER_CARD_OPTIONS_KEY_CHARACTER, String.valueOf(Character.HERBALIST.getId()));
        options.put(ModelConstants.ACTION_HERBALIST_OPTIONS_KEY_ISLAND, "1"); // Option for the character card action
        onSelectionOfCharacterCardAction.setPlayerId(2);
        assertDoesNotThrow(() -> onSelectionOfCharacterCardAction.setOptions(options));
        assertThrows(IllegalGameActionException.class, () -> onSelectionOfCharacterCardAction.act());

        // Now ask for wrong action
        options.clear();
        options.put(ModelConstants.ACTION_ON_SELECTION_OF_CHARACTER_CARD_OPTIONS_KEY_CHARACTER, "-2");
        assertDoesNotThrow(() -> onSelectionOfCharacterCardAction.setOptions(options));
        assertThrows(IllegalGameActionException.class, () -> onSelectionOfCharacterCardAction.act());
        // Now remove the table and get IllegalState
        gameEngine.setTable(null);
        assertThrows(IllegalGameStateException.class, () -> onSelectionOfCharacterCardAction.act());
    }

    /**
     * Checks that this action is removed from round once used.
     * Checks exception thrown if this action was run without being in round actions list.
     */
    @Test
    void modifyRoundAndActionList() {
        assertTrue(gameEngine.getRound().getPossibleActions().contains(ModelConstants.ACTION_ON_SELECTION_OF_CHARACTER_CARD_ID));
        assertDoesNotThrow(() -> onSelectionOfCharacterCardAction.modifyRoundAndActionList());
        assertFalse(gameEngine.getRound().getPossibleActions().contains(ModelConstants.ACTION_ON_SELECTION_OF_CHARACTER_CARD_ID));

        // Now run again, this action is not in round anymore -> exception
        assertThrows(IllegalGameStateException.class, () -> onSelectionOfCharacterCardAction.modifyRoundAndActionList());
    }
}