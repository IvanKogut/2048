package ua.kyiv.kpi.fpm.kogut.app.controller;

import ua.kyiv.kpi.fpm.kogut.app.model.Direction;
import ua.kyiv.kpi.fpm.kogut.app.model.GameData;
import ua.kyiv.kpi.fpm.kogut.app.model.Model;
import ua.kyiv.kpi.fpm.kogut.app.view.View;

public class Controller implements EventListener {

    private final Model model;
    private final View view;

    private Controller() {
        this.model = Model.init(this);
        this.view = View.init(this, this);
    }

    public static void start() {
        new Controller();
    }

    public GameData getGameData() {
        return model.getGameData();
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
        view.update();
        view.showMessageDialogOnLose();
    }

    @Override
    public void onWin() {
        view.update();
        view.showMessageDialogOnWin();
    }
}