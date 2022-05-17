package it.polimi.ingsw.view.cli.drawers;

import it.polimi.ingsw.model.exceptions.TowerNotSetException;
import it.polimi.ingsw.view.cli.exceptions.IllegalStudentIdException;
import it.polimi.ingsw.view.cli.exceptions.NoSpaceForIslandGroupException;
import it.polimi.ingsw.view.cli.exceptions.WrongNumberOfPlayersException;
import it.polimi.ingsw.view.game_objects.ClientIslandTile;
import it.polimi.ingsw.view.game_objects.ClientMotherNature;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class IslandTilesDrawer {

    private static void removeNull(String[][] template) {

        for (int i = 0; i < template.length; i++)
            for (int j = 0; j < template[i].length; j++)
                if (template[i][j] == null)
                    template[i][j] = "  ";
    }

    public static String[][] generateTemplate() throws WrongNumberOfPlayersException {

        int height = 52;
        int dim = 2 * DrawersConstant.ISLAND_TILE_LENGTH + (DrawersConstant.MAX_DIM_OF_ISLAND_GROUP - 1)*(DrawersConstant.ISLAND_TILE_LENGTH - DrawersConstant.ISLAND_GROUP_LENGTH_OFFSET) + 2;
        String[][] template = new String[height][dim];
        removeNull(template);
        return template;
    }


    public static void fillTemplate(String[][] template, ArrayList<ArrayList<ClientIslandTile>> islandGroups, ClientMotherNature clientMotherNature)
            throws NoSpaceForIslandGroupException, TowerNotSetException, IllegalStudentIdException {

        ArrayList<Long[]> coordinatesOfIslandGroups = findCoordinatesOfIslandGroups(Math.round(template.length / 2), islandGroups.size());

        // Put the biggest island group in first position
        int indexOfBiggestGroup = islandGroups.indexOf(Collections.max(islandGroups, Comparator.comparing(c -> c.size())));
        ArrayList<ArrayList<ClientIslandTile>> newIslandGroups = new ArrayList<>();
        newIslandGroups.add(islandGroups.get(indexOfBiggestGroup));
        int index = indexOfBiggestGroup + 1;
        while (index != indexOfBiggestGroup) {
            if (index != islandGroups.size()) {
                newIslandGroups.add(islandGroups.get(index));
                index++;
            }
            else
                index = 0;
        }

        for (ArrayList<ClientIslandTile> islandGroup : newIslandGroups) {
            String[][] islandGroupTemplate = IslandGroupDrawer.generateTemplateAndFillIslandGroup(islandGroup, clientMotherNature);
            Long[] coordinates = coordinatesOfIslandGroups.remove(0);

            int spaceAbove = Math.toIntExact(coordinates[0]);
            int spaceBelow = (int) (template.length - coordinates[0]);
            int halfHeightOfIslandGroup = Math.round(islandGroupTemplate.length / 2) + 1;

            // Case 1: enough space both above and below the coordinate
            if (spaceAbove >= halfHeightOfIslandGroup && spaceBelow >= halfHeightOfIslandGroup)
                checkSpaceInLengthAndCopyIslandGroup(template, islandGroupTemplate, Math.toIntExact(coordinates[1]), (int) (coordinates[0] - halfHeightOfIslandGroup + 1));

            // Case 2: enough space only above the coordinate
            else if (spaceAbove >= halfHeightOfIslandGroup && spaceBelow < halfHeightOfIslandGroup) {
                if (spaceAbove >= islandGroupTemplate.length)
                    checkSpaceInLengthAndCopyIslandGroup(template, islandGroupTemplate, Math.toIntExact(coordinates[1]), (int) (coordinates[0] - islandGroupTemplate.length));
                else
                    throw new NoSpaceForIslandGroupException();
            }

            // Case 3: enough space only below the coordinate
            else if (spaceAbove < halfHeightOfIslandGroup && spaceBelow >= halfHeightOfIslandGroup) {
                if (spaceBelow >= islandGroupTemplate.length)
                    checkSpaceInLengthAndCopyIslandGroup(template, islandGroupTemplate, Math.toIntExact(coordinates[1]), Math.toIntExact(coordinates[0]));
                else
                    throw new NoSpaceForIslandGroupException();
            }

            // Case 4: not enough space
            else
                throw new NoSpaceForIslandGroupException();
        }
    }

    private static void checkSpaceInLengthAndCopyIslandGroup(String[][] template, String[][] islandGroupTemplate,
                                                             int coordinateLength, int startingCoordinateHeight) throws NoSpaceForIslandGroupException {

        int heightOfIslandGroup = islandGroupTemplate.length;
        int lengthOfIslandGroup = islandGroupTemplate[0].length;
        int templateLength = template[0].length;
        int leftSpace = coordinateLength;
        int rightSpace = templateLength - coordinateLength;
        int halfLengthOfIslandGroup = Math.round(lengthOfIslandGroup / 2) + 1;

        // Case 1: enough space both on the right and on the left
        if (leftSpace >= halfLengthOfIslandGroup && rightSpace >= halfLengthOfIslandGroup)
            for (int i = 0; i < heightOfIslandGroup; i++)
                for (int j = 0; j < lengthOfIslandGroup; j++) {
                    if (j == 0)
                        removeSpacesOnRight(template, lengthOfIslandGroup, startingCoordinateHeight + i, coordinateLength - halfLengthOfIslandGroup, true);
                    template[startingCoordinateHeight + i][coordinateLength - halfLengthOfIslandGroup + 1 + j] = islandGroupTemplate[i][j];

                }

        // Case 2: enough space on the left but not on the right
        else if (leftSpace >= halfLengthOfIslandGroup && rightSpace < halfLengthOfIslandGroup) {
            if (leftSpace >= lengthOfIslandGroup)
                for (int i = 0; i < heightOfIslandGroup; i++)
                    for (int j = 0; j < lengthOfIslandGroup; j++) {
                        if (j == 0)
                            removeSpacesOnRight(template, lengthOfIslandGroup, startingCoordinateHeight + i, coordinateLength - lengthOfIslandGroup - 1, false);
                        template[startingCoordinateHeight + i][coordinateLength - lengthOfIslandGroup + j] = islandGroupTemplate[i][j];
                    }
            else
                throw new NoSpaceForIslandGroupException();

        }

        // Case 3: enough space on the right but not on the left
        else if (leftSpace < halfLengthOfIslandGroup && rightSpace >= halfLengthOfIslandGroup) {
            if (rightSpace >= lengthOfIslandGroup)
                for (int i = 0; i < heightOfIslandGroup; i++)
                    for (int j = 0; j < lengthOfIslandGroup; j++) {
                        if (j == 0)
                            removeSpacesOnRight(template, lengthOfIslandGroup, startingCoordinateHeight + i, coordinateLength + lengthOfIslandGroup, false);
                        template[startingCoordinateHeight + i][coordinateLength + j] = islandGroupTemplate[i][j];
                    }
            else
                throw new NoSpaceForIslandGroupException();
        }

        // Case 4: no space both on the left and on the right
        else
            throw new NoSpaceForIslandGroupException();

    }


    private static void removeSpacesOnRight(String[][] template, int lengthOfIslandGroup, int row, int column, boolean spaceOnBothSides) {

        if (spaceOnBothSides) {
            for (int i = 0; i < Math.round(lengthOfIslandGroup / 2); i++) {
                template[row][column] += " ";
                template[row][column + lengthOfIslandGroup + 1] += " ";
            }
        } else {
            for (int i = 0; i < lengthOfIslandGroup; i++)
                template[row][column] += " ";
        }
    }

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
}