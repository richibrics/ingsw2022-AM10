package it.polimi.ingsw.view.cli.drawers;

public class DrawersConstant {

    // Reset
    public static String RESET = "\033[0m";  // Text Reset
    // Regular Colors
    public static String BLACK = "\033[0;30m";   // BLACK
    public static String RED = "\033[0;31m";     // RED
    public static String GREEN = "\033[0;32m";   // GREEN
    public static String YELLOW = "\033[0;93m";  // YELLOW
    public static String BLUE = "\033[0;34m";    // BLUE
    public static String PURPLE = "\033[0;35m";  // PURPLE
    public static String GREY = "\033[0;37m";   // GREY
    public static String RED_BRIGHT = "\033[0;91m"; // RED BRIGHT
    public static final String WHITE_BRIGHT = "\033[0;97m"; // WHITE BRIGHT

    // Possible games
    public static int TWO_PLAYERS = 2;
    public static int THREE_PLAYERS = 3;
    public static int FOUR_PLAYERS = 4;

    // School boards
    public static int SPACE_BETWEEN_SCHOOL_BOARDS_LENGTH = 4;
    public static int SPACE_BETWEEN_SCHOOL_BOARDS_HEIGHT = 2;
    public static int SPACE_FOR_USERNAMES = 2;
    public static int START_OF_DINING_ROOM = 5;
    public static int SCHOOL_BOARD_HEIGHT = 11;
    public static int SCHOOL_BOARD_LENGTH = 32;
    public static int START_OF_TOWERS = 28;
    public static int START_OF_PROFESSORS = 26;

    // Mapping studentId -> color
    public static int STARTING_ID_YELLOW = 1;
    public static int ENDING_ID_YELLOW = 26;
    public static int STARTING_ID_BLUE = 27;
    public static int ENDING_ID_BLUE = 52;
    public static int STARTING_ID_GREEN = 53;
    public static int ENDING_ID_GREEN = 78;
    public static int STARTING_ID_RED = 79;
    public static int ENDING_ID_RED = 104;
    public static int STARTING_ID_PINK = 105;
    public static int ENDING_ID_PINK = 130;

    // Island tiles
    public static int ISLAND_TILE_HEIGHT = 5;
    public static int ISLAND_TILE_LENGTH = 12; // ISLAND_TILE_LENGTH = HORIZONTAL_LINE_LENGTH + 2 * OBLIQUE_LINE_LENGTH
    public static int ISLAND_GROUP_HEIGHT = 7; // height of island group
    public static int ISLAND_GROUP_LENGTH_OFFSET = 2; // cells saved after unification of two islands
    public static int HORIZONTAL_LINE_LENGTH = 8;
    public static int OBLIQUE_LINE_LENGTH = 2; // distance ab (length) and ac (height). ISLAND_TILE_HEIGHT = 2*OBLIQUE_LINE_LENGTH + 1
    public static int MAX_DIM_OF_ISLAND_GROUP = 9;

    /*
         c______
         /      \
       a/ b      \
        \        /
         \______/
     */

    // Cloud tiles
    public static int CLOUD_TILE_HEIGHT = 6; // 2 + 2 * OBLIQUE_LINE_LENGTH_CLOUD + VERTICAL_LINE_LENGTH_CLOUD
    public static int CLOUD_TILE_LENGTH = 15; // 2 * OBLIQUE_LINE_LENGTH_CLOUD + 2 * SPACE_BETWEEN_LAST_PLUS_OBLIQUE_AND_FIRST_PLUS_HORIZONTAL + 2 * HORIZONTAL_LINE_LENGTH_CLOUD - 1
    public static int SPACE_FOR_CLOUD_ID = 1;
    public static int SPACE_BETWEEN_CLOUD_TILES = 2;
    public static int HORIZONTAL_LINE_LENGTH_CLOUD = 4;
    public static int VERTICAL_LINE_LENGTH_CLOUD = 2; // it should be half the length of the horizontal line
    public static int OBLIQUE_LINE_LENGTH_CLOUD = 1; // it should increase by 1 every time the length of the
    public static int DISTANCE_BETWEEN_POINTS_OBLIQUE_LINE = 2;
    public static int SPACE_BETWEEN_LAST_PLUS_OBLIQUE_AND_FIRST_PLUS_HORIZONTAL = 2;

    /*
        + + + +
     +           +
   +               +
   +               +
     +           +
        + + + +
     */

    // Character card
    public static int CHARACTER_CARD_HEIGHT = 6; // CHARACTER_CARD_HEIGHT - 1 for "|" and 1 for "-"
    public static int CHARACTER_CARD_LENGTH = 8;
    public static int SPACE_BETWEEN_CHARACTER_CARDS = 2;
    public static int NUMBER_OF_CHARACTERS_OF_LONGEST_NAME = 15;
    public static int ADDITIONAL_SPACES_FOR_ID = 4;
    public static int OFFSET_OF_LEGEND = 1;
    public static int OFFSET_OF_ID_IN_CARD = 1; // distance between id and upper side
    public static int OFFSET_OF_COST_OF_CARD = 2; // // distance between cost and upper side

    /*
     ________  ________  ________
    |id: 1   ||id: 5   ||id: 9   | 1: Friar
    |cost: 3 ||cost: 3 ||cost: 3 | 5: Herbalist
    |        ||o   o   ||  oo    | 9: Mushroom Hunter
    |oo o    ||    o   || o      |
    |________||________||________|

    */
}