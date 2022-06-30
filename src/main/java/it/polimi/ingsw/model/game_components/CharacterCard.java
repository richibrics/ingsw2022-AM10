package it.polimi.ingsw.model.game_components;

import it.polimi.ingsw.model.actions.Action;
import it.polimi.ingsw.model.exceptions.ActionNotSetException;
import it.polimi.ingsw.model.exceptions.CharacterStudentsStorageFull;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Class that describes the CharacterCard as a game object, with methods to get information about it and to update its status.
 */
public class CharacterCard {
    final private Character character;
    final private ArrayList<StudentDisc> studentsStorage;
    private Action action;
    private boolean used;

    public CharacterCard(Character character) {
        this.character = character;
        this.studentsStorage = new ArrayList<>();
        this.used = false;
    }

    /**
     * Returns the id of the Character of the CharacterCard.
     *
     * @return the id of the Character of the CharacterCard
     */
    public int getId() {
        return this.character.getId();
    }

    /**
     * Returns the storage capacity of the Character of the CharacterCard (the number of StudentDiscs the CharacterCard
     * must have after the Setup and when the Character action ends)
     *
     * @return the storage capacity of the Character of the CharacterCard
     */
    public int getStorageCapacity() {
        return this.character.getStorageCapacity();
    }

    /**
     * Returns an ArrayList with the students the Storage of the card contains
     *
     * @return an ArrayList with the students the Storage of the card contains
     */
    public ArrayList<StudentDisc> getStudentsStorage() {
        return new ArrayList<>(this.studentsStorage);
    }

    /**
     * Adds the passed StudentDisc to the CharacterCard storage. I can't add anything if the Card capacity is reached.
     *
     * @param studentDisc the StudentDisc to add to the Card
     * @throws CharacterStudentsStorageFull if the CharacterCard already has reached its storage capacity and can't accept any other StudentDisc
     */
    public void addStudentToStorage(StudentDisc studentDisc) throws CharacterStudentsStorageFull {
        if (this.studentsStorage.size() >= this.getStorageCapacity())
            throw new CharacterStudentsStorageFull();
        this.studentsStorage.add(studentDisc);
    }

    /**
     * Removes from the storage the requested StudentDisc looking for its id.
     *
     * @param studentToRemove the id of the StudentDisc to remove
     * @return the removed StudentDisc
     * @throws NoSuchElementException if the requested Student is not in the CharacterCard stograge
     */
    public StudentDisc removeStudentFromStorage(int studentToRemove) throws NoSuchElementException {
        for (int i = 0; i < this.studentsStorage.size(); i++) {
            if (this.studentsStorage.get(i).getId() == studentToRemove) {
                return this.studentsStorage.remove(i);
            }
        }
        throw new NoSuchElementException("The requested student could not be found in the CharacterCard students storage");
    }

    /**
     * Returns the Action of this CharacterCard if available.
     *
     * @return the Action of this CharacterCard
     * @throws ActionNotSetException if the Action was not set by {@link CharacterCard#setAction(Action)}
     */
    public Action getAction() throws ActionNotSetException {
        if (this.action == null)
            throw new ActionNotSetException();
        return this.action;
    }

    /**
     * Sets the Action for the CharacterCard. This Action is already configured by CharacterManager and SetupAction.
     *
     * @param action the Action to set
     */
    public void setAction(Action action) {
        this.action = action;
    }

    /**
     * Sets the CharacterCard as used so the next time the Card cost will be higher (only for the first time).
     */
    public void setAsUsed() {
        this.used = true;
    }

    /**
     * Returns the cost of the CharacterCard checking if the Card has already been used.
     *
     * @return the cost of the CharacterCard
     */
    public int getCost() {
        if (this.used == true)
            return this.character.getCost() + 1;
        else
            return this.character.getCost();
    }
}
