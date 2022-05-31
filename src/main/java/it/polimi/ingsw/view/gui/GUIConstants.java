package it.polimi.ingsw.view.gui;

public class GUIConstants {
    // GUI General
    public static final int SLEEP_TIME_WAIT_FOR_STAGECONTROLLER_TO_BE_READY_IN_MILLISECONDS = 500;

    // SPLASHSCREEN
    public static final int SCENE_SPLASHSCREEN_WIDTH = 300;
    public static final int SCENE_SPLASHSCREEN_HEIGHT = 300;
    public static final String SCENE_SPLASHSCREEN_BACKGROUND_IMAGE_PATH = "/images/splashscreen.png";
    public static final String SCENE_SPLASHSCREEN_STYLESHEET_PATH = "/CSS/splashscreen_scene.css";
    public static final String SCENE_SPLASHSCREEN_STYLE_MAIN_LAYOUT_CLASS = "main_layout";


    // USER_MENU
    // Same size is better
    public static final int SCENE_USER_FORM_WIDTH = SCENE_SPLASHSCREEN_WIDTH;
    public static final int SCENE_USER_FORM_HEIGHT = SCENE_SPLASHSCREEN_HEIGHT;
    public static final int SCENE_USER_FORM_USERNAME_PLAYERS_CHOICE_SPACING = 10;
    public static final String SCENE_USER_FORM_STYLESHEET_PATH = "/CSS/user_form_scene.css";
    public static final String SCENE_USER_FORM_STYLE_USER_INPUT_BOX_CLASS = "user_input";
    public static final String SCENE_USER_FORM_STYLE_PLAYER_RADIO_BUTTONS_CLASS = "players_number_radio";
    public static final String SCENE_USER_FORM_STYLE_PLAYER_NUMBER_SELECTION_BOX_CLASS = "players_number";
    public static final String SCENE_USER_FORM_STYLE_MAIN_LAYOUT_CLASS = "main_layout";
    public static final String SCENE_USER_FORM_STYLE_BUTTON_CLASS = "lobby_button";
    public static final String SCENE_USER_FORM_ERIANTYS_LOGO_IMAGE_PATH = "/images/eriantys_text_logo2.jpg";
    public static final String SCENE_USER_FORM_ERIANTYS_LOGO_IMAGE_CLASS = "logo_image";
    public static final String SCENE_USER_FORM_ERIANTYS_LOGO_BOX_CLASS = "logo_image_box";
    public static final int SCENE_USER_FORM_ERIANTYS_LOGO_IMAGE_HEIGHT = SCENE_USER_FORM_HEIGHT*2/5; // in pixel

    // LOBBY
    // Same size is better
    public static final int SCENE_LOBBY_WIDTH = SCENE_SPLASHSCREEN_WIDTH;
    public static final int SCENE_LOBBY_HEIGHT = SCENE_SPLASHSCREEN_HEIGHT;
    public static final int SCENE_LOBBY_PLAYERS_CHOICE_SPACING = 10;
    public static final String SCENE_LOBBY_STYLESHEET_PATH = "/CSS/lobby_scene.css";
    public static final String SCENE_LOBBY_STYLE_PLAYER_RADIO_BUTTONS_CLASS = "players_number_radio";
    public static final String SCENE_LOBBY_STYLE_PLAYER_NUMBER_SELECTION_BOX_CLASS = "players_number";
    public static final String SCENE_LOBBY_STYLE_MAIN_LAYOUT_CLASS = "main_layout";
    public static final String SCENE_LOBBY_STYLE_BUTTON_CLASS = "lobby_button";
    public static final String SCENE_LOBBY_ERIANTYS_LOGO_IMAGE_PATH = "/images/eriantys_text_logo2.jpg";
    public static final String SCENE_LOBBY_ERIANTYS_LOGO_IMAGE_CLASS = "logo_image";
    public static final String SCENE_LOBBY_ERIANTYS_LOGO_BOX_CLASS = "logo_image_box";
    public static final int SCENE_LOBBY_ERIANTYS_LOGO_IMAGE_HEIGHT = SCENE_LOBBY_HEIGHT*2/5; // in pixel
    public static final String SCENE_LOBBY_LABEL_BOX_CLASS = "label";

}
