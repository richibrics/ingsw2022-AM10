package it.polimi.ingsw.view.input_management;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommandTest {

    @Test
    void parseCLIString() {
    }

    @Test
    void parseGUIEvent() {
    }

    @Test
    void getCLIMenuMessage() {
        Command command = new Command(4, 1, null);
        System.out.println(command.getCLIMenuMessage());
    }

    @Test
    void getGUIMenuMessage() {
        Command command = new Command(4, 1, null);
        System.out.println(command.getGUIMenuMessage());
    }

    @Test
    void hasQuestion() {
        Command command = new Command(4, 1, null);
        assertTrue(command.hasQuestion());
    }

    @Test
    void canEnd() {
        Command command = new Command(4, 1, null);
        assertFalse(command.canEnd());
    }

    @Test
    void getActionMessage() {
    }
}