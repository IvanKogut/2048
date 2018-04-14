package ua.kyiv.kpi.fpm.kogut.app.view;

import ua.kyiv.kpi.fpm.kogut.app.controller.Controller;
import ua.kyiv.kpi.fpm.kogut.app.controller.EventListener;
import ua.kyiv.kpi.fpm.kogut.app.model.Tile;

import javax.swing.*;
import java.awt.*;

public class View extends JFrame {

    private final Controller controller;
    private final Field field;

    private View(Controller controller, EventListener eventListener) {
        this.controller = controller;
        this.field = new Field(this, eventListener);

        add(new JLabel("S - save; L - load; R - restart"), BorderLayout.NORTH);
        add(field);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setSize(500, 500);
        setLocationRelativeTo(null);
        setTitle("2048");
        setVisible(true);
    }

    public static View init(Controller controller, EventListener eventListener) {
        return new View(controller, eventListener);
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

    public void update() {
        field.repaint();
    }

    public String getMessage() {
        return controller.getMessage();
    }

    public void showMessageDialogOnWin() {
        EventQueue.invokeLater(() -> {
            update();
            JOptionPane.showMessageDialog(
                    this,
                    "You are win!!!",
                    "Congratulation",
                    JOptionPane.INFORMATION_MESSAGE);
        });
    }

    public void showMessageDialogOnLose() {
        EventQueue.invokeLater(() -> {
            update();
            JOptionPane.showMessageDialog(
                    this,
                    "Try again...",
                    "Oops",
                    JOptionPane.INFORMATION_MESSAGE);
        });
    }
}