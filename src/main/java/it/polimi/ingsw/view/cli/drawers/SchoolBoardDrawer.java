package it.polimi.ingsw.view.cli.drawers;

import it.polimi.ingsw.model.exceptions.TowerNotSetException;
import it.polimi.ingsw.view.cli.exceptions.IllegalProfessorIdException;
import it.polimi.ingsw.view.cli.exceptions.IllegalStudentIdException;
import it.polimi.ingsw.view.cli.exceptions.WrongNumberOfPlayersException;
import it.polimi.ingsw.view.game_objects.*;

import java.util.ArrayList;

public class SchoolBoardDrawer {

    /**
     * Generates the matrix that represents the school boards.
     *
     * @param numOfPlayers the number of players in the game
     * @return the template with the representation of the school boards
     * @throws WrongNumberOfPlayersException if the number of players is invalid
     */

    public static String[][] generateTemplate(int numOfPlayers) throws WrongNumberOfPlayersException {

        // Case 1: two players game
        if (numOfPlayers == DrawersConstant.TWO_PLAYERS) {
            String[][] schoolBoards = new String[2 * DrawersConstant.SCHOOL_BOARD_HEIGHT + 2 * DrawersConstant.SPACE_FOR_USERNAMES + DrawersConstant.SPACE_BETWEEN_SCHOOL_BOARDS_HEIGHT]
                    [DrawersConstant.SCHOOL_BOARD_LENGTH];
            schoolBoardTemplate(schoolBoards, 0, DrawersConstant.SPACE_FOR_USERNAMES);
            schoolBoardTemplate(schoolBoards, 0, 2 * DrawersConstant.SPACE_FOR_USERNAMES + DrawersConstant.SCHOOL_BOARD_HEIGHT + DrawersConstant.SPACE_BETWEEN_SCHOOL_BOARDS_HEIGHT);
            UtilityFunctions.removeNullAndAddSingleSpace(schoolBoards);
            return schoolBoards;
        }

        // Case 2: three players game and four players game
        else if (numOfPlayers == DrawersConstant.THREE_PLAYERS || numOfPlayers == DrawersConstant.FOUR_PLAYERS) {
            String[][] schoolBoards = new String[2 * DrawersConstant.SCHOOL_BOARD_HEIGHT + 2 * DrawersConstant.SPACE_FOR_USERNAMES + DrawersConstant.SPACE_BETWEEN_SCHOOL_BOARDS_HEIGHT]
                    [2 * DrawersConstant.SCHOOL_BOARD_LENGTH + DrawersConstant.SPACE_BETWEEN_SCHOOL_BOARDS_LENGTH];
            // Create school board templates for first two players
            schoolBoardTemplate(schoolBoards, 0, DrawersConstant.SPACE_FOR_USERNAMES);
            schoolBoardTemplate(schoolBoards, DrawersConstant.SCHOOL_BOARD_LENGTH + DrawersConstant.SPACE_BETWEEN_SCHOOL_BOARDS_LENGTH, DrawersConstant.SPACE_FOR_USERNAMES);
            // Case 2.1: three players game
            if (numOfPlayers == DrawersConstant.THREE_PLAYERS)
                schoolBoardTemplate(schoolBoards, 0, 2 * DrawersConstant.SPACE_FOR_USERNAMES + DrawersConstant.SCHOOL_BOARD_HEIGHT + DrawersConstant.SPACE_BETWEEN_SCHOOL_BOARDS_HEIGHT);
                // Case 2.2: four players game
            else {
                schoolBoardTemplate(schoolBoards, 0, 2 * DrawersConstant.SPACE_FOR_USERNAMES + DrawersConstant.SCHOOL_BOARD_HEIGHT + DrawersConstant.SPACE_BETWEEN_SCHOOL_BOARDS_HEIGHT);
                schoolBoardTemplate(schoolBoards, DrawersConstant.SCHOOL_BOARD_LENGTH + DrawersConstant.SPACE_BETWEEN_SCHOOL_BOARDS_LENGTH,
                        2 * DrawersConstant.SPACE_FOR_USERNAMES + DrawersConstant.SCHOOL_BOARD_HEIGHT + DrawersConstant.SPACE_BETWEEN_SCHOOL_BOARDS_HEIGHT);
            }
            UtilityFunctions.removeNullAndAddSingleSpace(schoolBoards);
            return schoolBoards;
        } else
            throw new WrongNumberOfPlayersException();
    }

    /**
     * Generates the representation of a school board starting from coordinates ({@code startingIndexHeight}, {@code startingIndexLength}).
     *
     * @param schoolBoards        the matrix containing all the school boards
     * @param startingIndexLength the starting length
     * @param startingIndexHeight the starting height
     */

    private static void schoolBoardTemplate(String[][] schoolBoards, int startingIndexLength, int startingIndexHeight) {

        // Generate school board borders
        for (int j = startingIndexLength; j < startingIndexLength + DrawersConstant.SCHOOL_BOARD_LENGTH; j++) {
            if (j == startingIndexLength || j == startingIndexLength + DrawersConstant.START_OF_DINING_ROOM - 1 ||
                    j == startingIndexLength + DrawersConstant.START_OF_PROFESSORS - 1 ||
                    j == startingIndexLength + DrawersConstant.START_OF_TOWERS - 1 || j == startingIndexLength + DrawersConstant.SCHOOL_BOARD_LENGTH - 1) {
                schoolBoards[startingIndexHeight][j] = "•";
                schoolBoards[startingIndexHeight + DrawersConstant.SCHOOL_BOARD_HEIGHT - 1][j] = "•";
            } else {
                schoolBoards[startingIndexHeight][j] = "—";
                schoolBoards[startingIndexHeight + DrawersConstant.SCHOOL_BOARD_HEIGHT - 1][j] = "—";
            }
        }

        // Generate separators of lanes in dining room
        for (int i = startingIndexHeight + 2; i < startingIndexHeight + DrawersConstant.SCHOOL_BOARD_HEIGHT - 2; i += 2)
            for (int j = startingIndexLength + DrawersConstant.START_OF_DINING_ROOM; j <= startingIndexLength + DrawersConstant.START_OF_PROFESSORS - 1; j++)
                schoolBoards[i][j] = "—";

        // Generate separators
        for (int i = startingIndexHeight + 1; i < startingIndexHeight + DrawersConstant.SCHOOL_BOARD_HEIGHT - 1; i++) {
            schoolBoards[i][startingIndexLength] = "|";
            schoolBoards[i][startingIndexLength + DrawersConstant.START_OF_DINING_ROOM - 1] = "|";
            schoolBoards[i][startingIndexLength + DrawersConstant.START_OF_PROFESSORS - 1] = "|";
            schoolBoards[i][startingIndexLength + DrawersConstant.START_OF_TOWERS - 1] = "|";
            schoolBoards[i][startingIndexLength + DrawersConstant.SCHOOL_BOARD_LENGTH - 1] = "|";
        }
    }

    /**
     * Fills the school boards contained in {@code template}.
     *
     * @param template    the template with the representation of the school boards
     * @param clientTable the ClientTable object
     * @param clientTeams the ClientTeams object
     * @throws Exception if something bad happens
     */

    public static void fillTemplate(String[][] template, ClientTable clientTable, ClientTeams clientTeams) throws Exception {

        // Fill school boards of first team
        fillSchoolBoardsTeam(template, clientTable.getSchoolBoards(), 0, clientTeams.getTeams().get(0), 0, 0);

        // If the number of players equals four, fill school boards of second team. Get school boards 3 and 4
        if (clientTeams.getTeams().size() == DrawersConstant.FOUR_PLAYERS)
            fillSchoolBoardsTeam(template, clientTable.getSchoolBoards(), 2, clientTeams.getTeams().get(1), 0,
                    DrawersConstant.SCHOOL_BOARD_HEIGHT + DrawersConstant.SPACE_BETWEEN_SCHOOL_BOARDS_HEIGHT + DrawersConstant.SPACE_FOR_USERNAMES);

        else if (clientTeams.getTeams().size() == DrawersConstant.TWO_PLAYERS || clientTeams.getTeams().size() == DrawersConstant.THREE_PLAYERS) {

            // Fill school board of second team
            fillSchoolBoardsTeam(template, clientTable.getSchoolBoards(), 1, clientTeams.getTeams().get(1),
                    0, DrawersConstant.SCHOOL_BOARD_HEIGHT + DrawersConstant.SPACE_FOR_USERNAMES + DrawersConstant.SPACE_BETWEEN_SCHOOL_BOARDS_HEIGHT);

            // If the number of players equals three, fill school board of third team
            if (clientTeams.getTeams().size() == DrawersConstant.THREE_PLAYERS)
                fillSchoolBoardsTeam(template, clientTable.getSchoolBoards(), 2, clientTeams.getTeams().get(2),
                        DrawersConstant.SCHOOL_BOARD_LENGTH + DrawersConstant.SPACE_BETWEEN_SCHOOL_BOARDS_LENGTH, 0);
        }
    }

    /**
     * Fills the school boards of {@code clientTeam}. This method adds to the school boards of {@code clientTeam}
     * the student discs, the towers and the professor pawns. The coordinates ({@code startingIndexHeight}, {@code startingIndexLength})
     * specify the location of the first school board that must be filled.
     *
     * @param template                 the matrix which contains the school boards
     * @param clientSchoolBoards       a list of school boards. Each element of the list contains the game components placed
     *                                 on the school board
     * @param startingIndexSchoolBoard the index of the first school board that must be filled
     * @param clientTeam               the ClientTeam object
     * @param startingIndexLength      the starting length
     * @param startingIndexHeight      the starting height
     * @throws IllegalStudentIdException   if a student disc id is invalid
     * @throws IllegalProfessorIdException if a professor pawn id is invalid
     * @throws TowerNotSetException        if the towers have not been set
     */

    private static void fillSchoolBoardsTeam(String[][] template, ArrayList<ClientSchoolBoard> clientSchoolBoards, int startingIndexSchoolBoard,
                                             ClientTeam clientTeam, int startingIndexLength, int startingIndexHeight) throws IllegalStudentIdException, IllegalProfessorIdException, TowerNotSetException {

        // Write username and coins
        writeUsernameAndCoins(template, clientTeam.getPlayers().get(0).getUsername(), clientTeam.getPlayers().get(0).getCoins(),
                startingIndexLength + 1, startingIndexHeight);

        // Fill entrance
        fillEntrance(template, clientSchoolBoards.get(startingIndexSchoolBoard), startingIndexLength + 1,
                startingIndexHeight + DrawersConstant.SPACE_FOR_USERNAMES + 1);

        // Fill dining room
        fillDiningRoom(template, clientSchoolBoards.get(startingIndexSchoolBoard), startingIndexLength + DrawersConstant.START_OF_DINING_ROOM,
                startingIndexHeight + DrawersConstant.SPACE_FOR_USERNAMES + 1);

        // Fill professors section
        fillProfessorSection(template, clientTeam, startingIndexLength + DrawersConstant.START_OF_PROFESSORS,
                startingIndexHeight + DrawersConstant.SPACE_FOR_USERNAMES + 1);

        // Fill towers section
        fillTowersSection(template, clientTeam, startingIndexLength + DrawersConstant.START_OF_TOWERS,
                startingIndexHeight + DrawersConstant.SPACE_FOR_USERNAMES + 2);

        // If the team is composed of two players, fill second school
        if (clientTeam.getPlayers().size() == 2) {
            int startingIndexLengthSecondBoard = startingIndexLength + DrawersConstant.SCHOOL_BOARD_LENGTH + DrawersConstant.SPACE_BETWEEN_SCHOOL_BOARDS_LENGTH;

            // Write username and coins
            writeUsernameAndCoins(template, clientTeam.getPlayers().get(1).getUsername(), clientTeam.getPlayers().get(0).getCoins(),
                    startingIndexLengthSecondBoard + 1, startingIndexHeight);

            // Fill entrance
            fillEntrance(template, clientSchoolBoards.get(startingIndexSchoolBoard + 1), startingIndexLengthSecondBoard + 1,
                    startingIndexHeight + DrawersConstant.SPACE_FOR_USERNAMES + 1);

            // Fill dining room
            fillDiningRoom(template, clientSchoolBoards.get(startingIndexSchoolBoard + 1),
                    startingIndexLengthSecondBoard + DrawersConstant.START_OF_DINING_ROOM, startingIndexHeight + DrawersConstant.SPACE_FOR_USERNAMES + 1);

            // Fill professors section
            fillProfessorSection(template, clientTeam, startingIndexLengthSecondBoard + DrawersConstant.START_OF_PROFESSORS,
                    startingIndexHeight + DrawersConstant.SPACE_FOR_USERNAMES + 1);

            // Fill towers section
            fillTowersSection(template, clientTeam, startingIndexLengthSecondBoard + DrawersConstant.START_OF_TOWERS,
                    startingIndexHeight + DrawersConstant.SPACE_FOR_USERNAMES + 2);
        }
    }

    /**
     * Fills the entrance of the school board starting from ({@code startingIndexHeight}, {@code startingIndexLength}).
     *
     * @param template            the matrix containing the school boards
     * @param clientSchoolBoard   the ClientSchoolBoard object
     * @param startingIndexLength the starting length
     * @param startingIndexHeight the starting height
     * @throws IllegalStudentIdException if a student disc id is invalid
     */

    private static void fillEntrance(String[][] template, ClientSchoolBoard clientSchoolBoard, int startingIndexLength, int startingIndexHeight) throws IllegalStudentIdException {

        int i = startingIndexHeight;
        int j = startingIndexLength;

        // Empty entrance
        UtilityFunctions.emptySectionWithPawnsOrTowers(template, startingIndexLength, startingIndexHeight, DrawersConstant.SCHOOL_BOARD_HEIGHT - 1, DrawersConstant.SPACE_BETWEEN_STUDENTS, 0);

        // Fill entrance
        for (int studentId : clientSchoolBoard.getEntrance()) {
            if (i == startingIndexHeight + DrawersConstant.SCHOOL_BOARD_HEIGHT - 1) {
                j += DrawersConstant.SPACE_BETWEEN_STUDENTS;
                template[startingIndexHeight][j] = UtilityFunctions.getRepresentationOfStudentDisc(studentId);
                i = startingIndexHeight + DrawersConstant.SPACE_BETWEEN_STUDENTS;
            } else {
                template[i][j] = UtilityFunctions.getRepresentationOfStudentDisc(studentId);
                i += DrawersConstant.SPACE_BETWEEN_STUDENTS;
            }
        }
    }

    /**
     * Fills the dining room of the school board starting from ({@code startingIndexHeight}, {@code startingIndexLength}).
     *
     * @param template            the matrix containing the school boards
     * @param clientSchoolBoard   the ClientSchoolBoard object
     * @param startingIndexLength the starting length
     * @param startingIndexHeight the starting height
     * @throws IllegalStudentIdException if a student disc id is invalid
     */

    private static void fillDiningRoom(String[][] template, ClientSchoolBoard clientSchoolBoard, int startingIndexLength, int startingIndexHeight) throws IllegalStudentIdException {

        int i = startingIndexHeight;
        int j = startingIndexLength;

        for (ArrayList<Integer> lane : clientSchoolBoard.getDiningRoom()) {
            for (int id : lane) {
                template[i][j] = UtilityFunctions.getRepresentationOfStudentDisc(id);
                j += DrawersConstant.SPACE_BETWEEN_STUDENTS;
            }
            i += DrawersConstant.SPACE_BETWEEN_STUDENTS;
            j = startingIndexLength;
        }
    }

    /**
     * Fills the professor pawns section of the school board starting from ({@code startingIndexHeight}, {@code startingIndexLength}).
     *
     * @param template            the matrix containing the school boards
     * @param clientTeam          the ClientTeam object
     * @param startingIndexLength the starting length
     * @param startingIndexHeight the starting height
     * @throws IllegalProfessorIdException if a professor pawn id is invalid
     */

    private static void fillProfessorSection(String[][] template, ClientTeam clientTeam, int startingIndexLength, int startingIndexHeight) throws IllegalProfessorIdException {

        int i;
        int j = startingIndexLength;

        // Empty professor pawns section
        UtilityFunctions.emptySectionWithPawnsOrTowers(template, startingIndexLength, startingIndexHeight, DrawersConstant.SCHOOL_BOARD_HEIGHT - 1, DrawersConstant.SPACE_BETWEEN_PROFESSORS_IN_LENGTH, 2);

        for (ClientPawnColor professor : clientTeam.getProfessorPawns()) {
            i = startingIndexHeight + DrawersConstant.SPACE_BETWEEN_PROFESSORS_IN_HEIGHT * professor.getId();
            template[i][j] = UtilityFunctions.getRepresentationOfProfessorPawn(professor.getId());
        }
    }

    /**
     * Fills the towers section of the school board starting from ({@code startingIndexHeight}, {@code startingIndexLength}).
     *
     * @param template            the matrix containing the school boards
     * @param clientTeam          the ClientTeam object
     * @param startingIndexLength the starting length
     * @param startingIndexHeight the starting height
     * @throws TowerNotSetException if the towers have not been set
     */

    private static void fillTowersSection(String[][] template, ClientTeam clientTeam, int startingIndexLength, int startingIndexHeight) throws TowerNotSetException {

        int i = startingIndexHeight;
        int j = startingIndexLength;

        // Empty section with towers
        UtilityFunctions.emptySectionWithPawnsOrTowers(template, startingIndexLength, startingIndexHeight,DrawersConstant.SCHOOL_BOARD_HEIGHT - 3 , DrawersConstant.SPACE_BETWEEN_TOWERS, 1);

        // Get representation of tower
        String tower = UtilityFunctions.getRepresentationOfTower(clientTeam.getTowersColor());

        // Calculate ending height for towers
        int halfNumberOfTowers;
        if (clientTeam.getNumberOfTowers() % 2 == 1)
            halfNumberOfTowers = Math.round(clientTeam.getNumberOfTowers()/2) + 1;
        else
            halfNumberOfTowers = clientTeam.getNumberOfTowers()/2;

        int endingHeightForTowers = startingIndexHeight + halfNumberOfTowers * DrawersConstant.SPACE_BETWEEN_TOWERS;
        // Add towers to section
        for (int towerNumber = 0; towerNumber < clientTeam.getNumberOfTowers(); towerNumber++) {

            if (i == endingHeightForTowers) {
                j += DrawersConstant.SPACE_BETWEEN_TOWERS;
                template[startingIndexHeight][j] = tower;
                i = startingIndexHeight + DrawersConstant.SPACE_BETWEEN_TOWERS;
            } else {
                template[i][j] = tower;
                i += DrawersConstant.SPACE_BETWEEN_TOWERS;
            }
        }
    }

    /**
     * Writes the username of the player and the coins of the player starting from ({@code indexHeight}, {@code startingIndexLength}).
     *
     * @param template            the matrix containing the school boards
     * @param username            the username of the player
     * @param coins               the amount of coins owned by the player
     * @param startingIndexLength the starting length
     * @param indexHeight         the height
     */

    private static void writeUsernameAndCoins(String[][] template, String username, int coins, int startingIndexLength, int indexHeight) {
        char[] usernameChars = username.toCharArray();
        int index = startingIndexLength;
        for (char c : usernameChars) {
            template[indexHeight][index] = String.valueOf(c);
            index++;
        }
        index += 2;
        char[] coin = "coins".toCharArray();
        for (char c : coin) {
            template[indexHeight][index] = String.valueOf(c);
            index++;
        }
        template[indexHeight][index] = ":";
        template[indexHeight][index + 1] = String.valueOf(coins);
    }
}