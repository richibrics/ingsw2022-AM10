package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.controller.User;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.view.AbstractView;
import it.polimi.ingsw.view.ViewConstants;
import it.polimi.ingsw.view.ViewUtilityFunctions;
import it.polimi.ingsw.view.cli.drawers.*;
import it.polimi.ingsw.view.exceptions.IllegalGameModeException;
import it.polimi.ingsw.view.exceptions.IllegalUserPreferenceException;
import it.polimi.ingsw.view.game_objects.ClientLobby;
import it.polimi.ingsw.view.game_objects.ClientTable;
import it.polimi.ingsw.view.game_objects.ClientTeams;
import it.polimi.ingsw.view.input_management.Command;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

// TODO remove player id passed as argument to methods. Player id can be retrieved using this.getPlayerId()

public class Cli extends AbstractView {

    private final int SPACE_BETWEEN_ELEMENTS = 3;
    private final BufferedReader bufferIn;
    private final BufferedWriter bufferOut;
    private String[][] schoolBoardsTemplate;
    private String[][] characterCardsTemplate;
    private String[][] islandGroupsTemplate;
    private String[][] cloudTilesTemplate;
    private String[][] assistantCardsTemplate;

    public Cli() {
        super();
        this.bufferIn = new BufferedReader(new InputStreamReader(System.in));
        this.bufferOut = new BufferedWriter(new OutputStreamWriter(System.out));
    }

    private void clearTerminal() {
        try {
            this.bufferOut.write(CliConstants.ESCAPE_CODE_TO_CLEAR_TERMINAL);
            this.bufferOut.flush();
        } catch (IOException e) {
            this.clientServerConnection.askToCloseConnectionWithError(e.getMessage());
        }
    }

    @Override
    public void displayStateOfGame(ClientTable clientTable, ClientTeams clientTeams, int playerId) {
        try {
            // Clear terminal
            this.clearTerminal();
            // Create templates if not already created
            if (this.schoolBoardsTemplate == null)
                this.schoolBoardsTemplate = SchoolBoardDrawer.generateTemplate((int) clientTeams.getTeams().stream().flatMap(clientTeam -> clientTeam.getPlayers().stream()).count());
            if (this.characterCardsTemplate == null && this.getUser().getPreference() > 0)
                this.characterCardsTemplate = CharacterCardDrawer.generateTemplate(clientTable.getActiveCharacterCards());
            if (this.cloudTilesTemplate == null)
                this.cloudTilesTemplate = CloudTilesDrawer.generateTemplate(clientTable.getCloudTiles());

            // Fill templates
            SchoolBoardDrawer.fillTemplate(this.schoolBoardsTemplate, clientTable, clientTeams);
            if (this.getUser().getPreference() > 0)
                CharacterCardDrawer.fillTemplate(this.characterCardsTemplate, clientTable.getActiveCharacterCards());
            this.islandGroupsTemplate = IslandGroupsDrawer.generateAndFillTemplate(clientTable.getIslandTiles(), clientTable.getMotherNature());
            CloudTilesDrawer.fillTemplate(this.cloudTilesTemplate, clientTable.getCloudTiles());
            // Determine index of team and of player with id == playerId
            int indexOfTeam = clientTeams.getTeams().indexOf(clientTeams.getTeams().
                    stream().
                    filter(clientTeam -> clientTeam.getPlayers().
                            stream().
                            filter(clientPlayer -> clientPlayer.getPlayerId() == playerId).count() == 1).
                    toList().get(0));

            int indexOfPlayer = clientTeams.getTeams().get(indexOfTeam).getPlayers().indexOf(clientTeams.getTeams().get(indexOfTeam).getPlayers().
                    stream().
                    filter(clientPlayer -> clientPlayer.getPlayerId() == playerId).
                    toList().get(0));

            this.assistantCardsTemplate = AssistantCardDrawer.generateAndFillTemplate(clientTeams, indexOfTeam, indexOfPlayer);

            // Generate template that represents the entire state of the game
            int height;
            int length;
            if (this.getUser().getPreference() > 0) {
                height = Math.max(Math.max(this.schoolBoardsTemplate.length, this.islandGroupsTemplate.length), this.assistantCardsTemplate.length) +
                        Math.max(this.characterCardsTemplate.length, this.cloudTilesTemplate.length) + this.SPACE_BETWEEN_ELEMENTS;
                length = Math.max(this.schoolBoardsTemplate[0].length + this.islandGroupsTemplate[0].length + (this.assistantCardsTemplate.length != 0 ? this.assistantCardsTemplate[0].length : 0) +
                        2 * this.SPACE_BETWEEN_ELEMENTS, this.characterCardsTemplate[0].length + this.cloudTilesTemplate[0].length + this.SPACE_BETWEEN_ELEMENTS);
            }
            else {
                height = Math.max(Math.max(this.schoolBoardsTemplate.length, this.islandGroupsTemplate.length), this.assistantCardsTemplate.length) +
                        this.cloudTilesTemplate.length + this.SPACE_BETWEEN_ELEMENTS;
                length = Math.max(this.schoolBoardsTemplate[0].length + this.islandGroupsTemplate[0].length + (this.assistantCardsTemplate.length != 0 ? this.assistantCardsTemplate[0].length : 0) +
                        2 * this.SPACE_BETWEEN_ELEMENTS, this.cloudTilesTemplate[0].length);
            }

            String[][] template = new String[height][length];

            CliDrawersUtilityFunctions.removeNullAndAddSingleSpace(template);
            // Insert all the elements into the template
            this.positionElementsInTemplate(template);

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
            this.clientServerConnection.askToCloseConnectionWithError(e.getMessage());
        }
    }

    private void positionElementsInTemplate(String[][] template) {
        // Insert school boards, island groups and assistant cards into template
        this.insertTemplateInTemplate(template, this.schoolBoardsTemplate, 0, 0);
        this.insertTemplateInTemplate(template, this.islandGroupsTemplate, 0,
                this.schoolBoardsTemplate[0].length + this.SPACE_BETWEEN_ELEMENTS);
        if (this.assistantCardsTemplate.length != 0)
            this.insertTemplateInTemplate(template, this.assistantCardsTemplate, 0,
                    this.schoolBoardsTemplate[0].length + this.islandGroupsTemplate[0].length + 2 * this.SPACE_BETWEEN_ELEMENTS);

        // Determine starting height of lower section (character cards and clouds)
        int startingHeightOfLowerSection = Math.max(Math.max(this.schoolBoardsTemplate.length, this.islandGroupsTemplate.length), (this.assistantCardsTemplate.length != 0 ? this.assistantCardsTemplate[0].length : 0))
                + this.SPACE_BETWEEN_ELEMENTS;
        this.insertTemplateInTemplate(template, this.cloudTilesTemplate, startingHeightOfLowerSection, 0);
        if (this.getUser().getPreference() > 0)
            this.insertTemplateInTemplate(template, this.characterCardsTemplate, startingHeightOfLowerSection,
                    this.cloudTilesTemplate[0].length + this.SPACE_BETWEEN_ELEMENTS);
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
            String[] gameModeAndPlayers;
            for (Map.Entry<Integer, Integer> entry : clientLobby.getLobbyStatus().entrySet()) {
                gameModeAndPlayers = ViewUtilityFunctions.getGameModeAndNumberOfPlayersFromPreference(entry.getKey());
                this.bufferOut.write(String.format("%d %s waiting for %d-players %s game. \n", entry.getValue(),
                        entry.getValue() == 1 ? "player" : "players", Integer.parseInt(gameModeAndPlayers[0]), gameModeAndPlayers[1]));
                this.bufferOut.flush();
            }
        } catch (IOException | IllegalUserPreferenceException e) {
            this.clientServerConnection.askToCloseConnectionWithError(e.getMessage());
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
            this.clientServerConnection.askToCloseConnectionWithError(e.getMessage());
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
            String username = this.bufferIn.readLine().strip();
            while (username.length() > ViewConstants.MAX_LENGTH_OF_USERNAME || username.length() == 0)  {
                this.showError(String.format("The username exceeds the limit of %d characters or is empty. ", ViewConstants.MAX_LENGTH_OF_USERNAME), false);
                this.bufferOut.write("Please select a new username: ");
                this.bufferOut.flush();
                username = this.bufferIn.readLine().strip();
            }

            // Ask for game mode
            this.bufferOut.write("Select the game mode (b for basic, e for expert): ");
            this.bufferOut.flush();
            String gameMode = this.bufferIn.readLine();
            while (!gameMode.equalsIgnoreCase(CliConstants.EXPERT_GAME) && !gameMode.equalsIgnoreCase(CliConstants.BASIC_GAME)) {
                this.bufferOut.write("The selected game mode is incorrect. Please select a new game mode" +
                        " (b for basic, e for expert): ");
                this.bufferOut.flush();
                gameMode = this.bufferIn.readLine();
            }

            // Ask for preference
            this.bufferOut.write("Select the type of game you want to play (2 for two-players game," +
                    " 3 for three-players game and 4 for four-players game: ");
            this.bufferOut.flush();

            int numberOfPlayers = 0;
            boolean flag = true;
            do {
                try {
                    numberOfPlayers = Integer.parseInt(this.bufferIn.readLine());
                    if (numberOfPlayers != ViewConstants.TWO_PLAYERS_GAME && numberOfPlayers != ViewConstants.THREE_PLAYERS_GAME
                            && numberOfPlayers != ViewConstants.FOUR_PLAYERS_GAME) {
                        this.bufferOut.write("You selected a wrong preference. Please try again.\nSelect preference: ");
                        this.bufferOut.flush();
                    }
                    else
                        flag = false;
                } catch (NumberFormatException e) {
                    this.bufferOut.write("You selected a wrong preference. Please try again.\nSelect preference: ");
                    this.bufferOut.flush();
                }
            } while (flag);

            // Create user and allow clientServerConnection to take the newly created user
            this.user = new User(username, ViewUtilityFunctions.getPreferenceFromGameModeAndClientPreference(gameMode, numberOfPlayers));
            this.setUserReady(true);
        } catch (IOException | IllegalGameModeException e) {
            this.clientServerConnection.askToCloseConnectionWithError(e.getMessage());
        }
    }

    @Override
    public void showError(String message, boolean isCritical) {
        try {
            this.bufferOut.write(message + "\n");
            this.bufferOut.flush();
        } catch (IOException e) {
            this.clientServerConnection.askToCloseConnection();
        }
    }


    @Override
    public void askToChangePreference() {
        try {
            int newNumberOfPlayers;
            String newGameMode;
            boolean wrongPreferenceOrGameMode = false;

            // Ask for new preference
            this.bufferOut.write("\nHave you changed up your mind? We have the solution for you. Select " +
                    "here a new preference and game mode (e.g. 2e for two-players expert game, 3b for three-players basic game...): ");
            this.bufferOut.flush();
            char[] preferenceAndGameMode;
            do {
                boolean preferenceSet = false;
                newNumberOfPlayers = ViewConstants.NO_PLAYERS_GAME;
                newGameMode = CliConstants.GAME_MODE_NOT_USED;

                if (wrongPreferenceOrGameMode) {
                    this.bufferOut.write("\nYou selected a wrong preference or game mode. Please try again.\nSelect preference: ");
                    this.bufferOut.flush();
                }

                wrongPreferenceOrGameMode = true;

                while (!Thread.currentThread().isInterrupted() && !preferenceSet) {
                    if (this.bufferIn.ready()) {
                        preferenceAndGameMode = this.bufferIn.readLine().toCharArray();
                        if (preferenceAndGameMode.length == 2) {
                            newNumberOfPlayers = Integer.parseInt(String.valueOf(preferenceAndGameMode[0]));
                            newGameMode = String.valueOf(preferenceAndGameMode[1]);
                        }
                        preferenceSet = true;
                    }
                }

                if (Thread.currentThread().isInterrupted())
                    return;

            } while ((newNumberOfPlayers != ViewConstants.TWO_PLAYERS_GAME && newNumberOfPlayers != ViewConstants.THREE_PLAYERS_GAME
                    && newNumberOfPlayers != ViewConstants.FOUR_PLAYERS_GAME)
                    || (!newGameMode.equalsIgnoreCase(CliConstants.EXPERT_GAME) && !newGameMode.equalsIgnoreCase(CliConstants.BASIC_GAME)));

            // Update preference
            this.clientServerConnection.changePreference(ViewUtilityFunctions.getPreferenceFromGameModeAndClientPreference(newGameMode, newNumberOfPlayers));
            this.user = new User(this.user.getId(), newNumberOfPlayers);

        } catch (IOException | IllegalGameModeException e) {
            this.clientServerConnection.askToCloseConnectionWithError(e.getMessage());
        }
    }

    @Override
    public void displayWinners(String messageForPlayer) {
        this.clearTerminal();
        try {
            this.bufferOut.write("\n" + messageForPlayer);
            this.bufferOut.flush();

        } catch (IOException e) {
            this.clientServerConnection.askToCloseConnectionWithError(e.getMessage());
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

                    Command command = new Command(actionId, playerId, clientTable, clientTeams);
                    this.bufferOut.write("PS: If you want to cancel the selected action, write \"" + CliConstants.CANCEL_ACTION_INPUT_STRING + "\"");
                    String line;
                    this.bufferOut.write("\n");
                    while (command.hasQuestion() && !actionInputCanceled) {
                        if (command.canEnd()) {
                            this.bufferOut.write("\nWould you like to continue? Y/N: ");
                            this.bufferOut.flush();
                            line = this.bufferIn.readLine();
                            if (line.equalsIgnoreCase(CliConstants.TERMINATE_ACTION_INPUT_OPTIONAL_STRING))
                                break;
                        }

                        try {
                            this.bufferOut.write(command.getCLIMenuMessage() + ": ");
                            this.bufferOut.flush();
                            line = this.bufferIn.readLine();
                            if (line.equalsIgnoreCase(CliConstants.CANCEL_ACTION_INPUT_STRING))
                                actionInputCanceled = true;
                            else
                                command.parseCLIString(line);
                        } catch (IllegalArgumentException e) {
                            this.bufferOut.write("Wrong input. " + e.getMessage() + "\n");
                        }
                    }
                    if (!actionInputCanceled) {
                        this.actionMessage = command.getActionMessage();
                        this.clientServerConnection.setFlagActionMessageIsReady(true);
                        valueSet = true;
                    }
                } catch (NoSuchElementException e) {
                    this.bufferOut.write("Wrong input. " + e.getMessage() + "\n");
                } catch (NumberFormatException e) {
                    this.bufferOut.write("Invalid action number, retry" + "\n");
                }
            } catch (IOException e) {
                this.clientServerConnection.askToCloseConnectionWithError(e.getMessage());
            }
        }
    }

    private String getActionDescriptionFromId(int actionId) {
        return switch (actionId) {
            case ModelConstants.ACTION_ON_SELECTION_OF_WIZARD_ID -> CliConstants.ACTION_ON_SELECTION_OF_WIZARD_ID_DESCRIPTION;
            case ModelConstants.ACTION_ON_SELECTION_OF_ASSISTANTS_CARD_ID -> CliConstants.ACTION_ON_SELECTION_OF_ASSISTANTS_CARD_ID_DESCRIPTION;
            case ModelConstants.ACTION_ON_SELECTION_OF_CHARACTER_CARD_ID -> CliConstants.ACTION_ON_SELECTION_OF_CHARACTER_CARD_ID_DESCRIPTION;
            case ModelConstants.ACTION_MOVE_STUDENTS_FROM_ENTRANCE_ID -> CliConstants.ACTION_MOVE_STUDENTS_FROM_ENTRANCE_ID_DESCRIPTION;
            case ModelConstants.ACTION_MOVE_MOTHER_NATURE_ID -> CliConstants.ACTION_MOVE_MOTHER_NATURE_ID_DESCRIPTION;
            case ModelConstants.ACTION_FROM_CLOUD_TILE_TO_ENTRANCE_ID -> CliConstants.ACTION_FROM_CLOUD_TILE_TO_ENTRANCE_ID_DESCRIPTION;
            default -> null;
        };
    }

    @Override
    public boolean isCriticalErrorOpen() {
        return false;
    }
}