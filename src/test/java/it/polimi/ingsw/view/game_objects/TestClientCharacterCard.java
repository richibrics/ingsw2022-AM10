package it.polimi.ingsw.view.game_objects;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestClientCharacterCard {

    /**
     * Creates a ClientCharacterCard with its parameters.
     * Then it checks if the parameters are correct.
     *
     * @param value to test
     */
    @ParameterizedTest
    @ValueSource(ints = {0, 100, Integer.MAX_VALUE - 2})
    void testGet(int value) {
        ArrayList<Integer> storage = new ArrayList<>();
        storage.add(value);
        ClientCharacterCard clientCharacterCard = new ClientCharacterCard(value + 1, value / 2, storage);
        assertEquals(clientCharacterCard.getId(), value + 1);
        assertEquals(clientCharacterCard.getCost(), value / 2);
        assertEquals(clientCharacterCard.getStorage(), storage);
    }

}