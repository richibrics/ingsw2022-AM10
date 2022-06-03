package it.polimi.ingsw.view.gui.scene_controllers;

import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.view.ViewConstants;
import it.polimi.ingsw.view.ViewUtilityFunctions;
import it.polimi.ingsw.view.exceptions.IllegalCharacterIdException;
import it.polimi.ingsw.view.exceptions.IllegalStudentIdException;
import it.polimi.ingsw.view.game_objects.ClientCharacterCard;
import it.polimi.ingsw.view.game_objects.ClientIslandTile;
import it.polimi.ingsw.view.game_objects.ClientPawnColor;
import it.polimi.ingsw.view.gui.GUIConstants;
import it.polimi.ingsw.view.gui.SceneType;
import it.polimi.ingsw.view.gui.StageController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.*;

public class TableSceneController extends SceneController  {

    ArrayList<Image> characterCards;
    @FXML
    private Pane island1;
    @FXML
    private Pane island2;
    @FXML
    private Pane island3;
    @FXML
    private Pane island4;
    @FXML
    private Pane island5;
    @FXML
    private Pane island6;
    @FXML
    private Pane island7;
    @FXML
    private Pane island8;
    @FXML
    private Pane island9;
    @FXML
    private Pane island10;
    @FXML
    private Pane island11;
    @FXML
    private Pane island12;
    @FXML
    private Pane schoolBoard;
    private Image cloudTile;
    private Image yellowStudentDisc;
    private Image blueStudentDisc;
    private Image greenStudentDisc;
    private Image redStudentDisc;
    private Image pinkStudentDisc;
    private Image yellowProfessor;
    private Image blueProfessor;
    private Image greenProfessor;
    private Image redProfessor;
    private Image pinkProfessor;
    private Image coin;
    private Image blackTower;
    private Image greyTower;
    private Image whiteTower;
    private Image motherNaturePawn;
    private Image noEntryTile;
    private final Image[] studentDiscs;
    private Image[] professorPawns;
    private final Pane[] islandTiles;

    private final ArrayList<Integer[]> coordinates;

    public TableSceneController() {

        // Create array of Panes containing the island tiles
        islandTiles = new Pane[(ModelConstants.NUMBER_OF_ISLAND_TILES - 1) * ModelConstants.OFFSET_BETWEEN_ISLAND_IDS + ModelConstants.MIN_ID_OF_ISLAND + 1];

        // Create array of images of student discs
        this.studentDiscs = new Image[ClientPawnColor.values().length];

        this.coordinates = new ArrayList<>();
    }

    @Override
    protected Scene layout() {

        try {
            // For now TODO remove
            this.loadImages(true, 2);
            //
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scene/table_scene.fxml"));
            loader.setControllerFactory(type -> {
                if (type == TableSceneController.class) {
                    return this ;
                } else {
                    try {
                        return type.newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            AnchorPane root = loader.load();
            this.loadPanesInArrayOfIslandTilePanes();
            this.addStudentsMotherNatureAndNoEntryToIslandTiles();
            return new Scene(root);
        } catch (IOException | IllegalStudentIdException e) {
            return null;
        }
    }

    private void loadPanesInArrayOfIslandTilePanes() {
        this.islandTiles[ModelConstants.MIN_ID_OF_ISLAND] = this.island1;
        this.islandTiles[ModelConstants.MIN_ID_OF_ISLAND + ModelConstants.OFFSET_BETWEEN_ISLAND_IDS] = this.island2;
        this.islandTiles[ModelConstants.MIN_ID_OF_ISLAND + 2 * ModelConstants.OFFSET_BETWEEN_ISLAND_IDS] = this.island3;
        this.islandTiles[ModelConstants.MIN_ID_OF_ISLAND + 3 * ModelConstants.OFFSET_BETWEEN_ISLAND_IDS] = this.island4;
        this.islandTiles[ModelConstants.MIN_ID_OF_ISLAND + 4 * ModelConstants.OFFSET_BETWEEN_ISLAND_IDS] = this.island5;
        this.islandTiles[ModelConstants.MIN_ID_OF_ISLAND + 5 * ModelConstants.OFFSET_BETWEEN_ISLAND_IDS] = this.island6;
        this.islandTiles[ModelConstants.MIN_ID_OF_ISLAND + 6 * ModelConstants.OFFSET_BETWEEN_ISLAND_IDS] = this.island7;
        this.islandTiles[ModelConstants.MIN_ID_OF_ISLAND + 7 * ModelConstants.OFFSET_BETWEEN_ISLAND_IDS] = this.island8;
        this.islandTiles[ModelConstants.MIN_ID_OF_ISLAND + 8 * ModelConstants.OFFSET_BETWEEN_ISLAND_IDS] = this.island9;
        this.islandTiles[ModelConstants.MIN_ID_OF_ISLAND + 9 * ModelConstants.OFFSET_BETWEEN_ISLAND_IDS] = this.island10;
        this.islandTiles[ModelConstants.MIN_ID_OF_ISLAND + 10 * ModelConstants.OFFSET_BETWEEN_ISLAND_IDS] = this.island11;
        this.islandTiles[ModelConstants.MIN_ID_OF_ISLAND + 11 * ModelConstants.OFFSET_BETWEEN_ISLAND_IDS] = this.island12;
    }

    private void loadImagesOfStudentsInArrayOfImages() {
        this.studentDiscs[ClientPawnColor.YELLOW.getId()] = this.yellowStudentDisc;
        this.studentDiscs[ClientPawnColor.BLUE.getId()] = this.blueStudentDisc;
        this.studentDiscs[ClientPawnColor.GREEN.getId()] = this.greenStudentDisc;
        this.studentDiscs[ClientPawnColor.RED.getId()] = this.redStudentDisc;
        this.studentDiscs[ClientPawnColor.PINK.getId()] = this.pinkStudentDisc;
    }

    private void addStudentsMotherNatureAndNoEntryToIslandTiles() throws IllegalStudentIdException {

        // Get mother nature island id
        int motherNatureIsland = StageController.getStageController().getClientTable().getMotherNature().getIsland();

        for (ArrayList<ClientIslandTile> islandGroups : StageController.getStageController().getClientTable().getIslandTiles())
            for (ClientIslandTile clientIslandTile : islandGroups) {

                // REMOVE CHILDREN FROM PANE CONTAINING ISLAND TILE

                // Remove all the children of the pane containing the island tile which are not the island tile and put them
                // in a list
                ArrayList<Node> children = new ArrayList<>();
                for (Node node : this.islandTiles[clientIslandTile.getId()].getChildren())
                    if (!node.getId().contains(GUIConstants.ISLAND_TILE_NAME)) {
                        int indexOfNode = this.islandTiles[clientIslandTile.getId()].getChildren().indexOf(node);
                        if (!node.getId().contains(GUIConstants.MOTHER_NATURE_NAME))
                            children.add(this.islandTiles[clientIslandTile.getId()].getChildren().get(indexOfNode));
                        else
                            this.islandTiles[clientIslandTile.getId()].getChildren().remove(node);
                    }

                for (Node node : children)
                    this.islandTiles[clientIslandTile.getId()].getChildren().remove(node);

                // Generate coordinates for game objects placement
                this.regenerateCoordinatesForGameComponentsOfIslandTile();
                Integer[] coordinate;

                // ADD MOTHER NATURE PAWN

                // Put mother nature at the center of the island tile if the current island contains mother nature
                if (motherNatureIsland == clientIslandTile.getId()) {
                    // Create mother nature image view and set properties
                    ImageView motherNature = new ImageView(this.motherNaturePawn);
                    motherNature.setId(GUIConstants.MOTHER_NATURE_NAME);
                    motherNature.setPreserveRatio(false);
                    motherNature.setFitHeight(GUIConstants.HEIGHT_OF_MOTHER_NATURE_PAWN);
                    motherNature.setFitWidth(GUIConstants.WIDTH_OF_MOTHER_NATURE);
                    // Get number of columns and rows of matrix containing the game objects
                    int numberOfColumns = GUIConstants.HEIGHT_OF_RECTANGLE_CONTAINING_GAME_OBJECTS_IN_ISLAND / GUIConstants.HEIGHT_OF_STUDENT_DISC;
                    int numberOfRows = GUIConstants.WIDTH_OF_RECTANGLE_CONTAINING_GAME_OBJECTS_IN_ISLAND / GUIConstants.WIDTH_OF_STUDENT_DISC;
                    // Calculate index of first coordinate and of coordinates that must be removed from the list of coordinates
                    int indexOfFirstCoordinate = Math.floorDiv(numberOfColumns, 2) * numberOfRows + Math.floorDiv(numberOfRows, 2);
                    ArrayList<Integer> listOfIndexesOfCoordinatesToRemove = new ArrayList<>();
                    for (int i = 0; i < GUIConstants.HEIGHT_OF_MOTHER_NATURE_PAWN / GUIConstants.HEIGHT_OF_STUDENT_DISC; i++)
                        for (int j = 0; j < GUIConstants.WIDTH_OF_MOTHER_NATURE / GUIConstants.WIDTH_OF_STUDENT_DISC; j++)
                            listOfIndexesOfCoordinatesToRemove.add(indexOfFirstCoordinate + i * numberOfColumns + j + (i > 0 ? -1 : 0));
                    // Get first coordinate and set layout of mother nature
                    listOfIndexesOfCoordinatesToRemove.remove(0);
                    coordinate = this.coordinates.remove(indexOfFirstCoordinate);
                    motherNature.setX(coordinate[1]);
                    motherNature.setY(coordinate[0]);
                    // Add mother nature to pane
                    this.islandTiles[clientIslandTile.getId()].getChildren().add(motherNature);
                    // Remove other coordinate used for the mother nature pawn
                    int offsetRemoval = 1;
                    for (int index : listOfIndexesOfCoordinatesToRemove) {
                        this.coordinates.remove(index - offsetRemoval);
                        offsetRemoval++;
                    }
                }

                // ADD STUDENT DISCS

                Random random = new Random();
                for (int studentId : clientIslandTile.getStudents()) {
                    // Get coordinate for student disc placement
                    coordinate = this.coordinates.remove(random.nextInt(this.coordinates.size()));
                    if (children.stream().filter(node -> ViewUtilityFunctions.convertIdOfImageOfStudentDisc(node.getId()) == studentId).count() == 1) {
                        // Get corresponding node and add it to the list of children
                        Node student = children.stream().filter(node -> ViewUtilityFunctions.convertIdOfImageOfStudentDisc(node.getId()) == studentId).toList().get(0);
                        student.setLayoutX(coordinate[1]);
                        student.setLayoutY(coordinate[0]);
                        this.islandTiles[clientIslandTile.getId()].getChildren().add(student);
                    }
                    else {
                        ImageView student = new ImageView(this.studentDiscs[ViewUtilityFunctions.convertStudentIdToIdOfColor(studentId)]);
                        student.setId(ViewUtilityFunctions.convertStudentIdToStudentIdForImageView(studentId));
                        student.setPreserveRatio(false);
                        student.setFitWidth(GUIConstants.WIDTH_OF_STUDENT_DISC);
                        student.setFitHeight(GUIConstants.HEIGHT_OF_STUDENT_DISC);
                        student.setLayoutX(coordinate[1]);
                        student.setLayoutY(coordinate[0]);
                        this.islandTiles[clientIslandTile.getId()].getChildren().add(student);
                    }
                }

                // ADD NO ENTRY TILES

                if (clientIslandTile.hasNoEntry()) {
                    coordinate = this.coordinates.remove(random.nextInt(this.coordinates.size()));
                    ImageView noEntry = new ImageView(this.noEntryTile);
                    noEntry.setId(GUIConstants.NO_ENTRY_TILE_NAME);
                    noEntry.setPreserveRatio(false);
                    noEntry.setFitWidth(GUIConstants.WIDTH_OF_STUDENT_DISC);
                    noEntry.setFitHeight(GUIConstants.HEIGHT_OF_STUDENT_DISC);
                    noEntry.setLayoutX(coordinate[1]);
                    noEntry.setLayoutY(coordinate[0]);
                    this.islandTiles[clientIslandTile.getId()].getChildren().add(noEntry);
                }
            }
    }

    private void regenerateCoordinatesForGameComponentsOfIslandTile() {

        this.coordinates.clear();
        for (int i = GUIConstants.LAYOUT_Y_OF_FIRST_AVAILABLE_CELL_FOR_GAME_OBJECTS_IN_ISLAND;
             i < GUIConstants.LAYOUT_Y_OF_FIRST_AVAILABLE_CELL_FOR_GAME_OBJECTS_IN_ISLAND
                     + GUIConstants.HEIGHT_OF_RECTANGLE_CONTAINING_GAME_OBJECTS_IN_ISLAND;
             i += GUIConstants.HEIGHT_OF_STUDENT_DISC)
            for (int j = GUIConstants.LAYOUT_X_OF_FIRST_AVAILABLE_CELL_FOR_GAME_OBJECTS_IN_ISLAND;
                 j < GUIConstants.LAYOUT_X_OF_FIRST_AVAILABLE_CELL_FOR_GAME_OBJECTS_IN_ISLAND
                         + GUIConstants.WIDTH_OF_RECTANGLE_CONTAINING_GAME_OBJECTS_IN_ISLAND;
                 j += GUIConstants.WIDTH_OF_STUDENT_DISC)
                this.coordinates.add(new Integer[]{i, j});
    }

    public void loadImages(boolean expertMode, int numberOfPlayers) {

        // Create images of students
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
        this.blackTower = new Image(GUIConstants.SCENE_TABLE_BLACK_TOWER_IMAGE_PATH);
        this.whiteTower = new Image(GUIConstants.SCENE_TABLE_WHITE_TOWER_IMAGE_PATH);
        this.motherNaturePawn = new Image(GUIConstants.SCENE_TABLE_MOTHER_NATURE_IMAGE_PATH);

        if (numberOfPlayers == ViewConstants.THREE_PLAYERS_GAME)
            this.greyTower = new Image(GUIConstants.SCENE_TABLE_GREY_TOWER_IMAGE_PATH);

        if (numberOfPlayers == ViewConstants.THREE_PLAYERS_GAME || numberOfPlayers == ViewConstants.FOUR_PLAYERS_GAME)
            this.cloudTile = new Image(GUIConstants.SCENE_TABLE_CLOUD_IMAGE_PATH);

        if (expertMode) {
            this.coin = new Image(GUIConstants.SCENE_TABLE_COIN_IMAGE_PATH);
            this.characterCards = new ArrayList<>();
            try {
                for (ClientCharacterCard clientCharacterCard : StageController.getStageController().getClientTable().getActiveCharacterCards())
                    this.characterCards.add(new Image(ViewUtilityFunctions.convertCharacterIdToImagePath(clientCharacterCard.getId())));
            } catch (IllegalCharacterIdException e) {

            }
            this.noEntryTile = new Image(GUIConstants.SCENE_TABLE_NO_ENTRY_PATH);
        }
    }

    private void switchToOtherSchoolBoards(ActionEvent e) {

    }

    @FXML
    public void switchToDeck(ActionEvent e) {
        try {
            StageController.getStageController().showScene(SceneType.DECK_SCENE, false);
        } catch (Exception exception) {

        }
    }
}