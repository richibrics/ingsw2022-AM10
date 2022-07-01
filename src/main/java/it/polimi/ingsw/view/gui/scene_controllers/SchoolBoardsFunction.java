package it.polimi.ingsw.view.gui.scene_controllers;

import it.polimi.ingsw.model.ModelConstants;
import it.polimi.ingsw.view.ViewUtilityFunctions;
import it.polimi.ingsw.view.exceptions.IllegalLaneException;
import it.polimi.ingsw.view.exceptions.IllegalStudentIdException;
import it.polimi.ingsw.view.game_objects.ClientPawnColor;
import it.polimi.ingsw.view.game_objects.ClientTowerColor;
import it.polimi.ingsw.view.gui.GUIConstants;
import it.polimi.ingsw.view.gui.StageController;
import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Arrays;

public class SchoolBoardsFunction {

    public static Integer[] updateSchoolBoardEntrance(int indexOfSchoolBoard, Pane schoolBoard, Integer[][] coordinatesOfStudentsInEntrance,
                                                      Integer[] previousEntrance, boolean schoolBoardOfPlayer) throws IllegalStudentIdException {

        // Important: this method is based on the assumption that the only operations applied to the entrance are additions and removals of
        // student discs. No replacement should take place. This is true at the moment.


        // Get current entrance of school board with index indexOfSchoolBoard. The content of the current entrance is compared
        // to the content of the previous entrance
        Integer[] currentEntrance = StageController.getStageController().getClientTable().getSchoolBoards().get(indexOfSchoolBoard).getEntrance().toArray(new Integer[0]);
        int indexOfCoordinate = 0;

        // Case 1: no students in the previous entrance. Add all the students.
        if (previousEntrance.length == 0) {
            for (int studentId : currentEntrance) {
                // Create image view
                ImageView student = createImageViewOfStudent(studentId, coordinatesOfStudentsInEntrance[indexOfCoordinate][1],
                        coordinatesOfStudentsInEntrance[indexOfCoordinate][0]);
                // Add event handler
                if (schoolBoardOfPlayer)
                    student.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> new StudentInEntranceEventHandler().handle(event));
                // Add image view to pane with school board
                schoolBoard.getChildren().add(student);
                // The coordinate has been used, set to -1
                coordinatesOfStudentsInEntrance[indexOfCoordinate][2] = 1;
                indexOfCoordinate++;
            }
        }

        // Case 2: Add the new students to the entrance
        else if (previousEntrance.length < currentEntrance.length) {
            Integer[] newStudents = Arrays
                    .stream(currentEntrance)
                    .filter(id1 -> Arrays
                            .stream(previousEntrance).noneMatch(id2 -> id2.equals(id1)))
                    .toArray(Integer[]::new);

            int newStudentIndex = 0;
            for (int i = 0; i < coordinatesOfStudentsInEntrance.length && newStudents.length > newStudentIndex; i++) {
                // Check if coordinates is not taken
                if (coordinatesOfStudentsInEntrance[i][2] == 0) {
                    // Create image view
                    ImageView student = createImageViewOfStudent(newStudents[newStudentIndex], coordinatesOfStudentsInEntrance[indexOfCoordinate][1],
                            coordinatesOfStudentsInEntrance[indexOfCoordinate][0]);
                    // Add event handler
                    if (schoolBoardOfPlayer)
                        student.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> new StudentInEntranceEventHandler().handle(event));
                    // Add image view to pane with school board
                    schoolBoard.getChildren().add(student);
                    // The coordinate has been used, set third element to 1
                    coordinatesOfStudentsInEntrance[indexOfCoordinate][2] = 1;
                    newStudentIndex++;
                }
                indexOfCoordinate++;
            }
        }

        // Case 3: Remove students from entrance and update array of coordinates
        else if (previousEntrance.length > currentEntrance.length) {
            Integer[] studentsToRemove = Arrays
                    .stream(previousEntrance)
                    .filter(id1 -> Arrays
                            .stream(currentEntrance).noneMatch(id2 -> id2.equals(id1)))
                    .toArray(Integer[]::new);

            for (int i = 0; i < coordinatesOfStudentsInEntrance.length; i++) {
                // Get node at this.coordinatesOfStudentsInEntrance[i] if present
                int finalI = i;
                if (schoolBoard.getChildren()
                        .stream()
                        .filter(n -> n.getLayoutX() == coordinatesOfStudentsInEntrance[finalI][1]
                                && n.getLayoutY() == coordinatesOfStudentsInEntrance[finalI][0])
                        .count() == 1) {
                    Node node = schoolBoard.getChildren()
                            .stream()
                            .filter(n -> n.getLayoutX() == coordinatesOfStudentsInEntrance[finalI][1]
                                    && n.getLayoutY() == coordinatesOfStudentsInEntrance[finalI][0]).toList().get(0);
                    // Check if the node must be removed
                    if (Arrays.stream(studentsToRemove)
                            .filter(id -> id == ViewUtilityFunctions.convertIdOfImageOfStudentDisc(node.getId()))
                            .count() == 1) {
                        // Remove node
                        schoolBoard.getChildren().remove(node);
                        // The coordinate is not used, set third element to 0
                        coordinatesOfStudentsInEntrance[i][2] = 0;
                    }
                }
            }
        }

        //Case 4: Change students that have been replaced
        else {
            // Get student discs that must be replaced
            Integer[] studentsToReplace = Arrays.stream(previousEntrance)
                    .filter(id-> Arrays.asList(previousEntrance).contains(id) && Arrays.stream(currentEntrance).noneMatch(id2-> id2.equals(id)))
                    .toArray(Integer[]::new);

            // Get new students to add
            Integer[] studentsToAdd = Arrays.stream(currentEntrance)
                    .filter(id-> Arrays.stream(previousEntrance).noneMatch(id1-> id1.equals(id)) && Arrays.asList(currentEntrance).contains(id))
                    .toArray(Integer[]::new);

            int indexOfStudentToAdd = 0;
            for (int i = 0; i < coordinatesOfStudentsInEntrance.length; i++) {
                // Get node at this.coordinatesOfStudentsInEntrance[i] if present
                int finalI = i;
                if (schoolBoard.getChildren()
                        .stream()
                        .filter(n -> n.getLayoutX() == coordinatesOfStudentsInEntrance[finalI][1]
                                && n.getLayoutY() == coordinatesOfStudentsInEntrance[finalI][0])
                        .count() == 1) {
                    Node node = schoolBoard.getChildren()
                            .stream()
                            .filter(n -> n.getLayoutX() == coordinatesOfStudentsInEntrance[finalI][1]
                                    && n.getLayoutY() == coordinatesOfStudentsInEntrance[finalI][0]).toList().get(0);
                    // Check if the student must be replaced
                    if (Arrays.stream(studentsToReplace)
                            .filter(id -> id == ViewUtilityFunctions.convertIdOfImageOfStudentDisc(node.getId()))
                            .count() == 1) {
                        // Change node to image view
                        ImageView student = (ImageView) node;
                        // Change id of node
                        student.setId(GUIConstants.STUDENT_DISC_NAME + studentsToAdd[indexOfStudentToAdd]);
                        // Change image of node
                        student.setImage(Images.getImages().getStudentDiscs()[ViewUtilityFunctions.convertStudentIdToIdOfColor(studentsToAdd[indexOfStudentToAdd])]);
                        // Increment index for array of students to add
                        indexOfStudentToAdd++;
                    }
                }
            }
        }

        // Update previous entrance
        return currentEntrance;
    }

    public static void updateSchoolBoardDiningRoom(int indexOfSchoolBoard, Pane schoolBoard, Integer[][] firstAvailableCoordinatesOfDiningRoom,
                                                   ArrayList<ArrayList<Integer>> previousDiningRoom, boolean schoolBoardOfPlayer)
            throws IllegalLaneException, IllegalStudentIdException {

        // Important: this method is based on the assumption that the only operations applied to the dining room are the addition and
        // the removal of student discs. This is true at the moment. The students are removed after the thief character card
        // is applied

        int indexOfLaneInClientTable = 0;
        for (ArrayList<Integer> lane : StageController.getStageController().getClientTable().getSchoolBoards().get(indexOfSchoolBoard).getDiningRoom()) {
            // The index of color x differs from the corresponding lane in the image, thus a mapping is required
            int indexOfLaneInImage = ViewUtilityFunctions.convertPawnColorIndexToLaneIndex(indexOfLaneInClientTable);

            // Add new students to dining room
            if (lane.size() > previousDiningRoom.get(indexOfLaneInClientTable).size()) {
                int finalIndexOfLaneInClientTable = indexOfLaneInClientTable;
                Integer[] newStudents = lane
                        .stream()
                        .filter(id1 -> previousDiningRoom.get(finalIndexOfLaneInClientTable)
                                .stream().noneMatch(id2 -> id2.equals(id1)))
                        .toArray(Integer[]::new);
                for (int studentId : newStudents) {
                    // Create image view of student
                    ImageView student = createImageViewOfStudent(studentId, firstAvailableCoordinatesOfDiningRoom[indexOfLaneInImage][1],
                            firstAvailableCoordinatesOfDiningRoom[indexOfLaneInImage][0]);
                    // Add event handler to student if needed
                    if (schoolBoardOfPlayer)
                        student.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> new StudentInDiningEventHandler().handle(event));
                    // Add image view to pane with school board
                    schoolBoard.getChildren().add(student);
                    // Update first available coordinate
                    firstAvailableCoordinatesOfDiningRoom[indexOfLaneInImage][0] += GUIConstants.LAYOUT_Y_OFFSET_CELLS_DINING_ROOM;
                }
                // Update content of previous dining room
                previousDiningRoom.get(indexOfLaneInClientTable).clear();
                previousDiningRoom.get(indexOfLaneInClientTable).addAll(lane);
            }

            // Remove students from dining room
            else if (lane.size() < previousDiningRoom.get(indexOfLaneInClientTable).size()) {
                int layoutXOfStudentToRemove = firstAvailableCoordinatesOfDiningRoom[indexOfLaneInImage][1];
                int layoutYOfStudentToRemove = firstAvailableCoordinatesOfDiningRoom[indexOfLaneInImage][0] - GUIConstants.LAYOUT_Y_OFFSET_CELLS_DINING_ROOM;
                for (int i = 0; i < previousDiningRoom.get(indexOfLaneInClientTable).size() - lane.size(); i++) {
                    int finalLayoutYOfStudentToRemove = layoutYOfStudentToRemove;
                    Node node = schoolBoard.getChildren().stream()
                            .filter(node1 -> node1.getLayoutX() == layoutXOfStudentToRemove && node1.getLayoutY() == finalLayoutYOfStudentToRemove)
                            .toList().get(0);
                    schoolBoard.getChildren().remove(node);
                    layoutYOfStudentToRemove -= GUIConstants.LAYOUT_Y_OFFSET_CELLS_DINING_ROOM;
                    firstAvailableCoordinatesOfDiningRoom[indexOfLaneInImage][0] -= GUIConstants.LAYOUT_Y_OFFSET_CELLS_DINING_ROOM;
                }
            }
            // Increment lane index
            indexOfLaneInClientTable++;
        }
    }

    public static void updateSchoolBoardProfessorSection(int indexOfTeam, Pane schoolBoard, Integer[][] coordinatesOfProfessorPawns,
                                                         ArrayList<ClientPawnColor> previousProfessorSection) throws IllegalLaneException {

        // Case 1: Add professor pawns
        if (previousProfessorSection.size() < StageController.getStageController().getClientTeams()
                .getTeams().get(indexOfTeam).getProfessorPawns().size()) {

            // Find professors to add
            ClientPawnColor[] professorsToAdd = StageController.getStageController().getClientTeams().getTeams().get(indexOfTeam).getProfessorPawns()
                    .stream()
                    .filter(professor1 -> previousProfessorSection
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
                professorPawn.setLayoutX(coordinatesOfProfessorPawns[indexOfProfessorInSchoolBoardImage][1]);
                professorPawn.setLayoutY(coordinatesOfProfessorPawns[indexOfProfessorInSchoolBoardImage][0]);
                // Set effect
                professorPawn.setEffect(new ColorAdjust(0, 0, 0.1, 0));
                // Add professor pawn
                schoolBoard.getChildren().add(professorPawn);
            }

            previousProfessorSection.clear();
            previousProfessorSection.addAll(StageController.getStageController().getClientTeams().getTeams().get(indexOfTeam).getProfessorPawns());
        }

        // Case 2: Remove professor pawns
        else if (previousProfessorSection.size() > StageController.getStageController().getClientTeams()
                .getTeams().get(indexOfTeam).getProfessorPawns().size()) {

            // Find professors to remove
            ClientPawnColor[] professorsToRemove = previousProfessorSection
                    .stream()
                    .filter(professor1 -> StageController.getStageController().getClientTeams().getTeams().get(indexOfTeam).getProfessorPawns()
                            .stream().noneMatch(professor2 -> professor2 == professor1))
                    .toArray(ClientPawnColor[]::new);

            for (int i = 0; i < coordinatesOfProfessorPawns.length; i++) {
                // Get node at this.coordinatesOfProfessorPawns[i] if present
                int finalI = i;
                if (schoolBoard.getChildren()
                        .stream()
                        .filter(n -> n.getLayoutX() == coordinatesOfProfessorPawns[finalI][1]
                                && n.getLayoutY() == coordinatesOfProfessorPawns[finalI][0])
                        .count() == 1) {
                    Node node = schoolBoard.getChildren()
                            .stream()
                            .filter(n -> n.getLayoutX() == coordinatesOfProfessorPawns[finalI][1]
                                    && n.getLayoutY() == coordinatesOfProfessorPawns[finalI][0]).toList().get(0);

                    // Check if the node must be removed
                    if (Arrays.stream(professorsToRemove)
                            .filter(prof -> prof.getId() == ViewUtilityFunctions.convertIdOfImageOfProfessorPawn(node.getId()))
                            .count() == 1) {
                        // Remove node
                        schoolBoard.getChildren().remove(node);
                    }
                }
            }

            previousProfessorSection.clear();
            previousProfessorSection.addAll(StageController.getStageController().getClientTeams().getTeams().get(indexOfTeam).getProfessorPawns());
        }
    }

    public static int updateSchoolBoardTowersSection(int indexOfTeam, Pane schoolBoard, Integer[][] coordinatesOfTowers, int previousNumberOfTowers) {

        // Save current number of towers and towers color for simplicity
        int currentNumberOfTowers = StageController.getStageController().getClientTeams().getTeams().get(indexOfTeam).getNumberOfTowers();
        int idOfColorOfTowers = StageController.getStageController().getClientTeams().getTeams().get(indexOfTeam).getTowersColor().getId();


        // Case 1: Add towers to tower section
        if (previousNumberOfTowers < currentNumberOfTowers) {
            for (int i = previousNumberOfTowers; i < currentNumberOfTowers; i++) {
                ImageView tower = new ImageView(Images.getImages().getTowers()[idOfColorOfTowers]);
                tower.setId(GUIConstants.TOWER_NAME + (idOfColorOfTowers * ModelConstants.MAX_NUMBER_OF_TOWERS + i + 1));
                tower.setPreserveRatio(false);
                tower.setFitWidth(GUIConstants.WIDTH_OF_TOWER_SCHOOL_BOARD);
                tower.setFitHeight(GUIConstants.HEIGHT_OF_TOWER_SCHOOL_BOARD);
                tower.setLayoutX(coordinatesOfTowers[i][1]);
                tower.setLayoutY(coordinatesOfTowers[i][0]);
                schoolBoard.getChildren().add(tower);
            }
        }

        // Case 2: Remove towers to tower section
        if (previousNumberOfTowers > currentNumberOfTowers) {
            for (int i = currentNumberOfTowers; i < previousNumberOfTowers; i++) {
                // Get node at this.coordinatesOfTowers[i]
                int finalI = i;
                Node node = schoolBoard.getChildren()
                        .stream()
                        .filter(n -> n.getLayoutX() == coordinatesOfTowers[finalI][1]
                                && n.getLayoutY() == coordinatesOfTowers[finalI][0]).toList().get(0);

                // Remove node
                schoolBoard.getChildren().remove(node);
            }
        }
        // Update number of towers
        return currentNumberOfTowers;
    }

    public static ImageView createImageViewOfStudent(int studentId, int layoutX, int layoutY) throws IllegalStudentIdException {
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

    public static void generateCoordinates(Integer[][] coordinatesOfStudentsInEntrance, Integer[][] firstAvailableCoordinatesOfDiningRoom,
                                           Integer[][] coordinatesOfProfessorPawns, Integer[][] coordinatesOfTowers) {
        generateCoordinatesOfStudentsInEntrance(coordinatesOfStudentsInEntrance);
        initializeCoordinatesOfDiningRoom(firstAvailableCoordinatesOfDiningRoom);
        generateCoordinatesForProfessors(coordinatesOfProfessorPawns);
        generateCoordinatesForTowers(coordinatesOfTowers);
    }

    private static void generateCoordinatesOfStudentsInEntrance(Integer[][] coordinatesOfStudentsInEntrance) {
        int coordinateIndex = 0;
        for (int i = GUIConstants.LAYOUT_Y_OF_FIRST_CELL_FIRST_ROW_ENTRANCE;
             i < GUIConstants.LAYOUT_Y_OF_FIRST_CELL_FIRST_ROW_ENTRANCE + GUIConstants.COLUMNS_ENTRANCE * GUIConstants.LAYOUT_Y_OFFSET_CELLS_ENTRANCE; i += GUIConstants.LAYOUT_Y_OFFSET_CELLS_ENTRANCE)
            for (int j = GUIConstants.LAYOUT_X_OF_FIRST_CELL_FIRST_ROW_ENTRANCE;
                 j < GUIConstants.LAYOUT_X_OF_FIRST_CELL_FIRST_ROW_ENTRANCE + GUIConstants.CELLS_FIRST_ROW_ENTRANCE * GUIConstants.LAYOUT_X_OFFSET_CELLS_ENTRANCE; j += GUIConstants.LAYOUT_X_OFFSET_CELLS_ENTRANCE)
                if ((i != GUIConstants.LAYOUT_Y_OF_FIRST_CELL_FIRST_ROW_ENTRANCE + GUIConstants.LAYOUT_Y_OFFSET_CELLS_ENTRANCE)
                        || (j != GUIConstants.LAYOUT_X_OF_FIRST_CELL_FIRST_ROW_ENTRANCE)) {
                    coordinatesOfStudentsInEntrance[coordinateIndex][0] = i;
                    coordinatesOfStudentsInEntrance[coordinateIndex][1] = j;
                    // The coordinate is not used, set third element to 0
                    coordinatesOfStudentsInEntrance[coordinateIndex][2] = 0;
                    coordinateIndex++;
                }
    }

    private static void initializeCoordinatesOfDiningRoom(Integer[][] firstAvailableCoordinatesOfDiningRoom) {
        for (int i = 0; i < GUIConstants.LANES; i++) {
            firstAvailableCoordinatesOfDiningRoom[i][0] = GUIConstants.LAYOUT_Y_OF_FIRST_CELL_FIRST_LANE_ON_LEFT_BOTTOM_DINING_ROOM;
            firstAvailableCoordinatesOfDiningRoom[i][1] = GUIConstants.LAYOUT_X_OF_FIRST_CELL_FIRST_LANE_ON_LEFT_BOTTOM_DINING_ROOM +
                    i * GUIConstants.LAYOUT_X_OFFSET_CELLS_DINING_ROOM;
        }
    }

    private static void generateCoordinatesForProfessors(Integer[][] coordinatesOfProfessorPawns) {
        for (int i = 0; i < GUIConstants.LANES; i++) {
            coordinatesOfProfessorPawns[i][0] = GUIConstants.LAYOUT_Y_OF_FIRST_CELL_PROFESSOR_SECTION;
            coordinatesOfProfessorPawns[i][1] = GUIConstants.LAYOUT_X_OF_FIRST_CELL_PROFESSOR_SECTION + i * GUIConstants.LAYOUT_X_OFFSET_CELLS_PROFESSOR_SECTION;
        }
    }

    private static void generateCoordinatesForTowers(Integer[][] coordinatesOfTowers) {
        int indexOfCoordinate = 0;
        for (int i = GUIConstants.LAYOUT_Y_OF_FIRST_CELL_TOWERS; i > GUIConstants.LAYOUT_Y_OF_FIRST_CELL_TOWERS
                + GUIConstants.NUMBER_OF_ROWS_OF_TOWERS * GUIConstants.LAYOUT_Y_OFFSET_TOWERS; i += GUIConstants.LAYOUT_Y_OFFSET_TOWERS)
            for (int j = GUIConstants.LAYOUT_X_OF_FIRST_CELL_TOWERS; j < GUIConstants.LAYOUT_X_OF_FIRST_CELL_TOWERS
                    + GUIConstants.NUMBER_OF_TOWERS_IN_ROW * GUIConstants.LAYOUT_X_OFFSET_TOWERS; j += GUIConstants.LAYOUT_X_OFFSET_TOWERS) {
                coordinatesOfTowers[indexOfCoordinate][1] = j;
                coordinatesOfTowers[indexOfCoordinate][0] = i;
                indexOfCoordinate++;
            }
    }
}