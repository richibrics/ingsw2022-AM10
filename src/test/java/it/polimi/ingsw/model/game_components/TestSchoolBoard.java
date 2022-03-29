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
        assertDoesNotThrow(()->schoolBoard.removeStudentFromEntrance(studentsToAdd.get(0)));
        assertEquals(value-1, schoolBoard.getEntrance().size());
        assertThrows(NoSuchElementException.class, ()->schoolBoard.removeStudentFromEntrance(studentsToAdd.get(0)));
    }

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