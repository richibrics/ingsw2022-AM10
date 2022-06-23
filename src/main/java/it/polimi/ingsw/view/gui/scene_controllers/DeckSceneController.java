package it.polimi.ingsw.view.gui.scene_controllers;

import it.polimi.ingsw.view.game_objects.ClientAssistantCard;
import it.polimi.ingsw.view.gui.GUIConstants;
import it.polimi.ingsw.view.gui.SceneType;
import it.polimi.ingsw.view.gui.StageController;
import it.polimi.ingsw.view.input_management.CommandDataEntryValidationSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class DeckSceneController extends SceneController {

    private AnchorPane root;
    private ArrayList<Integer> assistantCardsId;

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
        int playerId = StageController.getStageController().getGuiView().getPlayerId();
        // Get the id of the player's schoolBoard through the player id
        int indexOfTeam = StageController.getStageController().getClientTeams().getTeams().indexOf(
                StageController.getStageController().getClientTeams().getTeams().
                stream().
                filter(clientTeam -> clientTeam.getPlayers().
                        stream().
                        filter(clientPlayer -> clientPlayer.getPlayerId() == playerId).count() == 1).
                toList().get(0));

        int indexOfPlayer = StageController.getStageController().getClientTeams().getTeams()
                .get(indexOfTeam).getPlayers().indexOf(StageController.getStageController().getClientTeams()
                        .getTeams().get(indexOfTeam).getPlayers().
                stream().
                filter(clientPlayer -> clientPlayer.getPlayerId() == playerId).
                toList().get(0));

        if (StageController.getStageController().getClientTeams().getTeams().get(indexOfTeam).getPlayers().get(indexOfPlayer).getWizard() != -1) {

            // Get assistant cards of player
            ArrayList<ClientAssistantCard> clientAssistantCards = StageController.getStageController().getClientTeams()
                    .getTeams().get(indexOfTeam).getPlayers().get(indexOfPlayer).getAssistantCards();

            // Get ids of assistant cards of player
            this.assistantCardsId = clientAssistantCards.stream()
                    .map(clientAssistantCard -> (clientAssistantCard.getId() - 1) % 10 + 1)
                    .collect(Collectors.toCollection(ArrayList::new));

            // Get ids of assistant cards on scene
            ArrayList<Integer> assistantsOnScene = this.root.getChildren().stream()
                    .filter(node -> node.getId().contains(GUIConstants.ASSISTANT_NAME))
                    .map(node -> Integer.valueOf(node.getId().replace(GUIConstants.ASSISTANT_NAME, "")))
                    .collect(Collectors.toCollection(ArrayList::new));

            // Get assistant cards to remove
            ArrayList<Integer> assistantCardsToRemove = assistantsOnScene.stream()
                    .filter(id -> !this.assistantCardsId.contains(id) && assistantsOnScene.contains(id))
                    .collect(Collectors.toCollection(ArrayList::new));

            ArrayList<Node> childrenToRemove = this.root.getChildren().stream()
                    .filter(node -> node.getId().contains(GUIConstants.ASSISTANT_NAME)
                            && assistantCardsToRemove.stream()
                            .anyMatch(id -> Integer.valueOf(node.getId().replace(GUIConstants.ASSISTANT_NAME, "")).equals(id)))
                    .collect(Collectors.toCollection(ArrayList::new));

            for (Node node : childrenToRemove)
                this.root.getChildren().remove(node);
        }
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
        // Handle event
        TableSceneController.handleEventWithCommand(CommandDataEntryValidationSet.ASSISTANT_CARD,
                event.getPickResult().getIntersectedNode().getId().replace(GUIConstants.ASSISTANT_NAME, ""), false);
    }
}