package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
class TestRound {

    /**
     * Set a list of possible actions and check if I get the same list; then edit the first passed list and check
     * that it does not affect the internal list.
     */
    @Test
    void getsetPossibleActions() {
        ArrayList<Integer> actions = new ArrayList<Integer>();
        actions.add(1);
        actions.add(2);
        Round round = new Round(2);
        // Set to actions
        round.setPossibleActions(actions);
        // Check it return actions
        assertEquals(actions,round.getPossibleActions());
        // Now I edit actions and check that the actions in the Round doesn't change
        actions.add(3);
        assertNotEquals(actions,round.getPossibleActions());
        // Now re-set to actions and check now it's the same
        round.setPossibleActions(actions);
        assertEquals(actions,round.getPossibleActions());
    }

    /**
     * Set an order of play with only 2 players to check Exception is thrown; then set a 4 players order and check
     * I get that new order with the get function.
     * Last thing to test is the Exception thrown with a duplicate player.
     *
     * In this test it's present also getCurrentPlayer
     */
    @Test
    void getsetorderOfPlay() {
        ArrayList<Integer> order = new ArrayList<Integer>();
        order.add(4);
        order.add(1);
        Round round = new Round(4);
        // Set new order with only 2 players in a match of 4, should throw
        assertThrows(InvalidParameterException.class, ()-> round.setOrderOfPlay(order));
        // Now set the order list correctly (4 players) and check I get it
        order.add(2);
        order.add(3);
        assertDoesNotThrow(()-> round.setOrderOfPlay(order));
        assertEquals(order,round.getOrderOfPlay());
        // Now change the order passed and check it doesn't affect the order in the Round
        order.add(5);
        assertNotEquals(order,round.getOrderOfPlay());
        // Now check I can't add twice a player
        order.remove(Integer.valueOf(5));
        order.remove(Integer.valueOf(1));
        order.add(2);
        assertEquals(4, order.size());
        assertThrows(InvalidParameterException.class, ()-> round.setOrderOfPlay(order));

        // Now check I get correctly the first player (first inserted, 4)
        assertEquals(4, assertDoesNotThrow(()->round.getCurrentPlayer()));
    }

    @Test
    void notifyAllObservers() {

    }

    /**
     * Set an order to the Round, call different times playerTurnEnded and check if players have shifted and also if
     * it indicates correctly when the Round has ended
     */
    @Test
    void playerTurnEnded() {
        ArrayList<Integer> order = new ArrayList<Integer>();
        order.add(4);
        order.add(1);
        order.add(2);
        order.add(3);
        Round round = new Round(4);
        round.setOrderOfPlay(order);

        assertTrue(round.playerTurnEnded());
        assertNotEquals(order, round.getOrderOfPlay()); // Check shifted correctly
        assertTrue(round.playerTurnEnded());
        assertNotEquals(order, round.getOrderOfPlay()); // Check shifted correctly
        assertTrue(round.playerTurnEnded());
        assertNotEquals(order, round.getOrderOfPlay()); // Check shifted correctly
        assertFalse(round.playerTurnEnded()); // false cause the round should be ended
        // Check shifted correctly: now that round ended, the order should be the same as at Round start
        assertEquals(order, round.getOrderOfPlay());
    }
}