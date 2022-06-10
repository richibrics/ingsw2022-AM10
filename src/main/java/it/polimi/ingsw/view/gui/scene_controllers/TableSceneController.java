package it.polimi.ingsw.view.gui.scene_controllers;

import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.view.ViewUtilityFunctions;
import it.polimi.ingsw.view.exceptions.IllegalLaneException;
import it.polimi.ingsw.view.exceptions.IllegalStudentIdException;
import it.polimi.ingsw.view.game_objects.*;
import it.polimi.ingsw.view.gui.GUIConstants;
import it.polimi.ingsw.view.gui.SceneType;
import it.polimi.ingsw.view.gui.StageController;
import it.polimi.ingsw.view.gui.exceptions.GuiViewNotSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class TableSceneController extends SceneController {

    // Array of panes containing island tiles
    private final Pane[] islandTiles;
    // Array of panes of character cards
    private final Pane[] characterCards;
    // Array of coordinates
    private final ArrayList<Integer[]> coordinatesOfIslandTile;
    private final Integer[][] coordinatesOfStudentsInEntrance;
    private final Integer[][] firstAvailableCoordinatesOfDiningRoom;
    private final Integer[][] coordinatesOfProfessorPawns;
    private final Integer[][] coordinatesOfTowers;
    private final ArrayList<Integer[]> coordinatesOfStudentsInCharacterCards;
    private final ArrayList<Integer[]> coordinatesOfStudentsOnCloud;
    // Previous state of character cards
    private final int[] previousPricesOfCharacterCards;
    // Previous state of school board
    private final ArrayList<ArrayList<Integer>> previousDiningRoom;
    private final ArrayList<ClientPawnColor> previousProfessorSection;
    // Array of panes containing cloud tiles
    private Pane[] cloudTiles;
    private Integer[] previousEntrance;
    private int previousNumberOfTowers;
    // Previous state of island tiles
    private boolean[][] previousStateOfIslandTiles;

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
        this.islandTiles = new Pane[(ModelConstants.NUMBER_OF_ISLAND_TILES - 1) * ModelConstants.OFFSET_BETWEEN_ISLAND_IDS + ModelConstants.MIN_ID_OF_ISLAND + 1];

        // Create array of Panes of character cards
        this.characterCards = new Pane[ModelConstants.NUMBER_OF_CHARACTER_CARDS];

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

        // Create array list of coordinates of storage of character card
        this.coordinatesOfStudentsInCharacterCards = new ArrayList<>();

        // Create array list of coordinates of students on cloud tile
        this.coordinatesOfStudentsOnCloud = new ArrayList<>();

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

        // Create array of previous prices of character cards
        this.previousPricesOfCharacterCards = new int[ModelConstants.NUMBER_OF_CHARACTER_CARDS];
    }

    // MAIN METHODS

    @Override
    protected Scene layout() {
        // Call for scene creation, that call updateScene to update the content of the table
        // The scene cannot be created if clientTable and clientTeams have not been set.
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/table_scene.fxml"));
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

            // Add cloud tiles to root
            this.addCloudTiles(root);

            // Add character cards if needed
            if (StageController.getStageController().getGuiView().getUser().getPreference() > 0)
                this.addCharacterCards(root);

            // Fill the game objects
            this.updateScene();
            // Return the newly created scene
            return new Scene(root);
        } catch (IOException | GuiViewNotSet e) {
            e.printStackTrace();
            // TODO do something
            return null;
        }
    }

    public void updateScene() {

        try {
            // FILL ISLANDS
            this.addSGameObjectsToIslandTiles();
            // Move islands to create island groups
            // TODO method for island movements

            // FILL SCHOOL BOARD
            // Get player id
            int playerId = StageController.getStageController().getGuiView().getPlayerId();
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

            // FILL CLOUD TILES
            this.fillCloudTiles();

            // FILL CHARACTER CARDS
            if (StageController.getStageController().getGuiView().getUser().getPreference() > 0)
                this.fillCharacterCards();
        } catch (IllegalStudentIdException | IllegalLaneException | GuiViewNotSet e) {
            e.printStackTrace();
            // TODO do something
        }
    }

    // METHODS FOR SCENE UPDATE

    private void addSGameObjectsToIslandTiles() throws IllegalStudentIdException {

        // Create array of state of island tiles if it has not been already created
        if (this.previousStateOfIslandTiles == null)
            this.previousStateOfIslandTiles = new boolean[(ModelConstants.NUMBER_OF_ISLAND_TILES - 1)
                    * ModelConstants.OFFSET_BETWEEN_ISLAND_IDS + ModelConstants.MIN_ID_OF_ISLAND + 1][3];

        // Get mother nature island id
        int motherNatureIsland = StageController.getStageController().getClientTable().getMotherNature().getIsland();

        for (ArrayList<ClientIslandTile> islandGroups : StageController.getStageController().getClientTable().getIslandTiles())
            for (ClientIslandTile clientIslandTile : islandGroups) {

                // Divide island tile in 40x40 cells. A 1 x 6 matrix of 20 x 20 cells remains unused in this phase
                int height = GUIConstants.HEIGHT_OF_RECTANGLE_CONTAINING_GAME_OBJECTS_IN_ISLAND - GUIConstants.HEIGHT_OF_STUDENT_DISC;
                int length = GUIConstants.WIDTH_OF_RECTANGLE_CONTAINING_GAME_OBJECTS_IN_ISLAND;
                this.getCoordinatesOfCellsForMotherNatureNoEntryAndTower(height, length);

                // Get all the children currently on the island tile which are not the island tile image view
                ArrayList<Node> children = new ArrayList<>();
                for (Node node : this.islandTiles[clientIslandTile.getId()].getChildren())
                    if (!node.getId().contains(GUIConstants.ISLAND_TILE_NAME))
                        children.add(node);

                // Check change in presence of mother nature or no entry tile or tower (no color change, only presence)
                // occupancyHasChanged is true when there has been a change in the occupancy of the island tile
                boolean occupancyHasChanged;
                boolean result;
                // Case 1: mother nature
                occupancyHasChanged = this.checkMotherNatureOrNoEntryOrTowerPresence(clientIslandTile, motherNatureIsland == clientIslandTile.getId(),
                        this.previousStateOfIslandTiles[clientIslandTile.getId()][0], children, GUIConstants.MOTHER_NATURE_NAME, Images.getImages().getMotherNaturePawn(),
                        GUIConstants.WIDTH_OF_MOTHER_NATURE, GUIConstants.HEIGHT_OF_MOTHER_NATURE_PAWN, false, null);

                // Case 2: no entry tile
                result = this.checkMotherNatureOrNoEntryOrTowerPresence(clientIslandTile, clientIslandTile.hasNoEntry(),
                        this.previousStateOfIslandTiles[clientIslandTile.getId()][1], children, GUIConstants.NO_ENTRY_TILE_NAME, Images.getImages().getNoEntryTile(),
                        GUIConstants.WIDTH_OF_NO_ENTRY, GUIConstants.HEIGHT_OF_NO_ENTRY, false, null);
                occupancyHasChanged = occupancyHasChanged || result;

                // Case 3: tower
                // Get tower image if possible
                Image imageOfTower = null;
                if (clientIslandTile.getTower() != null)
                    imageOfTower = Images.getImages().getTowers()[clientIslandTile.getTower().getId()];
                // A null image does not create problems since the image is not accessed
                result = this.checkMotherNatureOrNoEntryOrTowerPresence(clientIslandTile, clientIslandTile.getTower() != null,
                        this.previousStateOfIslandTiles[clientIslandTile.getId()][2], children, GUIConstants.TOWER_NAME, imageOfTower,
                        GUIConstants.WIDTH_OF_TOWER_ISLAND_TILE, GUIConstants.HEIGHT_OF_TOWER_ISLAND_TILE, true, clientIslandTile.getTower());
                occupancyHasChanged = occupancyHasChanged || result;

                // Update previous state
                this.previousStateOfIslandTiles[clientIslandTile.getId()][0] = motherNatureIsland == clientIslandTile.getId();
                this.previousStateOfIslandTiles[clientIslandTile.getId()][1] = clientIslandTile.hasNoEntry();
                this.previousStateOfIslandTiles[clientIslandTile.getId()][2] = clientIslandTile.getTower() != null;

                // Divide remaining coordinates in 20x20 cells
                this.divideCoordinatesInSmallerCellsForStudents();
                // Add remaining cells
                for (int j = GUIConstants.LAYOUT_X_OF_FIRST_AVAILABLE_CELL_FOR_GAME_OBJECTS_IN_ISLAND;
                     j < GUIConstants.LAYOUT_X_OF_FIRST_AVAILABLE_CELL_FOR_GAME_OBJECTS_IN_ISLAND + GUIConstants.WIDTH_OF_RECTANGLE_CONTAINING_GAME_OBJECTS_IN_ISLAND;
                     j += GUIConstants.WIDTH_OF_STUDENT_DISC)
                    this.coordinatesOfIslandTile.add(new Integer[]{height, j});

                // At this point children contains only student discs
                // If occupancy has changed the layout of the student discs must be modified

                Random random = new Random();
                Integer[] coordinate;
                for (int studentId : clientIslandTile.getStudents()) {
                    // Case 1: the student disc is already on the island tile
                    if (children.stream().filter(node -> ViewUtilityFunctions.convertIdOfImageOfStudentDisc(node.getId()) == studentId).count() == 1) {
                        // Get corresponding node and remove it from children
                        Node student = children.stream().filter(node -> ViewUtilityFunctions.convertIdOfImageOfStudentDisc(node.getId()) == studentId).toList().get(0);
                        children.remove(student);
                        // If occupancy has changed get new coordinates for student disc
                        if (occupancyHasChanged) {
                            coordinate = this.coordinatesOfIslandTile.remove(random.nextInt(this.coordinatesOfIslandTile.size()));
                            student.setLayoutX(coordinate[1]);
                            student.setLayoutY(coordinate[0]);
                        }
                        // If occupancy has not changed do not move student disc and remove its coordinates from the list of available
                        // coordinates
                        else
                            // Remove coordinate of node from the list of coordinates
                            this.coordinatesOfStudentsOnCloud.remove(this.coordinatesOfStudentsOnCloud
                                    .stream()
                                    .filter(coordinateOfStudent -> coordinateOfStudent[0] == student.getLayoutY() && coordinateOfStudent[1] == student.getLayoutX())
                                    .toList().get(0));
                    }
                    // Case 2: the student disc is not on the island tile
                    else {
                        coordinate = this.coordinatesOfIslandTile.remove(random.nextInt(this.coordinatesOfIslandTile.size()));
                        ImageView student = SchoolBoardsFunction.createImageViewOfStudent(studentId, coordinate[1], coordinate[0]);
                        this.islandTiles[clientIslandTile.getId()].getChildren().add(student);
                    }
                }
            }
    }

    private boolean checkMotherNatureOrNoEntryOrTowerPresence(ClientIslandTile islandTile, boolean currentStateOfIsland, boolean prevStateOfIsland, ArrayList<Node> children,
                                                              String gameObjectName, Image image, int width, int height, boolean tower, ClientTowerColor color) {

        // Case 1: Game object on the island in previous state
        if (prevStateOfIsland) {
            // Get node of game object
            Node node = children.stream().filter(n -> n.getId().contains(gameObjectName)).toList().get(0);
            // Case 1.1: Game object still on island. Keep it where it is and remove coordinate from array
            if (currentStateOfIsland) {
                // Remove coordinate of object
                this.coordinatesOfIslandTile.remove(this.coordinatesOfIslandTile.stream()
                        .filter(c -> c[0] == node.getLayoutY() && c[1] == node.getLayoutX()).toList().get(0));
                if (tower) {
                    // The color of the tower could have changed
                    ImageView towerImageView = (ImageView) node;
                    towerImageView.setImage(image);
                }
                // Remove node from list of children
                children.remove(node);
                return false;
            }
            // Case 1.2: Game object not on island. Remove it from island
            else {
                // Remove node
                this.islandTiles[islandTile.getId()].getChildren().remove(node);
                // Remove node from list of children
                children.remove(node);
                return true;
            }
        }
        // Case 2: Game object not on the island in previous state
        else {
            // Case 2.1: Game object on the island now. Create new image view of object
            if (currentStateOfIsland) {
                Integer[] coordinate = this.coordinatesOfIslandTile.remove(new Random().nextInt(this.coordinatesOfIslandTile.size()));
                ImageView imageView = new ImageView(image);
                imageView.setId(gameObjectName + islandTile.getId());
                imageView.setPreserveRatio(false);
                imageView.setFitWidth(width);
                imageView.setFitHeight(height);
                imageView.setLayoutX(coordinate[1]);
                imageView.setLayoutY(coordinate[0]);
                if (tower) {
                    if (color.equals(ClientTowerColor.BLACK))
                        imageView.setEffect(new ColorAdjust(0, 0, -0.4, 0));
                    else if (color.equals(ClientTowerColor.WHITE))
                        imageView.setEffect(new ColorAdjust(0, 0, 0.3, 0));
                }
                this.islandTiles[islandTile.getId()].getChildren().add(imageView);
                return true;
            }
            // Case 2.2: Game object not on the island
            else
                return false;
        }
    }

    private void moveIslandTiles() {

    }

    private void fillCloudTiles() throws IllegalStudentIdException {
        // This method works even if clouds are half full. Based on the current implementation this cannot happen.

        int indexOfCloud = 0;
        for (ClientCloudTile clientCloudTile : StageController.getStageController().getClientTable().getCloudTiles()) {

            // Regenerate coordinates
            this.regenerateCoordinatesForStudentDiscsOfCloudTile();

            // Get all the children of the pane containing the cloud tile which are not the cloud tile and put them
            // in a list
            ArrayList<Node> children = new ArrayList<>();
            for (Node node : this.cloudTiles[indexOfCloud].getChildren())
                if (node.getId().contains(GUIConstants.STUDENT_DISC_NAME))
                    children.add(node);

            // Add student discs
            Integer[] coordinate;
            Random random = new Random();
            for (int studentId : clientCloudTile.getStudents()) {
                // Case 1: the student disc is still on the cloud tile
                if (children.stream().filter(node -> ViewUtilityFunctions.convertIdOfImageOfStudentDisc(node.getId()) == studentId).count() == 1) {
                    // Get corresponding node and remove it from children
                    Node student = children.stream().filter(node -> ViewUtilityFunctions.convertIdOfImageOfStudentDisc(node.getId()) == studentId).toList().get(0);
                    children.remove(student);
                    // Remove coordinate of the node from the list of coordinates
                    this.coordinatesOfStudentsOnCloud.remove(this.coordinatesOfStudentsOnCloud
                            .stream()
                            .filter(coordinateOfStudent -> coordinateOfStudent[0] == student.getLayoutY() && coordinateOfStudent[1] == student.getLayoutX())
                            .toList().get(0));
                }
                // Case 1: the student disc is not on the cloud tile
                else {
                    coordinate = this.coordinatesOfStudentsOnCloud.remove(random.nextInt(this.coordinatesOfStudentsOnCloud.size()));
                    ImageView student = SchoolBoardsFunction.createImageViewOfStudent(studentId, coordinate[1], coordinate[0]);
                    this.cloudTiles[indexOfCloud].getChildren().add(student);
                }
            }

            // Remove remaining children from cloud tile
            while (children.size() != 0) {
                this.cloudTiles[indexOfCloud].getChildren().remove(children.get(0));
                children.remove(0);
            }

            // Update index of cloud tile
            indexOfCloud++;
        }
    }

    private void fillCharacterCards() throws IllegalStudentIdException {

        int indexOfCharacterCard = 0;
        for (ClientCharacterCard clientCharacterCard : StageController.getStageController().getClientTable().getActiveCharacterCards()) {
            // In this case it is necessary to "empty" the storage because the students in the storage could be substituted between consecutive
            // calls (i.e. no change in the size of the storage but change in the content)

            // Regenerate coordinates
            this.regenerateCoordinatesForCharacterCardsStorage();

            // Get all the children of the pane containing the character card which are not the character card and put them
            // in a list
            ArrayList<Node> children = new ArrayList<>();
            for (Node node : this.characterCards[indexOfCharacterCard].getChildren())
                if (node.getId().contains(GUIConstants.STUDENT_DISC_NAME))
                    children.add(node);

            // Add student discs
            Integer[] coordinate;
            Random random = new Random();
            for (int studentId : clientCharacterCard.getStorage()) {
                // Case 1: the student disc is still in the storage
                if (children.stream().filter(node -> ViewUtilityFunctions.convertIdOfImageOfStudentDisc(node.getId()) == studentId).count() == 1) {
                    // Get corresponding node and remove it from children
                    Node student = children.stream().filter(node -> ViewUtilityFunctions.convertIdOfImageOfStudentDisc(node.getId()) == studentId).toList().get(0);
                    children.remove(student);
                    // Remove coordinate of the node from the list of coordinates
                    this.coordinatesOfStudentsInCharacterCards.remove(this.coordinatesOfStudentsInCharacterCards
                            .stream()
                            .filter(coordinateOfCard -> coordinateOfCard[0] == student.getLayoutY() && coordinateOfCard[1] == student.getLayoutX())
                            .toList().get(0));
                }
                // Case 1: the student disc is not in the storage
                else {
                    coordinate = this.coordinatesOfStudentsInCharacterCards.remove(random.nextInt(this.coordinatesOfStudentsInCharacterCards.size()));
                    ImageView student = SchoolBoardsFunction.createImageViewOfStudent(studentId, coordinate[1], coordinate[0]);
                    this.characterCards[indexOfCharacterCard].getChildren().add(student);
                }
            }

            // Remove remaining children from character card storage
            while (children.size() != 0) {
                this.characterCards[indexOfCharacterCard].getChildren().remove(children.get(0));
                children.remove(0);
            }

            // Add coin if the price of the character card has changed
            if (this.previousPricesOfCharacterCards[indexOfCharacterCard] == 0)
                this.previousPricesOfCharacterCards[indexOfCharacterCard] = clientCharacterCard.getCost();

            if (clientCharacterCard.getCost() > this.previousPricesOfCharacterCards[indexOfCharacterCard]) {
                coordinate = this.coordinatesOfStudentsInCharacterCards.remove(random.nextInt(this.coordinatesOfStudentsInCharacterCards.size()));
                ImageView coin = new ImageView(Images.getImages().getCoin());
                coin.setId(GUIConstants.COIN_NAME + clientCharacterCard.getId());
                coin.setPreserveRatio(false);
                coin.setFitWidth(GUIConstants.WIDTH_OF_COIN);
                coin.setFitHeight(GUIConstants.HEIGHT_OF_COIN);
                coin.setLayoutX(coordinate[1]);
                coin.setLayoutY(coordinate[0]);
                this.characterCards[indexOfCharacterCard].getChildren().add(coin);
                this.previousPricesOfCharacterCards[indexOfCharacterCard] = clientCharacterCard.getCost();
            }

            // Update index of character card
            indexOfCharacterCard++;
        }
    }

    // COORDINATES GENERATOR

    private void getCoordinatesOfCellsForMotherNatureNoEntryAndTower(int height, int length) {
        // The first coordinate is the one of the cell in the top left corner
        this.coordinatesOfIslandTile.clear();
        for (int i = GUIConstants.LAYOUT_Y_OF_FIRST_AVAILABLE_CELL_FOR_GAME_OBJECTS_IN_ISLAND;
             i < GUIConstants.LAYOUT_Y_OF_FIRST_AVAILABLE_CELL_FOR_GAME_OBJECTS_IN_ISLAND + height; i += GUIConstants.HEIGHT_OF_MOTHER_NATURE_PAWN)
            for (int j = GUIConstants.LAYOUT_X_OF_FIRST_AVAILABLE_CELL_FOR_GAME_OBJECTS_IN_ISLAND;
                 j < GUIConstants.LAYOUT_X_OF_FIRST_AVAILABLE_CELL_FOR_GAME_OBJECTS_IN_ISLAND + length; j += GUIConstants.WIDTH_OF_MOTHER_NATURE)
                this.coordinatesOfIslandTile.add(new Integer[]{i, j});
    }

    private void divideCoordinatesInSmallerCellsForStudents() {
        // Divide 40x40 cells in 20x20 cells
        ArrayList<Integer[]> newCoordinates = new ArrayList<>();
        for (Integer[] coordinate : this.coordinatesOfIslandTile) {
            newCoordinates.add(coordinate);
            newCoordinates.add(new Integer[]{coordinate[0], coordinate[1] + GUIConstants.WIDTH_OF_STUDENT_DISC});
            newCoordinates.add(new Integer[]{coordinate[0] + GUIConstants.HEIGHT_OF_STUDENT_DISC, coordinate[1]});
            newCoordinates.add(new Integer[]{coordinate[0] + GUIConstants.HEIGHT_OF_STUDENT_DISC, coordinate[1] + GUIConstants.WIDTH_OF_STUDENT_DISC});
        }
        // Add new coordinates to array of coordinates
        this.coordinatesOfIslandTile.clear();
        this.coordinatesOfIslandTile.addAll(newCoordinates);
    }

    private void regenerateCoordinatesForCharacterCardsStorage() {

        this.coordinatesOfStudentsInCharacterCards.clear();
        for (int i = GUIConstants.LAYOUT_Y_OF_FIRST_AVAILABLE_CELL_FOR_GAME_OBJECTS_IN_CHARACTER_CARD;
             i < GUIConstants.LAYOUT_Y_OF_FIRST_AVAILABLE_CELL_FOR_GAME_OBJECTS_IN_CHARACTER_CARD
                     + GUIConstants.HEIGHT_OF_RECTANGLE_CONTAINING_GAME_OBJECTS_IN_CHARACTER_CARD;
             i += GUIConstants.HEIGHT_OF_STUDENT_DISC)
            for (int j = GUIConstants.LAYOUT_X_OF_FIRST_AVAILABLE_CELL_FOR_GAME_OBJECTS_IN_CHARACTER_CARD;
                 j < GUIConstants.LAYOUT_X_OF_FIRST_AVAILABLE_CELL_FOR_GAME_OBJECTS_IN_CHARACTER_CARD
                         + GUIConstants.WIDTH_OF_RECTANGLE_CONTAINING_GAME_OBJECTS_IN_CHARACTER_CARD;
                 j += GUIConstants.WIDTH_OF_STUDENT_DISC)

                this.coordinatesOfStudentsInCharacterCards.add(new Integer[]{i, j});

    }

    private void regenerateCoordinatesForStudentDiscsOfCloudTile() {
        this.coordinatesOfStudentsOnCloud.clear();
        // Get number of players
        int numberOfPlayers = (int) StageController.getStageController().getClientTeams().getTeams()
                .stream()
                .mapToLong(clientTeam -> clientTeam.getPlayers().size())
                .sum();
        if (numberOfPlayers % 2 == 0)
            Collections.addAll(this.coordinatesOfStudentsOnCloud, GUIConstants.POSITIONS_OF_STUDENTS_CLOUD_WITH_THREE_SPACES);
        else
            Collections.addAll(this.coordinatesOfStudentsOnCloud, GUIConstants.POSITIONS_OF_STUDENTS_CLOUD_WITH_FOUR_SPACES);
    }

    // PANES GENERATORS AND LOADERS

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

    private void addCloudTiles(AnchorPane root) {

        // Get number of cloud tiles
        int numberOfCloudTiles = StageController.getStageController().getClientTable().getCloudTiles().size();

        // Create array of Panes containing the cloud tiles
        this.cloudTiles = new Pane[numberOfCloudTiles];

        // Get cloud tiles images
        Image[] imagesOfClouds = Images.getImages().getCloudTiles();

        // Get layout x of first cloud on the left
        int layoutXOfFirstCloud;
        int offsetBetweenClouds = GUIConstants.LAYOUT_X_OFFSET_BETWEEN_CLOUD_TILES - GUIConstants.WIDTH_OF_CLOUD_TILE;

        if (numberOfCloudTiles % 2 == 0)
            layoutXOfFirstCloud = GUIConstants.LAYOUT_X_CENTER_OF_ISLAND_CIRCLE - (numberOfCloudTiles / 2)
                    * GUIConstants.WIDTH_OF_CLOUD_TILE - (numberOfCloudTiles / 2 - 1) * offsetBetweenClouds - offsetBetweenClouds / 2;

        else
            layoutXOfFirstCloud = GUIConstants.LAYOUT_X_CENTER_OF_ISLAND_CIRCLE - Math.floorDiv(numberOfCloudTiles, 2)
                    * (GUIConstants.WIDTH_OF_CLOUD_TILE + offsetBetweenClouds) - GUIConstants.WIDTH_OF_CLOUD_TILE / 2;

        // Add cloud tiles images to scene
        int indexOfImage = 0;
        for (int indexOfCloudTile = 0; indexOfCloudTile < numberOfCloudTiles; indexOfCloudTile++) {
            // Create pane
            Pane cloudPane = new Pane();
            cloudPane.setPrefSize(GUIConstants.WIDTH_OF_CLOUD_TILE, GUIConstants.HEIGHT_OF_CLOUD_TILE);
            cloudPane.setLayoutX(layoutXOfFirstCloud +
                    indexOfCloudTile * GUIConstants.LAYOUT_X_OFFSET_BETWEEN_CLOUD_TILES);
            cloudPane.setLayoutY(GUIConstants.LAYOUT_Y_OF_CLOUD_TILES);
            cloudPane.setId(GUIConstants.CLOUD_TILE_PANE_NAME + (indexOfCloudTile + 1));
            // Create image view and add to pane
            ImageView cloudTileImageView = new ImageView(imagesOfClouds[indexOfImage]);
            cloudTileImageView.setId(GUIConstants.CLOUD_TILE_NAME
                    + StageController.getStageController().getClientTable().getCloudTiles().get(indexOfCloudTile).getId());
            cloudTileImageView.setPreserveRatio(false);
            cloudTileImageView.setFitWidth(GUIConstants.WIDTH_OF_CLOUD_TILE);
            cloudTileImageView.setFitHeight(GUIConstants.HEIGHT_OF_CLOUD_TILE);
            cloudPane.getChildren().add(cloudTileImageView);
            // Add pane to root and to array of panes of character cards
            root.getChildren().add(cloudPane);
            this.cloudTiles[indexOfCloudTile] = cloudPane;

            // Update index of image if the number of players equals 3, i.e. if the number of cloud tiles cannot be divided by 2
            if (numberOfCloudTiles % 2 != 0)
                indexOfImage++;
        }
    }

    private void addCharacterCards(AnchorPane root) {
        int indexCharacterCard = 0;

        for (Image characterCardImage : Images.getImages().getCharacterCards()) {
            // Create pane
            Pane characterCardPane = new Pane();
            characterCardPane.setPrefSize(GUIConstants.WIDTH_OF_CHARACTER_CARD, GUIConstants.HEIGHT_OF_CHARACTER_CARD);
            characterCardPane.setLayoutX(GUIConstants.LAYOUT_X_OF_FIRST_CHARACTER_CARD_ON_LEFT +
                    indexCharacterCard * GUIConstants.LAYOUT_X_OFFSET_BETWEEN_CHARACTER_CARDS);
            characterCardPane.setLayoutY(GUIConstants.LAYOUT_Y_OF_CHARACTER_CARDS);
            characterCardPane.setId(GUIConstants.CHARACTER_CARD_PANE_NAME + (indexCharacterCard + 1));
            // Create image view and add to pane
            ImageView characterCardImageView = new ImageView(characterCardImage);
            characterCardImageView.setId(GUIConstants.CHARACTER_CARD_IMAGE_NAME
                    + StageController.getStageController().getClientTable().getActiveCharacterCards().get(indexCharacterCard).getId());
            characterCardImageView.setPreserveRatio(false);
            characterCardImageView.setFitWidth(GUIConstants.WIDTH_OF_CHARACTER_CARD);
            characterCardImageView.setFitHeight(GUIConstants.HEIGHT_OF_CHARACTER_CARD);
            characterCardPane.getChildren().add(characterCardImageView);
            // Add pane to root and to array of panes of character cards
            root.getChildren().add(characterCardPane);
            this.characterCards[indexCharacterCard] = characterCardPane;
            indexCharacterCard++;
        }
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