package it.polimi.ingsw.view.gui.scene_controllers;

import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.view.ViewUtilityFunctions;
import it.polimi.ingsw.view.exceptions.IllegalLaneException;
import it.polimi.ingsw.view.exceptions.IllegalStudentIdException;
import it.polimi.ingsw.view.game_objects.ClientPawnColor;
import it.polimi.ingsw.view.game_objects.ClientPlayer;
import it.polimi.ingsw.view.gui.GUIConstants;
import it.polimi.ingsw.view.gui.StageController;
import it.polimi.ingsw.view.gui.exceptions.GuiViewNotSet;
import it.polimi.ingsw.view.gui.exceptions.SceneLayoutException;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;

import java.util.ArrayList;
import java.util.Arrays;

public class SchoolBoardsSceneController extends SceneController{

    private Pane[] panesForSchoolBoards;
    private Integer[][][] coordinatesOfStudentsInEntrances;
    private Integer[][][] firstAvailableCoordinatesOfDiningRooms;
    private Integer[][] coordinatesOfProfessorPawns;
    private Integer[][] coordinatesOfTowers;

    private Integer[][] previousEntrances;
    private ArrayList<ArrayList<Integer>>[] previousDiningRooms;
    private ArrayList<ClientPawnColor>[] previousProfessorSections;
    private Integer[] previousNumberOfTowers;

    private Image schoolBoard;

    private void generateArraysOfCoordinates() {
        int numberOfSchoolBoards = StageController.getStageController().getClientTable().getSchoolBoards().size() - 1;
        this.coordinatesOfStudentsInEntrances = new Integer[numberOfSchoolBoards][ GUIConstants.COLUMNS_ENTRANCE * GUIConstants.CELLS_FIRST_ROW_ENTRANCE - 1][3];
        this.firstAvailableCoordinatesOfDiningRooms = new Integer[numberOfSchoolBoards][GUIConstants.LANES][2];
        this.coordinatesOfProfessorPawns = new Integer[GUIConstants.LANES][2];
        this.coordinatesOfTowers = new Integer[ModelConstants.MAX_NUMBER_OF_TOWERS][2];
    }

    private void generateArrayForPreviousStateOfSchoolBoards() {
        int numberOfSchoolBoards = StageController.getStageController().getClientTable().getSchoolBoards().size() - 1;
        this.previousEntrances = new Integer[numberOfSchoolBoards][0];
        this.previousDiningRooms = new ArrayList[numberOfSchoolBoards];
        for (int i = 0; i < numberOfSchoolBoards; i++) {
            this.previousDiningRooms[i] = new ArrayList<>();
            for (int j = 0; j < GUIConstants.LANES; j++)
                this.previousDiningRooms[i].add(new ArrayList<>());
        }
        this.previousProfessorSections = new ArrayList[numberOfSchoolBoards];
        for (int i = 0; i < numberOfSchoolBoards; i++)
            this.previousProfessorSections[i] = new ArrayList<>();
        this.previousNumberOfTowers = new Integer[numberOfSchoolBoards];
        Arrays.fill(this.previousNumberOfTowers, 0);
    }

    @Override
    protected Scene layout() {
        // Call for scene creation, that call updateScene to update the content of the table
        // The scene cannot be created if clientTable and clientTeams have not been set.
        try {
            if (StageController.getStageController().getClientTable() == null)
                throw new SceneLayoutException("The scene cannot be created before client table has been set");

            AnchorPane root = new AnchorPane();

            // Set background
            root.getStyleClass().add("background");
            root.getStylesheets().add("/CSS/table_background.css");

            root.setPrefSize(GUIConstants.WIDTH_OF_SCENE, GUIConstants.HEIGHT_OF_SCENE);
            // Generate arrays of coordinates
            this.generateArraysOfCoordinates();
            // Generate arrays for previous states of school boards

            // Create array of panes of school boards
            this.panesForSchoolBoards = new Pane[StageController.getStageController().getClientTable().getSchoolBoards().size() - 1];

            // Generate arrays for previous state
            this.generateArrayForPreviousStateOfSchoolBoards();

            // Generate coordinates and panes for school boards
            for (int i = 0; i < StageController.getStageController().getClientTable().getSchoolBoards().size() - 1; i++) {
                // Generate coordinates
                SchoolBoardsFunction.generateCoordinates(this.coordinatesOfStudentsInEntrances[i], this.firstAvailableCoordinatesOfDiningRooms[i],
                        this.coordinatesOfProfessorPawns, this.coordinatesOfTowers);

                // Add new pane to root and add image view of school board to pane. Save pane for school board in array of panes
                this.panesForSchoolBoards[i] = this.addPaneOfSchoolBoardAndImageOfSchoolBoardToRoot(root, GUIConstants.LAYOUT_X_OF_FIRST_SCHOOL_BOARD
                        + i * GUIConstants.LAYOUT_X_OFFSET_BETWEEN_SCHOOL_BOARDS, GUIConstants.LAYOUT_Y_OF_SCHOOL_BOARDS);
            }

            // Create association between players and school boards
            // Note that player and school boards have the same order in client teams and client table, thus given the
            // array of school boards and the array of players, obtained as clientTeams.getTeams()
            // .stream().flatMap(clientTeam -> clientTeam.getPlayers().stream()).toArray(ClientPlayer[]::new), the elements
            // at position i of the arrays are associated with the same client.
            // The players' usernames are written near the school boards.
            // Step 1: Get array of client players (without the client):
            ClientPlayer[] players = StageController.getStageController().getClientTeams().getTeams()
                    .stream()
                    .flatMap(clientTeam -> clientTeam.getPlayers().stream())
                    .filter(clientPlayer -> {
                        try {
                            return clientPlayer.getPlayerId() != StageController.getStageController().getGuiView().getPlayerId();
                        } catch (GuiViewNotSet e) {
                            return clientPlayer.getPlayerId() != -1;
                        }
                    })
                    .toArray(ClientPlayer[]::new);

            if (players.length == 0)
                throw new GuiViewNotSet();

            // Step 2: Write usernames over school boards
            for (int i = 0; i < players.length; i++)
                this.writeLabelForUsername(root, players[i].getUsername(), GUIConstants.LAYOUT_X_OF_FIRST_SCHOOL_BOARD + i * GUIConstants.LAYOUT_X_OFFSET_BETWEEN_SCHOOL_BOARDS);

            // Update scene, i.e. add elements to school boards
            this.updateScene();

            // Return new scene
            return new Scene(root);
        } catch (SceneLayoutException | GuiViewNotSet e) {
            e.printStackTrace();
            // TODO DO SOMETHING WITH EXCEPTIONS
            return null;
        }
    }

    public void updateScene() throws GuiViewNotSet {
        try {
            // Get index of school board of client
            int indexOfSchoolBoardOfClient = ViewUtilityFunctions.getPlayerSchoolBoardIndex(StageController.getStageController().getGuiView().getPlayerId(),
                    StageController.getStageController().getClientTeams());

            // Get array of client players:
            ClientPlayer[] players = StageController.getStageController().getClientTeams().getTeams()
                    .stream()
                    .flatMap(clientTeam -> clientTeam.getPlayers().stream())
                    .toArray(ClientPlayer[]::new);

            int indexOfPaneWithSchoolBoard = 0;
            for (int i = 0; i < StageController.getStageController().getClientTable().getSchoolBoards().size(); i++) {
                if (i != indexOfSchoolBoardOfClient) {
                    // The variable i is the index of the school board

                    // Determine index of team from player id
                    int finalI = i;
                    int indexOfTeam = StageController.getStageController().getClientTeams().getTeams()
                            .indexOf(StageController.getStageController().getClientTeams().getTeams()
                                    .stream()
                                    .filter(clientTeam -> clientTeam.getPlayers().
                                            stream().
                                            filter(clientPlayer -> clientPlayer.getPlayerId() == players[finalI].getPlayerId()).count() == 1)
                                    .toList().get(0));

                    // Update entrance
                    this.previousEntrances[indexOfPaneWithSchoolBoard] = SchoolBoardsFunction.updateSchoolBoardEntrance(i, this.panesForSchoolBoards[indexOfPaneWithSchoolBoard],
                            this.coordinatesOfStudentsInEntrances[indexOfPaneWithSchoolBoard], this.previousEntrances[indexOfPaneWithSchoolBoard]);
                    // Update dining room
                    SchoolBoardsFunction.updateSchoolBoardDiningRoom(i, this.panesForSchoolBoards[indexOfPaneWithSchoolBoard],
                            this.firstAvailableCoordinatesOfDiningRooms[indexOfPaneWithSchoolBoard], this.previousDiningRooms[indexOfPaneWithSchoolBoard]);
                    // Update professor section
                    SchoolBoardsFunction.updateSchoolBoardProfessorSection(indexOfTeam, this.panesForSchoolBoards[indexOfPaneWithSchoolBoard],
                            this.coordinatesOfProfessorPawns, this.previousProfessorSections[indexOfPaneWithSchoolBoard]);
                    // Update tower section
                    this.previousNumberOfTowers[indexOfPaneWithSchoolBoard] = SchoolBoardsFunction.updateSchoolBoardTowersSection(indexOfTeam,
                            this.panesForSchoolBoards[indexOfPaneWithSchoolBoard], this.coordinatesOfTowers,
                            this.previousNumberOfTowers[indexOfPaneWithSchoolBoard]);

                    // Update indexOfPaneWithSchoolBoard
                    indexOfPaneWithSchoolBoard++;
                }
            }

        } catch (IllegalStudentIdException | IllegalLaneException e) {
            e.printStackTrace();
            // TODO do something
        }
    }

    private Pane addPaneOfSchoolBoardAndImageOfSchoolBoardToRoot(AnchorPane root, int layoutXOfPane, int layoutYOfPane) {
        // Create pane for school board
        Pane paneForSchoolBoard = new Pane();
        paneForSchoolBoard.setPrefSize(GUIConstants.WIDTH_OF_PANE_FOR_SCHOOL_BOARD, GUIConstants.HEIGHT_OF_PANE_FOR_SCHOOL_BOARD);
        paneForSchoolBoard.setLayoutX(layoutXOfPane);
        paneForSchoolBoard.setLayoutY(layoutYOfPane);
        // Create image of school board
        if (this.schoolBoard == null)
            this.schoolBoard = new Image(GUIConstants.SCENE_TABLE_SCHOOL_BOARD_PATH);
        // Create image view of school board
        ImageView imageViewOfSchoolBoard = new ImageView(this.schoolBoard);
        imageViewOfSchoolBoard.setFitWidth(GUIConstants.WIDTH_OF_IMAGE_VIEW_OF_SCHOOL_BOARD);
        imageViewOfSchoolBoard.setFitHeight(GUIConstants.HEIGHT_OF_IMAGE_VIEW_OF_SCHOOL_BOARD);
        imageViewOfSchoolBoard.setLayoutX(GUIConstants.LAYOUT_X_OF_SCHOOL_BOARD_IN_PANE_FOR_SCHOOL_BOARD);
        imageViewOfSchoolBoard.setLayoutY(GUIConstants.LAYOUT_Y_OF_SCHOOL_BOARD_IN_PANE_FOR_SCHOOL_BOARD);
        imageViewOfSchoolBoard.setRotate(GUIConstants.ROTATION_OF_SCHOOL_BOARD_IN_PANE_FOR_SCHOOL_BOARD);
        imageViewOfSchoolBoard.setEffect(new ColorAdjust(0, 0, -0.2, -0.15));
        // Add image view to children of Pane for school board
        paneForSchoolBoard.getChildren().add(imageViewOfSchoolBoard);
        // Add pane for school board to root
        root.getChildren().add(paneForSchoolBoard);
        return paneForSchoolBoard;
    }

    private void writeLabelForUsername(AnchorPane root, String username, int layoutX) {
        // Create label
        Label label = new Label(GUIConstants.LABEL_FOR_USERNAME_START + username);
        // Set properties
        label.setPrefSize(GUIConstants.WIDTH_OF_LABEL_FOR_USERNAMES, GUIConstants.HEIGHT_OF_LABEL_FOR_USERNAMES);
        label.setLayoutX(layoutX);
        label.setLayoutY(GUIConstants.LAYOUT_Y_OF_LABELS_FOR_USERNAMES);
        label.setAlignment(Pos.CENTER_LEFT);
        label.setFont(Font.font(GUIConstants.FONT, FontPosture.REGULAR, GUIConstants.FONT_SIZE));
        // Add label to root
        root.getChildren().add(label);
    }
}