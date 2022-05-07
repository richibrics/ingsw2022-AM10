package it.polimi.ingsw.model.game_components;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class TestWizard {

    /**
     * Creates a Wizard and checks if it returns the correct id of Wizard.
     *
     * @param value to test.
     */
    @ParameterizedTest
    @ValueSource(ints = {0, 100, Integer.MAX_VALUE})
    public void testGetId(int value) {
        Wizard wizard = new Wizard(value, new ArrayList<>());
        assertEquals(wizard.getId(), value);
    }

    /**
     * Creates an ArrayList of AssistantCard to populate with new AssistantCard.
     * Then it creates a Wizard made by the ArrayList of AssistantCard and checks if the number of AssistantCard is correct.
     * After that it creates another AssistantCard and throws the NoSuchElementException if this AssistantCard does not exist in this wizard.
     * At the end every AssistantCard is removed one by one from the Wizard, and it checks if the Wizard has the same number
     * of AssistantCard as well as the remaining ones.
     *
     * @param value to test.
     */
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 10})
    public void testAssistantCards(int value) {
        ArrayList<AssistantCard> assistantCards = new ArrayList<>();
        for (int i = 0; i < value; i++) {
            assistantCards.add(new AssistantCard(i, i, value - i));
        }
        AssistantCard extraAssistantCard = new AssistantCard(value + 1, 1, 1);
        Wizard wizard = new Wizard(1, assistantCards);
        assertEquals(wizard.getAssistantCards().size(), value);
        assertThrows(NoSuchElementException.class, () -> wizard.removeAssistantCard(extraAssistantCard));
        int remaining = value;
        for (AssistantCard assistantCard : assistantCards) {
            assertDoesNotThrow(() -> wizard.removeAssistantCard(assistantCard));
            remaining = remaining - 1;
            assertEquals(wizard.getAssistantCards().size(), remaining);
            assertThrows(NoSuchElementException.class, () -> wizard.removeAssistantCard(assistantCard));
            assertEquals(wizard.getAssistantCards().size(), remaining);
        }
        assertEquals(wizard.getAssistantCards().size(), 0);
    }
}
