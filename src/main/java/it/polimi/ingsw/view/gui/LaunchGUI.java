package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.view.gui.scene_controllers.SplashscreenSceneController;
import it.polimi.ingsw.view.gui.scene_controllers.UserFormSceneController;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.List;

public class LaunchGUI extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("Starting GUI...");
        List<String> parameters = getParameters().getRaw();
        primaryStage.setTitle("Eriantys");
        StageController.getStageController().setStage(primaryStage);
        StageController.getStageController().registerSceneController(SceneType.SPLASHSCREEN_SCENE, new SplashscreenSceneController());
        StageController.getStageController().registerSceneController(SceneType.USER_FORM_SCENE, new UserFormSceneController());
        StageController.getStageController().showScene(SceneType.SPLASHSCREEN_SCENE);
    }
}
