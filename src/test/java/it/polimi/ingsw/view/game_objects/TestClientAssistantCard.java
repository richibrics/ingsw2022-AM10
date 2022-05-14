package it.polimi.ingsw.view.game_objects;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestClientAssistantCard {

    /**
     * Creates a ClientAssistantCard and checks if its parameters are correct.
     *
     * @param value to test.
     */
    @ParameterizedTest
    @ValueSource(ints = {0, 100, Integer.MAX_VALUE - 2})
    void testGet(int value) {
        ClientAssistantCard clientAssistantCard = new ClientAssistantCard(value + 1, value / 2, value);
        assertEquals(clientAssistantCard.getId(), value + 1);
        assertEquals(clientAssistantCard.getCardValue(), value / 2);
        assertEquals(clientAssistantCard.getMovements(), value);
    }

}