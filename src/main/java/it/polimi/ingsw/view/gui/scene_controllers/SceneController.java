package it.polimi.ingsw.view.gui.scene_controllers;

import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

public abstract class SceneController implements SceneControllerInterface {
    private Scene scene;
    private AnchorPane root;

    /**
     * Prepares a blank scene.
     */
    public SceneController() {
        this.scene = initialBlankScene();
    }

    /**
     * Draws the layout in a scene and returns it.
     *
     * @return a new ready-to-display Scene
     */
    protected abstract Scene layout();

    protected abstract void updateScene();

    /**
     * Returns the Scene.
     *
     * @return the Scene
     */
    @Override
    public Scene getScene(boolean redrawLayout) {
        if (redrawLayout)
            this.scene = this.layout();
        else
            this.updateScene();

        return this.scene;
    }

    /**
     * Get a scene to show if this scene controller should show a scene that hasn't been layout yet.
     *
     * @return blank scene
     */
    public Scene initialBlankScene() {
        return new Scene(new AnchorPane());
    }

    /**
     * Gets the root of the scene.
     *
     * @return the root of the scene
     */
    public AnchorPane getRoot() {
        return this.root;
    }

    /**
     * Sets the root of the scene.
     *
     * @param root the root of the scene
     */
    public void setRoot (AnchorPane root) {
        this.root = root;
    }
}