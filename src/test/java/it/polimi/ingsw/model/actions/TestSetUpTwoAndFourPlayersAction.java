package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.controller.User;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.game_components.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class TestSetUpTwoAndFourPlayersAction {

    static SetUpTwoAndFourPlayersAction setUpTwoAndFourPlayersAction;
    static GameEngine gameEngine;

    @BeforeEach
    void setUp() {
        User user1 = new User("1", 2);
        User user2 = new User("2", 2);
        Player player1 = new Player(user1, 1, 3);
        Player player2 = new Player(user2, 2, 3);
        ArrayList<Player> players1 = new ArrayList<>();
        players1.add(player1);
        Team team1 = new Team(1, players1);
        ArrayList<Player> players2 = new ArrayList<>();
        players2.add(player2);
        Team team2 = new Team(2, players2);
        ArrayList<Team> teams = new ArrayList<>();
        teams.add(team1);
        teams.add(team2);
        gameEngine = new GameEngine(teams);
        setUpTwoAndFourPlayersAction = new SetUpTwoAndFourPlayersAction(gameEngine);
    }

    //TODO
    @Test
    void getPlayerId() {
    }

    //TODO
    @Test
    void getOptions() {
    }

    //TODO
    @Test
    void setPlayerId() {
    }

    //TODO
    @Test
    void setOptions() {
    }

    @Test
    void modifyRound() {
        setUpTwoAndFourPlayersAction.modifyRound();
        ArrayList<Integer> orderOfPlay = setUpTwoAndFourPlayersAction.getGameEngine().getRound().getOrderOfPlay();
        assertEquals(orderOfPlay.size(), 2);
        Collections.sort(orderOfPlay);
        int index = 1;
        for (Integer integer : orderOfPlay) {
            assertEquals(integer, index);
            index++;
        }
        assertEquals(setUpTwoAndFourPlayersAction.getGameEngine().getRound().getPossibleActions().size(), 1);
        assertEquals(setUpTwoAndFourPlayersAction.getGameEngine().getRound().getPossibleActions().get(0), 1);
    }

    @Test
    void getId() {
        assertEquals(setUpTwoAndFourPlayersAction.getId(), -1);
    }

    @Test
    void getGameEngine() {
        assertEquals(setUpTwoAndFourPlayersAction.getGameEngine(), gameEngine);
    }

    @RepeatedTest(12)
    void act() {
        assertDoesNotThrow(()->setUpTwoAndFourPlayersAction.act());

        // Check if the method has created 12 island tiles
        assertEquals(assertDoesNotThrow(()->setUpTwoAndFourPlayersAction.getGameEngine().getTable().getIslandTiles().size()), 12);

        //Check if mother nature has an identifier between 1 and 12
        int motherNatureIslandId = assertDoesNotThrow(()->setUpTwoAndFourPlayersAction.getGameEngine().getTable().getMotherNature().getIslandTile().getId());
        assertTrue(motherNatureIslandId <= 12 && motherNatureIslandId >= 1);

        /* Check if there is a student disc on each island with an id different from the ids of the island with mother nature and the island opposite
        to that with mother nature */

        for (ArrayList<IslandTile> islandGroup : assertDoesNotThrow(()->setUpTwoAndFourPlayersAction.getGameEngine().getTable().getIslandTiles()))
            for (IslandTile islandTile : islandGroup) {
                if (islandTile.getId() != motherNatureIslandId && islandTile.getId() != ((motherNatureIslandId + 6) % 12 == 0 ? 12 : (motherNatureIslandId + 6) % 12))
                    assertEquals(islandTile.peekStudents().size(), 1);
                else
                    assertEquals(islandTile.peekStudents().size(), 0);
            }

        // Check if the method has created 2 cloud tiles
        assertEquals(assertDoesNotThrow(()->setUpTwoAndFourPlayersAction.getGameEngine().getTable().getCloudTiles().size()), 2);

        // Check if the method has created 2 school boards
        assertEquals(assertDoesNotThrow(()->setUpTwoAndFourPlayersAction.getGameEngine().getTable().getSchoolBoards().size()), 2);

        // Check if each school board has 7 student discs in the entrance
        for (SchoolBoard schoolBoard : assertDoesNotThrow(()->setUpTwoAndFourPlayersAction.getGameEngine().getTable().getSchoolBoards()))
            assertEquals(schoolBoard.getEntrance().size(), 7);

        // Check if the method has created 3 different character cards
        assertEquals(assertDoesNotThrow(()->setUpTwoAndFourPlayersAction.getGameEngine().getTable().getCharacterCards().size()), 3);
        int studentsForCharacter = 0;
        ArrayList<CharacterCard> cards = new ArrayList<>();
        for (CharacterCard characterCard : assertDoesNotThrow(()->setUpTwoAndFourPlayersAction.getGameEngine().getTable().getCharacterCards().values())) {
            for (CharacterCard card : cards)
                assertNotEquals(characterCard.getId(), card.getId());
            studentsForCharacter += characterCard.getStorageCapacity();
            cards.add(characterCard);
        }

        // Check if the method has created 5 professor pawn, one for each color
        assertEquals(assertDoesNotThrow(()->setUpTwoAndFourPlayersAction.getGameEngine().getTable().getAvailableProfessorPawns().size()), 5);
        for (PawnColor color : PawnColor.values())
            assertEquals(assertDoesNotThrow(()->setUpTwoAndFourPlayersAction.getGameEngine().getTable().getAvailableProfessorPawns().stream().filter(professorPawn -> professorPawn.getColor() == color).collect(Collectors.toList()).size()), 1);

        // Check if each team has 8 towers
        for (Team team : setUpTwoAndFourPlayersAction.getGameEngine().getTeams())
            assertEquals(team.getTowers().size(), 8);

        // Check the number of students left in the bag
        assertEquals(assertDoesNotThrow(()->setUpTwoAndFourPlayersAction.getGameEngine().getTable().getBag().getNumberOfStudents()), 130 - 24 - studentsForCharacter);
    }

    @Test
    void setUpIsland() {
        ArrayList<ArrayList<IslandTile>> islandGroups = setUpTwoAndFourPlayersAction.setUpIsland();
        int i = 1;
        for (ArrayList<IslandTile> islandGroup : islandGroups)
            for (IslandTile islandTile : islandGroup) {
                assertEquals(islandTile.getId(), i);
                i++;
            }
    }

    @Test
    void placeMotherNature() {
        ArrayList<ArrayList<IslandTile>> islandGroups = setUpTwoAndFourPlayersAction.setUpIsland();
        MotherNature motherNature = setUpTwoAndFourPlayersAction.placeMotherNature(islandGroups);
        assertTrue(motherNature.getIslandTile().getId() >=0 && motherNature.getIslandTile().getId() <=12);
        ArrayList<IslandTile> islandTiles = new ArrayList<>();
        for (ArrayList<IslandTile> islandGroup : islandGroups)
            islandTiles.add(islandGroup.get(0));
        assertTrue(islandTiles.contains(motherNature.getIslandTile()));
    }

    @Test
    void generateStudentDiscs() {
        ArrayList<StudentDisc> studentDiscs = setUpTwoAndFourPlayersAction.generateStudentDiscs();
        assertEquals(studentDiscs.size(), 130);
        for (PawnColor color : PawnColor.values())
            assertEquals(studentDiscs.stream().filter(studentDisc -> studentDisc.getColor() == color).collect(Collectors.toList()).size(), 26);
    }

    @Test
    void drawFromBagAndPutOnIsland() {
        Bag bag = new Bag();
        ArrayList<StudentDisc> studentDiscs = setUpTwoAndFourPlayersAction.generateStudentDiscs();
        ArrayList<ArrayList<IslandTile>> islandGroups = setUpTwoAndFourPlayersAction.setUpIsland();
        MotherNature motherNature = new MotherNature(islandGroups.get(7).get(0));
        assertDoesNotThrow(()->setUpTwoAndFourPlayersAction.drawFromBagAndPutOnIsland(bag, studentDiscs, islandGroups, 7));
        for (int i = 0; i < 12; i++) {
            if (i != 7 && i != 1)
                assertEquals(islandGroups.get(i).get(0).peekStudents().size(), 1);
            else
                assertEquals(islandGroups.get(i).get(0).peekStudents().size(), 0);
        }
    }

    @Test
    void putRemainingStudentsInBag() {
        Bag bag = new Bag();
        ArrayList<StudentDisc> studentDiscs = setUpTwoAndFourPlayersAction.generateStudentDiscs();
        ArrayList<ArrayList<IslandTile>> islandGroups = setUpTwoAndFourPlayersAction.setUpIsland();
        MotherNature motherNature = setUpTwoAndFourPlayersAction.placeMotherNature(islandGroups);
        assertDoesNotThrow(()->setUpTwoAndFourPlayersAction.drawFromBagAndPutOnIsland(bag, studentDiscs, islandGroups, 7));
    }

    @Test
    void drawCharacters() {
        ArrayList<StudentDisc> studentDiscs = setUpTwoAndFourPlayersAction.generateStudentDiscs();
        Bag bag = new Bag();
        setUpTwoAndFourPlayersAction.putRemainingStudentsInBag(bag, studentDiscs);
        Map<Integer, CharacterCard> characterCards = new HashMap<>();
        assertDoesNotThrow(()->setUpTwoAndFourPlayersAction.drawCharacters(characterCards, bag));
        assertEquals(characterCards.size(), 3);
        ArrayList<CharacterCard> cards = new ArrayList<>();
        for (CharacterCard characterCard : characterCards.values()) {
            for (CharacterCard card : cards)
                assertNotEquals(characterCard.getId(), card.getId());
            cards.add(characterCard);
        }
    }


    @Test
    void setUpCloudTiles() {
        ArrayList<CloudTile> cloudTiles = new ArrayList<>();
        setUpTwoAndFourPlayersAction.setUpCloudTiles(cloudTiles);
        assertEquals(cloudTiles.size(), 2);
        assertEquals(cloudTiles.get(0).getId(), 1);
        assertEquals(cloudTiles.get(1).getId(), 2);
    }

    @Test
    void setUpProfessors() {
        ArrayList<ProfessorPawn> professorPawns = new ArrayList<>();
        setUpTwoAndFourPlayersAction.setUpProfessors(professorPawns);
        assertEquals(professorPawns.size(), 5);
        for (PawnColor color : PawnColor.values())
            assertEquals(professorPawns.stream().filter(professorPawn -> professorPawn.getColor() == color).collect(Collectors.toList()).size(), 1);
    }

    @Test
    void setUpSchoolBoards() {
        ArrayList<SchoolBoard> schoolBoards = new ArrayList<>();
        setUpTwoAndFourPlayersAction.setUpSchoolBoards(schoolBoards);
        assertEquals(schoolBoards.size(), 2);
        assertEquals(assertDoesNotThrow(()->setUpTwoAndFourPlayersAction.getGameEngine().getTeams().get(0).getPlayers().get(0).getSchoolBoard()), schoolBoards.get(0));
        assertEquals(assertDoesNotThrow(()->setUpTwoAndFourPlayersAction.getGameEngine().getTeams().get(1).getPlayers().get(0).getSchoolBoard()), schoolBoards.get(1));
    }

    @Test
    void setUpTowers() {
        setUpTwoAndFourPlayersAction.setUpTowers();
        assertEquals(setUpTwoAndFourPlayersAction.getGameEngine().getTeams().get(0).getTowers().size(), 8);
        assertEquals(setUpTwoAndFourPlayersAction.getGameEngine().getTeams().get(0).getTowers().get(0).getColor(), TowerColor.WHITE);
        assertEquals(setUpTwoAndFourPlayersAction.getGameEngine().getTeams().get(1).getTowers().size(), 8);
        assertEquals(setUpTwoAndFourPlayersAction.getGameEngine().getTeams().get(1).getTowers().get(0).getColor(), TowerColor.BLACK);
    }

    @Test
    void drawStudentsAndPlaceOnEntrance() {
        ArrayList<StudentDisc> studentDiscs = setUpTwoAndFourPlayersAction.generateStudentDiscs();
        Bag bag = new Bag();
        setUpTwoAndFourPlayersAction.putRemainingStudentsInBag(bag, studentDiscs);
        ArrayList<SchoolBoard> schoolBoards = new ArrayList<>();
        setUpTwoAndFourPlayersAction.setUpSchoolBoards(schoolBoards);
        assertDoesNotThrow(()->setUpTwoAndFourPlayersAction.drawStudentsAndPlaceOnEntrance(bag));
        for (Team team : setUpTwoAndFourPlayersAction.getGameEngine().getTeams())
            for (Player player : team.getPlayers())
                assertEquals(assertDoesNotThrow(()->player.getSchoolBoard().getEntrance().size()), 7);
        assertEquals(assertDoesNotThrow(()->bag.getNumberOfStudents()), 130 - 14);
    }
}