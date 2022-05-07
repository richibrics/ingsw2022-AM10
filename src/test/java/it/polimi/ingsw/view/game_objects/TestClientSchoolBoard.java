package it.polimi.ingsw.view.game_objects;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestClientSchoolBoard {

    /**
     * Checks if it returns the correct parameters of ClientSchoolBoard.
     *
     * @param value to test
     */
    @ParameterizedTest
    @ValueSource(ints = {1, 10})
    void getEntrance(int value) {
        ArrayList<Integer> entrance = new ArrayList<>();
        entrance.add(value);
        ArrayList<ArrayList<Integer>> diningRoom = new ArrayList<>();
        ArrayList<Integer> diningRoomColor = new ArrayList<>();
        diningRoomColor.add(value + 1);
        diningRoom.add(diningRoomColor);
        ClientSchoolBoard clientSchoolBoard = new ClientSchoolBoard(entrance, diningRoom);
        assertEquals(clientSchoolBoard.getEntrance(), entrance);
        assertEquals(clientSchoolBoard.getDiningRoom(), diningRoom);
    }
}