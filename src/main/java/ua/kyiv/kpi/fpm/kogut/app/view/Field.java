package ua.kyiv.kpi.fpm.kogut.app.view;

import ua.kyiv.kpi.fpm.kogut.app.controller.EventListener;
import ua.kyiv.kpi.fpm.kogut.app.model.Direction;
import ua.kyiv.kpi.fpm.kogut.app.model.Model;
import ua.kyiv.kpi.fpm.kogut.app.model.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class Field extends JPanel {

    private final View view;

    Field(View view, EventListener eventListener) {
        this.view = view;

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        eventListener.onMove(Direction.LEFT);
                        break;
                    case KeyEvent.VK_RIGHT:
                        eventListener.onMove(Direction.RIGHT);
                        break;
                    case KeyEvent.VK_UP:
                        eventListener.onMove(Direction.UP);
                        break;
                    case KeyEvent.VK_DOWN:
                        eventListener.onMove(Direction.DOWN);
                        break;
                    case KeyEvent.VK_S:
                        eventListener.onSave();
                        break;
                    case KeyEvent.VK_L:
                        eventListener.onLoad();
                        break;
                    case KeyEvent.VK_R:
                        eventListener.onRestart();
                        break;
                }
            }
        });
        setFocusable(true);
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, view.getWidth(), view.getHeight());

        final Tile[][] gameTiles = view.getTiles();

        int x = 0;
        int y = 0;

        for (Tile[] tiles : gameTiles) {
            for (Tile tile : tiles) {
                tile.draw(g, x, y);
                x += Model.TILE_LENGTH;
            }

            x = 0;
            y += Model.TILE_LENGTH;
        }

        x = 0;
        y += Model.TILE_LENGTH;

        g.setColor(Color.BLACK);
        g.drawString(String.format("Score: %d, High: %d", view.getScore(), view.getMaxTile()), x, y);
        g.drawString(view.getMessage(), x, y + Model.TILE_LENGTH / 2);
    }
}