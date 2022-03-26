package model.game_components;

import model.exceptions.EmptyBagException;

import java.util.ArrayList;
import java.util.Collections;

public class Bag {
    final private ArrayList<StudentDisc> students;

    public Bag() {
        this.students = new ArrayList<>();
    }

    /**
     * Add StudentDisc to the Bag and shuffle all the StudentDiscs in the bag, so I will be able to draw them randomly
     *
     * @param studentsToPush ArrayList with StudentDiscs to add to the Bag
     * @see         StudentDisc
     */
    public void pushStudents(ArrayList<StudentDisc> studentsToPush)
    {
        this.students.addAll(studentsToPush);
        Collections.shuffle(this.students);
    }

    /**
     * Return the number of StudentDiscs in the Bag
     *
     * @return      Number of StudentDiscs in the Bag
     * @throws EmptyBagException if the bag doesn't contain any StudentDisc
     */
    public int getNumberOfStudents()
    {
        return this.students.size();
    }

    private StudentDisc drawStudent() throws EmptyBagException {
        // I make two separated "draw" methods so if I change the random draw mode I can edit only this method
        if(this.students.isEmpty())
            throw new EmptyBagException();
        return this.students.remove(0);
    }

    /**
     * Pop from the list a random Collection of Students and return them
     *
     * @param numberToDraw  Number of StudentDiscs that need to be drawn
     * @return      Randomly drawn StudentDiscs
     * @throws EmptyBagException if the bag doesn't contain enough StudentDisc
     * @see         StudentDisc
     */
    public ArrayList<StudentDisc> drawStudents(int numberToDraw) throws EmptyBagException {
        if(this.getNumberOfStudents()<numberToDraw)
            throw new EmptyBagException("The Bag doesn't contain enough elements");
        ArrayList<StudentDisc> drawnStudents = new ArrayList<>();
        for (int i = 0; i < numberToDraw; i++) {
            drawnStudents.add(this.drawStudent());
        }
        return drawnStudents;
    }
}
