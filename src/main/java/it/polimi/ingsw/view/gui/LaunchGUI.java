package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.view.gui.scene_controllers.LobbySceneController;
import it.polimi.ingsw.view.gui.scene_controllers.SplashscreenSceneController;
import it.polimi.ingsw.view.gui.scene_controllers.UserFormSceneController;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.List;

/**
 * Here we manage the GUI thread, changing/updating scenes when an update is needed.
 */
public class LaunchGUI extends Application {
    private boolean sceneChangeRequested;
    private boolean sceneUpdateRequested;
    private SceneType sceneChangeNextScene;

    @Override
    public void start(Stage primaryStage) throws Exception {
        sceneChangeRequested = false;
        sceneUpdateRequested = false;

        System.out.println("Starting GUI...");
        List<String> parameters = getParameters().getRaw();
        primaryStage.setTitle("Eriantys");
        StageController.getStageController().setStage(primaryStage);
        StageController.getStageController().registerSceneController(SceneType.SPLASHSCREEN_SCENE, new SplashscreenSceneController());
        StageController.getStageController().registerSceneController(SceneType.USER_FORM_SCENE, new UserFormSceneController());
        StageController.getStageController().registerSceneController(SceneType.LOBBY_SCENE, new LobbySceneController());
        StageController.getStageController().showScene(SceneType.SPLASHSCREEN_SCENE, true);
        StageController.getStageController().setReady(true);
    }
}
