package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.view.gui.scene_controllers.*;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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

        Logger.getAnonymousLogger().log(Level.INFO, "Starting GUI...");
        List<String> parameters = getParameters().getRaw();
        primaryStage.setTitle("Eriantys");
        StageController.getStageController().setStage(primaryStage);
        StageController.getStageController().registerSceneController(SceneType.SPLASHSCREEN_SCENE, new SplashscreenSceneController());
        StageController.getStageController().registerSceneController(SceneType.USER_FORM_SCENE, new UserFormSceneController());
        StageController.getStageController().registerSceneController(SceneType.LOBBY_SCENE, new LobbySceneController());
        StageController.getStageController().showScene(SceneType.SPLASHSCREEN_SCENE, true);
        StageController.getStageController().registerSceneController(SceneType.TABLE_SCENE, new TableSceneController());
        StageController.getStageController().registerSceneController(SceneType.SCHOOL_BOARD_SCENE, new SchoolBoardsSceneController());
        StageController.getStageController().registerSceneController(SceneType.DECK_SCENE, new DeckSceneController());
        StageController.getStageController().setReady(true);
    }
}
