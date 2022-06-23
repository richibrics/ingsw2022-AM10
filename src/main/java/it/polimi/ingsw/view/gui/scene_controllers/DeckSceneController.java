package it.polimi.ingsw.view.gui.scene_controllers;

import it.polimi.ingsw.view.gui.GUIConstants;
import it.polimi.ingsw.view.gui.SceneType;
import it.polimi.ingsw.view.gui.StageController;
import it.polimi.ingsw.view.input_management.CommandDataEntryValidationSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.Objects;

public class DeckSceneController extends SceneController {

    private AnchorPane root;

    @Override
    protected Scene layout() {
        try {
            this.root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/scenes/deck_scene.fxml")));
            Scene scene = new Scene(this.root);
            return scene;
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    protected void updateScene() {

    }

    @FXML
    private void switchToTable(ActionEvent event) {
        try {
            StageController.getStageController().showScene(SceneType.TABLE_SCENE, false);
        } catch (Exception exception) {
            StageController.getStageController().getGuiView().getClientServerConnection().askToCloseConnectionWithError(exception.getMessage());
        }
    }

    @FXML
    private void onSelectionOfAssistant(MouseEvent event) {
        // Get the clicked assistant card
        ImageView clickedAssistantCard = (ImageView) event.getSource();
        // Remove image view from scene
        AnchorPane root = (AnchorPane) clickedAssistantCard.getParent();
        root.getChildren().remove(clickedAssistantCard);
        // Handle event
        TableSceneController.handleEventWithCommand(CommandDataEntryValidationSet.ASSISTANT_CARD,
                clickedAssistantCard.getId().replace(GUIConstants.ASSISTANT_NAME, ""), false);
    }
}
