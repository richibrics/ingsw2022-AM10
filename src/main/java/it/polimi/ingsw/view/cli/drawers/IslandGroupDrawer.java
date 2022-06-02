package it.polimi.ingsw.view.cli.drawers;

import it.polimi.ingsw.model.exceptions.TowerNotSetException;
import it.polimi.ingsw.view.exceptions.IllegalStudentIdException;
import it.polimi.ingsw.view.game_objects.ClientIslandTile;
import it.polimi.ingsw.view.game_objects.ClientMotherNature;

import java.util.ArrayList;
import java.util.Random;

public class IslandGroupDrawer {

    /**
     * Generates the template of the island group {@code islandGroup} and fills it with student discs, towers, mother nature pawn and
     * no entry tiles if the island group contains one of the aforementioned game components.
     * @param islandGroup the island group
     * @param clientMotherNature the mother nature pawn
     * @return the matrix containing the complete representation of the island group
     * @throws IllegalStudentIdException if the student id is not valid
     */

    public static String[][] generateTemplateAndFillIslandGroup(ArrayList<ClientIslandTile> islandGroup, ClientMotherNature clientMotherNature) throws IllegalStudentIdException {

        String[][] template;

        // Case 1: one island in island group
        if (islandGroup.size() == 1) {
            // Create matrix
            template = new String[DrawersConstant.ISLAND_TILE_HEIGHT][DrawersConstant.ISLAND_TILE_LENGTH];
            // Generate template
            generateIslandTileTemplate(template, islandGroup.get(0).getId(), 0, 0);
            fillTemplate(template, clientMotherNature, islandGroup.get(0), 1, 1);
        }

        // Case 2: more than one island in island group
        else {
            template = new String[DrawersConstant.ISLAND_GROUP_HEIGHT]
                    [DrawersConstant.ISLAND_TILE_LENGTH + (islandGroup.size() - 1) * (DrawersConstant.ISLAND_TILE_LENGTH - DrawersConstant.ISLAND_GROUP_LENGTH_OFFSET)];
            int startingIndexHeight;
            int startingIndexLength = 0;

            for (int i = 0; i < islandGroup.size(); i++) {
                // The even islands are in a higher position in the matrix
                if (i % 2 == 0)
                    startingIndexHeight = 0;
                else
                    startingIndexHeight = DrawersConstant.OBLIQUE_LINE_LENGTH;

                generateIslandTileTemplate(template, islandGroup.get(i).getId(), startingIndexHeight, startingIndexLength);
                fillTemplate(template, clientMotherNature, islandGroup.get(i), startingIndexHeight + 1, startingIndexLength + 1);

                startingIndexLength += DrawersConstant.HORIZONTAL_LINE_LENGTH + DrawersConstant.OBLIQUE_LINE_LENGTH;
            }
        }
        // Remove null from matrix
        CliDrawersUtilityFunctions.
                removeNullAndAddSingleSpace(template);
        return template;
    }

    /**
     * Generates the template of the island tile starting from ({@code startingIndexHeight}, {@code startingIndexLength}).
     * @param template the matrix containing the representation of the island group to which the island tile belongs
     * @param islandId the id of the island tile
     * @param startingIndexHeight the starting height
     * @param startingIndexLength the starting length
     */

    private static void generateIslandTileTemplate(String[][] template, int islandId, int startingIndexHeight, int startingIndexLength) {

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
        i = startingIndexHeight + 2 * DrawersConstant.OBLIQUE_LINE_LENGTH; // 2 is the number of oblique sides on one side of the island tile
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

        // Add id to island tile
        i = startingIndexHeight + 1;
        j = startingIndexLength + DrawersConstant.OBLIQUE_LINE_LENGTH;
        char[] id = String.format("id %d", islandId).toCharArray();
        for (char c : id) {
            template[i][j] = String.valueOf(c);
            j++;
        }
    }

    /**
     * Fills the island tile with the student discs, the mother nature pawn, the towers and the no entry tile if the island
     * tile contains one of the aforementioned game components. The operation starts from ({@code startingIndexHeight}, {@code startingIndexLength}).
     * @param template the matrix containing the representation of the island group to which the island tile belongs
     * @param clientMotherNature the mother nature pawn
     * @param clientIslandTile the island tile
     * @param startingIndexHeight the starting height
     * @param startingIndexLength the starting height
     * @throws IllegalStudentIdException if the student id is invalid
     */

    private static void fillTemplate(String[][] template, ClientMotherNature clientMotherNature, ClientIslandTile clientIslandTile, int startingIndexHeight, int startingIndexLength)
            throws IllegalStudentIdException {

        ArrayList<Integer[]> arrayOfCoordinates = clearIslandTileAndGenerateSetOfCoordinates(template, startingIndexHeight, startingIndexLength);
        Random random = new Random();
        int indexOfRandomCoordinate;
        Integer[] randomCoordinates;

        // Add students to island tile
        for (Integer studentId : clientIslandTile.getStudents()) {
            indexOfRandomCoordinate = random.nextInt(arrayOfCoordinates.size());
            randomCoordinates = arrayOfCoordinates.remove(indexOfRandomCoordinate);
            template[randomCoordinates[0]][randomCoordinates[1]] = CliDrawersUtilityFunctions.getRepresentationOfStudentDisc(studentId);
        }

        // Add tower to island tile
        try {
            if (clientIslandTile.getTower() != null) {
                indexOfRandomCoordinate = random.nextInt(arrayOfCoordinates.size());
                randomCoordinates = arrayOfCoordinates.remove(indexOfRandomCoordinate);
                template[randomCoordinates[0]][randomCoordinates[1]] = CliDrawersUtilityFunctions.getRepresentationOfTower(clientIslandTile.getTower());
            }
        } catch (TowerNotSetException e) { /* No tower on the island */ }

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

    /**
     * Clears the island tile and generates a set of coordinates. The game components can be placed on the cells specified by
     * the coordinates in the set returned by this method.
     * @param template the matrix containing the island group to which the island tile belongs
     * @param firstAvailableRow the first available row
     * @param firstAvailableColumn the first available column
     * @return a set of coordinates in the form (height, length)
     */

    private static ArrayList<Integer[]> clearIslandTileAndGenerateSetOfCoordinates(String[][] template, int firstAvailableRow, int firstAvailableColumn) {

        ArrayList<Integer[]> arrayOfCoordinates = new ArrayList<>();
        int offset = 1;

        // Rows and columns are adjusted due to the presence of the id in the first available row of the island
        for (int i = firstAvailableRow + 1; i < firstAvailableRow + DrawersConstant.OBLIQUE_LINE_LENGTH; i++) {
            for (int j = firstAvailableColumn + DrawersConstant.OBLIQUE_LINE_LENGTH - offset - 1;
                 j < firstAvailableColumn + DrawersConstant.OBLIQUE_LINE_LENGTH + offset + DrawersConstant.HORIZONTAL_LINE_LENGTH - 1; j++) {
                arrayOfCoordinates.add(new Integer[]{i, j});
                // Clear content of island tile
                template[i][j] = " ";
            }
            offset++;
        }

        offset = 0;
        for (int i = firstAvailableRow + 2 * DrawersConstant.OBLIQUE_LINE_LENGTH - 2; i >= firstAvailableRow + DrawersConstant.OBLIQUE_LINE_LENGTH; i--) {
            for (int j = firstAvailableColumn + DrawersConstant.OBLIQUE_LINE_LENGTH - offset - 1;
                 j < firstAvailableColumn + DrawersConstant.OBLIQUE_LINE_LENGTH + offset + DrawersConstant.HORIZONTAL_LINE_LENGTH - 1; j++) {
                arrayOfCoordinates.add(new Integer[]{i, j});
                // Clear content of island tile
                template[i][j] = " ";
            }
            offset++;
        }
        return arrayOfCoordinates;
    }
}