package it.polimi.ingsw.view.gui.scene_controllers;

import javafx.scene.Scene;

public abstract class SceneController implements SceneControllerInterface {
    private Scene scene;

    /**
     * Prepares the Scene with the layout to serve it to the Stage without creating it everytime using {@link SceneController#getScene()}.
     */
    public SceneController() {
        this.scene = layout();
    }

    /**
     * Draws the layout in a scene and returns it.
     *
     * @return a new ready-to-display Scene
     */
    protected abstract Scene layout();

    /**
     * Returns the Scene.
     *
     * @return the Scene
     */
    @Override
    public Scene getScene(boolean redrawLayout) {
        if (redrawLayout)
            this.scene = this.layout();
        return this.scene;
    }
}
