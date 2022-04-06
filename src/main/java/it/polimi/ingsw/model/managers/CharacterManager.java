package it.polimi.ingsw.model.managers;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.actions.Action;
import it.polimi.ingsw.model.exceptions.CharacterStudentsStorageFull;
import it.polimi.ingsw.model.exceptions.EmptyBagException;
import it.polimi.ingsw.model.exceptions.TableNotSetException;
import it.polimi.ingsw.model.game_components.Bag;
import it.polimi.ingsw.model.game_components.Character;
import it.polimi.ingsw.model.game_components.CharacterCard;
import it.polimi.ingsw.model.game_components.StudentDisc;

import java.util.*;

/**
 * Contains methods to manage CharacterCard creation and use.
 * To create a CharacterCard randomly, use {@link CharacterManager#pickThreeCharacters()} to get 3 random Character that can be used
 * to instantiate 3 CharacterCards.
 * Pass a CharacterCard to {@link CharacterManager#setupCardStorage(CharacterCard, Bag)} to insert in the CharacterCard the correct number of StudentDisc.
 * Then you can use the factory method {@link CharacterManager#generateAction(CharacterCard)} to set the correct
 * Action for the passed CharacterCard.
 *
 * Normal use method {@link CharacterManager#selectCharacterCard(int, String)} is called by the Action that is called
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
     * @return three different Character
     * @see Character
     */
    public ArrayList<Character> pickThreeCharacters() {
        ArrayList<Integer> idPool = new ArrayList<>();
        ArrayList<Character> cards = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            idPool.add(i);
        }

        // Shuffle the pool
        Collections.shuffle(idPool);

        // Pick 3 elements
        for (int i = 0; i < 3; i++) {
            cards.add(Character.values()[idPool.get(i)-1]);
        }
        return cards;
    }

    /**
     * Adds a number of StudentDisc to the CharacterCard. The number is an information available in the CharacterCard
     * and is different for each Character.
     * If the CharacterCard already has Students, this method only fills the storage to match the exact number of
     * students that need to be inside.
     * @param characterCard the CharacterCard that will receive the Students drawn from the Bag
     * @param bag the bag from where the Students will be drawn
     * @throws EmptyBagException if there aren't other Students in the Bag
     * @throws CharacterStudentsStorageFull if you're trying to add other students to a full storage
     */
    public void setupCardStorage(CharacterCard characterCard, Bag bag) throws EmptyBagException, CharacterStudentsStorageFull {
        // In this method I don't get the Bag through the Table because this method is called during setup when
        // Table is not ready yet.
        ArrayList<StudentDisc> drawnStudents = bag.drawStudents(characterCard.getStorageCapacity()-characterCard.getStudentsStorage().size());
        for (StudentDisc studentDisc: drawnStudents)
            characterCard.addStudentToStorage(studentDisc);
    }

    /**
     * Creates an Effect Action that matches the CharacterCard passed and sets it in the Card.
     * @param characterCard the CharacterCard that receives the Action
     * @throws NoSuchElementException if {@code characterCard} is unknown and the method doesn't know which Action is needed
     */
    public void generateAction(CharacterCard characterCard) throws NoSuchElementException {
        // Change when Cards actions are ready
        if (characterCard.getId() == Character.FRIAR.getId())
            characterCard.setAction(null);
        else if (characterCard.getId() == Character.COOK.getId())
            characterCard.setAction(null);
        else if (characterCard.getId() == Character.AMBASSADOR.getId())
            characterCard.setAction(null);
        else if (characterCard.getId() == Character.MAILMAN.getId())
            characterCard.setAction(null);
        else if (characterCard.getId() == Character.HERBALIST.getId())
            characterCard.setAction(null);
        else if (characterCard.getId() == Character.CENTAUR.getId())
            characterCard.setAction(null);
        else if (characterCard.getId() == Character.THIEF.getId())
            characterCard.setAction(null);
        else if (characterCard.getId() == Character.JESTER.getId())
            characterCard.setAction(null);
        else if (characterCard.getId() == Character.MUSHROOM_HUNTER.getId())
            characterCard.setAction(null);
        else if (characterCard.getId() == Character.MINSTREL.getId())
            characterCard.setAction(null);
        else if (characterCard.getId() == Character.LADY.getId())
            characterCard.setAction(null);
        else if (characterCard.getId() == Character.KNIGHT.getId())
            characterCard.setAction(null);
        else
            throw new NoSuchElementException("Unknown CharacterCard id, can't assign an Action to it.");
    }

    /**
     * Gets from the table the Card with {@code cardId}, and then from the Card, it takes the Action.
     * With this action it checks its id: if his id is between 0 and the number of actions in the action manager actions
     * map, then this action is a decorator, and it is placed in the actions map in the action manager.
     * Otherwise, if its id is higher, I run this action.
     * If the action is run directly, set the Card (to {@code cardInUse}) that is associated with that to allow the Action use the Card storage
     * and set that value to null when the Action has ended.
     *
     * @param cardId the id of the requested CharacterCard
     * @param options the options of the CharacterCard invocation
     * @throws NoSuchElementException if there isn't a CharacterCard in the Table that matches {@code cardId}
     */
    public void selectCharacterCard(int cardId, String options) throws NoSuchElementException, TableNotSetException {
        Map<Integer, CharacterCard> cards = this.getGameEngine().getTable().getCharacterCards();
        if(!cards.containsKey(cardId))
            throw new NoSuchElementException("The requested CharacterCard is not present in the Table.");
        CharacterCard card = cards.get(cardId);
        // Implement with ActionManager (remember also to manage cardInUse)
    }
}