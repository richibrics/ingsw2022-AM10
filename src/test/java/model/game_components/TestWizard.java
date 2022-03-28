package model.game_components;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class TestWizard {
    @ParameterizedTest
    @ValueSource(ints = {0,100,Integer.MAX_VALUE})
    public void testGetId(int value)
    {
        Wizard wizard = new Wizard(value,new ArrayList<>());
        assertEquals(wizard.getId(),value);
    }

    @ParameterizedTest
    @ValueSource(ints = {0,1,10})
    public void testAssistantCards(int value)
    {
        ArrayList<AssistantCard> assistantCards = new ArrayList<>();
        for (int i = 0; i < value; i++) {
            assistantCards.add(new AssistantCard(i,i,value-i));
        }
        AssistantCard extraAssistantCard = new AssistantCard(value+1,1,1);
        Wizard wizard = new Wizard(1,assistantCards);
        assertEquals(wizard.getAssistantCards().size(),value);
        assertThrows(NoSuchElementException.class, () -> wizard.removeAssistantCard(extraAssistantCard));
        int remaining = value;
        for(AssistantCard assistantCard: assistantCards)
        {
            assertDoesNotThrow(() -> wizard.removeAssistantCard(assistantCard));
            remaining = remaining - 1;
            assertEquals(wizard.getAssistantCards().size(), remaining);
            assertThrows(NoSuchElementException.class, () -> wizard.removeAssistantCard(assistantCard));
            assertEquals(wizard.getAssistantCards().size(), remaining);
        }
        assertEquals(wizard.getAssistantCards().size(), 0);
    }
}
