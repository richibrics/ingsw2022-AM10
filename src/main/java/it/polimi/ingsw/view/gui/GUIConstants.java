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
    public static final int SCENE_USER_FORM_HEIGHT = 420;
    public static final int SCENE_USER_FORM_USERNAME_PLAYERS_CHOICE_SPACING = 10;
    public static final String SCENE_USER_FORM_STYLESHEET_PATH = "/CSS/user_form_scene.css";
    public static final String SCENE_USER_FORM_STYLE_USER_INPUT_BOX_CLASS = "user_input";
    public static final String SCENE_USER_FORM_STYLE_PLAYER_RADIO_BUTTONS_CLASS = "players_number_radio";
    public static final String SCENE_USER_FORM_STYLE_PLAYER_NUMBER_SELECTION_BOX_CLASS = "players_number";
    public static final String SCENE_USER_FORM_STYLE_MAIN_LAYOUT_CLASS = "main_layout";
    public static final String SCENE_USER_FORM_STYLE_BUTTON_CLASS = "lobby_button";
    public static final String SCENE_USER_FORM_ERIANTYS_LOGO_IMAGE_PATH = "/images/eriantys_text_logo5.png";
    public static final String SCENE_USER_FORM_ERIANTYS_LOGO_IMAGE_CLASS = "logo_image";
    public static final String SCENE_USER_FORM_ERIANTYS_LOGO_BOX_CLASS = "logo_image_box";
    public static final int SCENE_USER_FORM_ERIANTYS_LOGO_IMAGE_HEIGHT = SCENE_USER_FORM_HEIGHT*2/5; // in pixel

    // LOBBY
    // Same size is better
    public static final int SCENE_LOBBY_WIDTH = SCENE_USER_FORM_WIDTH;
    public static final int SCENE_LOBBY_HEIGHT = SCENE_USER_FORM_HEIGHT;
    public static final int SCENE_LOBBY_PLAYERS_CHOICE_SPACING = 10;
    public static final String SCENE_LOBBY_STYLESHEET_PATH = "/CSS/lobby_scene.css";
    public static final String SCENE_LOBBY_STYLE_PLAYER_RADIO_BUTTONS_CLASS = "players_number_radio";
    public static final String SCENE_LOBBY_STYLE_PLAYER_NUMBER_SELECTION_BOX_CLASS = "players_number";
    public static final String SCENE_LOBBY_STYLE_MAIN_LAYOUT_CLASS = "main_layout";
    public static final String SCENE_LOBBY_STYLE_BUTTON_CLASS = "lobby_button";
    public static final String SCENE_LOBBY_ERIANTYS_LOGO_IMAGE_PATH = "/images/eriantys_text_logo5.png";
    public static final String SCENE_LOBBY_ERIANTYS_LOGO_IMAGE_CLASS = "logo_image";
    public static final String SCENE_LOBBY_ERIANTYS_LOGO_BOX_CLASS = "logo_image_box";
    public static final int SCENE_LOBBY_ERIANTYS_LOGO_IMAGE_HEIGHT = SCENE_LOBBY_HEIGHT*2/5; // in pixel
    public static final String SCENE_LOBBY_LABEL_BOX_CLASS = "label";

    //Images paths for table
    public static final String SCENE_TABLE_CLOUD_IMAGE_PATH = "/images/cloud_tile.png";
    public static final String SCENE_TABLE_COIN_IMAGE_PATH = "/images/coin.png";
    public static final String SCENE_TABLE_NO_ENTRY_PATH = "/images/islands/no_entry_tile.png";
    public static final String SCENE_TABLE_BLACK_TOWER_IMAGE_PATH = "/images/towers/black_tower.png";
    public static final String SCENE_TABLE_GREY_TOWER_IMAGE_PATH = "/images/towers/grey_tower.png";
    public static final String SCENE_TABLE_WHITE_TOWER_IMAGE_PATH = "/images/towers/white_tower.png";
    public static final String SCENE_TABLE_YELLOW_STUDENT_DISC_IMAGE_PATH = "/images/pawns/student_yellow.png";
    public static final String SCENE_TABLE_BLUE_STUDENT_DISC_IMAGE_PATH = "/images/pawns/student_blue.png";
    public static final String SCENE_TABLE_GREEN_STUDENT_DISC_IMAGE_PATH = "/images/pawns/student_green.png";
    public static final String SCENE_TABLE_RED_STUDENT_DISC_IMAGE_PATH = "/images/pawns/student_red.png";
    public static final String SCENE_TABLE_PINK_STUDENT_DISC_IMAGE_PATH = "/images/pawns/student_pink.png";
    public static final String SCENE_TABLE_YELLOW_PROFESSOR_IMAGE_PATH = "/images/pawns/teacher_yellow.png";
    public static final String SCENE_TABLE_BLUE_PROFESSOR_IMAGE_PATH = "/images/pawns/teacher_blue.png";
    public static final String SCENE_TABLE_GREEN_PROFESSOR_IMAGE_PATH = "/images/pawns/teacher_green.png";
    public static final String SCENE_TABLE_RED_PROFESSOR_IMAGE_PATH = "/images/pawns/teacher_red.png";
    public static final String SCENE_TABLE_PINK_PROFESSOR_IMAGE_PATH = "/images/pawns/teacher_pink.png";
    public static final String SCENE_TABLE_MOTHER_NATURE_IMAGE_PATH = "/images/pawns/mother_nature.png";
    public static final String SCENE_TABLE_FRIAR_IMAGE_PATH = "/images/characters/friar.png";
    public static final String SCENE_TABLE_COOK_IMAGE_PATH = "/images/characters/cook.png";
    public static final String SCENE_TABLE_AMBASSADOR_IMAGE_PATH = "/images/characters/ambassador.png";
    public static final String SCENE_TABLE_MAILMAN_IMAGE_PATH = "/images/characters/mailman.png";
    public static final String SCENE_TABLE_HERBALIST_IMAGE_PATH = "/images/characters/herbalist.png";
    public static final String SCENE_TABLE_CENTAUR_IMAGE_PATH = "/images/characters/centaur.png";
    public static final String SCENE_TABLE_JESTER_IMAGE_PATH = "/images/characters/jester.png";
    public static final String SCENE_TABLE_KNIGHT_IMAGE_PATH = "/images/characters/knight.png";
    public static final String SCENE_TABLE_MUSHROOM_HUNTER_IMAGE_PATH = "/images/characters/mushroom_hunter.png";
    public static final String SCENE_TABLE_MINSTREL_IMAGE_PATH = "/images/characters/minstrel.png";
    public static final String SCENE_TABLE_LADY_IMAGE_PATH = "/images/characters/lady.png";
    public static final String SCENE_TABLE_THIEF_IMAGE_PATH = "/images/characters/thief.png";


    // Names used for assistant cards in fxml file of deck
    public static final String ASSISTANT_1 = "assistant1";
    public static final String ASSISTANT_2 = "assistant2";
    public static final String ASSISTANT_3 = "assistant3";
    public static final String ASSISTANT_4 = "assistant4";
    public static final String ASSISTANT_5 = "assistant5";
    public static final String ASSISTANT_6 = "assistant6";
    public static final String ASSISTANT_7 = "assistant7";
    public static final String ASSISTANT_8 = "assistant8";
    public static final String ASSISTANT_9 = "assistant9";
    public static final String ASSISTANT_10 = "assistant10";

    // Name used for island tile image view starts with tile
    public static final String ISLAND_TILE_NAME = "tile";

    // Name used for mother nature image view is mother
    public static final String MOTHER_NATURE_NAME = "mother";

    // Name used for student disc image view starts with student
    public static final String STUDENT_DISC_NAME = "student";

    // Name used for no entry tile image view
    public static final String NO_ENTRY_TILE_NAME = "noEntry";

    // Name used for professor pawn image view starts with professor
    public static final String PROFESSOR_PAWN_NAME = "professor";

    // Name used for towers
    public static final String TOWER_NAME = "towerColor";


    // Values for filling of island tiles
    // no entry tiles are 20x20 (same as students)
    public static final int WIDTH_OF_STUDENT_DISC = 20;
    public static final int HEIGHT_OF_STUDENT_DISC = 20;
    public static final int LAYOUT_X_OF_FIRST_AVAILABLE_CELL_FOR_GAME_OBJECTS_IN_ISLAND = 65; // In respect to the pane containing the island tile
    public static final int LAYOUT_Y_OF_FIRST_AVAILABLE_CELL_FOR_GAME_OBJECTS_IN_ISLAND = 48;
    // The following values must be divisible by WIDTH_OF_STUDENT_DISC and HEIGHT_OF_STUDENT_DISC respectively
    public static final int WIDTH_OF_MOTHER_NATURE = 40;
    public static final int HEIGHT_OF_MOTHER_NATURE_PAWN = 40;// The following values must be divisible by WIDTH_OF_STUDENT_DISC and HEIGHT_OF_STUDENT_DISC respectively
    public static final int WIDTH_OF_TOWER = 50;
    public static final int HEIGHT_OF_TOWER = 50;
    public static final int WIDTH_OF_RECTANGLE_CONTAINING_GAME_OBJECTS_IN_ISLAND = 120;
    public static final int HEIGHT_OF_RECTANGLE_CONTAINING_GAME_OBJECTS_IN_ISLAND = 140;

    //Values for entrance
    public static final int LAYOUT_X_OF_FIRST_CELL_FIRST_ROW_ENTRANCE = 54;
    public static final int LAYOUT_Y_OF_FIRST_CELL_FIRST_ROW_ENTRANCE = 704;
    public static final int LAYOUT_X_OFFSET_CELLS_ENTRANCE = 58;
    public static final int LAYOUT_Y_OFFSET_CELLS_ENTRANCE = 50;
    public static final int CELLS_FIRST_ROW_ENTRANCE = 5;
    public static final int COLUMNS_ENTRANCE = 2;

    // Values for dining room
    public static final int LAYOUT_X_OF_FIRST_CELL_FIRST_LANE_ON_LEFT_BOTTOM_DINING_ROOM = 53;
    public static final int LAYOUT_Y_OF_FIRST_CELL_FIRST_LANE_ON_LEFT_BOTTOM_DINING_ROOM = 629;
    public static final int LAYOUT_X_OFFSET_CELLS_DINING_ROOM = 58;
    public static final int LAYOUT_Y_OFFSET_CELLS_DINING_ROOM = -39;
    public static final int LANES = 5; // Must be equal to ClientPawnColor.values().length
    public static final int INDEX_GREEN_LANE = 0;
    public static final int INDEX_RED_LANE = 1;
    public static final int INDEX_YELLOW_LANE = 2;
    public static final int INDEX_PINK_LANE = 3;
    public static final int INDEX_BLUE_LANE = 4;

    // Values for professor section
    public static final int WIDTH_OF_PROFESSOR_PAWN = 30;
    public static final int HEIGHT_OF_PROFESSOR_PAWN = 30;
    public static final int LAYOUT_X_OF_FIRST_CELL_PROFESSOR_SECTION = 48;
    public static final int LAYOUT_Y_OF_FIRST_CELL_PROFESSOR_SECTION = 203;
    public static final int LAYOUT_X_OFFSET_CELLS_PROFESSOR_SECTION = 58;

    // Values for towers section
    public static final int LAYOUT_X_OF_FIRST_CELL_TOWERS = 66;
    public static final int LAYOUT_Y_OF_FIRST_CELL_TOWERS = 108;
    public static final int LAYOUT_X_OFFSET_TOWERS = 50;
    public static final int LAYOUT_Y_OFFSET_TOWERS = -60;
    public static final int NUMBER_OF_ROWS_OF_TOWERS = 2;
    public static final int NUMBER_OF_TOWERS_IN_ROW = 4;
}
