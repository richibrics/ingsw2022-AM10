package it.polimi.ingsw.network.messages;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestMessage {

    /**
     * Tests correct type is returned
     */
    @Test
    void getType() {
        Message message = new Message("prova","payl");
        assertEquals("prova",message.getType());
    }

    /**
     * Tests correct payload is returned
     */
    @Test
    void getPayload() {
        Message message = new Message("prova","payl");
        assertEquals("payl",message.getPayload());
    }
}