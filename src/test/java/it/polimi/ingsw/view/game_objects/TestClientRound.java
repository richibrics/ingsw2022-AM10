package it.polimi.ingsw.view.game_objects;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestClientRound {

    /**
     * Checks if it returns the correct parameters of ClientRound.
     *
     * @param value to test
     */
    @ParameterizedTest
    @ValueSource(ints = {0, 100, Integer.MAX_VALUE})
    void testGet(int value) {
        ArrayList<Integer> listOfActions = new ArrayList<>();
        listOfActions.add(1);
        listOfActions.add(4);
        listOfActions.add(6);
        listOfActions.add(8);
        int currentPlayer = value;
        ClientRound clientRound = new ClientRound(listOfActions, value);
        assertEquals(clientRound.getPossibleActions(), listOfActions);
        assertEquals(clientRound.getCurrentPlayer(), value);
    }
}