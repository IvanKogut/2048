package ua.kyiv.kpi.fpm.kogut.app.controller;

import ua.kyiv.kpi.fpm.kogut.app.model.Direction;
import ua.kyiv.kpi.fpm.kogut.app.model.Model;
import ua.kyiv.kpi.fpm.kogut.app.model.Tile;
import ua.kyiv.kpi.fpm.kogut.app.view.View;

/**
 * Created by Admin on 07.02.2017.
 */
public class Controller implements EventListener {

    private Model model;
    private View view;

    public Controller() {
        this.model = new Model();
        this.view = new View(this);

        view.init();
        view.setEventListener(this);
    }

    public Tile[][] getTiles() {
        return model.getGameTiles();
    }

    public int getScore() {
        return model.getScore();
    }

    public int getMaxTile() {
        return model.getMaxTile();
    }

    @Override
    public void move(Direction direction) {
        switch (direction) {
            case UP:
                model.up();
                break;
            case RIGHT:
                model.right();
                break;
            case DOWN:
                model.down();
                break;
            case LEFT:
                model.left();
                break;
        }

        view.update();
    }

    @Override
    public void saveGame() {
        model.saveTiles();
    }

    @Override
    public void loadGame() {
        model.loadTiles();
        view.update();
    }
}