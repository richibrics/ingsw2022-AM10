package it.polimi.ingsw.view.cli;

public class CliConstants {

    public static String ESCAPE_CODE_TO_CLEAR_TERMINAL = "\033[H\033[2J\33[H";

    public static int MAX_LENGTH_OF_USERNAME = 22;

    // Descriptions of actions
    public static String ACTION_ON_SELECTION_OF_WIZARD_ID_DESCRIPTION = "Selection of wizard.";
    public static String ACTION_ON_SELECTION_OF_ASSISTANTS_CARD_ID_DESCRIPTION = "Selection of assistant card.";
    public static String ACTION_ON_SELECTION_OF_CHARACTER_CARD_ID_DESCRIPTION = "Selection of character card (see character card available).";
    public static String ACTION_MOVE_STUDENTS_FROM_ENTRANCE_ID_DESCRIPTION = "Selection of a student currently in the entrance. The student can be moved " +
            "to an island or to the dining room.";
    public static String ACTION_MOVE_MOTHER_NATURE_ID_DESCRIPTION = "Change position of mother nature.";
    public static String ACTION_FROM_CLOUD_TILE_TO_ENTRANCE_ID_DESCRIPTION = "Selection of cloud tile. The students on the cloud are then moved to your entrance.";
    public static String TERMINATE_ACTION_INPUT_OPTIONAL_STRING = "N";
    public static String CANCEL_ACTION_INPUT_STRING = "cancel";
    public static String DINING_ROOM_CAPITAL_D = "D";
    public static String DINING_ROOM_LOWER_CASE_D = "d";
    public static final String EXPERT_GAME = "e";
    public static final String BASIC_GAME = "b";
    public static final String GAME_MODE_NOT_USED = " ";
    public static final String EXPERT_GAME_COMPLETE = "expert";
    public static final String BASIC_GAME_COMPLETE = "basic";
}
