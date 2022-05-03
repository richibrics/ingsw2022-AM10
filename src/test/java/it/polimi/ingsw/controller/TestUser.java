package it.polimi.ingsw.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class TestUser {

    @Test
    void getId() {
        User user = new User("test",0);
        assertEquals("test",user.getId());
    }

    @ParameterizedTest
    @ValueSource(ints = {0,100,Integer.MAX_VALUE})
    void getPreference(int value) {
        User user = new User("test",value);
        assertEquals(value,user.getPreference());
    }

    @Test
    void changePreference() {
        User user = new User("test",3);
        user.changePreference(2);
        assertEquals(user.getPreference(), 2);
    }
}