package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.view.game_objects.ClientLobby;
import it.polimi.ingsw.view.game_objects.ClientRound;
import it.polimi.ingsw.view.game_objects.ClientTable;
import it.polimi.ingsw.view.game_objects.ClientTeams;
import it.polimi.ingsw.view.gui.exceptions.CurrentSceneControllerNotSetException;
import it.polimi.ingsw.view.gui.exceptions.GuiViewNotSet;
import it.polimi.ingsw.view.gui.exceptions.SceneControllerNotRegisteredException;
import it.polimi.ingsw.view.gui.exceptions.StageNotSetException;
import it.polimi.ingsw.view.gui.scene_controllers.SceneController;
import it.polimi.ingsw.view.gui.scene_controllers.SceneControllerInterface;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

/**
 * Singleton, should be called using {@code StageController.getStageController}
 */
public class StageController {
    private static StageController stageController;
    private Stage stage;
    private SceneControllerInterface currentSceneController;
    private Map<SceneType, SceneControllerInterface> sceneControllers;
    private boolean isReady;
    private GUI guiView;

    // Objects to draw, received from the network
    private ClientTable clientTable;
    private ClientTeams clientTeams;
    private ClientRound clientRound;
    private ClientLobby clientLobby;

    public StageController() {
        isReady = false;
        sceneControllers = new HashMap<>();
        stage = null;
        currentSceneController = null;
    }

    /**
     * Returns the instance of the StageController.
     *
     * @return the instance of StageController
     */
    public static StageController getStageController() {
        if (stageController == null)
            stageController = new StageController();
        return stageController;
    }

    /**
     * Registers the scene controller in the scene, ready to be shown when requested using the type.
     *
     * @param sceneType       the type of sceneController that is going to be registered
     * @param sceneController the sceneController that is going to be registered
     */
    public void registerSceneController(SceneType sceneType, SceneController sceneController) {
        sceneControllers.put(sceneType, sceneController);
    }

    /**
     * Shows the Scene in the Stage.
     *
     * @param sceneType  the type of Scene that will be drawn
     * @param drawLayout true if the layout hasn't been created yet (first time scene is being shown) or if it has to be updated
     * @throws SceneControllerNotRegisteredException if the requested Scene could not be found
     */
    public void showScene(SceneType sceneType, boolean drawLayout) throws SceneControllerNotRegisteredException, StageNotSetException {
        if (this.stage == null)
            throw new StageNotSetException("The Stage was not set in the StageController.");
        SceneControllerInterface nextSceneController = sceneControllers.get(sceneType);

        // TODO do something to handle update of deck, table and school board

        if (nextSceneController == null)
            throw new SceneControllerNotRegisteredException("The requested SceneController could not be found (" + sceneType.toString() + ").");
        stage.setScene(nextSceneController.getScene(drawLayout));
        this.currentSceneController = nextSceneController;
        stage.show();
    }

    /**
     * Draws the current scene, re-rendering the layout.
     *
     * @throws CurrentSceneControllerNotSetException
     */
    public void updateScene() throws CurrentSceneControllerNotSetException {
        if (this.currentSceneController == null)
            throw new CurrentSceneControllerNotSetException("No SceneController set in the StageController");
        stage.setScene(this.currentSceneController.getScene(true));
        stage.show();
    }

    /**
     * Sets the Stage which is the main object of the graphical window.
     *
     * @param stage the Stage in which the Scene will be shown
     */
    public void setStage(Stage stage) {
        this.stage = stage;
        this.prepareStage();
    }

    /**
     * Sets properties to the Stage before showing the Scenes.
     */
    private void prepareStage() {
        stage.setResizable(false);
    }

    /**
     * Returns true if StageController has the Stage set and prepared.
     *
     * @return true if StageController has the Stage set and prepared.
     */
    public boolean isReady() {
        return isReady;
    }

    /**
     * Sets the isReady parameter. When isReady is true, the GUI client can start.
     *
     * @param ready
     */
    public void setReady(boolean ready) {
        isReady = ready;
    }

    /**
     * Returns the ViewInterface GUI.
     *
     * @return the ViewInterface GUI
     * @throws GuiViewNotSet if GUI was not set
     */
    public GUI getGuiView() throws GuiViewNotSet {
        if (this.guiView == null)
            throw new GuiViewNotSet();
        return guiView;
    }

    /**
     * Sets the ViewInterface GUI.
     *
     * @param guiView the GUI to set
     */
    public void setGuiView(GUI guiView) {
        this.guiView = guiView;
    }

    /**
     * Gets the Client Table set from the network.
     *
     * @return the Client Table set from the network.
     */
    public ClientTable getClientTable() {
        return clientTable;
    }

    /**
     * Sets the Client Table, received from the network.
     *
     * @param clientTable the Client Table, received from the network
     */
    public void setClientTable(ClientTable clientTable) {
        this.clientTable = clientTable;
    }

    /**
     * Gets the Client Teams set from the network.
     *
     * @return the Client Teams set from the network.
     */
    public ClientTeams getClientTeams() {
        return clientTeams;
    }

    /**
     * Sets the Client Teams, received from the network.
     *
     * @param clientTeams the Client Teams, received from the network
     */
    public void setClientTeams(ClientTeams clientTeams) {
        this.clientTeams = clientTeams;
    }

    /**
     * Gets the Client Round set from the network.
     *
     * @return the Client Round set from the network.
     */
    public ClientRound getClientRound() {
        return clientRound;
    }

    /**
     * Sets the Client Round, received from the network.
     *
     * @param clientRound the Client Round, received from the network
     */
    public void setClientRound(ClientRound clientRound) {
        this.clientRound = clientRound;
    }

    /**
     * Gets the Client Lobby set from the network.
     *
     * @return the Client Lobby set from the network.
     */
    public ClientLobby getClientLobby() {
        return clientLobby;
    }

    /**
     * Sets the Client Lobby, received from the network.
     *
     * @param clientLobby the Client Lobby, received from the network
     */
    public void setClientLobby(ClientLobby clientLobby) {
        this.clientLobby = clientLobby;
    }

    public SceneControllerInterface getSceneControllers(SceneType type) {
        return this.sceneControllers.get(type);
    }


    /**
     * Handles the Exception occurred in a Scene.
     * If it can't handle it closing the socket, closes directly the Application
     *
     * @param e the caught Exception
     */
    public void handleInternalException(Exception e) {
        e.printStackTrace();
        try {
            this.getGuiView().handleInternalException(e);
        } catch (GuiViewNotSet ex) {
            ex.printStackTrace();
            System.exit(0);
        }
    }
}
