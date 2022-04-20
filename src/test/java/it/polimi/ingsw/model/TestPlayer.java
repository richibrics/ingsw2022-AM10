package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.User;
import it.polimi.ingsw.model.exceptions.AssistantCardNotSetException;
import it.polimi.ingsw.model.exceptions.NotEnoughCoinException;
import it.polimi.ingsw.model.exceptions.SchoolBoardNotSetException;
import it.polimi.ingsw.model.exceptions.WizardNotSetException;
import it.polimi.ingsw.model.game_components.AssistantCard;
import it.polimi.ingsw.model.game_components.SchoolBoard;
import it.polimi.ingsw.model.game_components.Wizard;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TestPlayer {

    static User user;

    @BeforeAll
    static void beforeAll() {
        user = new User("test",2);
    }

    @ParameterizedTest
    @ValueSource(ints = {0,100,Integer.MAX_VALUE})
    void getPlayerId(int value) {
        Player player = new Player(user, value, 5);
        assertEquals(player.getPlayerId(), value);
    }

    @ParameterizedTest
    @ValueSource(ints = {0,100,Integer.MAX_VALUE})
    void getCoins(int value) {
        Player player = new Player(user, 1, value);
        assertEquals(player.getCoins(), value);
    }

    @Test
    void incrementCoins() {
        Player player = new Player(user, 0, 5);
        player.incrementCoins();
        player.incrementCoins();
        assertEquals(player.getCoins(), 7);
    }

    @Test
    void decrementCoins() {
        Player player = new Player(user, 0, 5);
        assertThrows(NotEnoughCoinException.class, () -> player.decrementCoins(6));
        assertDoesNotThrow(() -> player.decrementCoins(3));
        assertEquals(player.getCoins(), 2);
    }

    @Test
    void Wizard() {
        Player player = new Player(user, 0,5);
        assertFalse(player.hasWizard());
        assertThrows(WizardNotSetException.class, () -> player.getWizard());
        ArrayList<AssistantCard> assistantCards = new ArrayList<>();
        for (int i = 0; i < 12; i++)
            assistantCards.add(new AssistantCard(i,i,12-i));
        Wizard wizard = new Wizard(1, assistantCards);
        player.setWizard(wizard);
        assertEquals(assertDoesNotThrow(() -> player.getWizard()), wizard);
        assertTrue(player.hasWizard());
    }

    @Test
    void ActiveAssistantCard() {

        ArrayList<AssistantCard> assistantCards = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            assistantCards.add(new AssistantCard(i, i, 12 - i));
        }
        Wizard wizard = new Wizard(1, assistantCards);

        Player player = new Player(user, 0, 5);
        player.setWizard(wizard);
        assertThrows(AssistantCardNotSetException.class, () -> player.getActiveAssistantCard());
        player.setActiveAssistantCard(2);
        assertEquals(assertDoesNotThrow(() -> player.getActiveAssistantCard().getId()), 2);
        for (AssistantCard assistantCard : assertDoesNotThrow(() -> player.getWizard().getAssistantCards()))
            assertNotEquals(assistantCard.getId(), 2);

        assertEquals(player.popActiveAssistantCard().getId(), 2);
        assertThrows(AssistantCardNotSetException.class, () -> player.getActiveAssistantCard());
    }


    @Test
    void LastPlayedAssistantCard() {
        Player player = new Player(user, 0, 5);
        assertThrows(AssistantCardNotSetException.class, () -> player.getLastPlayedAssistantCard());
        AssistantCard assistantCard = new AssistantCard(1, 5, 2);
        player.setLastPlayedAssistantCard(assistantCard);
        assertEquals(assertDoesNotThrow(() -> player.getLastPlayedAssistantCard()), assistantCard);
        AssistantCard assistantCard2 = new AssistantCard(2, 4, 2);
        player.setLastPlayedAssistantCard(assistantCard2);
        assertEquals(assertDoesNotThrow(() -> player.getLastPlayedAssistantCard()), assistantCard2);
    }

    @Test
    void SchoolBoard() {
        Player player = new Player(user, 0, 5);
        assertThrows(SchoolBoardNotSetException.class, () -> player.getSchoolBoard());
        SchoolBoard schoolBoard1 = new SchoolBoard();
        player.setSchoolBoard(schoolBoard1);
        assertEquals(assertDoesNotThrow(() -> player.getSchoolBoard()), schoolBoard1);
        SchoolBoard schoolBoard2 = new SchoolBoard();
        player.setSchoolBoard(schoolBoard2);
        assertEquals(assertDoesNotThrow(() -> player.getSchoolBoard()), schoolBoard2);
    }
}