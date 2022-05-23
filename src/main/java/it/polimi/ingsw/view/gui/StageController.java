package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.view.gui.exceptions.CurrentSceneControllerNotSetException;
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

    public StageController() {
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
     * @param sceneType the type of Scene that will be drawn
     * @throws SceneControllerNotRegisteredException if the requested Scene could not be found
     */
    public void showScene(SceneType sceneType) throws SceneControllerNotRegisteredException, StageNotSetException {
        if (this.stage == null)
            throw new StageNotSetException("The Stage was not set in the StageController.");
        SceneControllerInterface nextSceneController = sceneControllers.get(sceneType);
        if (nextSceneController == null)
            throw new SceneControllerNotRegisteredException("The requested SceneController could not be found (" + sceneType.toString() + ").");
        stage.setScene(nextSceneController.getScene(false));
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
}
