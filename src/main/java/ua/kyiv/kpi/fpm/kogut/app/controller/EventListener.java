package ua.kyiv.kpi.fpm.kogut.app.controller;

import ua.kyiv.kpi.fpm.kogut.app.model.Direction;

/**
 * Created by Admin on 09.02.2017.
 */
public interface EventListener {

    void move(Direction direction);
    void save();
    void load();
    void restart();
    void lose();
    void win();
}
