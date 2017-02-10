package ua.kyiv.kpi.fpm.kogut.app.view;

import ua.kyiv.kpi.fpm.kogut.app.controller.Controller;
import ua.kyiv.kpi.fpm.kogut.app.controller.EventListener;
import ua.kyiv.kpi.fpm.kogut.app.model.Tile;

import javax.swing.*;

/**
 * Created by Admin on 07.02.2017.
 */
public class View extends JFrame {

    private Controller controller;
    private Field field;

    public View(Controller controller) {
        this.controller = controller;
    }

    public void init() {
        field = new Field(this);
        add(field);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setTitle("2048");
        setVisible(true);
    }

    public Tile[][] getTiles() {
        return controller.getTiles();
    }

    public int getScore() {
        return controller.getScore();
    }

    public int getMaxTile() {
        return controller.getMaxTile();
    }

    public void setEventListener(EventListener eventListener) {
        field.setEventListener(eventListener);
    }

    public void update() {
        field.repaint();
    }
}
