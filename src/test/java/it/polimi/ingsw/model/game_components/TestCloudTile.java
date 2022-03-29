package it.polimi.ingsw.model.game_components;

import it.polimi.ingsw.model.game_components.CloudTile;
import it.polimi.ingsw.model.game_components.PawnColor;
import it.polimi.ingsw.model.game_components.StudentDisc;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TestCloudTile {

    @ParameterizedTest
    @ValueSource(ints = {0,100,Integer.MAX_VALUE})
    public void testGetId(int value)
    {
        CloudTile cloudTile = new CloudTile(value);
        assertEquals(cloudTile.getId(),value);
    }

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
        assertTrue(arrayListElementsMatch(studentsToAdd,cloudTile.peekStudents()));
        assertTrue(arrayListElementsMatch(cloudTile.popStudents(),studentsToAdd));
        assertEquals(0,cloudTile.peekStudents().size());
    }

    public boolean arrayListElementsMatch(ArrayList<StudentDisc> list1, ArrayList<StudentDisc> list2)
    {
        if(list1.size()!=list2.size())
            return false;
        for (int i = 0; i < list1.size(); i++) {
            if(!list1.get(i).equals(list2.get(i)))
                return false;
        }
        return true;
    }
}
