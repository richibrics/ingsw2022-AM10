package it.polimi.ingsw.view.cli.drawers;

import it.polimi.ingsw.view.cli.exceptions.IllegalStudentIdException;
import it.polimi.ingsw.view.game_objects.ClientCloudTile;

import java.util.ArrayList;
import java.util.Random;

public class CloudTilesDrawer {

    /**
     * Generates the template which contains the cloud tiles. This method returns a representation of all the cloud tiles,
     * which includes also the id of each cloud tile.
     * @param cloudTiles the list of cloud tiles
     * @return the matrix with the cloud tiles
     */

    public static String[][] generateTemplate(ArrayList<ClientCloudTile> cloudTiles) {

        int height = DrawersConstant.CLOUD_TILE_HEIGHT + DrawersConstant.SPACE_FOR_CLOUD_ID;
        int length = cloudTiles.size() * DrawersConstant.CLOUD_TILE_LENGTH +
                (cloudTiles.size() - 1) * DrawersConstant.SPACE_BETWEEN_CLOUD_TILES;

        String[][] template = new String[height][length];

        // Generate the representation of each cloud tile
        int currentLength = 0;
        for (ClientCloudTile clientCloudTile : cloudTiles) {
            cloudTileTemplate(template, clientCloudTile.getId(), 0, currentLength);
            currentLength += DrawersConstant.CLOUD_TILE_LENGTH + DrawersConstant.SPACE_BETWEEN_CLOUD_TILES;
        }

        CliDrawersUtilityFunctions.removeNullAndAddSingleSpace(template);
        return template;
    }

    /**
     * Generates the representation of a cloud tile starting from ({@code startingHeight}, {@code startingLength}).
     * @param template the matrix containing the representation of the cloud tiles
     * @param cloudId the cloud id
     * @param startingHeight the starting height
     * @param startingLength the starting length
     */

    private static void cloudTileTemplate(String[][] template, int cloudId, int startingHeight, int startingLength) {

        // 1: Write id
        char[] id = String.format("id: %d", cloudId).toCharArray();
        int index = 0;
        int offsetId = DrawersConstant.DISTANCE_BETWEEN_POINTS_OBLIQUE_LINE * DrawersConstant.OBLIQUE_LINE_LENGTH_CLOUD +
                DrawersConstant.SPACE_BETWEEN_LAST_PLUS_OBLIQUE_AND_FIRST_PLUS_HORIZONTAL;
        for (char c : id) {
            template[startingHeight][startingLength + index + offsetId] = String.valueOf(c);
            index++;
        }

        startingHeight += DrawersConstant.SPACE_FOR_CLOUD_ID;

        // Generate sides with direction "/"
        int i = startingHeight + DrawersConstant.OBLIQUE_LINE_LENGTH_CLOUD;
        int j = startingLength + DrawersConstant.DISTANCE_BETWEEN_POINTS_OBLIQUE_LINE;
        int offsetHeight = DrawersConstant.VERTICAL_LINE_LENGTH_CLOUD + DrawersConstant.OBLIQUE_LINE_LENGTH_CLOUD;
        int offsetLength = 2 * (DrawersConstant.OBLIQUE_LINE_LENGTH_CLOUD - 1) + 2 * DrawersConstant.SPACE_BETWEEN_LAST_PLUS_OBLIQUE_AND_FIRST_PLUS_HORIZONTAL +
                (DrawersConstant.SPACE_BETWEEN_PLUS_HORIZONTAL_LINE + 1) * (DrawersConstant.HORIZONTAL_LINE_LENGTH_CLOUD - 1);
        for (int k = 0; k < DrawersConstant.OBLIQUE_LINE_LENGTH_CLOUD; k++) {
            template[i][j] = "+";
            template[i + offsetHeight][j + offsetLength] = "+";
            i--;
            j += DrawersConstant.DISTANCE_BETWEEN_POINTS_OBLIQUE_LINE;
        }

        // Generate sides with direction "\"
        i = startingHeight + 2 * DrawersConstant.OBLIQUE_LINE_LENGTH_CLOUD + DrawersConstant.VERTICAL_LINE_LENGTH_CLOUD;
        j = startingLength + 2 * DrawersConstant.OBLIQUE_LINE_LENGTH_CLOUD;
        // Same offset as before
        for (int k = 0; k < DrawersConstant.OBLIQUE_LINE_LENGTH_CLOUD; k++) {
            template[i][j] = "+";
            template[i - offsetHeight][j + offsetLength] = "+";
            i--;
            j -= DrawersConstant.DISTANCE_BETWEEN_POINTS_OBLIQUE_LINE;
        }

        // Generate horizontal lines
        int initialValue = startingLength + DrawersConstant.DISTANCE_BETWEEN_POINTS_OBLIQUE_LINE * DrawersConstant.OBLIQUE_LINE_LENGTH_CLOUD +
                DrawersConstant.SPACE_BETWEEN_LAST_PLUS_OBLIQUE_AND_FIRST_PLUS_HORIZONTAL;
        for (int k = initialValue; k < initialValue + (DrawersConstant.SPACE_BETWEEN_PLUS_HORIZONTAL_LINE + 1) * DrawersConstant.HORIZONTAL_LINE_LENGTH_CLOUD;
             k += DrawersConstant.SPACE_BETWEEN_PLUS_HORIZONTAL_LINE + 1) {
            template[startingHeight][k] = "+";
            template[startingHeight + DrawersConstant.CLOUD_TILE_HEIGHT - 1][k] = "+";
        }

        // Generate vertical lines
        initialValue = startingHeight + DrawersConstant.OBLIQUE_LINE_LENGTH_CLOUD + 1;
        for (int k = initialValue; k < initialValue + DrawersConstant.VERTICAL_LINE_LENGTH_CLOUD; k++) {
            template[k][startingLength] = "+";
            template[k][startingLength + DrawersConstant.CLOUD_TILE_LENGTH - 1] = "+";
        }
    }

    /**
     * Fills the template which represents the cloud tiles with the student discs.
     * @param template the matrix containing the cloud tiles
     * @param cloudTiles the list of cloud tiles
     * @throws IllegalStudentIdException if the id of the student disc is invalid
     */

    public static void fillTemplate(String[][] template, ArrayList<ClientCloudTile> cloudTiles) throws IllegalStudentIdException {

        // Only the central part of the cloud tile is taken into consideration
        int height = DrawersConstant.SPACE_FOR_CLOUD_ID + DrawersConstant.OBLIQUE_LINE_LENGTH_CLOUD + 1;
        int currentLength = DrawersConstant.DISTANCE_BETWEEN_POINTS_OBLIQUE_LINE * DrawersConstant.OBLIQUE_LINE_LENGTH_CLOUD +
                DrawersConstant.SPACE_BETWEEN_LAST_PLUS_OBLIQUE_AND_FIRST_PLUS_HORIZONTAL;
        ArrayList<Integer[]> arrayOfCoordinates;
        Random random = new Random();
        int index;
        Integer[] coordinates;

        for (ClientCloudTile clientCloudTile : cloudTiles) {
            // Empty cloud tile
            CliDrawersUtilityFunctions.emptySectionWithPawnsOrTowers(template,  currentLength, height, DrawersConstant.VERTICAL_LINE_LENGTH_CLOUD,
                    (DrawersConstant.SPACE_BETWEEN_PLUS_HORIZONTAL_LINE + 1) * DrawersConstant.HORIZONTAL_LINE_LENGTH_CLOUD, 0);

            arrayOfCoordinates = generateSetOfCoordinates(height, currentLength);
            for (int student : clientCloudTile.getStudents()) {
                index = random.nextInt(arrayOfCoordinates.size());
                coordinates = arrayOfCoordinates.remove(index);
                template[coordinates[0]][coordinates[1]] = CliDrawersUtilityFunctions.getRepresentationOfStudentDisc(student);
            }
            currentLength += (DrawersConstant.SPACE_BETWEEN_PLUS_HORIZONTAL_LINE + 1)   * (DrawersConstant.HORIZONTAL_LINE_LENGTH_CLOUD - 1) + 2 * DrawersConstant.SPACE_BETWEEN_LAST_PLUS_OBLIQUE_AND_FIRST_PLUS_HORIZONTAL +
                    2 * DrawersConstant.DISTANCE_BETWEEN_POINTS_OBLIQUE_LINE * DrawersConstant.OBLIQUE_LINE_LENGTH_CLOUD + DrawersConstant.SPACE_BETWEEN_CLOUD_TILES + 1;
        }
    }

    /**
     * Generates the coordinates for student disc placement on the cloud tile. The first coordinate is ({@code firstAvailableRow}, {@code firstAvailableColumn}).
     * @param firstAvailableRow the starting row
     * @param firstAvailableColumn the starting column
     * @return an array of coordinates
     */

    private static ArrayList<Integer[]> generateSetOfCoordinates(int firstAvailableRow, int firstAvailableColumn) {

        ArrayList<Integer[]> arrayOfCoordinates = new ArrayList<>();

        for (int i = firstAvailableRow; i < firstAvailableRow + DrawersConstant.VERTICAL_LINE_LENGTH_CLOUD; i++)
            for (int j = firstAvailableColumn; j < firstAvailableColumn +
                    (DrawersConstant.SPACE_BETWEEN_PLUS_HORIZONTAL_LINE + 1) * DrawersConstant.HORIZONTAL_LINE_LENGTH_CLOUD - 1; j++)
                arrayOfCoordinates.add(new Integer[]{i, j});

        return arrayOfCoordinates;
    }
}