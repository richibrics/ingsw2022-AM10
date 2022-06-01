package it.polimi.ingsw.view.input_management;

import it.polimi.ingsw.controller.exceptions.WrongMessageContentException;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.game_components.PawnColor;
import it.polimi.ingsw.network.messages.ActionMessage;
import it.polimi.ingsw.view.ViewUtilityFunctions;
import it.polimi.ingsw.view.cli.CliConstants;
import it.polimi.ingsw.view.cli.drawers.CliDrawersUtilityFunctions;
import it.polimi.ingsw.view.game_objects.*;
import javafx.event.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class Command {
    private final ClientTable clientTable;
    private final ClientTeams clientTeams;
    private final int playerId;
    private CommandData commandData;
    private int currentEntryIndex;
    private final Map<String, String> actionOptions;

    /**
     * Creates the Command, using the {@code actionId} to read the json
     * with CommandData from disk and load it into the class.
     *
     * @param actionId    the id of the action whom menu is being generated
     * @param playerId    id of the player of this client
     * @param clientTable the table of the game, to grab ids of objects on the table
     * @throws NoSuchElementException if the action id hasn't a CommandData file associated
     */
    public Command(int actionId, int playerId, ClientTable clientTable, ClientTeams clientTeams) throws NoSuchElementException {
        this.clientTable = clientTable;
        this.clientTeams = clientTeams;
        this.playerId = playerId;
        this.actionOptions = new HashMap<>();
        this.commandData = CommandFilesReader.getCommandFilesReader().getCommandData(actionId);
    }

    /**
     * Receives the string typed by the user in the CLI.
     * The input is validated through the validation rules of the entry and then saved in the message.
     *
     * @param input the string typed by the user in the CLI
     * @throws IllegalArgumentException if the input is not valid
     * @throws RuntimeException         if the client player can't be found in any team
     */
    public void parseCLIString(String input) throws IllegalArgumentException, RuntimeException {
        String finalValue = "null";
        int intInput = 0;
        String validation = this.commandData.getSchema().get(this.currentEntryIndex).getValidation();

        // An island number
        if (validation.equals(CommandDataEntryValidationSet.ISLAND)) {
            try {
                intInput = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid Island number");
            }
            if (intInput < ModelConstants.MIN_ID_OF_ISLAND || intInput > ModelConstants.NUMBER_OF_ISLAND_TILES)
                throw new IllegalArgumentException("Invalid Island number");
            finalValue = String.valueOf(intInput);
        }
        // An island number or D for dining room
        else if (validation.equals(CommandDataEntryValidationSet.ISLAND_OR_DINING_ROOM)) {
            if (input.equals(CliConstants.DINING_ROOM_CAPITAL_D) ||
                    input.equals(CliConstants.DINING_ROOM_LOWER_CASE_D)) { // Dining room
                finalValue = String.valueOf(ModelConstants.ACTION_MOVE_STUDENTS_FROM_ENTRANCE_OPTIONS_KEY_POSITION_VALUE_DINING_ROOM);
            } else { // Should be island number
                try {
                    intInput = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid Island number or dining room");
                }
                if (intInput < ModelConstants.MIN_ID_OF_ISLAND || intInput > ModelConstants.NUMBER_OF_ISLAND_TILES)
                    throw new IllegalArgumentException("Invalid Island number");
                finalValue = String.valueOf(intInput);
            }
        } else if (validation.equals(CommandDataEntryValidationSet.STUDENT_ENTRANCE)) {
            PawnColor color;
            try {
                color = PawnColor.convertStringToPawnColor(input);
            } catch (WrongMessageContentException e) {
                throw new IllegalArgumentException("Unknown color inserted");
            }
            // Get the id of the player's school board through the player id
            int indexOfPlayerSchoolBoard = ViewUtilityFunctions.getPlayerSchoolBoardIndex(this.playerId, clientTeams);
            // Now I have the color, get the id of the student using it. Here I do it from entrance of the User
            ArrayList<Integer> entrance = this.clientTable.getSchoolBoards().get(indexOfPlayerSchoolBoard).getEntrance();
            // Get last student from the table, if there's one
            int selectedStudent = -1;
            for (Integer student : entrance) {
                if (CliDrawersUtilityFunctions.getStudentColorById(student).equals(color)) {
                    selectedStudent = student;
                    break;
                }
            }
            if (selectedStudent == -1) {
                throw new IllegalArgumentException("No students available with the selected color");
            }
            finalValue = String.valueOf(selectedStudent);
        } else if (validation.equals(CommandDataEntryValidationSet.STUDENT_DINING_ROOM)) {
            PawnColor color;
            try {
                color = PawnColor.convertStringToPawnColor(input);
            } catch (WrongMessageContentException e) {
                throw new IllegalArgumentException("Unknown color inserted");
            }
            // Get the id of the player's school board through the player id
            int indexOfPlayerSchoolBoard = ViewUtilityFunctions.getPlayerSchoolBoardIndex(this.playerId, clientTeams);
            // Now I have the color, get the id of the student using it. Here I do it from dining room of the User
            ArrayList<Integer> table = this.clientTable.getSchoolBoards().get(indexOfPlayerSchoolBoard).getDiningRoom().get(color.getId());
            // Get last student from the table, if there's one
            if (table.size() > 0) {
                finalValue = String.valueOf(table.get(table.size() - 1));
            } else {
                throw new IllegalArgumentException("No students available with the selected color");
            }
        } else if (validation.equals(CommandDataEntryValidationSet.STUDENT_CHARACTER_CARD)) {
            PawnColor color;
            try {
                color = PawnColor.convertStringToPawnColor(input);
            } catch (WrongMessageContentException e) {
                throw new IllegalArgumentException("Unknown color inserted");
            }
            // Now I have the color, get the id of the student using it. Here I do it from the storage of the character card
            // that is selected
            // The character card is available in CommandData, so I take the storage from the table using it
            ArrayList<Integer> storage = null;
            for (ClientCharacterCard characterCard : this.clientTable.getActiveCharacterCards()) {
                if (characterCard.getId() == this.commandData.getCharacterId()) {
                    storage = characterCard.getStorage();
                    break;
                }
            }
            if (storage == null) {
                throw new IllegalArgumentException("Can't select any student");
            }
            // Get a student with the specified color from there, if there's one
            int selectedStudent = -1;
            for (Integer student : storage) {
                if (CliDrawersUtilityFunctions.getStudentColorById(student).equals(color)) {
                    selectedStudent = student;
                    break;
                }
            }
            if (selectedStudent == -1) {
                throw new IllegalArgumentException("No students available with the selected color");
            }
            finalValue = String.valueOf(selectedStudent);
        } else if (validation.equals(CommandDataEntryValidationSet.CHARACTER_CARD)) {
            // Check character card on the table. If so, get the Command for that card if it is available.
            try {
                intInput = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid Character card number");
            }
            if (intInput < ModelConstants.MIN_ID_OF_ISLAND || intInput > ModelConstants.NUMBER_OF_ISLAND_TILES)
                throw new IllegalArgumentException("Character card not available");

            ClientCharacterCard askedCard = null;

            // Check on the table
            for (ClientCharacterCard characterCard : this.clientTable.getActiveCharacterCards())
                if (intInput == characterCard.getId()) askedCard = characterCard;

            if (askedCard == null) throw new IllegalArgumentException("Character card not available");

            // Use the card: set in the message
            finalValue = String.valueOf(intInput); // Set in the ActionMessage options

        } else if (validation.equals(CommandDataEntryValidationSet.ASSISTANT_CARD)) {
            try {
                intInput = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid Assistant number");
            }
            if (intInput < ModelConstants.MIN_VALUE_OF_ASSISTANT_CARD || intInput > ModelConstants.MAX_VALUE_OF_ASSISTANT_CARD)
                throw new IllegalArgumentException("Invalid Assistant number");

            // Now using the assistant card value, get the correct id for the card of this student
            intInput = intInput + ModelConstants.MAX_VALUE_OF_ASSISTANT_CARD * (this.getWizardNumber() - 1);
            finalValue = String.valueOf(intInput);
        } else if (validation.equals(CommandDataEntryValidationSet.WIZARD)) {
            try {
                intInput = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid Wizard number");
            }
            if (intInput < ModelConstants.MIN_ID_OF_WIZARD || intInput > ModelConstants.MAX_ID_OF_WIZARD)
                throw new IllegalArgumentException("Invalid Wizard number");
            finalValue = String.valueOf(intInput);
        } else if (validation.equals(CommandDataEntryValidationSet.CLOUD_TILE)) {
            try {
                intInput = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid Cloud number");
            }
            int playerNumber = this.clientTable.getSchoolBoards().size();
            if (intInput < ModelConstants.MIN_ID_OF_CLOUD_TILE || intInput > playerNumber)
                throw new IllegalArgumentException("Invalid Cloud number");
            finalValue = String.valueOf(intInput);
        } else if (validation.equals(CommandDataEntryValidationSet.COLOR)) {
            // To validate a color, convert it to PawnColor and then get its String
            try {
                PawnColor color = PawnColor.convertStringToPawnColor(input);
                finalValue = color.toString();
            } catch (WrongMessageContentException e) {
                throw new IllegalArgumentException("Unknown color inserted");
            }
        } else throw new IllegalArgumentException("I'm expecting a " + validation);

        this.addValueToActionMessage(finalValue);


        // If using the card: load its CommandData if available (reset also MenuIndex)
        if (validation.equals(CommandDataEntryValidationSet.CHARACTER_CARD) && CommandFilesReader.getCommandFilesReader().doesCharacterCommandDataExist(intInput)) {
            // Character has its own CommandData, load it
            this.commandData = CommandFilesReader.getCommandFilesReader().getCharacterCommandData(intInput);
            this.currentEntryIndex = 0;
        }
    }

    /**
     * Receives the GUI event, like the clicked button id/tag.
     * The input is validated through the validation rules of the entry and then saved in the message.
     *
     * @param event the GUI event, like the clicked button id/tag.
     */
    public void parseGUIEvent(Event event) {

    }

    /**
     * Returns the question for the user input - CLI version.
     *
     * @return the question for the user input
     */
    public String getCLIMenuMessage() {
        return this.commandData.getSchema().get(this.currentEntryIndex).getQuestion() + this.commandData.getSchema().get(this.currentEntryIndex).getCliHint();
    }

    /**
     * Returns the question for the user input - GUI version.
     *
     * @return the question for the user input
     */
    public String getGUIMenuMessage() {
        return this.commandData.getSchema().get(this.currentEntryIndex).getQuestion() + this.commandData.getSchema().get(this.currentEntryIndex).getGuiHint();
    }

    /**
     * Returns True if there's a question at the current index.
     *
     * @return True if there's a question at the current index
     */
    public boolean hasQuestion() {
        return this.currentEntryIndex < this.commandData.getSchema().size();
    }

    /**
     * Returns True if the current input and the next ones aren't mandatory, or if there aren't questions next.
     *
     * @return True if the current input and the next ones aren't mandatory, or if there aren't questions next
     */
    public boolean canEnd() {
        return !hasQuestion() || this.commandData.getSchema().get(this.currentEntryIndex).isOptional();
    }

    /**
     * Adds the value of the response to the ActionMessage, using the correct Map key.
     * Then the menu entry index moves forward.
     *
     * @param value the value to add to the ActionMessage
     */
    private void addValueToActionMessage(String value) {
        this.actionOptions.put(this.commandData.getSchema().get(this.currentEntryIndex).getActionKey(), value);
        this.currentEntryIndex++;
    }

    /**
     * Returns the ActionMessage for the Server. This message is correct only if canEnd is true.
     *
     * @return the ActionMessage for the Server
     */
    public ActionMessage getActionMessage() {
        return new ActionMessage(this.commandData.getActionId(), this.actionOptions);
    }

    /**
     * Returns the number of the wizard previously selected by the player.
     *
     * @return the number of the wizard of the player
     * @throws RuntimeException if the player can't be found
     */
    private Integer getWizardNumber() throws RuntimeException {
        for (ClientTeam clientTeam : this.clientTeams.getTeams()) {
            for (ClientPlayer clientPlayer : clientTeam.getPlayers())
                if (clientPlayer.getPlayerId() == this.playerId) return clientPlayer.getWizard();
        }
        throw new RuntimeException("Can't find this client player in the teams");
    }
}
