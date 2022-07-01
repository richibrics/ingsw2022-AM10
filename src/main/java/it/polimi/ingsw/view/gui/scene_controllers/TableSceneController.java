package it.polimi.ingsw.view.gui.scene_controllers;

import it.polimi.ingsw.controller.Serializer;
import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.model.game_components.Character;
import it.polimi.ingsw.model.game_components.PawnColor;
import it.polimi.ingsw.view.ViewUtilityFunctions;
import it.polimi.ingsw.view.exceptions.IllegalLaneException;
import it.polimi.ingsw.view.exceptions.IllegalStudentIdException;
import it.polimi.ingsw.view.game_objects.*;
import it.polimi.ingsw.view.gui.GUIConstants;
import it.polimi.ingsw.view.gui.SceneType;
import it.polimi.ingsw.view.gui.StageController;
import it.polimi.ingsw.view.input_management.Command;
import it.polimi.ingsw.view.input_management.CommandDataEntryValidationSet;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class TableSceneController extends SceneController {

    // Array of panes containing island tiles
    private final Pane[] islandTiles;
    // Array of panes of character cards
    private final Pane[] characterCards;
    // Array of panes of assistant cards
    private final Pane[] panesOfAssistantCards;
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
    private final ArrayList<ClientPawnColor> previousProfessorSection;
    private final ArrayList<ArrayList<Integer>> previousCompositionOfIslandGroups;
    // Previous state of school board
    private ArrayList<ArrayList<Integer>> previousDiningRoom;
    // Array of image views of assistant cards played by the other players
    private ImageView[] assistantCards;
    // Array of labels for assistant cards
    private Label[] labelsForAssistantCards;
    // Bool for character cards
    private boolean firstFillOfCharacterCards;
    // Array of panes containing cloud tiles
    private Pane[] cloudTiles;
    private Integer[] previousEntrance;
    private int previousNumberOfTowers;
    // Previous state of island tiles
    private boolean[][] previousStateOfIslandTiles;
    @FXML
    private AnchorPane mainPane;
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

    private Label coin;

    public TableSceneController() {

        // Create array of Panes containing the island tiles
        this.islandTiles = new Pane[(ModelConstants.NUMBER_OF_ISLAND_TILES - 1) * ModelConstants.OFFSET_BETWEEN_ISLAND_IDS + ModelConstants.MIN_ID_OF_ISLAND + 1];

        // Create array of Panes of character cards
        this.characterCards = new Pane[ModelConstants.NUMBER_OF_CHARACTER_CARDS];

        // Create array of Panes of assistant cards
        this.panesOfAssistantCards = new Pane[ModelConstants.FOUR_PLAYERS - 1];

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

        // Create array for previous composition of island groups
        this.previousCompositionOfIslandGroups = new ArrayList<>();

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

        this.firstFillOfCharacterCards = true;
    }

    // METHODS FOR SCENE UPDATE

    @Override
    protected Scene layout() {
        // Call for scene creation, then call updateScene to update the content of the table
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
            // Set root
            this.setRoot(root);
            // Load panes of island tiles in array of panes
            this.loadPanesInArrayOfIslandTilePanes();
            // Load panes of assistant cards in array of panes
            this.addAssistantCards(root);
            // Generate coordinates
            SchoolBoardsFunction.generateCoordinates(this.coordinatesOfStudentsInEntrance, this.firstAvailableCoordinatesOfDiningRoom,
                    this.coordinatesOfProfessorPawns, this.coordinatesOfTowers);

            // Add cloud tiles to root
            this.addCloudTiles(root);

            // Add character cards if needed
            if (StageController.getStageController().getGuiView().getUser().getPreference() > 0)
                this.addCharacterCards(root);

            // Add bulb for hints
            this.addBulb(root);
            // Fill the game objects
            this.updateScene();
            // Return the newly created scene
            return new Scene(root);
        } catch (IOException e) {
            StageController.getStageController().getGuiView().getClientServerConnection().askToCloseConnectionWithError(e.getMessage());
            return null;
        }
    }

    @Override
    protected void updateScene() {

        try {
            // FILL ISLANDS
            this.addGameObjectsToIslandTiles();
            // Move islands to create island groups
            this.moveIslandTiles();

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

            // Write amount of coins over school board
            if (StageController.getStageController().getGuiView().getUser().getPreference() > 0)
                this.updateAmountOfCoins(indexOfTeam);
            // Update entrance
            this.previousEntrance = SchoolBoardsFunction.updateSchoolBoardEntrance(indexOfSchoolBoard, this.schoolBoard,
                    this.coordinatesOfStudentsInEntrance, this.previousEntrance, true);
            // Update dining room
            SchoolBoardsFunction.updateSchoolBoardDiningRoom(indexOfSchoolBoard, this.schoolBoard,
                    this.firstAvailableCoordinatesOfDiningRoom, this.previousDiningRoom, true);
            this.previousDiningRoom = StageController.getStageController().getClientTable().getSchoolBoards().get(indexOfSchoolBoard).getDiningRoom();
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

            // ADD ASSISTANT CARDS PLAYED BY THE OTHER PLAYERS
            this.addAssistantCardsPlayedByOtherPlayers();

        } catch (IllegalStudentIdException | IllegalLaneException e) {
            StageController.getStageController().getGuiView().getClientServerConnection().askToCloseConnectionWithError(e.getMessage());
        }
    }

    private void updateAmountOfCoins(int indexOfTeam) {
        // Determine index of player
        ClientTeams clientTeams = StageController.getStageController().getClientTeams();
        int playerId = StageController.getStageController().getGuiView().getPlayerId();
        int indexOfPlayer = clientTeams.getTeams().get(indexOfTeam).getPlayers().indexOf(clientTeams.getTeams().get(indexOfTeam).getPlayers().
                stream().
                filter(clientPlayer -> clientPlayer.getPlayerId() == playerId).
                toList().get(0));

        if (this.coin == null) {
            // Create label
            Label label = new Label(GUIConstants.LABEL_FOR_COIN_START + StageController.getStageController().getClientTeams()
                    .getTeams().get(indexOfTeam).getPlayers().get(indexOfPlayer).getCoins());
            // Set properties
            label.setId(GUIConstants.LABEL_ID);
            label.setPrefSize(GUIConstants.WIDTH_OF_LABEL_FOR_USERNAMES, GUIConstants.HEIGHT_OF_LABEL_FOR_USERNAMES);
            label.setLayoutX(GUIConstants.LAYOUT_X_OF_SCHOOL_BOARD_IN_TABLE_SCENE);
            label.setLayoutY(GUIConstants.LAYOUT_Y_OF_LABEL_FOR_COINS_IN_TABLE_SCENE);
            label.setAlignment(Pos.CENTER_LEFT);
            label.setFont(Font.font(GUIConstants.FONT, FontPosture.REGULAR, GUIConstants.FONT_SIZE_USERNAME));
            // Add label to root
            this.mainPane.getChildren().add(label);
            this.coin = label;
        } else {
            this.coin.setText(GUIConstants.LABEL_FOR_COIN_START + StageController.getStageController().getClientTeams()
                    .getTeams().get(indexOfTeam).getPlayers().get(indexOfPlayer).getCoins());
        }
    }

    private void addGameObjectsToIslandTiles() throws IllegalStudentIdException {

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

                // Get tower image if possible
                Image imageOfTower = null;
                if (clientIslandTile.getTower() != null)
                    imageOfTower = Images.getImages().getTowers()[clientIslandTile.getTower().getId()];

                // Check change in presence of mother nature or no entry tile or tower (no color change, only presence)
                // objectsHaveBeenAdded is true when there are new objects on the island of type tower, mother nature pawn
                // or no entry tiles (compared to the previous state of the island)
                // objectsHaveBeenRemoved is true when objects of type tower, mother nature pawn or no entry tiles have been removed
                // (compared to the previous state of the island)

                boolean objectsHaveBeenAdded = (!this.previousStateOfIslandTiles[clientIslandTile.getId()][0] && motherNatureIsland == clientIslandTile.getId()) ||
                        (!this.previousStateOfIslandTiles[clientIslandTile.getId()][1] && clientIslandTile.hasNoEntry()) ||
                        (!this.previousStateOfIslandTiles[clientIslandTile.getId()][2] && !clientIslandTile.getTower().equals(ClientTowerColor.EMPTY));
                boolean objectsHaveBeenRemoved = (this.previousStateOfIslandTiles[clientIslandTile.getId()][0] && !(motherNatureIsland == clientIslandTile.getId())) ||
                        (this.previousStateOfIslandTiles[clientIslandTile.getId()][1] && !clientIslandTile.hasNoEntry()) ||
                        (this.previousStateOfIslandTiles[clientIslandTile.getId()][2] && clientIslandTile.getTower().equals(ClientTowerColor.EMPTY));
                boolean changeStudentPosition = false;

                // Case 1: objectsHaveBeenAdded == true. In this case resample the coordinates of all the 40x40 objects
                // and of the student discs as well.
                if (objectsHaveBeenAdded) {
                    // Mother nature
                    this.handleObjectsHaveBeenAdded(clientIslandTile, motherNatureIsland == clientIslandTile.getId(),
                            this.previousStateOfIslandTiles[clientIslandTile.getId()][0], children, GUIConstants.MOTHER_NATURE_NAME, Images.getImages().getMotherNaturePawn(),
                            GUIConstants.WIDTH_OF_MOTHER_NATURE, GUIConstants.HEIGHT_OF_MOTHER_NATURE_PAWN, 0, null);

                    // No entry
                    this.handleObjectsHaveBeenAdded(clientIslandTile, clientIslandTile.hasNoEntry(),
                            this.previousStateOfIslandTiles[clientIslandTile.getId()][1], children, GUIConstants.NO_ENTRY_TILE_NAME, Images.getImages().getNoEntryTile(),
                            GUIConstants.WIDTH_OF_NO_ENTRY, GUIConstants.HEIGHT_OF_NO_ENTRY, 1, null);

                    // Tower
                    this.handleObjectsHaveBeenAdded(clientIslandTile, !clientIslandTile.getTower().equals(ClientTowerColor.EMPTY),
                            this.previousStateOfIslandTiles[clientIslandTile.getId()][2], children, GUIConstants.TOWER_NAME, imageOfTower,
                            GUIConstants.WIDTH_OF_TOWER_ISLAND_TILE, GUIConstants.HEIGHT_OF_TOWER_ISLAND_TILE, 2, clientIslandTile.getTower());

                    changeStudentPosition = true;
                }

                // Case 2: objectsHaveBeenRemoved and not objectHaveBeenAdded. In this case simply remove the coordinates of the 40x40 objects that
                // are still on the island tile without moving them. Do not move the students as well.
                else if (objectsHaveBeenRemoved) {
                    // Mother nature
                    if (this.previousStateOfIslandTiles[clientIslandTile.getId()][0] && !(motherNatureIsland == clientIslandTile.getId()))
                        this.removeTheObject(GUIConstants.MOTHER_NATURE_NAME, clientIslandTile, children);
                    else
                        this.removeCoordinateOfObjectWithoutChangingCoordinateOfNode(GUIConstants.MOTHER_NATURE_NAME, children, null, 0);

                    // No entry tile
                    if (this.previousStateOfIslandTiles[clientIslandTile.getId()][1] && !clientIslandTile.hasNoEntry())
                        this.removeTheObject(GUIConstants.NO_ENTRY_TILE_NAME, clientIslandTile, children);
                    else
                        this.removeCoordinateOfObjectWithoutChangingCoordinateOfNode(GUIConstants.NO_ENTRY_TILE_NAME, children, null, 1);

                    // Tower
                    if (this.previousStateOfIslandTiles[clientIslandTile.getId()][2] && clientIslandTile.getTower().equals(ClientTowerColor.EMPTY))
                        this.removeTheObject(GUIConstants.TOWER_NAME, clientIslandTile, children);
                    else
                        this.removeCoordinateOfObjectWithoutChangingCoordinateOfNode(GUIConstants.TOWER_NAME, children, imageOfTower, 2);
                }

                // Case 3: the composition of the island has not changed when it comes to towers, mother nature pawn and no entry tiles.
                // Leave the students where they are.
                else {
                    this.removeCoordinateOfObjectWithoutChangingCoordinateOfNode(GUIConstants.MOTHER_NATURE_NAME, children, null, 0);
                    this.removeCoordinateOfObjectWithoutChangingCoordinateOfNode(GUIConstants.NO_ENTRY_TILE_NAME, children, null, 1);
                    this.removeCoordinateOfObjectWithoutChangingCoordinateOfNode(GUIConstants.TOWER_NAME, children, imageOfTower, 2);
                }

                // Update previous state
                this.previousStateOfIslandTiles[clientIslandTile.getId()][0] = motherNatureIsland == clientIslandTile.getId();
                this.previousStateOfIslandTiles[clientIslandTile.getId()][1] = clientIslandTile.hasNoEntry();
                this.previousStateOfIslandTiles[clientIslandTile.getId()][2] = !clientIslandTile.getTower().equals(ClientTowerColor.EMPTY);

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
                        // If changeStudentPosition is true get new coordinates for student disc
                        if (changeStudentPosition) {
                            coordinate = this.coordinatesOfIslandTile.remove(random.nextInt(this.coordinatesOfIslandTile.size()));
                            student.setLayoutX(coordinate[1]);
                            student.setLayoutY(coordinate[0]);
                        }
                        // If changeStudentPosition is false do not move student disc and remove its coordinates from the list of available
                        // coordinates
                        else
                            // Remove coordinate of node from the list of coordinates
                            this.coordinatesOfIslandTile.remove(this.coordinatesOfIslandTile
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

    private void handleObjectsHaveBeenAdded(ClientIslandTile islandTile, boolean currentStateOfIsland, boolean prevStateOfIsland, ArrayList<Node> children,
                                            String gameObjectName, Image image, int width, int height, int type, ClientTowerColor color) {

        // IMPORTANT: type is 0 for mother nature, 1 for a no entry tile and 2 for a tower

        // Case 1: no change in the state for the current object
        if (currentStateOfIsland && prevStateOfIsland) {
            // Sample new coordinates for the node
            Node node = children.stream().filter(n -> n.getId().contains(gameObjectName)).toList().get(0);
            Integer[] coordinate = this.coordinatesOfIslandTile.remove(new Random().nextInt(this.coordinatesOfIslandTile.size()));
            node.setLayoutX(coordinate[1]);
            node.setLayoutY(coordinate[0]);
            if (type == 2) {
                // The color of the tower could have changed
                ImageView towerImageView = (ImageView) node;
                towerImageView.setImage(image);
            }
            // Remove node from list of children
            children.remove(node);
        }

        // Case 2: the object has been added
        else if (!prevStateOfIsland && currentStateOfIsland) {
            Integer[] coordinate = this.coordinatesOfIslandTile.remove(new Random().nextInt(this.coordinatesOfIslandTile.size()));
            ImageView imageView = new ImageView(image);
            imageView.setId(gameObjectName + islandTile.getId());
            imageView.setPreserveRatio(false);
            imageView.setFitWidth(width);
            imageView.setFitHeight(height);
            imageView.setLayoutX(coordinate[1]);
            imageView.setLayoutY(coordinate[0]);
            if (type == 0)
                imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onSelectionOfMotherNature);

            this.islandTiles[islandTile.getId()].getChildren().add(imageView);
        }

        // Case 3: the object has been removed
        else if (prevStateOfIsland) {
            this.removeTheObject(gameObjectName, islandTile, children);
        }
    }

    private void removeTheObject(String gameObjectName, ClientIslandTile islandTile, ArrayList<Node> children) {
        // Remove the object
        Node node = children.stream().filter(n -> n.getId().contains(gameObjectName)).toList().get(0);
        this.islandTiles[islandTile.getId()].getChildren().remove(node);
        children.remove(node);
    }

    private void removeCoordinateOfObjectWithoutChangingCoordinateOfNode(String gameObjectName, ArrayList<Node> children, Image image, int type) {

        // IMPORTANT: type is 0 for mother nature, 1 for a no entry tile and 2 for a tower

        if (children.stream().anyMatch(n -> n.getId().contains(gameObjectName))) {
            Node node = children.stream().filter(n -> n.getId().contains(gameObjectName)).toList().get(0);
            this.coordinatesOfIslandTile.remove(this.coordinatesOfIslandTile.stream()
                    .filter(coordinate -> node.getLayoutY() == coordinate[0] && node.getLayoutX() == coordinate[1]).toList().get(0));
            if (type == 2) {
                // The color of the tower could have changed
                ImageView towerImageView = (ImageView) node;
                towerImageView.setImage(image);
            }
            // Remove node from list of children
            children.remove(node);
        }
    }

    private void moveIslandTiles() {
        // This method is based on the assumption that two islands cannot be separated once they are unified. This is true
        // according to the current set of rules.

        // Find islands that must be moved
        // Case 1: the method is called for the first time
        if (this.previousCompositionOfIslandGroups.size() == 0) {
            for (ArrayList<ClientIslandTile> islandGroup : StageController.getStageController().getClientTable().getIslandTiles())
                // Unify islands if the size of the group is bigger than 1
                if (islandGroup.size() > 1) {
                    int indexOfCentralTile = Math.floorDiv(islandGroup.size(), 2);
                    for (int i = indexOfCentralTile; i < islandGroup.size() - 1; i++)
                        this.unifyIslandTiles(this.islandTiles[islandGroup.get(i).getId()],
                                this.islandTiles[islandGroup.get(i + 1).getId()]);
                    for (int i = indexOfCentralTile; i > 0; i--)
                        this.unifyIslandTiles(this.islandTiles[islandGroup.get(i).getId()],
                                this.islandTiles[islandGroup.get(i - 1).getId()]);
                }
        }
        // Case 2: Check if the composition of the island groups has changed. In this case move the islands
        else {
            // If the size of the new list of island group is smaller that the size of the previous list of island groups a change
            // has occurred
            if (StageController.getStageController().getClientTable().getIslandTiles().size() < this.previousCompositionOfIslandGroups.size()) {
                // Find group that has changed
                int index = 0;
                for (ArrayList<ClientIslandTile> islandGroup : StageController.getStageController().getClientTable().getIslandTiles()) {
                    int finalIndex = index;
                    if (islandGroup.size() > this.previousCompositionOfIslandGroups.get(index).size()) {
                        while (finalIndex < StageController.getStageController().getClientTable().getIslandTiles().size() + 1) {
                            int finalIndex1 = finalIndex;
                            if(!islandGroup.stream().anyMatch(islandTile -> previousCompositionOfIslandGroups.get(finalIndex1).contains(islandTile.getId())))
                                finalIndex++;
                            else
                                break;
                        }
                        if(finalIndex < StageController.getStageController().getClientTable().getIslandTiles().size() + 1) {
                            // Unify islands in islandGroup
                            int indexOfCentralTile = Math.floorDiv(islandGroup.size(), 2);
                            for (int i = indexOfCentralTile; i < islandGroup.size() - 1; i++)
                                this.unifyIslandTiles(this.islandTiles[islandGroup.get(i).getId()],
                                        this.islandTiles[islandGroup.get(i + 1).getId()]);
                            for (int i = indexOfCentralTile; i > 0; i--)
                                this.unifyIslandTiles(this.islandTiles[islandGroup.get(i).getId()],
                                        this.islandTiles[islandGroup.get(i - 1).getId()]);
                        }
                    } else
                        index++;
                }
            }
        }
        // Fill previous composition of groups
        this.previousCompositionOfIslandGroups.clear();
        for (ArrayList<ClientIslandTile> islandGroup : StageController.getStageController().getClientTable().getIslandTiles())
            this.previousCompositionOfIslandGroups.add(islandGroup.stream()
                    .map(ClientIslandTile::getId)
                    .collect(Collectors.toCollection(ArrayList::new)));
    }

    private void unifyIslandTiles(Pane fixedIsland, Pane islandToMove) {

        // Layout x and y of island tile to move
        double x = islandToMove.getLayoutX();
        double y = islandToMove.getLayoutY();
        // Useful values
        double alfa;
        double beta;
        double gamma;
        double delta;

        if (fixedIsland.getLayoutX() != islandToMove.getLayoutX() && fixedIsland.getLayoutY() != islandToMove.getLayoutY()) {
            double m = (-fixedIsland.getLayoutY() + islandToMove.getLayoutY()) / (fixedIsland.getLayoutX() - islandToMove.getLayoutX());
            alfa = Math.pow(GUIConstants.HALF_DIM_ISLAND_TILE_PANE + fixedIsland.getLayoutX(), 2)
                    + Math.pow(-islandToMove.getLayoutY() + fixedIsland.getLayoutY() - m * islandToMove.getLayoutX() - GUIConstants.HALF_DIM_ISLAND_TILE_PANE * m, 2)
                    - Math.pow(2 * GUIConstants.RADIUS_OF_ISLAND_TILE, 2);
            beta = fixedIsland.getLayoutX() + GUIConstants.HALF_DIM_ISLAND_TILE_PANE + m * (islandToMove.getLayoutY() - fixedIsland.getLayoutY()
                    + m * islandToMove.getLayoutX() + GUIConstants.HALF_DIM_ISLAND_TILE_PANE * m);
            gamma = 1 + Math.pow(m, 2);

            // Calculate new layout x
            delta = Math.pow(beta, 2) - alfa * gamma;
            double x1 = Math.round((beta + Math.sqrt(delta)) / gamma);
            double x2 =  Math.round((beta - Math.sqrt(delta)) / gamma);
            if(fixedIsland.getLayoutX() + GUIConstants.HALF_DIM_ISLAND_TILE_PANE < islandToMove.getLayoutX() + GUIConstants.HALF_DIM_ISLAND_TILE_PANE)
                x = (x1 >= fixedIsland.getLayoutX() + GUIConstants.HALF_DIM_ISLAND_TILE_PANE && x1 <= islandToMove.getLayoutX() + GUIConstants.HALF_DIM_ISLAND_TILE_PANE)?x1:x2;
            else
                x = (x1 >= islandToMove.getLayoutX() + GUIConstants.HALF_DIM_ISLAND_TILE_PANE && x1 <= fixedIsland.getLayoutX() + GUIConstants.HALF_DIM_ISLAND_TILE_PANE)?x1:x2;
            // x = Math.abs(islandToMove.getLayoutX() - x1) < Math.abs(islandToMove.getLayoutX() - x2) ? x1 : x2;
            // Calculate new layout y
            y = -(-islandToMove.getLayoutY() - GUIConstants.HALF_DIM_ISLAND_TILE_PANE + m * (x - islandToMove.getLayoutX() - GUIConstants.HALF_DIM_ISLAND_TILE_PANE));
        } else if (fixedIsland.getLayoutX() == islandToMove.getLayoutX()) {
            alfa = Math.pow(GUIConstants.HALF_DIM_ISLAND_TILE_PANE + fixedIsland.getLayoutY(), 2)
                    - Math.pow(2 * GUIConstants.RADIUS_OF_ISLAND_TILE, 2);
            beta = -(GUIConstants.HALF_DIM_ISLAND_TILE_PANE + fixedIsland.getLayoutY());
            gamma = 1;
            delta = Math.pow(beta, 2) - alfa * gamma;
            double y1 = -Math.round(((beta + Math.sqrt(delta)) / gamma));
            double y2 = -Math.round(((beta - Math.sqrt(delta)) / gamma));
            y = Math.abs(islandToMove.getLayoutY() - y1) < Math.abs(islandToMove.getLayoutY() - y2) ? y1 : y2;
            x = fixedIsland.getLayoutX() + GUIConstants.HALF_DIM_ISLAND_TILE_PANE;
        } else if (fixedIsland.getLayoutY() == islandToMove.getLayoutY()) {
            alfa = Math.pow(GUIConstants.HALF_DIM_ISLAND_TILE_PANE + fixedIsland.getLayoutX(), 2)
                    - Math.pow(2 * GUIConstants.RADIUS_OF_ISLAND_TILE, 2);
            beta = (GUIConstants.HALF_DIM_ISLAND_TILE_PANE + fixedIsland.getLayoutX());
            gamma = 1;
            delta = Math.pow(beta, 2) - alfa * gamma;
            double x1 = Math.round((beta + Math.sqrt(delta)) / gamma);
            double x2 = Math.round((beta - Math.sqrt(delta)) / gamma);
            x = Math.abs(islandToMove.getLayoutX() - x1) < Math.abs(islandToMove.getLayoutX() - x2) ? x1 : x2;
            y = fixedIsland.getLayoutY() + GUIConstants.HALF_DIM_ISLAND_TILE_PANE;
        }

        islandToMove.setLayoutX(x - GUIConstants.HALF_DIM_ISLAND_TILE_PANE);
        islandToMove.setLayoutY(y - GUIConstants.HALF_DIM_ISLAND_TILE_PANE);
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

            Integer[] coordinate;
            Random random = new Random();

            // If the character card is the mushroom hunter or the thief add the 5 colors to the card
            if ((clientCharacterCard.getId() == Character.MUSHROOM_HUNTER.getId() || clientCharacterCard.getId() == Character.THIEF.getId())) {
                if (this.firstFillOfCharacterCards)
                    for (PawnColor color : PawnColor.values()) {
                        Circle circle = new Circle(GUIConstants.CIRCLE_RADIUS, Paint.valueOf(color.toString()));
                        circle.setId(color.toString());
                        coordinate = this.coordinatesOfStudentsInCharacterCards.remove(random.nextInt(this.coordinatesOfStudentsInCharacterCards.size()));
                        circle.setLayoutX(coordinate[1]);
                        circle.setLayoutY(coordinate[0]);
                        circle.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onSelectionOfColorOfStudentInCharacterCard);
                        this.characterCards[indexOfCharacterCard].getChildren().add(circle);
                    }
            } else {
                // Get all the children of the pane containing the character card which are not the character card and put them
                // in a list
                ArrayList<Node> children = new ArrayList<>();
                for (Node node : this.characterCards[indexOfCharacterCard].getChildren())
                    if (node.getId().contains(GUIConstants.STUDENT_DISC_NAME))
                        children.add(node);

                // Add student discs
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
                        student.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onSelectionOfStudentInCharacterCard);
                        this.characterCards[indexOfCharacterCard].getChildren().add(student);
                    }
                }

                // Remove remaining children from character card storage
                while (children.size() != 0) {
                    this.characterCards[indexOfCharacterCard].getChildren().remove(children.get(0));
                    children.remove(0);
                }
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

        // Set false when first fill is completed
        this.firstFillOfCharacterCards = false;
    }

    private void addAssistantCardsPlayedByOtherPlayers() {
        // Get array of client players (without the client):
        ClientPlayer[] players = StageController.getStageController().getClientTeams().getTeams()
                .stream()
                .flatMap(clientTeam -> clientTeam.getPlayers().stream())
                .filter(clientPlayer -> clientPlayer.getPlayerId() != StageController
                        .getStageController().getGuiView().getPlayerId())
                .toArray(ClientPlayer[]::new);

        // Add last played assistant card of each player (except the client) to the table scene
        // Note that when the array of assistant cards changes the array of labels changes accordingly, i.e. the two
        // arrays evolve in the same way
        int indexOfPlayer = 0;
        // Create array if needed
        if (this.assistantCards == null) {
            this.assistantCards = new ImageView[players.length];
            this.labelsForAssistantCards = new Label[players.length];
        }
        for (ClientPlayer player : players) {
            // Case 1: the assistant card of the player is not null
            if (player.getLastPlayedAssistantCard() != null) {
                // Case 1.1: No image view in array
                if (this.assistantCards[indexOfPlayer] == null) {
                    // Create image view
                    ImageView assistant = new ImageView(Images.getImages().getAssistantCards()[player.getWizard() == 1
                            ? player.getLastPlayedAssistantCard().getId()
                            : player.getLastPlayedAssistantCard().getId() - (player.getWizard() - 1) * ModelConstants.MAX_VALUE_OF_ASSISTANT_CARD]);
                    assistant.setId(GUIConstants.ASSISTANT_CARD_NAME_TABLE_SCENE + player.getLastPlayedAssistantCard().getId());
                    assistant.setPreserveRatio(false);
                    assistant.setFitWidth(GUIConstants.WIDTH_OF_ASSISTANT_CARD_TABLE_SCENE);
                    assistant.setFitHeight(GUIConstants.HEIGHT_OF_ASSISTANT_CARD_TABLE_SCENE);
                    assistant.setLayoutX(GUIConstants.LAYOUT_X_OF_ASSISTANT_CARD_AND_LABEL_IN_PANE_TABLE_SCENE);
                    assistant.setLayoutY(GUIConstants.LAYOUT_Y_OF_ASSISTANT_CARD_IN_PANE_TABLE_SCENE);
                    Blend blend = new Blend(BlendMode.MULTIPLY);
                    blend.setOpacity(0.8);
                    assistant.setEffect(blend);
                    this.assistantCards[indexOfPlayer] = assistant;

                    // Create label
                    Label labelForUsername = new Label(player.getUsername());
                    labelForUsername.setId(GUIConstants.LABEL_ID_FOR_ASSISTANT_CARDS + player.getLastPlayedAssistantCard().getId());
                    labelForUsername.setPrefSize(GUIConstants.WIDTH_OF_ASSISTANT_CARD_TABLE_SCENE, GUIConstants.HEIGHT_OF_LABEL_FOR_ASSISTANT_CARDS);
                    labelForUsername.setLayoutX(GUIConstants.LAYOUT_X_OF_ASSISTANT_CARD_AND_LABEL_IN_PANE_TABLE_SCENE);
                    labelForUsername.setLayoutY(GUIConstants.LAYOUT_Y_OF_LABEL_IN_PANE_TABLE_SCENE);
                    labelForUsername.setAlignment(Pos.CENTER_LEFT);
                    labelForUsername.setFont(Font.font(GUIConstants.FONT, FontPosture.REGULAR, GUIConstants.FONT_SIZE_LABEL_ASSISTANT_CARDS));
                    this.labelsForAssistantCards[indexOfPlayer] = labelForUsername;

                    // Add image view and label to first free pane
                    for (Pane pane : this.panesOfAssistantCards)
                        if (pane != null && pane.getChildren().size() == 0) {
                            pane.getChildren().add(assistant);
                            pane.getChildren().add(labelForUsername);
                            break;
                        }
                }
                // Case 1.2: Modify assistant card image view
                else
                    this.assistantCards[indexOfPlayer].setImage(Images.getImages().getAssistantCards()[player.getWizard() == 1
                            ? player.getLastPlayedAssistantCard().getId()
                            : player.getLastPlayedAssistantCard().getId() - (player.getWizard() - 1) * ModelConstants.MAX_VALUE_OF_ASSISTANT_CARD]);

            }
            // Case 2: No last played assistant card for the current player
            else {
                // Case 2.1: If an assistant card is on the table remove it
                if (this.assistantCards[indexOfPlayer] != null) {
                    // Remove assistant card and label from the scene
                    for (Pane pane : this.panesOfAssistantCards)
                        if (pane != null && pane.getChildren().contains(this.assistantCards[indexOfPlayer])) {
                            pane.getChildren().remove(this.assistantCards[indexOfPlayer]);
                            pane.getChildren().remove(this.labelsForAssistantCards[indexOfPlayer]);
                            break;
                        }

                    // Remove assistant card and label from arrays
                    this.assistantCards[indexOfPlayer] = null;
                    this.labelsForAssistantCards[indexOfPlayer] = null;
                }
            }
            indexOfPlayer++;
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
            cloudTileImageView.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onSelectionOfCloudTile);
            cloudTileImageView.setId(GUIConstants.CLOUD_TILE_NAME
                    + StageController.getStageController().getClientTable().getCloudTiles().get(indexOfCloudTile).getId());
            cloudTileImageView.setPreserveRatio(false);
            cloudTileImageView.setFitWidth(GUIConstants.WIDTH_OF_CLOUD_TILE);
            cloudTileImageView.setFitHeight(GUIConstants.HEIGHT_OF_CLOUD_TILE);
            cloudTileImageView.setLayoutX(0);
            cloudTileImageView.setLayoutY(0);
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
            characterCardImageView.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onSelectionOfCharacterCard);
            characterCardImageView.setId(GUIConstants.CHARACTER_CARD_IMAGE_NAME
                    + StageController.getStageController().getClientTable().getActiveCharacterCards().get(indexCharacterCard).getId());
            characterCardImageView.setPreserveRatio(false);
            characterCardImageView.setFitWidth(GUIConstants.WIDTH_OF_CHARACTER_CARD);
            characterCardImageView.setFitHeight(GUIConstants.HEIGHT_OF_CHARACTER_CARD);
            characterCardImageView.setLayoutX(0);
            characterCardImageView.setLayoutY(0);
            characterCardPane.getChildren().add(characterCardImageView);
            // Add pane to root and to array of panes of character cards
            root.getChildren().add(characterCardPane);
            this.characterCards[indexCharacterCard] = characterCardPane;
            indexCharacterCard++;
        }
    }

    private void addAssistantCards(AnchorPane root) {
        // Get number of players that are not the client
        int playerId = StageController.getStageController().getGuiView().getPlayerId();
        int numberOfPlayers = (int) StageController.getStageController().getClientTeams().getTeams()
                .stream()
                .flatMap(clientTeam -> clientTeam.getPlayers().stream())
                .filter(clientPlayer -> clientPlayer.getPlayerId() != playerId)
                .count();

        for (int i = 0; i < numberOfPlayers; i++) {
            Pane pane = new Pane();
            pane.setId(GUIConstants.ASSISTANT_CARD_PANE_NAME_TABLE_SCENE + i);
            pane.setPrefSize(GUIConstants.WIDTH_OF_ASSISTANT_CARD_PANE_TABLE_SCENE,
                    GUIConstants.HEIGHT_OF_ASSISTANT_CARD_PANE_TABLE_SCENE);
            pane.setLayoutX(GUIConstants.LAYOUT_X_OF_FIRST_ASSISTANT_CARD_PANE_TABLE_SCENE +
                    i * GUIConstants.LAYOUT_X_OFFSET_BETWEEN_ASSISTANT_CARD_PANES_TABLE_SCENE);
            pane.setLayoutY(GUIConstants.LAYOUT_Y_OF_ASSISTANT_CARD_PANES_TABLE_SCENE);
            this.panesOfAssistantCards[i] = pane;
            root.getChildren().add(pane);
        }
    }

    private void addBulb(AnchorPane root) {
        ImageView bulb = new ImageView(Images.getImages().getBulb());
        bulb.setId(GUIConstants.BULB_NAME);
        bulb.setPreserveRatio(false);
        bulb.setFitWidth(GUIConstants.WIDTH_OF_BULB);
        bulb.setFitHeight(GUIConstants.HEIGHT_OF_BULB);
        bulb.setLayoutX(GUIConstants.LAYOUT_X_OF_BULB);
        bulb.setLayoutY(GUIConstants.LAYOUT_Y_OF_BULB);
        bulb.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onSelectionOfBulb);
        root.getChildren().add(bulb);
    }

    // EVENT HANDLERS

    @FXML
    private void switchToOtherSchoolBoards(ActionEvent event) {
        try {
            StageController.getStageController().showScene(SceneType.SCHOOL_BOARD_SCENE, false);
        } catch (Exception exception) {

        }
    }

    @FXML
    public void switchToDeck(ActionEvent event) {
        try {
            StageController.getStageController().showScene(SceneType.DECK_SCENE, false);
        } catch (Exception exception) {

        }
    }

    public void onSelectionOfIslandTile(MouseEvent event) {
        // Handle event
        if (StageController.getStageController().getGuiView().getAvailableCommands().size() == 1)
            if (StageController.getStageController().getGuiView().getAvailableCommands().values().stream().toList().get(0).getValidation().equals(CommandDataEntryValidationSet.ISLAND) ||
                    StageController.getStageController().getGuiView().getAvailableCommands().values().stream().toList().get(0).getValidation().equals(CommandDataEntryValidationSet.ISLAND_OR_DINING_ROOM))
                handleEventWithCommand(StageController.getStageController().getGuiView().getAvailableCommands().values().stream().toList().get(0).getValidation(),
                        event.getPickResult().getIntersectedNode().getId().replace(GUIConstants.ISLAND_TILE_NAME, ""), false);


    }

    public void onSelectionOfDiningRoom(MouseEvent event) {
        handleEventWithCommand(CommandDataEntryValidationSet.ISLAND_OR_DINING_ROOM,
                "d", false);
    }

    public void onSelectionOfMotherNature(MouseEvent event) {
        // Handle event
        handleEventWithCommand(CommandDataEntryValidationSet.ISLAND, null, true);
    }

    public void onSelectionOfCloudTile(MouseEvent event) {
        handleEventWithCommand(CommandDataEntryValidationSet.CLOUD_TILE,
                event.getPickResult().getIntersectedNode().getId().replace(GUIConstants.CLOUD_TILE_NAME, ""), false);
    }

    public void onSelectionOfCharacterCard(MouseEvent event) {
        handleEventWithCommand(CommandDataEntryValidationSet.CHARACTER_CARD,
                event.getPickResult().getIntersectedNode().getId().replace(GUIConstants.CHARACTER_CARD_IMAGE_NAME, ""), false);
    }

    public void onSelectionOfColorOfStudentInCharacterCard(MouseEvent event) {
        handleEventWithCommand(CommandDataEntryValidationSet.COLOR, event.getPickResult().getIntersectedNode().getId(), false);
    }

    public void onSelectionOfStudentInCharacterCard(MouseEvent event) {
        // Get color of student
        try {
            String color = ClientPawnColor.values()[ViewUtilityFunctions.convertStudentIdToIdOfColor(
                    Integer.parseInt(event.getPickResult().getIntersectedNode().getId().replace(GUIConstants.STUDENT_DISC_NAME, "")))].toString();
            handleEventWithCommand(CommandDataEntryValidationSet.STUDENT_CHARACTER_CARD, color, false);
        } catch (IllegalStudentIdException e) {
            StageController.getStageController().getGuiView().getClientServerConnection().askToCloseConnectionWithError(e.getMessage());
        }
    }


    private void onSelectionOfBulb(MouseEvent event) {
        Dialog dialog = new Dialog();
        //Set the title
        dialog.setTitle("Possible actions");
        //Set the content of the dialog
        dialog.setContentText(StageController.getStageController().getGuiView().getAvailableActionsHint());
        //Add button to the dialog pane
        dialog.getDialogPane().getButtonTypes().add(new ButtonType("OK", ButtonBar.ButtonData.LEFT));
        dialog.getDialogPane().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/hint.css")).toExternalForm());
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        // Add a custom icon.
        stage.getIcons().add(Images.getImages().getBulb());
        // Make dialog not closable using the x
        stage.setOnCloseRequest(Event::consume);
        dialog.show();
    }

    // STATIC METHODS

    public static void handleEventWithCommand(String commandDataEntryValidationSet, String id, boolean motherNature) {

        if (StageController.getStageController().getGuiView().getAvailableCommands().values().stream()
                .filter(command -> command.getValidation().equals(commandDataEntryValidationSet)).count() == 1) {
            Command command;
            if (StageController.getStageController().getGuiView().getAvailableCommands().size() > 1) {
                command = StageController.getStageController().getGuiView().getAvailableCommands().values().stream()
                        .filter(cmd -> cmd.getValidation().equals(commandDataEntryValidationSet)).toList().get(0);
                StageController.getStageController().getGuiView().getAvailableCommands().remove(command.getActionMessage().getActionId());
                StageController.getStageController().getGuiView().getAvailableCommands().clear();
                StageController.getStageController().getGuiView().getAvailableCommands().put(command.getActionMessage().getActionId(), command);
            } else
                command = StageController.getStageController().getGuiView().getAvailableCommands().values().stream().toList().get(0);

            if (!motherNature)
                command.parseCLIString(id);

            if (command.canEnd()) {
                if (command.hasQuestion()) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to continue?", ButtonType.YES, ButtonType.NO);
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get().equals(ButtonType.NO)) {
                        // Send message
                        StageController.getStageController().getGuiView().getClientServerConnection().sendMessage(
                                Serializer.fromActionMessageToMessage(command.getActionMessage()));
                        StageController.getStageController().getGuiView().getAvailableCommands().clear();
                    }
                } else {
                    // Send message
                    StageController.getStageController().getGuiView().getClientServerConnection().sendMessage(
                            Serializer.fromActionMessageToMessage(command.getActionMessage()));
                    StageController.getStageController().getGuiView().getAvailableCommands().clear();
                }
            }
        }
    }
}

class StudentInEntranceEventHandler implements EventHandler<MouseEvent> {

    @Override
    public void handle(MouseEvent event) {
        // Handle event
        try {
            // Get color of student
            String color = ClientPawnColor.values()[ViewUtilityFunctions.convertStudentIdToIdOfColor(
                    Integer.parseInt(event.getPickResult().getIntersectedNode().getId().replace(GUIConstants.STUDENT_DISC_NAME, "")))].toString();
            TableSceneController.handleEventWithCommand(CommandDataEntryValidationSet.STUDENT_ENTRANCE, color, false);
        } catch (IllegalStudentIdException e) {
            StageController.getStageController().getGuiView().getClientServerConnection().askToCloseConnectionWithError(e.getMessage());
        }
    }
}

class StudentInDiningEventHandler implements EventHandler<MouseEvent> {

    @Override
    public void handle(MouseEvent event) {
        try {
            // Get color of student
            String color = ClientPawnColor.values()[ViewUtilityFunctions.convertStudentIdToIdOfColor(
                    Integer.parseInt(event.getPickResult().getIntersectedNode().getId().replace(GUIConstants.STUDENT_DISC_NAME, "")))].toString();
            TableSceneController.handleEventWithCommand(CommandDataEntryValidationSet.STUDENT_DINING_ROOM, color, false);
        } catch (IllegalStudentIdException e) {
            StageController.getStageController().getGuiView().getClientServerConnection().askToCloseConnectionWithError(e.getMessage());
        }
    }
}