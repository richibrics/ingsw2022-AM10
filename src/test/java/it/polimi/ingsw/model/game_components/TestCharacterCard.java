package it.polimi.ingsw.model.game_components;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.model.actions.effects.FriarEffectAction;
import it.polimi.ingsw.model.exceptions.ActionNotSetException;
import it.polimi.ingsw.model.exceptions.CharacterStudentsStorageFull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class TestCharacterCard {

    @BeforeEach
    void setUp() {
    }

    /**
     * Initializes a CharacterCard with a {@code character} and tests the returned id is the same one that the Character has.
     *
     * @param character the Character for the CharacterCard
     */
    @ParameterizedTest
    @EnumSource(value = Character.class)
    void getId(Character character) {
        CharacterCard characterCard = new CharacterCard(character);
        assertEquals(character.getId(), characterCard.getId());
    }

    /**
     * Initializes a CharacterCard with a {@code character} and tests the returned storage capacity is the same one that
     * the Character has.
     *
     * @param character the Character for the CharacterCard
     */
    @ParameterizedTest
    @EnumSource(value = Character.class)
    void getStorageCapacity(Character character) {
        CharacterCard characterCard = new CharacterCard(character);
        assertEquals(character.getStorageCapacity(), characterCard.getStorageCapacity());
    }

    /**
     * Tests that after a CharacterCard creation I don't have any element and that I have the correct elements after
     * I inserted them.
     */
    @Test
    void getStudentsStorage() {
        CharacterCard characterCard = new CharacterCard(Character.FRIAR);
        assertEquals(0, characterCard.getStudentsStorage().size());
        assertDoesNotThrow(() -> characterCard.addStudentToStorage(new StudentDisc(1, PawnColor.BLUE)));
        assertDoesNotThrow(() -> characterCard.addStudentToStorage(new StudentDisc(2, PawnColor.BLUE)));
        assertEquals(2, characterCard.getStudentsStorage().size());
        assertEquals(1, characterCard.getStudentsStorage().get(0).getId());
        assertEquals(2, characterCard.getStudentsStorage().get(1).getId());
    }

    /**
     * Setups a CharacterCard that accepts 4 Students in its storage.
     * Checks if it can add 4 students, and then it adds another student to check it the limit exception has
     * been thrown.
     */
    @Test
    void addStudentToStorage() {
        // Friar accept up to 4 StudentDiscs in its storage
        CharacterCard characterCard = new CharacterCard(Character.FRIAR);
        for (int i = 0; i < 4; i++) {
            int id = i + 1;
            assertDoesNotThrow(() -> characterCard.addStudentToStorage(new StudentDisc(id, PawnColor.BLUE)));
        }
        // Now check I have them inside
        assertEquals(4, characterCard.getStudentsStorage().size());
        for (int i = 0; i < 4; i++) {
            int id = i + 1;
            assertEquals(id, characterCard.getStudentsStorage().get(i).getId());
        }

        // Now add another Student, should get Exception. Check then nothing has changed
        assertThrows(CharacterStudentsStorageFull.class, () -> characterCard.addStudentToStorage(new StudentDisc(5, PawnColor.BLUE)));
        assertEquals(4, characterCard.getStudentsStorage().size());
    }

    /**
     * Setups a CharacterCard that accepts 4 Students in its storage.
     * Checks if it can remove 4 students and check if they are removed. Then remove a non-existing Student and check
     * that an exception has been thrown.
     */
    @Test
    void removeStudentFromStorage() {
        // Friar accept up to 4 StudentDiscs in its storage
        CharacterCard characterCard = new CharacterCard(Character.FRIAR);
        for (int i = 0; i < 4; i++) {
            int id = i + 1;
            assertDoesNotThrow(() -> characterCard.addStudentToStorage(new StudentDisc(id, PawnColor.BLUE)));
        }
        assertEquals(4, characterCard.getStudentsStorage().size());

        // Now in the Card I have 4 students: remove them correctly and check it.
        assertDoesNotThrow(() -> characterCard.removeStudentFromStorage(1));
        assertEquals(3, characterCard.getStudentsStorage().size());
        assertDoesNotThrow(() -> characterCard.removeStudentFromStorage(2));
        assertEquals(2, characterCard.getStudentsStorage().size());
        assertDoesNotThrow(() -> characterCard.removeStudentFromStorage(3));
        assertEquals(1, characterCard.getStudentsStorage().size());
        assertDoesNotThrow(() -> characterCard.removeStudentFromStorage(4));
        assertEquals(0, characterCard.getStudentsStorage().size());

        // Remove a non available StudentDisc: exception
        assertThrows(NoSuchElementException.class, () -> characterCard.removeStudentFromStorage(4));
        assertEquals(0, characterCard.getStudentsStorage().size()); // Check correct state
    }

    /**
     * Set an Action and check it's not null and that it's the same I wanted to set
     */
    @Test
    void setgetAction() {
        GameEngine gameEngine = new GameEngine(new ArrayList<>());
        CharacterCard characterCard = new CharacterCard(Character.FRIAR);
        assertThrows(ActionNotSetException.class, () -> characterCard.getAction());
        characterCard.setAction(new FriarEffectAction(gameEngine));
        assertEquals(FriarEffectAction.class, assertDoesNotThrow(() -> characterCard.getAction()).getClass());
    }

    /**
     * Test setAsUsed, getCost
     * Get card cost, check it's like in Character.getCost. Then set the card as used twice and check the cost is
     * 1 penny higher.
     */
    @ParameterizedTest
    @EnumSource(value = Character.class)
    void setAsUsedGetCost(Character character) {
        CharacterCard characterCard = new CharacterCard(character);
        assertEquals(character.getCost(), characterCard.getCost());
        characterCard.setAsUsed();
        characterCard.setAsUsed();
        assertEquals(character.getCost() + 1, characterCard.getCost());
    }
}