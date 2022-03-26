package model.game_components;

import model.exceptions.EmptyBagException;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;

public class TestBag {

    @ParameterizedTest
    @ValueSource(ints = {10, 100, 200})
    public void testPushStudentsAndGetNumberOfStudents(int value)
    {
        Bag bag = new Bag();
        ArrayList students = new ArrayList<>();
        for (int i = 0; i < value; i++) {
            students.add(new StudentDisc(i, PawnColor.BLUE));
        }
        bag.pushStudents(students);
        assertEquals(value, bag.getNumberOfStudents());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 10, 100, 200})
    public void testDrawStudents(int value)
    {
        Bag bag = new Bag();
        ArrayList<StudentDisc> students = new ArrayList<>();
        for (int i = 0; i < value; i++) {
            students.add(new StudentDisc(i, PawnColor.BLUE));
        }
        bag.pushStudents(students);

        ArrayList<StudentDisc> drawnStudents = assertDoesNotThrow(()-> bag.drawStudents(value));

        // Check no duplicates
        assertEquals(value, new LinkedHashSet<>(drawnStudents).size());

        assertEquals(0, bag.getNumberOfStudents());
    }

    @Test
    public void TestEmptyBagException()
    {
        Bag bag = new Bag();
        assertEquals(bag.getNumberOfStudents(),0);
        assertThrows(EmptyBagException.class, () -> bag.drawStudents(1));

        ArrayList<StudentDisc> studentDiscs = new ArrayList<>();
        studentDiscs.add(new StudentDisc(0,PawnColor.BLUE));
        studentDiscs.add(new StudentDisc(1,PawnColor.BLUE));
        bag.pushStudents(studentDiscs);

        assertEquals(bag.getNumberOfStudents(),2);
        assertDoesNotThrow(()->bag.drawStudents(1));
        assertEquals(bag.getNumberOfStudents(),1);
        assertThrows(EmptyBagException.class, ()-> bag.drawStudents(2));
        assertEquals(bag.getNumberOfStudents(),1);
    }

    @Test
    public void TestRandomness()
    {
        // Place the same students in 2 bags. Draw them; if the draw order from the first StudentDisc
        // to the last is exactly the same there is no randomness.
        int N = 1000;
        Bag bag1 = new Bag();
        Bag bag2 = new Bag();
        ArrayList<StudentDisc> studentDiscs = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            studentDiscs.add(new StudentDisc(i,PawnColor.BLUE));
        }
        bag1.pushStudents(studentDiscs);
        bag2.pushStudents(studentDiscs);
        // bag1 & bag2: bags with the same students that should be ordered differently

        boolean samePositions = true;

        int i=0;
        StudentDisc studentFromBag1;
        StudentDisc studentFromBag2;
        while(samePositions && i<N)
        {
            studentFromBag1 = assertDoesNotThrow(()-> bag1.drawStudents(1).get(0));
            studentFromBag2 = assertDoesNotThrow(()-> bag2.drawStudents(1).get(0));
            if(!studentFromBag1.equals(studentFromBag2))
                samePositions=false;
            i+=1;
        }
        assertFalse(samePositions);
    }
}
