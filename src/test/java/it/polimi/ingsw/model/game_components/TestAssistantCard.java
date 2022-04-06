package it.polimi.ingsw.model.game_components;

import it.polimi.ingsw.model.game_components.AssistantCard;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestAssistantCard {

    /**
     * Creates an AssistantCard and checks if its parameters are correct.
     *
     * @param value to test.
     */
    @ParameterizedTest
    @ValueSource(ints = {0,100,Integer.MAX_VALUE-2})
    public void testGet(int value)
    {
        AssistantCard assistantCard = new AssistantCard(value+1,value/2,value);
        assertEquals(assistantCard.getId(),value+1);
        assertEquals(assistantCard.getCardValue(),value/2);
        assertEquals(assistantCard.getMovements(),value);
    }

    /**
     * Creates an AssistantCard with its parameters and checks if its movements are increased.
     */
    @Test
    public void incrementMovements()
    {
        AssistantCard assistantCard = new AssistantCard(1,2,4);
        assistantCard.incrementMovements(3);
        assertEquals(assistantCard.getMovements(), 7);
    }
}
