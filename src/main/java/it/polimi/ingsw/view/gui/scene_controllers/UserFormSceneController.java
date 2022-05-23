package it.polimi.ingsw.view.gui.scene_controllers;

import it.polimi.ingsw.view.gui.GUIConstants;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class UserFormSceneController extends SceneController {

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
        TextField textBox = new TextField();
        layoutUsernameInput.getChildren().add(usernameLabel);
        layoutUsernameInput.getChildren().add(textBox);

        ToggleGroup playersSelectionGroup = new ToggleGroup(); // Prevents multi choice in the radio buttons
        RadioButton twoPlayersRadioButton = new RadioButton();
        RadioButton threePlayersRadioButton = new RadioButton();
        RadioButton fourPlayersRadioButton = new RadioButton();
        twoPlayersRadioButton.setText("2 players");
        threePlayersRadioButton.setText("3 players");
        fourPlayersRadioButton.setText("4 players");
        twoPlayersRadioButton.setToggleGroup(playersSelectionGroup);
        threePlayersRadioButton.setToggleGroup(playersSelectionGroup);
        fourPlayersRadioButton.setToggleGroup(playersSelectionGroup);

        layoutRadioButtons.getChildren().add(twoPlayersRadioButton);
        layoutRadioButtons.getChildren().add(threePlayersRadioButton);
        layoutRadioButtons.getChildren().add(fourPlayersRadioButton);


        layoutUsernameInput.getStyleClass().add(GUIConstants.SCENE_USER_FORM_STYLE_USER_INPUT_BOX_CLASS);
        layoutRadioButtons.getStyleClass().add(GUIConstants.SCENE_USER_FORM_STYLE_PLAYER_NUMBER_SELECTION_BOX_CLASS);
        twoPlayersRadioButton.getStyleClass().add(GUIConstants.SCENE_USER_FORM_STYLE_PLAYER_RADIO_BUTTONS_CLASS);
        threePlayersRadioButton.getStyleClass().add(GUIConstants.SCENE_USER_FORM_STYLE_PLAYER_RADIO_BUTTONS_CLASS);
        fourPlayersRadioButton.getStyleClass().add(GUIConstants.SCENE_USER_FORM_STYLE_PLAYER_RADIO_BUTTONS_CLASS);

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
     * Then if the network thread changes the scene, it means everything is okay; otherwise this scene will be
     * displayed again
     *
     * @param e the event happened on the button
     */
    void handleNextPageButton(Event e) {

    }
}
