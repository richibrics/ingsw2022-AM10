package it.polimi.ingsw.view.game_objects;

import it.polimi.ingsw.model.game_components.TowerColor;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestClientTable {

    /**
     * Checks if it returns the correct parameters of ClientTable.
     *
     * @param value to test
     */
    @ParameterizedTest
    @ValueSource(ints = {0, 100, Integer.MAX_VALUE})
    void testGet(int value) {
        ArrayList<ClientSchoolBoard> schoolBoards = new ArrayList<>();
        ArrayList<Integer> entrance = new ArrayList<>();
        entrance.add(value);
        ArrayList<ArrayList<Integer>> diningRoom = new ArrayList<>();
        ArrayList<Integer> diningRoomColor = new ArrayList<>();
        diningRoomColor.add(value + 1);
        diningRoom.add(diningRoomColor);
        ClientSchoolBoard clientSchoolBoard = new ClientSchoolBoard(entrance, diningRoom);
        schoolBoards.add(clientSchoolBoard);

        ClientBag clientBag = new ClientBag(value + 1);

        ArrayList<ClientCloudTile> cloudTiles = new ArrayList<>();
        ArrayList<Integer> students = new ArrayList<>();
        students.add(value);
        ClientCloudTile clientCloudTile = new ClientCloudTile(value / 2, students);
        cloudTiles.add(clientCloudTile);

        ClientMotherNature clientMotherNature = new ClientMotherNature(value / 2);

        ArrayList<ArrayList<ClientIslandTile>> islandGroups = new ArrayList<>();
        ArrayList<ClientIslandTile> islandTiles = new ArrayList<>();
        ArrayList<Integer> students1 = new ArrayList<>();
        students1.add(value);
        ClientIslandTile clientIslandTile = new ClientIslandTile(value + 1, students1, true, ClientTowerColor.BLACK);
        islandTiles.add(clientIslandTile);
        islandGroups.add(islandTiles);

        ArrayList<ClientPawnColor> availableProfessorPawns = new ArrayList<>();
        availableProfessorPawns.add(ClientPawnColor.YELLOW);
        availableProfessorPawns.add(ClientPawnColor.BLUE);

        ArrayList<ClientCharacterCard> activeCharacterCard = new ArrayList<>();
        ArrayList<Integer> storage = new ArrayList<>();
        storage.add(value);
        ClientCharacterCard clientCharacterCard = new ClientCharacterCard(value + 1, value / 2, storage);
        activeCharacterCard.add(clientCharacterCard);

        int availableNoEntryTiles = 4;
        ClientTable clientTable = new ClientTable(schoolBoards, clientBag, cloudTiles, clientMotherNature, islandGroups, availableProfessorPawns, activeCharacterCard, availableNoEntryTiles);

        assertEquals(clientTable.getSchoolBoards(), schoolBoards);
        assertEquals(clientTable.getBag(), clientBag);
        assertEquals(clientTable.getCloudTiles(), cloudTiles);
        assertEquals(clientTable.getMotherNature(), clientMotherNature);
        assertEquals(clientTable.getIslandTiles(), islandGroups);
        assertEquals(clientTable.getAvailableProfessorPawns(), availableProfessorPawns);
        assertEquals(clientTable.getActiveCharacterCards(), activeCharacterCard);
        assertEquals(clientTable.getAvailableNoEntryTiles(), availableNoEntryTiles);
    }
}