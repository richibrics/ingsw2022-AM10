package it.polimi.ingsw.view.cli.drawers;

import it.polimi.ingsw.model.exceptions.TowerNotSetException;
import it.polimi.ingsw.view.cli.exceptions.IllegalProfessorIdException;
import it.polimi.ingsw.view.cli.exceptions.IllegalStudentIdException;
import it.polimi.ingsw.view.cli.exceptions.WrongNumberOfPlayersException;
import it.polimi.ingsw.view.game_objects.*;

import java.util.ArrayList;

public class SchoolBoardDrawer {

    public static void removeNull(String[][] template) {

        for (int i = 0; i < template.length; i++)
            for (int j = 0; j < template[i].length; j++)
                if (template[i][j] == null)
                    template[i][j] = " ";
    }

    public static String[][] generateTemplate(int numOfPlayers) throws WrongNumberOfPlayersException {

        if (numOfPlayers == DrawersConstant.TWO_PLAYERS) {
            String[][] schoolBoards = new String[2 * DrawersConstant.SCHOOL_BOARD_HEIGHT + 2 * DrawersConstant.SPACE_FOR_USERNAMES + DrawersConstant.SPACE_BETWEEN_SCHOOL_BOARDS_HEIGHT]
                    [DrawersConstant.SCHOOL_BOARD_LENGTH];
            schoolBoardTemplate(schoolBoards, 0, DrawersConstant.SPACE_FOR_USERNAMES);
            schoolBoardTemplate(schoolBoards, 0, 2 * DrawersConstant.SPACE_FOR_USERNAMES + DrawersConstant.SCHOOL_BOARD_HEIGHT + DrawersConstant.SPACE_BETWEEN_SCHOOL_BOARDS_HEIGHT);
            removeNull(schoolBoards);
            return schoolBoards;
        } else if (numOfPlayers == DrawersConstant.THREE_PLAYERS || numOfPlayers == DrawersConstant.FOUR_PLAYERS) {
            String[][] schoolBoards = new String[2 * DrawersConstant.SCHOOL_BOARD_HEIGHT + 2 * DrawersConstant.SPACE_FOR_USERNAMES + DrawersConstant.SPACE_BETWEEN_SCHOOL_BOARDS_HEIGHT]
                    [2 * DrawersConstant.SCHOOL_BOARD_LENGTH + DrawersConstant.SPACE_BETWEEN_SCHOOL_BOARDS_LENGTH];
            schoolBoardTemplate(schoolBoards, 0, DrawersConstant.SPACE_FOR_USERNAMES);
            schoolBoardTemplate(schoolBoards, DrawersConstant.SCHOOL_BOARD_LENGTH + DrawersConstant.SPACE_BETWEEN_SCHOOL_BOARDS_LENGTH, DrawersConstant.SPACE_FOR_USERNAMES);
            if (numOfPlayers == DrawersConstant.THREE_PLAYERS)
                schoolBoardTemplate(schoolBoards, 0, 2 * DrawersConstant.SPACE_FOR_USERNAMES + DrawersConstant.SCHOOL_BOARD_HEIGHT + DrawersConstant.SPACE_BETWEEN_SCHOOL_BOARDS_HEIGHT);
            else {
                schoolBoardTemplate(schoolBoards, 0, 2 * DrawersConstant.SPACE_FOR_USERNAMES + DrawersConstant.SCHOOL_BOARD_HEIGHT + DrawersConstant.SPACE_BETWEEN_SCHOOL_BOARDS_HEIGHT);
                schoolBoardTemplate(schoolBoards, DrawersConstant.SCHOOL_BOARD_LENGTH + DrawersConstant.SPACE_BETWEEN_SCHOOL_BOARDS_LENGTH,
                        2 * DrawersConstant.SPACE_FOR_USERNAMES + DrawersConstant.SCHOOL_BOARD_HEIGHT + DrawersConstant.SPACE_BETWEEN_SCHOOL_BOARDS_HEIGHT);
            }
            removeNull(schoolBoards);
            return schoolBoards;
        } else
            throw new WrongNumberOfPlayersException();
    }

    private static void schoolBoardTemplate(String[][] schoolBoard, int startingIndexLength, int startingIndexHeight) {

        // Generate school board borders
        for (int j = startingIndexLength; j < startingIndexLength + DrawersConstant.SCHOOL_BOARD_LENGTH; j++) {
            if (j == startingIndexLength || j == startingIndexLength + DrawersConstant.START_OF_DINING_ROOM - 1 ||
                    j == startingIndexLength + DrawersConstant.START_OF_PROFESSORS - 1 ||
                    j == startingIndexLength + DrawersConstant.START_OF_TOWERS - 1 || j == startingIndexLength + DrawersConstant.SCHOOL_BOARD_LENGTH - 1) {
                schoolBoard[startingIndexHeight][j] = "•";
                schoolBoard[startingIndexHeight + DrawersConstant.SCHOOL_BOARD_HEIGHT - 1][j] = "•";
            } else {
                schoolBoard[startingIndexHeight][j] = "—";
                schoolBoard[startingIndexHeight + DrawersConstant.SCHOOL_BOARD_HEIGHT - 1][j] = "—";
            }
        }

        // Generate separators of lanes in dining room
        for (int i = startingIndexHeight + 2; i < startingIndexHeight + DrawersConstant.SCHOOL_BOARD_HEIGHT - 2; i += 2)
            for (int j = startingIndexLength + DrawersConstant.START_OF_DINING_ROOM; j <= startingIndexLength + DrawersConstant.START_OF_PROFESSORS - 1; j++)
                schoolBoard[i][j] = "—";

        // Generate separators
        for (int i = startingIndexHeight + 1; i < startingIndexHeight + DrawersConstant.SCHOOL_BOARD_HEIGHT - 1; i++) {
            schoolBoard[i][startingIndexLength] = "|";
            schoolBoard[i][startingIndexLength + DrawersConstant.START_OF_DINING_ROOM - 1] = "|";
            schoolBoard[i][startingIndexLength + DrawersConstant.START_OF_PROFESSORS - 1] = "|";
            schoolBoard[i][startingIndexLength + DrawersConstant.START_OF_TOWERS - 1] = "|";
            schoolBoard[i][startingIndexLength + DrawersConstant.SCHOOL_BOARD_LENGTH - 1] = "|";
        }
    }

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

    private static void fillSchoolBoardsTeam(String[][] template, ArrayList<ClientSchoolBoard> clientSchoolBoards, int startingIndexSchoolBoard,
                                      ClientTeam clientTeam, int startingIndexLength, int startingIndexHeight) throws IllegalStudentIdException, IllegalProfessorIdException, TowerNotSetException {

        // Write username and coins
        writeUsernameAndCoins(template, clientTeam.getPlayers().get(0).getUsername(), clientTeam.getPlayers().get(0).getCoins(),
                startingIndexLength, startingIndexHeight);

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
                    startingIndexLengthSecondBoard, startingIndexHeight);

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

    private static void fillEntrance(String[][] template, ClientSchoolBoard clientSchoolBoard, int startingIndexLength, int startingIndexHeight) throws IllegalStudentIdException {

        int i = startingIndexHeight;
        int j = startingIndexLength;

        for (int studentId : clientSchoolBoard.getEntrance()) {

            if (i == startingIndexHeight + DrawersConstant.SCHOOL_BOARD_HEIGHT - 1) {
                j += 2;
                template[startingIndexHeight][j] = IdToColorConverter.getRepresentationOfStudentDisc(studentId);
                i = startingIndexHeight + 2;
            } else {
                template[i][j] = IdToColorConverter.getRepresentationOfStudentDisc(studentId);
                i += 2;
            }
        }
    }

    private static void fillDiningRoom(String[][] template, ClientSchoolBoard clientSchoolBoard, int startingIndexLength, int startingIndexHeight) throws IllegalStudentIdException {

        int i = startingIndexHeight;
        int j = startingIndexLength;

        for (ArrayList<Integer> lane : clientSchoolBoard.getDiningRoom()) {
            for (int id : lane) {
                template[i][j] = IdToColorConverter.getRepresentationOfStudentDisc(id);
                j += 2;
            }
            i += 2;
            j = startingIndexLength;
        }
    }

    private static void fillProfessorSection(String[][] template, ClientTeam clientTeam, int startingIndexLength, int startingIndexHeight) throws IllegalProfessorIdException {

        int i = startingIndexHeight;
        int j = startingIndexLength;

        for (ClientPawnColor professor : clientTeam.getProfessorPawns()) {
            template[i][j] = IdToColorConverter.getRepresentationOfProfessorPawn(professor.getId());
            i += 2;
        }
    }

    private static void fillTowersSection(String[][] template, ClientTeam clientTeam, int startingIndexLength, int startingIndexHeight) throws TowerNotSetException {

        int i = startingIndexHeight;
        int j = startingIndexLength;

        String tower = IdToColorConverter.getRepresentationOfTower(clientTeam.getTowersColor());
        for (int towerNumber = 0; towerNumber < clientTeam.getNumberOfTowers(); towerNumber++) {

            // TODO try to understand how to remove - 3
            if (i == startingIndexHeight + DrawersConstant.SCHOOL_BOARD_HEIGHT - 3) {
                j += 2;
                template[startingIndexHeight][j] = tower;
                i = startingIndexHeight + 2;
            } else {
                template[i][j] = tower;
                i += 2;
            }
        }
    }

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