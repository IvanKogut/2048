package ua.kyiv.kpi.fpm.kogut.app.view;

import ua.kyiv.kpi.fpm.kogut.app.model.Direction;
import ua.kyiv.kpi.fpm.kogut.app.controller.EventListener;
import ua.kyiv.kpi.fpm.kogut.app.model.Model;
import ua.kyiv.kpi.fpm.kogut.app.model.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by Admin on 09.02.2017.
 */
public class Field extends JPanel {

    public class KeyHandler extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    eventListener.move(Direction.LEFT);
                    break;
                case KeyEvent.VK_RIGHT:
                    eventListener.move(Direction.RIGHT);
                    break;
                case KeyEvent.VK_UP:
                    eventListener.move(Direction.UP);
                    break;
                case KeyEvent.VK_DOWN:
                    eventListener.move(Direction.DOWN);
                    break;
                case KeyEvent.VK_S:
                    eventListener.save();
                    break;
                case KeyEvent.VK_L:
                    eventListener.load();
                    break;
                case KeyEvent.VK_R:
                    eventListener.restart();
                    break;
            }
        }
    }

    private View view;
    private EventListener eventListener;

    public Field(View view) {
        this.view = view;
        addKeyListener(new KeyHandler());
        this.setFocusable(true);
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, view.getWidth(), view.getHeight());


        Tile[][] gameTiles = view.getTiles();

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

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }
}