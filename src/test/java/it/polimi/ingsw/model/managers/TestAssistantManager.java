package it.polimi.ingsw.model.managers;

import it.polimi.ingsw.controller.GameEngine;
import it.polimi.ingsw.controller.User;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Team;
import it.polimi.ingsw.model.exceptions.AssistantCardNotSetException;
import it.polimi.ingsw.model.game_components.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class TestAssistantManager {

    static GameEngine gameEngine;
    static AssistantManager assistantManager;

    @BeforeEach
    void setUp()
    {
        User user1 = new User("1", 2);
        Player player1 = new Player(user1, 1, 3);
        ArrayList<Player> players1 = new ArrayList<Player>();
        players1.add(player1);
        Team team1 = new Team(1, players1);

        User user2 = new User("2", 2);
        Player player2 = new Player(user2, 2, 3);
        ArrayList<Player> players2 = new ArrayList<Player>();
        players2.add(player2);
        Team team2 = new Team(2, players2);

        ArrayList<Team> teams = new ArrayList<>();
        teams.add(team1);
        teams.add(team2);
        gameEngine = new GameEngine(teams);

        SchoolBoard schoolBoard1 = new SchoolBoard();
        SchoolBoard schoolBoard2 = new SchoolBoard();
        ArrayList<SchoolBoard> schoolBoards = new ArrayList<>();
        player1.setSchoolBoard(schoolBoard1);
        player2.setSchoolBoard(schoolBoard2);
        schoolBoards.add(schoolBoard1);
        schoolBoards.add(schoolBoard2);

        StudentDisc student1 = new StudentDisc(1, PawnColor.BLUE);
        StudentDisc student2 = new StudentDisc(2, PawnColor.PINK);
        StudentDisc student3 = new StudentDisc(3, PawnColor.RED);
        ArrayList<StudentDisc> students = new ArrayList<>();
        students.add(student1);
        students.add(student2);
        students.add(student3);
        Bag bag = new Bag();
        bag.pushStudents(students);

        CloudTile cloud1 = new CloudTile(1);
        CloudTile cloud2 = new CloudTile(2);
        ArrayList<CloudTile> clouds = new ArrayList<>();
        clouds.add(cloud1);
        clouds.add(cloud2);

        ArrayList<ArrayList<IslandTile>> islands = new ArrayList<>();
        for(int i = 1; i <= 12; i++) {
            IslandTile island = new IslandTile(i);
            ArrayList<IslandTile> islandGroup = new ArrayList<>();
            islandGroup.add(island);
            islands.add(islandGroup);
        }

        MotherNature motherNature = new MotherNature(islands.get(6).get(0));

        ArrayList<ProfessorPawn> professorPawns = new ArrayList<>();

        Map<Integer, CharacterCard> characterCards = new HashMap<>();

        Table table = new Table(schoolBoards, bag, clouds, motherNature, islands, professorPawns, characterCards);

        gameEngine.setTable(table);

        assistantManager = new AssistantManager(gameEngine);
    }

    @Test
    void setWizard() {
        assertThrows(NoSuchElementException.class, ()->assistantManager.setWizard(3,1));
        assistantManager.setWizard(1,1);
        assertEquals(assertDoesNotThrow(()->assistantManager.getGameEngine().getTeams().get(0).getPlayers().get(0).getWizard().getAssistantCards().get(3).getCardValue()),4);
        assertEquals(assertDoesNotThrow(()->assistantManager.getGameEngine().getTeams().get(0).getPlayers().get(0).getWizard().getAssistantCards().get(6).getMovements()),4);
        assistantManager.setWizard(2, 3);
        assertEquals(assertDoesNotThrow(()->assistantManager.getGameEngine().getTeams().get(1).getPlayers().get(0).getWizard().getAssistantCards().get(4).getId()),25);
        assertEquals(assertDoesNotThrow(()->assistantManager.getGameEngine().getTeams().get(1).getPlayers().get(0).getWizard().getAssistantCards().get(4).getMovements()),3);
    }



    /**
     * Tests getValueOfAssistantCardInHand, getMovementsOfAssistantCardInHand, incrementMovementsOfAssistantCardInHand
     */

    @Test
    void AssistantCardInHand() {
        assistantManager.setWizard(1,1);
        assertDoesNotThrow(()->assistantManager.setAssistantCard(1,5));
        assertEquals(assertDoesNotThrow(()->assistantManager.getValueOfAssistantCardInHand(1)), 5);
        assertEquals(assertDoesNotThrow(()->assistantManager.getMovementsOfAssistantCardInHand(1)), 3);
        assertDoesNotThrow(()->assistantManager.incrementMovementsOfAssistantCardInHand(1,2));
        assertEquals(assertDoesNotThrow(()->assistantManager.getMovementsOfAssistantCardInHand(1)), 5);
    }



    @Test
    void getValueOfLastPlayedAssistantCard() {
        AssistantCard assistantCard = new AssistantCard(12, 2, 1);
        assistantManager.getGameEngine().getTeams().get(1).getPlayers().get(0).setLastPlayedAssistantCard(assistantCard);
        assertEquals(assertDoesNotThrow(()->assistantManager.getValueOfLastPlayedAssistantCard(2)),2);
    }



    @Test
    void moveAssistantCardInHandToLastPlayed() {
        assistantManager.setWizard(1,1);
        assertDoesNotThrow(()->assistantManager.setAssistantCard(1,5));
        assertDoesNotThrow(()->assistantManager.moveAssistantCardInHandToLastPlayed(1));
        assertThrows(AssistantCardNotSetException.class, ()->assistantManager.getValueOfAssistantCardInHand(1));
        assertEquals(assertDoesNotThrow(()->assistantManager.getValueOfLastPlayedAssistantCard(1)), 5);
    }

    /**
     * Ensures that the card value is correctly calculated by the card id
     */
    @Test
    void getCardValueById() {
        assertEquals(1,assistantManager.getCardValueById(1));
        assertEquals(5,assistantManager.getCardValueById(5));
        assertEquals(9,assistantManager.getCardValueById(9));
        assertEquals(10,assistantManager.getCardValueById(10));
        assertEquals(5,assistantManager.getCardValueById(15));
        assertEquals(9,assistantManager.getCardValueById(19));
        assertEquals(10,assistantManager.getCardValueById(20));
        assertEquals(6,assistantManager.getCardValueById(26));
        assertEquals(5,assistantManager.getCardValueById(35));
    }

    /**
     * Ensures that the correct cards are returned for a player: use a card and check it can't be used again;
     * check that the played card can't be played from the second player unless it's the only one he has.
     */
    @Test
    void getPlayableAssistantCardValues() {
        assistantManager.setWizard(1,1);
        ArrayList<Integer> results = assertDoesNotThrow(()->assistantManager.getPlayableAssistantCardValues(1));
        assertEquals(10, results.size());
        assertTrue(results.contains(assistantManager.getCardValueById(1)));
        assistantManager.setAssistantCard(1, 1);
        results = assertDoesNotThrow(()->assistantManager.getPlayableAssistantCardValues(1));
        assertEquals(9, results.size());
        assertFalse(results.contains(assistantManager.getCardValueById(1)));

        // Now get cards for player 2 and check he can't play the card with getCardValueById(1)
        assistantManager.setWizard(2,2);
        results = assertDoesNotThrow(()->assistantManager.getPlayableAssistantCardValues(2));
        assertEquals(9, results.size());
        assertFalse(results.contains(assistantManager.getCardValueById(1)));

        // Now leave only card with value = 1 to player 2 and check he can play that card
        for (int cardValue = 2; cardValue <= 10; cardValue++) {
            assistantManager.setAssistantCard(2, 10+cardValue);
        }

        results = assertDoesNotThrow(()->assistantManager.getPlayableAssistantCardValues(2));
        assertEquals(1, results.size());
        assertTrue(results.contains(assistantManager.getCardValueById(1)));
    }

    /**
     * Ask for a wizard when nobody has one, and he must be free, then set a wizard to a player and check it's not available
     */
    @Test
    void isWizardAvailableToBeChosen() {
        assertTrue(assertDoesNotThrow(()->assistantManager.isWizardAvailableToBeChosen(1)));
        assertDoesNotThrow(()->assistantManager.setWizard(1,1));
        assertFalse(assertDoesNotThrow(()->assistantManager.isWizardAvailableToBeChosen(1)));
    }
}