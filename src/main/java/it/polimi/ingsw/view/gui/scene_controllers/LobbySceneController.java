package it.polimi.ingsw.view.gui.scene_controllers;

import it.polimi.ingsw.controller.ControllerConstants;
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
    RadioButton twoPlayersEasyRadioButton;
    RadioButton threePlayersEasyRadioButton;
    RadioButton fourPlayersEasyRadioButton;
    RadioButton twoPlayersExpertRadioButton;
    RadioButton threePlayersExpertRadioButton;
    RadioButton fourPlayersExpertRadioButton;

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
        twoPlayersEasyRadioButton = new RadioButton();
        threePlayersEasyRadioButton = new RadioButton();
        fourPlayersEasyRadioButton = new RadioButton();
        twoPlayersExpertRadioButton = new RadioButton();
        threePlayersExpertRadioButton = new RadioButton();
        fourPlayersExpertRadioButton = new RadioButton();

        // Set the radio buttons with
        // - current preference as disabled and as selected
        // - set text with lobby status
        this.setRadiosProperties();

        // Use toggle group to avoid more than preference at time
        twoPlayersEasyRadioButton.setToggleGroup(playersSelectionGroup);
        threePlayersEasyRadioButton.setToggleGroup(playersSelectionGroup);
        fourPlayersEasyRadioButton.setToggleGroup(playersSelectionGroup);
        twoPlayersExpertRadioButton.setToggleGroup(playersSelectionGroup);
        threePlayersExpertRadioButton.setToggleGroup(playersSelectionGroup);
        fourPlayersExpertRadioButton.setToggleGroup(playersSelectionGroup);

        layoutRadioButtons.getChildren().add(twoPlayersEasyRadioButton);
        layoutRadioButtons.getChildren().add(threePlayersEasyRadioButton);
        layoutRadioButtons.getChildren().add(fourPlayersEasyRadioButton);
        layoutRadioButtons.getChildren().add(twoPlayersExpertRadioButton);
        layoutRadioButtons.getChildren().add(threePlayersExpertRadioButton);
        layoutRadioButtons.getChildren().add(fourPlayersExpertRadioButton);

        layoutRadioButtons.getStyleClass().add(GUIConstants.SCENE_LOBBY_STYLE_PLAYER_NUMBER_SELECTION_BOX_CLASS);
        twoPlayersEasyRadioButton.getStyleClass().add(GUIConstants.SCENE_LOBBY_STYLE_PLAYER_RADIO_BUTTONS_CLASS);
        threePlayersEasyRadioButton.getStyleClass().add(GUIConstants.SCENE_LOBBY_STYLE_PLAYER_RADIO_BUTTONS_CLASS);
        fourPlayersEasyRadioButton.getStyleClass().add(GUIConstants.SCENE_LOBBY_STYLE_PLAYER_RADIO_BUTTONS_CLASS);
        twoPlayersExpertRadioButton.getStyleClass().add(GUIConstants.SCENE_LOBBY_STYLE_PLAYER_RADIO_BUTTONS_CLASS);
        threePlayersExpertRadioButton.getStyleClass().add(GUIConstants.SCENE_LOBBY_STYLE_PLAYER_RADIO_BUTTONS_CLASS);
        fourPlayersExpertRadioButton.getStyleClass().add(GUIConstants.SCENE_LOBBY_STYLE_PLAYER_RADIO_BUTTONS_CLASS);

        Button nextButton = new Button("Change preference");
        nextButton.setOnAction(this::handleButton);
        layoutBottomButtons.getChildren().add(nextButton);
        layoutBottomButtons.getStyleClass().add(GUIConstants.SCENE_LOBBY_STYLE_BUTTON_CLASS);

        Scene scene = new Scene(layout, GUIConstants.SCENE_LOBBY_WIDTH, GUIConstants.SCENE_LOBBY_HEIGHT);
        scene.getStylesheets().add(LobbySceneController.class.getResource(GUIConstants.SCENE_LOBBY_STYLESHEET_PATH).toExternalForm());
        return scene;
    }

    @Override
    protected void updateScene() {

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
            twoPlayersEasyRadioButton.setDisable(false);
            twoPlayersEasyRadioButton.setSelected(false);
            threePlayersEasyRadioButton.setDisable(false);
            threePlayersEasyRadioButton.setSelected(false);
            fourPlayersEasyRadioButton.setDisable(false);
            fourPlayersEasyRadioButton.setSelected(false);
            twoPlayersExpertRadioButton.setDisable(false);
            twoPlayersExpertRadioButton.setSelected(false);
            threePlayersExpertRadioButton.setDisable(false);
            threePlayersExpertRadioButton.setSelected(false);
            fourPlayersExpertRadioButton.setDisable(false);
            fourPlayersExpertRadioButton.setSelected(false);
            if (currentPreference == ControllerConstants.TWO_PLAYERS_PREFERENCE_EASY) {
                twoPlayersEasyRadioButton.setDisable(true);
                twoPlayersEasyRadioButton.setSelected(true);
            } else if (currentPreference == ControllerConstants.THREE_PLAYERS_PREFERENCE_EASY) {
                threePlayersEasyRadioButton.setDisable(true);
                threePlayersEasyRadioButton.setSelected(true);
            } else if (currentPreference == ControllerConstants.FOUR_PLAYERS_PREFERENCE_EASY) {
                fourPlayersEasyRadioButton.setDisable(true);
                fourPlayersEasyRadioButton.setSelected(true);
            } else if (currentPreference == ControllerConstants.TWO_PLAYERS_PREFERENCE_EXPERT) {
                twoPlayersExpertRadioButton.setDisable(true);
                twoPlayersExpertRadioButton.setSelected(true);
            } else if (currentPreference == ControllerConstants.THREE_PLAYERS_PREFERENCE_EXPERT) {
                threePlayersExpertRadioButton.setDisable(true);
                threePlayersExpertRadioButton.setSelected(true);
            } else {
                fourPlayersExpertRadioButton.setDisable(true);
                fourPlayersExpertRadioButton.setSelected(true);
            }
        } catch (GuiViewNotSet e) {
            StageController.getStageController().handleInternalException(e);
        }
    }

    // Sets the radio buttons text with the current lobby status
    private void setRadiosTextWithLobby() {
        String twoEasyPlayersText = "2 players easy";
        String threeEasyPlayersText = "3 players easy";
        String fourEasyPlayersText = "4 players easy";
        String twoExpertPlayersText = "2 players expert";
        String threeExpertPlayersText = "3 players expert";
        String fourExpertPlayersText = "4 players expert";

        ClientLobby clientLobby = StageController.getStageController().getClientLobby();
        if (clientLobby != null) { // Already have it
            twoEasyPlayersText = twoEasyPlayersText + String.format(" - %d/%d ready", clientLobby.getLobbyStatus().get(ControllerConstants.TWO_PLAYERS_PREFERENCE_EASY), ModelConstants.TWO_PLAYERS);
            threeEasyPlayersText = threeEasyPlayersText + String.format(" - %d/%d ready", clientLobby.getLobbyStatus().get(ControllerConstants.THREE_PLAYERS_PREFERENCE_EASY), ModelConstants.THREE_PLAYERS);
            fourEasyPlayersText = fourEasyPlayersText + String.format(" - %d/%d ready", clientLobby.getLobbyStatus().get(ControllerConstants.FOUR_PLAYERS_PREFERENCE_EASY), ModelConstants.FOUR_PLAYERS);
            twoExpertPlayersText = twoExpertPlayersText + String.format(" - %d/%d ready", clientLobby.getLobbyStatus().get(ControllerConstants.TWO_PLAYERS_PREFERENCE_EXPERT), ModelConstants.TWO_PLAYERS);
            threeExpertPlayersText = threeExpertPlayersText + String.format(" - %d/%d ready", clientLobby.getLobbyStatus().get(ControllerConstants.THREE_PLAYERS_PREFERENCE_EXPERT), ModelConstants.THREE_PLAYERS);
            fourExpertPlayersText = fourExpertPlayersText + String.format(" - %d/%d ready", clientLobby.getLobbyStatus().get(ControllerConstants.FOUR_PLAYERS_PREFERENCE_EXPERT), ModelConstants.FOUR_PLAYERS);
        }

        twoPlayersEasyRadioButton.setText(twoEasyPlayersText);
        threePlayersEasyRadioButton.setText(threeEasyPlayersText);
        fourPlayersEasyRadioButton.setText(fourEasyPlayersText);
        twoPlayersExpertRadioButton.setText(twoExpertPlayersText);
        threePlayersExpertRadioButton.setText(threeExpertPlayersText);
        fourPlayersExpertRadioButton.setText(fourExpertPlayersText);
    }

    // Handles the click of the Change preference button
    void handleButton(Event e) {
        // If current radio button selected is the current preference, doesn't do anything
        try {
            int currentPreference = StageController.getStageController().getGuiView().getUser().getPreference();
            int selectedPreference =
                    twoPlayersEasyRadioButton.isSelected() ? ControllerConstants.TWO_PLAYERS_PREFERENCE_EASY :
                            threePlayersEasyRadioButton.isSelected() ? ControllerConstants.THREE_PLAYERS_PREFERENCE_EASY :
                                    fourPlayersEasyRadioButton.isSelected() ? ControllerConstants.FOUR_PLAYERS_PREFERENCE_EASY :
                                            twoPlayersExpertRadioButton.isSelected() ? ControllerConstants.TWO_PLAYERS_PREFERENCE_EXPERT :
                                                    threePlayersExpertRadioButton.isSelected() ? ControllerConstants.THREE_PLAYERS_PREFERENCE_EXPERT : ControllerConstants.FOUR_PLAYERS_PREFERENCE_EXPERT;

            // Sends the new preference to the GUI object that will warn the server
            if (selectedPreference != currentPreference) {
                StageController.getStageController().getGuiView().handleChangePreference(selectedPreference);
            }
        } catch (GuiViewNotSet ex) {
            StageController.getStageController().handleInternalException(ex);
        }
    }
}
