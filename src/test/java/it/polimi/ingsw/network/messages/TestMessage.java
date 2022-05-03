package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.MessageTypes;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestMessage {

    /**
     * Tests correct type is returned
     */
    @Test
    void getType() {
        Message message = new Message(MessageTypes.USER,"payl");
        assertEquals(MessageTypes.USER,message.getType());
    }

    /**
     * Tests correct payload is returned
     */
    @Test
    void getPayload() {
        Message message = new Message(MessageTypes.USER,"payl");
        assertEquals("payl",message.getPayload());
    }
}