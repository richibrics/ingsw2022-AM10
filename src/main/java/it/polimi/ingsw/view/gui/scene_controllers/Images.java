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

public class Images {

    private static Images images;

    // Images
    private final Image cloudTile;
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

    private Image[] studentDiscs;
    private Image[] professorPawns;
    private Image[] towers;
    private Image[] characterCards;

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

        this.yellowProfessor = new Image(GUIConstants.SCENE_TABLE_YELLOW_PROFESSOR_IMAGE_PATH);
        this.blueProfessor = new Image(GUIConstants.SCENE_TABLE_BLUE_PROFESSOR_IMAGE_PATH);
        this.greenProfessor = new Image(GUIConstants.SCENE_TABLE_GREEN_PROFESSOR_IMAGE_PATH);
        this.redProfessor = new Image(GUIConstants.SCENE_TABLE_RED_PROFESSOR_IMAGE_PATH);
        this.pinkProfessor = new Image(GUIConstants.SCENE_TABLE_PINK_PROFESSOR_IMAGE_PATH);

        // Load images in array
        this.loadImagesOfProfessorsInArrayOfImages();

        this.blackTower = new Image(GUIConstants.SCENE_TABLE_BLACK_TOWER_IMAGE_PATH);
        this.whiteTower = new Image(GUIConstants.SCENE_TABLE_WHITE_TOWER_IMAGE_PATH);
        this.greyTower = new Image(GUIConstants.SCENE_TABLE_GREY_TOWER_IMAGE_PATH);

        // Load images in array
        this.loadImagesOfTowersInArrayOfImages();

        this.motherNaturePawn = new Image(GUIConstants.SCENE_TABLE_MOTHER_NATURE_IMAGE_PATH);

        this.cloudTile = new Image(GUIConstants.SCENE_TABLE_CLOUD_IMAGE_PATH);


        this.coin = new Image(GUIConstants.SCENE_TABLE_COIN_IMAGE_PATH);

        this.characterCards = new Image[ModelConstants.NUMBER_OF_CHARACTER_CARDS];
        // Add character cards to array of character cards
        try {
            int index = 0;
            for (ClientCharacterCard clientCharacterCard : StageController.getStageController().getClientTable().getActiveCharacterCards()) {
                this.characterCards[index] = new Image(ViewUtilityFunctions.convertCharacterIdToImagePath(clientCharacterCard.getId()));
                index++;
            }
        } catch (IllegalCharacterIdException e) { }

        this.noEntryTile = new Image(GUIConstants.SCENE_TABLE_NO_ENTRY_PATH);
    }

    public static Images getImages() {
        if (images == null)
            return new Images();
        else
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

    public Image getCloudTile() {
        return cloudTile;
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

    public Image[] getCharacterCards() {
        return characterCards;
    }
}