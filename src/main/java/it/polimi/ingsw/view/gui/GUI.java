package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.controller.User;
import it.polimi.ingsw.view.AbstractView;
import it.polimi.ingsw.view.game_objects.ClientLobby;
import it.polimi.ingsw.view.game_objects.ClientTable;
import it.polimi.ingsw.view.game_objects.ClientTeams;
import it.polimi.ingsw.view.gui.exceptions.SceneControllerNotRegisteredException;
import it.polimi.ingsw.view.gui.exceptions.StageNotSetException;
import javafx.application.Platform;

import java.util.ArrayList;

import static javafx.application.Application.launch;

public class GUI extends AbstractView {

    /**
     * When GUI starts, show the Splashscreen
     */
    public GUI() {
        super();
        new Thread(() -> launch(LaunchGUI.class)).start();  // Start the GUI Thread
    }

    @Override
    public void displayStateOfGame(ClientTable clientTable, ClientTeams clientTeams, int playerId) {

    }

    @Override
    public void displayLobby(ClientLobby clientLobby) {
        // Store lobby
        StageController.getStageController().setClientLobby(clientLobby);
        // Show lobby
        Platform.runLater(() -> {
            try {
                StageController.getStageController().showScene(SceneType.LOBBY_SCENE, true);
            } catch (SceneControllerNotRegisteredException | StageNotSetException e) {
                this.handleInternalException(e);
            }
        });
    }

    @Override
    public void displayActions(ArrayList<Integer> possibleActions) {

    }

    @Override
    public void askForUser() {
        // Wait until StageController is ready
        this.waitForGuiToStart();

        // Stage is ready: can show the user window (use the GUI thread !)
        Platform.runLater(() -> {
            try {
                StageController.getStageController().showScene(SceneType.USER_FORM_SCENE, true);
            } catch (SceneControllerNotRegisteredException | StageNotSetException e) {
                this.handleInternalException(e);
            }
        });
    }

    @Override
    public void askToChangePreference() {
        // Done with displayLobby
    }

    @Override
    public void displayWinners(String messageForPlayer) {

    }

    @Override
    public void showMenu(ClientTable clientTable, ClientTeams clientTeams, int playerId, ArrayList<Integer> possibleActions) {

    }


    @Override
    public void showError(String message) {

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
        e.printStackTrace();
        this.clientServerConnection.askToCloseConnection();
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
}
