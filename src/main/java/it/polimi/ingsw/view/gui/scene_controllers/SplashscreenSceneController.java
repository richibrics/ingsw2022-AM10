package it.polimi.ingsw.view.gui.scene_controllers;

import it.polimi.ingsw.view.gui.exceptions.SceneControllerNotRegisteredException;
import it.polimi.ingsw.view.gui.exceptions.StageNotSetException;
import it.polimi.ingsw.view.gui.GUIConstants;
import it.polimi.ingsw.view.gui.SceneType;
import it.polimi.ingsw.view.gui.StageController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class SplashscreenSceneController extends SceneController {

    /**
     * Draws the layout in a scene and returns it.
     *
     * @return a new ready-to-display Scene
     */
    @Override
    protected Scene layout() {
        VBox layout = new VBox();
        layout.getStyleClass().add(GUIConstants.SCENE_SPLASHSCREEN_STYLE_MAIN_LAYOUT_CLASS);
        layout.setBackground(new Background(new BackgroundImage(new Image(UserFormSceneController.class.getResource(GUIConstants.SCENE_SPLASHSCREEN_BACKGROUND_IMAGE_PATH).toExternalForm(),GUIConstants.SCENE_SPLASHSCREEN_WIDTH,GUIConstants.SCENE_SPLASHSCREEN_HEIGHT,false,true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT)));

        Scene scene = new Scene(layout, GUIConstants.SCENE_SPLASHSCREEN_WIDTH, GUIConstants.SCENE_SPLASHSCREEN_HEIGHT);
        scene.getStylesheets().add(UserFormSceneController.class.getResource(GUIConstants.SCENE_SPLASHSCREEN_STYLESHEET_PATH).toExternalForm());
        return scene;
    }
}
