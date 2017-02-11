package ua.kyiv.kpi.fpm.kogut.app.controller;

import ua.kyiv.kpi.fpm.kogut.app.model.Direction;

/**
 * Created by Admin on 09.02.2017.
 */
public interface EventListener {

    void onMove(Direction direction);
    void onSave();
    void onLoad();
    void onRestart();
    void onLose();
    void onWin();
}
