package it.polimi.ingsw.view.gui.scene_controllers;

import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.view.ViewUtilityFunctions;
import it.polimi.ingsw.view.exceptions.IllegalLaneException;
import it.polimi.ingsw.view.exceptions.IllegalStudentIdException;
import it.polimi.ingsw.view.game_objects.ClientCharacterCard;
import it.polimi.ingsw.view.game_objects.ClientCloudTile;
import it.polimi.ingsw.view.game_objects.ClientIslandTile;
import it.polimi.ingsw.view.game_objects.ClientPawnColor;
import it.polimi.ingsw.view.gui.GUIConstants;
import it.polimi.ingsw.view.gui.SceneType;
import it.polimi.ingsw.view.gui.StageController;
import it.polimi.ingsw.view.gui.exceptions.GuiViewNotSet;
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
    private final ArrayList<ArrayList<Integer>> previousDiningRoom;
    private final ArrayList<ClientPawnColor> previousProfessorSection;
    // Array of panes containing cloud tiles
    private Pane[] cloudTiles;
    // Previous state of school board
    private Integer[] previousEntrance;
    private int previousNumberOfTowers;
    private int[] previousPricesOfCharacterCards;

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
        }
        catch (IOException | GuiViewNotSet e) {
            e.printStackTrace();
            // TODO do something
            return null;
        }
    }

    public void updateScene() {

        try {
            // Fill islands
            this.addStudentsMotherNatureAndNoEntryToIslandTiles();
            // Move islands to create island groups
            // TODO method for island movements

            // Update school board
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

            // Update cloud tiles
            this.fillCloudTiles();

            // Update character cards
            if (StageController.getStageController().getGuiView().getUser().getPreference() > 0)
                this.fillCharacterCards();
        }
        catch (IllegalStudentIdException | IllegalLaneException | GuiViewNotSet e) {
            e.printStackTrace();
            // TODO do something
        }
    }

    // METHODS FOR SCENE UPDATE

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
                    // Add the node to the list of children of the characterCardsPane
                    this.cloudTiles[indexOfCloud].getChildren().add(student);
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
                    // Add the node to the list of children of the characterCardsPane
                    this.characterCards[indexOfCharacterCard].getChildren().add(student);
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