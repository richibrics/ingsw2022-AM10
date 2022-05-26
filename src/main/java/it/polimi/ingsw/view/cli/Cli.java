package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.controller.User;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.network.client.ClientServerConnection;
import it.polimi.ingsw.network.messages.ActionMessage;
import it.polimi.ingsw.view.ViewInterface;
import it.polimi.ingsw.view.cli.drawers.*;
import it.polimi.ingsw.view.game_objects.ClientLobby;
import it.polimi.ingsw.view.game_objects.ClientTable;
import it.polimi.ingsw.view.game_objects.ClientTeams;
import it.polimi.ingsw.view.input_management.Command;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.NoSuchElementException;

public class Cli implements ViewInterface {

    private final int SPACE_BETWEEN_ELEMENTS = 3;
    private final BufferedReader bufferIn;
    private final BufferedWriter bufferOut;
    private final Object syncObject1;
    private final Object syncObject2;
    private String[][] schoolBoardsTemplate;
    private String[][] characterCardsTemplate;
    private String[][] islandGroupsTemplate;
    private String[][] cloudTilesTemplate;
    private ClientServerConnection clientServerConnection;
    private User user;
    private ActionMessage actionMessage;
    private boolean flagUserReady;
    private Command command;


    public Cli() {
        this.bufferIn = new BufferedReader(new InputStreamReader(System.in));
        this.bufferOut = new BufferedWriter(new OutputStreamWriter(System.out));
        this.flagUserReady = false;
        this.syncObject1 = new Object();
        this.syncObject2 = new Object();
    }

    private void clearTerminal() {
        try {
            this.bufferOut.write(CliConstants.ESCAPE_CODE_TO_CLEAR_TERMINAL);
            this.bufferOut.flush();
        } catch (IOException e) {
            this.clientServerConnection.askToCloseConnection();
        }
    }

    @Override
    public void displayStateOfGame(ClientTable clientTable, ClientTeams clientTeams) {
        try {
            // Clear terminal
            this.clearTerminal();
            // Create templates if not already created
            if (this.schoolBoardsTemplate == null)
                this.schoolBoardsTemplate = SchoolBoardDrawer.generateTemplate((int) clientTeams.getTeams().stream().flatMap(clientTeam -> clientTeam.getPlayers().stream()).count());
            if (this.characterCardsTemplate == null)
                this.characterCardsTemplate = CharacterCardDrawer.generateTemplate(clientTable.getActiveCharacterCards());
            if (this.cloudTilesTemplate == null)
                this.cloudTilesTemplate = CloudTilesDrawer.generateTemplate(clientTable.getCloudTiles());

            // Fill templates
            SchoolBoardDrawer.fillTemplate(this.schoolBoardsTemplate, clientTable, clientTeams);
            CharacterCardDrawer.fillTemplate(this.characterCardsTemplate, clientTable.getActiveCharacterCards());
            this.islandGroupsTemplate = IslandGroupsDrawer.generateAndFillTemplate(clientTable.getIslandTiles(), clientTable.getMotherNature());
            CloudTilesDrawer.fillTemplate(this.cloudTilesTemplate, clientTable.getCloudTiles());

            // Generate template that represents the entire state of the game
            int height = Math.max(this.schoolBoardsTemplate.length, this.islandGroupsTemplate.length) +
                    Math.max(this.characterCardsTemplate.length, this.cloudTilesTemplate.length) + this.SPACE_BETWEEN_ELEMENTS;
            int length = Math.max(this.schoolBoardsTemplate[0].length + this.islandGroupsTemplate[0].length,
                    this.characterCardsTemplate[0].length + this.cloudTilesTemplate[0].length) + this.SPACE_BETWEEN_ELEMENTS;

            String[][] template = new String[height][length];

            UtilityFunctions.removeNullAndAddSingleSpace(template);
            // Insert all the elements into the template
            this.positionElementsInTemplate(template);

            // Clear the content of the cli
            //this.clearTerminal();

            // Print the state of the game
            for (int i = 0; i < template.length; i++) {
                for (int j = 0; j < template[i].length; j++) {
                    this.bufferOut.write(template[i][j]);
                }
                this.bufferOut.write("\n");
                this.bufferOut.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.clientServerConnection.askToCloseConnection();
        }
    }

    private void positionElementsInTemplate(String[][] template) {
        // Insert school boards and island groups into template
        this.insertTemplateInTemplate(template, this.schoolBoardsTemplate, 0, 0);
        this.insertTemplateInTemplate(template, this.islandGroupsTemplate, 0,
                this.schoolBoardsTemplate[0].length + this.SPACE_BETWEEN_ELEMENTS);

        // Determine starting height of lower section (character cards and clouds)
        int startingHeightOfLowerSection;
        if (this.schoolBoardsTemplate.length >= this.islandGroupsTemplate.length)
            startingHeightOfLowerSection = this.schoolBoardsTemplate.length + this.SPACE_BETWEEN_ELEMENTS;
        else
            startingHeightOfLowerSection = this.islandGroupsTemplate.length + this.SPACE_BETWEEN_ELEMENTS;
        this.insertTemplateInTemplate(template, this.characterCardsTemplate, startingHeightOfLowerSection, 0);
        this.insertTemplateInTemplate(template, this.cloudTilesTemplate, startingHeightOfLowerSection,
                this.characterCardsTemplate[0].length + this.SPACE_BETWEEN_ELEMENTS);
    }

    private void insertTemplateInTemplate(String[][] generalTemplate, String[][] template, int startingHeight, int startingLength) {
        for (int i = 0; i < template.length; i++)
            for (int j = 0; j < template[0].length; j++)
                generalTemplate[i + startingHeight][j + startingLength] = template[i][j];
    }

    @Override
    public void displayLobby(ClientLobby clientLobby) {
        this.clearTerminal();
        try {
            this.bufferOut.write("This is the state of the lobby: \n");
            this.bufferOut.flush();
            for (Map.Entry<Integer, Integer> entry : clientLobby.getLobbyStatus().entrySet()) {
                this.bufferOut.write(String.format("%d %s waiting for %d-players game. \n", entry.getValue(),
                        entry.getValue() == 1 ? "player" : "players", entry.getKey()));
                this.bufferOut.flush();
            }
        } catch (IOException e) {
            this.clientServerConnection.askToCloseConnection();
        }
    }

    @Override
    public void displayActions(ArrayList<Integer> possibleActions) {
        clientServerConnection.setFlagActionMessageIsReady(false);
        try {
            this.bufferOut.write("\nAvailable actions:");

            // For each action in the list of possible actions display a description

            for (int actionId : possibleActions) {
                this.bufferOut.write("\n" + actionId + ": " + this.getActionDescriptionFromId(actionId));
            }
            this.bufferOut.flush();
        } catch (IOException e) {
            this.clientServerConnection.askToCloseConnection();
        }
    }

    @Override
    public void askForUser() {
        this.clearTerminal();
        try {
            // Message of presentation
            this.bufferOut.write("Welcome to Eriantys by Cranio Creations! Enjoy the game!\n");
            // Ask client for username. Max length of DrawersConstant.SCHOOL_BOARD_LENGTH
            this.bufferOut.write("Select a username: ");
            this.bufferOut.flush();
            String username = this.bufferIn.readLine();
            while (username.length() > DrawersConstant.SCHOOL_BOARD_LENGTH) {
                this.showError(String.format("The username exceeds the limit of %d characters. ",DrawersConstant.SCHOOL_BOARD_LENGTH));
                this.bufferOut.write("Please select a new username: ");
                this.bufferOut.flush();
                username = this.bufferIn.readLine();
            }

            // Ask for preference
            this.bufferOut.write("Select the type of game you want to play (2 for two-players game," +
                    " 3 for three-players game and 4 for four-players game: ");
            this.bufferOut.flush();
            int preference = Integer.valueOf(this.bufferIn.readLine());
            while (preference != 2 && preference != 3 && preference != 4) {
                this.bufferOut.write("You selected a wrong preference. Please try again.\nSelect preference: ");
                this.bufferOut.flush();
                preference = Integer.valueOf(this.bufferIn.readLine());
            }
            // Create user and allow clientServerConnection to take the newly created user
            this.user = new User(username, preference);
            this.setUserReady(true);
        } catch (IOException e) {
            this.clientServerConnection.askToCloseConnection();
        }
    }

    @Override
    public User getUser() {
        return new User(this.user.getId(), this.user.getPreference());
    }

    @Override
    public boolean userReady() {
        synchronized (this.syncObject1) {
            return this.flagUserReady;
        }
    }

    @Override
    public void showError(String message) {
        try {
            this.bufferOut.write(message);
            this.bufferOut.flush();
        } catch (IOException e) {
            this.clientServerConnection.askToCloseConnection();
        }
    }

    @Override
    public void setUserReady(boolean userReady) {
        synchronized (syncObject1) {
            this.flagUserReady = userReady;
        }
    }

    @Override
    public void askToChangePreference() {
        try {
            Integer newPreference = null;
            // Ask for new preference
            this.bufferOut.write("\nHave you changed up your mind? We have the solution for you. Select " +
                    "here a new preference (2 for two-players game, 3 for three-players game and 4 for four-players game: ");
            this.bufferOut.flush();
            do {
                boolean preferenceSet = false;
                if (newPreference != null) {
                    this.bufferOut.write("\nYou selected a wrong preference. Please try again.\nSelect preference: ");
                    this.bufferOut.flush();
                }

                while (!Thread.currentThread().isInterrupted() && !preferenceSet) {
                    if (this.bufferIn.ready()) {
                        newPreference = Integer.valueOf(this.bufferIn.readLine());
                        preferenceSet = true;
                    }
                }

                if (Thread.currentThread().isInterrupted())
                    return;

            } while (newPreference != 2 && newPreference != 3 && newPreference != 4);
            // Update preference
            this.clientServerConnection.changePreference(newPreference);

        } catch (IOException e) {
            this.clientServerConnection.askToCloseConnection();
        }
    }

    @Override
    public void displayWinners(String messageForPlayer) {
        this.clearTerminal();
        try {
            this.bufferOut.write("\n" + messageForPlayer);
            this.bufferOut.flush();

        } catch (IOException e) {
        }
    }

    @Override
    public void showMenu(ClientTable clientTable, ClientTeams clientTeams, int playerId, ArrayList<Integer> possibleActions) {
        boolean valueSet = false;
        boolean actionInputCanceled=false;
        while (!valueSet) {
            if(actionInputCanceled) // If the input was canceled, re-show the possible actions
                this.displayActions(possibleActions);

            actionInputCanceled = false;
            try { // IOException checker, the inside Try Catch is handled by Buffer which throws an exception too.
                try {
                    this.bufferOut.write("\n\nSelect the id of the action: ");
                    this.bufferOut.flush();
                    int actionId = Integer.parseInt(this.bufferIn.readLine());

                    // Check valid action
                    if (!possibleActions.contains(actionId)) {
                        this.bufferOut.write("Action not available, retry\n");
                        continue;
                    }

                    this.command = new Command(actionId, playerId, clientTable, clientTeams);
                    this.bufferOut.write("PS: If you want to cancel the selected action, write \"" + CliConstants.CANCEL_ACTION_INPUT_STRING + "\"");
                    String line;
                    this.bufferOut.write("\n");
                    while (this.command.hasQuestion() && !actionInputCanceled) {
                        if (this.command.canEnd()) {
                            this.bufferOut.write("\nWould you like to continue? Y/N: ");
                            this.bufferOut.flush();
                            line = this.bufferIn.readLine();
                            if (line.equals(CliConstants.TERMINATE_ACTION_INPUT_OPTIONAL_STRING))
                                break;
                        }

                        try {
                            this.bufferOut.write(this.command.getCLIMenuMessage() + ": ");
                            this.bufferOut.flush();
                            line = this.bufferIn.readLine();
                            if (line.equals(CliConstants.CANCEL_ACTION_INPUT_STRING))
                                actionInputCanceled = true;
                            else
                                this.command.parseCLIString(line);
                        } catch (IllegalArgumentException e) {
                            this.bufferOut.write("Wrong input. " + e.getMessage() + "\n");
                        }
                    }
                    if (!actionInputCanceled) {
                        this.actionMessage = this.command.getActionMessage();
                        this.clientServerConnection.setFlagActionMessageIsReady(true);
                        valueSet = true;
                    }
                } catch (NoSuchElementException e) {
                    this.bufferOut.write("Wrong input. " + e.getMessage() + "\n");
                } catch (NumberFormatException e) {
                    this.bufferOut.write("Invalid action number, retry" + "\n");
                }
            } catch (IOException e) {
                this.clientServerConnection.askToCloseConnection();
                System.out.println("IOException: " + e.getMessage() + "\n");
            }
        }
    }

    @Override
    public ActionMessage getActionMessage() {
        return new ActionMessage(this.actionMessage.getActionId(), this.actionMessage.getOptions());
    }

    @Override
    public void setClientServerConnection(ClientServerConnection clientServerConnection) {
        this.clientServerConnection = clientServerConnection;
    }

    private String getActionDescriptionFromId(int actionId) {
        switch (actionId) {
            case ModelConstants.ACTION_ON_SELECTION_OF_WIZARD_ID:
                return CliConstants.ACTION_ON_SELECTION_OF_WIZARD_ID_DESCRIPTION;
            case ModelConstants.ACTION_ON_SELECTION_OF_ASSISTANTS_CARD_ID:
                return CliConstants.ACTION_ON_SELECTION_OF_ASSISTANTS_CARD_ID_DESCRIPTION;
            case ModelConstants.ACTION_ON_SELECTION_OF_CHARACTER_CARD_ID:
                return CliConstants.ACTION_ON_SELECTION_OF_CHARACTER_CARD_ID_DESCRIPTION;
            case ModelConstants.ACTION_MOVE_STUDENTS_FROM_ENTRANCE_ID:
                return CliConstants.ACTION_MOVE_STUDENTS_FROM_ENTRANCE_ID_DESCRIPTION;
            case ModelConstants.ACTION_MOVE_MOTHER_NATURE_ID:
                return CliConstants.ACTION_MOVE_MOTHER_NATURE_ID_DESCRIPTION;
            case ModelConstants.ACTION_FROM_CLOUD_TILE_TO_ENTRANCE_ID:
                return CliConstants.ACTION_FROM_CLOUD_TILE_TO_ENTRANCE_ID_DESCRIPTION;
            default:
                return null;
        }
    }
}