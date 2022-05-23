package it.polimi.ingsw.view.input_management;

import it.polimi.ingsw.view.ViewConstants;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class CommandFilesReaderTest {
    /**
     * Ask for present action CommandData with action id = 3 and check it's correct, then check I have an exception with
     * non-existing CommandData
     */
    @Test
    void getCommandData() {
        assertEquals(3, CommandFilesReader.getCommandFilesReader().getCommandData(3).getActionId());
        assertThrows(NoSuchElementException.class, ()->CommandFilesReader.getCommandFilesReader().getCommandData(Integer.MIN_VALUE).getActionId());
    }
}