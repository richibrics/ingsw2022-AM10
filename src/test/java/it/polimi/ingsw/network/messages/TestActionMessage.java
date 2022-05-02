package it.polimi.ingsw.network.messages;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class TestActionMessage {

    /**
     * Tests correct action id is returned.
     */
    @Test
    void getActionId() {
        ActionMessage actionMessage = new ActionMessage(4,new HashMap<>());
        assertEquals(4,actionMessage.getActionId());
    }

    /**
     * Tests options are returned correctly.
     */
    @Test
    void getOptions() {
        HashMap<String, String> options = new HashMap<>();
        options.put("prova","okay");
        ActionMessage actionMessage = new ActionMessage(4,options);
        assertEquals(options,actionMessage.getOptions());
    }
}