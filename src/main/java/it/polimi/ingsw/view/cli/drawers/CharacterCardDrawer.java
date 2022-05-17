package it.polimi.ingsw.view.cli.drawers;

import it.polimi.ingsw.view.cli.exceptions.IllegalCharacterIdException;
import it.polimi.ingsw.view.cli.exceptions.IllegalStudentIdException;
import it.polimi.ingsw.view.game_objects.ClientCharacterCard;

import java.util.ArrayList;
import java.util.Random;

public class CharacterCardDrawer {

    public static String[][] generateTemplate(ArrayList<ClientCharacterCard> characterCards) throws IllegalCharacterIdException {

        // Define height and length of character cards template
        int height = DrawersConstant.CHARACTER_CARD_HEIGHT;
        int length = characterCards.size() * (DrawersConstant.CHARACTER_CARD_LENGTH + DrawersConstant.SPACE_BETWEEN_CHARACTER_CARDS) +
                DrawersConstant.NUMBER_OF_CHARACTERS_OF_LONGEST_NAME + DrawersConstant.ADDITIONAL_SPACES_FOR_ID;

        String[][] template = new String[height][length];

        int currentLength = 1;

        // Generate the template for each character card
        for (ClientCharacterCard clientCharacterCard : characterCards) {
            drawCharacterCard(template, clientCharacterCard.getId(), 0, currentLength);
            currentLength += DrawersConstant.CHARACTER_CARD_LENGTH + DrawersConstant.SPACE_BETWEEN_CHARACTER_CARDS;
        }

        // Write legend for character cards with id and name
        int currentHeight = DrawersConstant.OFFSET_OF_LEGEND;
        for (ClientCharacterCard clientCharacterCard : characterCards) {
            char[] chars = UtilityFunctions.idToNameConverter(clientCharacterCard.getId()).toCharArray();

            template[currentHeight][currentLength] = String.valueOf(clientCharacterCard.getId());
            template[currentHeight][currentLength + 1] = ":";
            template[currentHeight][currentLength + 2] = " ";

            int lengthOffset = 3;
            for (char c : chars) {
                template[currentHeight][currentLength + lengthOffset] = String.valueOf(c);
                lengthOffset++;
            }
            currentHeight++;
        }

        SchoolBoardDrawer.removeNull(template);
        return template;
    }

    private static void drawCharacterCard(String[][] template, int id, int startingHeight, int startingLength) {

        // Draw "_" on both horizontal sides
        for (int k = startingLength; k < startingLength + DrawersConstant.CHARACTER_CARD_LENGTH; k++) {
            template[startingHeight][k] = "_";
            template[startingHeight + DrawersConstant.CHARACTER_CARD_HEIGHT - 1][k] = "_";
        }

        // Write "|" on both vertical sides
        for (int k = startingHeight + 1; k < startingHeight + DrawersConstant.CHARACTER_CARD_HEIGHT; k++) {
            template[k][startingLength - 1] = "|";
            template[k][startingLength + DrawersConstant.CHARACTER_CARD_LENGTH] = "|";
        }

        // Write id of character card
        char[] idChars = "id: ".toCharArray();
        int index = 0;
        for (char c : idChars) {
            template[startingHeight + DrawersConstant.OFFSET_OF_ID_IN_CARD][startingLength + index] = String.valueOf(c);
            index++;
        }
        template[startingHeight + 1][startingLength + index] = String.valueOf(id);

    }

    public static void fillTemplate(String[][] template, ArrayList<ClientCharacterCard> characterCards) throws IllegalStudentIdException {

        int startingHeight = 0;
        int currentLength = 1;

        // Add cost and storage to character card representation
        char[] costChars = "cost: ".toCharArray();
        for (ClientCharacterCard clientCharacterCard : characterCards) {
            // Add cost
            int index = 0;
            for (char c : costChars) {
                template[startingHeight + DrawersConstant.OFFSET_OF_COST_OF_CARD][currentLength + index] = String.valueOf(c);
                index++;
            }
            template[startingHeight + DrawersConstant.OFFSET_OF_COST_OF_CARD][currentLength + index] = String.valueOf(clientCharacterCard.getCost());

            // Add students. Pick the position randomly
            ArrayList<Integer[]> arrayOfCoordinates = generateSetOfCoordinates(startingHeight + DrawersConstant.OFFSET_OF_COST_OF_CARD + 1, currentLength);
            Random random = new Random();
            int indexOfCoordinate;
            Integer[] coordinates;
            for (int student : clientCharacterCard.getStorage()) {
                indexOfCoordinate = random.nextInt(arrayOfCoordinates.size());
                coordinates = arrayOfCoordinates.remove(indexOfCoordinate);
                template[coordinates[0]][coordinates[1]] = IdToColorConverter.getRepresentationOfStudentDisc(student);
            }

            currentLength += DrawersConstant.CHARACTER_CARD_LENGTH + DrawersConstant.SPACE_BETWEEN_CHARACTER_CARDS;
        }
    }

    private static ArrayList<Integer[]> generateSetOfCoordinates(int firstAvailableRow, int firstAvailableColumn) {

        ArrayList<Integer[]> arrayOfCoordinates = new ArrayList<>();

        for (int i = firstAvailableRow; i < DrawersConstant.CHARACTER_CARD_HEIGHT - 1; i++)
            for (int j = firstAvailableColumn; j < firstAvailableColumn + DrawersConstant.CHARACTER_CARD_LENGTH; j++)
                arrayOfCoordinates.add(new Integer[]{i, j});

        return arrayOfCoordinates;
    }
}

