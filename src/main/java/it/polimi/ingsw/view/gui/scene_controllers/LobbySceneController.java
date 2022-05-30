package it.polimi.ingsw.view.gui.scene_controllers;

import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.view.game_objects.ClientLobby;
import it.polimi.ingsw.view.gui.GUIConstants;
import it.polimi.ingsw.view.gui.StageController;
import it.polimi.ingsw.view.gui.exceptions.GuiViewNotSet;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class LobbySceneController extends SceneController {
    RadioButton twoPlayersRadioButton;
    RadioButton threePlayersRadioButton;
    RadioButton fourPlayersRadioButton;

    /**
     * Draws the layout in a scene and returns it.
     *
     * @return a new ready-to-display Scene
     */
    @Override
    protected Scene layout() {
        VBox layout = new VBox(GUIConstants.SCENE_LOBBY_PLAYERS_CHOICE_SPACING);
        layout.getStyleClass().add(GUIConstants.SCENE_LOBBY_STYLE_MAIN_LAYOUT_CLASS);

        Pane eriantysLogoBox = new Pane();
        ImageView eriantysLogo = new ImageView(LobbySceneController.class.getResource(GUIConstants.SCENE_LOBBY_ERIANTYS_LOGO_IMAGE_PATH).toExternalForm());
        eriantysLogo.setFitHeight(GUIConstants.SCENE_LOBBY_ERIANTYS_LOGO_IMAGE_HEIGHT);
        eriantysLogo.setFitWidth(GUIConstants.SCENE_LOBBY_WIDTH);
        eriantysLogo.getStyleClass().add(GUIConstants.SCENE_LOBBY_ERIANTYS_LOGO_IMAGE_CLASS);
        eriantysLogoBox.getChildren().add(eriantysLogo);
        eriantysLogoBox.getStyleClass().add(GUIConstants.SCENE_LOBBY_ERIANTYS_LOGO_BOX_CLASS);
        layout.getChildren().add(eriantysLogoBox);

        VBox layoutRadioButtons = new VBox();
        VBox layoutBottomButtons = new VBox();

        Label label = new Label("Would you like to change the preference ?");
        label.getStyleClass().add(GUIConstants.SCENE_LOBBY_LABEL_BOX_CLASS);

        layout.getChildren().add(label);
        layout.getChildren().add(layoutRadioButtons);
        layout.getChildren().add(layoutBottomButtons);

        ToggleGroup playersSelectionGroup = new ToggleGroup(); // Prevents multi choice in the radio buttons
        twoPlayersRadioButton = new RadioButton();
        threePlayersRadioButton = new RadioButton();
        fourPlayersRadioButton = new RadioButton();

        // Set the radio buttons with
        // - current preference as disabled and as selected
        // - set text with lobby status
        this.setRadiosProperties();

        // Use toggle group to avoid more than preference at time
        twoPlayersRadioButton.setToggleGroup(playersSelectionGroup);
        threePlayersRadioButton.setToggleGroup(playersSelectionGroup);
        fourPlayersRadioButton.setToggleGroup(playersSelectionGroup);

        layoutRadioButtons.getChildren().add(twoPlayersRadioButton);
        layoutRadioButtons.getChildren().add(threePlayersRadioButton);
        layoutRadioButtons.getChildren().add(fourPlayersRadioButton);

        layoutRadioButtons.getStyleClass().add(GUIConstants.SCENE_LOBBY_STYLE_PLAYER_NUMBER_SELECTION_BOX_CLASS);
        twoPlayersRadioButton.getStyleClass().add(GUIConstants.SCENE_LOBBY_STYLE_PLAYER_RADIO_BUTTONS_CLASS);
        threePlayersRadioButton.getStyleClass().add(GUIConstants.SCENE_LOBBY_STYLE_PLAYER_RADIO_BUTTONS_CLASS);
        fourPlayersRadioButton.getStyleClass().add(GUIConstants.SCENE_LOBBY_STYLE_PLAYER_RADIO_BUTTONS_CLASS);

        Button nextButton = new Button("Change preference");
        nextButton.setOnAction(this::handleButton);
        layoutBottomButtons.getChildren().add(nextButton);
        layoutBottomButtons.getStyleClass().add(GUIConstants.SCENE_LOBBY_STYLE_BUTTON_CLASS);

        Scene scene = new Scene(layout, GUIConstants.SCENE_LOBBY_WIDTH, GUIConstants.SCENE_LOBBY_HEIGHT);
        scene.getStylesheets().add(LobbySceneController.class.getResource(GUIConstants.SCENE_LOBBY_STYLESHEET_PATH).toExternalForm());
        return scene;
    }

    /**
     * The radio buttons properties are edited here:
     * - if a preference is already in user, that radio button can't be selected
     * - set it as selected automatically
     * - text with lobby status
     */
    private void setRadiosProperties() {
        try {
            this.setRadiosTextWithLobby();
            int currentPreference = StageController.getStageController().getGuiView().getUser().getPreference();
            twoPlayersRadioButton.setDisable(false);
            twoPlayersRadioButton.setSelected(false);
            threePlayersRadioButton.setDisable(false);
            threePlayersRadioButton.setSelected(false);
            fourPlayersRadioButton.setDisable(false);
            fourPlayersRadioButton.setSelected(false);
            if (currentPreference == ModelConstants.TWO_PLAYERS) {
                twoPlayersRadioButton.setDisable(true);
                twoPlayersRadioButton.setSelected(true);
            } else if (currentPreference == ModelConstants.THREE_PLAYERS) {
                threePlayersRadioButton.setDisable(true);
                threePlayersRadioButton.setSelected(true);
            } else {
                fourPlayersRadioButton.setDisable(true);
                fourPlayersRadioButton.setSelected(true);
            }
        } catch (GuiViewNotSet e) {
            StageController.getStageController().handleInternalException(e);
        }
    }

    // Sets the radio buttons text with the current lobby status
    private void setRadiosTextWithLobby() {
        String twoPlayersText = "2 players";
        String threePlayersText = "3 players";
        String fourPlayersText = "4 players";

        ClientLobby clientLobby = StageController.getStageController().getClientLobby();
        if (clientLobby != null) { // Already have it
            twoPlayersText = twoPlayersText + String.format(" - %d/%d ready", clientLobby.getLobbyStatus().get(ModelConstants.TWO_PLAYERS), ModelConstants.TWO_PLAYERS);
            threePlayersText = threePlayersText + String.format(" - %d/%d ready", clientLobby.getLobbyStatus().get(ModelConstants.THREE_PLAYERS), ModelConstants.THREE_PLAYERS);
            fourPlayersText = fourPlayersText + String.format(" - %d/%d ready", clientLobby.getLobbyStatus().get(ModelConstants.FOUR_PLAYERS), ModelConstants.FOUR_PLAYERS);
        }

        twoPlayersRadioButton.setText(twoPlayersText);
        threePlayersRadioButton.setText(threePlayersText);
        fourPlayersRadioButton.setText(fourPlayersText);
    }

    // Handles the click of the Change preference button
    void handleButton(Event e) {
        // If current radio button selected is the current preference, doesn't do anything
        try {
            int currentPreference = StageController.getStageController().getGuiView().getUser().getPreference();
            int selectedPreference = twoPlayersRadioButton.isSelected() ? ModelConstants.TWO_PLAYERS : threePlayersRadioButton.isSelected() ? ModelConstants.THREE_PLAYERS : ModelConstants.FOUR_PLAYERS;

            // Sends the new preference to the GUI object that will warn the server
            if (selectedPreference != currentPreference) {
                StageController.getStageController().getGuiView().handleChangePreference(selectedPreference);
            }
        } catch (GuiViewNotSet ex) {
            StageController.getStageController().handleInternalException(ex);
        }
    }
}
