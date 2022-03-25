package model.game_components;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class SchoolBoard {
    final private ArrayList<StudentDisc> entrance;
    final private ArrayList<ArrayList<StudentDisc>> diningRoom;

    public SchoolBoard() {
        this.entrance = new ArrayList<>();
        this.diningRoom = new ArrayList<>();
        for(PawnColor color: PawnColor.values())
        {
            this.diningRoom.add(new ArrayList<>());
        }
    }

    /**
     * Returns the ArrayList of StudentDiscs in the Entrance
     *
     * @return  ArrayList of StudentDiscs in the entrance
     * @see     StudentDisc
     */
    public ArrayList<StudentDisc> getEntrance()
    {
        return new ArrayList<StudentDisc>(this.entrance);
    }

    /**
     * Returns the ArrayList of StudentDiscs of the specified color in the DiningRoom
     *
     * @param color Color of the requested row in the dining room
     * @return  ArrayList of StudentDiscs of the specified color in the DiningRoom
     * @see     StudentDisc
     */
    public ArrayList<StudentDisc> getDiningRoomColor(PawnColor color)
    {
        return new ArrayList<>(this.diningRoom.get(color.getId()));
    }

    /**
     * Returns the matrix (ArrayList of ArrayList) of StudentDiscs in the DiningRoom
     *
     * @return  Matrix of StudentDiscs in the DiningRoom
     * @see     StudentDisc
     */
    public ArrayList<ArrayList<StudentDisc>> getDiningRoom()
    {
        ArrayList<ArrayList<StudentDisc>> return_matrix = new ArrayList<>();
        for(PawnColor color: PawnColor.values())
        {
            return_matrix.add(color.getId(), getDiningRoomColor(color));
        }
        return return_matrix;
    }

    /**
     * Add StudentDiscs to the SchoolBoard entrance
     *
     * @param studentsToAdd ArrayList of StudentDiscs to add to the SchoolBoard entrance
     * @see     StudentDisc
     */
    public void addStudentsToEntrance(ArrayList<StudentDisc> studentsToAdd)
    {
        this.entrance.addAll(studentsToAdd);
    }

    /**
     * Add StudentDisc to the SchoolBoard dining room in the right table
     *
     * @param studentToAdd StudentDisc to add to the SchoolBoard dining room
     * @see StudentDisc
     */
    public void addStudentToDiningRoom(StudentDisc studentToAdd)
    {
        this.diningRoom.get(studentToAdd.getColor().getId()).add(studentToAdd);
    }

    /**
     * Remove the StudentDisc from the SchoolBoard entrance
     *
     * @param studentToRemove StudentDisc to remove from SchoolBoard entrance
     * @throws NoSuchElementException if the StudentDisc isn't in the SchoolBoard entrance
     */
    public void removeStudentFromEntrance(StudentDisc studentToRemove) throws NoSuchElementException
    {
        if(!this.entrance.remove(studentToRemove))
            throw new NoSuchElementException("Requested StudentDisc isn't in the entrance for this SchoolBoard");
    }
}
