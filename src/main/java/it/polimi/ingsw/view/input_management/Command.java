package it.polimi.ingsw.view.input_management;

import it.polimi.ingsw.network.messages.ActionMessage;
import it.polimi.ingsw.view.game_objects.ClientTable;
import javafx.event.Event;

import java.util.Map;
import java.util.NoSuchElementException;

public class Command {
    private CommandData commandData;
    private int currentEntryIndex;
    private Map<String,String> actionOptions;

    private final ClientTable clientTable;

    /**
     * Creates the Command, using the {@code actionId} to read the json
     * with CommandData from disk and load it into the class.
     * @param actionId the id of the action whom menu is being generated
     * @param clientTable the table of the game, to grab ids of objects on the table
     * @throws NoSuchElementException if the action id hasn't a CommandData file associated
     */
    public Command(int actionId, ClientTable clientTable) throws NoSuchElementException {
        this.clientTable = clientTable;
        this.commandData = CommandFilesReader.getCommandFilesReader().getCommandData(actionId);
    }

    /**
     * Receives the string typed by the user in the CLI.
     * The input is validated through the validation rules of the entry and then saved in the message.
     * @param input the string typed by the user in the CLI
     */
    public void parseCLIString(String input) {

    }

    /**
     * Receives the GUI event, like the clicked button id/tag.
     * The input is validated through the validation rules of the entry and then saved in the message.
     * @param event the GUI event, like the clicked button id/tag.
     */
    public void parseGUIEvent(Event event) {

    }

    /**
     * Returns the question for the user input - CLI version.
     * @return the question for the user input
     */
    public String getCLIMenuMessage() {
        return this.commandData.getSchema().get(this.currentEntryIndex).getQuestion() + this.commandData.getSchema().get(this.currentEntryIndex).getCliHint();
    }

    /**
     * Returns the question for the user input - GUI version.
     * @return the question for the user input
     */
    public String getGUIMenuMessage() {
        return this.commandData.getSchema().get(this.currentEntryIndex).getQuestion() + this.commandData.getSchema().get(this.currentEntryIndex).getGuiHint();
    }

    /**
     * Returns True if there's a question at the current index.
     * @return True if there's a question at the current index
     */
    public boolean hasQuestion() {
        return this.currentEntryIndex < this.commandData.getSchema().size();
    }

    /**
     * Returns True if the current input and the next ones aren't mandatory, or if there aren't questions next.
     * @return True if the current input and the next ones aren't mandatory, or if there aren't questions next
     */
    public boolean canEnd() {
        return !hasQuestion() || this.commandData.getSchema().get(this.currentEntryIndex).isOptional();
    }

    /**
     * Adds the value of the response to the ActionMessage, using the correct Map key.
     * Then the menu entry index moves forward.
     * @param value the value to add to the ActionMessage
     */
    private void addValueToActionMessage(String value) {
        this.actionOptions.put(this.commandData.getSchema().get(this.currentEntryIndex).getActionKey(), value);
        this.currentEntryIndex++;
    }

    /**
     * Returns the ActionMessage for the Server. This message is correct only if canEnd is true.
     * @return the ActionMessage for the Server
     */
    public ActionMessage getActionMessage() {
        return new ActionMessage(this.commandData.getActionId(), this.actionOptions);
    }
}
