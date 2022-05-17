package it.polimi.ingsw.view.cli.drawers;

import it.polimi.ingsw.model.exceptions.TowerNotSetException;
import it.polimi.ingsw.view.cli.exceptions.IllegalStudentIdException;
import it.polimi.ingsw.view.game_objects.ClientIslandTile;
import it.polimi.ingsw.view.game_objects.ClientMotherNature;

import java.util.ArrayList;
import java.util.Random;

public class IslandGroupDrawer {

    public static String[][] generateTemplateAndFillIslandGroup(ArrayList<ClientIslandTile> islandGroup, ClientMotherNature clientMotherNature) throws IllegalStudentIdException, TowerNotSetException {

        String[][] template;

        // Case 1: one island in island group
        if (islandGroup.size() == 1) {
            // Create matrix
            template = new String[DrawersConstant.ISLAND_TILE_HEIGHT][DrawersConstant.ISLAND_TILE_LENGTH];
            // Generate template
            generateIslandTileTemplate(template, 0, 0);
            fillTemplate(template, clientMotherNature, islandGroup.get(0), 1, 1);
        }

        // Case 2: more than one island in island group
        else {
            template = new String[DrawersConstant.ISLAND_GROUP_HEIGHT]
                    [DrawersConstant.ISLAND_TILE_LENGTH + (islandGroup.size() - 1) * (DrawersConstant.ISLAND_TILE_LENGTH - DrawersConstant.ISLAND_GROUP_LENGTH_OFFSET)];
            int startingIndexHeight = 0;
            int startingIndexLength = 0;

            for (int i = 0; i < islandGroup.size(); i++) {
                // The even islands are in a higher position in the matrix
                if (i % 2 == 0)
                    startingIndexHeight = 0;
                else
                    startingIndexHeight = DrawersConstant.OBLIQUE_LINE_LENGTH;

                generateIslandTileTemplate(template, startingIndexHeight, startingIndexLength);
                fillTemplate(template, clientMotherNature, islandGroup.get(i), startingIndexHeight + 1, startingIndexLength + 1);

                startingIndexLength += DrawersConstant.HORIZONTAL_LINE_LENGTH + DrawersConstant.OBLIQUE_LINE_LENGTH;
            }
        }
        // Remove null from matrix
        SchoolBoardDrawer.removeNull(template);
        return template;
    }


    private static void generateIslandTileTemplate(String[][] template, int startingIndexHeight, int startingIndexLength) {

        // Generate sides with "/"
        int i = startingIndexHeight + DrawersConstant.OBLIQUE_LINE_LENGTH;
        int j = startingIndexLength;
        for (int k = 0; k < DrawersConstant.OBLIQUE_LINE_LENGTH; k++) {
            template[i][j] = "/";
            template[i + DrawersConstant.OBLIQUE_LINE_LENGTH][j + DrawersConstant.OBLIQUE_LINE_LENGTH + DrawersConstant.HORIZONTAL_LINE_LENGTH] = "/";
            i--;
            j++;
        }

        // Generate sides with "\"
        i = startingIndexHeight + 2 * DrawersConstant.OBLIQUE_LINE_LENGTH;
        j = startingIndexLength + DrawersConstant.OBLIQUE_LINE_LENGTH - 1;
        for (int k = 0; k < DrawersConstant.OBLIQUE_LINE_LENGTH; k++) {
            template[i][j] = "\\";
            template[i - DrawersConstant.OBLIQUE_LINE_LENGTH][j + DrawersConstant.OBLIQUE_LINE_LENGTH + DrawersConstant.HORIZONTAL_LINE_LENGTH] = "\\";
            i--;
            j--;
        }

        // Generate horizontal sides
        for (int k = startingIndexLength + DrawersConstant.OBLIQUE_LINE_LENGTH;
             k < startingIndexLength + DrawersConstant.OBLIQUE_LINE_LENGTH + DrawersConstant.HORIZONTAL_LINE_LENGTH; k++) {
            template[startingIndexHeight][k] = "_";
            template[startingIndexHeight + DrawersConstant.ISLAND_TILE_HEIGHT - 1][k] = "_";
        }
    }

    private static void fillTemplate(String[][] template, ClientMotherNature clientMotherNature, ClientIslandTile clientIslandTile, int startingIndexHeight, int startingIndexLength)
            throws IllegalStudentIdException, TowerNotSetException {

        ArrayList<Integer[]> arrayOfCoordinates = generateSetOfCoordinates(startingIndexHeight, startingIndexLength);
        Random random = new Random();
        int indexOfRandomCoordinate;
        Integer[] randomCoordinates;

        // Add students to island tile
        for (Integer studentId : clientIslandTile.getStudents()) {
            indexOfRandomCoordinate = random.nextInt(arrayOfCoordinates.size());
            randomCoordinates = arrayOfCoordinates.remove(indexOfRandomCoordinate);
            template[randomCoordinates[0]][randomCoordinates[1]] = IdToColorConverter.getRepresentationOfStudentDisc(studentId);
        }

        // Add tower to island tile
        if (clientIslandTile.getTower() != null) {
            indexOfRandomCoordinate = random.nextInt(arrayOfCoordinates.size());
            randomCoordinates = arrayOfCoordinates.remove(indexOfRandomCoordinate);
            template[randomCoordinates[0]][randomCoordinates[1]] = IdToColorConverter.getRepresentationOfTower(clientIslandTile.getTower());
        }

        // Add no entry to island tile
        if (clientIslandTile.hasNoEntry()) {
            indexOfRandomCoordinate = random.nextInt(arrayOfCoordinates.size());
            randomCoordinates = arrayOfCoordinates.remove(indexOfRandomCoordinate);
            template[randomCoordinates[0]][randomCoordinates[1]] = DrawersConstant.RED_BRIGHT + "X" + DrawersConstant.RESET;
        }

        // Add mother nature pawn to island tile
        if (clientIslandTile.getId() == clientMotherNature.getIsland()) {
            indexOfRandomCoordinate = random.nextInt(arrayOfCoordinates.size());
            randomCoordinates = arrayOfCoordinates.remove(indexOfRandomCoordinate);
            template[randomCoordinates[0]][randomCoordinates[1]] = DrawersConstant.RED_BRIGHT + "M" + DrawersConstant.RESET;
        }

    }

    private static ArrayList<Integer[]> generateSetOfCoordinates(int firstAvailableRow, int firstAvailableColumn) {

        ArrayList<Integer[]> arrayOfCoordinates = new ArrayList<>();
        int offset = 0;

        for (int i = firstAvailableRow; i < firstAvailableRow + DrawersConstant.OBLIQUE_LINE_LENGTH; i++) {
            for (int j = firstAvailableColumn + DrawersConstant.OBLIQUE_LINE_LENGTH - offset - 1;
                 j < firstAvailableColumn + DrawersConstant.OBLIQUE_LINE_LENGTH + offset + DrawersConstant.HORIZONTAL_LINE_LENGTH - 1; j++)
                arrayOfCoordinates.add(new Integer[]{i, j});
            offset++;
        }

        offset = 0;
        for (int i = firstAvailableRow + 2 * DrawersConstant.OBLIQUE_LINE_LENGTH - 2; i >= firstAvailableRow + DrawersConstant.OBLIQUE_LINE_LENGTH; i--) {
            for (int j = firstAvailableColumn + DrawersConstant.OBLIQUE_LINE_LENGTH - offset - 1;
                 j < firstAvailableColumn + DrawersConstant.OBLIQUE_LINE_LENGTH + offset + DrawersConstant.HORIZONTAL_LINE_LENGTH - 1; j++)
                arrayOfCoordinates.add(new Integer[]{i, j});
            offset++;
        }
        return arrayOfCoordinates;
    }
}