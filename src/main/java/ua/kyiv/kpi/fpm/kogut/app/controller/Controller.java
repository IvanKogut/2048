package ua.kyiv.kpi.fpm.kogut.app.controller;

import ua.kyiv.kpi.fpm.kogut.app.model.Direction;
import ua.kyiv.kpi.fpm.kogut.app.model.Model;
import ua.kyiv.kpi.fpm.kogut.app.model.Tile;
import ua.kyiv.kpi.fpm.kogut.app.view.View;

public class Controller implements EventListener {

    private final Model model;
    private final View view;

    private Controller() {
        this.model = new Model();
        this.view = new View(this);

        view.init();
        view.setEventListener(this);

        model.setEventListener(this);
    }

    public static void start() {
        new Controller();
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
    public void onMove(Direction direction) {
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
    public void onSave() {
        model.saveTiles();
        view.update();
    }

    @Override
    public void onLoad() {
        model.loadTiles();
        view.update();
    }

    @Override
    public void onRestart() {
        model.restart();
        view.update();
    }

    @Override
    public void onLose() {
        view.showMessageDialogOnLose();
    }

    @Override
    public void onWin() {
        view.showMessageDialogOnWin();
    }

    public String getMessage() {
        return model.getMessage();
    }
}