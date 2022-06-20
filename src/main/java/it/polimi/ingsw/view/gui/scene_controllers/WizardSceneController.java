package it.polimi.ingsw.view.gui.scene_controllers;

import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.view.ViewUtilityFunctions;
import it.polimi.ingsw.view.game_objects.ClientPlayer;
import it.polimi.ingsw.view.gui.GUIConstants;
import it.polimi.ingsw.view.gui.SceneType;
import it.polimi.ingsw.view.gui.StageController;
import it.polimi.ingsw.view.gui.exceptions.SceneControllerNotRegisteredException;
import it.polimi.ingsw.view.gui.exceptions.StageNotSetException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class WizardSceneController extends SceneController {
    @Override
    protected Scene layout() {
        try {
            // TODO consider delaying the creation of the scene because the switch from the table scene to the
            // TODO wizard scene could be really fast
            // The scene cannot be created if clientTeams has not been set.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/wizard_scene.fxml"));
            loader.setControllerFactory(type -> {
                if (type == WizardSceneController.class) {
                    return this;
                } else {
                    try {
                        return type.newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            AnchorPane root = loader.load();
            // Get wizard ids
            ArrayList<Integer> wizardIds = new ArrayList<>();
            for (int id = ModelConstants.MIN_ID_OF_WIZARD; id <= ModelConstants.MAX_ID_OF_WIZARD; id++)
                wizardIds.add(id);
            // Get used wizards
            Integer[] usedWizards = StageController.getStageController().getClientTeams().getTeams()
                    .stream()
                    .flatMap(clientTeam -> clientTeam.getPlayers().stream())
                    .map(ClientPlayer::getWizard)
                    .filter(wizard -> wizard != -1)
                    .toArray(Integer[]::new);

            // Get available wizards
            Integer[] availableWizards = wizardIds.stream()
                    .filter(id -> Arrays.stream(usedWizards).noneMatch(wizard -> Objects.equals(wizard, id)))
                    .toArray(Integer[]::new);

            // Add eventHandler to available wizards and increase opacity of other wizards
            for (int wizard : wizardIds) {
                if (Arrays.stream(availableWizards).anyMatch(id -> id == wizard)) {
                    for (Node node : root.getChildren())
                        if (node.getId().equals(GUIConstants.WIZARD_CARD_NAME + wizard)) {
                            node.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onSelectionOfWizard);
                            // Make wizard pulse
                            ViewUtilityFunctions.createAnimationPulses((ImageView) node, 1.03);
                            break;
                        }
                } else {
                    for (Node node : root.getChildren())
                        if (node.getId().equals(GUIConstants.WIZARD_CARD_NAME + wizard)) {
                            node.setOpacity(0.2);
                            break;
                        }
                }
            }
            // Return new scene
            return new Scene(root);


        } catch (IOException e) {
            e.printStackTrace();
            // TODO do something
            return null;
        }
    }

    @Override
    protected void updateScene() {

    }

    private void onSelectionOfWizard(MouseEvent event) {
        try {
            int wizardId = Integer.parseInt(event.getPickResult().getIntersectedNode().getId().replace(GUIConstants.WIZARD_CARD_NAME, ""));
            // TODO communicate id to server
            StageController.getStageController().showScene(SceneType.TABLE_SCENE, false);
        } catch (SceneControllerNotRegisteredException | StageNotSetException e) {
            // TODO close connection
        }
    }
}