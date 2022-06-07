package it.polimi.ingsw.view.gui.scene_controllers;

import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.view.ViewUtilityFunctions;
import it.polimi.ingsw.view.exceptions.IllegalLaneException;
import it.polimi.ingsw.view.exceptions.IllegalStudentIdException;
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
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class TableSceneController extends SceneController {

    // Array of panes containing island tiles
    private final Pane[] islandTiles;

    // Array of coordinates
    private final ArrayList<Integer[]> coordinatesOfIslandTile;
    private final Integer[][] coordinatesOfStudentsInEntrance;
    private final Integer[][] firstAvailableCoordinatesOfDiningRoom;
    private final Integer[][] coordinatesOfProfessorPawns;
    private final Integer[][] coordinatesOfTowers;

    // Previous state of school board
    private Integer[] previousEntrance;
    private final ArrayList<ArrayList<Integer>> previousDiningRoom;
    private final ArrayList<ClientPawnColor> previousProfessorSection;
    private int previousNumberOfTowers;

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

    public TableSceneController() {

        // Create array of Panes containing the island tiles
        islandTiles = new Pane[(ModelConstants.NUMBER_OF_ISLAND_TILES - 1) * ModelConstants.OFFSET_BETWEEN_ISLAND_IDS + ModelConstants.MIN_ID_OF_ISLAND + 1];

        // Create array of coordinates for elements in island tile
        this.coordinatesOfIslandTile = new ArrayList<>();

        // Create array of coordinates for student discs in entrance
        this.coordinatesOfStudentsInEntrance = new Integer[GUIConstants.COLUMNS_ENTRANCE * GUIConstants.CELLS_FIRST_ROW_ENTRANCE - 1][3];

        // Create array of coordinates of first cell available in dining room for each color
        this.firstAvailableCoordinatesOfDiningRoom = new Integer[GUIConstants.LANES][2];

        // Create array of coordinates for professor pawns
        this.coordinatesOfProfessorPawns = new Integer[GUIConstants.LANES][2];

        // Create array of coordinates for towers in tower section
        this.coordinatesOfTowers = new Integer[ModelConstants.MAX_NUMBER_OF_TOWERS][2];

        // Create array for previous entrance
        this.previousEntrance = new Integer[0];

        // Create array list of lanes of previous dining room
        this.previousDiningRoom = new ArrayList<>();
        for (int i = 0; i < GUIConstants.LANES; i++)
            this.previousDiningRoom.add(new ArrayList<>());

        // Create array list for previous professor section
        this.previousProfessorSection = new ArrayList<>();

        // Initialize previous number of towers to 0
        this.previousNumberOfTowers = 0;
    }

    // MAIN METHODS

    @Override
    protected Scene layout() {
        // Call for scene creation, that call updateScene to update the content of the table
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scene/table_scene.fxml"));
            loader.setControllerFactory(type -> {
                if (type == TableSceneController.class) {
                    return this;
                } else {
                    try {
                        return type.newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            AnchorPane root = loader.load();
            // Load panes of island tiles in array of panes
            this.loadPanesInArrayOfIslandTilePanes();
            // Generate coordinates
            SchoolBoardsFunction.generateCoordinates(this.coordinatesOfStudentsInEntrance, this.firstAvailableCoordinatesOfDiningRoom,
                    this.coordinatesOfProfessorPawns, this.coordinatesOfTowers);
            // Fill scene with missing game objects // TODO MOVE TO UPDATE SCENE AND CHANGE FILLING SYSTEM
            this.addStudentsMotherNatureAndNoEntryToIslandTiles();
            // TODO Remove 1 as player id
            this.updateScene(1);
            return new Scene(root);
        } catch (IOException | IllegalStudentIdException e) {
            return null;
        }
    }

    // TODO remove player id and get it from gui
    public void updateScene(int playerId) {

        // Update school board
        try {
            // Get index of school board of client
            int indexOfSchoolBoard = ViewUtilityFunctions.getPlayerSchoolBoardIndex(playerId, StageController.getStageController().getClientTeams());

            // Determine index of team from player id
            int indexOfTeam = StageController.getStageController().getClientTeams().getTeams()
                    .indexOf(StageController.getStageController().getClientTeams().getTeams()
                            .stream()
                            .filter(clientTeam -> clientTeam.getPlayers().
                                    stream().
                                    filter(clientPlayer -> clientPlayer.getPlayerId() == playerId).count() == 1)
                            .toList().get(0));

            // Update entrance
            this.previousEntrance = SchoolBoardsFunction.updateSchoolBoardEntrance(indexOfSchoolBoard, this.schoolBoard,
                    this.coordinatesOfStudentsInEntrance, this.previousEntrance);
            // Update dining room
            SchoolBoardsFunction.updateSchoolBoardDiningRoom(indexOfSchoolBoard, this.schoolBoard,
                    this.firstAvailableCoordinatesOfDiningRoom, this.previousDiningRoom);
            // Update professor section
            SchoolBoardsFunction.updateSchoolBoardProfessorSection(indexOfTeam, this.schoolBoard, this.coordinatesOfProfessorPawns,
                    this.previousProfessorSection);
            // Update tower section
            this.previousNumberOfTowers = SchoolBoardsFunction.updateSchoolBoardTowersSection(indexOfTeam, this.schoolBoard, this.coordinatesOfTowers,
                    this.previousNumberOfTowers);

        } catch (IllegalStudentIdException | IllegalLaneException e) {
            e.printStackTrace();
        }
    }

    // FILLERS

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
                    ImageView motherNature = new ImageView(Images.getImages().getMotherNaturePawn());
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
                    coordinate = this.coordinatesOfIslandTile.remove(indexOfFirstCoordinate);
                    motherNature.setX(coordinate[1]);
                    motherNature.setY(coordinate[0]);
                    // Add mother nature to pane
                    this.islandTiles[clientIslandTile.getId()].getChildren().add(motherNature);
                    // Remove other coordinate used for the mother nature pawn
                    int offsetRemoval = 1;
                    for (int index : listOfIndexesOfCoordinatesToRemove) {
                        this.coordinatesOfIslandTile.remove(index - offsetRemoval);
                        offsetRemoval++;
                    }
                }

                // ADD STUDENT DISCS

                Random random = new Random();
                for (int studentId : clientIslandTile.getStudents()) {
                    // Get coordinate for student disc placement
                    coordinate = this.coordinatesOfIslandTile.remove(random.nextInt(this.coordinatesOfIslandTile.size()));
                    if (children.stream().filter(node -> ViewUtilityFunctions.convertIdOfImageOfStudentDisc(node.getId()) == studentId).count() == 1) {
                        // Get corresponding node and add it to the list of children
                        Node student = children.stream().filter(node -> ViewUtilityFunctions.convertIdOfImageOfStudentDisc(node.getId()) == studentId).toList().get(0);
                        student.setLayoutX(coordinate[1]);
                        student.setLayoutY(coordinate[0]);
                        this.islandTiles[clientIslandTile.getId()].getChildren().add(student);
                    } else {
                        ImageView student = SchoolBoardsFunction.createImageViewOfStudent(studentId, coordinate[1], coordinate[0]);
                        this.islandTiles[clientIslandTile.getId()].getChildren().add(student);
                    }
                }

                // ADD NO ENTRY TILES

                if (clientIslandTile.hasNoEntry()) {
                    coordinate = this.coordinatesOfIslandTile.remove(random.nextInt(this.coordinatesOfIslandTile.size()));
                    ImageView noEntry = new ImageView(Images.getImages().getNoEntryTile());
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

    // COORDINATES GENERATOR

    private void regenerateCoordinatesForGameComponentsOfIslandTile() {

        this.coordinatesOfIslandTile.clear();
        for (int i = GUIConstants.LAYOUT_Y_OF_FIRST_AVAILABLE_CELL_FOR_GAME_OBJECTS_IN_ISLAND;
             i < GUIConstants.LAYOUT_Y_OF_FIRST_AVAILABLE_CELL_FOR_GAME_OBJECTS_IN_ISLAND
                     + GUIConstants.HEIGHT_OF_RECTANGLE_CONTAINING_GAME_OBJECTS_IN_ISLAND;
             i += GUIConstants.HEIGHT_OF_STUDENT_DISC)
            for (int j = GUIConstants.LAYOUT_X_OF_FIRST_AVAILABLE_CELL_FOR_GAME_OBJECTS_IN_ISLAND;
                 j < GUIConstants.LAYOUT_X_OF_FIRST_AVAILABLE_CELL_FOR_GAME_OBJECTS_IN_ISLAND
                         + GUIConstants.WIDTH_OF_RECTANGLE_CONTAINING_GAME_OBJECTS_IN_ISLAND;
                 j += GUIConstants.WIDTH_OF_STUDENT_DISC)
                this.coordinatesOfIslandTile.add(new Integer[]{i, j});
    }

    // PANES LOADER

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

    // EVENTS HANDLERS

    @FXML
    private void switchToOtherSchoolBoards(ActionEvent e) {
        try {
            StageController.getStageController().showScene(SceneType.SCHOOL_BOARD_SCENE, false);
        } catch (Exception exception) {

        }
    }

    @FXML
    public void switchToDeck(ActionEvent e) {
        try {
            StageController.getStageController().showScene(SceneType.DECK_SCENE, false);
        } catch (Exception exception) {

        }
    }
}