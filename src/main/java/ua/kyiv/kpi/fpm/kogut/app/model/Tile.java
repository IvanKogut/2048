package ua.kyiv.kpi.fpm.kogut.app.model;

import java.awt.*;
import java.io.Serializable;

public class Tile implements Drawable, Serializable {

    private static final long serialVersionUID = 2438845260542108250L;

    int value;

    public Tile() {
        this.value = 0;
    }

    public int getValue() {
        return value;
    }

    public boolean isEmpty() {
        return value == 0;
    }

    public Color getFontColor() {
        if (value < 16) {
            return new Color(0x776e65);
        } else {
            return new Color(0xf9f6f2);
        }
    }

    public Color getTileColor() {
        switch (value) {
            case 0:
                return new Color(0xcdc1b4);
            case 2:
                return new Color(0xeee4da);
            case 4:
                return new Color(0xede0c8);
            case 8:
                return new Color(0xf2b179);
            case 16:
                return new Color(0xf59563);
            case 32:
                return new Color(0xf67c5f);
            case 64:
                return new Color(0xf65e3b);
            case 128:
                return new Color(0xedcf72);
            case 256:
                return new Color(0xedcc61);
            case 512:
                return new Color(0xedc850);
            case 1024:
                return new Color(0xedc53f);
            case 2048:
                return new Color(0xedc22e);
            default:
                return new Color(0xff0000);
        }
    }

    @Override
    public void draw(Graphics g, int leftUpperCornerX, int leftUpperCornerY) {
        g.setColor(getTileColor());

        g.drawRect(leftUpperCornerX, leftUpperCornerY, Model.TILE_LENGTH, Model.TILE_LENGTH);
        g.fillRect(leftUpperCornerX, leftUpperCornerY, Model.TILE_LENGTH, Model.TILE_LENGTH);

        g.setColor(getFontColor());
        String value = isEmpty() ? "" : "" + getValue();
        g.drawString(value, leftUpperCornerX + Model.TILE_LENGTH / 2, leftUpperCornerY + Model.TILE_LENGTH / 2);
    }
}