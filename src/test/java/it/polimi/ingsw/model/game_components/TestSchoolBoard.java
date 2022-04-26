package it.polimi.ingsw.model.game_components;

import it.polimi.ingsw.model.exceptions.IllegalStudentDiscMovementException;
import it.polimi.ingsw.model.game_components.PawnColor;
import it.polimi.ingsw.model.game_components.SchoolBoard;
import it.polimi.ingsw.model.game_components.StudentDisc;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.NoSuchElementException;


public class TestSchoolBoard {

    /**
     * Creates a SchoolBoard and checks if the entrance is correctly empty.
     * Then it creates an ArrayList of StudentDisc for the studentsToAdd to the SchoolBoard Entrance.
     * Next it verifies if the Entrance has the same number of students added before.
     * At the end it checks if it is possible to remove the students from the Entrance.
     * In this case it decreases the number of students in the SchoolBoard Entrance.
     * If it is not possible, it throws the NoSuchElementException.
     *
     * @param value to test.
     */
    @ParameterizedTest
    @ValueSource(ints = {1,10})
    public void testEntrance(int value)
    {
        SchoolBoard schoolBoard = new SchoolBoard();
        assertEquals(0, schoolBoard.getEntrance().size());

        ArrayList<StudentDisc> studentsToAdd = new ArrayList<>();
        for (int i = 0; i < value; i++) {
            studentsToAdd.add(new StudentDisc(i, PawnColor.BLUE));
        }
        schoolBoard.addStudentsToEntrance(studentsToAdd);

        assertEquals(value, schoolBoard.getEntrance().size());
        assertDoesNotThrow(()->schoolBoard.removeStudentFromEntrance(studentsToAdd.get(0).getId()));
        assertEquals(value-1, schoolBoard.getEntrance().size());
        assertThrows(NoSuchElementException.class, ()->schoolBoard.removeStudentFromEntrance(studentsToAdd.get(0).getId()));
    }

    /**
     * Creates a SchoolBoard and check if its DiningRoom is correctly empty.
     * Then it creates a StudentDisc to add to the SchoolBoard DiningRoom.
     * Next it checks if the student is correctly set in the DiningRoom by a color comparison.
     * Then it creates another StudentDisc to add to the DiningRoom and checks if it has the correct number of students.
     * At the end it verifies if the last student is correctly set in the DiningRoom.
     *
     * @param color color to test.
     */
    @ParameterizedTest
    @EnumSource(PawnColor.class)
    public void testDiningRoom(PawnColor color)
    {
        SchoolBoard schoolBoard = new SchoolBoard();
        for(PawnColor i: PawnColor.values())
        {
            assertEquals(0, schoolBoard.getDiningRoom().get(i.getId()).size());
            assertEquals(0, schoolBoard.getDiningRoomColor(i).size());
        }

        StudentDisc studentDisc = new StudentDisc(0,color);
        schoolBoard.addStudentToDiningRoom(studentDisc);

        for(PawnColor i: PawnColor.values())
        {
            // Test getDiningRoomColor
            if(color.equals(i))
                assertEquals(1, schoolBoard.getDiningRoom().get(i.getId()).size());
            else
                assertEquals(0, schoolBoard.getDiningRoom().get(i.getId()).size());

            // Test getDiningRoom
            if(color.equals(i))
                assertEquals(1, schoolBoard.getDiningRoomColor(i).size());
            else
                assertEquals(0, schoolBoard.getDiningRoomColor(i).size());
        }

        // Now I check the correct position in the table
        StudentDisc studentDisc2 = new StudentDisc(1,color);
        schoolBoard.addStudentToDiningRoom(studentDisc2);
        assertEquals(2, schoolBoard.getDiningRoomColor(color).size());
        assertEquals(studentDisc2, schoolBoard.getDiningRoomColor(color).get(schoolBoard.getDiningRoomColor(color).size()-1));
    }

    /**
     * Creates a SchoolBoard and four StudentDisc (the firsts two are added to the SchoolBoard DiningRoom).
     * Next it checks if the DiningRoom BLUE table has correctly the two students added before.
     * Then it tries to remove the first StudentDisc.
     * However, it is not the last student of the blue table, so it throws the IllegalStudentDiscMovementException.
     * Next it checks if the DiningRoom is not change.
     * Then it replaces the last BLUE student with the third StudentDisc, which is BLUE.
     * After that it checks if the DiningRoom BLUE table has correctly two students.
     * Then it tries to replace the second StudentDisc with the fourth one.
     * However they are not in the DiningRoom so it throws the NoSuchElementException and verifies if the DiningRoom is not changed.
     * After that it replaces the third StudentDisc with the fourth one.
     * Next it checks if the DiningRoom is correctly updated and verifies if the fourth StudentDisc is in the DiningRoom
     * while the third one is outside the DiningRoom.
     * At the end it checks if the DiningRoom final state is correct and gets the DiningRoom BLUE and GREEN tables.
     * Also tests {@link SchoolBoard#removeStudentFromDiningRoom(StudentDisc)}
     */
    @Test
    public void testReplaceStudentInDiningRoom()
    {
        SchoolBoard schoolBoard = new SchoolBoard();
        StudentDisc sd1 = new StudentDisc(1, PawnColor.BLUE);
        StudentDisc sd2 = new StudentDisc(2, PawnColor.BLUE);
        StudentDisc sd3 = new StudentDisc(3, PawnColor.BLUE);
        StudentDisc sd4 = new StudentDisc(4, PawnColor.GREEN);
        schoolBoard.addStudentToDiningRoom(sd1);
        schoolBoard.addStudentToDiningRoom(sd2);

        /*
        Current state:
            BLUE: sd1, sd2, ------
         */

        assertEquals(2, schoolBoard.getDiningRoomColor(PawnColor.BLUE).size());

        // Try to remove sd1 which is not the last student of the blue table
        assertThrows(IllegalStudentDiscMovementException.class, ()->schoolBoard.replaceStudentInDiningRoom(sd1,sd4));
        // Check nothing happened
        assertTrue(schoolBoard.getDiningRoomColor(sd1.getColor()).contains(sd1));
        assertFalse(schoolBoard.getDiningRoomColor(sd1.getColor()).contains(sd4));
        assertEquals(2, schoolBoard.getDiningRoomColor(sd1.getColor()).size());
        assertEquals(0, schoolBoard.getDiningRoomColor(sd4.getColor()).size());

        // Ask to replace sd2 (ok) with sd3 (same color)
        assertDoesNotThrow(()->schoolBoard.replaceStudentInDiningRoom(sd2,sd3));
        assertTrue(schoolBoard.getDiningRoomColor(sd3.getColor()).contains(sd3));
        assertFalse(schoolBoard.getDiningRoomColor(sd2.getColor()).contains(sd2));
        assertEquals(2, schoolBoard.getDiningRoomColor(sd1.getColor()).size());

        /*
        Current state:
            BLUE: sd1, sd3, ------
         */

        // Ask to replace sd2 (which is not inside) with sd4 and check correct list state after it
        assertThrows(NoSuchElementException.class, ()->schoolBoard.replaceStudentInDiningRoom(sd2,sd4));
        assertEquals(2, schoolBoard.getDiningRoomColor(sd1.getColor()).size());
        assertEquals(0, schoolBoard.getDiningRoomColor(sd4.getColor()).size());

        // Ask to replace sd3 with sd4 and check it happened
        assertDoesNotThrow(()->schoolBoard.replaceStudentInDiningRoom(sd3,sd4));
        assertEquals(1, schoolBoard.getDiningRoomColor(sd3.getColor()).size());
        assertEquals(1, schoolBoard.getDiningRoomColor(sd4.getColor()).size());
        assertTrue(schoolBoard.getDiningRoomColor(sd4.getColor()).contains(sd4));
        assertFalse(schoolBoard.getDiningRoomColor(sd3.getColor()).contains(sd3));

        /*
        Current state:
            BLUE: sd1, ------
            GREEN: sd4, ------
         */

        // Check final state
        assertEquals(0, schoolBoard.getDiningRoomColor(PawnColor.YELLOW).size());
        assertEquals(1, schoolBoard.getDiningRoomColor(PawnColor.BLUE).size());
        assertEquals(1, schoolBoard.getDiningRoomColor(PawnColor.GREEN).size());
        assertEquals(0, schoolBoard.getDiningRoomColor(PawnColor.RED).size());
        assertEquals(0, schoolBoard.getDiningRoomColor(PawnColor.PINK).size());
        assertTrue(schoolBoard.getDiningRoomColor(PawnColor.BLUE).contains(sd1));
        assertTrue(schoolBoard.getDiningRoomColor(PawnColor.GREEN).contains(sd4));
    }
}