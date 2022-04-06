package it.polimi.ingsw.model.game_components;

import it.polimi.ingsw.model.game_components.CloudTile;
import it.polimi.ingsw.model.game_components.PawnColor;
import it.polimi.ingsw.model.game_components.StudentDisc;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TestCloudTile {

    /**
     * Creates a CloudTile and checks if it returns the correct id
     *
     * @param value to test.
     */
    @ParameterizedTest
    @ValueSource(ints = {0,100,Integer.MAX_VALUE})
    public void testGetId(int value)
    {
        CloudTile cloudTile = new CloudTile(value);
        assertEquals(cloudTile.getId(),value);
    }

    /**
     * Creates a CloudTile and an ArrayList of StudentDisc made by students that will be added to the CloudTile.
     * Then it checks if the CloudTile has the same students added.
     * Then it pops the students and checks if it returns the correct students leaving the CloudTile empty.
     * At the end it checks if the CloudTile is correctly empty.
     *
     * @param value to test.
     */
    @ParameterizedTest
    @ValueSource(ints = {0,100})
    public void testStudents(int value)
    {
        CloudTile cloudTile = new CloudTile(0);
        ArrayList<StudentDisc> studentsToAdd = new ArrayList<>();
        for (int i = 0; i < value; i++) {
            studentsToAdd.add(new StudentDisc(i, PawnColor.BLUE));
        }
        cloudTile.addStudents(studentsToAdd);
        assertTrue(studentsToAdd.equals(cloudTile.peekStudents()));
        assertTrue(studentsToAdd.equals(cloudTile.popStudents()));
        assertEquals(0,cloudTile.peekStudents().size());
    }

}
