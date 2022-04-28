package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.controller.User;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.game_components.*;
import it.polimi.ingsw.model.managers.CommonManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.ValueSource;

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

    /**
     * This action does not parse options
     */
    @Test
    void setOptions() {
    }

    /**
     * Checks all players are inserted in the round order of play. Then checks that the next action is set correctly.
     * @throws Exception
     */
    @Test
    void modifyRoundAndActionList() throws Exception{
        setUpTwoAndFourPlayersAction.modifyRoundAndActionList();
        ArrayList<Integer> orderOfPlay = setUpTwoAndFourPlayersAction.getGameEngine().getRound().getOrderOfPlay();
        assertEquals(gameEngine.getNumberOfPlayers(), orderOfPlay.size());
        Collections.sort(orderOfPlay);
        int index = 1;
        for (Integer integer : orderOfPlay) {
            assertEquals(integer, index);
            index++;
        }
        assertEquals(1, setUpTwoAndFourPlayersAction.getGameEngine().getRound().getPossibleActions().size());
        assertEquals(ModelConstants.ACTION_ON_SELECTION_OF_WIZARD_ID, setUpTwoAndFourPlayersAction.getGameEngine().getRound().getPossibleActions().get(0));
    }

    /**
     * Checks Action id correctly set
     */
    @Test
    void getId() {
        assertEquals(setUpTwoAndFourPlayersAction.getId(), ModelConstants.ACTION_SETUP_ID);
    }

    /**
     * Checks Action game engine correctly set
     */
    @Test
    void getGameEngine() {
        assertEquals(setUpTwoAndFourPlayersAction.getGameEngine(), gameEngine);
    }

    /**
     * Checks if the setup created the correct number of objects. Tested with 2 players.
     */
    @RepeatedTest(12)
    void act() {
        assertEquals(ModelConstants.TWO_PLAYERS, gameEngine.getNumberOfPlayers());
        assertDoesNotThrow(()->setUpTwoAndFourPlayersAction.act());

        // Check if the method has created 12 island tiles
        assertEquals(ModelConstants.NUMBER_OF_ISLAND_TILES, assertDoesNotThrow(()->setUpTwoAndFourPlayersAction.getGameEngine().getTable().getIslandTiles().size()));

        //Check if mother nature has an identifier between 1 and 12
        int motherNatureIslandId = assertDoesNotThrow(()->setUpTwoAndFourPlayersAction.getGameEngine().getTable().getMotherNature().getIslandTile().getId());
        assertTrue(motherNatureIslandId <= ModelConstants.NUMBER_OF_ISLAND_TILES && motherNatureIslandId >= ModelConstants.MIN_ID_OF_ISLAND);

        /* Check if there is a student disc on each island with an id different from the ids of the island with mother nature and the island opposite
        to that with mother nature */

        for (ArrayList<IslandTile> islandGroup : assertDoesNotThrow(()->setUpTwoAndFourPlayersAction.getGameEngine().getTable().getIslandTiles()))
            for (IslandTile islandTile : islandGroup) {
                if (islandTile.getId() != motherNatureIslandId && islandTile.getId() != ((motherNatureIslandId + ModelConstants.NUMBER_OF_ISLAND_TILES/2) % ModelConstants.NUMBER_OF_ISLAND_TILES == 0 ? ModelConstants.NUMBER_OF_ISLAND_TILES : (motherNatureIslandId + ModelConstants.NUMBER_OF_ISLAND_TILES/2) % ModelConstants.NUMBER_OF_ISLAND_TILES))
                    assertEquals(islandTile.peekStudents().size(), 1);
                else
                    assertEquals(islandTile.peekStudents().size(), 0);
            }

        // Check if the method has created 2 cloud tiles
        assertEquals(gameEngine.getNumberOfPlayers(), assertDoesNotThrow(()->setUpTwoAndFourPlayersAction.getGameEngine().getTable().getCloudTiles().size()));

        // Check if the method has created 2 school boards
        assertEquals(gameEngine.getNumberOfPlayers(), assertDoesNotThrow(()->setUpTwoAndFourPlayersAction.getGameEngine().getTable().getSchoolBoards().size()));

        // Check if each school board has 7 student discs in the entrance
        for (SchoolBoard schoolBoard : assertDoesNotThrow(()->setUpTwoAndFourPlayersAction.getGameEngine().getTable().getSchoolBoards()))
            assertEquals(ModelConstants.INITIAL_NUMBER_OF_STUDENTS_IN_ENTRANCE_TWO_FOUR_PLAYERS, schoolBoard.getEntrance().size());

        // Check if the method has created 3 different character cards and count the students it has inserted in the character card storage
        assertEquals(ModelConstants.NUMBER_OF_CHARACTER_CARDS, assertDoesNotThrow(()->setUpTwoAndFourPlayersAction.getGameEngine().getTable().getCharacterCards().size()));
        int studentsForCharacter = 0;
        ArrayList<CharacterCard> cards = new ArrayList<>();
        for (CharacterCard characterCard : assertDoesNotThrow(()->setUpTwoAndFourPlayersAction.getGameEngine().getTable().getCharacterCards().values())) {
            for (CharacterCard card : cards)
                assertNotEquals(characterCard.getId(), card.getId());
            studentsForCharacter += characterCard.getStorageCapacity();
            cards.add(characterCard);
        }

        // Check if the method has created 5 professor pawn, one for each color
        assertEquals(PawnColor.values().length, assertDoesNotThrow(()->setUpTwoAndFourPlayersAction.getGameEngine().getTable().getAvailableProfessorPawns().size()));
        for (PawnColor color : PawnColor.values())
            assertEquals(1, assertDoesNotThrow(()->setUpTwoAndFourPlayersAction.getGameEngine().getTable().getAvailableProfessorPawns().stream().filter(professorPawn -> professorPawn.getColor() == color).collect(Collectors.toList()).size()));

        // Check if each team has 8 towers
        for (Team team : setUpTwoAndFourPlayersAction.getGameEngine().getTeams())
            assertEquals(ModelConstants.NUMBER_OF_TOWERS_TWO_FOUR_PLAYERS, team.getTowers().size());

        // Check the number of students left in the bag
        assertEquals(ModelConstants.INITIAL_NUMBER_OF_STUDENTS_PER_COLOR * PawnColor.values().length
                        - ModelConstants.INITIAL_NUMBER_OF_STUDENTS_PER_ISLAND * PawnColor.values().length
                        - ModelConstants.INITIAL_NUMBER_OF_STUDENTS_IN_ENTRANCE_TWO_FOUR_PLAYERS * gameEngine.getNumberOfPlayers()
                        - studentsForCharacter
                    , assertDoesNotThrow(()->setUpTwoAndFourPlayersAction.getGameEngine().getTable().getBag().getNumberOfStudents()));
    }

    /**
     * Checks if generated islands are correct.
     */
    @Test
    void setUpIsland() {
        ArrayList<ArrayList<IslandTile>> islandGroups = setUpTwoAndFourPlayersAction.setUpIsland();
        int i = ModelConstants.MIN_ID_OF_ISLAND;
        for (ArrayList<IslandTile> islandGroup : islandGroups)
            for (IslandTile islandTile : islandGroup) {
                assertEquals(islandTile.getId(), i);
                i++;
            }
    }

    /**
     * Checks mother nature is placed on an island tile.
     */
    @Test
    void placeMotherNature() {
        ArrayList<ArrayList<IslandTile>> islandGroups = setUpTwoAndFourPlayersAction.setUpIsland();
        MotherNature motherNature = setUpTwoAndFourPlayersAction.placeMotherNature(islandGroups);
        assertTrue(motherNature.getIslandTile().getId() >= ModelConstants.MIN_ID_OF_ISLAND && motherNature.getIslandTile().getId() <= ModelConstants.NUMBER_OF_ISLAND_TILES);
        ArrayList<IslandTile> islandTiles = new ArrayList<>();
        for (ArrayList<IslandTile> islandGroup : islandGroups)
            islandTiles.add(islandGroup.get(0));
        assertTrue(islandTiles.contains(motherNature.getIslandTile()));
    }

    /**
     * Checks the correct students are generated per color.
     */
    @Test
    void generateStudentDiscs() {
        ArrayList<StudentDisc> studentDiscs = setUpTwoAndFourPlayersAction.generateStudentDiscs();
        assertEquals(ModelConstants.INITIAL_NUMBER_OF_STUDENTS_PER_COLOR*PawnColor.values().length, studentDiscs.size());
        for (PawnColor color : PawnColor.values())
            assertEquals(ModelConstants.INITIAL_NUMBER_OF_STUDENTS_PER_COLOR, studentDiscs.stream().filter(studentDisc -> studentDisc.getColor() == color).collect(Collectors.toList()).size());
    }

    /**
     * Checks the players are placed on the island tiles (not where there is Mother Nature or on the opposite island).
     */
    @Test
    void drawFromBagAndPutOnIsland() {
        int islandForMotherNature = ModelConstants.NUMBER_OF_ISLAND_TILES/2 - 1;
        int oppositeMotherNatureIsland = (islandForMotherNature + ModelConstants.NUMBER_OF_ISLAND_TILES/2) % ModelConstants.NUMBER_OF_ISLAND_TILES;
        Bag bag = new Bag();
        ArrayList<StudentDisc> studentDiscs = setUpTwoAndFourPlayersAction.generateStudentDiscs();
        ArrayList<ArrayList<IslandTile>> islandGroups = setUpTwoAndFourPlayersAction.setUpIsland();
        MotherNature motherNature = new MotherNature(islandGroups.get(islandForMotherNature).get(0));
        assertDoesNotThrow(()->setUpTwoAndFourPlayersAction.drawFromBagAndPutOnIsland(bag, studentDiscs, islandGroups, islandForMotherNature));
        for (int i = 0; i < ModelConstants.NUMBER_OF_ISLAND_TILES; i++) {
            if (i != islandForMotherNature && i != oppositeMotherNatureIsland)
                assertEquals(1, islandGroups.get(i).get(0).peekStudents().size());
            else
                assertEquals(0, islandGroups.get(i).get(0).peekStudents().size());
        }
    }

    /**
     * Checks drawFromBagAndPutOnIsland does not throw. The correct number of students in it is checked in "act" test.
     */
    @Test
    void putRemainingStudentsInBag() {
        Bag bag = new Bag();
        ArrayList<StudentDisc> studentDiscs = setUpTwoAndFourPlayersAction.generateStudentDiscs();
        ArrayList<ArrayList<IslandTile>> islandGroups = setUpTwoAndFourPlayersAction.setUpIsland();
        MotherNature motherNature = setUpTwoAndFourPlayersAction.placeMotherNature(islandGroups);
        assertDoesNotThrow(()->setUpTwoAndFourPlayersAction.drawFromBagAndPutOnIsland(bag, studentDiscs, islandGroups, 1));

    }

    @Test
    void drawCharacters() {
        ArrayList<StudentDisc> studentDiscs = setUpTwoAndFourPlayersAction.generateStudentDiscs();
        Bag bag = new Bag();
        setUpTwoAndFourPlayersAction.putRemainingStudentsInBag(bag, studentDiscs);
        Map<Integer, CharacterCard> characterCards = new HashMap<>();
        assertDoesNotThrow(()->setUpTwoAndFourPlayersAction.drawCharacters(characterCards, bag));
        assertEquals(ModelConstants.NUMBER_OF_CHARACTER_CARDS, characterCards.size());
        ArrayList<CharacterCard> cards = new ArrayList<>();
        for (CharacterCard characterCard : characterCards.values()) {
            for (CharacterCard card : cards)
                assertNotEquals(characterCard.getId(), card.getId());
            cards.add(characterCard);
        }
    }

    /**
     * Checks cloud tiles are generated per player with correct id
     */
    @Test
    void setUpCloudTiles() {
        ArrayList<CloudTile> cloudTiles = new ArrayList<>();
        setUpTwoAndFourPlayersAction.setUpCloudTiles(cloudTiles);
        assertEquals(gameEngine.getNumberOfPlayers(), cloudTiles.size());
        for (int i = 0; i < gameEngine.getNumberOfPlayers(); i++)
            assertEquals(i+1, cloudTiles.get(i).getId());
    }

    /**
     * Checks professors are generated correctly
     */
    @Test
    void setUpProfessors() {
        ArrayList<ProfessorPawn> professorPawns = new ArrayList<>();
        setUpTwoAndFourPlayersAction.setUpProfessors(professorPawns);
        assertEquals(PawnColor.values().length, professorPawns.size());
        for (PawnColor color : PawnColor.values())
            assertEquals(1, professorPawns.stream().filter(professorPawn -> professorPawn.getColor() == color).collect(Collectors.toList()).size());
    }

    /**
     * Checks schoolBoards are generated correctly
     */
    @Test
    void setUpSchoolBoards() {
        ArrayList<SchoolBoard> schoolBoards = new ArrayList<>();
        setUpTwoAndFourPlayersAction.setUpSchoolBoards(schoolBoards);
        assertEquals(gameEngine.getNumberOfPlayers(), schoolBoards.size());
        for (int i = 0; i < gameEngine.getNumberOfPlayers(); i++) {
            int finalI = i;
            assertEquals(schoolBoards.get(i), assertDoesNotThrow(()-> CommonManager.takePlayerById(gameEngine, finalI +1).getSchoolBoard()));
        }
    }

    /**
     * Checks teams have correct number of towers
     */
    @Test
    void setUpTowers() {
        setUpTwoAndFourPlayersAction.setUpTowers();
        assertEquals(ModelConstants.NUMBER_OF_TOWERS_TWO_FOUR_PLAYERS, setUpTwoAndFourPlayersAction.getGameEngine().getTeams().get(0).getTowers().size());
        assertEquals(TowerColor.WHITE, setUpTwoAndFourPlayersAction.getGameEngine().getTeams().get(0).getTowers().get(0).getColor());
        assertEquals(ModelConstants.NUMBER_OF_TOWERS_TWO_FOUR_PLAYERS, setUpTwoAndFourPlayersAction.getGameEngine().getTeams().get(1).getTowers().size());
        assertEquals(TowerColor.BLACK, setUpTwoAndFourPlayersAction.getGameEngine().getTeams().get(1).getTowers().get(0).getColor());
    }

    /**
     * Checks correct number of students are placed on entrances and that the final number of students in the bag is correct.
     */
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
        assertEquals(ModelConstants.INITIAL_NUMBER_OF_STUDENTS_PER_COLOR*PawnColor.values().length - ModelConstants.INITIAL_NUMBER_OF_STUDENTS_IN_ENTRANCE_TWO_FOUR_PLAYERS* gameEngine.getNumberOfPlayers(),
                assertDoesNotThrow(()->bag.getNumberOfStudents()));
    }
}