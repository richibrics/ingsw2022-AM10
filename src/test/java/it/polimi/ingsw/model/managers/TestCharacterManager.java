package it.polimi.ingsw.model.managers;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.game_components.Bag;
import it.polimi.ingsw.model.game_components.Character;
import it.polimi.ingsw.model.game_components.CharacterCard;
import it.polimi.ingsw.model.game_components.PawnColor;
import it.polimi.ingsw.model.game_components.StudentDisc;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class TestCharacterManager {

    /**
     * Tests that 3 different Character are returned.
     */
    @Test
    void pickThreeCharacters() {
        CharacterManager characterManager = new CharacterManager(new GameEngine(new ArrayList<>()));
        ArrayList<Character> characters = characterManager.pickThreeCharacters();
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
        assertDoesNotThrow(()->characterManager.setupCardStorage(characterCard, bag));
        assertEquals(characterCard.getStorageCapacity(), characterCard.getStudentsStorage().size());

        // Check exceptions and refill: removes 1 student and refill the storage
        if(characterCard.getStorageCapacity()!=0) {
            assertDoesNotThrow(()->characterCard.removeStudentFromStorage(characterCard.getStudentsStorage().get(0).getId())); // Remove one
            assertEquals(characterCard.getStorageCapacity()-1, characterCard.getStudentsStorage().size()); // Removed ok
            assertDoesNotThrow(()->characterManager.setupCardStorage(characterCard, bag));
            assertEquals(characterCard.getStorageCapacity(), characterCard.getStudentsStorage().size()); // Refill ok
        }
    }

    /**
     * Tests that the correct Action is set for the requested CharacterCard.
     */
    @Test
    void generateAction() {
        // To do when Actions are implemented
    }

    /**
     * Tests that the CharacterCard action is run or is inserted in the ActionManager actions map.
     */
    @Test
    void selectCharacterCard() {
        // To do when Action manager and Actions are implemented
    }
}