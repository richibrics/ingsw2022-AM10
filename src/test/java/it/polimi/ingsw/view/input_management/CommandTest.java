package it.polimi.ingsw.view.input_management;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandTest {

    @Test
    void parseCLIString() {
    }

    @Test
    void parseGUIEvent() {
    }

    @Test
    void getCLIMenuMessage() {
        Command command = new Command(3, null);
        System.out.println(command.getCLIMenuMessage());
    }

    @Test
    void getGUIMenuMessage() {
        Command command = new Command(3, null);
        System.out.println(command.getGUIMenuMessage());
    }

    @Test
    void hasQuestion() {
        Command command = new Command(3, null);
        System.out.println(command.getGUIMenuMessage());
        assertTrue(command.hasQuestion());
    }

    @Test
    void canEnd() {
        Command command = new Command(3, null);
        System.out.println(command.getGUIMenuMessage());
        assertFalse(command.hasQuestion());
    }

    @Test
    void getActionMessage() {
    }
}