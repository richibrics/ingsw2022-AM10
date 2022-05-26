package it.polimi.ingsw.view.cli.drawers;

import it.polimi.ingsw.view.cli.exceptions.IllegalStudentIdException;
import it.polimi.ingsw.view.game_objects.ClientIslandTile;
import it.polimi.ingsw.view.game_objects.ClientMotherNature;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class IslandGroupsDrawer {

    /**
     * Generates the template that contains the island groups placed in a circle. Fills the template with the island groups
     * and their content.
     *
     * @param islandGroups       the list of island groups
     * @param clientMotherNature the mother nature pawn
     * @return the template containing a complete representation of the island groups
     * @throws IllegalStudentIdException if the id of one of the student discs is invalid
     */

    public static String[][] generateAndFillTemplate(ArrayList<ArrayList<ClientIslandTile>> islandGroups, ClientMotherNature clientMotherNature) throws IllegalStudentIdException {

        // Define dimension of initial template. The template will be redefined by a method that eliminates unused cells
        int height = 2 * (DrawersConstant.RADIUS + DrawersConstant.ISLAND_GROUP_HEIGHT + 1);
        int length = 2 * (DrawersConstant.ISLAND_TILE_LENGTH + (DrawersConstant.MAX_DIM_OF_ISLAND_GROUP - 1) * (DrawersConstant.ISLAND_TILE_LENGTH - DrawersConstant.ISLAND_GROUP_LENGTH_OFFSET)) + 1;
        String[][] template = new String[height][length];
        // Remove nulls from the template
        UtilityFunctions.removeNullAndAddTwoSpaces(template);
        // Fill the template
        fillTemplate(template, islandGroups, clientMotherNature);
        return resizeTemplateAndCopyIslands(template);
    }

    /**
     * Fills the matrix by adding the templates of all the island groups. Each island group is already filled with the
     * required game components.
     *
     * @param template           the matrix which contains all the island groups
     * @param islandGroups       the list of island groups
     * @param clientMotherNature the mother nature pawn
     * @throws IllegalStudentIdException if the id of one of the student discs is invalid
     */

    private static void fillTemplate(String[][] template, ArrayList<ArrayList<ClientIslandTile>> islandGroups, ClientMotherNature clientMotherNature) throws IllegalStudentIdException {

        // Generate set of coordinates
        ArrayList<Long[]> coordinatesOfIslandGroups = findCoordinatesOfIslandGroups(DrawersConstant.RADIUS, islandGroups.size());

        // Shift coordinates in order to have enough space to draw the island groups without having to check the borders of the template.
        // The right shift equals half the length of the longest possible island group, while the down shift equals the height of the island group + 1
        shiftCoordinates(coordinatesOfIslandGroups, Math.round((DrawersConstant.ISLAND_TILE_LENGTH + (DrawersConstant.MAX_DIM_OF_ISLAND_GROUP - 1) *
                (DrawersConstant.ISLAND_TILE_LENGTH - DrawersConstant.ISLAND_GROUP_LENGTH_OFFSET)) / 2) + 1, DrawersConstant.ISLAND_GROUP_HEIGHT + 1);

        // Adjust the coordinates to reduce the height difference between coordinates
        adjustSpaceBetweenCoordinatesInHeight(coordinatesOfIslandGroups);

        // Put the biggest island group in first position. Maintain the order of the island groups
        int indexOfBiggestGroup = islandGroups.indexOf(Collections.max(islandGroups, Comparator.comparing(c -> c.size())));
        ArrayList<ArrayList<ClientIslandTile>> newIslandGroups = new ArrayList<>();
        newIslandGroups.add(islandGroups.get(indexOfBiggestGroup));
        int index = indexOfBiggestGroup + 1;
        while (index != indexOfBiggestGroup) {
            if (index != islandGroups.size()) {
                newIslandGroups.add(islandGroups.get(index));
                index++;
            } else
                index = 0;
        }

        // Copy each island group in the template
        for (ArrayList<ClientIslandTile> islandGroup : newIslandGroups) {
            // Generate the template of the island group and fill it
            String[][] islandGroupTemplate = IslandGroupDrawer.generateTemplateAndFillIslandGroup(islandGroup, clientMotherNature);

            // Take the first coordinate
            Long[] coordinates = coordinatesOfIslandGroups.remove(0);

            // Useful values
            int halfHeightOfIslandGroup = Math.round(islandGroupTemplate.length / 2) + 1;
            int halfLengthOfIslandGroup = Math.round(islandGroupTemplate[0].length / 2) + 1;
            int startingCoordinateHeight = (int) (coordinates[0] - halfHeightOfIslandGroup + 1);

            // Copy template of the island group in matrix with all the island groups
            for (int i = 0; i < islandGroupTemplate.length; i++)
                for (int j = 0; j < islandGroupTemplate[0].length; j++) {
                    if (j == 0)
                        // Remove space on the right caused by the fact that each empty cell has two spaces
                        removeSpacesOnRight(template, islandGroupTemplate[0].length, startingCoordinateHeight + i, Math.toIntExact(coordinates[1]) - halfLengthOfIslandGroup);
                    template[startingCoordinateHeight + i][Math.toIntExact(coordinates[1]) - halfLengthOfIslandGroup + 1 + j] = islandGroupTemplate[i][j];
                }
        }
    }

    /**
     * Increases the height by {@code downShift} and the length by {@code rightShift}.
     *
     * @param coordinates the list of coordinates
     * @param rightShift  the right shift
     * @param downShift   the down shift
     */

    private static void shiftCoordinates(ArrayList<Long[]> coordinates, int rightShift, int downShift) {
        for (Long[] coordinate : coordinates) {
            coordinate[0] += downShift;
            coordinate[1] += rightShift;
        }
    }

    /**
     * Sorts the coordinates by height and reduces the distance between consecutive coordinates if it is bigger
     * than a threshold. The order of the coordinates in {@code coordinates} does not change.
     *
     * @param coordinates
     */

    private static void adjustSpaceBetweenCoordinatesInHeight(ArrayList<Long[]> coordinates) {
        ArrayList<Long[]> coordinatesSortedByHeight = new ArrayList<>(coordinates);
        Collections.sort(coordinatesSortedByHeight, Comparator.comparing(coordinate -> coordinate[0]));

        for (int i = coordinatesSortedByHeight.size() - 1; i > 0; i--)
            if (coordinatesSortedByHeight.get(i)[0] - coordinatesSortedByHeight.get(i - 1)[0] > DrawersConstant.ISLAND_GROUP_HEIGHT) {
                long offset = coordinatesSortedByHeight.get(i)[0] - coordinatesSortedByHeight.get(i - 1)[0] - DrawersConstant.ISLAND_GROUP_HEIGHT;
                for (int j = i - 1; j >= 0; j--)
                    coordinatesSortedByHeight.get(j)[0] += offset;
            }
    }

    /**
     * Removes the spaces on the right of the template caused by the presence of multiple spaces in the empty cells.
     *
     * @param template            the matrix which contains the island groups
     * @param lengthOfIslandGroup the length of the island group
     * @param row                 the row which contains the cell filled with spaces
     * @param column              the column which contains the cell filled with spaces. The first available cell on
     *                            the opposite side of the island group is also filled with spaces
     */

    private static void removeSpacesOnRight(String[][] template, int lengthOfIslandGroup, int row, int column) {

        for (int i = 0; i < Math.round(lengthOfIslandGroup / 2); i++) {
            template[row][column] += " ";
            template[row][column + lengthOfIslandGroup + 1] += " ";
        }
    }

    /**
     * Finds the coordinates of the island groups. The coordinates are placed on a circumference of radius {@code radius}
     * and specify equidistant points on the circumference.
     * @param radius the radius of the circumference
     * @param numberOfIslandGroups the number of island groups
     * @return a list of coordinates in the form (height, length)
     */

    private static ArrayList<Long[]> findCoordinatesOfIslandGroups(int radius, int numberOfIslandGroups) {

        ArrayList<Long[]> coordinatesOfIslandGroups = new ArrayList<>();
        long x;
        long y;

        for (int i = 0; i < numberOfIslandGroups; i++) {
            x = Math.round(radius * Math.cos(2 * Math.PI * i / numberOfIslandGroups));
            y = Math.round(radius * Math.sin(2 * Math.PI * i / numberOfIslandGroups));

            // y is the height in respect to the left-up corner of the matrix, while x is the length in respect
            // to the left-up corner of the matrix

            coordinatesOfIslandGroups.add(new Long[]{radius - y, x + radius});
        }

        return coordinatesOfIslandGroups;
    }

    /**
     * Creates a smaller template and copies the content of the bigger template in the smaller one.
     * @param oldTemplate the bigger template
     * @return a smaller template
     */

    private static String[][] resizeTemplateAndCopyIslands(String[][] oldTemplate) {

        int newStartingHeight = -1;
        boolean flagStartingHeight = false;
        int newEndingHeight = -1;
        boolean flagEndingHeight = false;
        int newStartingLength = -1;
        boolean flagStartingLength = false;
        int newEndingLength = -1;
        boolean flagEndingLength = false;

        // Determine starting height and ending height
        for (int i = 0; i < oldTemplate.length; i++) {
            for (int j = 0; j < oldTemplate[0].length; j++) {
                if (oldTemplate[i][j].equals("_") && !flagStartingHeight) {
                    newStartingHeight = i;
                    flagStartingHeight = true;
                } else if (oldTemplate[oldTemplate.length - i - 1][j].equals("_") && !flagEndingHeight) {
                    newEndingHeight = oldTemplate.length - i - 1;
                    flagEndingHeight = true;
                }
            }
        }

        // Determine starting length and ending length
        for (int j = 0; j < oldTemplate[0].length; j++) {
            for (int i = 0; i < oldTemplate.length; i++) {
                if ((oldTemplate[i][j].equals("/") || oldTemplate[i][j].equals("\\")) && !flagStartingLength) {
                    newStartingLength = j - 1;
                    flagStartingLength = true;
                } else if ((oldTemplate[i][oldTemplate[0].length - j - 1].equals("/") || oldTemplate[i][oldTemplate[0].length - j - 1].equals("\\")) && !flagEndingLength) {
                    newEndingLength = oldTemplate[0].length - j - 1;
                    flagEndingLength = true;
                }
            }
        }

        String[][] newTemplate = new String[newEndingHeight - newStartingHeight + 1][newEndingLength - newStartingLength + 1];

        // Copy content of old template in new template
        for (int i = newStartingHeight; i < newEndingHeight + 1; i++)
            for (int j = newStartingLength; j < newEndingLength + 1; j++)
                newTemplate[i - newStartingHeight][j - newStartingLength] = oldTemplate[i][j];

        return newTemplate;
    }
}