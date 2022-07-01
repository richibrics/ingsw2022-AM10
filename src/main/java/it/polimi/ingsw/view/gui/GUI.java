package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.controller.User;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.network.client.ClientServerConnection;
import it.polimi.ingsw.view.AbstractView;
import it.polimi.ingsw.view.game_objects.ClientLobby;
import it.polimi.ingsw.view.game_objects.ClientTable;
import it.polimi.ingsw.view.game_objects.ClientTeams;
import it.polimi.ingsw.view.gui.exceptions.SceneControllerNotRegisteredException;
import it.polimi.ingsw.view.gui.exceptions.StageNotSetException;
import it.polimi.ingsw.view.input_management.Command;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static javafx.application.Application.launch;

public class GUI extends AbstractView {

    boolean isErrorOpen;
    Map<Integer, Command> availableCommands;
    boolean firstTime;

    /**
     * When GUI starts, show the Splashscreen
     */
    public GUI() {
        super();
        this.isErrorOpen = false;
        this.firstTime = true;
        this.availableCommands = new HashMap<>();
        StageController.getStageController().setGuiView(this);
        new Thread(() -> launch(LaunchGUI.class)).start();  // Start the GUI Thread
    }

    @Override
    public void displayStateOfGame(ClientTable clientTable, ClientTeams clientTeams, int playerId) {
        // Set client table, client teams and player id of the client
        StageController.getStageController().setClientTable(clientTable);
        StageController.getStageController().setClientTeams(clientTeams);
        this.setPlayerId(playerId);

        final boolean finalFirstTime = firstTime; // Copy of the value of firstTime (for the async call of runLater)
        Platform.runLater(() -> {
            // Draw deck only the first time
            StageController.getStageController().getSceneControllers(SceneType.DECK_SCENE).getScene(finalFirstTime);
            // Draw school board scene
            StageController.getStageController().getSceneControllers(SceneType.SCHOOL_BOARD_SCENE).getScene(true);
        });

        // Show table scene
        if(firstTime)
            this.showScene(SceneType.TABLE_SCENE, true);
        else
            Platform.runLater(() -> {
                try {
                    StageController.getStageController().updateScene();
                } catch (SceneControllerNotRegisteredException | StageNotSetException e) {
                    this.handleInternalException(e);
                }
            });
        firstTime = false;
    }

    @Override
    public void displayLobby(ClientLobby clientLobby) {
        // Store lobby
        StageController.getStageController().setClientLobby(clientLobby);
        // Show lobby
        this.showScene(SceneType.LOBBY_SCENE, true);
    }

    @Override
    public void displayActions(ArrayList<Integer> possibleActions) {
        // Not displayed in gui
    }

    @Override
    public void askForUser() {
        // Wait until StageController is ready
        this.waitForGuiToStart();
        // Close connection when the client closes the window
        StageController.getStageController().getStage().setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                StageController.getStageController().getGuiView().getClientServerConnection().askToCloseConnection();
            }
        });

        // Stage is ready: can show the user window (use the GUI thread !)
        this.showScene(SceneType.USER_FORM_SCENE, true);
    }

    @Override
    public void askToChangePreference() {
        // Done with displayLobby
    }

    @Override
    public void displayWinners(String messageForPlayer) {
        Platform.runLater(() -> {
            this.isErrorOpen = true;

            Dialog dialog = new Dialog();
            //Set the title
            dialog.setTitle("Game result");
            //Set the content of the dialog
            dialog.setContentText(messageForPlayer);
            //Add button to the dialog pane
            dialog.getDialogPane().getButtonTypes().add(new ButtonType("OK", ButtonBar.ButtonData.LEFT));
            dialog.getDialogPane().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/endGame.css")).toExternalForm());
            dialog.getDialogPane().setMinHeight(GUIConstants.MIN_HEIGHT_OF_END_GAME_DIALOG);
            dialog.getDialogPane().setMinWidth(GUIConstants.MIN_WIDTH_OF_END_GAME_DIALOG);
            Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("/images/end.png"));
            dialog.showAndWait();

            this.isErrorOpen = false;
        });
    }

    @Override
    public void showMenu(ClientTable clientTable, ClientTeams clientTeams, int playerId, ArrayList<Integer> possibleActions) {
        this.availableCommands.clear();
        for (Integer id : possibleActions) {
            this.availableCommands.put(id, new Command(id, this.getPlayerId(), clientTable, clientTeams));
        }

        // Case 1: the action is the selection of the wizard
        if (possibleActions.size() == 1 && possibleActions.get(0) == ModelConstants.ACTION_ON_SELECTION_OF_WIZARD_ID) {
            // Show wizard scene, then switch back to table scene
            this.showScene(SceneType.WIZARD_SCENE, true);
        }
    }

    @Override
    public void updateWhenCurrentPlayerChanges(int currentPlayer) {
        Platform.runLater(()->{
            AnchorPane rootOfTableScene = StageController.getStageController().getSceneControllers(SceneType.TABLE_SCENE).getRoot();

            if (rootOfTableScene.getChildren().stream().anyMatch(node -> node.getId().equals("turn"))) {
                rootOfTableScene.getChildren().remove(rootOfTableScene.getChildren().stream().filter(node1 -> node1.getId().equals("turn")).toList().get(0));
            }

            Label label = new Label();

            // Set properties
            label.setId("turn");

            if (currentPlayer == StageController.getStageController().getGuiView().getPlayerId())
                label.setText(GUIConstants.CLIENT_TURN);
            else
                label.setText(GUIConstants.NOT_CLIENT_TURN);

            label.setPrefSize(GUIConstants.WIDTH_OF_TURN, GUIConstants.HEIGHT_OF_TURN);
            label.setLayoutX(GUIConstants.LAYOUT_X_OF_TURN);
            label.setLayoutY(GUIConstants.LAYOUT_Y_OF_TURN);
            label.setFont(Font.font(GUIConstants.FONT, FontPosture.REGULAR, GUIConstants.FONT_SIZE_TURN));
            label.setAlignment(Pos.CENTER_LEFT);

            rootOfTableScene.getChildren().add(label);
        });
    }

    /**
     * Shows the error message in a Dialog.
     * @param message the message of the error
     * @param isCritical if the error leads to app close
     */
    @Override
    public void showError(String message, boolean isCritical) {
        if (!this.isErrorOpen)
            this.isErrorOpen = isCritical; // Set as open and as only openable only if critical

        Platform.runLater(() -> {
            Dialog dialog = new Dialog();
            //Set the title
            dialog.setTitle("Error");
            //Set the content of the dialog
            dialog.setContentText(message);
            //Add button to the dialog pane
            dialog.getDialogPane().getButtonTypes().add(new ButtonType("OK", ButtonBar.ButtonData.LEFT));
            dialog.getDialogPane().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/error.css")).toExternalForm());
            Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("/images/redXMark.png"));
            dialog.showAndWait();

            if (isCritical) {
                this.isErrorOpen = false;
            }
        });
    }

    /**
     * Shows an alert with the message
     * @param message the message of the alert
     */
    @Override
    public void showInfo(String message) {
        Platform.runLater(()->{
            Dialog dialog = new Dialog();
            //Set the title
            dialog.setTitle("Match info");
            //Set the content of the dialog
            dialog.setContentText(message);
            //Add button to the dialog pane
            dialog.getDialogPane().getButtonTypes().add(new ButtonType("OK", ButtonBar.ButtonData.LEFT));
            dialog.getDialogPane().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/hint.css")).toExternalForm());
            Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
            // Add a custom icon.
            stage.getIcons().add(new Image(GUIConstants.SCENE_TABLE_BULB_IMAGE_PATH));
            dialog.show();
        });
    }

    /**
     * Blocks until StageController is ready. Sets also the GUI object in the stage controller.
     */
    private void waitForGuiToStart() {
        while (!StageController.getStageController().isReady()) {
            try {
                Thread.sleep(GUIConstants.SLEEP_TIME_WAIT_FOR_STAGECONTROLLER_TO_BE_READY_IN_MILLISECONDS);
            } catch (InterruptedException e) {
                this.handleInternalException(e);
            }
        }
        StageController.getStageController().setGuiView(this);
    }

    /**
     * Handles the Exception occurred in GUI or in a Scene
     *
     * @param e the caught Exception
     */
    public void handleInternalException(Exception e) {
        this.clientServerConnection.askToCloseConnectionWithError(e.getMessage());
    }

    // Methods for GUI callback

    /**
     * Receives the username and the number of players from the GUI and makes the user available to the ClientServerConnection.
     *
     * @param username      the user username
     * @param playersNumber the user preference
     */
    public void handleSendUser(String username, int playersNumber) {
        // Create user and allow clientServerConnection to take the newly created user
        this.user = new User(username, playersNumber);
        this.setUserReady(true);
    }

    /**
     * Receives the new number of players from the GUI and notifies the ClientServerConnection.
     *
     * @param playersNumber the user preference
     */
    public void handleChangePreference(int playersNumber) {
        this.clientServerConnection.changePreference(playersNumber);
        this.user = new User(this.user.getId(), playersNumber);
    }

    /**
     * Changes the GUI scene using the GUI thread.
     *
     * @param sceneType  the type of Scene that will be drawn
     * @param drawLayout true if the layout hasn't been created yet (first time scene is being shown) or if it has to be updated
     */
    private void showScene(SceneType sceneType, boolean drawLayout) {
        Platform.runLater(() -> {
            try {
                StageController.getStageController().showScene(sceneType, drawLayout);
            } catch (SceneControllerNotRegisteredException | StageNotSetException e) {
                this.handleInternalException(e);
            }
        });
    }

    /**
     * Returns the commands of the available actions.
     *
     * @return the commands of the available actions
     */
    public Map<Integer, Command> getAvailableCommands() {
        return availableCommands;
    }

    /**
     * Returns a string that explains what the user can do, using the possible actions commands.
     *
     * @return the string with actions hint
     */
    public String getAvailableActionsHint() {
        if (this.availableCommands.size() != 0)
            return this.availableCommands.values().stream().map(Command::getGUIMenuMessage).collect(Collectors.joining("\nor\n"));
        else
            return GUIConstants.NO_COMMANDS_AVAILABLE_HINT;
    }

    /**
     * Returns true if an error is open; for example, if an error was displayed and the user hasn't still
     * closed it, this returns true.
     * @return true if an error is open.
     */
    public boolean isCriticalErrorOpen() {
        return isErrorOpen;
    }

    public ClientServerConnection getClientServerConnection() {
        return this.clientServerConnection;
    }
}