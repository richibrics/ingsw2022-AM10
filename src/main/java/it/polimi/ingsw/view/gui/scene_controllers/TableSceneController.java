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
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
    private final ArrayList<ArrayList<Integer>> previousDiningRoom;
    private final ArrayList<ClientPawnColor> previousProfessorSection;
    private final int previousNumberOfTowers;
    private Integer[] previousEntrance;
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
            this.generateCoordinates();
            // Fill scene with missing game objects
            this.addStudentsMotherNatureAndNoEntryToIslandTiles();
            // TODO Remove 1 as player id
            this.updateScene(1);
            return new Scene(root);
        } catch (IOException | IllegalStudentIdException e) {
            return null;
        }
    }

    public void updateScene(int playerId) {
        // Update school board
        try {
            // Update entrance
            this.updateSchoolBoardEntrance(playerId);
            // Update dining room
            this.updateSchoolBoardDiningRoom(playerId);
            // Update professor section
            this.updateSchoolBoardProfessorSection(playerId);
            // Update tower section
            this.updateSchoolBoardTowersSection(playerId);
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
                        ImageView student = this.createImageViewOfStudent(studentId, coordinate[1], coordinate[0]);
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

    private void updateSchoolBoardEntrance(int playerId) throws IllegalStudentIdException {

        // Important: this method is based on the assumption that the only operations applied to the entrance are additions and removals of
        // student discs. No replacement should take place. This is true at the moment.

        int indexOfSchoolBoard = ViewUtilityFunctions.getPlayerSchoolBoardIndex(playerId, StageController.getStageController().getClientTeams());
        // Get current entrance of school board of client. The content of the current entrance is compared to the content of the previous entrance
        Integer[] currentEntrance = StageController.getStageController().getClientTable().getSchoolBoards().get(indexOfSchoolBoard).getEntrance().toArray(new Integer[0]);
        int indexOfCoordinate = 0;
        // Case 1: no students in the previous entrance. Add all the students.
        if (this.previousEntrance == null) {
            for (int studentId : currentEntrance) {
                // Create image view
                ImageView student = this.createImageViewOfStudent(studentId, this.coordinatesOfStudentsInEntrance[indexOfCoordinate][1],
                        this.coordinatesOfStudentsInEntrance[indexOfCoordinate][0]);
                // Add image view to pane with school board
                this.schoolBoard.getChildren().add(student);
                // The coordinate has been used, set to -1
                this.coordinatesOfStudentsInEntrance[indexOfCoordinate][2] = 1;
                indexOfCoordinate++;
            }
            // Update previous entrance
            this.previousEntrance = currentEntrance;
        }

        // Case 2: Add the new students to the entrance
        else if (this.previousEntrance.length < currentEntrance.length) {
            Integer[] newStudents = Arrays
                    .stream(currentEntrance)
                    .filter(id1 -> Arrays
                            .stream(this.previousEntrance).noneMatch(id2 -> id2 == id1))
                    .toArray(Integer[]::new);

            for (int i = 0; i < this.coordinatesOfStudentsInEntrance.length; i++) {
                // Check if coordinates is not taken
                if (this.coordinatesOfStudentsInEntrance[i][2] == 0) {
                    // Create image view
                    ImageView student = this.createImageViewOfStudent(newStudents[i], this.coordinatesOfStudentsInEntrance[indexOfCoordinate][1],
                            this.coordinatesOfStudentsInEntrance[indexOfCoordinate][0]);
                    // Add image view to pane with school board
                    this.schoolBoard.getChildren().add(student);
                    // The coordinate has been used, set third element to 1
                    this.coordinatesOfStudentsInEntrance[indexOfCoordinate][2] = 1;
                }
                indexOfCoordinate++;
            }
            // Update previous entrance
            this.previousEntrance = currentEntrance;
        }
        // Case 3: Remove students from entrance and update array of coordinates
        else if (this.previousEntrance.length > currentEntrance.length) {
            Integer[] studentsToRemove = Arrays
                    .stream(this.previousEntrance)
                    .filter(id1 -> Arrays
                            .stream(currentEntrance).noneMatch(id2 -> id2 == id1))
                    .toArray(Integer[]::new);

            for (int i = 0; i < this.coordinatesOfStudentsInEntrance.length; i++) {
                // Get node at this.coordinatesOfStudentsInEntrance[i] if present
                int finalI = i;
                if (this.schoolBoard.getChildren()
                        .stream()
                        .filter(n -> n.getLayoutX() == this.coordinatesOfStudentsInEntrance[finalI][1]
                                && n.getLayoutY() == this.coordinatesOfStudentsInEntrance[finalI][0])
                        .count() == 1) {
                    Node node = this.schoolBoard.getChildren()
                            .stream()
                            .filter(n -> n.getLayoutX() == this.coordinatesOfStudentsInEntrance[finalI][1]
                                    && n.getLayoutY() == this.coordinatesOfStudentsInEntrance[finalI][0]).toList().get(0);
                    // Check if the node must be removed
                    if (Arrays.stream(studentsToRemove)
                            .filter(id -> id == ViewUtilityFunctions.convertIdOfImageOfStudentDisc(node.getId()))
                            .count() == 1) {
                        // Remove node
                        this.schoolBoard.getChildren().remove(node);
                        // The coordinate is not used, set third element to 0
                        this.coordinatesOfStudentsInEntrance[1][2] = 0;
                    }
                }
            }
            // Update previous entrance
            this.previousEntrance = currentEntrance;
        }
    }

    private void updateSchoolBoardDiningRoom(int playerId) throws IllegalLaneException, IllegalStudentIdException {

        // Important: this method is based on the assumption that the only operation applied to the dining room is the addition of
        // student discs. This is true at the moment.

        int indexOfSchoolBoard = ViewUtilityFunctions.getPlayerSchoolBoardIndex(playerId, StageController.getStageController().getClientTeams());
        int indexOfLaneInClientTable = 0;
        for (ArrayList<Integer> lane : StageController.getStageController().getClientTable().getSchoolBoards().get(indexOfSchoolBoard).getDiningRoom()) {
            // The index of color x differs from the corresponding lane in the image, thus a mapping is required
            int indexOfLaneInImage = ViewUtilityFunctions.convertPawnColorIndexToLaneIndex(indexOfLaneInClientTable);

            // Add new students to dining room
            if (lane.size() > this.previousDiningRoom.get(indexOfLaneInClientTable).size()) {
                int finalIndexOfLaneInClientTable = indexOfLaneInClientTable;
                Integer[] newStudents = lane
                        .stream()
                        .filter(id1 -> this.previousDiningRoom.get(finalIndexOfLaneInClientTable)
                                .stream().noneMatch(id2 -> id2 == id1))
                        .toArray(Integer[]::new);
                for (int studentId : newStudents) {
                    // Create image view of student
                    ImageView student = this.createImageViewOfStudent(studentId, this.firstAvailableCoordinatesOfDiningRoom[indexOfLaneInImage][1],
                            this.firstAvailableCoordinatesOfDiningRoom[indexOfLaneInImage][0]);
                    // Add image view to pane with school board
                    this.schoolBoard.getChildren().add(student);
                    // Update first available coordinate
                    this.firstAvailableCoordinatesOfDiningRoom[indexOfLaneInImage][0] += GUIConstants.LAYOUT_Y_OFFSET_CELLS_DINING_ROOM;
                }
                // Update content of previous dining room
                this.previousDiningRoom.get(indexOfLaneInClientTable).clear();
                this.previousDiningRoom.get(indexOfLaneInClientTable).addAll(lane);
            }
            // Increment lane index
            indexOfLaneInClientTable++;
        }
    }

    private void updateSchoolBoardProfessorSection(int playerId) throws IllegalLaneException {

        // Determine index of team from player id
        int indexOfTeam = StageController.getStageController().getClientTeams().getTeams()
                .indexOf(StageController.getStageController().getClientTeams().getTeams()
                        .stream()
                        .filter(clientTeam -> clientTeam.getPlayers().
                                stream().
                                filter(clientPlayer -> clientPlayer.getPlayerId() == playerId).count() == 1)
                        .toList().get(0));
        // Case 1: Add professor pawns
        if (this.previousProfessorSection.size() < StageController.getStageController().getClientTeams()
                .getTeams().get(indexOfTeam).getProfessorPawns().size()) {

            // Find professors to add
            ClientPawnColor[] professorsToAdd = StageController.getStageController().getClientTeams().getTeams().get(indexOfTeam).getProfessorPawns()
                    .stream()
                    .filter(professor1 -> this.previousProfessorSection
                            .stream().noneMatch(professor2 -> professor2 == professor1))
                    .toArray(ClientPawnColor[]::new);

            // Add professors to scene
            for (ClientPawnColor professor : professorsToAdd) {
                // Create image view
                ImageView professorPawn = new ImageView(Images.getImages().getProfessorPawns()[professor.getId()]);
                professorPawn.setId(ViewUtilityFunctions.convertProfessorIdToProfessorIdForImageView(professor.getId()));
                professorPawn.setPreserveRatio(false);
                professorPawn.setFitWidth(GUIConstants.WIDTH_OF_PROFESSOR_PAWN);
                professorPawn.setFitHeight(GUIConstants.HEIGHT_OF_PROFESSOR_PAWN);
                // Get index of coordinates of professor pawn image view
                int indexOfProfessorInSchoolBoardImage = ViewUtilityFunctions.convertPawnColorIndexToLaneIndex(professor.getId());
                // Set layout
                professorPawn.setLayoutX(this.coordinatesOfProfessorPawns[indexOfProfessorInSchoolBoardImage][1]);
                professorPawn.setLayoutY(this.coordinatesOfProfessorPawns[indexOfProfessorInSchoolBoardImage][0]);
                // Set effect
                professorPawn.setEffect(new ColorAdjust(0, 0, 0.1, 0));
                // Add professor pawn
                this.schoolBoard.getChildren().add(professorPawn);
            }

            this.previousProfessorSection.clear();
            this.previousProfessorSection.addAll(StageController.getStageController().getClientTeams().getTeams().get(indexOfTeam).getProfessorPawns());
        }

        // Case 2: Remove professor pawns
        else if (this.previousProfessorSection.size() > StageController.getStageController().getClientTeams()
                .getTeams().get(indexOfTeam).getProfessorPawns().size()) {

            // Find professors to remove
            ClientPawnColor[] professorsToRemove = this.previousProfessorSection
                    .stream()
                    .filter(professor1 -> StageController.getStageController().getClientTeams().getTeams().get(indexOfTeam).getProfessorPawns()
                            .stream().noneMatch(professor2 -> professor2 == professor1))
                    .toArray(ClientPawnColor[]::new);

            for (int i = 0; i < this.coordinatesOfProfessorPawns.length; i++) {
                // Get node at this.coordinatesOfProfessorPawns[i] if present
                int finalI = i;
                if (this.schoolBoard.getChildren()
                        .stream()
                        .filter(n -> n.getLayoutX() == this.coordinatesOfProfessorPawns[finalI][1]
                                && n.getLayoutY() == this.coordinatesOfProfessorPawns[finalI][0])
                        .count() == 1) {
                    Node node = this.schoolBoard.getChildren()
                            .stream()
                            .filter(n -> n.getLayoutX() == this.coordinatesOfProfessorPawns[finalI][1]
                                    && n.getLayoutY() == this.coordinatesOfProfessorPawns[finalI][0]).toList().get(0);

                    // Check if the node must be removed
                    if (Arrays.stream(professorsToRemove)
                            .filter(prof -> prof.getId() == ViewUtilityFunctions.convertIdOfImageOfProfessorPawn(node.getId()))
                            .count() == 1) {
                        // Remove node
                        this.schoolBoard.getChildren().remove(node);
                    }
                }
            }

            this.previousProfessorSection.clear();
            this.previousProfessorSection.addAll(StageController.getStageController().getClientTeams().getTeams().get(indexOfTeam).getProfessorPawns());
        }
    }

    private void updateSchoolBoardTowersSection(int playerId) {

        // Determine index of team from player id
        int indexOfTeam = StageController.getStageController().getClientTeams().getTeams()
                .indexOf(StageController.getStageController().getClientTeams().getTeams()
                        .stream()
                        .filter(clientTeam -> clientTeam.getPlayers().
                                stream().
                                filter(clientPlayer -> clientPlayer.getPlayerId() == playerId).count() == 1)
                        .toList().get(0));

        // Save current number of towers and towers color for simplicity
        int currentNumberOfTowers = StageController.getStageController().getClientTeams().getTeams().get(indexOfTeam).getNumberOfTowers();
        int idOfColorOfTowers = StageController.getStageController().getClientTeams().getTeams().get(indexOfTeam).getTowersColor().getId();


        // Case 1: Add towers to tower section
        if (this.previousNumberOfTowers < currentNumberOfTowers) {
            for (int i = this.previousNumberOfTowers; i < currentNumberOfTowers; i++) {
                ImageView tower = new ImageView(Images.getImages().getTowers()[idOfColorOfTowers]);
                tower.setId(GUIConstants.TOWER_NAME + (idOfColorOfTowers * ModelConstants.MAX_NUMBER_OF_TOWERS + i + 1));
                tower.setPreserveRatio(false);
                tower.setFitWidth(GUIConstants.WIDTH_OF_TOWER);
                tower.setFitHeight(GUIConstants.HEIGHT_OF_TOWER);
                tower.setLayoutX(this.coordinatesOfTowers[i][1]);
                tower.setLayoutY(this.coordinatesOfTowers[i][0]);
                tower.setEffect(new ColorAdjust(0, 0, -0.4, 0));
                this.schoolBoard.getChildren().add(tower);
            }
        }

        // Case 2: Remove towers to tower section
        if (this.previousNumberOfTowers > currentNumberOfTowers) {
            for (int i = currentNumberOfTowers; i < this.previousNumberOfTowers; i++) {
                // Get node at this.coordinatesOfTowers[i]
                int finalI = i;
                Node node = this.schoolBoard.getChildren()
                        .stream()
                        .filter(n -> n.getLayoutX() == this.coordinatesOfTowers[finalI][1]
                                && n.getLayoutY() == this.coordinatesOfTowers[finalI][0]).toList().get(0);

                // Remove node
                this.schoolBoard.getChildren().remove(node);
            }
        }
    }

    private ImageView createImageViewOfStudent(int studentId, int layoutX, int layoutY) throws IllegalStudentIdException {
        ImageView student = new ImageView(Images.getImages().getStudentDiscs()[ViewUtilityFunctions.convertStudentIdToIdOfColor(studentId)]);
        student.setId(ViewUtilityFunctions.convertStudentIdToStudentIdForImageView(studentId));
        student.setPreserveRatio(false);
        student.setFitWidth(GUIConstants.WIDTH_OF_STUDENT_DISC);
        student.setFitHeight(GUIConstants.HEIGHT_OF_STUDENT_DISC);
        student.setLayoutX(layoutX);
        student.setLayoutY(layoutY);
        student.setEffect(new ColorAdjust(0, 0, 0.1, 0));
        return student;
    }

    // COORDINATE GENERATORS

    private void generateCoordinates() {
        this.generateCoordinatesOfStudentsInEntrance();
        this.initializeCoordinatesOfDiningRoom();
        this.generateCoordinatesForProfessors();
        this.generateCoordinatesForTowers();
    }

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

    private void generateCoordinatesOfStudentsInEntrance() {
        int coordinateIndex = 0;
        for (int i = GUIConstants.LAYOUT_Y_OF_FIRST_CELL_FIRST_ROW_ENTRANCE;
             i < GUIConstants.LAYOUT_Y_OF_FIRST_CELL_FIRST_ROW_ENTRANCE + GUIConstants.COLUMNS_ENTRANCE * GUIConstants.LAYOUT_Y_OFFSET_CELLS_ENTRANCE; i += GUIConstants.LAYOUT_Y_OFFSET_CELLS_ENTRANCE)
            for (int j = GUIConstants.LAYOUT_X_OF_FIRST_CELL_FIRST_ROW_ENTRANCE;
                 j < GUIConstants.LAYOUT_X_OF_FIRST_CELL_FIRST_ROW_ENTRANCE + GUIConstants.CELLS_FIRST_ROW_ENTRANCE * GUIConstants.LAYOUT_X_OFFSET_CELLS_ENTRANCE; j += GUIConstants.LAYOUT_X_OFFSET_CELLS_ENTRANCE)
                if ((i != GUIConstants.LAYOUT_Y_OF_FIRST_CELL_FIRST_ROW_ENTRANCE + GUIConstants.LAYOUT_Y_OFFSET_CELLS_ENTRANCE)
                        || (j != GUIConstants.LAYOUT_X_OF_FIRST_CELL_FIRST_ROW_ENTRANCE)) {
                    this.coordinatesOfStudentsInEntrance[coordinateIndex][0] = i;
                    this.coordinatesOfStudentsInEntrance[coordinateIndex][1] = j;
                    // The coordinate is not used, set third element to 0
                    this.coordinatesOfStudentsInEntrance[coordinateIndex][2] = 0;
                    coordinateIndex++;
                }
    }

    private void initializeCoordinatesOfDiningRoom() {
        for (int i = 0; i < GUIConstants.LANES; i++) {
            this.firstAvailableCoordinatesOfDiningRoom[i][0] = GUIConstants.LAYOUT_Y_OF_FIRST_CELL_FIRST_LANE_ON_LEFT_BOTTOM_DINING_ROOM;
            this.firstAvailableCoordinatesOfDiningRoom[i][1] = GUIConstants.LAYOUT_X_OF_FIRST_CELL_FIRST_LANE_ON_LEFT_BOTTOM_DINING_ROOM +
                    i * GUIConstants.LAYOUT_X_OFFSET_CELLS_DINING_ROOM;
        }
    }

    private void generateCoordinatesForProfessors() {
        for (int i = 0; i < GUIConstants.LANES; i++) {
            this.coordinatesOfProfessorPawns[i][0] = GUIConstants.LAYOUT_Y_OF_FIRST_CELL_PROFESSOR_SECTION;
            this.coordinatesOfProfessorPawns[i][1] = GUIConstants.LAYOUT_X_OF_FIRST_CELL_PROFESSOR_SECTION + i * GUIConstants.LAYOUT_X_OFFSET_CELLS_PROFESSOR_SECTION;
        }
    }

    private void generateCoordinatesForTowers() {
        int indexOfCoordinate = 0;
        for (int i = GUIConstants.LAYOUT_Y_OF_FIRST_CELL_TOWERS; i > GUIConstants.LAYOUT_Y_OF_FIRST_CELL_TOWERS
                + GUIConstants.NUMBER_OF_ROWS_OF_TOWERS * GUIConstants.LAYOUT_Y_OFFSET_TOWERS; i += GUIConstants.LAYOUT_Y_OFFSET_TOWERS)
            for (int j = GUIConstants.LAYOUT_X_OF_FIRST_CELL_TOWERS; j < GUIConstants.LAYOUT_X_OF_FIRST_CELL_TOWERS
                    + GUIConstants.NUMBER_OF_TOWERS_IN_ROW * GUIConstants.LAYOUT_X_OFFSET_TOWERS; j += GUIConstants.LAYOUT_X_OFFSET_TOWERS) {
                this.coordinatesOfTowers[indexOfCoordinate][1] = j;
                this.coordinatesOfTowers[indexOfCoordinate][0] = i;
                indexOfCoordinate++;
            }
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

    }

    @FXML
    public void switchToDeck(ActionEvent e) {
        try {
            StageController.getStageController().showScene(SceneType.DECK_SCENE, false);
        } catch (Exception exception) {

        }
    }

    // UTILITY FUNCTIONS


}