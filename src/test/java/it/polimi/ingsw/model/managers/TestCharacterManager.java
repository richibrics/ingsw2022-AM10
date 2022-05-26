package it.polimi.ingsw.model.managers;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.controller.User;
import it.polimi.ingsw.controller.exceptions.WrongMessageContentException;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.actions.CalculateInfluenceAction;
import it.polimi.ingsw.model.actions.SetUpThreePlayersAction;
import it.polimi.ingsw.model.actions.effects.*;
import it.polimi.ingsw.model.exceptions.ActionNotSetException;
import it.polimi.ingsw.model.exceptions.IllegalGameActionException;
import it.polimi.ingsw.model.exceptions.NotEnoughCoinException;
import it.polimi.ingsw.model.game_components.Character;
import it.polimi.ingsw.model.game_components.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class TestCharacterManager {

    /**
     * Tests that 3 different Character are returned.
     */
    @Test
    void pickCharacters() {
        CharacterManager characterManager = new CharacterManager(new GameEngine(new ArrayList<>()));
        ArrayList<Character> characters = assertDoesNotThrow(()->characterManager.pickCharacters(3));
        assertEquals(3, characters.size());
        assertNotEquals(characters.get(0), characters.get(1));
        assertNotEquals(characters.get(0), characters.get(2));
        assertNotEquals(characters.get(1), characters.get(2));
    }

    /**
     * Tests that the correct number of Students are added to a CharacterCard.
     * Also tests the refill of students.
     */
    @ParameterizedTest
    @EnumSource(value = Character.class)
    void setupCardStorage(Character character) {
        CharacterManager characterManager = new CharacterManager(new GameEngine(new ArrayList<>()));
        Bag bag = new Bag();
        ArrayList<StudentDisc> studentDiscs = new ArrayList<>();
        studentDiscs.add(new StudentDisc(1, PawnColor.BLUE));
        studentDiscs.add(new StudentDisc(2, PawnColor.BLUE));
        studentDiscs.add(new StudentDisc(3, PawnColor.BLUE));
        studentDiscs.add(new StudentDisc(4, PawnColor.BLUE));
        studentDiscs.add(new StudentDisc(5, PawnColor.BLUE));
        studentDiscs.add(new StudentDisc(6, PawnColor.BLUE));
        studentDiscs.add(new StudentDisc(7, PawnColor.BLUE));
        studentDiscs.add(new StudentDisc(8, PawnColor.BLUE));
        studentDiscs.add(new StudentDisc(9, PawnColor.BLUE));
        studentDiscs.add(new StudentDisc(10, PawnColor.BLUE));
        bag.pushStudents(studentDiscs);

        CharacterCard characterCard = new CharacterCard(character);
        assertDoesNotThrow(() -> characterManager.setupCardStorage(characterCard, bag));
        assertEquals(characterCard.getStorageCapacity(), characterCard.getStudentsStorage().size());

        // Check exceptions and refill: removes 1 student and refill the storage
        if (characterCard.getStorageCapacity() != 0) {
            assertDoesNotThrow(() -> characterCard.removeStudentFromStorage(characterCard.getStudentsStorage().get(0).getId())); // Remove one
            assertEquals(characterCard.getStorageCapacity() - 1, characterCard.getStudentsStorage().size()); // Removed ok
            assertDoesNotThrow(() -> characterManager.setupCardStorage(characterCard, bag));
            assertEquals(characterCard.getStorageCapacity(), characterCard.getStudentsStorage().size()); // Refill ok
        }
    }


    /**
     * Tests that the correct Action is set for the requested CharacterCard.
     */
    @Test
    void generateAction() {
        CharacterManager characterManager = new CharacterManager(new GameEngine(new ArrayList<>()));

        CharacterCard characterCard1 = new CharacterCard(Character.FRIAR);
        characterManager.generateAction(characterCard1);
        assertEquals(assertDoesNotThrow(() -> characterCard1.getAction().getClass()), FriarEffectAction.class);

        CharacterCard characterCard2 = new CharacterCard(Character.COOK);
        characterManager.generateAction(characterCard2);
        assertEquals(assertDoesNotThrow(() -> characterCard2.getAction().getClass()), AssignProfessorActionCookEffect.class);

        CharacterCard characterCard3 = new CharacterCard(Character.AMBASSADOR);
        characterManager.generateAction(characterCard3);
        assertEquals(assertDoesNotThrow(() -> characterCard3.getAction().getClass()), AmbassadorEffectAction.class);

        CharacterCard characterCard4 = new CharacterCard(Character.MAILMAN);
        characterManager.generateAction(characterCard4);
        assertEquals(assertDoesNotThrow(() -> characterCard4.getAction().getClass()), MailmanEffectAction.class);

        CharacterCard characterCard5 = new CharacterCard(Character.HERBALIST);
        characterManager.generateAction(characterCard5);
        assertEquals(assertDoesNotThrow(() -> characterCard5.getAction().getClass()), HerbalistEffectAction.class);

        CharacterCard characterCard6 = new CharacterCard(Character.CENTAUR);
        characterManager.generateAction(characterCard6);
        assertEquals(assertDoesNotThrow(() -> characterCard6.getAction().getClass()), CalculateInfluenceActionCentaurEffect.class);

        CharacterCard characterCard7 = new CharacterCard(Character.THIEF);
        characterManager.generateAction(characterCard7);
        assertEquals(assertDoesNotThrow(() -> characterCard7.getAction().getClass()), ThiefEffectAction.class);

        CharacterCard characterCard8 = new CharacterCard(Character.JESTER);
        characterManager.generateAction(characterCard8);
        assertEquals(assertDoesNotThrow(() -> characterCard8.getAction().getClass()), JesterEffectAction.class);

        CharacterCard characterCard9 = new CharacterCard(Character.MUSHROOM_HUNTER);
        characterManager.generateAction(characterCard9);
        assertEquals(assertDoesNotThrow(() -> characterCard9.getAction().getClass()), CalculateInfluenceActionMushroomHunterEffect.class);

        CharacterCard characterCard10 = new CharacterCard(Character.MINSTREL);
        characterManager.generateAction(characterCard10);
        assertEquals(assertDoesNotThrow(() -> characterCard10.getAction().getClass()), MinstrelEffectAction.class);

        CharacterCard characterCard11 = new CharacterCard(Character.LADY);
        characterManager.generateAction(characterCard11);
        assertEquals(assertDoesNotThrow(() -> characterCard11.getAction().getClass()), LadyEffectAction.class);

        CharacterCard characterCard12 = new CharacterCard(Character.KNIGHT);
        characterManager.generateAction(characterCard12);
        assertEquals(assertDoesNotThrow(() -> characterCard12.getAction().getClass()), CalculateInfluenceActionKnightEffect.class);
    }


    /**
     * Tests that the CharacterCard action is run or is inserted in the ActionManager actions map.
     * The first test is performed with Herbalist (directly run)
     */
    @Test
    void selectCharacterCard() {
        GameEngine gameEngine = setupGameForSelectCharacterCard();
        // Test the herbalist, which is an active card in this match
        HashMap<String, String> options = new HashMap<>();
        options.put(ModelConstants.ACTION_HERBALIST_OPTIONS_KEY_ISLAND, "1");
        // check island state before run
        assertFalse(assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, 1).hasNoEntry()));
        assertDoesNotThrow(() -> gameEngine.getCharacterManager().selectCharacterCard(Character.HERBALIST.getId(), 1, options));
        // check action runs okay
        assertTrue(assertDoesNotThrow(() -> CommonManager.takeIslandTileById(gameEngine, 1).hasNoEntry()));

        // check throw from action with wrong options
        options.put(ModelConstants.ACTION_HERBALIST_OPTIONS_KEY_ISLAND, "a");
        assertThrows(WrongMessageContentException.class, () -> gameEngine.getCharacterManager().selectCharacterCard(Character.HERBALIST.getId(), 1, options));

        // check throw caused by non-existing card
        assertThrows(NoSuchElementException.class, () -> gameEngine.getCharacterManager().selectCharacterCard(-5, 1, options));

        // check throw caused by other generic exceptions: pass null as options
        assertThrows(RuntimeException.class, () -> gameEngine.getCharacterManager().selectCharacterCard(Character.HERBALIST.getId(), 1, null));

        // check throw for errors in act/modifyRoundAndActionList
        options.put(ModelConstants.ACTION_HERBALIST_OPTIONS_KEY_ISLAND, "1");

        // IllegalGameActionException when recalling the set no entry tile on the same island group
        assertThrows(IllegalGameActionException.class, () -> gameEngine.getCharacterManager().selectCharacterCard(Character.HERBALIST.getId(), 1, options));

        // Now I test with the second card I have: Knight, which is the effect for CalculateInfluence,
        // so I check if it's set in the correct position (test before and after)
        assertEquals(CalculateInfluenceAction.class, gameEngine.getActionManager().getActions()[ModelConstants.ACTION_CALCULATE_INFLUENCE_ID].getClass());
        assertDoesNotThrow(() -> gameEngine.getCharacterManager().selectCharacterCard(Character.KNIGHT.getId(), 1, options));
        assertEquals(CalculateInfluenceActionKnightEffect.class, gameEngine.getActionManager().getActions()[ModelConstants.ACTION_CALCULATE_INFLUENCE_ID].getClass());
    }

    private GameEngine setupGameForSelectCharacterCard() {
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
        GameEngine gameEngine = new GameEngine(teams);
        assertDoesNotThrow(() -> gameEngine.getActionManager().generateActions());

        // Run the setup with a customized method to test the cards I want
        SetUpThreePlayersAction setUpThreePlayersAction = new SetUpThreePlayersAction(gameEngine) {
            @Override
            protected void drawCharacters(Map<Integer, CharacterCard> characterCards, Bag bag) throws Exception {
                ArrayList<Character> characters = new ArrayList<>();
                characters.add(Character.HERBALIST);
                characters.add(Character.KNIGHT);
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

        return gameEngine;
    }

    @Test
    public void decreasePlayersMoneyEditCardCost() {
        GameEngine gameEngine = setupPlayerMoneyChecks();
        ArrayList<Integer> cardsId = (ArrayList<Integer>) assertDoesNotThrow(()->gameEngine.getTable().getCharacterCards().keySet().stream().collect(Collectors.toList()));

        // Player 1 has 3 coins so can play all the cards
        int cardCost = assertDoesNotThrow(()->gameEngine.getTable().getCharacterCards().get(cardsId.get(0)).getCost());
        assertEquals(3, assertDoesNotThrow(()->CommonManager.takePlayerById(gameEngine, 1).getCoins()));
        assertDoesNotThrow(()->gameEngine.getCharacterManager().decreasePlayersMoneyEditCardCost(1, cardsId.get(0)));
        assertEquals(3-cardCost, assertDoesNotThrow(()->CommonManager.takePlayerById(gameEngine, 1).getCoins()));

        // Now player 3 that has 4 coins uses again the card, and I check the cost of the card is greater
        cardCost = cardCost + 1;
        assertEquals(4, assertDoesNotThrow(()->CommonManager.takePlayerById(gameEngine, 3).getCoins()));
        assertDoesNotThrow(()->gameEngine.getCharacterManager().decreasePlayersMoneyEditCardCost(3, cardsId.get(0)));
        assertEquals(4-cardCost, assertDoesNotThrow(()->CommonManager.takePlayerById(gameEngine, 3).getCoins()));

        // Now player 2 without money asks for the card: exception
        assertThrows(NotEnoughCoinException.class, ()->gameEngine.getCharacterManager().decreasePlayersMoneyEditCardCost(2, cardsId.get(0)));

        // Check exceptions: no player, no card
        assertThrows(NoSuchElementException.class, ()->gameEngine.getCharacterManager().decreasePlayersMoneyEditCardCost(5, cardsId.get(0)));
        assertThrows(NoSuchElementException.class, ()->gameEngine.getCharacterManager().decreasePlayersMoneyEditCardCost(1, -1));
    }

    /**
     * Tests checkPlayerCanPlayCard: player with money can play, player without money can't; exceptions check
     */
    @Test
    public void checkPlayerCanPlayCard() {
        GameEngine gameEngine = setupPlayerMoneyChecks();
        ArrayList<Integer> cardsId = (ArrayList<Integer>) assertDoesNotThrow(()->gameEngine.getTable().getCharacterCards().keySet().stream().collect(Collectors.toList()));

        // Player 1 has 3 coins so can play all the cards
        // Test true
        assertTrue(assertDoesNotThrow(()->gameEngine.getCharacterManager().checkPlayerCanPlayCard(1, cardsId.get(0))));

        // Player 2 has 0 coins so can't play any card
        // Test False
        assertFalse(assertDoesNotThrow(()->gameEngine.getCharacterManager().checkPlayerCanPlayCard(2, cardsId.get(0))));

        // Check exceptions: no player, no card
        assertThrows(NoSuchElementException.class, ()->gameEngine.getCharacterManager().checkPlayerCanPlayCard(5, cardsId.get(0)));
        assertThrows(NoSuchElementException.class, ()->gameEngine.getCharacterManager().checkPlayerCanPlayCard(1, -1));
    }

    GameEngine setupPlayerMoneyChecks() {
        User user1 = new User("1", 3);
        User user2 = new User("2", 3);
        User user3 = new User("3", 3);
        Player player1 = new Player(user1, 1, 3);
        Player player2 = new Player(user2, 2, 0);
        Player player3 = new Player(user3, 3, 4);
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
        GameEngine gameEngine = new GameEngine(teams);
        assertDoesNotThrow(() -> gameEngine.getActionManager().generateActions());

        SetUpThreePlayersAction setUpThreePlayersAction = new SetUpThreePlayersAction(gameEngine);
        assertDoesNotThrow(() -> setUpThreePlayersAction.act());
        return gameEngine;
    }

    /**
     * Tests the passed character card receives the action.
     */
    @Test
    public void prepareCharacterCardsActions() {
        ArrayList<CharacterCard> characterCards= new ArrayList<>();
        characterCards.add(new CharacterCard(Character.HERBALIST));

        // Check action is still null
        for (CharacterCard characterCard: characterCards)
            assertThrows(ActionNotSetException.class, characterCard::getAction);

        GameEngine gameEngine = setupGameForSelectCharacterCard();

        // Prepare the action
        gameEngine.getCharacterManager().prepareCharacterCardsActions(characterCards);

        // Check action set correctly
        for (CharacterCard characterCard: characterCards)
            assertDoesNotThrow(characterCard::getAction);
    }
}