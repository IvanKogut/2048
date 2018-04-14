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

        showMainGameFrame();
    }

    public static View init(Controller controller, EventListener eventListener) {
        return new View(controller, eventListener);
    }

    private void showMainGameFrame() {
        add(new JLabel("S - save; L - load; R - restart"), BorderLayout.NORTH);
        add(field);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setSize(500, 500);
        setLocationRelativeTo(null);
        setTitle("2048");
        setVisible(true);
    }

    public void update() {
        field.repaint();
    }

    public void showMessageDialogOnLose() {
        putMessageToEventQueue("Try again...", "Oops");
    }

    public void showMessageDialogOnWin() {
        putMessageToEventQueue("You are win!!!", "Congratulation");
    }

    private void putMessageToEventQueue(final String message, final String title) {
        EventQueue.invokeLater(() -> {
            update();
            JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
        });
    }

    Tile[][] getTiles() {
        return controller.getTiles();
    }

    int getScore() {
        return controller.getScore();
    }

    int getMaxTile() {
        return controller.getMaxTile();
    }

    String getMessage() {
        return controller.getMessage();
    }
}