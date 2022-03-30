package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.game_components.Table;

public class GameEngine {

    private Table table;

    public GameEngine() {

    }

    public Table getTable() {
        return this.table;
    }

    public void setTable(Table table) {
        this.table = table;
    }
}
