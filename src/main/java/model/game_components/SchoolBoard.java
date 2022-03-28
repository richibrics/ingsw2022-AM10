package model.game_components;

import model.exceptions.IllegalStudentDiscMovementException;

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
        return new ArrayList<>(this.entrance);
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
        // Get the dining room table of the student color
        ArrayList<StudentDisc> studentsTable = this.diningRoom.get(studentToAdd.getColor().getId());
        studentsTable.add(studentsTable.size(), studentToAdd);
    }

    /**
     * Remove the StudentDisc from the SchoolBoard entrance
     *
     * @param studentToRemove StudentDisc to remove from SchoolBoard entrance
     * @throws NoSuchElementException if the StudentDisc isn't in the SchoolBoard entrance
     * @see     StudentDisc
     */
    public void removeStudentFromEntrance(StudentDisc studentToRemove) throws NoSuchElementException
    {
        if(!this.entrance.remove(studentToRemove))
            throw new NoSuchElementException("Requested StudentDisc isn't in the entrance for this SchoolBoard");
    }

    /**
     * Replace a StudentDisc already in the dining room (that must be in the last position of the table), with another StudentDisc
     *
     * @param studentToRemove   StudentDisc that has to be removed from the table
     * @param studentToAdd      StudentDisc that has to be added to the table
     * @throws NoSuchElementException if the StudentDisc that has to be removed isn't in the dining room
     * @throws IllegalStudentDiscMovementException if the StudentDisc isn't in the last position of the table
     * @see    StudentDisc
     */
    public void replaceStudentInDiningRoom(StudentDisc studentToRemove, StudentDisc studentToAdd) throws IllegalStudentDiscMovementException, NoSuchElementException
    {
        // Get the dining room table of the student to remove color
        ArrayList<StudentDisc> studentsTable = this.diningRoom.get(studentToRemove.getColor().getId());
        // Check if the student is in the table
        if(!studentsTable.contains(studentToRemove))
            throw new NoSuchElementException("Requested StudentDisc isn't in the player's dining room");
        // Check if the student is in the table in a position that isn't the last of the row
        if(!studentsTable.get(studentsTable.size()-1).equals(studentToRemove))
            throw new IllegalStudentDiscMovementException();
        // Now I can remove the StudentDisc from the table
        studentsTable.remove(studentToRemove);
        // Insert the new student in the dining room
        this.addStudentToDiningRoom(studentToAdd);
    }
}
