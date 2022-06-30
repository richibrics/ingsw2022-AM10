package it.polimi.ingsw.view.gui.scene_controllers;

import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

public interface SceneControllerInterface {
    Scene getScene(boolean redrawLayout);

    AnchorPane getRoot();

    void setRoot(AnchorPane root);
}
