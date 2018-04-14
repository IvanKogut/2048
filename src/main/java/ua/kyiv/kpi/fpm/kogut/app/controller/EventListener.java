package ua.kyiv.kpi.fpm.kogut.app.controller;

import ua.kyiv.kpi.fpm.kogut.app.model.Direction;

public interface EventListener {

    void onMove(Direction direction);

    void onSave();

    void onLoad();

    void onRestart();

    void onLose();

    void onWin();
}
