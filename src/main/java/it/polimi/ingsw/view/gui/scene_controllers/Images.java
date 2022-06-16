package it.polimi.ingsw.view.gui.scene_controllers;

import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.view.ViewUtilityFunctions;
import it.polimi.ingsw.view.exceptions.IllegalCharacterIdException;
import it.polimi.ingsw.view.game_objects.ClientCharacterCard;
import it.polimi.ingsw.view.game_objects.ClientPawnColor;
import it.polimi.ingsw.view.game_objects.ClientTowerColor;
import it.polimi.ingsw.view.gui.GUIConstants;
import it.polimi.ingsw.view.gui.StageController;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Random;

public class Images {

    private static Images images;

    // Images
    private final Image yellowStudentDisc;
    private final Image blueStudentDisc;
    private final Image greenStudentDisc;
    private final Image redStudentDisc;
    private final Image pinkStudentDisc;
    private final Image yellowProfessor;
    private final Image blueProfessor;
    private final Image greenProfessor;
    private final Image redProfessor;
    private final Image pinkProfessor;
    private final Image coin;
    private final Image blackTower;
    private final Image greyTower;
    private final Image whiteTower;
    private final Image motherNaturePawn;
    private final Image noEntryTile;
    private final Image cloudTileDragon;
    private final Image cloudTileBear;
    private final Image cloudTileCastle;
    private final Image cloudTileSports;
    private final Image cloudTileThreePlaces;
    private final Image assistant1;
    private final Image assistant2;
    private final Image assistant3;
    private final Image assistant4;
    private final Image assistant5;
    private final Image assistant6;
    private final Image assistant7;
    private final Image assistant8;
    private final Image assistant9;
    private final Image assistant10;


    private final Image[] characterCards;
    private Image[] studentDiscs;
    private Image[] professorPawns;
    private Image[] towers;
    private Image[] cloudTiles;
    private Image[] assistantCards;


    private Images() {
        // Create images of students
        // this.yellowStudentDisc = new Image(GUIConstants.SCENE_TABLE_YELLOW_STUDENT_DISC_IMAGE_PATH);
        this.yellowStudentDisc = new Image(GUIConstants.SCENE_TABLE_YELLOW_STUDENT_DISC_IMAGE_PATH);
        this.blueStudentDisc = new Image(GUIConstants.SCENE_TABLE_BLUE_STUDENT_DISC_IMAGE_PATH);
        this.greenStudentDisc = new Image(GUIConstants.SCENE_TABLE_GREEN_STUDENT_DISC_IMAGE_PATH);
        this.redStudentDisc = new Image(GUIConstants.SCENE_TABLE_RED_STUDENT_DISC_IMAGE_PATH);
        this.pinkStudentDisc = new Image(GUIConstants.SCENE_TABLE_PINK_STUDENT_DISC_IMAGE_PATH);

        // Load images in array
        this.loadImagesOfStudentsInArrayOfImages();

        // Create images of professor pawns
        this.yellowProfessor = new Image(GUIConstants.SCENE_TABLE_YELLOW_PROFESSOR_IMAGE_PATH);
        this.blueProfessor = new Image(GUIConstants.SCENE_TABLE_BLUE_PROFESSOR_IMAGE_PATH);
        this.greenProfessor = new Image(GUIConstants.SCENE_TABLE_GREEN_PROFESSOR_IMAGE_PATH);
        this.redProfessor = new Image(GUIConstants.SCENE_TABLE_RED_PROFESSOR_IMAGE_PATH);
        this.pinkProfessor = new Image(GUIConstants.SCENE_TABLE_PINK_PROFESSOR_IMAGE_PATH);

        // Load images in array
        this.loadImagesOfProfessorsInArrayOfImages();

        // Create images of towers
        this.blackTower = new Image(GUIConstants.SCENE_TABLE_BLACK_TOWER_IMAGE_PATH);
        this.whiteTower = new Image(GUIConstants.SCENE_TABLE_WHITE_TOWER_IMAGE_PATH);
        this.greyTower = new Image(GUIConstants.SCENE_TABLE_GREY_TOWER_IMAGE_PATH);

        // Load images in array
        this.loadImagesOfTowersInArrayOfImages();

        // Create image of mother nature
        this.motherNaturePawn = new Image(GUIConstants.SCENE_TABLE_MOTHER_NATURE_IMAGE_PATH);

        // Create images of cloud for 2 and four players
        this.cloudTileDragon = new Image(GUIConstants.SCENE_TABLE_CLOUD_WITH_DRAGON_IMAGE_PATH);
        this.cloudTileBear = new Image(GUIConstants.SCENE_TABLE_CLOUD_WITH_BEAR_IMAGE_PATH);
        this.cloudTileCastle = new Image(GUIConstants.SCENE_TABLE_CLOUD_WITH_CASTLE_IMAGE_PATH);
        this.cloudTileSports = new Image(GUIConstants.SCENE_TABLE_CLOUD_WITH_SPORTS_IMAGE_PATH);
        this.cloudTileThreePlaces = new Image(GUIConstants.SCENE_TABLE_CLOUD_WITH_THREE_PLACES_IMAGE_PATH);

        // Get number of players
        int numberOfPlayers = (int) StageController.getStageController().getClientTeams().getTeams()
                .stream()
                .mapToLong(clientTeam -> clientTeam.getPlayers().size())
                .sum();

        // Load images of cloud tiles
        if (numberOfPlayers == ModelConstants.TWO_PLAYERS || numberOfPlayers == ModelConstants.FOUR_PLAYERS)
            this.loadImagesOfCloudTilesTwoFourPlayers();

        else if (numberOfPlayers == ModelConstants.THREE_PLAYERS)
            this.loadImagesOfCloudTilesThreePlayers();

        this.coin = new Image(GUIConstants.SCENE_TABLE_COIN_IMAGE_PATH);

        this.characterCards = new Image[ModelConstants.NUMBER_OF_CHARACTER_CARDS];
        // Add character cards to array of character cards
        try {
            int index = 0;
            for (ClientCharacterCard clientCharacterCard : StageController.getStageController().getClientTable().getActiveCharacterCards()) {
                this.characterCards[index] = new Image(ViewUtilityFunctions.convertCharacterIdToImagePath(clientCharacterCard.getId()));
                index++;
            }
        } catch (IllegalCharacterIdException e) {
        }

        this.noEntryTile = new Image(GUIConstants.SCENE_TABLE_NO_ENTRY_PATH);

        // Create images of assistant cards
        this.assistant1 = new Image(GUIConstants.SCENE_TABLE_ASSISTANT1_IMAGE_PATH);
        this.assistant2 = new Image(GUIConstants.SCENE_TABLE_ASSISTANT2_IMAGE_PATH);
        this.assistant3 = new Image(GUIConstants.SCENE_TABLE_ASSISTANT3_IMAGE_PATH);
        this.assistant4 = new Image(GUIConstants.SCENE_TABLE_ASSISTANT4_IMAGE_PATH);
        this.assistant5 = new Image(GUIConstants.SCENE_TABLE_ASSISTANT5_IMAGE_PATH);
        this.assistant6 = new Image(GUIConstants.SCENE_TABLE_ASSISTANT6_IMAGE_PATH);
        this.assistant7 = new Image(GUIConstants.SCENE_TABLE_ASSISTANT7_IMAGE_PATH);
        this.assistant8 = new Image(GUIConstants.SCENE_TABLE_ASSISTANT8_IMAGE_PATH);
        this.assistant9 = new Image(GUIConstants.SCENE_TABLE_ASSISTANT9_IMAGE_PATH);
        this.assistant10 = new Image(GUIConstants.SCENE_TABLE_ASSISTANT10_IMAGE_PATH);

        //Load images of assistants
        this.loadImagesOfAssistantCards();
    }

    public static Images getImages() {
        if (images == null) {
            images = new Images();
        }
        return images;
    }

    public void loadImagesOfStudentsInArrayOfImages() {
        this.studentDiscs = new Image[ClientPawnColor.values().length];
        this.studentDiscs[ClientPawnColor.YELLOW.getId()] = this.yellowStudentDisc;
        this.studentDiscs[ClientPawnColor.BLUE.getId()] = this.blueStudentDisc;
        this.studentDiscs[ClientPawnColor.GREEN.getId()] = this.greenStudentDisc;
        this.studentDiscs[ClientPawnColor.RED.getId()] = this.redStudentDisc;
        this.studentDiscs[ClientPawnColor.PINK.getId()] = this.pinkStudentDisc;
    }

    private void loadImagesOfProfessorsInArrayOfImages() {
        this.professorPawns = new Image[ClientPawnColor.values().length];
        this.professorPawns[ClientPawnColor.YELLOW.getId()] = this.yellowProfessor;
        this.professorPawns[ClientPawnColor.BLUE.getId()] = this.blueProfessor;
        this.professorPawns[ClientPawnColor.GREEN.getId()] = this.greenProfessor;
        this.professorPawns[ClientPawnColor.RED.getId()] = this.redProfessor;
        this.professorPawns[ClientPawnColor.PINK.getId()] = this.pinkProfessor;
    }

    private void loadImagesOfTowersInArrayOfImages() {
        this.towers = new Image[ClientTowerColor.values().length];
        this.towers[ClientTowerColor.WHITE.getId()] = this.whiteTower;
        this.towers[ClientTowerColor.BLACK.getId()] = this.blackTower;
        this.towers[ClientTowerColor.GREY.getId()] = this.greyTower;
    }

    private void loadImagesOfCloudTilesTwoFourPlayers() {
        this.cloudTiles = new Image[1];
        this.cloudTiles[0] = this.cloudTileThreePlaces;
    }

    private void loadImagesOfCloudTilesThreePlayers() {
        this.cloudTiles = new Image[ModelConstants.THREE_PLAYERS];
        ArrayList<Image> temp = new ArrayList<>();
        temp.add(this.cloudTileDragon);
        temp.add(this.cloudTileBear);
        temp.add(this.cloudTileCastle);
        temp.add(this.cloudTileSports);

        // Select three images randomly
        Random rand = new Random();
        for (int i = 0; i < this.cloudTiles.length; i++) {
            this.cloudTiles[i] = temp.remove(rand.nextInt(temp.size()));
        }
    }

    private void loadImagesOfAssistantCards() {
        this.assistantCards = new Image[ModelConstants.MAX_VALUE_OF_ASSISTANT_CARD + 1];
        this.assistantCards[ModelConstants.MIN_VALUE_OF_ASSISTANT_CARD] = this.assistant1;
        this.assistantCards[ModelConstants.MIN_VALUE_OF_ASSISTANT_CARD + ModelConstants.OFFSET_BETWEEN_ASSISTANT_CARDS] = this.assistant2;
        this.assistantCards[ModelConstants.MIN_VALUE_OF_ASSISTANT_CARD + 2 * ModelConstants.OFFSET_BETWEEN_ASSISTANT_CARDS] = this.assistant3;
        this.assistantCards[ModelConstants.MIN_VALUE_OF_ASSISTANT_CARD + 3 * ModelConstants.OFFSET_BETWEEN_ASSISTANT_CARDS] = this.assistant4;
        this.assistantCards[ModelConstants.MIN_VALUE_OF_ASSISTANT_CARD + 4 * ModelConstants.OFFSET_BETWEEN_ASSISTANT_CARDS] = this.assistant5;
        this.assistantCards[ModelConstants.MIN_VALUE_OF_ASSISTANT_CARD + 5 * ModelConstants.OFFSET_BETWEEN_ASSISTANT_CARDS] = this.assistant6;
        this.assistantCards[ModelConstants.MIN_VALUE_OF_ASSISTANT_CARD + 6 * ModelConstants.OFFSET_BETWEEN_ASSISTANT_CARDS] = this.assistant7;
        this.assistantCards[ModelConstants.MIN_VALUE_OF_ASSISTANT_CARD + 7 * ModelConstants.OFFSET_BETWEEN_ASSISTANT_CARDS] = this.assistant8;
        this.assistantCards[ModelConstants.MIN_VALUE_OF_ASSISTANT_CARD + 8 * ModelConstants.OFFSET_BETWEEN_ASSISTANT_CARDS] = this.assistant9;
        this.assistantCards[ModelConstants.MIN_VALUE_OF_ASSISTANT_CARD + 9 * ModelConstants.OFFSET_BETWEEN_ASSISTANT_CARDS] = this.assistant10;
    }

    public Image getCoin() {
        return coin;
    }

    public Image getMotherNaturePawn() {
        return motherNaturePawn;
    }

    public Image getNoEntryTile() {
        return noEntryTile;
    }

    public Image[] getStudentDiscs() {
        return studentDiscs;
    }

    public Image[] getProfessorPawns() {
        return professorPawns;
    }

    public Image[] getTowers() {
        return towers;
    }

    public Image[] getAssistantCards() {
        return assistantCards;
    }

    public Image[] getCharacterCards() {
        return characterCards;
    }

    public Image[] getCloudTiles() {
        return this.cloudTiles;
    }
}