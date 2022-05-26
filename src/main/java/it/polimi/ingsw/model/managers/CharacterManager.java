package it.polimi.ingsw.model.managers;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.controller.exceptions.IllegalGameStateException;
import it.polimi.ingsw.controller.exceptions.WrongMessageContentException;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.actions.Action;
import it.polimi.ingsw.model.actions.effects.*;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.game_components.Bag;
import it.polimi.ingsw.model.game_components.Character;
import it.polimi.ingsw.model.game_components.CharacterCard;
import it.polimi.ingsw.model.game_components.StudentDisc;

import java.util.*;

/**
 * Contains methods to manage CharacterCard creation and use.
 * To create a CharacterCard randomly, use {@link CharacterManager#pickCharacters(int)} to get 3 random Character that can be used
 * to instantiate 3 CharacterCards.
 * Pass a CharacterCard to {@link CharacterManager#setupCardStorage(CharacterCard, Bag)} to insert in the CharacterCard the correct number of StudentDisc.
 * Then you can use the factory method {@link CharacterManager#generateAction(CharacterCard)} to set the correct
 * Action for the passed CharacterCard.
 * <p>
 * Normal use method {@link CharacterManager#selectCharacterCard(int, int, Map)} is called by the Action that is called
 * when a CharacterCard is selected.
 */
public class CharacterManager extends Manager {

    // Here I set the Card that activated the current CharacterCard action that is running. null if that is not running.
    private CharacterCard cardInUse;

    public CharacterManager(GameEngine gameEngine) {
        super(gameEngine);
        cardInUse = null;
    }

    /**
     * Random draws three different Character from the enumeration and returns them in an ArrayList.
     *
     * @param number number of characters to draw. Must be from 0 to 12
     * @return three different Character
     * @see Character
     */
    public ArrayList<Character> pickCharacters(int number) {
        ArrayList<Integer> idPool = new ArrayList<>();
        ArrayList<Character> cards = new ArrayList<>();
        for (int i = 1; i <= Character.values().length; i++) {
            idPool.add(i);
        }

        // Shuffle the pool
        Collections.shuffle(idPool);

        // Pick 3 elements
        for (int i = 0; i < number; i++) {
            cards.add(Character.values()[idPool.get(i) - 1]);
        }
        return cards;
    }

    /**
     * Adds a number of StudentDisc to the CharacterCard. The number is an information available in the CharacterCard
     * and is different for each Character.
     * If the CharacterCard already has Students, this method only fills the storage to match the exact number of
     * students that need to be inside.
     *
     * @param characterCard the CharacterCard that will receive the Students drawn from the Bag
     * @param bag           the bag from where the Students will be drawn
     * @throws EmptyBagException            if there aren't other Students in the Bag
     * @throws CharacterStudentsStorageFull if you're trying to add other students to a full storage
     */
    public void setupCardStorage(CharacterCard characterCard, Bag bag) throws EmptyBagException, CharacterStudentsStorageFull {
        // In this method I don't get the Bag through the Table because this method is called during setup when
        // Table is not ready yet.
        ArrayList<StudentDisc> drawnStudents = bag.drawStudents(characterCard.getStorageCapacity() - characterCard.getStudentsStorage().size());
        for (StudentDisc studentDisc : drawnStudents)
            characterCard.addStudentToStorage(studentDisc);
    }

    /**
     * Creates an Effect Action that matches the CharacterCard passed and sets it in the Card.
     * This needs the Action to be generated before calling this method.
     *
     * @param characterCard the CharacterCard that receives the Action
     * @throws NoSuchElementException if {@code characterCard} is unknown and the method doesn't know which Action is needed
     */
    public void generateAction(CharacterCard characterCard) throws NoSuchElementException {
        // The decorators also need the original action, so I grab it from the action manager
        if (characterCard.getId() == Character.FRIAR.getId())
            characterCard.setAction(new FriarEffectAction(this.getGameEngine()));
        else if (characterCard.getId() == Character.COOK.getId())
            characterCard.setAction(new AssignProfessorActionCookEffect(this.getGameEngine(), this.getGameEngine().getActionManager().getActions()[ModelConstants.ACTION_ASSIGN_PROFESSORS_ID]));
        else if (characterCard.getId() == Character.AMBASSADOR.getId())
            characterCard.setAction(new AmbassadorEffectAction(this.getGameEngine()));
        else if (characterCard.getId() == Character.MAILMAN.getId())
            characterCard.setAction(new MailmanEffectAction(this.getGameEngine()));
        else if (characterCard.getId() == Character.HERBALIST.getId())
            characterCard.setAction(new HerbalistEffectAction(this.getGameEngine()));
        else if (characterCard.getId() == Character.CENTAUR.getId())
            characterCard.setAction(new CalculateInfluenceActionCentaurEffect(this.getGameEngine(), this.getGameEngine().getActionManager().getActions()[ModelConstants.ACTION_CALCULATE_INFLUENCE_ID]));
        else if (characterCard.getId() == Character.THIEF.getId())
            characterCard.setAction(new ThiefEffectAction(this.getGameEngine()));
        else if (characterCard.getId() == Character.JESTER.getId())
            characterCard.setAction(new JesterEffectAction(this.getGameEngine()));
        else if (characterCard.getId() == Character.MUSHROOM_HUNTER.getId())
            characterCard.setAction(new CalculateInfluenceActionMushroomHunterEffect(this.getGameEngine(), this.getGameEngine().getActionManager().getActions()[ModelConstants.ACTION_CALCULATE_INFLUENCE_ID]));
        else if (characterCard.getId() == Character.MINSTREL.getId())
            characterCard.setAction(new MinstrelEffectAction(this.getGameEngine()));
        else if (characterCard.getId() == Character.LADY.getId())
            characterCard.setAction(new LadyEffectAction(this.getGameEngine()));
        else if (characterCard.getId() == Character.KNIGHT.getId())
            characterCard.setAction(new CalculateInfluenceActionKnightEffect(this.getGameEngine(), this.getGameEngine().getActionManager().getActions()[ModelConstants.ACTION_CALCULATE_INFLUENCE_ID]));
        else
            throw new NoSuchElementException("Unknown CharacterCard id, can't assign an Action to it.");
    }

    /**
     * Gets from the table the Card with {@code cardId}, and then from the Card, it takes the Action.
     * With this action it checks its id: if its id is contained in the list of the id of the actions in the action manager,
     * then this action is a decorator, and it is placed in the actions list in the action manager.
     * Otherwise, I run this action.
     * If the action is run directly, save the Card (to {@code cardInUse} attribute) that is associated with that to allow the Action use the Card storage
     * and set that value to null when the Action has ended.
     *
     * @param cardId  the id of the requested CharacterCard
     * @param options the options of the CharacterCard invocation
     * @throws NoSuchElementException       if there isn't a CharacterCard in the Table that matches {@code cardId}
     * @throws TableNotSetException         if table is not set in GameEngine
     * @throws ActionNotSetException        if Action is not set in the CharacterCard
     * @throws WrongMessageContentException if there's an exception relative to options map
     * @throws RuntimeException             if there's an Exception during Action run
     * @throws IllegalGameActionException   if there's an IllegalGameActionException during card act
     * @throws IllegalGameStateException    if there's an IllegalGameStateException during card act
     */
    public void selectCharacterCard(int cardId, int playerId, Map<String, String> options) throws NoSuchElementException, TableNotSetException, ActionNotSetException, WrongMessageContentException, RuntimeException, IllegalGameActionException, IllegalGameStateException {
        Map<Integer, CharacterCard> cards = this.getGameEngine().getTable().getCharacterCards();
        if (!cards.containsKey(cardId))
            throw new NoSuchElementException("The requested CharacterCard is not present in the Table.");
        CharacterCard card = cards.get(cardId);
        // (remember also to manage cardInUse)

        // Set options and player id to the action
        try {
            card.getAction().setOptions(options);
        } catch (WrongMessageContentException e) {
            throw new WrongMessageContentException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage()); // To avoid throw general Exception from here
        }
        card.getAction().setPlayerId(playerId);

        if (isActionADecorator(card.getAction())) {
            // Have to replace the original action in the action manager with this subclass
            this.getGameEngine().getActionManager().getActions()[card.getAction().getId()] = card.getAction();
        } else {
            // Can run the action
            this.cardInUse = card;
            try {
                card.getAction().act();
                card.getAction().modifyRoundAndActionList();
            } catch (IllegalGameActionException e) {
                throw new IllegalGameActionException(e.getMessage());
            } catch (IllegalGameStateException e) {
                throw new IllegalGameStateException(e.getMessage());
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage()); // To avoid throw general Exception from here
            }
            this.cardInUse = null;
        }
    }

    /**
     * Check if Character card action is a decorator or not.
     *
     * @param characterAction the character's action which id is going to be searched
     * @return true if {@code characterAction} is a decorator of an Action contained in the Actions list in the ActionManager;
     * false otherwise
     */
    private boolean isActionADecorator(Action characterAction) {
        for (int i = 0; i < ModelConstants.NUMBER_OF_STANDARD_ACTIONS; i++) {
            if (this.getGameEngine().getActionManager().getActions()[i].getId() == characterAction.getId())
                return true;
        }
        return false;
    }

    /**
     * Checks if the player has enough money to use the CharacterCard
     * @param playerId the player who wants to play the card
     * @param characterCardId the cards that the player wants to play
     * @return true if the player can play the card
     * @throws NoSuchElementException if requested player/card could not be found
     * @throws TableNotSetException if table was not set
     */
    public boolean checkPlayerCanPlayCard(int playerId, int characterCardId) throws NoSuchElementException, TableNotSetException {
        Map<Integer, CharacterCard> cards = getGameEngine().getTable().getCharacterCards();
        if(!cards.containsKey(characterCardId))
            throw new NoSuchElementException("Requested CharacterCard couldn't be found on table");
        int cost = cards.get(characterCardId).getCost();
        if (CommonManager.takePlayerById(getGameEngine(), playerId).getCoins()>=cost)
            return true;
        else
            return false;
    }

    /**
     * Decreases player's money of the cost of the character card. If player hasn't enough money, an exception is thrown.
     * This also marks the card as used, to edit its cost.
     * @param playerId the player who wants to play the card
     * @param characterCardId the cards that the player wants to play
     * @throws NoSuchElementException if requested player/card could not be found
     * @throws NotEnoughCoinException if player hasn't enough money to play that card
     * @throws TableNotSetException if table was not set
     */
    public void decreasePlayersMoneyEditCardCost(int playerId, int characterCardId) throws NoSuchElementException, NotEnoughCoinException, TableNotSetException {
        if(!this.checkPlayerCanPlayCard(playerId, characterCardId))
            throw new NotEnoughCoinException();
        Map<Integer, CharacterCard> cards = getGameEngine().getTable().getCharacterCards();
        // Card is there on the table, else an exception would have been thrown when checked for enough player coins.
        int cost = cards.get(characterCardId).getCost();
        CommonManager.takePlayerById(getGameEngine(),playerId).decrementCoins(cost);
        cards.get(characterCardId).setAsUsed();
    }

    /**
     * Instantiates the CharacterCards actions.
     * @param characterCards the CharacterCards to prepare
     */
    public void prepareCharacterCardsActions(List<CharacterCard> characterCards) {
        for (CharacterCard characterCard: characterCards) {
            this.generateAction(characterCard);
        }
    }
}