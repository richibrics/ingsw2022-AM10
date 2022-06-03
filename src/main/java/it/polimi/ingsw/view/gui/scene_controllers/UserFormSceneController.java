package it.polimi.ingsw.view.gui.scene_controllers;

import it.polimi.ingsw.controller.ControllerConstants;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.view.gui.GUIConstants;
import it.polimi.ingsw.view.gui.StageController;
import it.polimi.ingsw.view.gui.exceptions.GuiViewNotSet;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class UserFormSceneController extends SceneController {
    TextField textBox;
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
        VBox layout = new VBox(GUIConstants.SCENE_USER_FORM_USERNAME_PLAYERS_CHOICE_SPACING);
        layout.getStyleClass().add(GUIConstants.SCENE_USER_FORM_STYLE_MAIN_LAYOUT_CLASS);

        Pane eriantysLogoBox = new Pane();
        ImageView eriantysLogo = new ImageView(UserFormSceneController.class.getResource(GUIConstants.SCENE_USER_FORM_ERIANTYS_LOGO_IMAGE_PATH).toExternalForm());
        eriantysLogo.setFitHeight(GUIConstants.SCENE_USER_FORM_ERIANTYS_LOGO_IMAGE_HEIGHT);
        eriantysLogo.setFitWidth(GUIConstants.SCENE_USER_FORM_WIDTH);
        eriantysLogo.getStyleClass().add(GUIConstants.SCENE_USER_FORM_ERIANTYS_LOGO_IMAGE_CLASS);
        eriantysLogoBox.getChildren().add(eriantysLogo);
        eriantysLogoBox.getStyleClass().add(GUIConstants.SCENE_USER_FORM_ERIANTYS_LOGO_BOX_CLASS);
        layout.getChildren().add(eriantysLogoBox);

        VBox layoutUsernameInput = new VBox();
        VBox layoutRadioButtons = new VBox();
        VBox layoutBottomButtons = new VBox();

        layout.getChildren().add(layoutUsernameInput);
        layout.getChildren().add(layoutRadioButtons);
        layout.getChildren().add(layoutBottomButtons);

        Label usernameLabel = new Label("Username");
        textBox = new TextField();
        layoutUsernameInput.getChildren().add(usernameLabel);
        layoutUsernameInput.getChildren().add(textBox);

        ToggleGroup playersSelectionGroup = new ToggleGroup(); // Prevents multi choice in the radio buttons
        twoPlayersEasyRadioButton = new RadioButton();
        threePlayersEasyRadioButton = new RadioButton();
        fourPlayersEasyRadioButton = new RadioButton();
        twoPlayersExpertRadioButton = new RadioButton();
        threePlayersExpertRadioButton = new RadioButton();
        fourPlayersExpertRadioButton = new RadioButton();
        twoPlayersEasyRadioButton.setText("2 players easy");
        threePlayersEasyRadioButton.setText("3 players easy");
        fourPlayersEasyRadioButton.setText("4 players easy");
        twoPlayersExpertRadioButton.setText("2 players expert");
        threePlayersExpertRadioButton.setText("3 players expert");
        fourPlayersExpertRadioButton.setText("4 players expert");
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


        layoutUsernameInput.getStyleClass().add(GUIConstants.SCENE_USER_FORM_STYLE_USER_INPUT_BOX_CLASS);
        layoutRadioButtons.getStyleClass().add(GUIConstants.SCENE_USER_FORM_STYLE_PLAYER_NUMBER_SELECTION_BOX_CLASS);
        twoPlayersEasyRadioButton.getStyleClass().add(GUIConstants.SCENE_USER_FORM_STYLE_PLAYER_RADIO_BUTTONS_CLASS);
        threePlayersEasyRadioButton.getStyleClass().add(GUIConstants.SCENE_USER_FORM_STYLE_PLAYER_RADIO_BUTTONS_CLASS);
        fourPlayersEasyRadioButton.getStyleClass().add(GUIConstants.SCENE_USER_FORM_STYLE_PLAYER_RADIO_BUTTONS_CLASS);
        twoPlayersExpertRadioButton.getStyleClass().add(GUIConstants.SCENE_USER_FORM_STYLE_PLAYER_RADIO_BUTTONS_CLASS);
        threePlayersExpertRadioButton.getStyleClass().add(GUIConstants.SCENE_USER_FORM_STYLE_PLAYER_RADIO_BUTTONS_CLASS);
        fourPlayersExpertRadioButton.getStyleClass().add(GUIConstants.SCENE_USER_FORM_STYLE_PLAYER_RADIO_BUTTONS_CLASS);

        Button nextButton = new Button("Enter in Lobby");
        nextButton.setOnAction(this::handleNextPageButton);
        layoutBottomButtons.getChildren().add(nextButton);
        layoutBottomButtons.getStyleClass().add(GUIConstants.SCENE_USER_FORM_STYLE_BUTTON_CLASS);

        Scene scene = new Scene(layout, GUIConstants.SCENE_USER_FORM_WIDTH, GUIConstants.SCENE_USER_FORM_HEIGHT);
        scene.getStylesheets().add(UserFormSceneController.class.getResource(GUIConstants.SCENE_USER_FORM_STYLESHEET_PATH).toExternalForm());
        return scene;
    }

    /**
     * When "Enter in Lobby" is clicked, this callback is called.
     * Here are performed some checks:
     * - input text is filled
     * - players number selected
     * <p>
     * Then, using the network, the message is sent.
     * Then, if the network thread changes the scene, it means everything is okay; otherwise this scene will be
     * displayed again - with an error probably
     *
     * @param e the event happened in the window
     */
    void handleNextPageButton(Event e) {
        try {
            int selectedPreference =
                    twoPlayersEasyRadioButton.isSelected() ? ControllerConstants.TWO_PLAYERS_PREFERENCE_EASY :
                            threePlayersEasyRadioButton.isSelected() ? ControllerConstants.THREE_PLAYERS_PREFERENCE_EASY :
                                    fourPlayersEasyRadioButton.isSelected() ? ControllerConstants.FOUR_PLAYERS_PREFERENCE_EASY :
                                            twoPlayersExpertRadioButton.isSelected() ? ControllerConstants.TWO_PLAYERS_PREFERENCE_EXPERT :
                                                    threePlayersExpertRadioButton.isSelected() ? ControllerConstants.THREE_PLAYERS_PREFERENCE_EXPERT : ControllerConstants.FOUR_PLAYERS_PREFERENCE_EXPERT;
            StageController.getStageController().getGuiView().handleSendUser(textBox.getText(), selectedPreference);
        } catch (GuiViewNotSet ex) {
            StageController.getStageController().handleInternalException(ex);
        }
    }
}
